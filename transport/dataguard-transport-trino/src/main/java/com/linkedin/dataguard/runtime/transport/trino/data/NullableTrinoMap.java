
package com.linkedin.dataguard.runtime.transport.trino.data;

import com.google.common.base.Throwables;
import com.linkedin.dataguard.runtime.transport.trino.NullableTrinoWrapper;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.trino.data.TrinoData;
import io.trino.spi.TrinoException;
import io.trino.spi.block.Block;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.block.PageBuilderStatus;
import io.trino.spi.type.MapType;
import io.trino.spi.type.Type;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.linkedin.dataguard.runtime.transport.trino.TrinoUtil.*;
import static io.trino.spi.StandardErrorCode.*;
import static io.trino.spi.type.TypeUtils.*;

/**
 * Ported and modified from {@link com.linkedin.transport.trino.data.TrinoMap}. A TrinoMap implementation that supports
 * fields with null values. One difference from TrinoMap is that the "put" method allows putting a value that is null
 * by using writeNullableValue.
 * This is needed to allow setting null values for elements within this complex type which Transport's implementation
 * does not support. Another difference is that current implementation simplifies the "seekKey" method by using Object#equals
 * to compare keys instead of using a method handle to invoke the key's equals method (which is not supported without
 * introducting functionDependencies).
 */
public class NullableTrinoMap extends TrinoData implements StdMap {

  final Type _keyType;
  final Type _valueType;
  final Type _mapType;
  final StdFactory _stdFactory;
  Block _block;

  public NullableTrinoMap(Type mapType, StdFactory stdFactory) {
    BlockBuilder mutable = mapType.createBlockBuilder(new PageBuilderStatus().createBlockBuilderStatus(), 1);
    mutable.beginBlockEntry();
    mutable.closeEntry();
    _block = ((MapType) mapType).getObject(mutable.build(), 0);

    _keyType = ((MapType) mapType).getKeyType();
    _valueType = ((MapType) mapType).getValueType();
    _mapType = mapType;

    _stdFactory = stdFactory;
  }

  public NullableTrinoMap(Block block, Type mapType, StdFactory stdFactory) {
    this(mapType, stdFactory);
    _block = block;
  }

  @Override
  public int size() {
    return _block.getPositionCount() / 2;
  }

  @Override
  public StdData get(StdData key) {
    Object trinoKey = ((PlatformData) key).getUnderlyingData();
    int i = seekKey(trinoKey);
    if (i != -1) {
      Object value = readNativeValue(_valueType, _block, i);
      StdData stdValue = NullableTrinoWrapper.createStdData(value, _valueType, _stdFactory);
      return stdValue;
    } else {
      return null;
    }
  }

  // TODO: Do not copy the _mutable BlockBuilder on every update. As long as updates are append-only or for fixed-size
  // types, we can skip copying.
  @Override
  public void put(StdData key, StdData value) {
    BlockBuilder mutable = _mapType.createBlockBuilder(new PageBuilderStatus().createBlockBuilderStatus(), 1);
    BlockBuilder entryBuilder = mutable.beginBlockEntry();
    if (key instanceof StdMap || key instanceof StdStruct || key instanceof StdArray) {
      throw new UnsupportedOperationException("Complex types are not supported as keys in maps");
    }
    Object trinoKey = ((PlatformData) key).getUnderlyingData();
    int valuePosition = seekKey(trinoKey);
    for (int i = 0; i < _block.getPositionCount(); i += 2) {
      // Write the current key to the map
      _keyType.appendTo(_block, i, entryBuilder);
      // Find out if we need to change the corresponding value
      if (i == valuePosition - 1) {
        // Use the user-supplied value
        writeNullableValue(value, entryBuilder);
      } else {
        // Use the existing value in original _block
        _valueType.appendTo(_block, i + 1, entryBuilder);
      }
    }
    if (valuePosition == -1) {
      ((TrinoData) key).writeToBlock(entryBuilder);
      writeNullableValue(value, entryBuilder);
    }

    mutable.closeEntry();
    _block = ((MapType) _mapType).getObject(mutable.build(), 0);
  }

  public Set<StdData> keySet() {
    return new AbstractSet<StdData>() {
      @Override
      public Iterator<StdData> iterator() {
        return new Iterator<StdData>() {
          int i = -2;

          @Override
          public boolean hasNext() {
            return !(i + 2 == size() * 2);
          }

          @Override
          public StdData next() {
            if (!hasNext()) {
              throw new NoSuchElementException("No more elements in the iterator");
            }
            i += 2;
            return NullableTrinoWrapper.createStdData(readNativeValue(_keyType, _block, i), _keyType, _stdFactory);
          }
        };
      }

      @Override
      public int size() {
        return NullableTrinoMap.this.size();
      }
    };
  }

  @Override
  public Collection<StdData> values() {
    return new AbstractCollection<StdData>() {

      @Override
      public Iterator<StdData> iterator() {
        return new Iterator<StdData>() {
          int i = -2;

          @Override
          public boolean hasNext() {
            return !(i + 2 == size() * 2);
          }

          @Override
          public StdData next() {
            if (!hasNext()) {
              throw new NoSuchElementException("No more elements in the iterator");
            }
            i += 2;
            return NullableTrinoWrapper.createStdData(readNativeValue(_valueType, _block, i + 1), _valueType, _stdFactory);
          }
        };
      }

      @Override
      public int size() {
        return NullableTrinoMap.this.size();
      }
    };
  }

  @Override
  public boolean containsKey(StdData key) {
    return get(key) != null;
  }

  @Override
  public Object getUnderlyingData() {
    return _block;
  }

  @Override
  public void setUnderlyingData(Object value) {
    _block = (Block) value;
  }

  private int seekKey(Object key) {
    for (int i = 0; i < _block.getPositionCount(); i += 2) {
      try {
        if (readNativeValue(_keyType, _block, i).equals(key)) {
          return i + 1;
        }
      } catch (Throwable t) {
        Throwables.propagateIfInstanceOf(t, Error.class);
        Throwables.propagateIfInstanceOf(t, TrinoException.class);
        throw new TrinoException(GENERIC_INTERNAL_ERROR, t);
      }
    }
    return -1;
  }

  @Override
  public void writeToBlock(BlockBuilder blockBuilder) {
    _mapType.writeObject(blockBuilder, _block);
  }
}
