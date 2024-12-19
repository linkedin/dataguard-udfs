package com.linkedin.dataguard.runtime.fieldpaths.tms;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.ArrayElementSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import java.util.List;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;
import static org.assertj.core.api.Assertions.*;


public class TestTMSEnforcer {
  @Test
  public void testIsLastSelector() {
    ArrayElementSelector arrayElementSelector = new ArrayElementSelector("test");
    List<TMSPathSelector> tmsPathSelectors = ImmutableList.of(arrayElementSelector);
    assertThat(isLastSelector(0, tmsPathSelectors)).isTrue();
    assertThat(isLastSelector(1, tmsPathSelectors)).isFalse();
  }
}