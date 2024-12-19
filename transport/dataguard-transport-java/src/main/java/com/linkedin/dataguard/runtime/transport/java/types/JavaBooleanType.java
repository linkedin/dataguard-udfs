package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdBooleanType;


public class JavaBooleanType implements StdBooleanType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_BOOLEAN);
  }
}
