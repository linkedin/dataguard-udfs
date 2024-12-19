package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdFloatType;


public class JavaFloatType implements StdFloatType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_FLOAT);
  }
}
