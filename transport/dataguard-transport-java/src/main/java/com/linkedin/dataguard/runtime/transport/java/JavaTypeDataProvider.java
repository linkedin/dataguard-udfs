package com.linkedin.dataguard.runtime.transport.java;

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.typesystem.AbstractBoundVariables;


public class JavaTypeDataProvider extends FormatSpecificTypeDataProvider {
  @Override
  public boolean hasCaseSensitiveFieldNames() {
    return true;
  }

  @Override
  public StdFactory getStdFactory() {
    return new JavaFactory();
  }

  @Override
  public StdFactory getStdFactoryWithBinding(AbstractBoundVariables boundVariables) {
    return new JavaFactory((JavaBoundVariables) boundVariables);
  }

  @Override
  public AbstractBoundVariables createAbstractBoundVariables() {
    return new JavaBoundVariables();
  }
}
