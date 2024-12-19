SELECT
    count(redact_field_if(
        true, string_field_1, 'string_field_1', null))
FROM inputTableName