// Generated from com/linkedin/dataguard/runtime/fieldpaths/virtual/VirtualFieldPath.g4 by ANTLR 4.9.3

    package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link VirtualFieldPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface VirtualFieldPathVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#mountedPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountedPath(VirtualFieldPathParser.MountedPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#rowSelectorAwareFieldPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowSelectorAwareFieldPath(VirtualFieldPathParser.RowSelectorAwareFieldPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#virtualFieldPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtualFieldPath(VirtualFieldPathParser.VirtualFieldPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#mountPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMountPoint(VirtualFieldPathParser.MountPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#rowSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowSelector(VirtualFieldPathParser.RowSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fieldRefExprDefault}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldRefExprDefault(VirtualFieldPathParser.FieldRefExprDefaultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberRef(VirtualFieldPathParser.MemberRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lookupRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLookupRef(VirtualFieldPathParser.LookupRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code streamRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStreamRef(VirtualFieldPathParser.StreamRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code filterRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterRef(VirtualFieldPathParser.FilterRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#collectionPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionPredicate(VirtualFieldPathParser.CollectionPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicateTrivial}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateTrivial(VirtualFieldPathParser.PredicateTrivialContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrPredicate(VirtualFieldPathParser.OrPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicateBase}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateBase(VirtualFieldPathParser.PredicateBaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicateParen}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateParen(VirtualFieldPathParser.PredicateParenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInPredicate(VirtualFieldPathParser.InPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotPredicate(VirtualFieldPathParser.NotPredicateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndPredicate(VirtualFieldPathParser.AndPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#predicateWithParenthesis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateWithParenthesis(VirtualFieldPathParser.PredicateWithParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedExpr(VirtualFieldPathParser.SignedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code refExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefExpr(VirtualFieldPathParser.RefExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesizedExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpr(VirtualFieldPathParser.ParenthesizedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(VirtualFieldPathParser.LiteralExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpr(VirtualFieldPathParser.FunctionCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(VirtualFieldPathParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplicativeExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(VirtualFieldPathParser.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(VirtualFieldPathParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(VirtualFieldPathParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link VirtualFieldPathParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalLiteral(VirtualFieldPathParser.DecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link VirtualFieldPathParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(VirtualFieldPathParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSign(VirtualFieldPathParser.SignContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(VirtualFieldPathParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#booleanLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(VirtualFieldPathParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code contextVariable}
	 * labeled alternative in {@link VirtualFieldPathParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextVariable(VirtualFieldPathParser.ContextVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code predicateCurrentVariable}
	 * labeled alternative in {@link VirtualFieldPathParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateCurrentVariable(VirtualFieldPathParser.PredicateCurrentVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(VirtualFieldPathParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeOperator(VirtualFieldPathParser.MultiplicativeOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#additiveOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveOperator(VirtualFieldPathParser.AdditiveOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link VirtualFieldPathParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(VirtualFieldPathParser.IdentifierContext ctx);
}