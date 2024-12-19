package com.linkedin.dataguard.benchmark.enforcement;


import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.MapType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import static com.linkedin.dataguard.benchmark.enforcement.DataGenerator.ColumnInfo.*;
import static java.lang.String.*;


/**
 * Given a tablename in the property `spark.datagenprops.inputTableName`, populates the table with random data according
 * to the table's schema.
 */
public final class DataGenerator {

  private DataGenerator() { }

  static Logger logger =  Logger.getLogger(DataGenerator.class);

  // The queries in resources/queries expect the following schema for the table they are being
  // executed on.
  public static final String EXPECTED_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (\n"
      + "    string_field_1 STRING,\n"
      + "    string_field_2 STRING,\n"
      + "    string_field_3 STRING,\n"
      + "    string_field_4 STRING,\n"
      + "    string_field_5 STRING,\n"
      + "    string_field_6 STRING,\n"
      + "    array_field_10entries ARRAY<STRUCT<\n"
      + "        string_field_1: STRING,\n"
      + "        string_field_2: STRING\n"
      + "    >>,\n"
      + "    array_field_20entries ARRAY<STRUCT<\n"
      + "        string_field_1: STRING,\n"
      + "        string_field_2: STRING\n"
      + "    >>,\n"
      + "    array_field_30entries ARRAY<STRUCT<\n"
      + "        string_field_1: STRING,\n"
      + "        string_field_2: STRING\n"
      + "    >>,\n"
      + "    array_field_40entries ARRAY<STRUCT<\n"
      + "        string_field_1: STRING,\n"
      + "        string_field_2: STRING\n"
      + "    >>,\n"
      + "    nested_array_field_10entries ARRAY<STRUCT<\n"
      + "        string_field_1: STRING,\n"
      + "        string_field_2: STRING,\n"
      + "        array_field_10entries: ARRAY<STRUCT<\n"
      + "            string_field_1: STRING,\n"
      + "            string_field_2: STRING\n"
      + "        >>\n"
      + "    >>,\n"
      + "    wide_shallow_struct_field_1 STRUCT<string_field_1: STRING>,\n"
      + "    wide_shallow_struct_field_2 STRUCT<string_field_1: STRING, string_field_2: STRING>,\n"
      + "    wide_shallow_struct_field_3 STRUCT<string_field_1: STRING, string_field_2: STRING, string_field_3: STRING>,\n"
      + "    wide_shallow_struct_field_4 STRUCT<string_field_1: STRING, string_field_2: STRING, string_field_3: STRING, string_field_4: STRING>,\n"
      + "    wide_shallow_struct_field_5 STRUCT<string_field_1: STRING, string_field_2: STRING, string_field_3: STRING, string_field_4: STRING, "
      + "string_field_5: STRING>,\n"
      + "    deep_struct_field_1 STRUCT<string_field_1: STRING>,\n"
      + "    deep_struct_field_2 STRUCT<string_field_1: STRING, deep_struct_field_1: STRUCT<string_field_1: STRING>>,\n"
      // deep_struct_field_3
      + "    deep_struct_field_3 STRUCT<string_field_1: STRING, deep_struct_field_2: STRUCT<string_field_1: STRING, "
      + "deep_struct_field_1: STRUCT<string_field_1: STRING>>>,\n"
      // deep_struct_field_4
      + "    deep_struct_field_4 STRUCT<string_field_1: STRING, deep_struct_field_3: STRUCT<string_field_1: STRING, "
      + "deep_struct_field_2: STRUCT<string_field_1: STRING, deep_struct_field_1: STRUCT<string_field_1: STRING>>>>,\n"
      // deep_struct_field_5
      + "    deep_struct_field_5 STRUCT<string_field_1: STRING, deep_struct_field_4: STRUCT<string_field_1: STRING, "
      + "deep_struct_field_3: STRUCT<string_field_1: STRING, deep_struct_field_2: STRUCT<string_field_1: STRING, "
      + "deep_struct_field_1: STRUCT<string_field_1: STRING>>>>>\n"
      + ")";

  public static void main(String[] args) {

    SparkSession spark = SparkSession
        .builder()
        .enableHiveSupport()
        .config("spark.sql.mapKeyDedupPolicy", "LAST_WIN")
        .appName(DataGenerator.class.getSimpleName())
        .getOrCreate();

    Properties properties = Utils.sparkConfToProperties(spark.sparkContext().getConf());

    logger.info("==============================================================");
    logger.info("Spark properties: " + properties);

    String inputTableName = properties.getProperty("inputTableName", "u_foo.bartable");
    logger.info("Input table: " + inputTableName);

    int numOutputFiles = Integer.parseInt(properties.getProperty("numOutputFiles", "10"));
    logger.info("Number of output files: " + numOutputFiles);

    long numRecords = Long.parseLong(properties.getProperty("numRecords", "100"));
    logger.info("Number of records: " + numRecords);

    int arrayLength = Integer.parseInt(properties.getProperty("arrayLength", "10"));
    logger.info("Array element count (default): " + arrayLength);

    int mapLength = Integer.parseInt(properties.getProperty("mapLength", "10"));
    logger.info("Map entry count (default): " + mapLength);

    logger.info("==============================================================");

    DataGenerationConfig dataGenerationConfig = new DataGenerationConfig(
        numRecords,
        arrayLength,
        mapLength,
        numOutputFiles);

    try {
      runDataGeneration(spark, inputTableName, dataGenerationConfig);
    } finally {
      spark.stop();
    }
  }

  static void runDataGeneration(SparkSession spark, String inputTable, DataGenerationConfig dataGenerationConfig) {
    spark.sql("CREATE DATABASE IF NOT EXISTS " + inputTable.split("\\.")[0]);
    spark.sql("DROP TABLE IF EXISTS " + inputTable);
    spark.sql(format(EXPECTED_CREATE_TABLE, inputTable));

    // Get schema for the configured table
    StructType schema = spark.table(inputTable).schema();
    logger.info(format("Input table schema: %s", schema.json()));

    Dataset<Row> syntheticData = generateSyntheticData(spark, schema, dataGenerationConfig);
    logger.info(format("Wrote data frame with row count: %d", syntheticData.count()));

    // Repartition to avoid creating small files
    syntheticData.repartition(dataGenerationConfig.getNumOutputFiles()).write().mode(SaveMode.Overwrite).insertInto(inputTable);

    List<Row> output = spark.sql("SELECT * FROM " + inputTable).collectAsList();
    for (Row row : output) {
      logger.info(row.json());
    }
  }

  static Dataset<Row> generateSyntheticData(SparkSession spark, StructType schema, DataGenerationConfig config) {
    // Create an initial DataFrame with a single column containing row numbers, the column is named "id"
    Dataset<Row> df = spark.range(config.getNumRecords()).toDF();

    for (StructField field : schema.fields()) {
      DataType dataType = field.dataType();
      String columnName = field.name();

      logger.info(format("Loading columnName: %s, dataType: %s", columnName, dataType.json()));

      // Add a new column with random data based on the data type
      ColumnInfo column = generateRandomColumn(columnName, dataType, config);
      logger.info(format("Loaded sql for columnName '%s': %s", columnName, column.getColumnExprString()));
      df = df.withColumn(columnName, column.getColumn());
    }

    // Drop the initial row number column that was created with spark.range().toDF call
    df = df.drop("id");

    return df;
  }

  /**
   * Generates a {@link ColumnInfo} object given a column name, corresponding data type and parameters.
   * The ColumnInfo wraps Spark's {@link Column} object, which encodes the expression used for generating data
   * in this column. The expressions involve some form of randomness in it to ensure random data generation.
   */
  static ColumnInfo generateRandomColumn(String columnName, DataType dataType, DataGenerationConfig config) {
    if (dataType.equals(DataTypes.IntegerType)) {
      return generateRandomInteger();
    } else if (dataType.equals(DataTypes.StringType)) {
      return generateRandomString();
    } else if (dataType.equals(DataTypes.DoubleType)) {
      return generateRandomDouble();
    } else if (dataType.equals(DataTypes.LongType)) {
      return generateRandomLong();
    } else if (dataType.equals(DataTypes.BooleanType)) {
      return generateRandomBoolean();
    } else if (dataType.equals(DataTypes.TimestampType)) {
      return generateRandomTimestamp();
    } else if (dataType instanceof ArrayType) {
      DataType elementType = ((ArrayType) dataType).elementType();
      ColumnInfo elementColumn = generateRandomColumn("entries", elementType, config);
      int size = getEntryCount(columnName, config.getArrayLength());
      return generateRandomArray(columnName, size, elementColumn);
    } else if (dataType instanceof MapType) {
      DataType keyType = ((MapType) dataType).keyType();
      DataType valueType = ((MapType) dataType).valueType();
      ColumnInfo keys = generateRandomColumn("keys", keyType, config);
      ColumnInfo values = generateRandomColumn("values", valueType, config);
      int size = getEntryCount(columnName, config.getMapLength());
      logger.info(format("Using entrycount=%d, for field %s", size, columnName));
      return generateRandomMap(keys, values, size);
    } else if (dataType instanceof StructType) {
      return generateRandomStruct((StructType) dataType, config);
    }
    // Add more data types as needed
    throw new IllegalStateException("Unable to generate random data for type: " + dataType);
  }

  private static ColumnInfo generateRandomInteger() {
    return column("cast(rand() * 10000 as int)");
  }

  private static ColumnInfo generateRandomString() {
    // Append a random integer at the end to avoid dictionary encoding optimizations in ORC, resulting in drastic reduction
    // in scanned data
    return column("array('alice', 'bob', 'charlie', 'dev', 'elbow', 'fighter', 'golden')"
        + "[cast(rand() * 7 as int)] || cast(rand() * 100000 as int)");
  }

  private static ColumnInfo generateRandomDouble() {
    return column("rand() * 10000");
  }

  private static ColumnInfo generateRandomLong() {
    return column("cast(rand() * 10000 as bigint)");
  }

  private static ColumnInfo generateRandomBoolean() {
    return column("cast(rand() * 2 as boolean)");
  }

  private static ColumnInfo generateRandomTimestamp() {
    return column("cast((unix_timestamp(current_timestamp()) + (rand() * 1000000)) as timestamp)");
  }

  private static ColumnInfo generateRandomArray(String columnName, int size, ColumnInfo elementColumn) {
    logger.info(format("Using array length %d for field %s", size, columnName));
    String arraySql = "array_repeat(" + elementColumn.getColumnExprString() + ", " + size + ")";
    return column(arraySql);
  }

  private static ColumnInfo generateRandomStruct(StructType dataType, DataGenerationConfig config) {
    StructType structType = dataType;
    String input = "struct(";
    for (StructField field : structType.fields()) {
      input += generateRandomColumn(field.name(), field.dataType(), config).getColumnExprString();
      input += ", ";
    }
    input = input.replaceAll(", $", ")");
    return column(input);
  }

  /**
   * Determines the number of entries to put in a collection type element (e.g. array or map), given the name of the column
   * and a default value.
   * If the column name ends with pattern `_<some_number>entries`, the convention is to pick that number as the count
   * of elements in the collection.
   */
  public static int getEntryCount(String columnName, int defaultValue) {
    String[] parts = columnName.split("_");

    try {
      String lastPart = parts[parts.length - 1];
      if (lastPart.endsWith("entries")) {
        String countStr = lastPart.substring(0, lastPart.indexOf("entries"));
        return Integer.parseInt(countStr);
      }
    } catch (Exception e) {
      return defaultValue;
    }

    return defaultValue;
  }

  private static ColumnInfo generateRandomMap(ColumnInfo keyColumn, ColumnInfo valueColumn, int length) {
    // Create arrays of keys and values of the specified length
    String keysArray = "transform(sequence(1, " + length + "), x -> " + keyColumn.getColumnExprString() + ")";
    String valuesArray = "transform(sequence(1, " + length + "), x -> " + valueColumn.getColumnExprString() + ")";
    // Combine keys and values arrays into a map
    String mapExprString = "map_from_arrays(" + keysArray + ", " + valuesArray + ")";
    Column col = functions.map_from_arrays(functions.expr(keysArray), functions.expr(valuesArray));
    return new ColumnInfo(mapExprString, col);
  }

  /**
   * A wrapper object referencing Spark's Column object, and accompanying expression as a string. Storing the expression
   * string is useful for building expressions recursively for nested columns. Column#toString is not useful in that case
   * since it represents lambda expressions using its custom internal syntax.
   */
  static class ColumnInfo {
    private final String columnExprString;
    private final Column column;

    ColumnInfo(String columnExprString, Column column) {
      this.columnExprString = columnExprString;
      this.column = column;
    }

    public static ColumnInfo column(String columnExprString) {
      return new ColumnInfo(columnExprString, functions.expr(columnExprString));
    }

    public String getColumnExprString() {
      return columnExprString;
    }

    public Column getColumn() {
      return column;
    }
  }
}
