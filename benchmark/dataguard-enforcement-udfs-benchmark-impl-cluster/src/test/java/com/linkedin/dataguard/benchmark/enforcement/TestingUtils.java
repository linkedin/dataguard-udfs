package com.linkedin.dataguard.benchmark.enforcement;

import com.linkedin.dataguard.runtime.spark.TestJavaUtils;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;


public class TestingUtils {

  public final static String METASTORE_DB_PATH = "metastore_db";
  public final static String SPARK_WAREHOUSE_PATH = "spark-warehouse";

  public static SparkSession getSparkSession() {
    // Spark does not allow more than one spark context per JVM. So let's stop
    // any active spark session that could have been created in another test first.
    if (!SparkSession.getActiveSession().isEmpty()) {
      SparkSession.getActiveSession().get().stop();
    }
    return TestJavaUtils.createSparkSession(
        true,
        "spark-session-test-name",
        1,
        new SparkConf());
  }

  public static void deleteTablesHelper() {
    try {
      FileUtils.deleteDirectory(new File(METASTORE_DB_PATH));
      FileUtils.deleteDirectory(new File(SPARK_WAREHOUSE_PATH));
    } catch (IOException e) {
      throw new RuntimeException(
          "Could not delete metastore_db. You may have to delete them manually", e);
    }
  }

}
