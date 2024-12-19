package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;


public abstract class Literal extends Expression {

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitLiteral(this, input);
  }

  abstract public Object getLiteralValue();

  abstract public StdData toStdData(StdFactory factory);
}
