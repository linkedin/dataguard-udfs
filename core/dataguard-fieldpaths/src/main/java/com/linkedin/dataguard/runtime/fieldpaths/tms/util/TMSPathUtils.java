package com.linkedin.dataguard.runtime.fieldpaths.tms.util;

import com.linkedin.dataguard.runtime.fieldpaths.tms.ParsedTMSPath;
import com.linkedin.dataguard.runtime.fieldpaths.tms.ParserFactory;
import com.linkedin.dataguard.runtime.fieldpaths.tms.TMSParser;
import com.linkedin.dataguard.runtime.fieldpaths.tms.TMSParsingException;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.ArrayElementSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapKeySelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapValueSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.UnionTypeSelector;
import java.util.ArrayList;
import java.util.List;


public class TMSPathUtils {
  private static final TMSParser TMS_PARSER = ParserFactory.getParser();
  private static final String ELEMENT_TYPE_PLACEHOLDER = "elemType";
  private static final String KEY_TYPE_PLACEHOLDER = "keyType";
  private static final String VALUE_TYPE_PLACEHOLDER = "valueType";

  public static ParsedTMSPath parseTMSPath(String tmsPath) {
    ParsedTMSPath parsedTMSPath;
    try {
      parsedTMSPath = TMS_PARSER.parsePath(tmsPath);
    } catch (TMSParsingException e) {
      throw new RuntimeException("Cannot parse the TMS path " + tmsPath, e);
    }
    return parsedTMSPath;
  }

  public static String toCanonicalTMSPath(List<TMSPathSelector> components) {
    List<String> canonicalTmsPathParts = new ArrayList<>();
    for (TMSPathSelector tmsPathSelector : components) {
      if (tmsPathSelector instanceof ArrayElementSelector) {
        canonicalTmsPathParts.add(String.format("[type=%s]", ELEMENT_TYPE_PLACEHOLDER));
      } else if (tmsPathSelector instanceof MapKeySelector) {
        canonicalTmsPathParts.add(String.format("[key=%s]", KEY_TYPE_PLACEHOLDER));
      } else if (tmsPathSelector instanceof MapValueSelector) {
        canonicalTmsPathParts.add(String.format("[value=%s]", VALUE_TYPE_PLACEHOLDER));
      } else if (tmsPathSelector instanceof StructFieldSelector) {
        canonicalTmsPathParts.add(((StructFieldSelector) tmsPathSelector).getFieldName());
      } else if (tmsPathSelector instanceof UnionTypeSelector) {
        String lastPath = canonicalTmsPathParts.get(canonicalTmsPathParts.size() - 1);
        lastPath = String.format("%s[type=%s]", lastPath, ((UnionTypeSelector) tmsPathSelector).getUnionTypeSelected());
        canonicalTmsPathParts.set(canonicalTmsPathParts.size() - 1, lastPath);
      }
    }
    return String.join(".", canonicalTmsPathParts);
  }

  public static boolean isSameSelector(TMSPathSelector selector1, TMSPathSelector selector2) {
    if (selector1 instanceof StructFieldSelector && selector2 instanceof StructFieldSelector) {
      String fieldName1 = ((StructFieldSelector) selector1).getFieldName();
      String fieldName2 = ((StructFieldSelector) selector2).getFieldName();
      return fieldName1.equalsIgnoreCase(fieldName2);
    }

    if (selector1 instanceof UnionTypeSelector && selector2 instanceof UnionTypeSelector) {
      String type1 = ((UnionTypeSelector) selector1).getUnionTypeSelected();
      String type2 = ((UnionTypeSelector) selector2).getUnionTypeSelected();
      return type1.equalsIgnoreCase(type2);
    }

    return selector1.getClass().equals(selector2.getClass());
  }

  private TMSPathUtils() {
  }
}
