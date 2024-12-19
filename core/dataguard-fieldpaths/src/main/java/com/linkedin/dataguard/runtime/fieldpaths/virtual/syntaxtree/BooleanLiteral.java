package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import java.util.Objects;

import static java.lang.Boolean.*;
import static java.util.Objects.*;


/**
 * Represents boolean values in the grammar.
 */
public class BooleanLiteral extends Literal {

  private final boolean value;

  public BooleanLiteral(String value) {
    this.value = parseBoolean(requireNonNull(value));
  }

  public boolean getValue() {
    return value;
  }

  @Override
  public Object getLiteralValue() {
    return value;
  }

  @Override
  public StdData toStdData(StdFactory factory) {
    return factory.createBoolean(value);
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitBooleanLiteral(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BooleanLiteral that = (BooleanLiteral) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
