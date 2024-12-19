package com.linkedin.dataguard.runtime.udf.spark

import com.linkedin.dataguard.runtime.fieldpaths.virtual.DataGuardPathOperations
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Enforcer
import com.linkedin.dataguard.runtime.spark.SparkEnforcerWrapper
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper.createStdType
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.codegen.{CodegenContext, CodegenFallback, ExprCode}
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.types._
import org.apache.spark.unsafe.types.UTF8String
import org.slf4j.LoggerFactory

import java.lang.String.format
import java.util
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * RedactSecondarySchemaFieldIf is a UDF that transforms a column value by replacing some parts of it, if a condition is met.
 * The information on what needs modification is provided by one of the input parameters that represent a path in dataguard grammar format
 * The args are expected in the format [condition, columnObject, path, columnName, replacementType],
 * where condition is a boolean value,
 * columnObject is the object to be modified,
 * path is the location in the input object to be removed,
 * columnName is the name of the column represented by columnObject,
 * and replacementType is the type of the replacement value.
 * If condition is true, it removes the element in input at the path, else returns the unmodified input
 */
case class RedactSecondarySchemaFieldIfUDF(expressions: Seq[Expression]) extends Expression with CodegenFallback {

  require(expressions.length == 5, "Exactly 5 expressions are required")
  private val LOG = LoggerFactory.getLogger(classOf[RedactFieldIfUDF])
  private val ENFORCERS = new ConcurrentHashMap[String, Enforcer]
  @transient lazy private val SPARK_TYPE_DATA_PROVIDER = new NullableSparkTypeDataProvider();
  @transient lazy private val dataguard_PATH_OPERATIONS = new DataGuardPathOperations(SPARK_TYPE_DATA_PROVIDER);

  def conditionExpr: Expression = expressions.head
  def inputExpr: Expression = expressions(1)
  def pathExpr: Expression = expressions(2)
  def columnNameExpr: Expression = expressions(3)
  def replacementTypeExpr: Expression = expressions(4)
  override def nullable: Boolean = inputExpr.nullable

  override def eval(inputRow: InternalRow): Any = {
    val inputObject = inputExpr.eval(inputRow)
    val condition = conditionExpr.eval(inputRow).asInstanceOf[Boolean]
    if (shouldReturnEarly(condition)) {
      return inputObject
    }
    val replacementType = replacementTypeExpr.eval(inputRow).toString
    val replacementWrapper = ReplacementWrapper.getReplacementWrapper(replacementType)
    val replacementValue = replacementWrapper.getReplacementValue
    val replacementValueType = replacementWrapper.getReplacementType
    var result: Any = inputObject
    val rootColumnType = inputExpr.dataType
    val columnName = columnNameExpr.eval(inputRow).toString
    val paths:Seq[UTF8String] = pathExpr.eval(inputRow).asInstanceOf[ArrayData].toSeq(StringType)
    // apply REDACT_SECONDARY_SCHEMA_FIELD_IF on the input object at path
    for (path <- paths) {
      val enforcer: Enforcer = getOrCreateEnforcer(columnName, path.toString, rootColumnType)
      result = SparkEnforcerWrapper.transform(rootColumnType, result, replacementValue, replacementValueType, enforcer)
    }
    result
  }

  // Retrieves the enforcer from cache key-ed by path
  // Instantiate a new enforcer instance if the enforcer is not present
  def getOrCreateEnforcer(rootColumnName: String, path: String, rootColumnType: DataType): Enforcer = {
    // The cache is not shared across UDF instances, rather across rows for the same UDF invocation. Every UDF
    // invocation in the query results in a new instance of the UDF. So we can use the tmsPath as the cache key
    // and not worry about the exact same path for redaction on a datasets reusing the TMSEnforcer for a
    // different dataset.
    val effectivePath: String = path.replace(format("$.%s", rootColumnName), "$")
    ENFORCERS.computeIfAbsent(path, (ignored: String) => dataguard_PATH_OPERATIONS.createEnforcer(Optional.empty[String](), effectivePath, createStdType(rootColumnType)))
  }

  def shouldReturnEarly(conditionObject: Any): Boolean = {
    if (conditionObject == null) {
      // Early return without doing anything if the condition object itself is null
      if (LOG.isDebugEnabled) {
        LOG.debug("condition object is null. Returning input object without any modification")
      }
      return true
    }
    val condition = conditionObject.asInstanceOf[Boolean]
    !condition
  }

  override def dataType: DataType = inputExpr.dataType

  override def children: Seq[Expression] = conditionExpr :: inputExpr :: pathExpr :: columnNameExpr :: replacementTypeExpr :: Nil

  object ReplacementWrapper {
    private val DEFAULT_REPLACEMENT_WRAPPER = new ReplacementWrapper(null, NullType)
    private val DEFAULT_UTF8_STRING = UTF8String.fromString("")
    /**
     * Each data type has a corresponding replacement value and a corresponding data type
     * For any data type not in the list, its corresponding replacement value will be null
     * and its corresponding data type will be null type.
     * This is okay only because for non-primitive, only null values are allowed, and we will not use the data type.
     */
    private val DATA_TYPE_TO_REPLACEMENT_WRAPPER = new util.HashMap[String, ReplacementWrapper]

    def getReplacementWrapper(replacementType: String): ReplacementWrapper = DATA_TYPE_TO_REPLACEMENT_WRAPPER.getOrDefault(replacementType, DEFAULT_REPLACEMENT_WRAPPER)

    DATA_TYPE_TO_REPLACEMENT_WRAPPER.put("FLOAT", new ReplacementWrapper(0.0f, FloatType))
    DATA_TYPE_TO_REPLACEMENT_WRAPPER.put("DOUBLE", new ReplacementWrapper(0.0d, DoubleType))
    DATA_TYPE_TO_REPLACEMENT_WRAPPER.put("INT", new ReplacementWrapper(0, IntegerType))
    DATA_TYPE_TO_REPLACEMENT_WRAPPER.put("LONG", new ReplacementWrapper(0L, LongType))
    DATA_TYPE_TO_REPLACEMENT_WRAPPER.put("BOOLEAN", new ReplacementWrapper(false, BooleanType))
    DATA_TYPE_TO_REPLACEMENT_WRAPPER.put("STRING", new ReplacementWrapper(DEFAULT_UTF8_STRING, StringType))

  }

  final class ReplacementWrapper(val replacementValue: Any, val replacementType: DataType) {
    def getReplacementValue: Any = replacementValue
    def getReplacementType: DataType = replacementType
  }
}