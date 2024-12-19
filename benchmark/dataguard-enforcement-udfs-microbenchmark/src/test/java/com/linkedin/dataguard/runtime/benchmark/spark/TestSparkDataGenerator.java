package com.linkedin.dataguard.runtime.benchmark.spark;

import com.linkedin.dataguard.runtime.benchmark.common.BenchmarkingUtils;
import com.linkedin.dataguard.runtime.benchmark.common.DataGenerator;
import com.linkedin.dataguard.runtime.benchmark.common.TestDataGenerator;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider;
import com.linkedin.dataguard.runtime.transport.spark.data.NullableSparkStruct;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.util.GenericArrayData;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.benchmark.common.BenchmarkingUtils.*;
import static com.linkedin.dataguard.runtime.benchmark.spark.SparkBenchmarkingUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class TestSparkDataGenerator extends TestDataGenerator {
  @Override
  public FormatSpecificTypeDataProvider getTypeDataProvider() {
    return new NullableSparkTypeDataProvider();
  }

  @Override
  public Class<?> getStructClass() {
    return NullableSparkStruct.class;
  }

  @Test
  public void testAllEnforcementParamsDataGeneration() {

    DataGenerator dataGenerator = new DataGenerator(
        new NullableSparkTypeDataProvider(),
        Optional.of(1234));

    for (Map.Entry<String, EnforcementParams> entry : ENFORCEMENT_PARAMS_MAP.entrySet()) {
      EnforcementParams params = entry.getValue();
      List<Expression> expressions = createExpressions(params, 1.0);
      List<StdData> xx = dataGenerator.generateRandomRecords(params.getTableSchemaTypeSignature(), 5, 1);
      StdData data = xx.get(0);
      InternalRow row = (InternalRow) ((PlatformData) data).getUnderlyingData();
      List<Object> outputs = new ArrayList<>();
      for (Expression expression : expressions) {
        outputs.add(expression.eval(row));
      }
    }
  }

  @Test
  public void testRedactFieldIf() {
    String enforcementSetup = "STRUCT_WIDTH_VARIATION_1";
    BenchmarkingUtils.EnforcementParams enforcementParams = ENFORCEMENT_PARAMS_MAP.get(enforcementSetup);

    List<Expression> expressions = createExpressions(enforcementParams, 0.5);

    DataGenerator dataGenerator = new DataGenerator(
        new NullableSparkTypeDataProvider(),
        Optional.of(1234));

    StdData data = dataGenerator.generateRandomRecords(enforcementParams.getTableSchemaTypeSignature(), 1).get(0);

    InternalRow row = (InternalRow) ((PlatformData) data).getUnderlyingData();
    for (Expression expression : expressions) {
      row = (InternalRow) expression.eval(row);
    }

    // validate the string can be accessed
    row.getString(0);
  }

  @Test
  public void testSecondarySchemaFieldIf() {
    String enforcementSetup = "ARRAY1D_10_REDACTION_SECONDARY";
    BenchmarkingUtils.EnforcementParams enforcementParams = ENFORCEMENT_PARAMS_MAP.get(enforcementSetup);

    List<Expression> expressions = createExpressions(enforcementParams, 1.0);

    DataGenerator dataGenerator = new DataGenerator(
        new NullableSparkTypeDataProvider(),
        Optional.of(1234));

    StdData data = dataGenerator.generateRandomRecords(enforcementParams.getTableSchemaTypeSignature(), 1).get(0);
    InternalRow row = (InternalRow) ((PlatformData) data).getUnderlyingData();
    Expression res = expressions.get(0);
    GenericArrayData outputData = (GenericArrayData) res.eval(row);
    assertEquals(outputData.array().length, 10);
  }

  @Test
  public void testUseCaseWhen() {
    String enforcementSetup = "STRING_ENFORCEMENT_ONE_FIELD";
    BenchmarkingUtils.EnforcementParams enforcementParams = ENFORCEMENT_PARAMS_MAP.get(enforcementSetup);

    List<Expression> expressions = createExpressions(enforcementParams, 1.0, true);
    DataGenerator dataGenerator = new DataGenerator(
        new NullableSparkTypeDataProvider(),
        Optional.of(1234));

    StdData data = dataGenerator.generateRandomRecords(enforcementParams.getTableSchemaTypeSignature(), 1).get(0);
    InternalRow row = (InternalRow) ((PlatformData) data).getUnderlyingData();
    Expression res = expressions.get(0);
    assertThat(res.eval(row)).isNull();
  }
}
