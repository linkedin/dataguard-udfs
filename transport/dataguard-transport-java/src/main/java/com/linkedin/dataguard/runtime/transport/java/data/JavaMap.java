package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.dataguard.runtime.transport.java.JavaUtil;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class JavaMap implements PlatformData, StdMap {
  private final StdFactory _stdFactory;
  private Map<Integer, Object> _indexToKey;
  private Map<Object, Object> _map;

  public JavaMap(StdFactory stdFactory) {
    this(new HashMap<>(), stdFactory);
  }

  public JavaMap(StdFactory stdFactory, List<String> fieldNames) {
    this(new HashMap<>(), stdFactory, initIndexToKey(fieldNames));
  }

  public JavaMap(Map<Object, Object> map, StdFactory stdFactory) {
    this(map, stdFactory, initIndexToKey(map));
  }

  public JavaMap(Map<Object, Object> map, StdFactory stdFactory, Map<Integer, Object> indexToKey) {
    Objects.requireNonNull(map, "map cannot be null");
    Objects.requireNonNull(stdFactory, "stdFactory cannot be null");
    Objects.requireNonNull(indexToKey, "indexToKey cannot be null");
    _map = new HashMap<>(map);
    _stdFactory = stdFactory;
    _indexToKey = initIndexToKey(map);
  }

  @Override
  public Map<Object, Object> getUnderlyingData() {
    return _map;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _map = (Map<Object, Object>) value;
  }

  @Override
  public int size() {
    return _map.size();
  }

  @Override
  public StdData get(StdData key) {
    Objects.requireNonNull(key, "key cannot be null");

    PlatformData keyData = (PlatformData) key;
    if (!(keyData.getUnderlyingData() instanceof String)) {
      throw new IllegalArgumentException("Map key must be of type String, found " + keyData.getUnderlyingData().getClass());
    }

    return JavaUtil.createStdData(
        _map.get(keyData.getUnderlyingData()),
        _stdFactory);
  }

  @Override
  public void put(StdData key, StdData value) {
    Objects.requireNonNull(key, "key cannot be null");

    PlatformData keyData = (PlatformData) key;
    Object keyStr = keyData.getUnderlyingData();
    Object val = value == null ? null : ((PlatformData) value).getUnderlyingData();

    _map.put(keyStr, val);
    _indexToKey.put(_indexToKey.size(), keyStr);
  }

  @Override
  public Set<StdData> keySet() {
    return _map.keySet()
        .stream()
        .map(key -> JavaUtil.createStdData(key, _stdFactory))
        .collect(Collectors.toSet());
  }

  @Override
  public Collection<StdData> values() {
    return _map.values().stream()
        .map(value -> JavaUtil.createStdData(value, _stdFactory))
        .collect(Collectors.toList());
  }

  @Override
  public boolean containsKey(StdData key) {
    Objects.requireNonNull(key, "key cannot be null");

    PlatformData keyData = (PlatformData) key;
    if (!(keyData.getUnderlyingData() instanceof String)) {
      throw new IllegalArgumentException("Map key must be of type String, found " + keyData.getUnderlyingData().getClass());
    }
    return _map.containsKey(keyData.getUnderlyingData());
  }

  private static Map<Integer, Object> initIndexToKey(final List<String> fieldNames) {
    Map<Integer, Object> indexToKey = new LinkedHashMap<>();
    for (int i = 0; i < fieldNames.size(); i++) {
      indexToKey.put(i, fieldNames.get(i));
    }
    return indexToKey;
  }

  private static Map<Integer, Object> initIndexToKey(final Map<Object, Object> map) {
    final Map<Integer, Object> indexToKey = new LinkedHashMap<>();

    if (map != null) {
      int index = 0;
      for (Object key : map.keySet()) {
        indexToKey.put(index, key);
      }
    }
    return indexToKey;
  }
}
