package com.linkedin.dataguard.runtime.transport.trino;

import com.linkedin.dataguard.runtime.transport.trino.types.TrinoTypeSystem;
import io.trino.spi.type.Type;
import com.linkedin.transport.typesystem.AbstractBoundVariables;
import com.linkedin.transport.typesystem.AbstractTypeSystem;


/**
 * Trino implementation for AbstractBoundVariables
 */
public class TrinoBoundVariables extends AbstractBoundVariables<Type> {
  @Override
  protected AbstractTypeSystem<Type> getTypeSystem() {
    return new TrinoTypeSystem();
  }
}