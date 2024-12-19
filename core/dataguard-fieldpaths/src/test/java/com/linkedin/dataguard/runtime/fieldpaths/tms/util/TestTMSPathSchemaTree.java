package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTMSPathSchemaTree {
  private static final List<String> SCHEMA_FIELDS = ImmutableList.of(
        "field1",
        "optionalField[type=foo]",
        "realUnionField[type=bar1]",
        "realUnionField[type=bar2]",
        "structField.subField",
        "optionalStructField[type=aStruct].subField",
        "optionalStructField[type=aStruct].optionalSubField[type=foo]",
        "unionStructField[type=fooStruct].subField",
        "unionStructField[type=barStruct].subField",
        "unionStructField[type=barStruct].optionalSubField[type=foo]",
        "arrayField.[type=foo]",
        "optionalArrayField[type=foo].[type=barElement]",
        "mapField.[key=barKeyType]",
        "optionalMapField[type=foo].[key=barKeyType]",
        "mapField.[value=barValueType]",
        "optionalMapField[type=foo].[value=barValueType]"
  );

  private static final List<String> CANONICAL_FIELDS = ImmutableList.of(
      "field1",
      "optionalField",
      "realUnionField[type=bar1]",
      "realUnionField[type=bar2]",
      "structField.subField",
      "optionalStructField.subField",
      "optionalStructField.optionalSubField",
      "unionStructField[type=fooStruct].subField",
      "unionStructField[type=barStruct].subField",
      "unionStructField[type=barStruct].optionalSubField",
      "arrayField.[type=elemType]",
      "optionalArrayField.[type=elemType]",
      "mapField.[key=keyType]",
      "optionalMapField.[key=keyType]",
      "mapField.[value=valueType]",
      "optionalMapField.[value=valueType]"
  );

  @Test
  public void testSchemaTree() {
    TMSPathSchemaTree tree = new TMSPathSchemaTree(SCHEMA_FIELDS);
    for (int i = 0; i < SCHEMA_FIELDS.size(); i++) {
      String originalPath = SCHEMA_FIELDS.get(i);
      String canonicalPath = CANONICAL_FIELDS.get(i);
      assertEquals(canonicalPath, tree.getCanonicalPath(originalPath));
      assertEquals(originalPath, tree.getOriginalPath(canonicalPath));
    }
  }

  @Test
  public void testGetOriginalPathWithNonCanonicalPath() {
    TMSPathSchemaTree tree = new TMSPathSchemaTree(SCHEMA_FIELDS);
    assertEquals("optionalField[type=foo]", tree.getOriginalPath("optionalField[type=foo]"));
    assertEquals("optionalStructField[type=aStruct].subField", tree.getOriginalPath("optionalStructField[type=aStruct].subField"));
    assertEquals("optionalStructField[type=aStruct].optionalSubField[type=foo]", tree.getOriginalPath("optionalStructField.optionalSubField[type=foo]"));
    assertEquals("optionalStructField[type=aStruct].optionalSubField[type=foo]", tree.getOriginalPath("optionalStructField[type=aStruct].optionalSubField"));
    assertEquals("optionalStructField[type=aStruct].optionalSubField[type=foo]", tree.getOriginalPath("optionalStructField.optionalSubField"));
    assertEquals("arrayField.[type=foo]", tree.getOriginalPath("arrayField.[type=anything]"));
    assertEquals("optionalArrayField[type=foo].[type=barElement]", tree.getOriginalPath("optionalArrayField.[type=anything]"));
    assertEquals("mapField.[key=barKeyType]", tree.getOriginalPath("mapField.[key=anything]"));
    assertEquals("mapField.[value=barValueType]", tree.getOriginalPath("mapField.[value=anything]"));
    assertEquals("optionalMapField[type=foo].[key=barKeyType]", tree.getOriginalPath("optionalMapField[type=foo].[key=anything]"));
    assertEquals("optionalMapField[type=foo].[value=barValueType]", tree.getOriginalPath("optionalMapField[type=foo].[value=anything]"));
  }
}
