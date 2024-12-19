package com.linkedin.dataguard.runtime.trino;

import com.google.common.collect.Lists;
import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.RedactHandler;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.dataguard.runtime.trino.handler.factory.TrinoHandlerFactory;
import com.linkedin.dataguard.runtime.transport.trino.types.TrinoTypeDataProvider;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.trino.data.TrinoBoolean;
import java.util.List;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;


public class TestTrinoUnionTypeHandler {

  public static final TrinoTypeDataProvider TYPE_DATA_PROVIDER = new TrinoTypeDataProvider();

  @Test
  public void redactNullableUnionOfPrimitive() {
    StdFactory stdFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdType booleanType = stdFactory.createStdType("boolean");
    StdBoolean defaultValue = stdFactory.createBoolean(false);
    StdBoolean stdData = stdFactory.createBoolean(true);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createTrinoUnionTMSEnforcer("f[type=boolean]", TYPE_DATA_PROVIDER, booleanType, action);
    RedactHandler unionTypeHandler = tmsEnforcer.getHandlers().get(0);
    Object result = ((TrinoBoolean) unionTypeHandler.redact(stdData)).getUnderlyingData();
    assertThat(result).isEqualTo(false);
  }

  @Test
  public void redactNullableUnionOfStruct() {
    StdFactory stdFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdData defaultValue = stdFactory.createBoolean(false);
    Action action = new EraseAction(defaultValue);
    StdType structType = stdFactory.createStdType("row(a boolean, b boolean)");
    StdBoolean trueData = stdFactory.createBoolean(true);
    StdStruct structData = createStructData(stdFactory, structType, Lists.newArrayList(trueData, null));
    TMSEnforcer tmsEnforcer = createTrinoUnionTMSEnforcer("f[type=struct<a:boolean,b:boolean>].a", TYPE_DATA_PROVIDER, structType, action);
    RedactHandler unionTypeHandler = tmsEnforcer.getHandlers().get(0);
    StdStruct result = (StdStruct) unionTypeHandler.redact(structData);
    assertThat(((TrinoBoolean) result.getField(0)).getUnderlyingData()).isEqualTo(false);
    assertThat((TrinoBoolean) result.getField(1)).isEqualTo(null);
  }

  @Test
  public void redactComplexUnionThrows() {
    StdFactory stdFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdData defaultValue = stdFactory.createBoolean(false);
    Action action = new EraseAction(defaultValue);
    StdType structType = stdFactory.createStdType("row(tag integer, field0 boolean, field1 varchar)");
    StdInteger tagData = stdFactory.createInteger(0);
    StdBoolean booleanData = stdFactory.createBoolean(true);
    assertThatThrownBy(() -> createTrinoUnionTMSEnforcer(
        "f[type=struct<tag:integer,field0:boolean,field1:varchar>]",
        TYPE_DATA_PROVIDER,
        structType,
        action))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Complex union type redaction is not supported in Trino yet");
  }

  private static StdStruct createStructData(StdFactory stdFactory, StdType structType, List<StdData> structFieldData) {
    StdStruct structData = stdFactory.createStruct(structType);
    for (int i = 0; i < structFieldData.size(); i++) {
      structData.setField(i, structFieldData.get(i));
    }
    return structData;
  }

  private TMSEnforcer createTrinoUnionTMSEnforcer(String tmsPath, TrinoTypeDataProvider typeDataProvider, StdType rootType, Action action) {
    return new TMSEnforcer(tmsPath, rootType, typeDataProvider, action, new TrinoHandlerFactory());
  }
}