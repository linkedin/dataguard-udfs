package com.linkedin.dataguard.runtime.transport.java.types;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.transport.java.JavaUtil;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdType;

import static java.util.Objects.*;


public class JavaArrayType implements StdArrayType {

  private final JavaTypeInfo elementType;

  public JavaArrayType(JavaTypeInfo elementType) {
    this.elementType = requireNonNull(elementType);
  }

  @Override
  public StdType elementType() {
    return JavaUtil.createStdType(elementType);
  }

  @Override
  public Object underlyingType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_LIST, ImmutableList.of(elementType), ImmutableList.of("element"));
  }
}
