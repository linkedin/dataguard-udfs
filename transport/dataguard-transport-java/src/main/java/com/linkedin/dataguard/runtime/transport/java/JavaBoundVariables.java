package com.linkedin.dataguard.runtime.transport.java;

import com.linkedin.dataguard.runtime.transport.java.types.JavaTypeInfo;
import com.linkedin.transport.typesystem.AbstractBoundVariables;
import com.linkedin.transport.typesystem.AbstractTypeSystem;
import com.linkedin.transport.typesystem.AbstractTypeFactory;
import com.linkedin.transport.typesystem.TypeSignature;


/**
 * An implementation of Transport's {@link AbstractBoundVariables} for testing type system. Uses {@link JavaTypeSystem}
 * to provide utilities for types represented in {@link JavaTypeInfo} format.
 *
 * {@link AbstractBoundVariables} implements methods to generate binding from type signature to format-specific types
 * (in this case, it's {@link JavaTypeInfo}). It is then used by {@link AbstractTypeFactory} to get the bound type
 * given a signature.
 *
 * For example, given a typesignature->type bindings like
 * - "array(T) -> array(JavaBooleanType)"
 * - "R" -> "JavaString"
 * A binding is generated within {@link JavaBoundVariables}. After that, given a type-signature like "Map(R,T)",
 * {@link AbstractTypeFactory#createType(TypeSignature, AbstractBoundVariables)} can use the bound variables to return
 * the type `JavaMapType(JavaStringType, JavaBooleanType)`.
 */
public class JavaBoundVariables extends AbstractBoundVariables<JavaTypeInfo> {
  @Override
  protected AbstractTypeSystem getTypeSystem() {
    return new JavaTypeSystem();
  }
}
