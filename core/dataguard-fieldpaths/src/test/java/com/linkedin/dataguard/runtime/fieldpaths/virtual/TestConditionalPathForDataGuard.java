package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.DataGuardPathOperations.*;
import static org.junit.jupiter.api.Assertions.*;


public class TestConditionalPathForDataGuard {

  @Test
  public void testConditionalPathForDataGuard() {
    assertFalse(isConditionalPathForRoot("[?($.x == 2)]$.z.y[?($.z.k < 5)]"));
    assertFalse(isConditionalPathForRoot("$.z.y[?($.z.k < 5)]"));
    assertTrue(isConditionalPathForRoot("[?($.x == 2)]$"));
  }
}
