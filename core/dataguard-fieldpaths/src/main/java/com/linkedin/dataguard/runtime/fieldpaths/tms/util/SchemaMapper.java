package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class SchemaMapper {

  /**
   * Maps TMS field paths of a source schema to field paths of a destination schema. Two paths are matched
   * if they have the same canonical form (inferred from the {@link TMSPathSchemaTree} representing each schema).
   *
   * @param srcSchema List of field paths to represent the source schema
   * @param destSchema List of field paths to represent the destination schema
   * @return A map that map each field path in source schema to a field path in the destination schema. If there is
   * no matched field path in the destination schema, the mapping value will be null.
   */
  public static Map<String, String> mapSchema(List<String> srcSchema, List<String> destSchema) {
    TMSPathSchemaTree srcTree = new TMSPathSchemaTree(srcSchema);
    TMSPathSchemaTree destTree = new TMSPathSchemaTree(destSchema);
    Map<String, String> results =  new HashMap<>();
    for (String originalSrcPath : srcSchema) {
      String canonicalSrcPath = srcTree.getCanonicalPath(originalSrcPath);
      String originalDestPath = destTree.getOriginalPath(canonicalSrcPath);
      results.put(originalSrcPath, originalDestPath);
    }
    return results;
  }

  private SchemaMapper() {
  }
}
