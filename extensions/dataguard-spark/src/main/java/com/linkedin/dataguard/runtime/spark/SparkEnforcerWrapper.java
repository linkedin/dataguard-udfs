package com.linkedin.dataguard.runtime.spark;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Enforcer;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EraseAction;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import org.apache.spark.sql.types.DataType;

import static com.linkedin.dataguard.runtime.transport.spark.NullableSparkWrapper.*;


public final class SparkEnforcerWrapper {

  private SparkEnforcerWrapper() {
  }

  /**
   *  The transform API to be invoked from the spark REDACT_FIELD_IF udf
   *  that applies redaction transformation on the spark data object
   * @param columnObjectType The type for the data to be enforced
   * @param columnObject The data to be enforced as a spark object
   * @param replacementData The data to be used as replacement value
   * @param replacementDataType The data type for the replacement data
   * @param enforcer The enforcer to be used for applying secondary schema enforcement
   * @return The transformed data object
   */
  public static Object transform(
      DataType columnObjectType,
      Object columnObject,
      Object replacementData,
      DataType replacementDataType,
      Enforcer enforcer) {
    StdData objData = createStdData(columnObject, columnObjectType);
    StdData replacementStdData = createStdData(replacementData, replacementDataType);
    StdData result = enforcer.applyAction(objData, new EraseAction(replacementStdData), objData);
    return result == null ? null : ((PlatformData) result).getUnderlyingData();
  }
}
