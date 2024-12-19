package com.linkedin.dataguard.runtime.spark;

import java.lang.reflect.InvocationTargetException;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class TestJavaUtils {

  public static final Integer DEFAULT_SQL_SHUFFLE_PARTITIONS = 1;
  private static final Logger LOG = LoggerFactory.getLogger(TestJavaUtils.class);

  private TestJavaUtils() {}

  /**
   * Create the local SparkSession used for general-purpose spark unit test as well as DaliSpark.
   * end-to-end read/write solution unit test.
   *
   * <p>go/inclusivecode deferred(the word `master` in this doc refers to an arg defined in the
   * spark project which must be updated first - see SPARK-32333)
   *
   * @param enableHiveSupport whether to enable HiveSupport. user only needs to enable if they want
   *     to test writing to hive(Dali URI). If so, please follow go/dalisparktest to set up env
   *     correctly
   * @param appName name of the local spark app
   * @param numThreads parallelism of the local spark app
   * @param sparkConf provide user specific Spark conf object rather than using default one. The
   *     spark.appName and spark.master config in sparkConf will not be honored. User should set
   *     sparkConf and numThreads explicitly.
   */
  public static SparkSession createSparkSession(
      boolean enableHiveSupport, String appName, int numThreads, SparkConf sparkConf) {
    return createSparkSession(enableHiveSupport, appName, String.valueOf(numThreads), sparkConf);
  }

  static SparkSession createSparkSession(
      boolean enableHiveSupport, String appName, String numThreads, SparkConf sparkConf) {

    /*
     * Below configs are mimicking the default settings in our Spark cluster so user does not need to set them
     * in their prod jobs.
     *
     * Expression Encoder config is to enable scala case class to understand avro java type as its field
     */
    SparkConf conf = sparkConf == null ? new SparkConf() : sparkConf;

    // If user provides "spark.sql.shuffle.partitions", we don't override the shuffle partitions.
    // Otherwise, we set it to 1. This is to avoid shuffling data in local testing to speed up the
    // test.
    if (!conf.contains("spark.sql.shuffle.partitions")) {
      conf.set("spark.sql.shuffle.partitions", Integer.toString(DEFAULT_SQL_SHUFFLE_PARTITIONS));
    }

    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    conf.set("spark.driver.host", "localhost");
    conf.set(
        "spark.expressionencoder.org.apache.avro.specific.SpecificRecord",
        "org.apache.spark.sql.avro.AvroExpressionEncoder$");

    SparkSession.Builder sessionBuilder =
        SparkSession.builder().appName(appName).master("local[" + numThreads + "]").config(conf);

    // Hive-support is only needed if user wants to write into Dali URI in the local testing
    if (enableHiveSupport) {
      try {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class<?> clazz = loader.loadClass("dali.data.test.DaliDataLocalMode");
        clazz.getMethod("setup").invoke(null);
      } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        LOG.info("No dali on classpath or dali version < 9.3.6, set up Hive with Spark configuration");
      }
      sessionBuilder.enableHiveSupport();
    }

    SparkSession sparkSession = sessionBuilder.getOrCreate();
    return sparkSession;
  }

  public static SparkSession createSparkSession(String appName, int numThreads, SparkConf sparkConf) {
    return createSparkSession(false, appName, numThreads, sparkConf);
  }

  public static SparkSession createSparkSession(String appName, SparkConf sparkConf) {
    return createSparkSession(false, appName, 4, sparkConf);
  }

  public static SparkSession createSparkSession(SparkConf sparkConf) {
    return createSparkSession(false, "localTest", 4, sparkConf);
  }

  public static SparkSession createSparkSession() {
    return createSparkSession(false, "localTest", 4, null);
  }
}
