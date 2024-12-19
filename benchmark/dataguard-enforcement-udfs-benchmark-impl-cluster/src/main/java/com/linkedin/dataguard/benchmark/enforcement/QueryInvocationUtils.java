package com.linkedin.dataguard.benchmark.enforcement;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static com.linkedin.dataguard.benchmark.enforcement.QueryRunner.BenchmarkQueryConfig.*;


/**
 * Collection of methods to invoke a specific query from a suite. For reasoning around duplication of methods, please
 * see javadoc on {@link QueryRunner#runQueries}.
 */
public final class QueryInvocationUtils {

  private QueryInvocationUtils() {
  }

  static Logger logger =  Logger.getLogger(QueryInvocationUtils.class);

  public static void runQuery(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    // Find the right suite and query to execute by looking at caller method name
    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    String patternString = "run(.*?)XX(.*?)Method";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(methodName);

    if (matcher.find()) {
      String suite = matcher.group(1);
      String queryTag = matcher.group(2);
      logger.info("methodName: " + methodName + ", suite: " + suite + ", queryTag: " + queryTag);
      String query = sqls.get(getQuery(suite, queryTag));

      if (query == null) {
        throw new RuntimeException("Could not find test query for " + getQuery(suite, queryTag));
      }

      logger.info("running query: " + query);

      try {
        List<Row> values = spark.sql(query).collectAsList();
        logger.info("Finished query: " + suite + "/" + queryTag);
        logger.info("Output: " + values);
      } catch (Exception e) {
        logger.error("Failed query: " + suite + "/" + queryTag, e);
      }
    } else {
      throw new RuntimeException("Bad test value: " + methodName);
    }
  }

  static void runConsentRateStringXXbaseline0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXbaseline3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXbaseline4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXdataguard0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXdataguard3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStringXXdataguard4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXbaseline0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXbaseline3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXbaseline4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXdataguard0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXdataguard3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructXXdataguard4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXbaseline0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXbaseline3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXbaseline4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXdataguard0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXdataguard3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runConsentRateStructFieldXXdataguard4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXbaseline0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXbaseline3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXbaseline4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXbaseline5Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXdataguard0Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXdataguard3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXdataguard4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionColumnSizeXXdataguard5Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXbaseline3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXbaseline4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXbaseline5Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXdataguard3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXdataguard4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldCountsXXdataguard5Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXbaseline3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXbaseline4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXbaseline5Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXdataguard3Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXdataguard4Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionFieldDepthsXXdataguard5Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays10EntriesXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays10EntriesXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays10EntriesXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays10EntriesXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays20EntriesXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays20EntriesXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays20EntriesXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays20EntriesXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays30EntriesXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays30EntriesXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays30EntriesXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays30EntriesXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays40EntriesXXbaseline1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays40EntriesXXdataguard1Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays40EntriesXXbaseline2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }

  static void runRedactionOn1dArrays40EntriesXXdataguard2Method(Map<QueryRunner.BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    runQuery(sqls, spark);
  }
}
