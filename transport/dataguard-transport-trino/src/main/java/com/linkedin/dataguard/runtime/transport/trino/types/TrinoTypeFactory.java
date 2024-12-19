
package com.linkedin.dataguard.runtime.transport.trino.types;

import com.linkedin.transport.typesystem.AbstractTypeFactory;
import com.linkedin.transport.typesystem.AbstractTypeSystem;
import io.trino.spi.type.Type;


/**
 * Trino-specific type factory implementation.
 */
public class TrinoTypeFactory extends AbstractTypeFactory<Type> {
  @Override
  protected AbstractTypeSystem<Type> getTypeSystem() {
    return new TrinoTypeSystem();
  }
}
