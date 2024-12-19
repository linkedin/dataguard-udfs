SELECT count(
    redact_secondary_schema_field_if(
        true,
        array_field_XXXXentries,
        array('$.array_field_XXXXentries[?(@.string_field_1 == \'charlie104\' || true)][:].string_field_2'),
        'array_field_XXXXentries',
        'UNKNOWN'))
FROM inputTableName;