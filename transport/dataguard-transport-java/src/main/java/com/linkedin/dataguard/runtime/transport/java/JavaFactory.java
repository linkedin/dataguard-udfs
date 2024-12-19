package com.linkedin.dataguard.runtime.transport.java;

import com.linkedin.dataguard.runtime.transport.java.data.JavaArray;
import com.linkedin.dataguard.runtime.transport.java.data.JavaBinary;
import com.linkedin.dataguard.runtime.transport.java.data.JavaBoolean;
import com.linkedin.dataguard.runtime.transport.java.data.JavaDouble;
import com.linkedin.dataguard.runtime.transport.java.data.JavaFloat;
import com.linkedin.dataguard.runtime.transport.java.data.JavaInteger;
import com.linkedin.dataguard.runtime.transport.java.data.JavaLong;
import com.linkedin.dataguard.runtime.transport.java.data.JavaMap;
import com.linkedin.dataguard.runtime.transport.java.data.JavaString;
import com.linkedin.dataguard.runtime.transport.java.data.JavaStruct;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdBinary;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdDouble;
import com.linkedin.transport.api.data.StdFloat;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.data.StdLong;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdString;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.typesystem.TypeSignature;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Implementation of Transport's {@link StdFactory} for the testing type system. This implements methods to create
 * {@link StdData} and {@link StdType} objects within the type system e.g. JavaStructType, JavaStringType, JavaFloat,
 * JavaInteger etc.
 *
 * This class is modeled in a similar way to the AvroFactory implementation in Transport.
 */
public class JavaFactory implements StdFactory {

  private final JavaBoundVariables boundVariables;
  private final JavaTypeFactory typeFactory;

  public JavaFactory() {
    this(new JavaBoundVariables());
  }

  public JavaFactory(JavaBoundVariables variables) {
    this.boundVariables = variables;
    this.typeFactory = new JavaTypeFactory();
  }

  @Override
  public StdInteger createInteger(int value) {
    return new JavaInteger(value);
  }

  @Override
  public StdLong createLong(long value) {
    return new JavaLong(value);
  }

  @Override
  public StdBoolean createBoolean(boolean value) {
    return new JavaBoolean(value);
  }

  @Override
  public StdString createString(String value) {
    if (value == null) {
      return null;
    }
    return new JavaString(value);
  }

  @Override
  public StdFloat createFloat(float value) {
    return new JavaFloat(value);
  }

  @Override
  public StdDouble createDouble(double value) {
    return new JavaDouble(value);
  }

  @Override
  public StdBinary createBinary(ByteBuffer value) {
    if (value == null) {
      return null;
    }
    return new JavaBinary(value);
  }

  @Override
  public StdArray createArray(StdType stdType, int expectedSize) {
    return new JavaArray(this);
  }

  @Override
  public StdArray createArray(StdType stdType) {
    return createArray(stdType, 0);
  }

  @Override
  public StdMap createMap(StdType stdType) {
    return new JavaMap(this);
  }

  @Override
  public StdStruct createStruct(List<String> fieldNames, List<StdType> fieldTypes) {
    return new JavaStruct(this, fieldNames, Collections.nCopies(fieldNames.size(), null));
  }

  @Override
  public StdStruct createStruct(List<StdType> fieldTypes) {
    final String fieldNamePrefix = "field";
    final List<String> fieldNames = new ArrayList<>();
    IntStream.range(0, fieldTypes.size()).forEachOrdered(n -> {
      fieldNames.add(fieldNamePrefix + n);
    });
    return createStruct(fieldNames, fieldTypes);
  }

  @Override
  public StdStruct createStruct(StdType stdType) {
    // Based on StdFactory docs, this stdType is guaranteed
    // to be a StdStructType
    StdStructType structType = ((StdStructType) stdType);
    return createStruct(structType.fieldNames(),
        structType.fieldTypes().stream().collect(Collectors.toList()));
  }

  @Override
  public StdType createStdType(String typeSignature) {
    return JavaUtil.createStdType(typeFactory.createType(TypeSignature.parse(typeSignature), boundVariables));
  }

  private void writeObject(java.io.ObjectOutputStream out) {
  }
  private void readObject(java.io.ObjectInputStream in)
      throws IOException, ClassNotFoundException {
  }
  private void readObjectNoData()
      throws ObjectStreamException {
  }
}
