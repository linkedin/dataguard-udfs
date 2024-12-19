package com.linkedin.dataguard.runtime.transport.spark

import com.linkedin.dataguard.runtime.transport.spark.data.{NullableSparkArray, NullableSparkMap, NullableSparkStruct}
import com.linkedin.transport.api.data.StdData
import com.linkedin.transport.api.types.StdType
import com.linkedin.transport.spark.data._
import com.linkedin.transport.spark.types._
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.util.{ArrayData, MapData}
import org.apache.spark.sql.types._
import org.apache.spark.unsafe.types.UTF8String

/**
 * Ported and modified from {@link com.linkedin.transport.spark.SparkWrapper}. A SparkWrapper implementation that supports
 * null values for Array, Map, and Struct fields. The only difference from SparkWrapper is that it uses {@link NullableSparkStruct},
 * {@link NullableSparkArray} and {@link NullableSparkMap}, instead of {@link com.linkedin.transport.spark.data.SparkStruct},
 * {@link com.linkedin.transport.spark.data.SparkArray} and {@link com.linkedin.transport.spark.data.SparkMap} respectively,
 * for representing complex data types.
 *
 * This is needed to allow setting null values for elements within this complex type which Transport's implementation
 * does not support.
 */
object NullableSparkWrapper {

  def createStdData(data: Any, dataType: DataType): StdData = { // scalastyle:ignore cyclomatic.complexity
    if (data == null) null else dataType match {
      case _: IntegerType => SparkInteger(data.asInstanceOf[Integer])
      case _: LongType => SparkLong(data.asInstanceOf[java.lang.Long])
      case _: BooleanType => SparkBoolean(data.asInstanceOf[java.lang.Boolean])
      case _: StringType => SparkString(data.asInstanceOf[UTF8String])
      case _: FloatType => SparkFloat(data.asInstanceOf[java.lang.Float])
      case _: DoubleType => SparkDouble(data.asInstanceOf[java.lang.Double])
      case _: BinaryType => SparkBinary(data.asInstanceOf[Array[Byte]])
      case _: ArrayType => NullableSparkArray(data.asInstanceOf[ArrayData], dataType.asInstanceOf[ArrayType])
      case _: MapType => NullableSparkMap(data.asInstanceOf[MapData], dataType.asInstanceOf[MapType])
      case _: StructType => NullableSparkStruct(data.asInstanceOf[InternalRow], dataType.asInstanceOf[StructType])
      case _: NullType => null
      case _ => throw new UnsupportedOperationException("Unrecognized Spark Type: " + dataType.getClass)
    }
  }

  def createStdType(dataType: DataType): StdType = dataType match {
    case _: IntegerType => SparkIntegerType(dataType.asInstanceOf[IntegerType])
    case _: LongType => SparkLongType(dataType.asInstanceOf[LongType])
    case _: BooleanType => SparkBooleanType(dataType.asInstanceOf[BooleanType])
    case _: StringType => SparkStringType(dataType.asInstanceOf[StringType])
    case _: FloatType => SparkFloatType(dataType.asInstanceOf[FloatType])
    case _: DoubleType => SparkDoubleType(dataType.asInstanceOf[DoubleType])
    case _: BinaryType => SparkBinaryType(dataType.asInstanceOf[BinaryType])
    case _: ArrayType => SparkArrayType(dataType.asInstanceOf[ArrayType])
    case _: MapType => SparkMapType(dataType.asInstanceOf[MapType])
    case _: StructType => SparkStructType(dataType.asInstanceOf[StructType])
    case _: NullType => SparkUnknownType(dataType.asInstanceOf[NullType])
    case _ => throw new UnsupportedOperationException("Unrecognized Spark Type: " + dataType.getClass)
  }
}
