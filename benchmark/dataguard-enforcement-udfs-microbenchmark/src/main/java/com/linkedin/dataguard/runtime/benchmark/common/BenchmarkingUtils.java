package com.linkedin.dataguard.runtime.benchmark.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.linkedin.transport.api.data.StdData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.openjdk.jmh.profile.AsyncProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.lang.String.*;


/**
 * Generic format-agnostic utilities for benchmarking
 */
public class BenchmarkingUtils {

  private BenchmarkingUtils() {
  }

  private static final String FLAT_SCHEMA =
      "row(string_field_1 varchar, " + "string_field_2 varchar, " + "string_field_3 varchar, " + "string_field_4 varchar, "
          + "string_field_5 varchar)";

  private static final String DEEP_STRUCT_1_SCHEMA = "row(deep_struct_field_1 row(string_field_1 varchar))";
  private static final String DEEP_STRUCT_2_SCHEMA =
      "row(deep_struct_field_2 row(" + "string_field_1 varchar, deep_struct_field_1 row(" + "string_field_1 varchar)))";
  private static final String DEEP_STRUCT_3_SCHEMA =
      "row(deep_struct_field_3 row(" + "string_field_1 varchar, deep_struct_field_2 row(" + "string_field_1 varchar, deep_struct_field_1 row("
          + "string_field_1 varchar))))";
  private static final String DEEP_STRUCT_4_SCHEMA =
      "row(deep_struct_field_4 row(" + "string_field_1 varchar, deep_struct_field_3 row(" + "string_field_1 varchar, deep_struct_field_2 row("
          + "string_field_1 varchar, deep_struct_field_1 row(" + "string_field_1 varchar)))))";
  private static final String DEEP_STRUCT_5_SCHEMA =
      "row(deep_struct_field_5 row(" + "string_field_1 varchar, deep_struct_field_4 row(" + "string_field_1 varchar, deep_struct_field_3 row("
          + "string_field_1 varchar, deep_struct_field_2 row(" + "string_field_1 varchar, deep_struct_field_1 row(" + "string_field_1 varchar))))))";

  private static final String WIDE_SHALLOW_STRUCT_1_SCHEMA =
      "row(wide_shallow_struct_field_1 row(string_field_1 varchar))";
  private static final String WIDE_SHALLOW_STRUCT_2_SCHEMA =
      "row(wide_shallow_struct_field_2 row(string_field_1 varchar, string_field_2 varchar))";
  private static final String WIDE_SHALLOW_STRUCT_3_SCHEMA =
      "row(wide_shallow_struct_field_3 row(string_field_1 varchar, string_field_2 varchar, string_field_3 varchar))";
  private static final String WIDE_SHALLOW_STRUCT_4_SCHEMA =
      "row(wide_shallow_struct_field_4 row(string_field_1 varchar, string_field_2 varchar, string_field_3 varchar, "
          + "string_field_4 varchar))";
  private static final String WIDE_SHALLOW_STRUCT_5_SCHEMA =
      "row(wide_shallow_struct_field_5 row(string_field_1 varchar, string_field_2 varchar, string_field_3 varchar, "
          + "string_field_4 varchar, string_field_5 varchar))";

  private static final String ARRAY1D_TEMPLATE = "row(array_field_%dentries array(row(string_field_1 varchar, string_field_2 varchar)))";
  private static final String ARRAY1D_10_ENTRIES_SCHEMA = format(ARRAY1D_TEMPLATE, 10);
  private static final String ARRAY1D_20_ENTRIES_SCHEMA = format(ARRAY1D_TEMPLATE, 20);
  private static final String ARRAY1D_30_ENTRIES_SCHEMA = format(ARRAY1D_TEMPLATE, 30);
  private static final String ARRAY1D_40_ENTRIES_SCHEMA = format(ARRAY1D_TEMPLATE, 40);
  private static final Map<Integer, String> ARRAY1D_SCHEMAS = ImmutableMap.<Integer, String>builder()
      .put(10, ARRAY1D_10_ENTRIES_SCHEMA)
      .put(20, ARRAY1D_20_ENTRIES_SCHEMA)
      .put(30, ARRAY1D_30_ENTRIES_SCHEMA)
      .put(40, ARRAY1D_40_ENTRIES_SCHEMA)
      .build();

