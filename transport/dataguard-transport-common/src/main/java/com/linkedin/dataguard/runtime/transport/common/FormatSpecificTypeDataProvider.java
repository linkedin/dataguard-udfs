package com.linkedin.dataguard.runtime.transport.common;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.typesystem.AbstractBoundVariables;
import com.linkedin.transport.typesystem.TypeSignature;

/**
 * Provider class used for format specific type and data operations. This should be implemented using format-specific
 * Transport abstractions (e.g. AvroFactory, AvroBoundVariables for Avro) for every new format that we want to support
 * for enforcement through this library.
 */
public abstract class FormatSpecificTypeDataProvider {

  /**
   * This is used to determine whether field name comparison needs to ignore case or not
   * @return true if field names are case-sensitive, false otherwise
   */
  public abstract boolean hasCaseSensitiveFieldNames();

  /**
   * Creates an {@link StdFactory} without any variable binding. The factory will be used by enforcement code to
   * build {@link StdType} and {@link StdData} objects during enforcement. The implementations of this class can choose
   * to always return a singleton factory object in this method.
   *
   * @return an instance of StdFactory implementation for the format
   */
  public abstract StdFactory getStdFactory();

  /**
   * The implementations of this class should return a new factory that uses the mapping defined by
   * {@code boundVariables} to produce types.
   *
   * @return a {@link StdFactory} implementation built with the given {@code boundVariables}.
   */
  public abstract StdFactory getStdFactoryWithBinding(AbstractBoundVariables boundVariables);

  /**
   * This is used to define binding of generic {@link TypeSignature} to format-specific types (e.g. Schema for Avro).
   *
   * @return Transport's {@link AbstractBoundVariables} implementation for the format.
   */
  public abstract AbstractBoundVariables createAbstractBoundVariables();

  /**
   * This is a utility to create {@code StdType} objects for a concrete {@link TypeSignature}.
   * Transport type signatures can be defined using generic types (like `array(T)`) OR concrete types (like
   * `array(varchar)`). However, this method only accepts type signatures with non-generic AKA concrete types.
   *
   * Reference on concrete vs generic type signature:
   * {@link com.linkedin.transport.typesystem.ConcreteTypeSignatureElement}
   * {@link com.linkedin.transport.typesystem.GenericTypeSignatureElement}
   *
   * @return an instance of StdType implementation for the format that corresponds to the given {@code typeSignature}
   * @throws RuntimeException If a {@code typeSignature} with generic types is provided here, this method will throw a
   *                          runtime exception.
   */
  public StdType createConcreteStdType(String typeSignature) {
    return getStdFactory().createStdType(typeSignature);
  }
}
