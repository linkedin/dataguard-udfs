
package com.linkedin.dataguard.runtime.transport.trino.data;

import com.linkedin.dataguard.runtime.transport.trino.NullableTrinoWrapper;
import com.linkedin.dataguard.runtime.transport.trino.TrinoUtil;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.trino.data.TrinoData;
import io.trino.spi.block.Block;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.block.PageBuilderStatus;
import io.trino.spi.type.ArrayType;
import io.trino.spi.type.Type;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.trino.spi.type.TypeUtils.*;

/**
 * Ported and modified from {@link com.linkedin.transport.trino.data.TrinoArray}. A TrinoArray implementation that supports
 * fields with null values by using writeNullableValue.
 * The only difference from TrinoArray is that the "add" method allows adding elements with null values.
 * This is needed to allow setting null values for elements within this complex type which Transport's
 * implementation does not support.
 */
public class NullableTrinoArray extends TrinoData implements StdArray {

  private final StdFactory _stdFactory;
  private final ArrayType _arrayType;
  private final Type _elementType;

  private Block _block;
  private BlockBuilder _mutable;

  public NullableTrinoArray(Block block, ArrayType arrayType, StdFactory stdFactory) {
    _block = block;
    _arrayType = arrayType;
    _elementType = arrayType.getElementType();
    _stdFactory = stdFactory;
  }

  public NullableTrinoArray(ArrayType arrayType, int expectedEntries, StdFactory stdFactory) {
    _block = null;
    _elementType = arrayType.getElementType();
    _mutable = _elementType.createBlockBuilder(new PageBuilderStatus().createBlockBuilderStatus(), expectedEntries);
    _stdFactory = stdFactory;
    _arrayType = arrayType;
  }

  @Override
  public int size() {
    return _mutable == null ? _block.getPositionCount() : _mutable.getPositionCount();
  }

  @Override
  public StdData get(int idx) {
    Block sourceBlock = _mutable == null ? _block : _mutable;
    int position = NullableTrinoWrapper.checkedIndexToBlockPosition(sourceBlock, idx);
    Object element = readNativeValue(_elementType, sourceBlock, position);
    return NullableTrinoWrapper.createStdData(element, _elementType, _stdFactory);
  }

  @Override
  public void add(StdData e) {
    if (_mutable == null) {
      _mutable = _elementType.createBlockBuilder(new PageBuilderStatus().createBlockBuilderStatus(), 1);
    }
    TrinoUtil.writeNullableValue(e, _mutable);
  }

  @Override
  public Object getUnderlyingData() {
    return _mutable == null ? _block : _mutable.build();
  }

  @Override
  public void setUnderlyingData(Object value) {
    _block = (Block) value;
  }

  @Override
  public Iterator<StdData> iterator() {
    return new Iterator<StdData>() {
      Block sourceBlock = _mutable == null ? _block : _mutable;
      int size = NullableTrinoArray.this.size();
      int position = 0;

      @Override
      public boolean hasNext() {
        return position != size;
      }

      @Override
      public StdData next() {
        if (!hasNext()) {
          throw new NoSuchElementException("No more elements in the iterator");
        }
        Object element = readNativeValue(_elementType, sourceBlock, position);
        position++;
        return NullableTrinoWrapper.createStdData(element, _elementType, _stdFactory);
      }
    };
  }

  @Override
  public void writeToBlock(BlockBuilder blockBuilder) {
    _arrayType.writeObject(blockBuilder, getUnderlyingData());
  }
}
