package com.linkedin.dataguard.runtime.udf.spark;

import com.linkedin.dataguard.runtime.spark.TestJavaUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.FunctionIdentifier;
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import scala.collection.Seq;


public class UdfUtils {

  private final static String METASTORE_DB_PATH = "metastore_db";
  private final static String SPARK_WAREHOUSE_PATH = "spark-warehouse";

  public static void register(String name, Class<? extends Expression> udfClass, SparkSession sparkSession) {
    FunctionRegistry registry = sparkSession.sessionState().functionRegistry();
    registry.registerFunction(new FunctionIdentifier(name), children -> {
      try {
        Constructor<? extends Expression> constructor =
            udfClass.getDeclaredConstructor(Seq.class);
        return constructor.newInstance(children);
      } catch (Exception e) {
        throw new IllegalStateException("Failed to register " + name, e);
      }
    });
  }


  public static SparkSession getSparkSession() {
    // Spark does not allow more than one spark context per JVM. So let's stop
    // any active spark session that could have been created in another test first.
    if (!SparkSession.getActiveSession().isEmpty()) {
      SparkSession.getActiveSession().get().stop();
    }
    return TestJavaUtils.createSparkSession(
        false,
        UdfUtils.class.getSimpleName(),
        1,
        new SparkConf());
  }

  public static SparkSession getSparkSessionWithHiveSupport() {
    // Spark does not allow more than one spark context per JVM. So let's stop
    // any active spark session that could have been created in another test first.
    if (!SparkSession.getActiveSession().isEmpty()) {
      SparkSession.getActiveSession().get().stop();
    }


    return TestJavaUtils.createSparkSession(
        true,
        UdfUtils.class.getSimpleName(),
        1,
        new SparkConf());
  }

  public static void deleteTablesHelper() {
    try {
      FileUtils.deleteDirectory(new File(METASTORE_DB_PATH));
      FileUtils.deleteDirectory(new File(SPARK_WAREHOUSE_PATH));
    } catch (IOException e) {
      throw new RuntimeException(
          "Could not delete metastore_db or spark_warehouse_path. You may have to delete them manually", e);
    }
  }

  public static Dataset<Row> getDatasetFromJsonArray(String schemaStr, JSONArray jsonArray, SparkSession sparkSession) {
    List<String> dataList = (List<String>) jsonArray.stream().map(obj -> {
      if (obj == null) {
        return null;
      }
      return ((JSONObject) obj).toJSONString();
    }).collect(Collectors.toList());
    return sparkSession.read()
        .schema((StructType) DataType.fromJson(schemaStr))
        .json(sparkSession.createDataset(dataList, Encoders.STRING()));
  }
}
