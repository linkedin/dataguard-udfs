SELECT
    count(redact_field_if(
        rand() < 0.75, wide_shallow_struct_field_1, 'wide_shallow_struct_field_1', null))
FROM inputTableName