package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdString;
import java.util.Objects;


public class JavaString implements PlatformData, StdString {
  private String _value;

  public JavaString(String value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public String getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (String) value;
  }

  @Override
  public String get() {
    return _value;
  }
}
