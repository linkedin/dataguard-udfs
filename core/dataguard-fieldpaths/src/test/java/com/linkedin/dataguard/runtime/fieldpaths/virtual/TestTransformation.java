package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.linkedin.dataguard.runtime.transport.java.JavaTypeDataProvider;
import com.linkedin.dataguard.runtime.transport.java.types.JavaIntegerType;
import com.linkedin.dataguard.runtime.transport.java.types.JavaStringType;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.ArrayIndexOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.ArrayTransformOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Enforcer;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.MapKeyLookup;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Operator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.PredicatedArrayOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.StructFieldAccess;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerComparisonExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerFieldPathExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.transport.java.JavaUtil.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerBaseElement.*;
import static com.linkedin.dataguard.runtime.transport.java.data.JavaStruct.*;
import static org.junit.jupiter.api.Assertions.*;


public class TestTransformation {

  private static FormatSpecificTypeDataProvider typeDataProvider;
  private static StdFactory factory;
  private static StdType testingTableSchema;
  private static StdStruct testingTableRecord;

  private static DataGuardPathOperations dataguardPathOperations;

  @BeforeAll
  public static void setup() {
    typeDataProvider = new JavaTypeDataProvider();
    factory = typeDataProvider.getStdFactory();
    dataguardPathOperations = new DataGuardPathOperations(typeDataProvider);
    testingTableSchema = factory.createStdType(""
        + "row("
        + "   a integer, "
        + "   b row(f0 integer, f1 boolean),"
        + "   c array(row(f0 integer, f1 array(varchar))),"
        + "   d map(varchar, row(f0 integer, f1 array(varchar), f2 row(f3 integer, f4 integer))),"
        + "   e map(varchar, integer),"
        + "   f array(array(integer)))");
    testingTableRecord = (StdStruct) createTestingRecord();
  }

  @Test
  public void testEnforcerCreation() {
    checkEnforcer("$.a", field("a"));
    checkEnforcer("$.b.f0", field("b"), field("f0"));
    checkEnforcer("$.c[5]", field("c"), arrayGet(5, (StdArrayType) getSchema("c")));
    checkEnforcer("$.c[:].f0",
        field("c"),
        arrayTransform(
            new EnforcerFieldPathExpression(
                PREDICATE_CURRENT_ELEMENT,
                new Enforcer(field("f0")),
                factory.createStdType("integer")),
            getSchema("c")));
    checkEnforcer("$.c[:].f1[3]",
        field("c"),
        arrayTransform(
            new EnforcerFieldPathExpression(
                PREDICATE_CURRENT_ELEMENT,
                new Enforcer(field("f1"), arrayGet(3, (StdArrayType) factory.createStdType("array(varchar)"))),
                factory.createStdType("varchar")),
            getSchema("c")));
    checkEnforcer("$.d['k'].f2.f3",
        field("d"), mapGet(new JavaStringType(), "k"), field("f2"), field("f3"));

    checkEnforcer("$.c[?(@.f0 == 2)][:].f1",
        field("c"),
        new PredicatedArrayOperator(
            new EnforcerComparisonExpr(
                new EnforcerFieldPathExpression(
                    PREDICATE_CURRENT_ELEMENT,
                    new Enforcer(field("f0")),
                    factory.createStdType("integer")),
                enforcerLiteral(new JavaIntegerType(), 2),
                ComparisonOperator.EQUALS, factory),
            (StdArrayType) getSchema("c"), factory),
        arrayTransform(
            new EnforcerFieldPathExpression(
                PREDICATE_CURRENT_ELEMENT,
                new Enforcer(field("f1")),
                factory.createStdType("varchar")),
            factory.createStdType("array(varchar)")));
  }

  private EnforcerLiteral enforcerLiteral(StdType stdType, Object object) {
    return new EnforcerLiteral(stdType, object, createStdData(object, factory));
  }

