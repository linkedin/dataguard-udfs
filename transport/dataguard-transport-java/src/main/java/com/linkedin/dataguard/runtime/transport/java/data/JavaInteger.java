package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdInteger;
import java.util.Objects;


public class JavaInteger implements PlatformData, StdInteger {
  private int _value;

  public JavaInteger(int value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public Integer getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (int) value;
  }

  @Override
  public int get() {
    return _value;
  }
}
