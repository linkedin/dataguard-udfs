## Testing Implementation for Transport

- The enforcement engine in this MP relies on [Transport Type System](https://github.com/linkedin/transport). The type 
  system supports various formats like Avro, Hive, Spark, Trino etc.
- The core engine implementation in virtual-fields-representation-parser. There will be format-specific wrappers 
  in other subprojects like virtual-fields-representation-avro.
- However, for end-to-end testing of the engine functionality, we do not want to depend on a specific format. This 
  subproject contains a "testing" type system, which helps with e2e testing for fieldpath enforcement.