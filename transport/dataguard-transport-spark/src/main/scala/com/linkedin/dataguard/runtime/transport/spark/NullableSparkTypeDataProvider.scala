package com.linkedin.dataguard.runtime.transport.spark

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider
import com.linkedin.transport.api.StdFactory
import com.linkedin.transport.spark.typesystem.SparkBoundVariables
import com.linkedin.transport.typesystem.AbstractBoundVariables
import org.apache.spark.sql.types.DataType

/**
 * Create the Spark-specific type data provider using NullableSparkFactory
 */
class NullableSparkTypeDataProvider extends FormatSpecificTypeDataProvider {

  override def hasCaseSensitiveFieldNames: Boolean = false

  override def getStdFactory: StdFactory = new NullableSparkFactory(new SparkBoundVariables());

  override def getStdFactoryWithBinding(boundVariables: AbstractBoundVariables[_]): StdFactory =
    boundVariables match {
      case bv: AbstractBoundVariables[DataType] => new NullableSparkFactory(bv)
      case _ => throw new IllegalArgumentException("Invalid type for boundVariables")
    }

  override def createAbstractBoundVariables: AbstractBoundVariables[_] = new SparkBoundVariables()
}
