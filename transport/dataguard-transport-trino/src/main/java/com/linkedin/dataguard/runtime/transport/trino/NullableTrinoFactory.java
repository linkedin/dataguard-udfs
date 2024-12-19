package com.linkedin.dataguard.runtime.transport.trino;

import com.linkedin.dataguard.runtime.transport.trino.data.NullableTrinoArray;
import com.linkedin.dataguard.runtime.transport.trino.data.NullableTrinoMap;
import com.linkedin.dataguard.runtime.transport.trino.data.NullableTrinoStruct;
import com.linkedin.dataguard.runtime.transport.trino.types.TrinoTypeFactory;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdBinary;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdDouble;
import com.linkedin.transport.api.data.StdFloat;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.data.StdLong;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdString;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.trino.data.TrinoBinary;
import com.linkedin.transport.trino.data.TrinoBoolean;
import com.linkedin.transport.trino.data.TrinoDouble;
import com.linkedin.transport.trino.data.TrinoFloat;
import com.linkedin.transport.trino.data.TrinoInteger;
import com.linkedin.transport.trino.data.TrinoLong;
import com.linkedin.transport.trino.data.TrinoString;
import com.linkedin.transport.typesystem.AbstractBoundVariables;
import com.linkedin.transport.typesystem.TypeSignature;
import io.airlift.slice.Slices;
import io.trino.spi.type.ArrayType;
import io.trino.spi.type.MapType;
import io.trino.spi.type.RowType;
import io.trino.spi.type.Type;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;


/**
 * Ported and modified from {@link com.linkedin.transport.trino.TrinoFactory}. A TrinoFactory implementation that supports
 * null values. The differences are that this implementation uses {@link NullableTrinoStruct}, {@link NullableTrinoArray},
 * and {@link NullableTrinoMap}. Also, this implementation does not support (and need) binding for generic types.
 */
public class NullableTrinoFactory implements StdFactory {

  private static final long serialVersionUID = 1L;
  final transient TrinoTypeFactory trinoTypeFactory;
  final transient AbstractBoundVariables<Type> boundVariables;

  public NullableTrinoFactory(AbstractBoundVariables<Type> boundVariables) {
    this.trinoTypeFactory = new TrinoTypeFactory();
    this.boundVariables = checkNotNull(boundVariables);
  }

  @Override
  public StdInteger createInteger(int value) {
    return new TrinoInteger(value);
  }

  @Override
  public StdLong createLong(long value) {
    return new TrinoLong(value);
  }

  @Override
  public StdBoolean createBoolean(boolean value) {
    return new TrinoBoolean(value);
  }

  @Override
  public StdString createString(String value) {
    checkNotNull(value, "Cannot create a null StdString");
    return new TrinoString(Slices.utf8Slice(value));
  }

  @Override
  public StdFloat createFloat(float value) {
    return new TrinoFloat(value);
  }

  @Override
  public StdDouble createDouble(double value) {
    return new TrinoDouble(value);
  }

  @Override
  public StdBinary createBinary(ByteBuffer value) {
    return new TrinoBinary(Slices.wrappedBuffer(value.array()));
  }

  @Override
  public StdArray createArray(StdType stdType, int expectedSize) {
    return new NullableTrinoArray((ArrayType) stdType.underlyingType(), expectedSize, this);
  }

  @Override
  public StdArray createArray(StdType stdType) {
    return createArray(stdType, 0);
  }

  @Override
  public StdMap createMap(StdType stdType) {
    return new NullableTrinoMap((MapType) stdType.underlyingType(), this);
  }

  @Override
  public NullableTrinoStruct createStruct(List<String> fieldNames, List<StdType> fieldTypes) {
    return new NullableTrinoStruct(fieldNames,
        fieldTypes.stream().map(stdType -> (Type) stdType.underlyingType()).collect(Collectors.toList()), this);
  }

  @Override
  public NullableTrinoStruct createStruct(List<StdType> fieldTypes) {
    return new NullableTrinoStruct(
        fieldTypes.stream().map(stdType -> (Type) stdType.underlyingType()).collect(Collectors.toList()), this);
  }

  @Override
  public StdStruct createStruct(StdType stdType) {
    return new NullableTrinoStruct((RowType) stdType.underlyingType(), this);
  }

  @Override
  public StdType createStdType(String typeSignatureStr) {
    Type type = trinoTypeFactory.createType(TypeSignature.parse(typeSignatureStr), boundVariables);
    return NullableTrinoWrapper.createStdType(type);
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }
}
