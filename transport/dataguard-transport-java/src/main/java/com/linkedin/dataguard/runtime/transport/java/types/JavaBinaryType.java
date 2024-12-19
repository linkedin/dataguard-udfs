package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.transport.api.types.StdBinaryType;


public class JavaBinaryType implements StdBinaryType {
  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_BYTEBUFFER);
  }
}
