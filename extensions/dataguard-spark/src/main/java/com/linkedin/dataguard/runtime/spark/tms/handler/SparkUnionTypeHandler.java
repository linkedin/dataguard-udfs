package com.linkedin.dataguard.runtime.spark.tms.handler;

import com.google.common.collect.ImmutableMap;
import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.UnionTypeHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.UnionTypeSelector;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.transport.spark.data.NullableSparkStruct;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.spark.types.SparkStructType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;
import static com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper.*;



public class SparkUnionTypeHandler extends UnionTypeHandler {

  public static final String UNION_TO_STRUCT_TAG = "tag";
  private static final String UNION_TO_STRUCT_FIELD = "field";
  private static final Logger LOG = LoggerFactory.getLogger(SparkUnionTypeHandler.class);
  private static final Map<String, String> SPARK_TYPE_TO_STD_TYPE_MAP = ImmutableMap.<String, String>builder()
      .put(DataTypes.BooleanType.toString(), "boolean")
      .put(DataTypes.IntegerType.toString(), "int")
      .put(DataTypes.StringType.toString(), "string")
      .put(DataTypes.LongType.toString(), "bigint")
      .put(DataTypes.DoubleType.toString(), "double")
      .put(DataTypes.FloatType.toString(), "float")
      .put(DataTypes.BinaryType.toString(), "binary")
      .build();

