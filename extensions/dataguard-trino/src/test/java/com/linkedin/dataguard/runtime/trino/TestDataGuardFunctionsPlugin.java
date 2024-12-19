package com.linkedin.dataguard.runtime.trino;

import com.google.common.collect.ImmutableList;
import io.trino.Session;
import io.trino.spi.type.TimeZoneKey;
import io.trino.testing.AbstractTestQueryFramework;
import io.trino.testing.LocalQueryRunner;
import io.trino.testing.MaterializedResult;
import io.trino.testing.QueryRunner;
import io.trino.testing.TestingSession;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class TestDataGuardFunctionsPlugin extends AbstractTestQueryFramework {
  private static final Session SESSION = TestingSession.testSessionBuilder()
      .setTimeZoneKey(TimeZoneKey.UTC_KEY)
      .build();

  @Test
  public void testRedactFieldIf() {
    assertQuery(
        "SELECT REDACT_FIELD_IF(false, 5, 'x', NULL)",
        "SELECT 5");

    MaterializedResult result = computeActual("SELECT "
        + "REDACT_FIELD_IF(false, CAST(ROW(5, 6) AS ROW(a int, b int)), 'x.y', NULL)");
    assertEquals(result.getRowCount(), 1);
    assertEquals(result.getOnlyValue(), ImmutableList.of(5, 6));
  }

  @Override
  protected QueryRunner createQueryRunner() {
    LocalQueryRunner localQueryRunner = LocalQueryRunner.create(SESSION);
    // TODO: create memory catalog if needed for testing
    localQueryRunner.installPlugin(new DataGuardFunctionsPlugin());
    return localQueryRunner;
  }

}
