SELECT count(
    redact_field_if(
        true,
        redact_field_if(
                true,
                wide_shallow_struct_field_5,
                'wide_shallow_struct_field_5.string_field_1',
                null),
        'wide_shallow_struct_field_5.string_field_2',
        null))
FROM inputTableName;