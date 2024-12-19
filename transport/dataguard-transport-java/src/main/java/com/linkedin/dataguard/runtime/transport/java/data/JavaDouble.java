package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdDouble;
import java.util.Objects;


public class JavaDouble implements PlatformData, StdDouble {
  private double _value;

  public JavaDouble(double value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public Double getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (double) value;
  }

  public double get() {
    return _value;
  }
}
