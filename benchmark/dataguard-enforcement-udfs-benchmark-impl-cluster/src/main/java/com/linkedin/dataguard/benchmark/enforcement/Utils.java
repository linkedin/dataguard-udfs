package com.linkedin.dataguard.benchmark.enforcement;

import java.util.Properties;
import org.apache.spark.SparkConf;
import scala.Tuple2;


public final class Utils {

  private Utils() {
  }

  public static final String SPARK_CONF_PREFIX = "spark.datagenprops.";

  /**
   *
   * @param sparkConf
   * @return
   */
  public static Properties sparkConfToProperties(SparkConf sparkConf) {
    Properties properties = new Properties();
    for (Tuple2<String, String> entry : sparkConf.getAllWithPrefix(SPARK_CONF_PREFIX)) {
      properties.put(entry._1, entry._2);
    }
    return properties;
  }

}
