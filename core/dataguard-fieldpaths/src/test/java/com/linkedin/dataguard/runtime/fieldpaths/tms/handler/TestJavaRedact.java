package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.transport.java.JavaTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory.HandlerFactory;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdString;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.Constants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;


public class TestJavaRedact extends TestRedact {

  private static final FormatSpecificTypeDataProvider TYPE_DATA_PROVIDER = new JavaTypeDataProvider();
  private static final HandlerFactory HANDLER_FACTORY = new HandlerFactory();

  @Override
  protected FormatSpecificTypeDataProvider getTypeDataProvider() {
    return TYPE_DATA_PROVIDER;
  }

  @Override
  protected HandlerFactory getHandlerFactory() {
    return HANDLER_FACTORY;
  }

  @Test
  public void testRedactUnionThrows() {
    StdFactory stdFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    assertThatThrownBy(() ->  createTMSEnforcer("f[type=string]", "varchar", action))
        .isInstanceOf(UnsupportedOperationException.class);
  }
}