  private StdType getSchema(String topLevelColumn) {
    if (topLevelColumn.length() > 1) {
      throw new RuntimeException("Not supported");
    }
    int index = topLevelColumn.charAt(0) - 'a';
    return ((StdStructType) testingTableSchema).fieldTypes().get(index);
  }

  @Test
  public void testExtraction() {
    checkExtract("$.a", 5);
    checkExtract("$.b", structInfo(ImmutableList.of("f0", "f1"), 10, true));
    checkExtractWithRowSelector("[?($.a == 0)]", "$.b", null);
    checkExtractWithRowSelector("", "[?($.a == 0)]$.b", null);
    checkExtractWithRowSelector("[?($.a == 5)]", "$.b", structInfo(ImmutableList.of("f0", "f1"), 10, true));
    checkExtractWithRowSelector("", "[?($.a == 5)]$.b", structInfo(ImmutableList.of("f0", "f1"), 10, true));
    checkExtract("$.b.f1", true);
    checkExtract("$.c[:].f0", ImmutableList.of(15, 20));
    checkExtract("$.c[500]", null);
    checkExtract("$.c[1].f1[0]", "rose");
    checkExtract("$.c[:].f1", ImmutableList.of(ImmutableList.of("f00", "bar", "baz"), ImmutableList.of("rose", "bar")));
    checkExtract("$.d['missingkey'].f1", null);
    checkExtract("$.d['mykey'].f1[1]", "toyota");
    checkExtract("$.e['ca']", 11);

    checkExtract("$.c[?(@.f0 == 15)][:].f1[?(@ == 'bar')]",
        ImmutableList.of(ImmutableList.of("bar")));
    checkExtract("$.c[?(@.f0 > 30 && @.f0 < 90)][:].f1[?(@ == 'rose')]",
        ImmutableList.of());
    checkExtract("$.c[?(@.f0 == 40)]",
        ImmutableList.of());
    checkExtract("$.f[?(1 == 1)][:][?(@ > 39 && @ < 46)]",
        ImmutableList.of(ImmutableList.of(40), ImmutableList.of(45)));
  }

  /**
   * Create a test record with the same schema as {@code testingTableSchema}
   * @return
   */
  private static StdData createTestingRecord() {
    return createStdData(
        structInfo(
            ImmutableList.of("a", "b", "c", "d", "e", "f"),
            //a
            5,
            //b
            structInfo(ImmutableList.of("f0", "f1"), 10, true),
            //c
            ImmutableList.of(
                structInfo(
                    ImmutableList.of("f0", "f1"),
                    15, ImmutableList.of("f00", "bar", "baz")),
                structInfo(
                    ImmutableList.of("f0", "f1"),
                    20, ImmutableList.of("rose", "bar"))),
            //d
            ImmutableMap.of("mykey",
                structInfo(
                    ImmutableList.of("f0", "f1", "f2"),
                    25,
                    ImmutableList.of("honda", "toyota", "bmw"),
                    structInfo(ImmutableList.of("f3", "f4"), 30, 40))),
            //e
            ImmutableMap.of("ny", 3, "ca", 11),
            //f
            ImmutableList.of(
                ImmutableList.of(35, 40),
                ImmutableList.of(45, 50, 55))), factory);
  }

