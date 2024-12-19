SELECT
    count(redact_field_if(
        true, wide_shallow_struct_field_1, 'wide_shallow_struct_field_1', null))
FROM inputTableName