package com.linkedin.dataguard.runtime.transport.spark

import com.linkedin.dataguard.runtime.transport.spark.data.{NullableSparkArray, NullableSparkMap, NullableSparkStruct}
import com.linkedin.transport.api.data._
import com.linkedin.transport.api.types.StdType
import com.linkedin.transport.spark.SparkFactory
import com.linkedin.transport.typesystem.AbstractBoundVariables
import org.apache.spark.sql.types._

import java.util.{List => JavaList}

/**
 * Extended from {@link com.linkedin.transport.spark.SparkFactory}. A SparkFactory implementation that supports
 * null values. The differences are that this implementation uses {@link NullableSparkStruct}, {@link NullableSparkArray},
 * and {@link NullableSparkMap}.
 */
class NullableSparkFactory(private val _boundVariables: AbstractBoundVariables[DataType]) extends SparkFactory(_boundVariables) {

  // we do not pass size to `new Array()` as the size argument of createArray is supposed to be just a hint about
  // the expected number of entries in the StdArray. `new Array(size)` will create an array with null entries
  override def createArray(stdType: StdType, size: Int): StdArray = NullableSparkArray(
    null, stdType.underlyingType().asInstanceOf[ArrayType]
  )

  override def createMap(stdType: StdType): StdMap = NullableSparkMap(
    //TODO: make these as separate mutable standard spark types
    null, stdType.underlyingType().asInstanceOf[MapType]
  )

  override def createStruct(fieldNames: JavaList[String], fieldTypes: JavaList[StdType]): StdStruct = {
    val structFields = new Array[StructField](fieldTypes.size())
    (0 until fieldTypes.size()).foreach({
      idx => {
        structFields(idx) = StructField(
          if (fieldNames == null) "field" + idx else fieldNames.get(idx),
          fieldTypes.get(idx).underlyingType().asInstanceOf[DataType]
        )
      }
    })
    NullableSparkStruct(null, StructType(structFields))
  }

  override def createStruct(stdType: StdType): StdStruct = {
    //TODO: make these as separate mutable standard spark types
    val structType = stdType.underlyingType().asInstanceOf[StructType]
    NullableSparkStruct(null, structType)
  }
}
