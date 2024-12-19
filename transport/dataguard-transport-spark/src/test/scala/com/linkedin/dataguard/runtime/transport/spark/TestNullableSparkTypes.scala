package com.linkedin.dataguard.runtime.transport.spark

import com.linkedin.dataguard.runtime.transport.spark.data.NullableSparkStruct
import com.linkedin.transport.spark.data.{SparkInteger, SparkString}
import com.linkedin.transport.spark.typesystem.SparkBoundVariables
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

class TestNullableSparkTypes {

  private val sparkFactory: NullableSparkFactory = new NullableSparkFactory(new SparkBoundVariables())

  @Test
  def testNullableSparkArray(): Unit = {
    val array = sparkFactory.createArray(sparkFactory.createStdType("array(varchar)"))
    val data = sparkFactory.createString("v0")
    array.add(data)
    array.add(null)
    assertEquals(2, array.size())
    assertEquals("v0", array.get(0).asInstanceOf[SparkString].get())
    assertNull(array.get(1))
  }

  @Test
  def testNullableSparkArrayComplex(): Unit = {
    val array = sparkFactory.createArray(sparkFactory.createStdType("array(row(attributeName varchar, value double))"))
    val struct = sparkFactory.createStruct(sparkFactory.createStdType("row(attributeName varchar, value double)"))
    val x = sparkFactory.createString("nameValue")
    val y = sparkFactory.createDouble(123.0)
    struct.setField("attributeName", x)
    struct.setField("value", y)
    array.add(struct)
    array.add(null)
    assertEquals(2, array.size())
    assertEquals("nameValue", array.get(0).asInstanceOf[NullableSparkStruct].getField("attributeName").asInstanceOf[SparkString].get())
    assertNull(array.get(1))
  }

  @Test
  def testNullableSparkStruct(): Unit = {
    val struct = sparkFactory.createStruct(sparkFactory.createStdType("row(x integer, y varchar, z double)"))
    val x = sparkFactory.createInteger(123)
    val y = sparkFactory.createString("yValue")
    struct.setField(0, x)
    struct.setField("y", y)
    struct.setField("z", null)
    assertEquals(123, struct.getField("x").asInstanceOf[SparkInteger].get())
    assertEquals("yValue", struct.getField("y").asInstanceOf[SparkString].get())
    assertNull(struct.getField("z"))
  }

  @Test
  def testNullableSparkMap(): Unit = {
    val map = sparkFactory.createMap(sparkFactory.createStdType("map(varchar, integer)"))
    val value1 = sparkFactory.createInteger(123)
    val value3 = sparkFactory.createInteger(456)
    val key1 = sparkFactory.createString("key1")
    val key2 = sparkFactory.createString("key2")
    map.put(key1, value1)
    map.put(key2, null)
    map.put(null, value3)
    assertEquals(123, map.get(key1).asInstanceOf[SparkInteger].get())
    assertEquals(456, map.get(null).asInstanceOf[SparkInteger].get())
    assertNull(map.get(key2))
  }
}

