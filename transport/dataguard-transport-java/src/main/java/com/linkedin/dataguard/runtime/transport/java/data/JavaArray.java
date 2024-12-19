package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.dataguard.runtime.transport.java.JavaUtil;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class JavaArray implements PlatformData, StdArray {
  private final StdFactory _stdFactory;
  private List<Object> _array;

  public JavaArray(StdFactory stdFactory) {
    this(new ArrayList<>(), stdFactory);
  }

  public JavaArray(List<Object> array, StdFactory stdFactory) {
    Objects.requireNonNull(array, "array must not be null");
    Objects.requireNonNull(stdFactory, "stdFactory must not be null");
    _stdFactory = stdFactory;
    _array = array;
  }

  @Override
  public int size() {
    return _array.size();
  }

  @Override
  public StdData get(int idx) {
    Object element = _array.get(idx);
    return JavaUtil.createStdData(element, _stdFactory);
  }

  @Override
  public void add(StdData e) {
    // TODO: add addNull method
    if (e != null) {
      _array.add(((PlatformData) e).getUnderlyingData());
    } else {
      _array.add(null);
    }
  }

  @Override
  public List<Object> getUnderlyingData() {
    return _array;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value must not be null");
    _array = (List<Object>) value;
  }

  @Override
  public Iterator<StdData> iterator() {
    return new Iterator<StdData>() {
      int size = _array.size();
      int position = 0;

      @Override
      public boolean hasNext() {
        return position != size;
      }

      @Override
      public StdData next() {
        return get(position++);
      }
    };
  }
}
