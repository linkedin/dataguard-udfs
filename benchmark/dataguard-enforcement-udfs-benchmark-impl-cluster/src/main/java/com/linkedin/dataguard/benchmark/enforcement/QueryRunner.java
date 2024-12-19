package com.linkedin.dataguard.benchmark.enforcement;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.linkedin.dataguard.runtime.udf.spark.RedactFieldIfUDF;
import com.linkedin.dataguard.runtime.udf.spark.RedactSecondarySchemaFieldIfUDF;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.FunctionIdentifier;
import org.apache.spark.sql.catalyst.expressions.Expression;
import scala.collection.Seq;

import static com.google.common.collect.Maps.*;
import static java.lang.String.*;
import static java.util.stream.Collectors.toMap;


/**
 * Runs the queries specified in `resources/queries`. The test queries follow the structure:
 *
 * - q1
 *     - foo.sql
 *     - bar.sql
 *     - baz.sql
 * - q2
 *     - xyz.sql
 *     - pqr.sql
 * - q3
 *     ....
 *
 * Every .sql file represents a SQL query that can be invoked by QueryRunner.
 * Every outer directory (i.e. q1, q2) represents a logical grouping of queries stored in it, from the perspective of
 * measuring comparative performance.
 */
public final class QueryRunner {

  private QueryRunner() {
  }

  static Logger logger =  Logger.getLogger(QueryRunner.class);

  public static void main(String[] args) throws Exception {

    SparkSession spark = SparkSession
        .builder()
        .enableHiveSupport()
        .appName("QueryRunnerApp")
        .getOrCreate();

    Properties properties = Utils.sparkConfToProperties(spark.sparkContext().getConf());

    logger.info("==============================================================");
    logger.info("Spark properties: " + properties);

    String inputTable = properties.getProperty("inputTableName", "u_foo.bartable");
    logger.info("Input table: " + inputTable);

    try {
      setupUdfs(spark);
      logger.info("Successfully setup udfs, now executing queries ...");
      runQueries(spark, inputTable);
    } finally {
      spark.stop();
    }
  }

  /**
   * Register UDFs to be able to invoke them during query execution
   */
  static void setupUdfs(SparkSession spark) {

    logger.info("Setting up redact_field_if");
    spark.sessionState().functionRegistry().registerFunction(
        new FunctionIdentifier("redact_field_if"),
        RedactFieldIfUDF::new);

    logger.info("Setting up redact_secondary_schema_field_if");
    spark.sessionState().functionRegistry().registerFunction(
        new FunctionIdentifier("redact_secondary_schema_field_if"),
        RedactSecondarySchemaFieldIfUDF::new);
  }

  /**
   * Runs a pre-configured set of queries on the input table.
   *
   * After these queries run, we want to collect and compare/analyze granular statistics about various stages
   * (corresponding to various queries) in the application. These statistics are available only after some
   * delay (hours), and stages cannot be easily correlated to queries as the query SQL is not present in the logs.
   *
   * However, spark logs do contain the stacktrace pointing to the code that triggered execution of a stage. In order to
   * correlate query to Spark application stage performance, we invoke every query through a query-specific method, which
   * encodes information about the test case. e.g. spark application logs will contain the following stacktrace for one
   * of the stages, which can be correlated to `q2/dataguard2.sql` since there's the `runQueryq2XXdataguard2Method` reference in
   * the stacktrace.
   *
   * =========================================================================================
   * collectAsList at QueryRunner.java:538+details
   * org.apache.spark.sql.Dataset.collectAsList(Dataset.scala:2976)
   * com.linkedin.hello.spark.QueryRunner.runQuery(QueryRunner.java:538)
   * com.linkedin.hello.spark.QueryRunner.runQueryq2XXdataguard2Method(QueryRunner.java:299)
   * com.linkedin.hello.spark.QueryRunner.runQueries(QueryRunner.java:175)
   * com.linkedin.hello.spark.QueryRunner.main(QueryRunner.java:80)
   * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
   * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
   * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
   * java.lang.reflect.Method.invoke(Method.java:498)
   * org.apache.spark.deploy.yarn.ApplicationMaster$$anon$2.run(ApplicationMaster.scala:739)
   * =========================================================================================
   *
   */
  public static void runQueries(SparkSession spark, String tableName) throws Exception {
    // Locate resource directory from classpath or jar
    URI uri = getURIForResourcePath("queries");
    initFileSystemForResourceFilesFromJar(uri);
    Path rootDirPath = Paths.get(uri);

    // Extract SQLs to run from resources

    List<String> scenarios = ImmutableList.<String>builder()
        .add("consentRateString")
        .add("consentRateStruct")
        .add("consentRateStructField")
        .add("redactionColumnSize")
        .add("redactionFieldCounts")
        .add("redactionFieldDepths")
        .add("redactionOn1dArrays")
        .build();

    Map<BenchmarkQueryConfig, String> sqls = getBenchmarkingSuiteConfigs(tableName, rootDirPath);
    logger.info("Candidate sqls: " + sqls);

    // Run a no-op query to avoid cold-start for the first query
    List<Row> result = spark.sql("SELECT count(*) FROM " + tableName).collectAsList();
    logger.info("Found row count as: " + result.get(0).json());

    // We don't run the following scenarios because Spark does not allow aggregation (i.e. count)
    // operations on expressions that invoke probability (i.e. rand() < x). This rand() expression can instead be
    // modified to reference a column with the same probability distribution. But that is not implemented currently.
//    runConsentRateStringScenario(sqls, spark);
//    runConsentRateStructScenario(sqls, spark);
//    runConsentRateStructFieldScenario(sqls, spark);
    runRedactionFieldCountsScenario(sqls, spark);
    runRedactionFieldDepthsScenario(sqls, spark);
    runRedactionOn1DArraysScenarios(sqls, spark);
    runRedactionColumnSizeScenario(sqls, spark);
  }

