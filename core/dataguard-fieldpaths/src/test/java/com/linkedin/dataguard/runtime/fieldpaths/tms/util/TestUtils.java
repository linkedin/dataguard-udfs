package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdType;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.*;


public class TestUtils {

  public static void compare(StdData expected, StdData actual) {
    if (expected == null || actual == null) {
      assertThat(actual).isEqualTo(expected);
    } else {
      Object actualData = ((PlatformData) actual).getUnderlyingData();
      Object expectedData = ((PlatformData) expected).getUnderlyingData();
      // string equals is needed to compare Spark map data
      assertThat(actualData.equals(expectedData) || actualData.toString().equals(expectedData.toString()))
          .isEqualTo(true);
    }
  }

  public static StdMap createStdMapWithRowKeyAndStringData(
      List<List<String>> structDataArray,
      List<String> valueArray,
      String mapTypeSignature,
      String keyTypeSignature,
      StdFactory stdFactory
  ) {
    StdType stdMapType = stdFactory.createStdType(mapTypeSignature);
    StdMap input = stdFactory.createMap(stdMapType);
    for (int i = 0; i < structDataArray.size(); i++) {
      List<String> structData = structDataArray.get(i);
      String value = valueArray.get(i);
      StdStruct stdStruct = createStdStructWithStringData(structData, keyTypeSignature, stdFactory);
      if (value == null) {
        input.put(stdStruct, null);
      } else {
        input.put(stdStruct, stdFactory.createString(value));
      }
    }
    return input;
  }

  public static StdArray createStdArrayWithStringData(
      List<String> elements,
      String typeSignature,
      StdFactory stdFactory
  ) {
    StdType stdType = stdFactory.createStdType(typeSignature);
    StdArray stdArray = stdFactory.createArray(stdType);
    for (String element : elements) {
      if (element == null) {
        stdArray.add(null);
      } else {
        stdArray.add(stdFactory.createString(element));
      }
    }
    return stdArray;
  }

  public static StdStruct createStdStructWithStringData(
      List<String> elements,
      String typeSignature,
      StdFactory stdFactory
  ) {
    StdType stdType = stdFactory.createStdType(typeSignature);
    StdStruct stdStruct = stdFactory.createStruct(stdType);
    for (int i = 0; i < elements.size(); i++) {
      if (elements.get(i) == null) {
        stdStruct.setField(i, null);
      } else {
        stdStruct.setField(i, stdFactory.createString(elements.get(i)));
      }
    }
    return stdStruct;
  }

  public static StdStruct createPrimitiveUnionStruct(
      List<Object> elements,
      String typeSignature,
      StdFactory stdFactory
  ) {
    StdType stdType = stdFactory.createStdType(typeSignature);
    StdStruct stdStruct = stdFactory.createStruct(stdType);
    for (int i = 0; i < elements.size(); i++) {
      Object element = elements.get(i);
      if (element == null) {
        stdStruct.setField(i, null);
      } else {
        if (element instanceof String) {
          stdStruct.setField(i, stdFactory.createString((String) element));
        } else if (element instanceof Integer) {
          stdStruct.setField(i, stdFactory.createInteger((Integer) element));
        } else if (element instanceof Long) {
          stdStruct.setField(i, stdFactory.createLong((Long) element));
        } else if (element instanceof Double) {
          stdStruct.setField(i, stdFactory.createDouble((Double) element));
        } else if (element instanceof Boolean) {
          stdStruct.setField(i, stdFactory.createBoolean((Boolean) element));
        } else {
          throw new IllegalArgumentException("Unsupported type: " + element.getClass().getName());
        }
      }
    }
    return stdStruct;
  }

  public static StdMap createStdMapWithStringKeyAndData(
      Map<String, String> dataMap,
      String typeSignature,
      StdFactory stdFactory
  ) {
    StdType stdType = stdFactory.createStdType(typeSignature);
    StdMap stdMap = stdFactory.createMap(stdType);
    dataMap.forEach((key, value) -> {
      StdData valueData = null;
      if (value != null) {
        valueData = stdFactory.createString(value);
      }
      stdMap.put(stdFactory.createString(key), valueData);
    });
    return stdMap;
  }
}
