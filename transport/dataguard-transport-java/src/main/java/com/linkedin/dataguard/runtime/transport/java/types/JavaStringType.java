package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdStringType;


public class JavaStringType implements StdStringType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_STRING);
  }
}
