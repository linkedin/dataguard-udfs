SELECT
    count(redact_field_if(
        true, wide_shallow_struct_field_4, 'wide_shallow_struct_field_4', null))
FROM inputTableName