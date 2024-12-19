package com.linkedin.dataguard.runtime.udf.spark

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction
import com.linkedin.dataguard.runtime.spark.tms.handler.factory.SparkHandlerFactory
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper.{createStdData, createStdType}
import com.linkedin.dataguard.runtime.udf.spark.RedactFieldIfUDF.{DELIMITER_PATTERN, LOG}
import com.linkedin.transport.api.data.PlatformData
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.types.DataType
import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern


case class RedactFieldIfUDF(expressions: Seq[Expression])
  extends Expression with CodegenFallback {

  require(expressions.length == 4, "Exactly 4 expressions are required")

  private val ENFORCERS = new ConcurrentHashMap[String, TMSEnforcer]
  @transient lazy private val TYPE_DATA_PROVIDER = new NullableSparkTypeDataProvider
  @transient lazy private val HANDLER_FACTORY = new SparkHandlerFactory

  private val conditionExpr: Expression = expressions.head

  private val inputExpr: Expression = expressions(1)
  private val rootColumnType: DataType  = inputExpr.dataType

  private val pathExpr: Expression = expressions(2)

  private val replacementValueExpr: Expression = expressions(3)

  private val replacementValueType: DataType = {
    if (replacementValueExpr == null)
      null
    else replacementValueExpr.dataType
  }

  private val CHILDREN = conditionExpr :: inputExpr :: pathExpr :: replacementValueExpr :: Nil

  override def nullable: Boolean = inputExpr.nullable

  def getOrCreateEnforcer(tmsPath: String, eraseAction: EraseAction): TMSEnforcer = {
    ENFORCERS.computeIfAbsent(
      // The cache is not shared across UDF instances, rather across rows for the same UDF invocation. Every UDF
      // invocation in the query results in a new instance of the UDF. So we can use the tmsPath as the cache key
      // and not worry about the exact same path for redaction on a datasets reusing the TMSEnforcer for a
      // different dataset.
      tmsPath,
      _ => {
        val stdType = createStdType(rootColumnType)
        new TMSEnforcer(tmsPath, stdType, TYPE_DATA_PROVIDER, eraseAction, HANDLER_FACTORY)
      }
    )
  }

  override def eval(inputRow: InternalRow): Any = {
    val condition = conditionExpr.eval(inputRow).asInstanceOf[Boolean]
    val inputObject = inputExpr.eval(inputRow)
    // early termination if the data object itself is null or the condition is false
    if (shouldReturnEarly(inputObject, condition)) {
      return inputObject
    }

    val groupedTmsPaths = pathExpr.eval(inputRow)
    if (groupedTmsPaths == null) {
      throw new IllegalArgumentException("groupedTmsPaths cannot be null")
    }
    val replacementValue = replacementValueExpr.eval(inputRow)
    var dataObject = createStdData(inputObject, rootColumnType)
    val replacementValueStdData = createStdData(replacementValue, replacementValueType)
    val tmsPaths: Array[String] = DELIMITER_PATTERN.split(groupedTmsPaths.toString)
    val eraseAction = new EraseAction(replacementValueStdData)
    for (tmsPath <- tmsPaths) {
      val enforcer = getOrCreateEnforcer(tmsPath, eraseAction)
      dataObject = enforcer.redact(dataObject, 0)
    }
    if (dataObject == null) {
      return null
    }
    dataObject.asInstanceOf[PlatformData].getUnderlyingData
  }

  def shouldReturnEarly(inputObject: Any, conditionObject: Any): Boolean = {
    if (inputObject == null) {
      if (LOG.isDebugEnabled) {
        LOG.debug("input object is null. Returning input object without any modification")
      }
      return true
    }
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

  override def children: Seq[Expression] = CHILDREN
}

object RedactFieldIfUDF {
  val UDF_BATCHING_DELIM = "###"
  val LOG = LoggerFactory.getLogger(classOf[RedactFieldIfUDF])
  val DELIMITER_PATTERN: Pattern = Pattern.compile(UDF_BATCHING_DELIM)
}