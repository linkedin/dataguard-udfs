
package com.linkedin.dataguard.runtime.transport.trino;

import com.linkedin.dataguard.runtime.transport.trino.data.NullableTrinoMap;
import com.linkedin.dataguard.runtime.transport.trino.data.NullableTrinoArray;
import com.linkedin.dataguard.runtime.transport.trino.data.NullableTrinoStruct;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.trino.data.TrinoBinary;
import com.linkedin.transport.trino.data.TrinoBoolean;
import com.linkedin.transport.trino.data.TrinoDouble;
import com.linkedin.transport.trino.data.TrinoFloat;
import com.linkedin.transport.trino.data.TrinoInteger;
import com.linkedin.transport.trino.data.TrinoLong;
import com.linkedin.transport.trino.data.TrinoString;
import com.linkedin.transport.trino.types.TrinoArrayType;
import com.linkedin.transport.trino.types.TrinoBinaryType;
import com.linkedin.transport.trino.types.TrinoBooleanType;
import com.linkedin.transport.trino.types.TrinoDoubleType;
import com.linkedin.transport.trino.types.TrinoFloatType;
import com.linkedin.transport.trino.types.TrinoIntegerType;
import com.linkedin.transport.trino.types.TrinoLongType;
import com.linkedin.transport.trino.types.TrinoMapType;
import com.linkedin.transport.trino.types.TrinoStringType;
import com.linkedin.transport.trino.types.TrinoStructType;
import io.airlift.slice.Slice;
import io.trino.spi.TrinoException;
import io.trino.spi.block.Block;
import io.trino.spi.type.ArrayType;
import io.trino.spi.type.BigintType;
import io.trino.spi.type.BooleanType;
import io.trino.spi.type.DoubleType;
import io.trino.spi.type.IntegerType;
import io.trino.spi.type.MapType;
import io.trino.spi.type.RealType;
import io.trino.spi.type.RowType;
import io.trino.spi.type.Type;
import io.trino.spi.type.VarbinaryType;
import io.trino.spi.type.VarcharType;

import static io.trino.spi.StandardErrorCode.*;
import static java.lang.Float.*;
import static java.lang.Math.*;
import static java.lang.String.*;

/**
 * Ported and modified from {@link com.linkedin.transport.trino.TrinoWrapper}. A TrinoWrapper implementation that supports
 * null values for Array, Map, and Struct fields. The only difference from TrinoWrapper is that it uses {@link NullableTrinoStruct},
 * {@link NullableTrinoArray} and {@link NullableTrinoMap}, instead of {@link com.linkedin.transport.trino.data.TrinoStruct},
 * {@link com.linkedin.transport.trino.data.TrinoArray} and {@link com.linkedin.transport.trino.data.TrinoMap} respectively,
 * for representing complex data types.
 *
 * This is needed to allow setting null values for elements within this complex type which Transport's implementation
 * does not support.
 */
public final class NullableTrinoWrapper {

  private NullableTrinoWrapper() {
  }

  public static StdData createStdData(Object trinoData, Type trinoType, StdFactory stdFactory) {
    if (trinoData == null) {
      return null;
    }
    if (trinoType instanceof IntegerType) {
      // Trino represents SQL Integers (i.e., corresponding to IntegerType above) as long or Long
      // Therefore, to pass it to the TrinoInteger class, we first cast it to Long, then extract
      // the int value.
      return new TrinoInteger(((Long) trinoData).intValue());
    } else if (trinoType instanceof BigintType) {
      return new TrinoLong((long) trinoData);
    } else if (trinoType instanceof BooleanType) {
      return new TrinoBoolean((boolean) trinoData);
    } else if (trinoType instanceof VarcharType) {
      return new TrinoString((Slice) trinoData);
    } else if (trinoType instanceof RealType) {
      // Trino represents SQL Reals (i.e., corresponding to RealType above) as long or Long
      // Therefore, to pass it to the TrinoFloat class, we first cast it to Long, extract
      // the int value and convert it the int bits to float.
      long value = (long) trinoData;
      int floatValue;
      try {
        floatValue = toIntExact(value);
      } catch (ArithmeticException e) {
        throw new TrinoException(GENERIC_INTERNAL_ERROR,
            format("Value (%sb) is not a valid single-precision float", Long.toBinaryString(value)));
      }
      return new TrinoFloat(intBitsToFloat(floatValue));
    } else if (trinoType instanceof DoubleType) {
      return new TrinoDouble((double) trinoData);
    } else if (trinoType instanceof VarbinaryType) {
      return new TrinoBinary((Slice) trinoData);
    } else if (trinoType instanceof ArrayType) {
      return new NullableTrinoArray((Block) trinoData, (ArrayType) trinoType, stdFactory);
    } else if (trinoType instanceof MapType) {
      return new NullableTrinoMap((Block) trinoData, trinoType, stdFactory);
    } else if (trinoType instanceof RowType) {
      return new NullableTrinoStruct((Block) trinoData, trinoType, stdFactory);
    }
    assert false : "Unrecognized Trino Type: " + trinoType.getClass();
    return null;
  }

  public static StdType createStdType(Object trinoType) {
    if (trinoType instanceof IntegerType) {
      return new TrinoIntegerType((IntegerType) trinoType);
    } else if (trinoType instanceof BigintType) {
      return new TrinoLongType((BigintType) trinoType);
    } else if (trinoType instanceof BooleanType) {
      return new TrinoBooleanType((BooleanType) trinoType);
    } else if (trinoType instanceof VarcharType) {
      return new TrinoStringType((VarcharType) trinoType);
    } else if (trinoType instanceof RealType) {
      return new TrinoFloatType((RealType) trinoType);
    } else if (trinoType instanceof DoubleType) {
      return new TrinoDoubleType((DoubleType) trinoType);
    } else if (trinoType instanceof VarbinaryType) {
      return new TrinoBinaryType((VarbinaryType) trinoType);
    } else if (trinoType instanceof ArrayType) {
      return new TrinoArrayType((ArrayType) trinoType);
    } else if (trinoType instanceof MapType) {
      return new TrinoMapType((MapType) trinoType);
    } else if (trinoType instanceof RowType) {
      return new TrinoStructType(((RowType) trinoType));
    }
    assert false : "Unrecognized Trino Type: " + trinoType.getClass();
    return null;
  }

  /**
   * @return index if the index is in range, -1 otherwise.
   */
  public static int checkedIndexToBlockPosition(Block block, long index) {
    int blockLength = block.getPositionCount();
    if (index >= 0 && index < blockLength) {
      return toIntExact(index);
    }
    return -1; // -1 indicates that the element is out of range and the calling function should return null
  }
}
