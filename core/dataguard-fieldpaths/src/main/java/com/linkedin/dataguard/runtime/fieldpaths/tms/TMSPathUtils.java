package com.linkedin.dataguard.runtime.fieldpaths.tms;

import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import java.util.List;
import java.util.stream.Collectors;


public class TMSPathUtils {

  /**
   * Returns the list of path selectors for the given TMS path with the first selector removed
   * Example: In REDACT_IF and EXTRACT_COLLECTION, the root columns and full TMS paths are passed as parameters
   *  Thus we need to remove the first selector in TMS path which selecting the column, which is already in the parameter
   */
  public static List<TMSPathSelector> getAlignedPathSelectors(String tmsPath, TMSParser parser) {
    List<TMSPathSelector> pathSelectors = null;
    try {
      pathSelectors = parser.parsePath(tmsPath).getTMSPathSelectors();
    } catch (TMSParsingException e) {
      throw new RuntimeException(e);
    }
    return pathSelectors.stream()
        .skip(1) // Skips the first selector
        .collect(Collectors.toList());
  }
}
