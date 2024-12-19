package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.dataguard.runtime.transport.java.JavaUtil;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdStruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.ImmutableList.*;
import static com.google.common.collect.ImmutableMap.*;
import static java.lang.String.*;
import static java.util.Objects.*;


public class JavaStruct implements PlatformData, StdStruct {

  private final StdFactory factory;
  private StructInfo structInfo;

  public JavaStruct(
      StdFactory factory,
      List<String> fieldNames,
      List<Object> fields) {
    this(factory, new StructInfo(fieldNames, fields));
  }

  public JavaStruct(
      StdFactory factory,
      StructInfo info) {
    this.factory = requireNonNull(factory);
    this.structInfo = requireNonNull(info);
  }

  @Override
  public StdData getField(int index) {
    return JavaUtil.createStdData(structInfo.getField(index), factory);
  }

  @Override
  public StdData getField(String name) {
    return JavaUtil.createStdData(structInfo.getField(name), factory);
  }

  @Override
  public void setField(int index, StdData value) {
    Object data = (value == null) ? null : ((PlatformData) value).getUnderlyingData();
    structInfo.setField(index, data);
  }

  @Override
  public void setField(String name, StdData value) {
    Object data = (value == null) ? null : ((PlatformData) value).getUnderlyingData();
    structInfo.setField(structInfo.getFieldIndex(name), data);
  }

  @Override
  public List<StdData> fields() {
    return structInfo.getFields().stream()
        .map(field -> JavaUtil.createStdData(field, factory))
        .collect(toImmutableList());
  }

  @Override
  public Object getUnderlyingData() {
    return structInfo;
  }

  @Override
  public void setUnderlyingData(Object object) {
    StructInfo value = (StructInfo) object;
    this.structInfo = value;
  }

  public static StructInfo structInfo(List<String> names, Object... fields) {
    return new StructInfo(names, Arrays.asList(fields));
  }

  public static StructInfo structInfo(List<String> names, List<Object> fields) {
    return new StructInfo(names, fields);
  }

  public static class StructInfo {
    private final List<String> names;
    private final List<Object> fields;
    private final Map<String, Integer> nameToIndexMapping;

    public StructInfo(List<String> names, List<Object> fields) {
      // Copy so that they can be modifiable
      this.names = requireNonNull(new ArrayList<>(names));
      this.fields = requireNonNull(new ArrayList<>(fields));
      checkArgument(fields.size() == names.size());
      this.nameToIndexMapping = IntStream.range(0, names.size()).boxed()
          .collect(toImmutableMap(names::get, Function.identity()));
    }

    public List<Object> getFields() {
      return fields;
    }

    public List<String> getNames() {
      return names;
    }

    public int getSize() {
      return names.size();
    }

    public Object getField(int index) {
      return fields.get(index);
    }

    public void setField(int index, Object value) {
      fields.set(index, value);
    }

    public int getFieldIndex(String name) {
      Integer index = nameToIndexMapping.get(name);
      checkArgument(index != null, format("Field %s not found", name));
      return index;
    }

    public Object getField(String name) {
      return getField(getFieldIndex(name));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      StructInfo that = (StructInfo) o;
      return names.equals(that.names) && fields.equals(that.fields);
    }

    @Override
    public int hashCode() {
      return hash(names, fields);
    }
  }
}
