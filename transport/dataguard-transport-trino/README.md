## Trino Extension for Transport

- The type and data classes in this MP are ported from [Transport](https://github.com/linkedin/transport) and modified to
  support setting nullable fields in maps, arrays and structs, which is a fundamental requirement for field redaction.