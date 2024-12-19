package com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics;

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.RowSelectorAwareFieldPath;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdDouble;
import com.linkedin.transport.api.data.StdFloat;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.data.StdLong;
import com.linkedin.transport.api.data.StdString;
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
import com.linkedin.transport.api.types.StdUnknownType;

import static java.lang.String.*;
import static java.util.Objects.*;


public final class FieldPathTypes {

  private FieldPathTypes() {
  }

  // Keeping this here for backwards compatibility.
  public static StdType validatePathOnInputType(
      RowSelectorAwareFieldPath syntaxTree,
      StdType inputType,
      FormatSpecificTypeDataProvider typeDataProvider) {
    return SemanticValidator.validatePathOnInputType(syntaxTree, inputType, typeDataProvider);
  }

  // TODO: add error message as parameter
  public static <T> T checkTypeAndCast(StdType type, Class<T> expectedTypeClass) {
    if (!expectedTypeClass.isInstance(type)) {
      throw new SemanticException(format("Expected %s, found %s", expectedTypeClass.getName(), type.getClass()));
    }
    return expectedTypeClass.cast(type);
  }

  public static int getStdStructTypeFieldIndex(StdStructType structType, String value,
      boolean hasCaseSensitiveFieldNames) {
    requireNonNull(value);
    for (int i = 0; i < structType.fieldNames().size(); i++) {
      // handle case sensitivity based field comparison
      if ((!hasCaseSensitiveFieldNames && value.equalsIgnoreCase(structType.fieldNames().get(i)))
      || (hasCaseSensitiveFieldNames && value.equals(structType.fieldNames().get(i)))) {
        return i;
      }
    }
    // index -1 indicates field not found
    return -1;
  }

  public static StdType getArrayType(StdType type, StdFactory factory) {

    String newTypeSignature = new StringBuilder()
        .append("array(")
        .append(getTypeSignature(type))
        .append(")")
        .toString();

    return factory.createStdType(newTypeSignature);
  }

  public static StdType getMapEntryType(StdMapType mapType, FormatSpecificTypeDataProvider factory) {

    String newTypeSignature = new StringBuilder()
        .append("row(")
        .append("key " + getTypeSignature(mapType.keyType()))
        .append(", ")
        .append("value " + getTypeSignature(mapType.valueType()))
        .append(")")
        .toString();

    return factory.createConcreteStdType(newTypeSignature);
  }

  // TODO: This method inverts the logic in this link:
  //  https://github.com/linkedin/transport/blob/master/transportable-udfs-type-system/src/main/java/com/
  //  linkedin/transport/typesystem/TypeSignature.java#L146
  //  Does this belong in transport? Or are there better APIs to create types?
  //  See if we can use AbstractTypeFactory<T> instead.
  public static String getTypeSignature(StdType type) {
    if (type instanceof StdBooleanType) {
      return "boolean";
    }
    if (type instanceof StdIntegerType) {
      return "integer";
    }
    if (type instanceof StdLongType) {
      return "bigint";
    }
    if (type instanceof StdStringType) {
      return "varchar";
    }
    if (type instanceof StdFloatType) {
      return "real";
    }
    if (type instanceof StdDoubleType) {
      return "double";
    }
    if (type instanceof StdBinaryType) {
      return "varbinary";
    }
    if (type instanceof StdUnknownType) {
      return "unknown";
    }
    if (type instanceof StdArrayType) {
      StdArrayType arrayType = (StdArrayType) type;
      return new StringBuilder()
          .append("array(")
          .append(getTypeSignature(arrayType.elementType()))
          .append(")")
          .toString();
    }
    if (type instanceof StdMapType) {
      StdMapType mapType = (StdMapType) type;
      return new StringBuilder()
          .append("map(")
          .append(getTypeSignature(mapType.keyType()))
          .append(",")
          .append(getTypeSignature(mapType.valueType()))
          .toString();
    }
    if (type instanceof StdStructType) {
      StdStructType structType = (StdStructType) type;
      StringBuilder structTypeSignature = new StringBuilder();
      structTypeSignature.append("row(");
      for (int i = 0; i < structType.fieldTypes().size(); i++) {
        String fieldName = structType.fieldNames().get(i);
        String fieldTypeSignature = getTypeSignature(structType.fieldTypes().get(i));
        structTypeSignature.append(fieldName + " " + fieldTypeSignature);
        if (i != structType.fieldTypes().size() - 1) {
          structTypeSignature.append(", ");
        }
      }

      structTypeSignature.append(")");
      return structTypeSignature.toString();
    }

    throw new RuntimeException("StdType not supported: " + type);
  }

  public static boolean isSelectedType(StdData data, String selectedType) {
    switch (selectedType) {
      case "string": return data instanceof StdString;
      case "integer": return (data instanceof StdInteger) || (data instanceof StdLong);
      case "long": return data instanceof StdLong;
      case "boolean": return data instanceof StdBoolean;
      case "float": return (data instanceof StdFloat) || (data instanceof StdDouble);
      case "double": return data instanceof StdDouble;
      default: return false;
    }
  }
}
