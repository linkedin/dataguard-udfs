package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;

import static java.util.Objects.*;


/**
 * Supports referencing a field out of a struct column
 * e.g. x.y where x is a struct and y is a field inside the struct
 */
public class MemberReferenceExpression extends FieldReferenceExpression {

  private final FieldReferenceExpression base;
  private final Identifier fieldName;

  public MemberReferenceExpression(FieldReferenceExpression base, Identifier fieldName) {
    this.base = requireNonNull(base);
    this.fieldName = requireNonNull(fieldName);
  }

  public FieldReferenceExpression getBase() {
    return base;
  }

  public Identifier getFieldName() {
    return fieldName;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitMemberReferenceExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MemberReferenceExpression that = (MemberReferenceExpression) o;
    return base.equals(that.base) && fieldName.equals(that.fieldName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, fieldName);
  }
}
