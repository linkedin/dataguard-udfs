package com.linkedin.dataguard.runtime.transport.java.types;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.transport.java.JavaUtil;
import com.linkedin.transport.api.types.StdMapType;
import com.linkedin.transport.api.types.StdType;


public class JavaMapType implements StdMapType {

  private final JavaTypeInfo typeInfo;

  public JavaMapType(JavaTypeInfo keyType, JavaTypeInfo valueType) {
    this.typeInfo = new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_MAP, ImmutableList.of(keyType, valueType), ImmutableList.of("key", "value"));
  }

  @Override
  public StdType keyType() {
    return JavaUtil.createStdType(typeInfo.getParameterTypes().get(0));
  }

  @Override
  public StdType valueType() {
    return JavaUtil.createStdType(typeInfo.getParameterTypes().get(1));
  }

  @Override
  public Object underlyingType() {
    return typeInfo;
  }
}
