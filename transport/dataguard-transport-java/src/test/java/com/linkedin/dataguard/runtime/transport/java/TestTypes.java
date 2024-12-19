package com.linkedin.dataguard.runtime.transport.java;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.linkedin.dataguard.runtime.transport.java.data.JavaInteger;
import com.linkedin.dataguard.runtime.transport.java.types.JavaMapType;
import com.linkedin.dataguard.runtime.transport.java.data.JavaArray;
import com.linkedin.dataguard.runtime.transport.java.data.JavaLong;
import com.linkedin.dataguard.runtime.transport.java.data.JavaMap;
import com.linkedin.dataguard.runtime.transport.java.data.JavaString;
import com.linkedin.dataguard.runtime.transport.java.data.JavaStruct;
import com.linkedin.dataguard.runtime.transport.java.types.JavaIntegerType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaStringType;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdIntegerType;
import com.linkedin.transport.api.types.StdStringType;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.transport.java.data.JavaStruct.*;
import static org.junit.jupiter.api.Assertions.*;


public class TestTypes {

  private final JavaFactory factory = new JavaFactory();

  @Test
  public void testRowType() {
    StdType rowType = factory.createStdType("row(a varchar, b integer)");
    assertTrue(rowType instanceof StdStructType);
    List<? extends StdType> types = ((StdStructType) rowType).fieldTypes();
    List<String> names = ((StdStructType) rowType).fieldNames();
    assertEquals(types.size(), 2);
    assertTrue(types.get(0) instanceof StdStringType);
    assertTrue(types.get(1) instanceof StdIntegerType);
    assertEquals(names, ImmutableList.of("a", "b"));
  }

  @Test
  public void testArrayType() {
    StdType arrayType = factory.createStdType("array(integer)");
    assertTrue(arrayType instanceof StdArrayType);
    assertTrue(((StdArrayType) arrayType).elementType() instanceof StdIntegerType);
  }

  @Test
  public void testMapType() {
    StdType mapType = factory.createStdType("map(varchar, integer)");
    assertTrue(mapType instanceof JavaMapType);
    assertTrue(((JavaMapType) mapType).keyType() instanceof StdStringType);
    assertTrue(((JavaMapType) mapType).valueType() instanceof StdIntegerType);
  }

  @Test
  public void testData() {
    assertEquals(factory.createDouble(5.6d).get(), 5.6d);
    assertEquals(factory.createInteger(5).get(), 5);
    ByteBuffer b = ByteBuffer.allocate(1).put(0, (byte) 12);
    assertEquals(factory.createBinary(b).get(), b);
    assertEquals(factory.createBoolean(true).get(), true);
    assertEquals(factory.createLong(18L).get(), 18L);
    assertEquals(factory.createFloat(18.3f).get(), 18.3f);
    assertEquals(factory.createFloat(18.3f).get(), 18.3f);

    JavaArray array = (JavaArray) factory.createArray(factory.createStdType("array(integer)"));
    array.setUnderlyingData(ImmutableList.of(10, 11, 5, 9));
    Assertions.assertEquals(((JavaInteger) array.get(3)).getUnderlyingData(),9);
    assertEquals(array.getUnderlyingData(), ImmutableList.of(10, 11, 5, 9));

    JavaMap map = (JavaMap) factory.createMap(null);
    map.setUnderlyingData(ImmutableMap.of("k1", 5, "k2", 6L, "k3", "val"));
    assertEquals(
        map.keySet().stream()
            .map(JavaString.class::cast)
            .map(JavaString::get)
            .collect(Collectors.toSet()),
        ImmutableSet.of("k1", "k2", "k3"));
    assertEquals(
        ((JavaLong) (map.get(JavaUtil.createStdData("k2", new JavaFactory())))).get(),
        6L);

    JavaStruct struct = (JavaStruct) factory.createStruct(
        ImmutableList.of("a", "b"),
        ImmutableList.of(new JavaStringType(), new JavaIntegerType()));
    struct.setUnderlyingData(structInfo(ImmutableList.of("a", "b"), "strValue", 44));
    assertEquals(((JavaString) struct.getField(0)).get(), "strValue");
    assertEquals(((JavaInteger) struct.getField(1)).get(), 44);
  }
}