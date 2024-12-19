package com.linkedin.dataguard.runtime.spark.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.RedactHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.TestRedact;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory.HandlerFactory;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.dataguard.runtime.spark.tms.handler.factory.SparkHandlerFactory;
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdString;
import com.linkedin.transport.api.data.StdStruct;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.Constants.*;
import static com.linkedin.dataguard.runtime.fieldpaths.tms.util.TestUtils.*;
import static com.linkedin.dataguard.runtime.spark.util.TestUtils.*;


public class TestSparkRedact extends TestRedact {

  private static final FormatSpecificTypeDataProvider TYPE_DATA_PROVIDER = new NullableSparkTypeDataProvider();
  private static final HandlerFactory HANDLER_FACTORY = new SparkHandlerFactory();

  @Override
  protected FormatSpecificTypeDataProvider getTypeDataProvider() {
    return TYPE_DATA_PROVIDER;
  }

  @Override
  protected HandlerFactory getHandlerFactory() {
    return HANDLER_FACTORY;
  }

  @Test
  public void testRedactUnionOfPrimitive() {
    StdFactory stdFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    String typeSignature = "varchar";
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createSparkUnionTMSEnforcer("f[type=string]", TYPE_DATA_PROVIDER, typeSignature, action);
    RedactHandler unionTypeHandler = tmsEnforcer.getHandlers().get(0);
    StdString input = stdFactory.createString(REDACT_INPUT_STRING);
    StdString expected = stdFactory.createString(REDACT_OUTPUT_STRING);
    StdData actual = unionTypeHandler.redact(input);
    compare(expected, actual);
  }

  @Test
  public void testRedactComplexUnionOfPrimitive() {
    StdFactory stdFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    String typeSignature = "row(tag integer, field0 varchar, field1 integer)";
    Action action = new EraseAction(defaultValue);
    StdStruct input = createPrimitiveUnionStruct(Arrays.asList(0, "value1", null), typeSignature, stdFactory);
    TMSEnforcer tmsEnforcer = createSparkUnionTMSEnforcer("f[type=string]", TYPE_DATA_PROVIDER, typeSignature, action);
    RedactHandler unionTypeHandler = tmsEnforcer.getHandlers().get(0);
    StdStruct expected = createPrimitiveUnionStruct(Arrays.asList(0, REDACT_OUTPUT_STRING, null), typeSignature, stdFactory);
    StdData actual = unionTypeHandler.redact(input);
    compare(expected, actual);
  }

  @Test
  public void testRedactUnionOfStruct() {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    String typeSignature = "row(f0 varchar, f1 varchar)";
    StdStruct input = createStdStructWithStringData(
        Arrays.asList("value1", "value2"), typeSignature, stdFactory);
    StdStruct expected = createStdStructWithStringData(
        Arrays.asList(REDACT_OUTPUT_STRING, "value2"), typeSignature, stdFactory);
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createSparkUnionTMSEnforcer("f[type=struct<f0:string,f1:string>].f0", typeDataProvider, typeSignature, action);
    RedactHandler structFieldHandler = tmsEnforcer.getHandlers().get(0);
    StdStruct actual = (StdStruct) structFieldHandler.redact(input);
    compare(expected, actual);
  }


  @Test
  public void testRedactComplexPathWithUnionReturnsDefaultValue() {
    String mapTypeSignature = "map(varchar,array(row(f0 varchar, f1 row(f2 varchar, f3 varchar))))";
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    StdMap input = createComplexStructure(stdFactory, false);
    StdMap expected = createComplexStructure(stdFactory, true);
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createSparkUnionTMSEnforcer(
        "f[type=array<struct<f0:string,f1:struct<f2:string,f3:string>>>].[value=array<struct<f0:string,f1:struct<f2:string,f3:string>>>].[type=struct<f0:string,f1:struct<f2:string,f3:string>>].f1.f2",
        typeDataProvider,
        mapTypeSignature, action);
    RedactHandler mapValueHandler = tmsEnforcer.getHandlers().get(0);
    StdMap actual = (StdMap) mapValueHandler.redact(input);
    compare(expected, actual);
  }
}