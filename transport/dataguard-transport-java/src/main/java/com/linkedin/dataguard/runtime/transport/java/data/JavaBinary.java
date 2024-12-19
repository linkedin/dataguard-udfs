package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdBinary;
import java.nio.ByteBuffer;
import java.util.Objects;


public class JavaBinary implements PlatformData, StdBinary {
  private ByteBuffer _value;

  public JavaBinary(ByteBuffer value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public ByteBuffer getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (ByteBuffer) value;
  }

  @Override
  public ByteBuffer get() {
    return _value;
  }
}