  public static final Map<String, EnforcementParams> ENFORCEMENT_PARAMS_MAP = ImmutableMap.<String, EnforcementParams>builder()
      .putAll(getFlatSchemaEnforcementParams())
      .putAll(getFieldPathDepthVariationEnforcementParams())
      .putAll(getNestedUdfCallVariationEnforcementParams())
      .putAll(getStructWidthVariationEnforcementParams())
      .putAll(getStructDepthVariationEnforcementParams())
      .putAll(getStructWidthVariationEnforcementParamsTopLevel())
      .putAll(getArrayRedactionEnforcementParams())
      .build();

  private static Map<String, EnforcementParams> getStructDepthVariationEnforcementParams() {
    return ImmutableMap.<String, EnforcementParams>builder()
        .put("STRUCT_DEPTH_VARIATION_1",
            new EnforcementParams(DEEP_STRUCT_1_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_1", ImmutableList.of("deep_struct_field_1.string_field_1"))
                    .build()))
        .put("STRUCT_DEPTH_VARIATION_2",
            new EnforcementParams(DEEP_STRUCT_2_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_2", ImmutableList.of("deep_struct_field_2.string_field_1"))
                    .build()))
        .put("STRUCT_DEPTH_VARIATION_3",
            new EnforcementParams(DEEP_STRUCT_3_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_3", ImmutableList.of("deep_struct_field_3.string_field_1"))
                    .build()))
        .put("STRUCT_DEPTH_VARIATION_4",
            new EnforcementParams(DEEP_STRUCT_4_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_4", ImmutableList.of("deep_struct_field_4.string_field_1"))
                    .build()))
        .put("STRUCT_DEPTH_VARIATION_5",
            new EnforcementParams(DEEP_STRUCT_5_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_5", ImmutableList.of("deep_struct_field_5.string_field_1"))
                    .build()))
        .build();
  }

