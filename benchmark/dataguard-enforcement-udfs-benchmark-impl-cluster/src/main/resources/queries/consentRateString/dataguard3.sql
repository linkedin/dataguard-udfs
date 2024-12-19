SELECT
    count(redact_field_if(
        rand() < 0.75, string_field_1, 'string_field_1', null))
FROM inputTableName