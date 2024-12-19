SELECT count(
    redact_field_if(
        true,
        wide_shallow_struct_field_5,
        'wide_shallow_struct_field_5.string_field_1',
        null))
FROM inputTableName;