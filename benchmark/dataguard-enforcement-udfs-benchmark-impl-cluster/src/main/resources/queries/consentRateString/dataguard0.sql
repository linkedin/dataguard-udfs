SELECT
    count(redact_field_if(false, string_field_1, 'string_field_1', null))
FROM inputTableName