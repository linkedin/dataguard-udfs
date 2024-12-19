package com.linkedin.dataguard.runtime.transport.trino.types;

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.transport.trino.NullableTrinoFactory;
import com.linkedin.dataguard.runtime.transport.trino.TrinoBoundVariables;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.typesystem.AbstractBoundVariables;


/**
 * Create the Trino-specific type data provider using NullableTrinoFactory
 */
public class TrinoTypeDataProvider extends FormatSpecificTypeDataProvider {

  private static final StdFactory STD_FACTORY = new NullableTrinoFactory(new TrinoBoundVariables());

  @Override
  public boolean hasCaseSensitiveFieldNames() {
    return false;
  }

  @Override
  public StdFactory getStdFactory() {
    return STD_FACTORY;
  }

  @Override
  public StdFactory getStdFactoryWithBinding(AbstractBoundVariables boundVariables) {
    return new NullableTrinoFactory(boundVariables);
  }

  @Override
  public AbstractBoundVariables createAbstractBoundVariables() {
    return new TrinoBoundVariables();
  }
}
