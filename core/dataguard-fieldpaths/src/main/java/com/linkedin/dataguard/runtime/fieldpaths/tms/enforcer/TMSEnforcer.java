package com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer;

import com.linkedin.dataguard.runtime.fieldpaths.tms.TMSPathUtils;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.RedactHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory.HandlerFactory;
import com.linkedin.dataguard.runtime.fieldpaths.tms.ParserFactory;
import com.linkedin.dataguard.runtime.fieldpaths.tms.TMSParser;
import com.linkedin.dataguard.runtime.fieldpaths.tms.TMSParsingException;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.ArrayElementSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapKeySelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.MapValueSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.UnionTypeSelector;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;


/**
 * TMSEnforcer is responsible for redacting the field specified by the TMS path on a data object.
 * For each selector in the parsed tmsPath, one handler corresponding to that selector is created
 * to perform redaction at that level. This Enforcer class is implemented based on std type and should be shared
 * across all formats.
 */
public class TMSEnforcer {

  private static final Logger LOG = LoggerFactory.getLogger(TMSEnforcer.class);
  private final List<TMSPathSelector> selectors;
  private final StdType rootType;
  private final FormatSpecificTypeDataProvider typeDataProvider;
  private final Action action;
  private final List<RedactHandler> handlers;
  private final TMSParser parser;
  private final HandlerFactory handlerFactory;

  /**
   * @param tmsPath TMS path to be used for enforcement
   * @param rootType Root type of the data object
   * @param typeDataProvider Type data provider for the given format
   * @param action Action to be performed on the data object when enforcement is triggered
   * @param handlerFactory Factory to create handlers for each selector
   * Given the list of selectors and the root type, this constructor creates a list of handlers
   * and resolves the corresponding child type for each handler.
   */
  public TMSEnforcer(
      String tmsPath,
      StdType rootType,
      FormatSpecificTypeDataProvider typeDataProvider,
      Action action, HandlerFactory handlerFactory) {
    this.handlerFactory =  checkNotNull(handlerFactory);
    this.parser = ParserFactory.getParser();
    this.selectors = TMSPathUtils.getAlignedPathSelectors(tmsPath, this.parser);
    this.rootType = rootType;
    this.typeDataProvider = typeDataProvider;
    this.action = action;
    this.handlers = resolveHandlers(selectors, rootType);
  }

  private List<RedactHandler> resolveHandlers(List<TMSPathSelector> selectors, StdType rootType) {
    List<RedactHandler> handlers = new ArrayList<>();
    StdType currentType = rootType;
    for (int i = 0; i < selectors.size(); i++) {
      TMSPathSelector selector = selectors.get(i);
      RedactHandler handler = null;
      if (selector instanceof ArrayElementSelector) {
        handler = handlerFactory.createArrayElementHandler(this, currentType, i);
      } else if (selector instanceof MapKeySelector) {
        handler = handlerFactory.createMapKeyHandler(this, currentType, i);
      } else if (selector instanceof MapValueSelector) {
        handler = handlerFactory.createMapValueHandler(this, currentType, i);
      } else if (selector instanceof StructFieldSelector) {
        handler = handlerFactory.createStructFieldHandler(this, currentType, i);
      } else if (selector instanceof UnionTypeSelector) {
        handler = handlerFactory.createUnionTypeHandler(this, currentType, i);
      } else {
        throw new RuntimeException(String.format("Unsupported selector type: %s", selector.getClass()));
      }
      handlers.add(handler);
      currentType = handler.getChildStdType();
      if (currentType == null) {
        break;
      }
    }
    return handlers;
  }

  public StdData redact(StdData data, int pathIndex) {
    if (selectors.isEmpty()) {
      return action.getReplacementValue(data);
    }
    if (data == null) {
      if (pathIndex == selectors.size()) {
        return action.getReplacementValue(null);
      } else {
        return null;
      }
    }
    // early return if the data object itself is null or root column is redacted
    RedactHandler handler = handlers.get(pathIndex);
    return handler.redact(data);
  }

  // check whether the selector is the last in the list
  public static boolean isLastSelector(int pathIndex, List<TMSPathSelector> tmsPathSelectors) {
    return pathIndex == tmsPathSelectors.size() - 1;
  }

  public List<TMSPathSelector> getSelectors() {
    return selectors;
  }

  public StdType getRootType() {
    return rootType;
  }

  public List<RedactHandler> getHandlers() {
    return handlers;
  }

  public FormatSpecificTypeDataProvider getTypeDataProvider() {
    return typeDataProvider;
  }

  public Action getAction() {
    return action;
  }
}