  public SparkUnionTypeHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
  }

  @Override
  public StdData redact(StdData data) {
    TMSEnforcer tmsEnforcer = getTmsEnforcer();
    Action action = tmsEnforcer.getAction();
    List<TMSPathSelector> tmsPathSelectors = tmsEnforcer.getSelectors();
    return redactSpark(pathIndex, tmsPathSelectors, action, data);
  }

  private StdData redactSpark(int pathIndex,
      List<TMSPathSelector> tmsPathSelectors, Action action, StdData dataOrig) {
    TMSPathSelector selector = tmsPathSelectors.get(pathIndex);
    StdData defaultValueStdData = action.getReplacementValue(dataOrig);
    Object defaultValue = defaultValueStdData == null ? null : ((PlatformData) defaultValueStdData).getUnderlyingData();
    StdType type = getStdType();
    if (!(type instanceof SparkStructType)
        || !isComplexUnion(((SparkStructType) type).fieldNames())) {
      // nullable union
      return isLastSelector(pathIndex, tmsPathSelectors) ? defaultValueStdData
          : tmsEnforcer.redact(dataOrig, pathIndex + 1);
    }
    SparkStructType structType = (SparkStructType) type;
    NullableSparkStruct structData = (NullableSparkStruct) dataOrig;
    String unionTypeSelected = ((UnionTypeSelector) selector).getUnionTypeSelected();
    String targetFieldName = getTargetFieldName(unionTypeSelected, structType);
    int indexOfFieldToRemove = getTargetFieldIndex(unionTypeSelected, structType);
    if (targetFieldName == null) {
      // No corresponding type signature match found
      if (isLastSelector(pathIndex, tmsPathSelectors)
          || !(tmsPathSelectors.get(pathIndex + 1) instanceof StructFieldSelector)) {
        // next selector is not struct field selector
        if (LOG.isDebugEnabled()) {
          LOG.debug(
              "next selector after the union type selector is not struct field selector, redacting the entire union");
        }
        return defaultValueStdData;
      }
      StructFieldSelector nextSelector = (StructFieldSelector) tmsPathSelectors.get(pathIndex + 1);
      // Currently apart from union types that matches type defined in union type selector,
      // we can also process the struct union type where the child field is also a struct
      String childFieldName = nextSelector.getFieldName();
      List<Integer> matchedGrandChildren = findMatchingGrandChildren(structType, childFieldName);
      if (matchedGrandChildren.size() > 1) {
        if (LOG.isDebugEnabled()) {
          LOG.debug(
              "Found multiple matches ({}) for grand child field of {} in structs within union type, redacting the entire union",
              matchedGrandChildren.size(), childFieldName);
        }
        return defaultValueStdData;
      }
      if (matchedGrandChildren.isEmpty()) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Found no match for grand child field of {}, keeping the original data", childFieldName);
        }
        return dataOrig;
      }
      // Happy scenario
      indexOfFieldToRemove = matchedGrandChildren.get(0);
    }
    StdFactory stdFactory = getTmsEnforcer().getTypeDataProvider().getStdFactory();
    if (isLastSelector(pathIndex, tmsPathSelectors)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("last selector is union type selector in {}, redacting field: {} with default value: {}",
            tmsPathSelectors, targetFieldName, defaultValue);
      }
      return createNewStdStructWithSameSchemaDifferentData(structData, stdFactory, structType, indexOfFieldToRemove,
          defaultValueStdData);
    }
    StdData nextData = structData.getField(indexOfFieldToRemove);
    StdData redactedFieldStdData = tmsEnforcer.redact(nextData, pathIndex + 1);
    if (LOG.isDebugEnabled()) {
      LOG.debug("current selector is union type selector in {}, recursively redacting field: {}", tmsPathSelectors,
          targetFieldName);
    }
    return createNewStdStructWithSameSchemaDifferentData(structData, stdFactory, structType, indexOfFieldToRemove,
        redactedFieldStdData);
  }

  @Override
  public StdType getChildStdType() {
    StdType currentType = getStdType();
    List<TMSPathSelector> selectors = tmsEnforcer.getSelectors();
    TMSPathSelector selector = selectors.get(pathIndex);
    String unionTypeSelected = ((UnionTypeSelector) selector).getUnionTypeSelected();
    if (!(currentType instanceof StdStructType) || !isComplexUnion(((StdStructType) currentType).fieldNames())) {
      // simple union
      return currentType;
    }
    SparkStructType structType = (SparkStructType) currentType;
    int targetFieldIndex = getTargetFieldIndex(unionTypeSelected, structType);
    if (targetFieldIndex < 0) {
      if (isLastSelector(pathIndex, selectors)) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Last selector cannot be union type selector: {}. Returning null as the result type", selectors);
        }
        return null;
      }
      TMSPathSelector nextSelector = selectors.get(pathIndex + 1);
      if (nextSelector.getClass() != StructFieldSelector.class) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Only supporting union type selector followed by struct field selector but observed selectors: {}",
              selectors);
        }
      }
      StructFieldSelector nextStructSelector = (StructFieldSelector) nextSelector;
      // Currently apart from union types that matches type defined in union type selector,
      // we can also process the struct union type where the child field is also a struct
      String childFieldName = nextStructSelector.getFieldName();
      List<Integer> matchedGrandChildren = findMatchingGrandChildren(structType, childFieldName);
      if (matchedGrandChildren.isEmpty()) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Found no match for grand child field of {}, Returning null as the result type", childFieldName);
        }
        return null;
      }
      if (matchedGrandChildren.size() > 1) {
        if (LOG.isWarnEnabled()) {
          LOG.warn(
              "Found multiple matches ({}) for grand child field of {} in structs within union type, Returning null as the result type",
              matchedGrandChildren.size(), childFieldName);
        }
        return null;
      }
      return createStdType((DataType) structType.fieldTypes().get(matchedGrandChildren.get(0)).underlyingType());
    }
    return createStdType((DataType) structType.fieldTypes().get(targetFieldIndex).underlyingType());
  }

  private static String getTargetFieldName(String unionTypeSelected, SparkStructType structType) {
    String targetFieldName = null;
    int targetFieldIndex = getTargetFieldIndex(unionTypeSelected, structType);
    if (targetFieldIndex >= 0) {
      targetFieldName = "field" + (targetFieldIndex - 1);
      if (LOG.isDebugEnabled()) {
        LOG.debug("found target field name: {} ", targetFieldName);
      }
    }
    return targetFieldName;
  }

  private static int getTargetFieldIndex(String unionTypeSelected, SparkStructType structType) {
    List<? extends StdType> fieldTypes = structType.fieldTypes();
    for (int i = 1; i < fieldTypes.size(); i++) {
      StdType type = fieldTypes.get(i);
      // Only support comparison with primitive types in union for now
      String typeName = SPARK_TYPE_TO_STD_TYPE_MAP.getOrDefault(type.underlyingType().toString(), "");
      if (unionTypeSelected.equalsIgnoreCase(typeName)) {
        return i;
      }
    }
    return -1;
  }

  public static boolean isComplexUnion(List<String> fieldNames) {
    // complex union is represented as struct<tag:int, field0:<type0>, field1:<type1>...> in spark
    return fieldNames.size() >= 2
        && fieldNames.get(0).equals(UNION_TO_STRUCT_TAG)
        && IntStream.range(1, fieldNames.size())
            .allMatch(i -> fieldNames.get(i).equals(UNION_TO_STRUCT_FIELD + (i - 1)));
  }

  public static List<Integer> findMatchingGrandChildren(SparkStructType structType, String grandChildName) {
    List<Integer> results = new ArrayList<>();
    for (int i = 0; i < structType.fieldNames().size(); i++) {
      StdType childType = structType.fieldTypes().get(i);
      if (!(childType instanceof SparkStructType)) {
        continue;
      }
      SparkStructType childStructType = (SparkStructType) childType;
      int index = i;
      childStructType.fieldNames().stream()
          .filter(f -> f.equals(grandChildName)).findFirst().ifPresent(ignored -> results.add(index));

    }
    return results;
  }

  public static StdData createNewStdStructWithSameSchemaDifferentData(
      StdStruct struct, StdFactory stdFactory, StdType stdType, int fieldIndex, StdData defaultValueStdData) {
    StdStruct newStruct = stdFactory.createStruct(stdType);
    for (int i = 0; i < struct.fields().size(); i++) {
      // if the target field to redact is already null, we don't need to replace it with default value?
      StdData field = struct.getField(i);
      if (i != fieldIndex || field == null) {
        newStruct.setField(i, field);
        continue;
      }
      newStruct.setField(i, defaultValueStdData);
    }
    return newStruct;
  }
}