  /**
   * Vary the width of the struct, from which a string field is being redacted
   */
  private static Map<String,? extends EnforcementParams> getStructWidthVariationEnforcementParams() {
    return ImmutableMap.<String, EnforcementParams>builder()
        .put("STRUCT_WIDTH_VARIATION_1",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_1_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_1",
                    ImmutableList.of("wide_shallow_struct_field_1.string_field_1"))))
        .put("STRUCT_WIDTH_VARIATION_2",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_2_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_2",
                    ImmutableList.of("wide_shallow_struct_field_2.string_field_1"))))
        .put("STRUCT_WIDTH_VARIATION_3",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_3_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_3",
                    ImmutableList.of("wide_shallow_struct_field_3.string_field_1"))))
        .put("STRUCT_WIDTH_VARIATION_4",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_4_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_4",
                    ImmutableList.of("wide_shallow_struct_field_4.string_field_1"))))
        .put("STRUCT_WIDTH_VARIATION_5",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5.string_field_1"))))
        .build();
  }

  /**
   * Vary the size of the struct being redacted
   */
  private static Map<String,? extends EnforcementParams> getStructWidthVariationEnforcementParamsTopLevel() {
    return ImmutableMap.<String, EnforcementParams>builder()
        .put("STRUCT_WIDTH_VARIATION_1_TOPLEVEL",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_1_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_1",
                    ImmutableList.of("wide_shallow_struct_field_1"))))
        .put("STRUCT_WIDTH_VARIATION_2_TOPLEVEL",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_2_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_2",
                    ImmutableList.of("wide_shallow_struct_field_2"))))
        .put("STRUCT_WIDTH_VARIATION_3_TOPLEVEL",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_3_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_3",
                    ImmutableList.of("wide_shallow_struct_field_3"))))
        .put("STRUCT_WIDTH_VARIATION_4_TOPLEVEL",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_4_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_4",
                    ImmutableList.of("wide_shallow_struct_field_4"))))
        .put("STRUCT_WIDTH_VARIATION_5_TOPLEVEL",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5"))))
        .build();
  }

  /**
   * Vary the number of UDF invocations on the column with type WIDE_SHALLOW_STRUCT_5_SCHEMA
   */
  private static Map<String, EnforcementParams> getNestedUdfCallVariationEnforcementParams() {
    return ImmutableMap.<String, EnforcementParams>builder()
        .put("NESTED_UDF_CALL_VARIATION_1",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5.string_field_1"))))
        .put("NESTED_UDF_CALL_VARIATION_2",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5.string_field_1",
                        "wide_shallow_struct_field_5.string_field_2"))))
        .put("NESTED_UDF_CALL_VARIATION_3",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5.string_field_1",
                        "wide_shallow_struct_field_5.string_field_2",
                        "wide_shallow_struct_field_5.string_field_3"))))
        .put("NESTED_UDF_CALL_VARIATION_4",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5.string_field_1",
                        "wide_shallow_struct_field_5.string_field_2",
                        "wide_shallow_struct_field_5.string_field_3",
                        "wide_shallow_struct_field_5.string_field_4"))))
        .put("NESTED_UDF_CALL_VARIATION_5",
            new EnforcementParams(WIDE_SHALLOW_STRUCT_5_SCHEMA,
                ImmutableMap.of("wide_shallow_struct_field_5",
                    ImmutableList.of("wide_shallow_struct_field_5.string_field_1",
                        "wide_shallow_struct_field_5.string_field_2",
                        "wide_shallow_struct_field_5.string_field_3",
                        "wide_shallow_struct_field_5.string_field_4",
                        "wide_shallow_struct_field_5.string_field_5"))))
        .build();
  }

  /**
   * Vary the depth of fieldpath to be redacted, on the same DEEP_STRUCT_5_SCHEMA schema
   */
  private static Map<String, EnforcementParams> getFieldPathDepthVariationEnforcementParams() {

    return ImmutableMap.<String, EnforcementParams>builder()
        .put("FIELD_DEPTH_VARIATION_1",
            new EnforcementParams(DEEP_STRUCT_5_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_5", ImmutableList.of("deep_struct_field_5.string_field_1"))
                    .build()))
        .put("FIELD_DEPTH_VARIATION_2",
            new EnforcementParams(DEEP_STRUCT_5_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_5", ImmutableList.of("deep_struct_field_5.deep_struct_field_4.string_field_1"))
                    .build()))
        .put("FIELD_DEPTH_VARIATION_3",
            new EnforcementParams(DEEP_STRUCT_5_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_5", ImmutableList.of("deep_struct_field_5.deep_struct_field_4.deep_struct_field_3.string_field_1"))
                    .build()))
        .put("FIELD_DEPTH_VARIATION_4",
            new EnforcementParams(DEEP_STRUCT_5_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_5", ImmutableList.of("deep_struct_field_5.deep_struct_field_4.deep_struct_field_3.deep_struct_field_2.string_field_1"))
                    .build()))
        .put("FIELD_DEPTH_VARIATION_5",
            new EnforcementParams(DEEP_STRUCT_5_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("deep_struct_field_5", ImmutableList.of("deep_struct_field_5.deep_struct_field_4.deep_struct_field_3.deep_struct_field_2.deep_struct_field_1.string_field_1"))
                    .build()))
        .build();
  }

  /**
   * Vary the number string columns to be redacted
   */
  private static Map<String, EnforcementParams> getFlatSchemaEnforcementParams() {
    return ImmutableMap.<String, EnforcementParams>builder()
        .put("STRING_ENFORCEMENT_ONE_FIELD",
            new EnforcementParams(FLAT_SCHEMA,
                ImmutableMap.of("string_field_1", ImmutableList.of("string_field_1"))))
        .put("STRING_ENFORCEMENT_TWO_FIELDS",
            new EnforcementParams(FLAT_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("string_field_1", ImmutableList.of("string_field_1"))
                    .put("string_field_2", ImmutableList.of("string_field_2"))
                    .build()))
        .put("STRING_ENFORCEMENT_THREE_FIELDS",
            new EnforcementParams(FLAT_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("string_field_1", ImmutableList.of("string_field_1"))
                    .put("string_field_2", ImmutableList.of("string_field_2"))
                    .put("string_field_3", ImmutableList.of("string_field_3"))
                    .build()))
        .put("STRING_ENFORCEMENT_FOUR_FIELDS",
            new EnforcementParams(FLAT_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("string_field_1", ImmutableList.of("string_field_1"))
                    .put("string_field_2", ImmutableList.of("string_field_2"))
                    .put("string_field_3", ImmutableList.of("string_field_3"))
                    .put("string_field_4", ImmutableList.of("string_field_4"))
                    .build()))
        .put("STRING_ENFORCEMENT_FIVE_FIELDS",
            new EnforcementParams(FLAT_SCHEMA,
                ImmutableMap.<String, List<String>>builder()
                    .put("string_field_1", ImmutableList.of("string_field_1"))
                    .put("string_field_2", ImmutableList.of("string_field_2"))
                    .put("string_field_3", ImmutableList.of("string_field_3"))
                    .put("string_field_4", ImmutableList.of("string_field_4"))
                    .put("string_field_5", ImmutableList.of("string_field_5"))
                    .build()))
        .build();
  }

  private static Map<String,? extends EnforcementParams> getArrayRedactionEnforcementParams() {

    ImmutableMap.Builder<String, EnforcementParams> params = ImmutableMap.builder();
    List<Integer> arraySizes = ImmutableList.of(10, 20, 30, 40);

    for (int arraySize : arraySizes) {

      if (!ARRAY1D_SCHEMAS.containsKey(arraySize)) {
        throw new RuntimeException("Did not find array schema for size: " + arraySize);
      }

      params.put(
          format("ARRAY1D_%d_REDACTION_PRIMARY", arraySize),
          new EnforcementParams(ARRAY1D_SCHEMAS.get(arraySize),
              ImmutableMap.of(
                  format("array_field_%dentries", arraySize),
                  ImmutableList.of(format("array_field_%dentries.[type=struct].string_field_1", arraySize)))));
      params.put(
          format("ARRAY1D_%d_REDACTION_SECONDARY", arraySize),
          new EnforcementParams(ARRAY1D_SCHEMAS.get(arraySize),
              ImmutableMap.of(
                  format("array_field_%dentries", arraySize),
                  ImmutableList.of(format("$.array_field_%dentries[?(@.string_field_1 == 'jackfruit')][:].string_field_2", arraySize)))));
    }

    return params.build();
  }


  /**
   * Specifies the fieldpath and type information needed to construct redaction UDFs
   */
  public static class EnforcementParams {
    private final String tableSchemaTypeSignature;
    private final Map<String, List<String>> fieldPathsForColumns;

    public EnforcementParams(
        String tableSchemaTypeSignature,
        Map<String, List<String>> fieldPathsForColumns) {
      this.tableSchemaTypeSignature = tableSchemaTypeSignature;
      this.fieldPathsForColumns = fieldPathsForColumns;
    }

    public String getTableSchemaTypeSignature() {
      return tableSchemaTypeSignature;
    }

    public Map<String, List<String>> getFieldPathsForColumns() {
      return fieldPathsForColumns;
    }
  }

  public static void runBenchmark(Class<?> clazz) {
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MMM-dd-HH:mm");
    String workPath = System.getProperty("user.dir");
    String profilerPath = workPath + "/../../lib/libasyncProfiler.so";
    Date resultDate = new Date(System.currentTimeMillis());
    Options opt = new OptionsBuilder()
        .include(clazz.getSimpleName())
        .resultFormat(ResultFormatType.JSON)
        .result(workPath + "/../build/benchmark_with_async_prof_" + date.format(resultDate) + ".json")
        .addProfiler(AsyncProfiler.class, "libPath=" + profilerPath + ";output=flamegraph")
        .build();
    try {
      new Runner(opt).run();
    } catch (RunnerException e) {
      throw new RuntimeException("Failed to run benchmark: ", e);
    }
  }

  public static List<StdData> setupRecords(String enforcementSetup, DataGenerator dataGenerator, int recordCount, int distinctValues) {
    EnforcementParams params = ENFORCEMENT_PARAMS_MAP.get(enforcementSetup);
    if (params == null) {
      throw new RuntimeException("Bad params");
    }
    return dataGenerator.generateRandomRecords(params.getTableSchemaTypeSignature(), recordCount, distinctValues);
  }
}
