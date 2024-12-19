SELECT count(
    redact_field_if(true, array_field_XXXXentries, 'array_field_XXXXentries.[type=struct].string_field_2', null))
FROM inputTableName;