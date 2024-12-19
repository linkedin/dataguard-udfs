package com.linkedin.dataguard.runtime.transport.spark

import com.linkedin.transport.spark.typesystem.SparkBoundVariables
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

class TestNullableSparkTypeDataProvider {

  private val sparkNullableDataProvider: NullableSparkTypeDataProvider = new NullableSparkTypeDataProvider()
  private val sparkBoundVariables = new SparkBoundVariables()

  @Test
  def testNullableSparkArray(): Unit = {
    assertTrue(sparkNullableDataProvider.getStdFactory.isInstanceOf[NullableSparkFactory])
    assertTrue(sparkNullableDataProvider.getStdFactoryWithBinding(sparkBoundVariables).isInstanceOf[NullableSparkFactory])
    assertTrue(sparkNullableDataProvider.createAbstractBoundVariables.isInstanceOf[SparkBoundVariables])
    assertFalse(sparkNullableDataProvider.hasCaseSensitiveFieldNames)
  }
}

