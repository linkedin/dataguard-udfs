package com.linkedin.dataguard.runtime.trino.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.UnionTypeHandler;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.trino.types.TrinoStructType;
import java.util.List;
import java.util.stream.IntStream;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;


public class TrinoUnionTypeHandler extends UnionTypeHandler {

  private static final String UNION_TO_STRUCT_TAG = "tag";
  private static final String UNION_TO_STRUCT_FIELD = "field";

  public TrinoUnionTypeHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
  }

  @Override
  public StdData redact(StdData data) {
    TMSEnforcer tmsEnforcer = getTmsEnforcer();
    StdType currentType = getStdType();
    if (!(currentType instanceof TrinoStructType)
        || !isComplexUnion(((TrinoStructType) currentType).fieldNames())) {
      // nullable union
      return (isLastSelector(pathIndex, tmsEnforcer.getSelectors()))
          ? tmsEnforcer.getAction().getReplacementValue(data)
          : tmsEnforcer.redact(data, pathIndex + 1);
    }
    throw new RuntimeException("Complex union type redaction is not supported in Trino yet");
  }

  @Override
  public StdType getChildStdType() {
    StdType currentType = getStdType();
    if (!(currentType instanceof StdStructType) || !isComplexUnion(((StdStructType) currentType).fieldNames())) {
      // simple union
      return currentType;
    }
    throw new RuntimeException("Complex union type redaction is not supported in Trino yet");
  }

  public static boolean isComplexUnion(List<String> fieldNames) {
    // complex union is represented as struct<tag:int, field0:<type0>, field1:<type1>...> in Trino
    return fieldNames.size() >= 2
        && fieldNames.get(0).equals(UNION_TO_STRUCT_TAG)
        && IntStream.range(1, fieldNames.size())
            .allMatch(i -> fieldNames.get(i).equals(UNION_TO_STRUCT_FIELD + (i - 1)));
  }
}
