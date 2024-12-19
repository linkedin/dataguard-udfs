SELECT
    count(redact_field_if(
        rand() < 0.25, string_field_1, 'string_field_1', null))
FROM inputTableName