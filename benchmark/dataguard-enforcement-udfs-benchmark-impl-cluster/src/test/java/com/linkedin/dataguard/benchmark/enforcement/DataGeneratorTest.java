package com.linkedin.dataguard.benchmark.enforcement;

import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.benchmark.enforcement.DataGenerator.*;
import static com.linkedin.dataguard.benchmark.enforcement.TestingUtils.*;
import static java.lang.String.*;
import static org.junit.jupiter.api.Assertions.*;


public class DataGeneratorTest {

  private static SparkSession _sparkSession;

  private static final DataGenerationConfig DATA_GENERATION_CONFIG = new DataGenerationConfig(10, 10, 10, 10);

  @BeforeAll
  static void initSparkSessionState() throws Exception {
    _sparkSession = getSparkSession();
  }

  @BeforeAll
  @AfterAll
  static void deleteTables() {
    deleteTablesHelper();
  }

  @Test
  public void testGenerateRandomColumn() {
    // Trivial assertions for primitive types
    testSingleDataType(DataTypes.IntegerType, "cast(rand() * 10000 as int)");
    testSingleDataType(DataTypes.StringType, "array('alice', 'bob', 'charlie', 'dev', 'elbow', 'fighter', 'golden')[cast(rand() * 7 as int)] || cast(rand() * 100000 as int)");
    testSingleDataType(DataTypes.DoubleType, "rand() * 10000");
    testSingleDataType(DataTypes.LongType, "cast(rand() * 10000 as bigint)");
    testSingleDataType(DataTypes.BooleanType, "cast(rand() * 2 as boolean)");
    testSingleDataType(DataTypes.TimestampType, "cast((unix_timestamp(current_timestamp()) + (rand() * 1000000)) as timestamp)");

    // Assertion for complex types
    testSingleDataType(
        DataTypes.createArrayType(DataTypes.IntegerType),
        format("array_repeat(cast(rand() * 10000 as int), %d)", 50),
        // The convention is that column names with suffix encodes that array should have 50 entries
        "testArray_50entries");

    testSingleDataType(
        DataTypes.createArrayType(DataTypes.IntegerType),
        format("array_repeat(cast(rand() * 10000 as int), %d)", DATA_GENERATION_CONFIG.getArrayLength()),
        "testArray");

    testSingleDataType(
        new StructType(new StructField[]{
            new StructField("field1", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("field2", DataTypes.StringType, false, Metadata.empty())
        }),
        "struct("
            + "cast(rand() * 10000 as int), "
            + "array('alice', 'bob', 'charlie', 'dev', 'elbow', 'fighter', 'golden')[cast(rand() * 7 as int)] || cast(rand() * 100000 as int))");

    testSingleDataType(
        DataTypes.createMapType(DataTypes.StringType, DataTypes.IntegerType),
        "map_from_arrays("
            + "transform(sequence(1, 10), x -> array('alice', 'bob', 'charlie', 'dev', 'elbow', 'fighter', 'golden')[cast(rand() * 7 as int)] || cast(rand() * 100000 as int)), "
            + "transform(sequence(1, 10), x -> cast(rand() * 10000 as int)))");
  }

  @Test
  public void testEntrycountGetter() throws Exception {
    assertEquals(11, getEntryCount("array_field_11entries", DATA_GENERATION_CONFIG.getArrayLength()));
    assertEquals(502, getEntryCount("array_field_502entries", DATA_GENERATION_CONFIG.getArrayLength()));
    assertEquals(DATA_GENERATION_CONFIG.getArrayLength(), getEntryCount("array_field_abcentries", DATA_GENERATION_CONFIG.getArrayLength()));
    assertEquals(DATA_GENERATION_CONFIG.getArrayLength(), getEntryCount("blah", DATA_GENERATION_CONFIG.getArrayLength()));
  }

  private void testSingleDataType(DataType dataType, String expected, String columnName) {
    ColumnInfo columnInfo = generateRandomColumn(columnName, dataType, DATA_GENERATION_CONFIG);
    assertEquals(expected, columnInfo.getColumnExprString());
  }

  private void testSingleDataType(DataType dataType, String expected) {
    testSingleDataType(dataType, expected,"testColumn");
  }

  // Validate data generation for all types
  @Test
  public void testDataGenerator() {
    StructType schema = new StructType(new StructField[]{
        new StructField("integerField", DataTypes.IntegerType, false, Metadata.empty()),
        new StructField("stringField", DataTypes.StringType, false, Metadata.empty()),
        new StructField("doubleField", DataTypes.DoubleType, false, Metadata.empty()),
        new StructField("longField", DataTypes.LongType, false, Metadata.empty()),
        new StructField("booleanField", DataTypes.BooleanType, false, Metadata.empty()),
        new StructField("timestampField", DataTypes.TimestampType, false, Metadata.empty()),
        new StructField("arrayField", DataTypes.createArrayType(DataTypes.StringType), false, Metadata.empty()),
        new StructField("mapField", DataTypes.createMapType(DataTypes.StringType, DataTypes.IntegerType), false, Metadata.empty()),
        new StructField("structField", new StructType(new StructField[]{
            new StructField("nestedIntegerField", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("nestedStringField", DataTypes.StringType, false, Metadata.empty())
        }), false, Metadata.empty()),
        new StructField("nestedArrayField", DataTypes.createArrayType(new StructType(new StructField[]{
            new StructField("nestedIntArray", DataTypes.IntegerType, false, Metadata.empty())
        })), false, Metadata.empty()),
        new StructField("nestedMapField", DataTypes.createMapType(DataTypes.StringType, new StructType(new StructField[]{
            new StructField("nestedMapValue", DataTypes.StringType, false, Metadata.empty())
        })), false, Metadata.empty())
    });

    DataGenerationConfig config = new DataGenerationConfig(1000, 10, 10, 10);
    Dataset<Row> data = generateSyntheticData(_sparkSession, schema, config);
    List<Row> rows = data.collectAsList();
  }
}
