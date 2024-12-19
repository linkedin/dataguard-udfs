package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdFloat;
import java.util.Objects;


public class JavaFloat implements PlatformData, StdFloat {
  private float _value;

  public JavaFloat(float value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public Float getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (float) value;
  }

  public float get() {
    return _value;
  }
}
