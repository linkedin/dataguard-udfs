SELECT
    count(redact_field_if(
            false, wide_shallow_struct_field_1, 'wide_shallow_struct_field_1.string_field_1', null))
FROM inputTableName