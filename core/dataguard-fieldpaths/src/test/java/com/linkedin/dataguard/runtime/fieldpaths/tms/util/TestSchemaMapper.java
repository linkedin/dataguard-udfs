package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestSchemaMapper {

  @Test
  public void testMapSchema() {
    List<String> fromSchema = ImmutableList.of(
        "simpleField",
        "caseInsensitiveField",
        "fromOnlyField",
        "fromFieldNullable[type=foo]",
        "toFieldNullable",
        "structField.fromOnly",
        "structField.both",
        "realUnion[type=foo1].subField",
        "realUnion[type=foo2].subField",
        "fromNullableArrayOfStruct[type=arrayType1].[type=fromStruct1].subfield",
        "toNullableArrayOfStruct.[type=fromStruct2].subfield",
        "fromNullableMap[type=mapType1].[value=fromMapValue].toNullable",
        "toNullableMap.[key=fromMapKey]",
        "bigStruct.subfield.[value=map].[value=float]"
    );

    List<String> toSchema = ImmutableList.of(
        "simpleField",
        "CASEinsensitiveFIELD",
        "toOnlyField",
        "fromFieldNullable",
        "toFieldNullable[type=foo]",
        "structField.toOnly",
        "structField.both",
        "realUnion[type=foo1].subField",
        "realUnion[type=FOO2].subField",
        "fromNullableArrayOfStruct.[type=toStruct1].subfield",
        "toNullableArrayOfStruct[type=arrayType1].[type=toStruct2].subfield",
        "fromNullableMap.[value=toMapValue].toNullable[type=foo]",
        "toNullableMap[type=mapType2].[key=fromMapKey]",
        "bigStruct[type=struct].subfield.[value=map].[value=float]"
    );

    Map<String, String> expected = new HashMap<>();
    expected.put("simpleField", "simpleField");
    expected.put("caseInsensitiveField", "CASEinsensitiveFIELD");
    expected.put("fromOnlyField", null);
    expected.put("fromFieldNullable[type=foo]", "fromFieldNullable");
    expected.put("toFieldNullable", "toFieldNullable[type=foo]");
    expected.put("structField.fromOnly", null);
    expected.put("structField.both", "structField.both");
    expected.put("realUnion[type=foo1].subField", "realUnion[type=foo1].subField");
    expected.put("realUnion[type=foo2].subField", "realUnion[type=FOO2].subField");
    expected.put("fromNullableArrayOfStruct[type=arrayType1].[type=fromStruct1].subfield", "fromNullableArrayOfStruct.[type=toStruct1].subfield");
    expected.put("toNullableArrayOfStruct.[type=fromStruct2].subfield", "toNullableArrayOfStruct[type=arrayType1].[type=toStruct2].subfield");
    expected.put("fromNullableMap[type=mapType1].[value=fromMapValue].toNullable", "fromNullableMap.[value=toMapValue].toNullable[type=foo]");
    expected.put("toNullableMap.[key=fromMapKey]", "toNullableMap[type=mapType2].[key=fromMapKey]");
    expected.put("bigStruct.subfield.[value=map].[value=float]", "bigStruct[type=struct].subfield.[value=map].[value=float]");

    Map<String, String> actual = SchemaMapper.mapSchema(fromSchema, toSchema);
    assertEquals(expected, actual);
  }

}
