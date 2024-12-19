package com.linkedin.dataguard.runtime.fieldpaths.tms;

import com.google.common.collect.ImmutableSet;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.*;

public class ParsedTMSPath {
  public final static String PATHSPEC_DELIMITER = "/";
  public final static String PATHSPEC_ARRAY_SEGMENT = "*";
  public final static String PATHSPEC_MAP_KEY_SEGMENT = "$key";
  public final static String PATHSPEC_MAP_VALUE_SEGMENT = "*";
  public final static String PATHSPEC_IGNORE = "ignore";

  /*
  Adding Slack message for context, since it was sent as a private message to a group of people
  Group: @pboddu, @aabuduweili, @edlu, @ibuenros, @jezheng, @jren, @nkanamarlapudi, and @yhsieh
  Date: Sep 26th 20223
  From: @edlu

  Message:
  Summary of the resolution of the lengthy thread with SI:
  1. For the Restli method urn, SI will not inline any PDL schemas. All PDL types will be a mounted schema.
  2. SI will send the RegisteredSchema for all PDL types (including typeref, enum and fixed types, as well as records).
  3. To ensure that there is an annotatable field for these types in the RegisteredSchema, SI will use a prefix for the
  normalizedFieldPath: $topLevelTyperefRoot$ for typeref, $topLevelEnumRoot$ for enum, $topLevelFixedRoot$ for fixed
  types (or something similar).

  Current situation:
  1. There are some Restli methods and registered schema in EI, but not a complete inventory for either. This means that
  links from a restli method to an included registered schema may not work yet.
  2. The existing enum registered schemas do not have an annotatable field, so these are currently empty.
  3. The existing typeref fields may have an empty string for the fieldPath.
  Both issues 2 and 3 will be fixed, with the updates to the normalizer that SI is working on. We are currently targeting
  Friday for deployment, and will update if there are changes. We should also have more of a full inventory on Friday.
   */
  public final static Set<String> TMS_COMPONENT_TOP_LEVEL_FIELDS =
      ImmutableSet.of("$topLevelTyperefRoot$", "$topLevelEnumRoot$", "$topLevelFixedRoot$");

  private final List<TMSPathSelector> tmsPathSelectors;

  public ParsedTMSPath(List<TMSPathSelector> tmsPathSelectors) {
    this.tmsPathSelectors = requireNonNull(tmsPathSelectors, "tmsPathSelectors is null");
  }

  public List<TMSPathSelector> getTMSPathSelectors() {
    return tmsPathSelectors;
  }

  @Deprecated
  public String toPegasusPathSpec() {
    return tmsPathSelectors.stream()
        .map(tmsPathSelector -> tmsPathSelector.toPegasusPathSpecSegment())
        .filter(pegasusPathSegment -> !PATHSPEC_IGNORE.equals(pegasusPathSegment))
        .collect(Collectors.joining(PATHSPEC_DELIMITER));
  }

  public List<String> toPegasusPathSpecComponents() {
    return tmsPathSelectors.stream()
        .map(tmsPathSelector -> tmsPathSelector.toPegasusPathSpecSegment())
        .filter(pegasusPathSegment -> !PATHSPEC_IGNORE.equals(pegasusPathSegment))
        .collect(Collectors.toList());
  }
}
