// Generated from com/linkedin/dataguard/runtime/fieldpaths/virtual/VirtualFieldPath.g4 by ANTLR 4.9.3

    package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link VirtualFieldPathParser}.
 */
public interface VirtualFieldPathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#mountedPath}.
	 * @param ctx the parse tree
	 */
	void enterMountedPath(VirtualFieldPathParser.MountedPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#mountedPath}.
	 * @param ctx the parse tree
	 */
	void exitMountedPath(VirtualFieldPathParser.MountedPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#rowSelectorAwareFieldPath}.
	 * @param ctx the parse tree
	 */
	void enterRowSelectorAwareFieldPath(VirtualFieldPathParser.RowSelectorAwareFieldPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#rowSelectorAwareFieldPath}.
	 * @param ctx the parse tree
	 */
	void exitRowSelectorAwareFieldPath(VirtualFieldPathParser.RowSelectorAwareFieldPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#virtualFieldPath}.
	 * @param ctx the parse tree
	 */
	void enterVirtualFieldPath(VirtualFieldPathParser.VirtualFieldPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#virtualFieldPath}.
	 * @param ctx the parse tree
	 */
	void exitVirtualFieldPath(VirtualFieldPathParser.VirtualFieldPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#mountPoint}.
	 * @param ctx the parse tree
	 */
	void enterMountPoint(VirtualFieldPathParser.MountPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#mountPoint}.
	 * @param ctx the parse tree
	 */
	void exitMountPoint(VirtualFieldPathParser.MountPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#rowSelector}.
	 * @param ctx the parse tree
	 */
	void enterRowSelector(VirtualFieldPathParser.RowSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#rowSelector}.
	 * @param ctx the parse tree
	 */
	void exitRowSelector(VirtualFieldPathParser.RowSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fieldRefExprDefault}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void enterFieldRefExprDefault(VirtualFieldPathParser.FieldRefExprDefaultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fieldRefExprDefault}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void exitFieldRefExprDefault(VirtualFieldPathParser.FieldRefExprDefaultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void enterMemberRef(VirtualFieldPathParser.MemberRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void exitMemberRef(VirtualFieldPathParser.MemberRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lookupRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void enterLookupRef(VirtualFieldPathParser.LookupRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lookupRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void exitLookupRef(VirtualFieldPathParser.LookupRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code streamRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void enterStreamRef(VirtualFieldPathParser.StreamRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code streamRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void exitStreamRef(VirtualFieldPathParser.StreamRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code filterRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void enterFilterRef(VirtualFieldPathParser.FilterRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code filterRef}
	 * labeled alternative in {@link VirtualFieldPathParser#fieldReferenceExpr}.
	 * @param ctx the parse tree
	 */
	void exitFilterRef(VirtualFieldPathParser.FilterRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#collectionPredicate}.
	 * @param ctx the parse tree
	 */
	void enterCollectionPredicate(VirtualFieldPathParser.CollectionPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#collectionPredicate}.
	 * @param ctx the parse tree
	 */
	void exitCollectionPredicate(VirtualFieldPathParser.CollectionPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateTrivial}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicateTrivial(VirtualFieldPathParser.PredicateTrivialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateTrivial}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicateTrivial(VirtualFieldPathParser.PredicateTrivialContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterOrPredicate(VirtualFieldPathParser.OrPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitOrPredicate(VirtualFieldPathParser.OrPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateBase}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicateBase(VirtualFieldPathParser.PredicateBaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateBase}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicateBase(VirtualFieldPathParser.PredicateBaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateParen}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicateParen(VirtualFieldPathParser.PredicateParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateParen}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicateParen(VirtualFieldPathParser.PredicateParenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterInPredicate(VirtualFieldPathParser.InPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitInPredicate(VirtualFieldPathParser.InPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterNotPredicate(VirtualFieldPathParser.NotPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitNotPredicate(VirtualFieldPathParser.NotPredicateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterAndPredicate(VirtualFieldPathParser.AndPredicateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andPredicate}
	 * labeled alternative in {@link VirtualFieldPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitAndPredicate(VirtualFieldPathParser.AndPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#predicateWithParenthesis}.
	 * @param ctx the parse tree
	 */
	void enterPredicateWithParenthesis(VirtualFieldPathParser.PredicateWithParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#predicateWithParenthesis}.
	 * @param ctx the parse tree
	 */
	void exitPredicateWithParenthesis(VirtualFieldPathParser.PredicateWithParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSignedExpr(VirtualFieldPathParser.SignedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSignedExpr(VirtualFieldPathParser.SignedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code refExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRefExpr(VirtualFieldPathParser.RefExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code refExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRefExpr(VirtualFieldPathParser.RefExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpr(VirtualFieldPathParser.ParenthesizedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpr(VirtualFieldPathParser.ParenthesizedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(VirtualFieldPathParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(VirtualFieldPathParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpr(VirtualFieldPathParser.FunctionCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpr(VirtualFieldPathParser.FunctionCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(VirtualFieldPathParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(VirtualFieldPathParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiplicativeExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpr(VirtualFieldPathParser.MultiplicativeExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiplicativeExpr}
	 * labeled alternative in {@link VirtualFieldPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpr(VirtualFieldPathParser.MultiplicativeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(VirtualFieldPathParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(VirtualFieldPathParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(VirtualFieldPathParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(VirtualFieldPathParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link VirtualFieldPathParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(VirtualFieldPathParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalLiteral}
	 * labeled alternative in {@link VirtualFieldPathParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(VirtualFieldPathParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link VirtualFieldPathParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(VirtualFieldPathParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerLiteral}
	 * labeled alternative in {@link VirtualFieldPathParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(VirtualFieldPathParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterSign(VirtualFieldPathParser.SignContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitSign(VirtualFieldPathParser.SignContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(VirtualFieldPathParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(VirtualFieldPathParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(VirtualFieldPathParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(VirtualFieldPathParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code contextVariable}
	 * labeled alternative in {@link VirtualFieldPathParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterContextVariable(VirtualFieldPathParser.ContextVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code contextVariable}
	 * labeled alternative in {@link VirtualFieldPathParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitContextVariable(VirtualFieldPathParser.ContextVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code predicateCurrentVariable}
	 * labeled alternative in {@link VirtualFieldPathParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterPredicateCurrentVariable(VirtualFieldPathParser.PredicateCurrentVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code predicateCurrentVariable}
	 * labeled alternative in {@link VirtualFieldPathParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitPredicateCurrentVariable(VirtualFieldPathParser.PredicateCurrentVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(VirtualFieldPathParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(VirtualFieldPathParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeOperator(VirtualFieldPathParser.MultiplicativeOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeOperator(VirtualFieldPathParser.MultiplicativeOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#additiveOperator}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveOperator(VirtualFieldPathParser.AdditiveOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#additiveOperator}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveOperator(VirtualFieldPathParser.AdditiveOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VirtualFieldPathParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(VirtualFieldPathParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VirtualFieldPathParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(VirtualFieldPathParser.IdentifierContext ctx);
}