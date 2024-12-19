package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.google.common.collect.ImmutableMap;
import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory.HandlerFactory;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdString;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdType;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.Constants.*;
import static com.linkedin.dataguard.runtime.fieldpaths.tms.util.TestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;


public abstract class TestRedact {

  protected abstract FormatSpecificTypeDataProvider getTypeDataProvider();
  protected abstract HandlerFactory getHandlerFactory();

  @Test
  public void testRedactArrayWithDefaultValue() {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    StdArray input = createStdArrayWithStringData(
        Arrays.asList("foo", "bar", null), "array(varchar)", stdFactory);
    StdArray expected = createStdArrayWithStringData(
        Arrays.asList(REDACT_OUTPUT_STRING, REDACT_OUTPUT_STRING, REDACT_OUTPUT_STRING), "array(varchar)", stdFactory);
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createTMSEnforcer("f.[type=string]", "array(varchar)", action);
    RedactHandler arrayElementHandler = tmsEnforcer.getHandlers().get(0);
    StdArray actual = (StdArray) arrayElementHandler.redact(input);
    compare(expected, actual);
  }

  @Test
  public void testRedactStructWithDefaultValue() {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    String typeSignature = "row(f0 varchar, f1 varchar)";
    StdStruct input = createStdStructWithStringData(
        Arrays.asList("value1", "value2"), typeSignature, stdFactory);
    StdStruct expected = createStdStructWithStringData(
        Arrays.asList(REDACT_OUTPUT_STRING, "value2"), typeSignature, stdFactory);
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createTMSEnforcer("f.f0", typeSignature, action);
    RedactHandler structFieldHandler = tmsEnforcer.getHandlers().get(0);
    StdStruct actual = (StdStruct) structFieldHandler.redact(input);
    compare(expected, actual);
    TMSEnforcer tmsEnforcerWithNonExistentField = createTMSEnforcer("f.f2", typeSignature, action);
    assertThatThrownBy(() -> tmsEnforcerWithNonExistentField.getHandlers().get(0).redact(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("-1");
  }

  @Test
  public void testRedactMapKeyThrows() {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    String structTypeSignature = "row(f0 varchar, f1 varchar)";
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    String mapTypeSignature = "map(row(f0 varchar, f1 varchar),varchar)";
    StdMap input = createStdMapWithRowKeyAndStringData(
        Arrays.asList(Arrays.asList("value1", "value2"), Arrays.asList("value3", "value4")),
        Arrays.asList("value1", "value2"), mapTypeSignature, structTypeSignature, stdFactory);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createTMSEnforcer("f.[key=struct<f0:string,f1:string>].f0", mapTypeSignature, action);
    RedactHandler mapKeyHandler = tmsEnforcer.getHandlers().get(0);
    assertThatThrownBy(() -> mapKeyHandler.redact(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("map key redaction should always be on primitive key type");
  }

  @Test
  public void testRedactMapKeyReturnsDefaultValue() {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    String structTypeSignature = "row(f0 varchar, f1 varchar)";
    String mapTypeSignature = "map(row(f0 varchar, f1 varchar),varchar)";
    StdMap input = createStdMapWithRowKeyAndStringData(
        Arrays.asList(Arrays.asList("value1", "value2"), Arrays.asList("value3", "value4")),
        Arrays.asList("value1", "value2"), mapTypeSignature, structTypeSignature, stdFactory);
    Action action = new EraseAction(null);
    TMSEnforcer tmsEnforcer = createTMSEnforcer("f.[key=struct<f0:string,f1:string>]",
        "map(row(f0 varchar, f1 varchar),varchar)", action);
    RedactHandler mapKeyHandler = tmsEnforcer.getHandlers().get(0);
    Object result = mapKeyHandler.redact(input);
    assertThat(result).isEqualTo(null);
  }

  @Test
  public void testRedactMapValueReturnsDefaultValue() {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    String mapTypeSignature = "map(varchar,varchar)";
    StdMap input = createStdMapWithStringKeyAndData(
        ImmutableMap.of("key1", "value1", "key2", "value2"), mapTypeSignature, stdFactory);
    StdMap expected = createStdMapWithStringKeyAndData(
        ImmutableMap.of("key1", REDACT_OUTPUT_STRING, "key2", REDACT_OUTPUT_STRING), mapTypeSignature, stdFactory);
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createTMSEnforcer("f.[value=string]", mapTypeSignature, action);
    RedactHandler mapValueHandler = tmsEnforcer.getHandlers().get(0);
    StdMap actual = (StdMap) mapValueHandler.redact(input);
    compare(expected, actual);
  }

  @Test
  public void testRedactComplexPathReturnsDefaultValue() {
    String mapTypeSignature = "map(varchar,array(row(f0 varchar, f1 row(f2 varchar, f3 varchar))))";
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    StdMap input = createComplexStructure(stdFactory, false);
    StdMap expected = createComplexStructure(stdFactory, true);
    StdString defaultValue = stdFactory.createString(REDACT_OUTPUT_STRING);
    Action action = new EraseAction(defaultValue);
    TMSEnforcer tmsEnforcer = createTMSEnforcer(
        "f.[value=array<struct<f0:string,f1:struct<f2:string,f3:string>>>].[type=struct<f0:string,f1:struct<f2:string,f3:string>>].f1.f2",
        mapTypeSignature, action);
    RedactHandler mapValueHandler = tmsEnforcer.getHandlers().get(0);
    StdMap actual = (StdMap) mapValueHandler.redact(input);
    compare(expected, actual);
  }

  protected TMSEnforcer createTMSEnforcer(
      String tmsPath, String typeSignature, Action action) {
    FormatSpecificTypeDataProvider typeDataProvider = getTypeDataProvider();
    StdFactory stdFactory = typeDataProvider.getStdFactory();
    StdType rootType = stdFactory.createStdType(typeSignature);
    return new TMSEnforcer(tmsPath, rootType, typeDataProvider, action, getHandlerFactory());
  }

  // Creates a complex structuer of type signature map(varchar,array(row(f0 varchar, f1 row(f2 varchar, f3 varchar))))
  public static StdMap createComplexStructure(StdFactory stdFactory, boolean isRedacted) {
    String mapTypeSignature = "map(varchar,array(row(f0 varchar, f1 row(f2 varchar, f3 varchar))))";
    StdType mapType = stdFactory.createStdType(mapTypeSignature);
    String arrayTypeSignature = "array(row(f0 varchar, f1 row(f2 varchar, f3 varchar)))";
    StdType arrayType = stdFactory.createStdType(arrayTypeSignature);
    String outerStructTypeSignature = "row(f0 varchar, f1 row(f2 varchar, f3 varchar))";
    StdType outerStructType = stdFactory.createStdType(outerStructTypeSignature);
    String innerStructTypeSignature = "row(f2 varchar, f3 varchar)";
    StdType innerStructType = stdFactory.createStdType(innerStructTypeSignature);
    StdStruct innerStruct = stdFactory.createStruct(innerStructType);
    String value1 = isRedacted ? REDACT_OUTPUT_STRING : "value1";
    innerStruct.setField(0, stdFactory.createString(value1));
    innerStruct.setField(1, stdFactory.createString("value2"));
    StdStruct outerStruct = stdFactory.createStruct(outerStructType);
    outerStruct.setField(0, stdFactory.createString("value3"));
    outerStruct.setField(1, innerStruct);
    StdArray array = stdFactory.createArray(arrayType);
    array.add(outerStruct);
    StdMap map = stdFactory.createMap(mapType);
    map.put(stdFactory.createString("key1"), array);
    return map;
  }
}