package com.linkedin.dataguard.runtime.transport.java;

import com.linkedin.dataguard.runtime.transport.java.data.JavaArray;
import com.linkedin.dataguard.runtime.transport.java.data.JavaBinary;
import com.linkedin.dataguard.runtime.transport.java.data.JavaBoolean;
import com.linkedin.dataguard.runtime.transport.java.data.JavaDouble;
import com.linkedin.dataguard.runtime.transport.java.data.JavaFloat;
import com.linkedin.dataguard.runtime.transport.java.data.JavaInteger;
import com.linkedin.dataguard.runtime.transport.java.data.JavaLong;
import com.linkedin.dataguard.runtime.transport.java.data.JavaMap;
import com.linkedin.dataguard.runtime.transport.java.data.JavaString;
import com.linkedin.dataguard.runtime.transport.java.data.JavaStruct;
import com.linkedin.dataguard.runtime.transport.java.types.JavaArrayType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaFloatType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaLongType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaMapType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaTypeInfo;
import com.linkedin.dataguard.runtime.transport.java.types.JavaBinaryType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaBooleanType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaDoubleType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaIntegerType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaStringType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaStructType;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;


/**
 * Provides utility methods to create types and data objects using the testing factory.
 * This is analogous to AvroWrapper class in Transport.
 */
public final class JavaUtil {

  public static StdData createStdData(Object data, StdFactory factory) {
    if (data == null) {
      return null;
    }
    if (data instanceof Integer) {
      return new JavaInteger((int) data);
    } else if (data instanceof Long) {
      return new JavaLong((long) data);
    } else if (data instanceof Boolean) {
      return new JavaBoolean((boolean) data);
    } else if (data instanceof String) {
      return new JavaString((String) data);
    } else if (data instanceof List) {
      return new JavaArray((List) data, factory);
    } else if (data instanceof Map) {
      return new JavaMap((Map) data, factory);
    } else if (data instanceof JavaStruct.StructInfo) {
      return new JavaStruct(factory, (JavaStruct.StructInfo) data);
    } else if (data instanceof Float) {
      return new JavaFloat((float) data);
    } else if (data instanceof Double) {
      return new JavaDouble((double) data);
    } else if (data instanceof ByteBuffer) {
      return new JavaBinary((ByteBuffer) data);
    }
    assert false : "Unrecognized Data Type: " + data.getClass();
    return null;
  }

  private JavaUtil() { }

  public static StdType createStdType(JavaTypeInfo type) {
    switch (type.getRuntimeClass()) {
      case JAVA_BOOLEAN:
        return new JavaBooleanType();
      case JAVA_INTEGER:
        return new JavaIntegerType();
      case JAVA_LONG:
        return new JavaLongType();
      case JAVA_STRING:
        return new JavaStringType();
      case JAVA_FLOAT:
        return new JavaFloatType();
      case JAVA_DOUBLE:
        return new JavaDoubleType();
      case JAVA_BYTEBUFFER:
        return new JavaBinaryType();
      case JAVA_STRUCT:
        return new JavaStructType(type.getParameterNames(), type.getParameterTypes());
      case JAVA_LIST:
        if (type.getParameterTypes().size() != 1) {
          throw new RuntimeException("Expected one element, found " + type.getParameterTypes());
        }
        return new JavaArrayType(type.getParameterTypes().get(0));
      case JAVA_MAP:
        if (type.getParameterTypes().size() != 2) {
          throw new RuntimeException("Expected one element, found " + type.getParameterTypes());
        }
        return new JavaMapType(type.getParameterTypes().get(0),
            type.getParameterTypes().get(1));
      default:
        throw new UnsupportedOperationException(String.format("Unsupported type %s provided", type));
    }
  }
}