  private static void runConsentRateStringScenario(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    QueryInvocationUtils.runConsentRateStringXXbaseline0Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXbaseline2Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXbaseline3Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXbaseline4Method(sqls, spark);

    QueryInvocationUtils.runConsentRateStringXXdataguard0Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXdataguard2Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXdataguard3Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStringXXdataguard4Method(sqls, spark);
  }

  private static void runConsentRateStructScenario(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    QueryInvocationUtils.runConsentRateStructXXbaseline0Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXbaseline2Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXbaseline3Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXbaseline4Method(sqls, spark);

    QueryInvocationUtils.runConsentRateStructXXdataguard0Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXdataguard2Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXdataguard3Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructXXdataguard4Method(sqls, spark);
  }

  private static void runConsentRateStructFieldScenario(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    QueryInvocationUtils.runConsentRateStructFieldXXbaseline0Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXbaseline2Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXbaseline3Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXbaseline4Method(sqls, spark);

    QueryInvocationUtils.runConsentRateStructFieldXXdataguard0Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXdataguard2Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXdataguard3Method(sqls, spark);
    QueryInvocationUtils.runConsentRateStructFieldXXdataguard4Method(sqls, spark);
  }

  private static void runRedactionColumnSizeScenario(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    QueryInvocationUtils.runRedactionColumnSizeXXbaseline0Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXbaseline2Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXbaseline3Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXbaseline4Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXbaseline5Method(sqls, spark);

    QueryInvocationUtils.runRedactionColumnSizeXXdataguard0Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXdataguard2Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXdataguard3Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXdataguard4Method(sqls, spark);
    QueryInvocationUtils.runRedactionColumnSizeXXdataguard5Method(sqls, spark);
  }

  private static void runRedactionFieldCountsScenario(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    QueryInvocationUtils.runRedactionFieldCountsXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXbaseline2Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXbaseline3Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXbaseline4Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXbaseline5Method(sqls, spark);

    QueryInvocationUtils.runRedactionFieldCountsXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXdataguard2Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXdataguard3Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXdataguard4Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldCountsXXdataguard5Method(sqls, spark);
  }

