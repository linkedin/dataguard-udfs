package com.linkedin.dataguard.runtime.transport.java.types;

import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.*;


/**
 * A simple container class to represent testing types. This is analogous to Schema class in Avro type system.
 */
public class JavaTypeInfo {

  private final JavaClassType runtimeClass;
  private final List<JavaTypeInfo> parameterTypes;

  private final List<String> parameterNames;

  public JavaTypeInfo(JavaClassType runtimeClass, List<JavaTypeInfo> parameterTypes, List<String> names) {
    this.runtimeClass = requireNonNull(runtimeClass);
    this.parameterTypes = requireNonNull(parameterTypes);
    this.parameterNames = requireNonNull(names);
  }

  public JavaTypeInfo(JavaClassType runtimeClass) {
    this(runtimeClass, ImmutableList.of(), ImmutableList.of());
  }

  public enum JavaClassType {

    JAVA_LIST(List.class),
    JAVA_BYTEBUFFER(ByteBuffer.class),
    JAVA_BOOLEAN(Boolean.class),
    JAVA_DOUBLE(Double.class),
    JAVA_FLOAT(Float.class),
    JAVA_INTEGER(Integer.class),
    JAVA_LONG(Long.class),
    JAVA_MAP(Map.class),
    JAVA_STRING(String.class),
    JAVA_STRUCT(Map.class),
    JAVA_UNKNOWN(null);

    private final Class<?> clazz;

    JavaClassType(Class<?> clazz) {
      this.clazz = clazz;
    }

    public Class<?> getClazz() {
      return clazz;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JavaTypeInfo that = (JavaTypeInfo) o;
    return runtimeClass == that.runtimeClass && parameterTypes.equals(that.parameterTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(runtimeClass, parameterTypes);
  }

  public JavaClassType getRuntimeClass() {
    return runtimeClass;
  }

  public List<JavaTypeInfo> getParameterTypes() {
    return parameterTypes;
  }

  public List<String> getParameterNames() {
    return parameterNames;
  }
}
