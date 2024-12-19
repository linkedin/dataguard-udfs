package com.linkedin.dataguard.runtime.benchmark.common;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdBinaryType;
import com.linkedin.transport.api.types.StdBooleanType;
import com.linkedin.transport.api.types.StdDoubleType;
import com.linkedin.transport.api.types.StdFloatType;
import com.linkedin.transport.api.types.StdIntegerType;
import com.linkedin.transport.api.types.StdLongType;
import com.linkedin.transport.api.types.StdMapType;
import com.linkedin.transport.api.types.StdStringType;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Format agnostic data generator for microbenchmarking
 */
public class DataGenerator {

  private static final int MAX_LIST_SIZE = 10;
  /**
   * The expected length of the array being generated is encoded in the name of the array column/field. If it follows
   * the following pattern, i.e. ends with `_(\d+)entries`, e.g. xxxx_10entries, we pick the number 10 as the length
   * of arrays in that column. This is not encoded as a top-level parameter to allow arrays with different lengths in
   * the same schema. If no match, MAX_LIST_SIZE value will be used by default.
   */
  private static final Pattern ENTRY_COUNT_PATTERN = Pattern.compile(".*_(\\d+)entries$");

  private static final List<Integer> TEST_INTEGER_VALUES = ImmutableList.of(34, 49, 9, 85, 16, 123, 11, 27, 55, 92);
  private static final List<Long> TEST_LONG_VALUES = ImmutableList.of(34L, 49L, 9L, 85L, 16L, 123L, 11L, 27L, 55L, 92L);
  private static final List<String> TEST_STRING_VALUES = ImmutableList.of("jackfruit", "apple", "banana", "orange",
      "pear", "foo", "bar", "baz", "rose", "pine");
  private static final List<Double> TEST_DOUBLE_VALUES = ImmutableList.of(0.5d, 1.7d, 10.6d, 25d, 36.4d, 123.1d, 11.7d, 27.0d, 55.3d, 92.3d);
  private static final List<Float> TEST_FLOAT_VALUES = ImmutableList.of(0.9f, 0.1f, 5.6f, 9.18f, 81.23f, 123.1f, 11.7f, 27.0f, 55.3f, 92.3f);

  private final FormatSpecificTypeDataProvider typeDataProvider;
  private final StdFactory factory;
  private final Random random;


  public DataGenerator(FormatSpecificTypeDataProvider typeDataProvider, Optional<Integer> seed) {
    this.typeDataProvider = typeDataProvider;
    this.factory = typeDataProvider.getStdFactory();
    this.random = seed.map(Random::new).orElseGet(Random::new);
  }

  public List<StdData> generateRandomRecords(String typeSignature, int recordCount) {
    return generateRandomRecords(typeSignature, recordCount, 10000000);
  }

  /**
   * Generates random data given the parameters
   *
   * @param typeSignature Type of the expected random record
   * @param recordCount Number of records to be produced
   * @param distinctValues Number of distinct values to use for the primitive fields. This helps with
   *                       benchmarking performance variations with change in probability of condition matching
   *                       for secondary schema field redactions
   * @return list of random records
   */
  public List<StdData> generateRandomRecords(String typeSignature, int recordCount, int distinctValues) {
    StdType type = factory.createStdType(typeSignature);
    return IntStream.range(0, recordCount).boxed()
        .map(i -> generateRecord("", type, distinctValues))
        .collect(Collectors.toList());
  }

  public StdData generateRecord(String fieldName, StdType stdType, int distinctValues) {
    if (stdType instanceof StdBooleanType) {
      return factory.createBoolean(random.nextBoolean());
    }
    if (stdType instanceof StdIntegerType) {
      int randomIntValue = TEST_INTEGER_VALUES.get(Math.min(random.nextInt(TEST_INTEGER_VALUES.size()), distinctValues));
      return factory.createInteger(randomIntValue);
    }
    if (stdType instanceof StdLongType) {
      long randomLongValue = TEST_LONG_VALUES.get(Math.min(random.nextInt(TEST_LONG_VALUES.size()), distinctValues));
      return factory.createLong(randomLongValue);
    }
    if (stdType instanceof StdStringType) {
      String randomStringValue = TEST_STRING_VALUES.get(random.nextInt(Math.min(TEST_STRING_VALUES.size(), distinctValues)));
      return factory.createString(randomStringValue);
    }
    if (stdType instanceof StdFloatType) {
      float randomFloatValue = TEST_FLOAT_VALUES.get(Math.min(random.nextInt(TEST_FLOAT_VALUES.size()), distinctValues));
      return factory.createFloat(randomFloatValue);
    }
    if (stdType instanceof StdDoubleType) {
      double randomDoubleValue = TEST_DOUBLE_VALUES.get(Math.min(random.nextInt(TEST_DOUBLE_VALUES.size()), distinctValues));
      return factory.createDouble(randomDoubleValue);
    }
    if (stdType instanceof StdBinaryType) {
      ByteBuffer randomByteBuffer = ByteBuffer.wrap(TEST_STRING_VALUES
          .get(Math.min(random.nextInt(TEST_STRING_VALUES.size()), distinctValues))
          .getBytes(StandardCharsets.UTF_8));
      return factory.createBinary(randomByteBuffer);
    }
    if (stdType instanceof StdStructType) {
      StdStructType structType = (StdStructType) stdType;
      StdStruct struct = factory.createStruct(stdType);
      List<String> fieldNames = structType.fieldNames();
      for (int i = 0; i < structType.fieldTypes().size(); i++) {
        StdType fieldType = structType.fieldTypes().get(i);
        struct.setField(fieldNames.get(i), generateRecord(fieldNames.get(i), fieldType, distinctValues));
      }
      return struct;
    }
    if (stdType instanceof StdArrayType) {
      StdArrayType arrayType = (StdArrayType) stdType;

      int listSize = extractCollectionSize(fieldName);
      StdArray array = factory.createArray(arrayType);
      for (int i = 0; i < listSize; i++) {
        array.add(generateRecord("element", arrayType.elementType(), distinctValues));
      }
      return array;
    }
    if (stdType instanceof StdMapType) {
      StdMapType mapType = (StdMapType) stdType;
      int mapSize = extractCollectionSize(fieldName);
      StdMap map = factory.createMap(mapType);
      for (int i = 0; i < mapSize; i++) {
        map.put(
            generateRecord("key", mapType.keyType(), distinctValues),
            generateRecord("value", mapType.valueType(), distinctValues));
      }
      return map;
    }

    throw new UnsupportedOperationException("Unsupported type: " + stdType);
  }

  /**
   * See docs on ENTRY_COUNT_PATTERN definition.
   */
  private int extractCollectionSize(String fieldName) {
    int collectionSize = random.nextInt(MAX_LIST_SIZE);
    Matcher matcher = ENTRY_COUNT_PATTERN.matcher(fieldName);
    if (matcher.matches()) {
      collectionSize = Integer.parseInt(matcher.group(1));
    }
    return collectionSize;
  }

  public FormatSpecificTypeDataProvider getTypeDataProvider() {
    return typeDataProvider;
  }
}