  @Test
  public void testExtractionOnRecordWithNulls() {
    StdData recordWithNulls = createTestingRecordWithNulls();
    checkExtract("$.a", null, recordWithNulls);
    checkExtract("$.b.f1", null, recordWithNulls);
    checkExtractWithRowSelector("[?($.a == 0)]", "$.b.f1", null, recordWithNulls);
    checkExtract("$.c[:].f1", new ArrayList<>(Arrays.asList(null, Arrays.asList("rose", null))), recordWithNulls);
    checkExtract("$.c[:].f1", new ArrayList<>(Arrays.asList(null, Arrays.asList("rose", null))), recordWithNulls);
    checkExtract("$.d['mykey'].f1[?(@ == 'foo')]", null, recordWithNulls);
    checkExtract("$.d['mykey'].f2.f4", null, recordWithNulls);
    checkExtract("$.d['nonexistentkey'].f2.f4", null, recordWithNulls);
    checkExtract("$.d['nonexistentkey']", null, recordWithNulls);
    checkExtract("$.e['ny']", null, recordWithNulls);
    checkExtract("$.f[:][?(@ == 45)]", new ArrayList<>(Arrays.asList(null, ImmutableList.of(), ImmutableList.of(45))), recordWithNulls);
    checkExtract("$.f[?(@[0] == 45)]", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(45, null, 55)))), recordWithNulls);
    checkExtract("$.c[:].f1[?(@ == 'rose')]", new ArrayList<>(Arrays.asList(null, ImmutableList.of("rose"))), recordWithNulls);
  }

  @Test
  public void testTransformOnRecordWithNulls() {
    // In all the testcases below , paths are relative to one of the columns, and not the record. This is done to
    // avoid reconstructing large root object which reduces the readability of tests.

    // e.g. in this test, the path "$" is relative to column "a".
    // $ is null, and it's replaced with the replacement value (as the path points to $ to be redacted)
    checkTransformOnRecordWithNulls("$", "a", "blahValue", "blahValue");
    checkTransformOnRecordWithNulls("[?($ == 0)]$", "a", "blahValue", "blahValue");
    checkTransformWithRowSelectorOnRecordWithNulls("[?($ == 0)]", "$", "a", "blahValue", "blahValue");

    // since b is null, the object after applying action on $.f1 is also null
    checkTransformOnRecordWithNulls("$.f1", "b", false, null);
    checkTransformWithRowSelectorOnRecordWithNulls("[?($.f0 == 0)]", "$.f1", "b", false, null);

    // f1 is replaced with an array of "somevalue"
    checkTransformOnRecordWithNulls("$[:].f1", "c", new ArrayList<>(Arrays.asList("somevalue")),
        ImmutableList.of(
            structInfo(ImmutableList.of("f0", "f1"), 15, new ArrayList<>(Arrays.asList("somevalue"))),
            structInfo(ImmutableList.of("f0", "f1"), null, new ArrayList<>(Arrays.asList("somevalue")))));

    checkTransformOnRecordWithNulls("$[:].f1[:]", "c", "blahvalue",
        // every element within the array is replaced by "blahvalue". For null array, this is a no-op.
        ImmutableList.of(structInfo(ImmutableList.of("f0", "f1"), 15, null),
            structInfo(ImmutableList.of("f0", "f1"), null, new ArrayList<>(Arrays.asList("blahvalue", "blahvalue")))));

    checkTransformOnRecordWithNulls("$['mykey'].f1[?(@ == 'foo')]", "d", new ArrayList<>(),
        // no change since the key value is null
        new HashMap<String, StructInfo>() {{ put("mykey", null); }});

    checkTransformOnRecordWithNulls("$['nonexistentkey'].f2.f4", "d", 5000,
        // no change since the key does not exist
        new HashMap<String, StructInfo>() {{ put("mykey", null); }});

    checkTransformOnRecordWithNulls("$['ny']", "e", -999,
        // key exists, so the value is replaced.
        new HashMap<String, Integer>() {{
          put("ny", -999);
          put("ca", 11);
        }});

    checkTransformOnRecordWithNulls("$['nonexistentkey']", "e", -999,
        // the key does not exist. So the enforcement is ignored.
        new HashMap<String, Integer>() {{
          put("ny", null);
          put("ca", 11);
        }});

    checkTransformOnRecordWithNulls("$[:][?(@ == 45)]", "f", -999,
        // replace values for the array element with value 45
        Arrays.asList(new List[]{null, Arrays.asList(310), Arrays.asList(-999, null, 55)}));

    checkTransformOnRecordWithNulls("$[?(@[0] == 45)]", "f", new ArrayList<>(Arrays.asList(-999, -999)),
        // the whole array for which 0th index is 45 should be replaced
        Arrays.asList(new List[]{null, Arrays.asList(310), Arrays.asList(-999, -999)}));

    checkTransformOnRecordWithNulls("$[:].f1[?(@ == 'rose')]", "c", "lily",
        ImmutableList.of(structInfo(ImmutableList.of("f0", "f1"), 15, null),
            structInfo(ImmutableList.of("f0", "f1"), null, Arrays.asList(new String[]{"lily", null}))));

    checkTransformWithRowSelectorOnRecordWithNulls("[?($['ny'] == 0)]", "$['ca']", "e", -999,
        // null key doesn't match condition, so the value is replaced.
        new HashMap<String, Integer>() {{
          put("ny", null);
          put("ca", -999);
        }});
  }

  private void checkTransformOnRecordWithNulls(String path, String rootColumn, Object replacementValue, Object expectedTransformed) {
    checkTransformWithRowSelectorOnRecordWithNulls(null, path, rootColumn, replacementValue, expectedTransformed);
  }

  private void checkTransformWithRowSelectorOnRecordWithNulls(String mountPoint, String path, String rootColumn, Object replacementValue, Object expectedTransformed) {
    StdStruct record = (StdStruct) createTestingRecordWithNulls();
    PlatformData columValue = ((PlatformData) record.getField(rootColumn));
    checkTransformWithRowSelector(mountPoint, path, getSchema(rootColumn),
        columValue == null ? null : columValue.getUnderlyingData(), replacementValue, expectedTransformed);
  }


  /**
   * @return a test record with the same schema as {@code testingTableSchema}, with nulls inserted in various places
   */
  private static StdData createTestingRecordWithNulls() {
    return createStdData(
        structInfo(
            ImmutableList.of("a", "b", "c", "d", "e", "f"),
            //a
            null,
            //b
            null,
            //c
            ImmutableList.of(
                structInfo(
                    ImmutableList.of("f0", "f1"),
                    15, null),
                structInfo(
                    ImmutableList.of("f0", "f1"),
                    null, Arrays.asList(new String[]{"rose", null}))),
            //d
            new HashMap<String, StructInfo>() {{
              put("mykey", null);
            }},
            //e
            new HashMap<String, Integer>() {{
              put("ny", null);
              put("ca", 11);
            }},
            //f
            Arrays.asList(new List[]{
                null,
                Arrays.asList(310),
                Arrays.asList(45, null, 55)})), factory);
  }

  private void checkExtract(String path, Object expected) {
    checkExtract(path,expected, testingTableRecord);
  }

  private void checkExtract(String path, Object expected, StdData testingInputRecord) {
    checkExtractWithRowSelector(null, path, expected, testingInputRecord);
  }

  private void checkExtractWithRowSelector(String mountPoint, String path, Object expected) {
    checkExtractWithRowSelector(mountPoint, path, expected, testingTableRecord);
  }

  private void checkExtractWithRowSelector(String mountPoint, String path, Object expected, StdData testingInputRecord) {
    Enforcer enforcer = dataguardPathOperations.createEnforcer(Optional.ofNullable(mountPoint), path, testingTableSchema);
    StdData actualStdData = enforcer.extract(testingInputRecord, testingInputRecord);
    Object actual = actualStdData == null ? null : ((PlatformData) actualStdData).getUnderlyingData();
    assertEquals(actual, expected);
  }

  @Test
  public void testEnforcement() {
    checkTransform("$.x",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), 50, "hi"),
        -999,
        structInfo(ImmutableList.of("x", "y"), -999, "hi"));

    checkTransformWithRowSelector("[?($.y == 'hi')]","$.x",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), 50, "hi"),
        -999,
        structInfo(ImmutableList.of("x", "y"), -999, "hi"));

    checkTransformWithRowSelector("[?($.y IN ['hi', 'hello'])]","$.x",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), 50, "hi"),
        -999,
        structInfo(ImmutableList.of("x", "y"), -999, "hi"));

    checkTransformWithRowSelector("[?($.y == 'ho')]","$.x",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), 50, "hi"),
        -999,
        structInfo(ImmutableList.of("x", "y"), 50, "hi"));

    checkTransformWithRowSelector("[?($.y == 'hi')]","$",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), 50, "hi"),
        null,
        null);

    checkTransformWithRowSelector("[?($.x > 0)]","$.x",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), 50, "hi"),
        -999,
        structInfo(ImmutableList.of("x", "y"), -999, "hi"));

    checkTransformWithRowSelector("[?($.x > 0)]","$.x",
        factory.createStdType("row(x integer, y varchar)"),
        structInfo(ImmutableList.of("x", "y"), -1, "hi"),
        -999,
        structInfo(ImmutableList.of("x", "y"), -1, "hi"));

    checkTransform("$.x.w",
        factory.createStdType("row(x row(z integer, w varchar, v double), y varchar)"),
        structInfo(ImmutableList.of("x", "y"),
            structInfo(ImmutableList.of("z", "w", "v"), 400, "john", 3.5), "hi"),
        "dataguardReplacementValue",
        structInfo(ImmutableList.of("x", "y"),
            structInfo(ImmutableList.of("z", "w", "v"), 400, "dataguardReplacementValue", 3.5), "hi"));

    checkTransform("$[:].x.y",
        factory.createStdType("array(row(x row(y integer, z varchar), w varchar))"),
        ImmutableList.of(
            structInfo(ImmutableList.of("x", "w"),
              structInfo(ImmutableList.of("y", "z"), 5555, 7777),
              "wValue"),
            structInfo(ImmutableList.of("x", "w"),
                structInfo(ImmutableList.of("y", "z"), 9999, 8888),
                "wValue2")),
        -9999,
        ImmutableList.of(
            structInfo(ImmutableList.of("x", "w"),
                structInfo(ImmutableList.of("y", "z"), -9999, 7777),
                "wValue"),
            structInfo(ImmutableList.of("x", "w"),
                structInfo(ImmutableList.of("y", "z"), -9999, 8888),
                "wValue2")));

    checkTransform("$[1].z",
        factory.createStdType("array(row(y integer, z varchar))"),
        ImmutableList.of(
            structInfo(ImmutableList.of("y", "z"), 123, "zValue"),
            structInfo(ImmutableList.of("y", "z"), 543, "zValue2"),
            structInfo(ImmutableList.of("y", "z"), 100, "zValue3")),
        "dataguardReplacementValueFoo",
        ImmutableList.of(
            structInfo(ImmutableList.of("y", "z"), 123, "zValue"),
            structInfo(ImmutableList.of("y", "z"), 543, "dataguardReplacementValueFoo"),
            structInfo(ImmutableList.of("y", "z"), 100, "zValue3")));

    checkTransform("$.c0[?(@.x == 60 || (@.x == 50 && @.y == 'value1') || @.z > 100.0)][:].y",
        factory.createStdType("row(c0 array(row(x integer, y varchar, z double)), c1 integer)"),
        structInfo(
            ImmutableList.of("c0", "c1"),
            ImmutableList.of(
                structInfo(ImmutableList.of("x", "y", "z"), 50, "value1", 30.5),
                structInfo(ImmutableList.of("x", "y", "z"), 60, "value2", 90.8),
                structInfo(ImmutableList.of("x", "y", "z"), 90, "value3", 30.4),
                structInfo(ImmutableList.of("x", "y", "z"), 90, "value4", 800.9)),
            6),
        "fooReplacementValuedataguard",
        structInfo(
            ImmutableList.of("c0", "c1"),
            ImmutableList.of(
                structInfo(ImmutableList.of("x", "y", "z"), 50, "fooReplacementValuedataguard", 30.5),
                structInfo(ImmutableList.of("x", "y", "z"), 60, "fooReplacementValuedataguard", 90.8),
                structInfo(ImmutableList.of("x", "y", "z"), 90, "value3", 30.4),
                structInfo(ImmutableList.of("x", "y", "z"), 90, "fooReplacementValuedataguard", 800.9)),
            6));

    checkTransform("$.c0['k2'][?(@ > 10)]",
        factory.createStdType("row(c0 map(varchar, array(integer)))"),
        structInfo(
            ImmutableList.of("c0"),
            ImmutableMap.of(
                "k1", ImmutableList.of(5, 10, 15),
                "k2", ImmutableList.of(6, 11, 16),
                "k3", ImmutableList.of(7, 12, 17))),
        -42,
        structInfo(
            ImmutableList.of("c0"),
            ImmutableMap.of(
                "k1", ImmutableList.of(5, 10, 15),
                "k2", ImmutableList.of(6, -42, -42),
                "k3", ImmutableList.of(7, 12, 17))));

    checkTransform("$.fdsMap['blah'].values[getFDSIndex1D($.fdsMap['blah'].indices0, 'myfilter')]",
        factory.createStdType("row(fdsMap map(varchar, row(indices0 array(varchar), values array(integer))))"),
        structInfo(
            ImmutableList.of("fdsMap"),
            ImmutableMap.of(
                "blah",
                structInfo(ImmutableList.of("indices0", "values"),
                    ImmutableList.of(ImmutableList.of("foo", "myfilter", "bar"), ImmutableList.of(25, 111, 35))),
                "blah234",
                structInfo(ImmutableList.of("indices0", "values"),
                    ImmutableList.of(ImmutableList.of("foo", "myfilter", "bar"), ImmutableList.of(25, 111, 35))))),
        -9999,
        structInfo(
            ImmutableList.of("fdsMap"),
            ImmutableMap.of(
                "blah",
                structInfo(ImmutableList.of("indices0", "values"),
                    ImmutableList.of(ImmutableList.of("foo", "myfilter", "bar"), Arrays.asList(new Integer[]{25, -9999, 35}))),
                "blah234",
                structInfo(ImmutableList.of("indices0", "values"),
                    ImmutableList.of(ImmutableList.of("foo", "myfilter", "bar"), ImmutableList.of(25, 111, 35))))));
  }

  private void checkTransform(
      String path,
      StdType rootType,
      Object input,
      Object replacementValue,
      Object expectedTransformed) {
    checkTransformWithRowSelector(null, path, rootType, input, replacementValue, expectedTransformed);
  }

  private void checkTransformWithRowSelector(
      String mountPoint,
      String path,
      StdType rootType,
      Object input,
      Object replacementValue,
      Object expectedTransformed) {
    Enforcer enforcer = dataguardPathOperations.createEnforcer(Optional.ofNullable(mountPoint), path, rootType);
    StdData rootObject = createStdData(input, factory);
    StdData replacementValueStdData = createStdData(replacementValue, factory);
    StdData actual = enforcer.applyAction(rootObject, new EraseAction(replacementValueStdData), rootObject);
    assertEquals(
        actual == null ? null : ((PlatformData) actual).getUnderlyingData(),
        expectedTransformed);
  }

  private void checkEnforcer(String fieldPath, Operator... operators) {
    Enforcer enforcer = dataguardPathOperations.createEnforcer(Optional.empty(), fieldPath, testingTableSchema);
    System.out.println(enforcer);
    assertEquals(enforcer,
        new Enforcer(Arrays.asList(operators)));
  }

  private static ArrayIndexOperator arrayGet(int i, StdArrayType type) {
    return new ArrayIndexOperator(i, factory, type);
  }

  private MapKeyLookup mapGet(StdType keyType, Object key) {
    return new MapKeyLookup(enforcerLiteral(keyType, key));
  }

  private static ArrayTransformOperator arrayTransform(EnforcerFieldPathExpression enforcer, StdType type) {
    return new ArrayTransformOperator(enforcer, factory, (StdArrayType) type);
  }

  private static StructFieldAccess field(String field) {
    return new StructFieldAccess(field);
  }
}
