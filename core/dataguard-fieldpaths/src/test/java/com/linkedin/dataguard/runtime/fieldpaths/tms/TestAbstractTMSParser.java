package com.linkedin.dataguard.runtime.fieldpaths.tms;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.ArrayElementSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapKeySelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapValueSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.UnionTypeSelector;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TestAbstractTMSParser {

  protected abstract TMSParser getParser();

  @Test
  public void testPaths() throws Exception {
    String tmsPath = "response.[key=CompoundKey].jobPosting.$topLevelTyperefRoot$";
    assertEquals(parsedSelectors(tmsPath), ImmutableList.of(field("response"), key("CompoundKey"), field("jobPosting"), field("$topLevelTyperefRoot$")));
    assertEquals(toPegasusPathSpec(tmsPath, 3), "response/$key/jobPosting");

    tmsPath = "response.[key=CompoundKey].traceId.$topLevelFixedRoot$";
    assertEquals(parsedSelectors(tmsPath), ImmutableList.of(field("response"), key("CompoundKey"), field("traceId"), field("$topLevelFixedRoot$")));
    assertEquals(toPegasusPathSpec(tmsPath, 3), "response/$key/traceId");

    tmsPath = "response.[key=CompoundKey].reason.$topLevelEnumRoot$";
    assertEquals(parsedSelectors(tmsPath), ImmutableList.of(field("response"), key("CompoundKey"), field("reason"), field("$topLevelEnumRoot$")));
    assertEquals(toPegasusPathSpec(tmsPath, 3), "response/$key/reason");

    assertEquals(parsedSelectors("[key=com.linkedin.foo].z"), ImmutableList.of(key("com.linkedin.foo"), field("z")));
    assertEquals(toPegasusPathSpec("[key=com.linkedin.foo].z", 2), "$key/z");

    assertEquals(parsedSelectors("xab.ydz"), ImmutableList.of(field("xab"), field("ydz")));
    assertEquals(toPegasusPathSpec("xab.ydz", 2), "xab/ydz");

    assertEquals(parsedSelectors("x.[type=com.linkedin.foo]"), ImmutableList.of(field("x"), array("com.linkedin.foo")));
    assertEquals(toPegasusPathSpec("x.[type=com.linkedin.foo]", 2), "x/*");

    assertEquals(parsedSelectors("x[type=com.linkedin.foo]"), ImmutableList.of(field("x"), unionType("com.linkedin.foo")));
    assertEquals(toPegasusPathSpec("x[type=com.linkedin.foo]", 2), "x/com.linkedin.foo");

    assertEquals(parsedSelectors("x.[value=union][type=com.linkedin.foo_bar]"), ImmutableList.of(field("x"), value("union"), unionType("com.linkedin.foo_bar")));
    assertEquals(toPegasusPathSpec("x.[value=union][type=com.linkedin.foo_bar]", 3), "x/*/com.linkedin.foo_bar");

    assertEquals(parsedSelectors("x.[type=union][type=jk]"), ImmutableList.of(field("x"), array("union"), unionType("jk")));
    assertEquals(toPegasusPathSpec("x.[type=union][type=jk]", 3), "x/*/jk");

    assertEquals(parsedSelectors("x[type=union][type=jk]"), ImmutableList.of(field("x"), unionType("union"), unionType("jk")));
    assertEquals(toPegasusPathSpec("x[type=union][type=jk]", 3), "x/union/jk");

    assertEquals(parsedSelectors("[value=array<string>]"), ImmutableList.of(value("array<string>")));
    assertEquals(toPegasusPathSpec("[value=array<string>]", 1), "*");

    assertEquals(parsedSelectors("[key=struct<z:int,w:int>].z"), ImmutableList.of(key("struct<z:int,w:int>"), field("z")));
    assertEquals(toPegasusPathSpec("[key=struct<z:int,w:int>].z", 2), "$key/z");

    assertEquals(parsedSelectors("[type=abc]"), ImmutableList.of(array("abc")));
    assertEquals(toPegasusPathSpec("[type=abc]", 1), "*");

    assertEquals(parsedSelectors("[type=abc.<>-$:, k9]"), ImmutableList.of(array("abc.<>-$:, k9")));
    assertEquals(toPegasusPathSpec("[type=abc.<>-$:, k9]", 1), "*");

    assertEquals(parsedSelectors("abc._@$#|: -p9.tv"), ImmutableList.of(field("abc"), field("_@$#|: -p9"), field("tv")));
    assertEquals(toPegasusPathSpec("abc._@$#|: -p9.tv", 3), "abc/_@$#|: -p9/tv");

    assertEquals(
        parsedSelectors("abc.def[type=array].[type=string].k.[key=com.linkedin.foo].z"),
        ImmutableList.of(
            field("abc"), field("def"), unionType("array"), array("string"), field("k"), key("com.linkedin.foo"), field("z")));
    assertEquals(toPegasusPathSpec("abc.def[type=array].[type=string].k.[key=com.linkedin.foo].z", 7), "abc/def/array/*/k/$key/z");
  }

  @Test
  public void testBadPaths() {
    assertThrowsParsingException("x.[type=].z");
    assertThrowsParsingException("x.[key=]");
    assertThrowsParsingException("x.[value=]");
    assertThrowsParsingException("x[type=]");
    assertThrowsParsingException("x[key=y]");
    assertThrowsParsingException("x.[key=bc[type=a]]");
    assertThrowsParsingException("x.y.[type=j[type=xy].y");
    assertThrowsParsingException("x.l,k.p");
    assertThrowsParsingException("x..z");
    assertThrowsParsingException(".y");
    assertThrowsParsingException("x.l<>k.p");
    assertThrowsParsingException(".[type=abc]");
  }

  private void assertThrowsParsingException(String path) {
    assertThrows(TMSParsingException.class, () -> parsedSelectors(path));
  }

  protected List<TMSPathSelector> parsedSelectors(String path) throws Exception {
    return getParser().parsePath(path).getTMSPathSelectors();
  }

  protected String toPegasusPathSpec(String tmsPath, int expectedNoOfComponents) throws Exception {
    List<String> pathSpecComponents = getParser()
        .parsePath(tmsPath)
        .toPegasusPathSpecComponents();
    assertEquals(pathSpecComponents.size(), expectedNoOfComponents);
    return pathSpecComponents
        .stream()
        .collect(Collectors.joining(ParsedTMSPath.PATHSPEC_DELIMITER));
  }

  protected static StructFieldSelector field(String name) {
    return new StructFieldSelector(name);
  }

  protected static ArrayElementSelector array(String type) {
    return new ArrayElementSelector(type);
  }

  protected static MapKeySelector key(String type) {
    return new MapKeySelector(type);
  }

  protected static MapValueSelector value(String type) {
    return new MapValueSelector(type);
  }

  protected static UnionTypeSelector unionType(String type) {
    return new UnionTypeSelector(type);
  }
}
