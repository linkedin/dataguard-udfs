
package com.linkedin.dataguard.runtime.transport.trino.types;

import com.linkedin.transport.typesystem.AbstractTypeSystem;
import io.trino.spi.type.ArrayType;
import io.trino.spi.type.BigintType;
import io.trino.spi.type.BooleanType;
import io.trino.spi.type.DoubleType;
import io.trino.spi.type.IntegerType;
import io.trino.spi.type.MapType;
import io.trino.spi.type.RealType;
import io.trino.spi.type.RowType;
import io.trino.spi.type.TypeOperators;
import io.trino.spi.type.VarbinaryType;
import io.trino.spi.type.VarcharType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import io.trino.spi.type.Type;

/**
 * Trino-specific type system implementation.
 */
public class TrinoTypeSystem extends AbstractTypeSystem<Type> {
  public static final TypeOperators TYPE_OPERATORS = new TypeOperators();

  @Override
  protected Type getArrayElementType(Type dataType) {
    return ((ArrayType) dataType).getElementType();
  }

  @Override
  protected Type getMapKeyType(Type dataType) {
    return ((MapType) dataType).getKeyType();
  }

  @Override
  protected Type getMapValueType(Type dataType) {
    return ((MapType) dataType).getValueType();
  }

  @Override
  protected List<Type> getStructFieldTypes(Type dataType) {
    return ((RowType) dataType).getTypeParameters();
  }

  @Override
  protected boolean isUnknownType(Type dataType) {
    return false;
  }

  @Override
  protected boolean isBooleanType(Type dataType) {
    return dataType instanceof BooleanType;
  }

  @Override
  protected boolean isIntegerType(Type dataType) {
    return dataType instanceof IntegerType;
  }

  @Override
  protected boolean isLongType(Type dataType) {
    return dataType instanceof BigintType;
  }

  @Override
  protected boolean isStringType(Type dataType) {
    return dataType instanceof VarcharType;
  }

  @Override
  protected boolean isFloatType(Type dataType) {
    return dataType instanceof RealType;
  }

  @Override
  protected boolean isDoubleType(Type dataType) {
    return dataType instanceof DoubleType;
  }

  @Override
  protected boolean isBinaryType(Type dataType) {
    return dataType instanceof VarbinaryType;
  }

  @Override
  protected boolean isArrayType(Type dataType) {
    return dataType instanceof ArrayType;
  }

  @Override
  protected boolean isMapType(Type dataType) {
    return dataType instanceof MapType;
  }

  @Override
  protected boolean isStructType(Type dataType) {
    return dataType instanceof RowType;
  }

  @Override
  protected Type createBooleanType() {
    return BooleanType.BOOLEAN;
  }

  @Override
  protected Type createIntegerType() {
    return IntegerType.INTEGER;
  }

  @Override
  protected Type createLongType() {
    return BigintType.BIGINT;
  }

  @Override
  protected Type createStringType() {
    return VarcharType.VARCHAR;
  }

  @Override
  protected Type createFloatType() {
    return RealType.REAL;
  }

  @Override
  protected Type createDoubleType() {
    return DoubleType.DOUBLE;
  }

  @Override
  protected Type createBinaryType() {
    return VarbinaryType.VARBINARY;
  }

  @Override
  protected Type createUnknownType() {
    throw new UnsupportedOperationException("unsupported type UnknownType");
  }

  @Override
  protected Type createArrayType(Type elementType) {
    return new ArrayType(elementType);
  }

  @Override
  protected Type createMapType(Type keyType, Type valueType) {
    return new MapType(keyType, valueType, TYPE_OPERATORS);
  }

  @Override
  protected Type createStructType(List<String> fieldNames, List<Type> fieldTypes) {
    List<RowType.Field> fields = IntStream.range(0, fieldNames.size())
        .mapToObj(i -> new RowType.Field(Optional.of(fieldNames.get(i)), fieldTypes.get(i)))
        .collect(Collectors.toList());
    return RowType.from(fields);
  }
}

