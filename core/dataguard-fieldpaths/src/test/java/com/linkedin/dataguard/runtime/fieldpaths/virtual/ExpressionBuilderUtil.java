package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionLookupReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionPredicateReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionStreamReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Expression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FunctionCall;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Identifier;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.IntegerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MemberReferenceExpression;


public class ExpressionBuilderUtil {

  private ExpressionBuilderUtil() {}

  public static MemberReferenceExpression structMember(FieldReferenceExpression base, String identifier) {
    return new MemberReferenceExpression(base, new Identifier(identifier));
  }

  public static CollectionStreamReferenceExpression collectionStream(FieldReferenceExpression base) {
    return new CollectionStreamReferenceExpression(base);
  }

  public static CollectionLookupReferenceExpression collectionLookup(FieldReferenceExpression base, int index) {
    return new CollectionLookupReferenceExpression(base, new IntegerLiteral(String.valueOf(index)));
  }

  public static CollectionLookupReferenceExpression collectionLookup(FieldReferenceExpression base, FunctionCall functionCall) {
    return new CollectionLookupReferenceExpression(base, functionCall);
  }

  public static CollectionPredicateReferenceExpression applyFilter(FieldReferenceExpression base, Expression predicate) {
    return new CollectionPredicateReferenceExpression(base, predicate);
  }
}
