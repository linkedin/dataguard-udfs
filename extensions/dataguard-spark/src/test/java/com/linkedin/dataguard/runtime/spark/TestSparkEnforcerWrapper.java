package com.linkedin.dataguard.runtime.spark;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Enforcer;
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.util.ArrayBasedMapData;
import org.apache.spark.sql.catalyst.util.ArrayData;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.unsafe.types.UTF8String;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerFactory.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.Parsing.*;
import static com.linkedin.dataguard.runtime.spark.SparkEnforcerWrapper.*;
import static com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper.*;
import static java.lang.String.*;


public class TestSparkEnforcerWrapper {

  private static DataType mapType;
  private static DataType arrayType;
  private static DataType structType;
  private static DataType complexArrayType;
  private static DataType stringType;
  private static NullableSparkTypeDataProvider typeDataProvider;
  private static final String DEFAULT_STRING = "default";
  private static final UTF8String DEFAULT_UTF8_STRING = UTF8String.fromString(DEFAULT_STRING);

  @BeforeAll
  public static void setup() {
    stringType = DataTypes.StringType;
    mapType = DataTypes.createMapType(DataTypes.StringType, DataTypes.StringType);
    arrayType = DataTypes.createArrayType(DataTypes.StringType);
    structType = DataTypes.createStructType(
        ImmutableList.of(DataTypes.createStructField("f0", DataTypes.StringType, false),
        DataTypes.createStructField("f1", DataTypes.StringType, false)));
    complexArrayType = DataTypes.createArrayType(structType);
    typeDataProvider = new NullableSparkTypeDataProvider();
  }

  @Test
  public void testTransformArrayTransform() {
    ArrayData complexArrayData = createComplexArrayData("u0", "u1", "v0", "v1");
    Enforcer enforcer = createEnforcerSpark("$.x[:].f1", "x", complexArrayType);
    Object transformedArrayDataActual  = transform(complexArrayType, complexArrayData,
        DEFAULT_UTF8_STRING, stringType, enforcer);
    ArrayData expectedArrayData = createComplexArrayData("u0", DEFAULT_STRING, "v0", DEFAULT_STRING);
    Assertions.assertEquals(expectedArrayData, transformedArrayDataActual);
  }

  private static ArrayData createComplexArrayData(String u1, String u2, String v1, String v2) {
    Seq<Object> seq1 = JavaConverters.asScalaIteratorConverter(ImmutableList.<Object>of(UTF8String.fromString(u1),
        UTF8String.fromString(u2)).iterator()).asScala().toSeq();
    Seq<Object> seq2 = JavaConverters.asScalaIteratorConverter(ImmutableList.<Object>of(UTF8String.fromString(v1),
        UTF8String.fromString(v2)).iterator()).asScala().toSeq();
    InternalRow structData1 = InternalRow.fromSeq(seq1);
    InternalRow structData2 = InternalRow.fromSeq(seq2);
    return ArrayData.toArrayData(new Object[]{structData1, structData2});
  }

  private static InternalRow createStructData(String u1, String u2) {
    Seq<Object> seq1 = JavaConverters.asScalaIteratorConverter(ImmutableList.<Object>of(UTF8String.fromString(u1),
        UTF8String.fromString(u2)).iterator()).asScala().toSeq();
    return InternalRow.fromSeq(seq1);
  }

  private static ArrayData createSimpleArrayData(String u1, String u2, String u3) {
    return ArrayData.toArrayData(
        new Object[]{
            UTF8String.fromString(u1),
            UTF8String.fromString(u2),
            UTF8String.fromString(u3)
        });
  }

  @Test
  public void testTransformStructFieldAccess() {
    InternalRow structData = createStructData("v0", "v1");
    Enforcer enforcer = createEnforcerSpark("$.x.f0", "x", structType);
    Object transformedStructDataActual  = transform(structType, structData, DEFAULT_UTF8_STRING,
        stringType, enforcer);
    InternalRow transformedStructDataExpected  = createStructData("default", "v1");
    Assertions.assertEquals(transformedStructDataExpected, transformedStructDataActual);
  }

  @Test
  public void testTransformArrayIndexAccess() {
    ArrayData arrayData = createSimpleArrayData("v1", "v2", "v3");
    Enforcer enforcer = createEnforcerSpark("$.x[0]", "x", arrayType);
    Object transformedArrayDataActual  = transform(arrayType, arrayData, DEFAULT_UTF8_STRING, stringType, enforcer);
    ArrayData transformedArrayDataExpected = createSimpleArrayData(DEFAULT_STRING, "v2", "v3");
    Assertions.assertEquals(transformedArrayDataExpected, transformedArrayDataActual);
  }

  @Test
  public void testTransformMapKeyLookup() {
    ArrayBasedMapData mapData =
        new ArrayBasedMapData(createSimpleArrayData("k1", "k2", "k3"), createSimpleArrayData("v1", "v2", "v3"));
    ArrayBasedMapData transformedMapDataExpected =
        new ArrayBasedMapData(createSimpleArrayData("k1", "k2", "k3"), createSimpleArrayData(DEFAULT_STRING, "v2", "v3"));

    Enforcer enforcer = createEnforcerSpark("$.x['k1']", "x", mapType);
    ArrayBasedMapData transformedMapDataActual  = (ArrayBasedMapData) transform(mapType, mapData, DEFAULT_UTF8_STRING, stringType,  enforcer);
    Assertions.assertEquals(transformedMapDataActual.keyArray(), transformedMapDataExpected.keyArray());
    Assertions.assertEquals(transformedMapDataActual.valueArray(), transformedMapDataExpected.valueArray());
  }

  private static Enforcer createEnforcerSpark(String path, String rootColumnName, DataType rootType) {
    String effectivePath = path.replace(format("$.%s", rootColumnName), "$");
    return createEnforcer(parseRowSelectorAwareFieldPath(effectivePath), createStdType(rootType), typeDataProvider);
  }
}
