package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdIntegerType;


public class JavaIntegerType implements StdIntegerType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_INTEGER);
  }
}
