package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import java.util.List;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.util.TMSPathUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestTMSPathUtils {

  @Test
  public void testParser() {
    assertTrue(parseTMSPath("foo").getTMSPathSelectors().get(0) instanceof StructFieldSelector);
    assertThrows(RuntimeException.class, () -> parseTMSPath("invalid[]path"));
  }

  @Test
  public void testToCanonicalTMSPath() {
    assertEquals("structField[type=fooArray].[type=elemType].[value=valueType].[key=keyType]",
        toCanonicalTMSPath(parseTMSPath("structField[type=fooArray].[type=fooElement].[value=fooValue].[key=fooKey]").getTMSPathSelectors()));
  }

  @Test
  public void testIsSameSelector() {
    testIsSameSelectorHelper("foo.field1", "foo.field1", true);
    testIsSameSelectorHelper("foo.field1", "foo.fIEld1", true);
    testIsSameSelectorHelper("foo.field1", "foo.field2", false);
    testIsSameSelectorHelper("foo[type=type1]", "foo[type=type1]", true);
    testIsSameSelectorHelper("foo[type=type1]", "foo[type=tYpE1]", true);
    testIsSameSelectorHelper("foo[type=type1]", "foo[type=type2]", false);
    testIsSameSelectorHelper("foo.[type=array1]", "foo.[type=array2]", true);
    testIsSameSelectorHelper("foo.[type=array1]", "foo.[value=array1]", false);
  }

  private void testIsSameSelectorHelper(String path1, String path2, boolean expected) {
    TMSPathSelector selector1 = getLastSelector(path1);
    TMSPathSelector selector2 = getLastSelector(path2);
    assertEquals(expected, isSameSelector(selector1, selector2));
  }

  private TMSPathSelector getLastSelector(String tmsPath) {
    List<TMSPathSelector> selectors = parseTMSPath(tmsPath).getTMSPathSelectors();
    return selectors.get(selectors.size() - 1);
  }
}
