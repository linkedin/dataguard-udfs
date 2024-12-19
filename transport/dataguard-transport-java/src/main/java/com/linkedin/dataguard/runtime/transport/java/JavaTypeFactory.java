package com.linkedin.dataguard.runtime.transport.java;

import com.linkedin.dataguard.runtime.transport.java.types.JavaTypeInfo;
import com.linkedin.transport.typesystem.AbstractTypeFactory;
import com.linkedin.transport.typesystem.AbstractTypeSystem;


/**
 * Implementation of Transport's AbstractTypeFactory for testing type system.
 *
 * The {@link AbstractTypeFactory} contains uses {@link AbstractTypeSystem} abstraction to create type objects. In
 * addition, it implements methods to construct types given signature and bound variables.
 */
public class JavaTypeFactory extends AbstractTypeFactory<JavaTypeInfo> {
  @Override
  protected AbstractTypeSystem<JavaTypeInfo> getTypeSystem() {
    return new JavaTypeSystem();
  }
}
