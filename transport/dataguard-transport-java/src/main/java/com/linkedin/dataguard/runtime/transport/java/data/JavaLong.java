package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdLong;
import java.util.Objects;


public class JavaLong implements PlatformData, StdLong {
  private long _value;

  public JavaLong(long value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public Long getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (long) value;
  }

  @Override
  public long get() {
    return _value;
  }
}
