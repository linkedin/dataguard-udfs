package com.linkedin.dataguard.runtime.transport.java.types;

import com.linkedin.dataguard.runtime.transport.java.JavaUtil;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import java.util.List;

import static com.google.common.collect.ImmutableList.*;


public class JavaStructType implements StdStructType {

  private final JavaTypeInfo typeInfo;

  public JavaStructType(List<String> fieldNames, List<JavaTypeInfo> types) {
    this.typeInfo = new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_STRUCT, types, fieldNames);
  }

  @Override
  public List<String> fieldNames() {
    return typeInfo.getParameterNames();
  }

  @Override
  public List<? extends StdType> fieldTypes() {
    return typeInfo.getParameterTypes().stream()
        .map(JavaUtil::createStdType)
        .collect(toImmutableList());
  }

  @Override
  public Object underlyingType() {
    return typeInfo;
  }
}
