package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import java.util.Objects;

import static java.util.Objects.*;


public class StringLiteral extends Literal {
  private final String value;

  public StringLiteral(String value) {
    this.value = requireNonNull(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public Object getLiteralValue() {
    return value;
  }

  @Override
  public StdData toStdData(StdFactory factory) {
    return factory.createString(value);
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitStringLiteral(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringLiteral that = (StringLiteral) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
