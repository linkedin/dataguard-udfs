package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import java.util.Objects;

import static java.lang.Integer.*;
import static java.util.Objects.*;


public class IntegerLiteral extends Literal {
  private final int value;

  public IntegerLiteral(String value) {
    this.value = parseInt(requireNonNull(value));
  }

  public int getValue() {
    return value;
  }

  @Override
  public Object getLiteralValue() {
    return value;
  }

  @Override
  public StdData toStdData(StdFactory factory) {
    return factory.createInteger(value);
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitIntegerLiteral(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegerLiteral that = (IntegerLiteral) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
