package com.linkedin.dataguard.benchmark.enforcement;


import java.util.Properties;
import org.apache.spark.SparkConf;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.benchmark.enforcement.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class UtilsTest {

  @Test
  public void testSparkConfToProperties() {
    SparkConf sparkConf = new SparkConf()
        .setAppName("TestApp")
        .setMaster("local[*]")
        .set("spark.datagenprops.custom.config1", "value1")
        .set("spark.datagenprops.custom.config2", "value2");
    Properties properties = sparkConfToProperties(sparkConf);
    assertEquals("value1", properties.getProperty("custom.config1"));
    assertEquals("value2", properties.getProperty("custom.config2"));
  }
}
