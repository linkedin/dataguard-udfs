package com.linkedin.dataguard.runtime.trino;

import com.google.common.io.Resources;
import io.trino.Session;
import io.trino.spi.type.TimeZoneKey;
import io.trino.testing.AbstractTestQueryFramework;
import io.trino.testing.LocalQueryRunner;
import io.trino.testing.MaterializedRow;
import io.trino.testing.QueryRunner;
import io.trino.testing.TestingSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;


public class TestRedactFieldIf extends AbstractTestQueryFramework {
  private static final String TEST_CONFIG_DIR = "testRedactFieldIf";
  private static final JSONParser PARSER = new JSONParser();;
  private static final Session SESSION = TestingSession.testSessionBuilder()
      .setTimeZoneKey(TimeZoneKey.UTC_KEY)
      .build();

  @Override
  protected QueryRunner createQueryRunner() {
    LocalQueryRunner localQueryRunner = LocalQueryRunner.builder(SESSION).build();
    localQueryRunner.installPlugin(new DataGuardFunctionsPlugin());
    return localQueryRunner;
  }

  @Test(dataProvider = "redactFieldIf")
  public void testRedactIfForSingleTestCase(File testCase) throws IOException, ParseException {
    JSONObject jsonObject = (JSONObject) PARSER.parse(FileUtils.readFileToString(testCase, StandardCharsets.UTF_8));
    String tmsPath = jsonObject.get("tmsPath").toString();
    String defaultValueString = jsonObject.get("defaultValueString").toString();
    String condition = jsonObject.get("condition").toString();
    QueryRunner queryRunner = getQueryRunner();
    JSONArray expectedOutputStrings = (JSONArray) jsonObject.get("expectedOutputStrings");
    JSONArray inputStrings = (JSONArray) jsonObject.get("inputStrings");
    assertThat(expectedOutputStrings.size()).isEqualTo(inputStrings.size());
    for (int i = 0; i < expectedOutputStrings.size(); i++) {
      String expectedOutputString = expectedOutputStrings.get(i).toString();
      String inputString = inputStrings.get(i).toString();
      MaterializedRow actual = queryRunner
          .execute(String.format("SELECT REDACT_FIELD_IF(%s, %s, '%s', %s)", condition, inputString, tmsPath, defaultValueString))
          .getMaterializedRows().get(0);
      // Not using assertQuery as the second query uses H2 engine which doesn't support MAP() etc.
      MaterializedRow expected = queryRunner.execute(String.format("SELECT %s", expectedOutputString)).getMaterializedRows().get(0);
      assertThat(actual).isEqualTo(expected);
    }
  }

  @DataProvider(name = "redactFieldIf")
  public Object[][] createRedactFieldIfData() {
    URL resourceUrl = Resources.getResource(TEST_CONFIG_DIR);
    File[] testCases = new File(resourceUrl.getFile()).listFiles();
    if (testCases == null || testCases.length == 0) {
      throw new RuntimeException("No test cases found");
    }
    File[][] testCasesArray = new File[testCases.length][1];
    for (int i = 0; i < testCases.length; i++) {
      testCasesArray[i][0] = testCases[i];
    }
    return testCasesArray;
  }

}
