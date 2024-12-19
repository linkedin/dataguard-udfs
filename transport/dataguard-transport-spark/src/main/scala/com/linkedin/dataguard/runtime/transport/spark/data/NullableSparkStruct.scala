package com.linkedin.dataguard.runtime.transport.spark.data

import com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper
import com.linkedin.transport.api.data.{PlatformData, StdData, StdStruct}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.types.StructType

import java.util.{List => JavaList}
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer


/**
 * Ported and modified from {@link com.linkedin.transport.spark.data.SparkStruct}. A HiveStruct implementation that
 * supports fields with null values. The only difference from SparkStruct is that the two setField methods allow setting
 * null values. This is needed to allow setting null values for elements within this complex type which Transport's
 * implementation does not support.
 */
case class NullableSparkStruct(private var _row: InternalRow,
                       private val _structType: StructType) extends StdStruct with PlatformData {

  private var _mutableBuffer: ArrayBuffer[Any] = {
    if (_row == null) createMutableStruct() else null
  }

  override def getField(name: String): StdData = getField(_structType.fieldIndex(name))

  override def getField(index: Int): StdData = {
    val fieldDataType = _structType(index).dataType
    if (_mutableBuffer == null) NullableSparkWrapper.createStdData(_row.get(index, fieldDataType), fieldDataType) else NullableSparkWrapper.createStdData(_mutableBuffer(index), fieldDataType)
  }

  override def setField(name: String, value: StdData): Unit = {
    setField(_structType.fieldIndex(name), value)
  }

  override def setField(index: Int, value: StdData): Unit = {
    if (_mutableBuffer == null) _mutableBuffer = createMutableStruct()
    if (value == null) _mutableBuffer(index) = null else _mutableBuffer(index) = value.asInstanceOf[PlatformData].getUnderlyingData
  }

  private def createMutableStruct() = {
    if (_row != null) ArrayBuffer[Any](_row.toSeq(_structType): _*) else ArrayBuffer.fill[Any](_structType.length) {null}
  }

  override def fields(): JavaList[StdData] = _structType.indices.map(getField).asJava

  override def getUnderlyingData: AnyRef = {
    if (_mutableBuffer == null) _row else InternalRow.fromSeq(_mutableBuffer)
  }

  override def setUnderlyingData(value: scala.Any): Unit = {
    _row = value.asInstanceOf[InternalRow]
    _mutableBuffer = null
  }
}
