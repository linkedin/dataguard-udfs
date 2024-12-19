package com.linkedin.dataguard.runtime.trino;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.dataguard.runtime.trino.handler.factory.TrinoHandlerFactory;
import com.linkedin.dataguard.runtime.transport.trino.types.TrinoTypeDataProvider;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import io.airlift.slice.Slice;
import io.trino.spi.TrinoException;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlNullable;
import io.trino.spi.function.SqlType;
import io.trino.spi.function.TypeParameter;
import io.trino.spi.type.StandardTypes;
import io.trino.spi.type.Type;

import static com.linkedin.dataguard.runtime.transport.trino.NullableTrinoWrapper.*;
import static io.trino.spi.StandardErrorCode.*;


/**
 * Trino implementation for REDACT_FIELD_IF UDF
 */
@ScalarFunction(value = "redact_field_if")
public final class RedactFieldIf {

  private RedactFieldIf() {
  }

  private static final TrinoTypeDataProvider TYPE_DATA_PROVIDER = new TrinoTypeDataProvider();
  private static final EraseAction NULL_ERASE_ACTION = new EraseAction(null);
  // refer to UDF_BATCHING_DELIM under datapolicy-enforcement-common/policy-definition-api as SOT
  private static final String UDF_BATCHING_DELIM = "###";

  @TypeParameter("T")
  @TypeParameter("S")
  @SqlType("T")
  @SqlNullable
  public static Object redactFieldIf(
      @TypeParameter("T") Type type,
      @TypeParameter("S") Type replacementValueType,
      // Marking nullable to inspect and throw in case of null value
      @SqlNullable @SqlType(StandardTypes.BOOLEAN) Boolean condition,
      // can be null
      @SqlNullable @SqlType("T") Object columnToBeTransformed,
      // Marking nullable to inspect and throw in case of null value
      @SqlNullable @SqlType(StandardTypes.VARCHAR) Slice tmsPaths,
      // can be null
      @SqlNullable @SqlType("S") Object replacementValue) {

    if (tmsPaths == null || condition == null) {
      throw new TrinoException(INVALID_FUNCTION_ARGUMENT, "TMS path and condition cannot be null");
    }
    if (!condition || columnToBeTransformed == null) {
      return columnToBeTransformed;
    }
    String tmsPathsStr = tmsPaths.toStringUtf8();
    StdFactory trinoFactory = TYPE_DATA_PROVIDER.getStdFactory();
    StdData objData = createStdData(columnToBeTransformed, type, trinoFactory);
    String[] tmsPathArray = tmsPathsStr.split(UDF_BATCHING_DELIM);
    StdData result = objData;
    Action eraseActionForReplacementData = createStdDataForReplacementValue(replacementValue, replacementValueType, trinoFactory);
    for (String tmsPathStr: tmsPathArray) {
      TMSEnforcer tmsEnforcer = createTMSEnforcer(tmsPathStr, type, eraseActionForReplacementData);
      result = tmsEnforcer.redact(result, 0);
    }
    return result == null ? null : ((PlatformData) result).getUnderlyingData();
  }

  public static Action createStdDataForReplacementValue(Object replacementValue, Type type, StdFactory trinoFactory) {
    if (replacementValue == null) {
      return NULL_ERASE_ACTION;
    }
    return new EraseAction(createStdData(replacementValue, type, trinoFactory));
  }

  private static TMSEnforcer createTMSEnforcer(String tmsPath, Type rootType, Action action) {
    return new TMSEnforcer(
        tmsPath,
        createStdType(rootType),
        TYPE_DATA_PROVIDER,
        action,
        TrinoHandlerFactory.getInstance());
  }
}
