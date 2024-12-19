package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdLongType;


public class JavaLongType implements StdLongType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_LONG);
  }
}
