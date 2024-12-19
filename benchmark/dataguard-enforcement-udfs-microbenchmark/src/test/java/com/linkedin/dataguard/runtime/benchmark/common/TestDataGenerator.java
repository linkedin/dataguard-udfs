package com.linkedin.dataguard.runtime.benchmark.common;

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public abstract class TestDataGenerator {

  public abstract FormatSpecificTypeDataProvider getTypeDataProvider();

  public abstract Class<?> getStructClass();

  @Test
  public void testDataGenerator() {
    DataGenerator gen = new DataGenerator(getTypeDataProvider(), Optional.of(5));
    StdType type = gen.getTypeDataProvider().getStdFactory()
        .createStdType("row(array_50entries array(row(section varchar, terms array(row(term varchar, value real)))))");
    StdData data = gen.generateRecord("", type, 1000000);
    assertThat(data.getClass()).isEqualTo(getStructClass());
  }
}
