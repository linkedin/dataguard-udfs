// Generated from com/linkedin/dataguard/runtime/fieldpaths/tms/TMSPath.g4 by ANTLR 4.9.3

  package com.linkedin.dataguard.runtime.fieldpaths.tms;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TMSPathParser}.
 */
public interface TMSPathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#tmsPath}.
	 * @param ctx the parse tree
	 */
	void enterTmsPath(TMSPathParser.TmsPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#tmsPath}.
	 * @param ctx the parse tree
	 */
	void exitTmsPath(TMSPathParser.TmsPathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selectorRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 */
	void enterSelectorRef(TMSPathParser.SelectorRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selectorRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 */
	void exitSelectorRef(TMSPathParser.SelectorRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionSelectorRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 */
	void enterUnionSelectorRef(TMSPathParser.UnionSelectorRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionSelectorRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 */
	void exitUnionSelectorRef(TMSPathParser.UnionSelectorRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code baseRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 */
	void enterBaseRef(TMSPathParser.BaseRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code baseRef}
	 * labeled alternative in {@link TMSPathParser#fieldPath}.
	 * @param ctx the parse tree
	 */
	void exitBaseRef(TMSPathParser.BaseRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(TMSPathParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(TMSPathParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#fieldSelector}.
	 * @param ctx the parse tree
	 */
	void enterFieldSelector(TMSPathParser.FieldSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#fieldSelector}.
	 * @param ctx the parse tree
	 */
	void exitFieldSelector(TMSPathParser.FieldSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#typeSelector}.
	 * @param ctx the parse tree
	 */
	void enterTypeSelector(TMSPathParser.TypeSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#typeSelector}.
	 * @param ctx the parse tree
	 */
	void exitTypeSelector(TMSPathParser.TypeSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#keySelector}.
	 * @param ctx the parse tree
	 */
	void enterKeySelector(TMSPathParser.KeySelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#keySelector}.
	 * @param ctx the parse tree
	 */
	void exitKeySelector(TMSPathParser.KeySelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#valueSelector}.
	 * @param ctx the parse tree
	 */
	void enterValueSelector(TMSPathParser.ValueSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#valueSelector}.
	 * @param ctx the parse tree
	 */
	void exitValueSelector(TMSPathParser.ValueSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(TMSPathParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(TMSPathParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TMSPathParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(TMSPathParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TMSPathParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(TMSPathParser.FieldNameContext ctx);
}