  private static void runRedactionFieldDepthsScenario(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {
    QueryInvocationUtils.runRedactionFieldDepthsXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXbaseline2Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXbaseline3Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXbaseline4Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXbaseline5Method(sqls, spark);

    QueryInvocationUtils.runRedactionFieldDepthsXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXdataguard2Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXdataguard3Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXdataguard4Method(sqls, spark);
    QueryInvocationUtils.runRedactionFieldDepthsXXdataguard5Method(sqls, spark);
  }

  private static void runRedactionOn1DArraysScenarios(Map<BenchmarkQueryConfig, String> sqls, SparkSession spark) {

    QueryInvocationUtils.runRedactionOn1dArrays10EntriesXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays10EntriesXXbaseline2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays10EntriesXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays10EntriesXXdataguard2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays20EntriesXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays20EntriesXXbaseline2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays20EntriesXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays20EntriesXXdataguard2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays30EntriesXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays30EntriesXXbaseline2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays30EntriesXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays30EntriesXXdataguard2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays40EntriesXXbaseline1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays40EntriesXXbaseline2Method(sqls, spark);

    QueryInvocationUtils.runRedactionOn1dArrays40EntriesXXdataguard1Method(sqls, spark);
    QueryInvocationUtils.runRedactionOn1dArrays40EntriesXXdataguard2Method(sqls, spark);
  }

  private static Map<BenchmarkQueryConfig, String> getBenchmarkingSuiteConfigs(String tableName, Path rootDirPath)
      throws Exception {

    Map<String, Map<String, String>> scenarios = new HashMap<>();
    try (Stream<Path> walks = Files.walk(rootDirPath)) {
      walks.filter(p -> p.toString().endsWith(".sql")).forEach(childPath -> {
        String relativePath = rootDirPath.relativize(childPath).toString();
        try {
          String resourceString =
              readAsStringFromResource(Paths.get("queries", relativePath).toString());
          String query = resourceString.replace("inputTableName", tableName);
          String[] testCaseInfo = relativePath.split("/");
          scenarios.computeIfAbsent(testCaseInfo[0], ignore -> new HashMap<>());
          scenarios.get(testCaseInfo[0]).put(testCaseInfo[1].split("\\.")[0], query);
        } catch (Exception e) {
          throw new RuntimeException(format("Error loading SQL from file: %s", relativePath), e);
        }
      });
    }

    Map<BenchmarkQueryConfig, String> sqls = new HashMap<>();

    for (Map.Entry<String, Map<String, String>> entry : scenarios.entrySet()) {
      String scenario = entry.getKey();
      Map<String, String> scenarioSqls = entry.getValue();

      switch (scenario) {
        case "consentRateString":
          sqls.putAll(allScenarioSqlsAsConfig(scenario, scenarioSqls));
          break;
        case "consentRateStruct":
          sqls.putAll(allScenarioSqlsAsConfig(scenario, scenarioSqls));
          break;
        case "consentRateStructField":
          sqls.putAll(allScenarioSqlsAsConfig(scenario, scenarioSqls));
          break;
        case "redactionColumnSize":
          sqls.putAll(allScenarioSqlsAsConfig(scenario, scenarioSqls));
          sqls.putAll(getScenarioBaselineSqls(scenario, scenarioSqls));
          break;
        case "redactionFieldCounts":
          sqls.putAll(allScenarioSqlsAsConfig(scenario, scenarioSqls));
          sqls.putAll(getScenarioBaselineSqls(scenario, scenarioSqls));
          break;
        case "redactionFieldDepths":
          sqls.putAll(allScenarioSqlsAsConfig(scenario, scenarioSqls));
          sqls.putAll(getScenarioBaselineSqls(scenario, scenarioSqls));
          break;
        case "redactionOn1dArrays":
          Map<String, String> arrayWith10Entries = replaceArrayEntryCount(scenarioSqls, "10");
          Map<String, String> arrayWith20Entries = replaceArrayEntryCount(scenarioSqls, "20");
          Map<String, String> arrayWith30Entries = replaceArrayEntryCount(scenarioSqls, "30");
          Map<String, String> arrayWith40Entries = replaceArrayEntryCount(scenarioSqls, "40");

          sqls.putAll(allScenarioSqlsAsConfig(scenario + "10Entries", arrayWith10Entries));
          sqls.putAll(getScenarioBaselineSqls(scenario + "10Entries", arrayWith10Entries));
          sqls.putAll(allScenarioSqlsAsConfig(scenario + "20Entries", arrayWith20Entries));
          sqls.putAll(getScenarioBaselineSqls(scenario + "20Entries", arrayWith20Entries));
          sqls.putAll(allScenarioSqlsAsConfig(scenario + "30Entries", arrayWith30Entries));
          sqls.putAll(getScenarioBaselineSqls(scenario + "30Entries", arrayWith30Entries));
          sqls.putAll(allScenarioSqlsAsConfig(scenario + "40Entries", arrayWith40Entries));
          sqls.putAll(getScenarioBaselineSqls(scenario + "40Entries", arrayWith40Entries));
          break;
        default:
          throw new RuntimeException("Unsupported scenario type: " + scenario);
      }
    }

    return sqls;
  }

  private static Map<String, String> replaceArrayEntryCount(Map<String, String> scenarioSqls, String arrayEntryCount) {
    return transformValues(scenarioSqls, sql -> {
      if (!sql.contains("XXXX")) {
        throw new RuntimeException("No array field to parametrize in sql:" + sql);
      }
      return sql.replace("XXXX", arrayEntryCount);
    });
  }

  private static Map<? extends BenchmarkQueryConfig, String> getScenarioBaselineSqls(String scenario,
      Map<String, String> scenarioSqls) {
    Pattern p = Pattern.compile("true", Pattern.CASE_INSENSITIVE);
    return allScenarioSqlsAsConfig(scenario,
        scenarioSqls.entrySet().stream().collect(toMap(e -> {
          if (!e.getKey().contains("dataguard")) {
            throw new RuntimeException("Bad test name, must contain `dataguard` in the name: " + e.getKey());
          }
          return e.getKey().replace("dataguard", "baseline");
        },
        e -> {
          Matcher matcher = p.matcher(e.getValue());
          if (!matcher.find()) {
            throw new RuntimeException("No matches for `true` in: " + e.getValue());
          }
          return matcher.replaceAll("false");
        })));
  }

  private static Map<? extends BenchmarkQueryConfig, String> allScenarioSqlsAsConfig(String scenario, Map<String, String> queries) {
    return queries.entrySet().stream().collect(toMap(
        e -> new BenchmarkQueryConfig(scenario, e.getKey()),
        e -> e.getValue()));
  }

  public static URI getURIForResourcePath(String resourcePath) throws URISyntaxException {
    URL url = QueryRunner.class.getResource(File.separator + resourcePath);
    if (url == null) {
      throw new RuntimeException(format("Invalid resource path: ", resourcePath));
    }
    return url.toURI();
  }

  /**
   * Provides appropriate file system given a URI. Helps support reading from a jar or test class path.
   */
  public static FileSystem initFileSystemForResourceFilesFromJar(URI rootUri) throws Exception {
    try {
      return FileSystems.getFileSystem(rootUri);
    } catch (FileSystemNotFoundException e) {
      Map<String, String> env = new HashMap<>();
      env.put("create", "true");
      return FileSystems.newFileSystem(rootUri, env);
    } catch (IllegalArgumentException e) {
      /*
      For test cases we are encountering java.lang.IllegalArgumentException: Path component should be '/'
      Ignoring it, as default file system already seems to be initialized/working.
      */
      return FileSystems.getDefault();
    }
  }

  public static String readAsStringFromResource(String resourceName) throws Exception {
    return readAsStringFromResource(resourceName, ImmutableMap.of());
  }

  /**
   * Uses an {@link InputStream} to read the resource file with the given name and returns a string
   *
   * @param resourceName Name of the resource file
   */
  public static String readAsStringFromResource(String resourceName, Map<String, String> replacementMap) throws Exception {
    String data;
    try (InputStream inputStream = QueryRunner.class.getClassLoader().getResourceAsStream(resourceName)) {
      if (inputStream == null) {
        throw new RuntimeException("Input stream is null. Failed to find resource: " + resourceName);
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append("\n");
      }
      data = stringBuilder.toString();
      reader.close();
    }
    for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
      data = data.replace(entry.getKey(), entry.getValue());
    }
    return data;
  }

  public static class BenchmarkQueryConfig {
    private final String suite;
    private final String queryTag;

    public BenchmarkQueryConfig(String suite, String queryTag) {
      this.suite = suite.toLowerCase();
      this.queryTag = queryTag.toLowerCase();
    }

    public String getSuite() {
      return suite;
    }

    public String getQueryTag() {
      return queryTag;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      BenchmarkQueryConfig that = (BenchmarkQueryConfig) o;
      return Objects.equals(suite, that.suite) && Objects.equals(queryTag, that.queryTag);
    }

    @Override
    public int hashCode() {
      return Objects.hash(suite, queryTag);
    }

    public static BenchmarkQueryConfig getQuery(String suite, String queryTag) {
      return new BenchmarkQueryConfig(suite, queryTag);
    }

    @Override
    public String toString() {
      return "BenchmarkQueryConfig{" + "suite='" + suite + '\'' + ", queryTag='" + queryTag + '\'' + '}';
    }
  }
}
