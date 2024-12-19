package com.linkedin.dataguard.runtime.udf.spark;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import static org.assertj.core.api.AssertionsForClassTypes.*;


// Scans through all the json files in the testRedactSecondarySchemaFieldIf directory and runs test for each of them.
// Each json file needs to have the following fields:
//  1. schema: the spark schema of the input and output dataset
//  2. dataguardPaths: the dataguard path to redact
//  3. defaultValueDataType: the data type string for the default value to use for redaction. This string will directly be used in the SQL query
//  4. topColumn: the top level column to redact
//  5. condition: the condition to check whether to redact the field
//  6. input: the input dataset in json format
//  7. expectedOutput: the expected output dataset in json format
public class TestRedactSecondarySchemaFieldIf {
  // Currently the test case doesn't extend the SparkSessionBase class due to potential config conflicts
  // that caused failure in UDF registration: org.apache.spark.sql.AnalysisException:
  // No handler for UDAF 'com.linkedin.policy.decoration.udfs.RedactSecondarySchemaFieldIf
  // TODO: Investigate and see how to resolve the config conflicts and extend SparkSessionBase
  private static SparkSession _sparkSession;
  private static JSONParser _parser;
  private static final String TEST_DIR = "testRedactSecondarySchemaFieldIf";

  private static Stream<Arguments> provideTestArguments() {
    ClassLoader classLoader = TestRedactSecondarySchemaFieldIf.class.getClassLoader();
    URL resourceUrl = classLoader.getResource(TEST_DIR);
    if (resourceUrl == null) {
      throw new RuntimeException("Test directory not found");
    }
    File[] testCases = new File(resourceUrl.getFile()).listFiles();
    if (testCases == null || testCases.length == 0) {
      throw new RuntimeException("No test cases found");
    }
    return Arrays.stream(testCases).map(Arguments::of);
  }

  @ParameterizedTest
  @MethodSource("provideTestArguments")
  public void testRedactSecondarySchemaFieldIfForSingleTestCase(File testCase)
      throws IOException, ParseException {
    JSONObject jsonObject = (JSONObject) _parser.parse(
        FileUtils.readFileToString(
            testCase, StandardCharsets.UTF_8));
    String schemaStr = jsonObject.get("schema").toString();
    String dataguardPaths = jsonObject.get("dataguardPaths").toString();
    String defaultValueDataType = jsonObject.get("defaultValueDataType").toString();
    String topColumn = jsonObject.get("topColumn").toString();
    String condition = jsonObject.get("condition").toString();
    String inputDatasetName = "inputDataset";
    JSONArray inputArray = (JSONArray) jsonObject.get("input");
    JSONArray expectedOutputArray = (JSONArray) jsonObject.get("expectedOutput");
    Dataset<Row> inputData = getDatasetFromJsonArray(schemaStr, inputArray, _sparkSession);
    Dataset<Row> expectedOutput = getDatasetFromJsonArray(schemaStr, expectedOutputArray, _sparkSession);
    inputData.createOrReplaceTempView(inputDatasetName);
    List<Row> expectedList = expectedOutput.collectAsList();
    String sqlString = String.format(
        "select REDACT_SECONDARY_SCHEMA_FIELD_IF(%s, %s, %s, '%s', %s) from %s",
        condition,
        topColumn,
        dataguardPaths,
        topColumn,
        defaultValueDataType, inputDatasetName);
    Dataset<Row> actual = _sparkSession.sql(sqlString);
    List<Row> actualList = actual.collectAsList();
    assertThat(actualList).isEqualTo(expectedList);
  }

  @BeforeAll
  static void initSparkSessionState() {
    _parser = new JSONParser();
    _sparkSession = getSparkSessionWithHiveSupport();
    register("REDACT_SECONDARY_SCHEMA_FIELD_IF", RedactSecondarySchemaFieldIfUDF.class, _sparkSession);  }

  @BeforeAll
  @AfterAll
  static void deleteTables () {
    deleteTablesHelper();
  }
}