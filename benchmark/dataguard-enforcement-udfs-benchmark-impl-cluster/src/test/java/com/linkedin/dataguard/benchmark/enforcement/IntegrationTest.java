package com.linkedin.dataguard.benchmark.enforcement;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.benchmark.enforcement.DataGenerator.*;
import static com.linkedin.dataguard.benchmark.enforcement.QueryRunner.*;
import static com.linkedin.dataguard.benchmark.enforcement.TestingUtils.*;
import static java.lang.String.*;
import static org.junit.jupiter.api.Assertions.*;


public class IntegrationTest {

  private static SparkSession _sparkSession;

  private static final String TABLE_NAME = "my_table";

  @BeforeAll
  static void initSparkSessionState() {
    _sparkSession = getSparkSession();
  }

  @BeforeAll
  @AfterAll
  static void deleteTables() {
    deleteTablesHelper();
  }

  @Test
  public void testQueryExecution() throws Exception {
    _sparkSession.sql(format(EXPECTED_CREATE_TABLE, TABLE_NAME));
    int numRecords = 100;
    DataGenerator.runDataGeneration(_sparkSession, TABLE_NAME,
        new DataGenerationConfig(numRecords, 10, 10, 5));
    assertEquals(
        numRecords,
        _sparkSession.sql(format("SELECT count(*) FROM %s", TABLE_NAME)).collectAsList().get(0).getLong(0));
    setupUdfs(_sparkSession);
    runQueries(_sparkSession, TABLE_NAME);
  }
}
