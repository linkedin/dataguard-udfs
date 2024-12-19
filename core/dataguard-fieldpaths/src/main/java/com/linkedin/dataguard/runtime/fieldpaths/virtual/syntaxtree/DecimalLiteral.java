package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import java.util.Objects;

import static java.lang.Double.*;
import static java.util.Objects.*;


/**
 * Base class to represent decimals.
 */
public class DecimalLiteral extends Literal {

  private final double value;

  public DecimalLiteral(String value) {
    this.value = parseDouble(requireNonNull(value));
  }

  public double getValue() {
    return value;
  }

  @Override
  public Object getLiteralValue() {
    return value;
  }

  @Override
  public StdData toStdData(StdFactory factory) {
    return factory.createDouble(value);
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitDecimalLiteral(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DecimalLiteral that = (DecimalLiteral) o;
    return Double.compare(that.value, value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
