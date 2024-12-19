package com.linkedin.dataguard.runtime.udf.spark;

import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.linkedin.dataguard.runtime.udf.spark.UdfUtils.*;
import static org.assertj.core.api.Assertions.*;


public class TestRedactFieldifUDF {

  private static SparkSession _sparkSession;
  private static JSONParser _parser;
  private static final String TEST_DIR = "testRedactFieldIf";

  private static Stream<Arguments> provideTestArguments() {
    File[] testCases = new File(Resources.getResource(TEST_DIR).getFile()).listFiles();
    if (testCases == null || testCases.length == 0) {
      throw new RuntimeException("No test cases found");
    }
    return Arrays.stream(testCases).map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("provideTestArguments")
  public void testRedactIfForSingleTestCase(File testCase) throws IOException, ParseException {
    JSONObject jsonObject = (JSONObject) _parser.parse(
        FileUtils.readFileToString(
            testCase, StandardCharsets.UTF_8));
    String schemaStr = jsonObject.get("schema").toString();
    String tmsPath = jsonObject.get("tmsPath").toString();
    String defaultValueString = jsonObject.get("defaultValueString").toString();
    String topColumn = jsonObject.get("topColumn").toString();
    String condition = jsonObject.get("condition").toString();
    String inputDatasetName = "inputDataset";
    JSONArray inputArray = (JSONArray) jsonObject.get("input");
    JSONArray expectedOutputArray = (JSONArray) jsonObject.get("expectedOutput");
    Dataset<Row> inputData = getDatasetFromJsonArray(schemaStr, inputArray, _sparkSession);
    Dataset<Row> expectedOutput = getDatasetFromJsonArray(schemaStr, expectedOutputArray, _sparkSession);
    inputData.createOrReplaceTempView(inputDatasetName);
    String sqlString = String.format(
        "select REDACT_FIELD_IF(%s, %s, \"%s\", %s) from %s",
        condition,
        topColumn,
        tmsPath,
        defaultValueString, inputDatasetName);
    Dataset<Row> actual = _sparkSession.sql(sqlString);
    List<Row> actualList = actual.collectAsList();
    List<Row> expectedList = expectedOutput.collectAsList();
    assertThat(actualList).isEqualTo(expectedList);
  }

  @BeforeAll
  static void initSparkSessionState() {
    _sparkSession = getSparkSessionWithHiveSupport();
    _parser = new JSONParser();
    register("REDACT_FIELD_IF", RedactFieldIfUDF.class, _sparkSession);
  }

  @BeforeAll
  @AfterAll
  static void deleteTables () {
    deleteTablesHelper();
  }
}
