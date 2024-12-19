package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Enforcer;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerFactory;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.ParsingException;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticException;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ContextVariable;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.RowSelectorAwareFieldPath;
import com.linkedin.transport.api.types.StdType;
import java.util.Optional;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.Parsing.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.FieldPathTypes.*;
import static java.lang.String.*;
import static java.util.Objects.*;


/**
 * This is the core API class that wrappers should depend on to work with field paths. It provides utilities for
 * type validation, extraction and enforcement.
 */
public final class DataGuardPathOperations {

  private final FormatSpecificTypeDataProvider typeDataProvider;

  public DataGuardPathOperations(FormatSpecificTypeDataProvider typeDataProvider) {
    this.typeDataProvider = requireNonNull(typeDataProvider);
  }

  /**
   * Checks whether the {@code rowSelectorAwareFieldPath}
   * - conforms to the `rowSelectorAwareFieldPath` rules in the grammar AND
   * - when applied on the StdType {@code schema}, points to a valid field address.
   * ("valid" implies that the sequence of operations defined by the field path can be applied
   * on a record conforming to the schema to retrieve values out of the record.)
   *
   * @return The type of the field address represented by {@code rowSelectorAwareFieldPath}.
   *
   * @throws {ParseException} for parsing failure
   * @throws {SemanticException} for semantic validation failure
   */
  public StdType validateFieldPathAndGetType(String rowSelectorAwareFieldPath, StdType schema)
      throws ParsingException, SemanticException {
    return validatePathOnInputType(parseRowSelectorAwareFieldPath(rowSelectorAwareFieldPath), schema, typeDataProvider);
  }

  /**
   * Concatenates {@code mountPoint} and {@code secondarySchemaFieldPath} to derive the full path. Then semantically
   * validates whether this path returns a valid field address on {@code datasetSchema}.
   *
   * @return The {@link StdType} corresponding to the field address represented by mounted path.
   * @throws {ParseException} for parsing failures on concatenated path
   * @throws {SemanticException} if validation fails on the concatenated path
   */
  public StdType validateMountingAndGetType(
      Optional<String> mountPointOptional,
      String secondarySchemaFieldPath,
      StdType datasetSchema) {
    String mountedPath = buildMountedPath(mountPointOptional.orElse(""), secondarySchemaFieldPath);
    return validatePathOnInputType(parseMountedPath(mountedPath), datasetSchema, typeDataProvider);
  }

  /**
   * Creates an enforcer object given a mountpoint, fieldpath, and the root type. This object can be used to perform
   * extraction or transformation of input data.
   *
   * @param mountPointOptional mount point string
   * @param secondarySchemaFieldPath field path string
   * @param rootType type of the record on which the fieldpath is supposed to be applied
   * @return {@link Enforcer} object that contains the recipe of enforcement for the given path
   */
  public Enforcer createEnforcer(Optional<String> mountPointOptional, String secondarySchemaFieldPath, StdType rootType) {
    // for unconditioned root field path, the entire record is redacted
    String mountedPath = buildMountedPath(mountPointOptional.orElse(""), secondarySchemaFieldPath);
    return EnforcerFactory.createEnforcer(parseMountedPath(mountedPath), rootType, typeDataProvider);
  }

  /**
   * Checks if the given {@code rowSelectorAwareFieldPath} is a conditional path for root without further operations.
   * This API can be used in offline use case to determine whether row or column enforcement should be applied
   * For row selector with root path ($), row enforcement will be applied.
   * For row selector with field path, column enforcement on the field path will be applied
   */
  public static boolean isConditionalPathForRoot(String rowSelectorAwareFieldPath) {
    RowSelectorAwareFieldPath rowSelectorAwareFieldPathNode = parseRowSelectorAwareFieldPath(rowSelectorAwareFieldPath);
    return rowSelectorAwareFieldPathNode.getRowSelectorPredicate().isPresent()
        && rowSelectorAwareFieldPathNode.getFieldPath().equals(ContextVariable.CONTEXT_VARIABLE);
  }

  public static String buildMountedPath(String mountPoint, String secondarySchemaFieldPath) {
    return format("[mount_point=(%s)]%s", mountPoint, secondarySchemaFieldPath);
  }
}
