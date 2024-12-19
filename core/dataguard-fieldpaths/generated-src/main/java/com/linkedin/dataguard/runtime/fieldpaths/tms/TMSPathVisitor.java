// Generated from com/linkedin/dataguard/runtime/fieldpaths/tms/TMSPath.g4 by ANTLR 4.9.3

  package com.linkedin.dataguard.runtime.fieldpaths.tms;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TMSPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TMSPathVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#tmsPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTmsPath(TMSPathParser.TmsPathContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectorRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectorRef(TMSPathParser.SelectorRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unionSelectorRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionSelectorRef(TMSPathParser.UnionSelectorRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code baseRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseRef(TMSPathParser.BaseRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(TMSPathParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#fieldSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldSelector(TMSPathParser.FieldSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#typeSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSelector(TMSPathParser.TypeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#keySelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeySelector(TMSPathParser.KeySelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#valueSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueSelector(TMSPathParser.ValueSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(TMSPathParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link TMSPathParser#fieldName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldName(TMSPathParser.FieldNameContext ctx);
}