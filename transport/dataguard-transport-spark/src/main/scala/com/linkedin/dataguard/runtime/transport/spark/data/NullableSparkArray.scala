package com.linkedin.dataguard.runtime.transport.spark.data

import com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper
import com.linkedin.transport.api.data.{PlatformData, StdArray, StdData}
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.types.{ArrayType, DataType}

import scala.collection.mutable.ArrayBuffer
import java.util


/**
 * Ported and modified from {@link com.linkedin.transport.spark.data.SparkArray}. A SparkArray implementation that supports
 * fields with null values. The only difference from SparkArray is that the "add" method allows adding elements with null
 * values. This is needed to allow setting null values for elements within this complex type which Transport's
 * implementation does not support.
 */
case class NullableSparkArray(private var _arrayData: ArrayData,
                      private val _arrayType: DataType) extends StdArray with PlatformData {

  private val _elementType = _arrayType.asInstanceOf[ArrayType].elementType
  private var _mutableBuffer = {
    if (_arrayData == null) {
      createMutableArray()
    } else {
      null
    }
  }

  override def add(e: StdData): Unit = {
    // Once add is called, we cannot use  Spark's readonly ArrayData API
    // we have to add elements to a mutable buffer and start using that
    // always instead of the readonly stdType
    if (_mutableBuffer == null) {
      // from now on mutable is in affect
      _mutableBuffer = createMutableArray()
    }
    if (e == null) {
      _mutableBuffer.append(null)
    } else {
      _mutableBuffer.append(e.asInstanceOf[PlatformData].getUnderlyingData)
    }
  }

  private def createMutableArray(): ArrayBuffer[Any] = {
    var arrayBuffer: ArrayBuffer[Any] = null
    if (_arrayData == null) {
      arrayBuffer = new ArrayBuffer[Any]()
    } else {
      arrayBuffer = new ArrayBuffer[Any](_arrayData.numElements())
      _arrayData.foreach(_elementType, (i, e) => arrayBuffer.append(e))
    }
    arrayBuffer
  }

  override def getUnderlyingData: AnyRef = {
    if (_mutableBuffer == null) {
      _arrayData
    } else {
      ArrayData.toArrayData(_mutableBuffer)
    }
  }

  override def setUnderlyingData(value: scala.Any): Unit = {
    _arrayData = value.asInstanceOf[ArrayData]
    _mutableBuffer = null
  }

  override def iterator(): util.Iterator[StdData] = new util.Iterator[StdData] {
    private var idx = 0

    override def next(): StdData = {
      val e = get(idx)
      idx += 1
      e
    }

    override def hasNext: Boolean = idx < size()
  }

  override def size(): Int = {
    if (_mutableBuffer != null) {
      _mutableBuffer.size
    } else {
      _arrayData.numElements()
    }
  }

  override def get(idx: Int): StdData = {
    if (_mutableBuffer == null) {
      NullableSparkWrapper.createStdData(_arrayData.get(idx, _elementType), _elementType)
    } else {
      NullableSparkWrapper.createStdData(_mutableBuffer(idx), _elementType)
    }
  }
}
