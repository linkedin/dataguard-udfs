SELECT
    count(redact_field_if(
        true, wide_shallow_struct_field_2, 'wide_shallow_struct_field_2', null))
FROM inputTableName