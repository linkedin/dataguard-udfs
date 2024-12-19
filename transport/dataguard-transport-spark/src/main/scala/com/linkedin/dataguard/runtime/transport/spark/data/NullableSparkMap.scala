package com.linkedin.dataguard.runtime.transport.spark.data

import com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper
import com.linkedin.transport.api.data.{PlatformData, StdData, StdMap}
import org.apache.spark.sql.catalyst.util.{ArrayBasedMapData, MapData}
import org.apache.spark.sql.types.MapType

import java.util
import scala.collection.mutable


/**
 * Ported and modified from {@link com.linkedin.transport.spark.data.SparkMap}. A SparkMap implementation that supports
 * fields with null values. The only difference from SparkMap is that the "put" method allows putting a value that is null.
 * This is needed to allow setting null values for elements within this complex type which Transport's implementation
 * does not support.
 */
case class NullableSparkMap(private var _mapData: MapData,
                    private val _mapType: MapType) extends StdMap with PlatformData {

  private val _keyType = _mapType.keyType
  private val _valueType = _mapType.valueType
  private var _mutableMap: mutable.Map[Any, Any] = {
    if (_mapData == null) {
      createMutableMap()
    } else {
      null
    }
  }

  override def put(key: StdData, value: StdData): Unit = {
    if (_mutableMap == null) {
      _mutableMap = createMutableMap()
    }
    val newKey = Option(key).map(_.asInstanceOf[PlatformData].getUnderlyingData).orNull
    val newValue = Option(value).map(_.asInstanceOf[PlatformData].getUnderlyingData).orNull
    _mutableMap.put(newKey, newValue)
  }

  override def keySet(): util.Set[StdData] = {
    val keysIterator: Iterator[Any] = if (_mutableMap == null) {
      new Iterator[Any] {
        var offset : Int = 0

        override def next(): Any = {
          offset += 1
          _mapData.keyArray().get(offset - 1, _keyType)
        }

        override def hasNext: Boolean = offset < NullableSparkMap.this.size()
      }
    } else {
      _mutableMap.keysIterator
    }

    new util.AbstractSet[StdData] {

      override def iterator(): util.Iterator[StdData] = new util.Iterator[StdData] {

        override def next(): StdData = NullableSparkWrapper.createStdData(keysIterator.next(), _keyType)

        override def hasNext: Boolean = keysIterator.hasNext
      }

      override def size(): Int = NullableSparkMap.this.size()
    }
  }

  override def size(): Int = {
    if (_mutableMap == null) {
      _mapData.numElements()
    } else {
      _mutableMap.size
    }
  }

  override def values(): util.Collection[StdData] = {
    val valueIterator: Iterator[Any] = if (_mutableMap == null) {
      new Iterator[Any] {
        var offset : Int = 0

        override def next(): Any = {
          offset += 1
          _mapData.valueArray().get(offset - 1, _valueType)
        }

        override def hasNext: Boolean = offset < NullableSparkMap.this.size()
      }
    } else {
      _mutableMap.valuesIterator
    }

    new util.AbstractCollection[StdData] {

      override def iterator(): util.Iterator[StdData] = new util.Iterator[StdData] {

        override def next(): StdData = NullableSparkWrapper.createStdData(valueIterator.next(), _valueType)

        override def hasNext: Boolean = valueIterator.hasNext
      }

      override def size(): Int = NullableSparkMap.this.size()
    }
  }

  override def containsKey(key: StdData): Boolean = get(key) != null

  override def get(key: StdData): StdData = {
    // Spark's complex data types (MapData, ArrayData, InternalRow) do not implement equals/hashcode
    // If the key is of the above complex data types, get() will return null
    if (_mutableMap == null) {
      _mutableMap = createMutableMap()
    }
    val newKey = Option(key).map(_.asInstanceOf[PlatformData].getUnderlyingData).orNull
    NullableSparkWrapper.createStdData(_mutableMap.get(newKey).orNull, _valueType)
  }

  private def createMutableMap(): mutable.Map[Any, Any] = {
    val mutableMap = mutable.Map.empty[Any, Any]
    if (_mapData != null) {
      _mapData.foreach(_keyType, _valueType, (k, v) => mutableMap.put(k, v))
    }
    mutableMap
  }

  override def getUnderlyingData: AnyRef = {
    if (_mutableMap == null) {
      _mapData
    } else {
      ArrayBasedMapData(_mutableMap)
    }
  }

  override def setUnderlyingData(value: scala.Any): Unit = {
    _mapData = value.asInstanceOf[MapData]
    _mutableMap = null
  }
}
