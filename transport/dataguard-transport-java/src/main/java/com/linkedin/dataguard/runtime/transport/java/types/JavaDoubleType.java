package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdDoubleType;


public class JavaDoubleType implements StdDoubleType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_DOUBLE);
  }
}
