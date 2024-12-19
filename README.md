# README

## Overview

This repository contains the implementation of enforcement UDFs. 

- `core`: contains modules for field path representation, and engine-agnostic enforcement implementation 
- `extensions`: contains engine-specifc UDF implementations
- `transport`: contains extensions to generic type system implementations in [transport](https://github.com/linkedin/transport/)
- `benchmark`: contains code for benchmarking UDF performance (both microbenchmarks and cluster benchmarks)

## Development 

### Building the repo

```agsl
./gradlew build
```

### Core Modules

The [dataguard-fieldpaths](core%2Fdataguard-fieldpaths) module in the `core` directory contains reusable logic for redacting 
fields, implemented in an engine-agnostic way. 

The grammar for representing fieldpaths is described in [VirtualFieldPath.g4](core%2Fdataguard-fieldpaths%2Fsrc%2Fmain%2Fantlr%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Ffieldpaths%2Fvirtual%2FVirtualFieldPath.g4),
and processed using ANTLR library. The code for parsing, semantic analysis and enforcement corresponding to this grammar 
is present in [com.linkedin.dataguard.runtime.fieldpaths.virtual](core%2Fdataguard-fieldpaths%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Ffieldpaths%2Fvirtual) 
module.

A non-trivial number of field paths in LinkedIn's data catalog exist in a slightly different 
and less expressive legacy language. This module also contains code for processing those fieldpaths under [TMSPath.g4](core%2Fdataguard-fieldpaths%2Fsrc%2Fmain%2Fantlr%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Ffieldpaths%2Ftms%2FTMSPath.g4). 
The code for processing the legacy field paths is in [com.linkedin.dataguard.runtime.fieldpaths.tms](core%2Fdataguard-fieldpaths%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Ffieldpaths%2Ftms)
module. 

### Transport Modules

Enforcement code leverages the generic type system (StdType and StdData objects) defined in [transport](https://github.com/linkedin/transport/) library 
in order to implement the engine-agnostic enforcement logic. This common code is then leveraged in engine-specific UDFs, by providing 
engine-specific implementations for the generic objects.

However, the implementation in transport library has some gaps, e.g. the objects are non-nullable. It also lacks 
APIs (e.g. `FormatSpecificTypeDataProvider`) relevant for enforcement, and restricts the ability to perform optimizations. So
we extend the implementations in modules under `transport/` directory to modify the transport implementations and add 
APIs as needed. 

- `dataguard-transport-common`: Defines common APIs to be used in enforcement
- `dataguard-transport-java`: A Java implementation for transport API, useful for testing enforcement code in a type-agnostic way
- `dataguard-transport-spark`: extension of Spark type-system implementation from transport library
- `dataguard-transport-trino`: extension of Trino type-system implementation from transport library

### Extensions

The `extensions/` directory contains engine-specific implementations of enforcement code.

#### Spark

Contains the following UDFs to perform enforcement:
- [RedactFieldIfUDF.scala](extensions%2Fdataguard-spark%2Fsrc%2Fmain%2Fscala%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Fudf%2Fspark%2FRedactFieldIfUDF.scala) and 
- [RedactSecondarySchemaFieldIfUDF.scala](extensions%2Fdataguard-spark%2Fsrc%2Fmain%2Fscala%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Fudf%2Fspark%2FRedactSecondarySchemaFieldIfUDF.scala)

As described in the [Core Modules](#Core Modules) section, LinkedIn's metadata ecosystem contains fieldpaths in two languages,
one of them being legacy, and less expressive. The two UDFs implement redaction logic corresponding to the fieldpaths 
represented in the two languages. But they can be combined into a single UDF as described in the accompanying VLDB submission.

Note that for implementing the UDFs, we use the [Expression API](https://github.com/apache/spark/blob/v3.1.1/sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/expressions/Expression.scala) 
in Spark. This lets us define generic enforcement UDFs where input and output column types do not need to be pre-defined, as 
opposed to the [Scalar UDF API](https://spark.apache.org/docs/3.5.2/sql-ref-functions-udf-scalar.html). The other approach 
for defining such generic UDFs is [Hive GenericUDF API](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDF.java), but 
we go with the Spark native imlementation to avoid overhead associated with data conversion between `Hive<->Spark` during 
UDF execution.

#### Trino

For Trino UDF implementation, the [plugin SPI](https://trino.io/docs/current/develop/functions.html) is used.
The implementation can be found in [RedactFieldIf.java](extensions%2Fdataguard-trino%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fdataguard%2Fruntime%2Ftrino%2FRedactFieldIf.java). 
Note that we only implement the UDF for one of the two languages, since all Trino usecases for policy enforcement so far have been 
limited to datasets with metadata defined using only one of those languages.

### Benchmarking

#### Microbechmarking

The [dataguard-enforcement-udfs-microbenchmark](benchmark%2Fdataguard-enforcement-udfs-microbenchmark) module contains 
micro-benchmarking code and scenarios implemented using the [JMH framework](https://github.com/openjdk/jmh). 

The microbenchmarking is done for Spark UDFs.

```agsl
./gradlew :benchmark:dataguard-enforcement-udfs-microbenchmark:jmhExec
```

This will also generate a CPU profile by default under `build/` directory.

#### Cluster Benchmarking

The [dataguard-enforcement-udfs-benchmark-impl-cluster](benchmark%2Fdataguard-enforcement-udfs-benchmark-impl-cluster) module 
contains code to benchmark queries for various scenarios on a Spark cluster.

For running cluster benchmark locally:

1. [one-time] Download and extract [Apache Spark](https://archive.apache.org/dist/spark/spark-3.1.1/spark-3.1.1-bin-hadoop2.7.tgz), 
  and update `PATH` env variable.

```agsl
export SPARK_HOME=/Users/padesai/Downloads/spark-3.1.1-bin-hadoop2.7
export PATH=$SPARK_HOME/bin:$PATH
```

2. Build the project with `shadowJar` configuration

```agsl
./gradlew :benchmark:dataguard-enforcement-udfs-benchmark-impl-cluster:shadowJar
```

3. Execute the spark-submit command to launch the `DataGenerator` or `QueryRunner` class.

```agsl
spark-submit --class com.linkedin.dataguard.benchmark.enforcement.DataGenerator --master local[*] benchmark/dataguard-enforcement-udfs-benchmark-impl-cluster/build/libs/dataguard-enforcement-udfs-benchmark-impl-cluster-1.0-all.jar
```

For running this on your Yarn cluster, please use `--master yarn` instead of `local[*]`, and refer to the instructions 
[here](https://archive.apache.org/dist/spark/docs/3.1.1/running-on-yarn.html#launching-spark-on-yarn).

