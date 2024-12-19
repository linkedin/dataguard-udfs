package com.linkedin.dataguard.runtime.trino;

import com.linkedin.dataguard.runtime.transport.trino.types.TrinoTypeDataProvider;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdBinary;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdDouble;
import com.linkedin.transport.api.data.StdFloat;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.data.StdLong;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdString;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.trino.data.TrinoBinary;
import com.linkedin.transport.trino.data.TrinoBoolean;
import com.linkedin.transport.trino.data.TrinoDouble;
import com.linkedin.transport.trino.data.TrinoFloat;
import com.linkedin.transport.trino.data.TrinoInteger;
import com.linkedin.transport.trino.data.TrinoLong;
import com.linkedin.transport.trino.data.TrinoString;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import java.nio.ByteBuffer;
import java.util.stream.IntStream;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;


public class TestNullableTrinoWrapper {

  @Test
  public void testCreateStdData() {
    TrinoTypeDataProvider typeDataProvider = new TrinoTypeDataProvider();
    StdFactory trinoFactory = typeDataProvider.getStdFactory();

    StdInteger intData = trinoFactory.createInteger(1);
    assertThat(intData).isInstanceOf(TrinoInteger.class);
    assertThat(intData.get()).isEqualTo(1);

    StdBoolean booleanData = trinoFactory.createBoolean(true);
    assertThat(booleanData).isInstanceOf(TrinoBoolean.class);
    assertThat(booleanData.get()).isEqualTo(true);

    StdLong longData = trinoFactory.createLong(1L);
    assertThat(longData).isInstanceOf(TrinoLong.class);
    assertThat(longData.get()).isEqualTo(1L);

    byte[] bytes = {88, 89, 90};
    Slice slice = Slices.allocate(bytes.length);
    slice.setBytes(0, bytes);
    StdString stringData = trinoFactory.createString("XYZ");
    assertThat(stringData).isInstanceOf(TrinoString.class);
    assertThat(stringData.get()).isEqualTo("XYZ");

    StdFloat floatData = trinoFactory.createFloat(1.23f);
    assertThat(floatData).isInstanceOf(TrinoFloat.class);
    assertThat(floatData.get()).isEqualTo(1.23f);

    StdDouble doubleData = trinoFactory.createDouble(1.23d);
    assertThat(doubleData).isInstanceOf(TrinoDouble.class);
    assertThat(doubleData.get()).isEqualTo(1.23d);

    StdBinary binaryData = trinoFactory.createBinary(ByteBuffer.wrap(bytes));
    assertThat(binaryData).isInstanceOf(TrinoBinary.class);
    assertThat(binaryData.get().array()).isEqualTo(bytes);

    StdType arrayStdType = trinoFactory.createStdType("array(integer)");
    StdArray arrayData   = trinoFactory.createArray(arrayStdType);
    arrayData.add(intData);
    arrayData.add(null);
    assertThat(((TrinoInteger) arrayData.get(0)).get()).isEqualTo(1);
    assertThat(arrayData.get(1)).isEqualTo(null);

    StdMap mapData = createTestMapData(trinoFactory, 10);
    vaildateTestMapData(trinoFactory, mapData, 10);

    StdType structStdType = trinoFactory.createStdType("row(f1 varchar, f2 varchar)");
    StdStruct structData   = trinoFactory.createStruct(structStdType);
    StdString v1 = trinoFactory.createString("v1");
    structData.setField("f1", v1);
    structData.setField("f2", null);
    assertThat(((TrinoString) structData.getField("f1")).get()).isEqualTo("v1");
    assertThat(structData.getField("f2")).isEqualTo(null);

    StdType complexStdType = trinoFactory.createStdType("array(row(f1 array(varchar), f2 map(varchar,varchar), f3 varchar))");
    StdArray outerArray   = trinoFactory.createArray(complexStdType);
    StdType complexStructStdType = trinoFactory.createStdType("row(f1 array(varchar), f2 map(varchar,varchar), f3 varchar)");
    StdStruct complexStructData   = trinoFactory.createStruct(complexStructStdType);
    StdType complexArrayStdType = trinoFactory.createStdType("array(varchar)");
    StdArray innerArray   = trinoFactory.createArray(complexArrayStdType);
    innerArray.add(trinoFactory.createString("v1"));
    complexStructData.setField("f1", innerArray);
    complexStructData.setField("f2", mapData);
    complexStructData.setField("f3", v1);
    outerArray.add(complexStructData);
    StdStruct stdStruct = (StdStruct) outerArray.get(0);
    assertThat(((TrinoString) ((StdArray) stdStruct.getField("f1")).get(0)).get()).isEqualTo("v1");
    assertThat(((TrinoString) ((StdMap) stdStruct.getField("f2")).get(trinoFactory.createString("key0"))).get()).isEqualTo("value0");
    assertThat(((TrinoString) stdStruct.getField("f3")).get()).isEqualTo("v1");
  }

  private static StdMap createTestMapData(StdFactory trinoFactory, int size) {
    StdType mapStdType = trinoFactory.createStdType("map(varchar, varchar)");
    StdMap mapData   = trinoFactory.createMap(mapStdType);
    IntStream.range(0, size).forEach(
        i -> {
          StdString mapKey = trinoFactory.createString("key" + i);
          if (i % 2 == 0) {
            mapData.put(mapKey, trinoFactory.createString("value" + i));
          } else {
            mapData.put(mapKey, null);
          }
        }
    );
    return mapData;
  }

  private static StdMap vaildateTestMapData(StdFactory trinoFactory, StdMap mapData, int size) {
    IntStream.range(0, size).forEach(
        i -> {
          StdString mapKey = trinoFactory.createString("key" + i);
          if (i % 2 == 0) {
            assertThat(((TrinoString) mapData.get(mapKey)).get()).isEqualTo("value" + i);
          } else {
            assertThat(mapData.get(mapKey)).isEqualTo(null);
          }
        }
    );
    return mapData;
  }
}
