SELECT count(redact_field_if(
    true,
    deep_struct_field_5,
    'deep_struct_field_5.deep_struct_field_4.deep_struct_field_3.deep_struct_field_2.deep_struct_field_1.string_field_1',
    null))
FROM inputTableName;