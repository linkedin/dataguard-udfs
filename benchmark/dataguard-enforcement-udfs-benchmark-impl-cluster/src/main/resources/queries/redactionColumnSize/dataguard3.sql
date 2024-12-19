SELECT
    count(redact_field_if(
        true, wide_shallow_struct_field_3, 'wide_shallow_struct_field_3', null))
FROM inputTableName