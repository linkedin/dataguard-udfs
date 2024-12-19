package com.linkedin.dataguard.runtime.benchmark.spark;

import com.linkedin.dataguard.runtime.benchmark.common.BenchmarkingUtils;
import com.linkedin.dataguard.runtime.benchmark.common.DataGenerator;
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static com.linkedin.dataguard.runtime.benchmark.common.BenchmarkingUtils.*;
import static com.linkedin.dataguard.runtime.benchmark.spark.SparkBenchmarkingUtil.*;


/**
 * A microbenchmark to validate performance of Spark UDF executions
 */
@Fork(1)
@Warmup(iterations = 10, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 15, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SparkDataGuardEnforcementBenchmark {

  private static final int SEED = 1234;
  private static final DataGenerator SPARK_DATA_GENERATOR = new DataGenerator(new NullableSparkTypeDataProvider(), Optional.of(SEED));

  public static void main(String[] args) throws Exception {
    runBenchmark(SparkDataGuardEnforcementBenchmark.class);
  }

  @Benchmark
  public List<Object> benchmarkEnforcement(BenchmarkInput input) {
    return input.records.stream()
        .map(record -> ((PlatformData) record).getUnderlyingData())
        .map(data -> {
          List<Object> outputs = new ArrayList<>();
          for (int i = 0; i < input.udfCalls.size(); i++) {
            outputs.add(input.udfCalls.get(i).eval((InternalRow) data));
          }
          return outputs;
        })
        .collect(Collectors.toList());
  }

  @State(Scope.Thread)
  public static class BenchmarkInput {

    // Number of records to be generated
    @Param({"10000"})
    private int recordCount = 10000;

    // Benchmarking Scenarios, these should be present in BenchmarkingUtils.ENFORCEMENT_PARAMS_MAP
    @Param({
        "STRING_ENFORCEMENT_ONE_FIELD",// "STRING_ENFORCEMENT_TWO_FIELDS", "STRING_ENFORCEMENT_THREE_FIELDS", "STRING_ENFORCEMENT_FOUR_FIELDS", "STRING_ENFORCEMENT_FIVE_FIELDS",
//        "FIELD_DEPTH_VARIATION_1", "FIELD_DEPTH_VARIATION_2", "FIELD_DEPTH_VARIATION_3", "FIELD_DEPTH_VARIATION_4", "FIELD_DEPTH_VARIATION_5",
//        "NESTED_UDF_CALL_VARIATION_1", "NESTED_UDF_CALL_VARIATION_2", "NESTED_UDF_CALL_VARIATION_3", "NESTED_UDF_CALL_VARIATION_4", "NESTED_UDF_CALL_VARIATION_5",
//        "STRUCT_WIDTH_VARIATION_1", "STRUCT_WIDTH_VARIATION_2", "STRUCT_WIDTH_VARIATION_3", "STRUCT_WIDTH_VARIATION_4", "STRUCT_WIDTH_VARIATION_5",
//        "STRUCT_WIDTH_VARIATION_1_TOPLEVEL", "STRUCT_WIDTH_VARIATION_2_TOPLEVEL", "STRUCT_WIDTH_VARIATION_3_TOPLEVEL", "STRUCT_WIDTH_VARIATION_4_TOPLEVEL", "STRUCT_WIDTH_VARIATION_5_TOPLEVEL",
//        "STRUCT_DEPTH_VARIATION_1", "STRUCT_DEPTH_VARIATION_2", "STRUCT_DEPTH_VARIATION_3", "STRUCT_DEPTH_VARIATION_4", "STRUCT_DEPTH_VARIATION_5",
//        "ARRAY1D_10_REDACTION_PRIMARY", "ARRAY1D_10_REDACTION_SECONDARY", "ARRAY1D_20_REDACTION_PRIMARY", "ARRAY1D_20_REDACTION_SECONDARY", "ARRAY1D_30_REDACTION_PRIMARY", "ARRAY1D_30_REDACTION_SECONDARY", "ARRAY1D_40_REDACTION_PRIMARY", "ARRAY1D_40_REDACTION_SECONDARY"
    })
    private String enforcementSetup = "STRUCT_WIDTH_VARIATION_1";

    // What % of entries should be redacted
    @Param({"0.0", "1.0"})
    private float percentRedaction;

    // Distinct values to use for populating any primitive field
    @Param({"10"})
    private int distinctElementValues;

    // Use case-when instead of redaction UDF. This only works for scenarios where primary redaction is applied on a
    // top-level field.
    @Param({"false"})
    private boolean useCaseWhen;

    private List<StdData> records;

    private List<Expression> udfCalls;

    @Setup(Level.Trial)
    public void setupRecords() {
      this.records = BenchmarkingUtils.setupRecords(enforcementSetup, SPARK_DATA_GENERATOR, recordCount,
          distinctElementValues);
      EnforcementParams enforcementParams = ENFORCEMENT_PARAMS_MAP.get(enforcementSetup);
      this.udfCalls = createExpressions(enforcementParams, percentRedaction, useCaseWhen);
    }
  }
}