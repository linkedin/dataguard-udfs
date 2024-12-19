package com.linkedin.dataguard.runtime.transport.java;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.transport.java.types.JavaTypeInfo;
import com.linkedin.transport.typesystem.AbstractBoundVariables;
import com.linkedin.transport.typesystem.AbstractTypeSystem;
import java.util.List;


/**
 * Implementation of Transport's {@link AbstractTypeSystem}. The methods here are used by Transport's
 * {@link AbstractBoundVariables} class as utilities while binding type signatures to types represented
 * in {@link JavaTypeInfo} format.
 */
public class JavaTypeSystem extends AbstractTypeSystem<JavaTypeInfo> {

  public static final String JAVA_KEY_NAME = "key";
  public static final String JAVA_VALUE_NAME = "value";
  public static final String JAVA_ELEMENT_NAME = "element";

  @Override
  protected JavaTypeInfo getArrayElementType(JavaTypeInfo dataType) {
    return dataType.getParameterTypes().get(0);
  }

  @Override
  protected JavaTypeInfo getMapKeyType(JavaTypeInfo dataType) {
    return dataType.getParameterTypes().get(0);
  }

  @Override
  protected JavaTypeInfo getMapValueType(JavaTypeInfo dataType) {
    return dataType.getParameterTypes().get(1);
  }

  @Override
  protected List<JavaTypeInfo> getStructFieldTypes(JavaTypeInfo dataType) {
    return dataType.getParameterTypes();
  }

  @Override
  protected boolean isUnknownType(JavaTypeInfo dataType) {
    return false;
  }

  @Override
  protected boolean isBooleanType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_BOOLEAN;
  }

  @Override
  protected boolean isIntegerType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_INTEGER;
  }

  @Override
  protected boolean isLongType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_LONG;
  }

  @Override
  protected boolean isStringType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_STRING;
  }

  @Override
  protected boolean isFloatType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_FLOAT;
  }

  @Override
  protected boolean isDoubleType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_DOUBLE;
  }

  @Override
  protected boolean isBinaryType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_BYTEBUFFER;
  }

  @Override
  protected boolean isArrayType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_LIST;
  }

  @Override
  protected boolean isMapType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_MAP;
  }

  @Override
  protected boolean isStructType(JavaTypeInfo dataType) {
    return dataType.getRuntimeClass() == JavaTypeInfo.JavaClassType.JAVA_STRUCT;
  }

  @Override
  protected JavaTypeInfo createBooleanType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_BOOLEAN);
  }

  @Override
  protected JavaTypeInfo createIntegerType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_INTEGER);
  }

  @Override
  protected JavaTypeInfo createLongType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_LONG);
  }

  @Override
  protected JavaTypeInfo createStringType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_STRING);
  }

  @Override
  protected JavaTypeInfo createFloatType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_FLOAT);
  }

  @Override
  protected JavaTypeInfo createDoubleType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_DOUBLE);
  }

  @Override
  protected JavaTypeInfo createBinaryType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_BYTEBUFFER);
  }

  @Override
  protected JavaTypeInfo createUnknownType() {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_UNKNOWN);
  }

  @Override
  protected JavaTypeInfo createArrayType(JavaTypeInfo elementType) {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_LIST, ImmutableList.of(elementType), ImmutableList.of(JAVA_ELEMENT_NAME));
  }

  @Override
  protected JavaTypeInfo createMapType(JavaTypeInfo keyType, JavaTypeInfo valueType) {
    return new JavaTypeInfo(
        JavaTypeInfo.JavaClassType.JAVA_MAP, ImmutableList.of(keyType, valueType), ImmutableList.of(JAVA_KEY_NAME, JAVA_VALUE_NAME));
  }

  @Override
  protected JavaTypeInfo createStructType(List<String> fieldNames, List<JavaTypeInfo> fieldTypes) {
    return new JavaTypeInfo(JavaTypeInfo.JavaClassType.JAVA_STRUCT,
        fieldTypes,
        fieldNames);
  }
}
