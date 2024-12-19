// Generated from com/linkedin/dataguard/runtime/fieldpaths/virtual/VirtualFieldPath.g4 by ANTLR 4.9.3

    package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class VirtualFieldPathParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, LEFT_PAREN=3, RIGHT_PAREN=4, LEFT_BRACKET=5, RIGHT_BRACKET=6, 
		LEFT_CURLY=7, RIGHT_CURLY=8, QUESTION_MARK=9, DOT=10, COLON=11, ASTERISK=12, 
		DIVISION_OP=13, MOD_OP=14, PLUS=15, MINUS=16, CONTEXT_VARIABLE=17, CURRENT_VARIABLE=18, 
		AND_OP=19, OR_OP=20, NOT_OP=21, IN_OP=22, EQUALITY_OP=23, INEQUALITY_OP=24, 
		LT_OP=25, GT_OP=26, LTE_OP=27, GTE_OP=28, TRUE=29, FALSE=30, DECIMAL_VALUE=31, 
		INTEGER_VALUE=32, STRING=33, IDENTIFIER=34, WS=35, ANY=36;
	public static final int
		RULE_mountedPath = 0, RULE_rowSelectorAwareFieldPath = 1, RULE_virtualFieldPath = 2, 
		RULE_mountPoint = 3, RULE_rowSelector = 4, RULE_fieldReferenceExpr = 5, 
		RULE_collectionPredicate = 6, RULE_predicate = 7, RULE_predicateWithParenthesis = 8, 
		RULE_expr = 9, RULE_literal = 10, RULE_functionCall = 11, RULE_numericLiteral = 12, 
		RULE_sign = 13, RULE_stringLiteral = 14, RULE_booleanLiteral = 15, RULE_variable = 16, 
		RULE_comparisonOperator = 17, RULE_multiplicativeOperator = 18, RULE_additiveOperator = 19, 
		RULE_identifier = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"mountedPath", "rowSelectorAwareFieldPath", "virtualFieldPath", "mountPoint", 
			"rowSelector", "fieldReferenceExpr", "collectionPredicate", "predicate", 
			"predicateWithParenthesis", "expr", "literal", "functionCall", "numericLiteral", 
			"sign", "stringLiteral", "booleanLiteral", "variable", "comparisonOperator", 
			"multiplicativeOperator", "additiveOperator", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'mount_point='", "','", "'('", "')'", "'['", "']'", "'{'", "'}'", 
			"'?'", "'.'", "':'", "'*'", "'/'", "'%'", "'+'", "'-'", "'$'", "'@'", 
			"'&&'", "'||'", "'!'", "'IN'", "'=='", "'!='", "'<'", "'>'", "'<='", 
			"'>='", "'true'", "'false'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACKET", "RIGHT_BRACKET", 
			"LEFT_CURLY", "RIGHT_CURLY", "QUESTION_MARK", "DOT", "COLON", "ASTERISK", 
			"DIVISION_OP", "MOD_OP", "PLUS", "MINUS", "CONTEXT_VARIABLE", "CURRENT_VARIABLE", 
			"AND_OP", "OR_OP", "NOT_OP", "IN_OP", "EQUALITY_OP", "INEQUALITY_OP", 
			"LT_OP", "GT_OP", "LTE_OP", "GTE_OP", "TRUE", "FALSE", "DECIMAL_VALUE", 
			"INTEGER_VALUE", "STRING", "IDENTIFIER", "WS", "ANY"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "VirtualFieldPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public VirtualFieldPathParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class MountedPathContext extends ParserRuleContext {
		public MountPointContext mountPoint() {
			return getRuleContext(MountPointContext.class,0);
		}
		public RowSelectorAwareFieldPathContext rowSelectorAwareFieldPath() {
			return getRuleContext(RowSelectorAwareFieldPathContext.class,0);
		}
		public TerminalNode EOF() { return getToken(VirtualFieldPathParser.EOF, 0); }
		public MountedPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mountedPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterMountedPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitMountedPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitMountedPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MountedPathContext mountedPath() throws RecognitionException {
		MountedPathContext _localctx = new MountedPathContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_mountedPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			mountPoint();
			setState(43);
			rowSelectorAwareFieldPath();
			setState(44);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RowSelectorAwareFieldPathContext extends ParserRuleContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(VirtualFieldPathParser.EOF, 0); }
		public RowSelectorContext rowSelector() {
			return getRuleContext(RowSelectorContext.class,0);
		}
		public RowSelectorAwareFieldPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rowSelectorAwareFieldPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterRowSelectorAwareFieldPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitRowSelectorAwareFieldPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitRowSelectorAwareFieldPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RowSelectorAwareFieldPathContext rowSelectorAwareFieldPath() throws RecognitionException {
		RowSelectorAwareFieldPathContext _localctx = new RowSelectorAwareFieldPathContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_rowSelectorAwareFieldPath);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACKET) {
				{
				setState(46);
				rowSelector();
				}
			}

			setState(49);
			fieldReferenceExpr(0);
			setState(50);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VirtualFieldPathContext extends ParserRuleContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(VirtualFieldPathParser.EOF, 0); }
		public VirtualFieldPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_virtualFieldPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterVirtualFieldPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitVirtualFieldPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitVirtualFieldPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VirtualFieldPathContext virtualFieldPath() throws RecognitionException {
		VirtualFieldPathContext _localctx = new VirtualFieldPathContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_virtualFieldPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			fieldReferenceExpr(0);
			setState(53);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MountPointContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACKET() { return getToken(VirtualFieldPathParser.LEFT_BRACKET, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(VirtualFieldPathParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(VirtualFieldPathParser.RIGHT_PAREN, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(VirtualFieldPathParser.RIGHT_BRACKET, 0); }
		public RowSelectorContext rowSelector() {
			return getRuleContext(RowSelectorContext.class,0);
		}
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public MountPointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mountPoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterMountPoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitMountPoint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitMountPoint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MountPointContext mountPoint() throws RecognitionException {
		MountPointContext _localctx = new MountPointContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_mountPoint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(LEFT_BRACKET);
			setState(56);
			match(T__0);
			setState(57);
			match(LEFT_PAREN);
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_BRACKET) {
				{
				setState(58);
				rowSelector();
				}
			}

			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CONTEXT_VARIABLE || _la==CURRENT_VARIABLE) {
				{
				setState(61);
				fieldReferenceExpr(0);
				}
			}

			setState(64);
			match(RIGHT_PAREN);
			setState(65);
			match(RIGHT_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RowSelectorContext extends ParserRuleContext {
		public CollectionPredicateContext collectionPredicate() {
			return getRuleContext(CollectionPredicateContext.class,0);
		}
		public RowSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rowSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterRowSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitRowSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitRowSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RowSelectorContext rowSelector() throws RecognitionException {
		RowSelectorContext _localctx = new RowSelectorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_rowSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			collectionPredicate();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldReferenceExprContext extends ParserRuleContext {
		public FieldReferenceExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldReferenceExpr; }
	 
		public FieldReferenceExprContext() { }
		public void copyFrom(FieldReferenceExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FieldRefExprDefaultContext extends FieldReferenceExprContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public FieldRefExprDefaultContext(FieldReferenceExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterFieldRefExprDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitFieldRefExprDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitFieldRefExprDefault(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MemberRefContext extends FieldReferenceExprContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(VirtualFieldPathParser.DOT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public MemberRefContext(FieldReferenceExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterMemberRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitMemberRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitMemberRef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LookupRefContext extends FieldReferenceExprContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(VirtualFieldPathParser.LEFT_BRACKET, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(VirtualFieldPathParser.RIGHT_BRACKET, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public LookupRefContext(FieldReferenceExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterLookupRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitLookupRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitLookupRef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StreamRefContext extends FieldReferenceExprContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(VirtualFieldPathParser.LEFT_BRACKET, 0); }
		public TerminalNode COLON() { return getToken(VirtualFieldPathParser.COLON, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(VirtualFieldPathParser.RIGHT_BRACKET, 0); }
		public StreamRefContext(FieldReferenceExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterStreamRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitStreamRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitStreamRef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FilterRefContext extends FieldReferenceExprContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public CollectionPredicateContext collectionPredicate() {
			return getRuleContext(CollectionPredicateContext.class,0);
		}
		public FilterRefContext(FieldReferenceExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterFilterRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitFilterRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitFilterRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldReferenceExprContext fieldReferenceExpr() throws RecognitionException {
		return fieldReferenceExpr(0);
	}

	private FieldReferenceExprContext fieldReferenceExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FieldReferenceExprContext _localctx = new FieldReferenceExprContext(_ctx, _parentState);
		FieldReferenceExprContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_fieldReferenceExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new FieldRefExprDefaultContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(70);
			variable();
			}
			_ctx.stop = _input.LT(-1);
			setState(91);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(89);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new MemberRefContext(new FieldReferenceExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_fieldReferenceExpr);
						setState(72);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(73);
						match(DOT);
						setState(74);
						identifier();
						}
						break;
					case 2:
						{
						_localctx = new LookupRefContext(new FieldReferenceExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_fieldReferenceExpr);
						setState(75);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(76);
						match(LEFT_BRACKET);
						setState(79);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case MINUS:
						case TRUE:
						case FALSE:
						case DECIMAL_VALUE:
						case INTEGER_VALUE:
						case STRING:
							{
							setState(77);
							literal();
							}
							break;
						case IDENTIFIER:
							{
							setState(78);
							functionCall();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(81);
						match(RIGHT_BRACKET);
						}
						break;
					case 3:
						{
						_localctx = new StreamRefContext(new FieldReferenceExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_fieldReferenceExpr);
						setState(83);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(84);
						match(LEFT_BRACKET);
						setState(85);
						match(COLON);
						setState(86);
						match(RIGHT_BRACKET);
						}
						break;
					case 4:
						{
						_localctx = new FilterRefContext(new FieldReferenceExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_fieldReferenceExpr);
						setState(87);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(88);
						collectionPredicate();
						}
						break;
					}
					} 
				}
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CollectionPredicateContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACKET() { return getToken(VirtualFieldPathParser.LEFT_BRACKET, 0); }
		public TerminalNode QUESTION_MARK() { return getToken(VirtualFieldPathParser.QUESTION_MARK, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(VirtualFieldPathParser.LEFT_PAREN, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(VirtualFieldPathParser.RIGHT_PAREN, 0); }
		public TerminalNode RIGHT_BRACKET() { return getToken(VirtualFieldPathParser.RIGHT_BRACKET, 0); }
		public CollectionPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectionPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterCollectionPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitCollectionPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitCollectionPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionPredicateContext collectionPredicate() throws RecognitionException {
		CollectionPredicateContext _localctx = new CollectionPredicateContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_collectionPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			match(LEFT_BRACKET);
			setState(95);
			match(QUESTION_MARK);
			setState(96);
			match(LEFT_PAREN);
			setState(97);
			predicate(0);
			setState(98);
			match(RIGHT_PAREN);
			setState(99);
			match(RIGHT_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateContext extends ParserRuleContext {
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
	 
		public PredicateContext() { }
		public void copyFrom(PredicateContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PredicateTrivialContext extends PredicateContext {
		public BooleanLiteralContext booleanLiteral() {
			return getRuleContext(BooleanLiteralContext.class,0);
		}
		public PredicateTrivialContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterPredicateTrivial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitPredicateTrivial(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitPredicateTrivial(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrPredicateContext extends PredicateContext {
		public PredicateContext leftPredicate;
		public PredicateContext rightPredicate;
		public TerminalNode OR_OP() { return getToken(VirtualFieldPathParser.OR_OP, 0); }
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public OrPredicateContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterOrPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitOrPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitOrPredicate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PredicateBaseContext extends PredicateContext {
		public ExprContext leftExpr;
		public ExprContext rightExpr;
		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public PredicateBaseContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterPredicateBase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitPredicateBase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitPredicateBase(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PredicateParenContext extends PredicateContext {
		public PredicateWithParenthesisContext predicateWithParenthesis() {
			return getRuleContext(PredicateWithParenthesisContext.class,0);
		}
		public PredicateParenContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterPredicateParen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitPredicateParen(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitPredicateParen(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InPredicateContext extends PredicateContext {
		public ExprContext leftExpr;
		public TerminalNode IN_OP() { return getToken(VirtualFieldPathParser.IN_OP, 0); }
		public TerminalNode LEFT_BRACKET() { return getToken(VirtualFieldPathParser.LEFT_BRACKET, 0); }
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public TerminalNode RIGHT_BRACKET() { return getToken(VirtualFieldPathParser.RIGHT_BRACKET, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public InPredicateContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterInPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitInPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitInPredicate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotPredicateContext extends PredicateContext {
		public TerminalNode NOT_OP() { return getToken(VirtualFieldPathParser.NOT_OP, 0); }
		public PredicateWithParenthesisContext predicateWithParenthesis() {
			return getRuleContext(PredicateWithParenthesisContext.class,0);
		}
		public NotPredicateContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterNotPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitNotPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitNotPredicate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndPredicateContext extends PredicateContext {
		public PredicateContext leftPredicate;
		public PredicateContext rightPredicate;
		public TerminalNode AND_OP() { return getToken(VirtualFieldPathParser.AND_OP, 0); }
		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}
		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class,i);
		}
		public AndPredicateContext(PredicateContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterAndPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitAndPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitAndPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		return predicate(0);
	}

	private PredicateContext predicate(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PredicateContext _localctx = new PredicateContext(_ctx, _parentState);
		PredicateContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_predicate, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				_localctx = new PredicateTrivialContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(102);
				booleanLiteral();
				}
				break;
			case 2:
				{
				_localctx = new PredicateBaseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(103);
				((PredicateBaseContext)_localctx).leftExpr = expr(0);
				setState(104);
				comparisonOperator();
				setState(105);
				((PredicateBaseContext)_localctx).rightExpr = expr(0);
				}
				break;
			case 3:
				{
				_localctx = new PredicateParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(107);
				predicateWithParenthesis();
				}
				break;
			case 4:
				{
				_localctx = new NotPredicateContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108);
				match(NOT_OP);
				setState(109);
				predicateWithParenthesis();
				}
				break;
			case 5:
				{
				_localctx = new InPredicateContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(110);
				((InPredicateContext)_localctx).leftExpr = expr(0);
				setState(111);
				match(IN_OP);
				setState(112);
				match(LEFT_BRACKET);
				setState(113);
				literal();
				setState(118);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(114);
					match(T__1);
					setState(115);
					literal();
					}
					}
					setState(120);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(121);
				match(RIGHT_BRACKET);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(133);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(131);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new AndPredicateContext(new PredicateContext(_parentctx, _parentState));
						((AndPredicateContext)_localctx).leftPredicate = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_predicate);
						setState(125);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(126);
						match(AND_OP);
						setState(127);
						((AndPredicateContext)_localctx).rightPredicate = predicate(4);
						}
						break;
					case 2:
						{
						_localctx = new OrPredicateContext(new PredicateContext(_parentctx, _parentState));
						((OrPredicateContext)_localctx).leftPredicate = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_predicate);
						setState(128);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(129);
						match(OR_OP);
						setState(130);
						((OrPredicateContext)_localctx).rightPredicate = predicate(3);
						}
						break;
					}
					} 
				}
				setState(135);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PredicateWithParenthesisContext extends ParserRuleContext {
		public TerminalNode LEFT_PAREN() { return getToken(VirtualFieldPathParser.LEFT_PAREN, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(VirtualFieldPathParser.RIGHT_PAREN, 0); }
		public PredicateWithParenthesisContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateWithParenthesis; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterPredicateWithParenthesis(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitPredicateWithParenthesis(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitPredicateWithParenthesis(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateWithParenthesisContext predicateWithParenthesis() throws RecognitionException {
		PredicateWithParenthesisContext _localctx = new PredicateWithParenthesisContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_predicateWithParenthesis);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(LEFT_PAREN);
			setState(137);
			predicate(0);
			setState(138);
			match(RIGHT_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SignedExprContext extends ExprContext {
		public SignContext sign() {
			return getRuleContext(SignContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SignedExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterSignedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitSignedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitSignedExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RefExprContext extends ExprContext {
		public FieldReferenceExprContext fieldReferenceExpr() {
			return getRuleContext(FieldReferenceExprContext.class,0);
		}
		public RefExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterRefExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitRefExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitRefExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesizedExprContext extends ExprContext {
		public TerminalNode LEFT_PAREN() { return getToken(VirtualFieldPathParser.LEFT_PAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(VirtualFieldPathParser.RIGHT_PAREN, 0); }
		public ParenthesizedExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterParenthesizedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitParenthesizedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitParenthesizedExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralExprContext extends ExprContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterLiteralExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitLiteralExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallExprContext extends ExprContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public FunctionCallExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterFunctionCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitFunctionCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitFunctionCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AdditiveExprContext extends ExprContext {
		public ExprContext left;
		public AdditiveOperatorContext operator;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AdditiveOperatorContext additiveOperator() {
			return getRuleContext(AdditiveOperatorContext.class,0);
		}
		public AdditiveExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterAdditiveExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitAdditiveExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitAdditiveExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultiplicativeExprContext extends ExprContext {
		public ExprContext left;
		public MultiplicativeOperatorContext operator;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MultiplicativeOperatorContext multiplicativeOperator() {
			return getRuleContext(MultiplicativeOperatorContext.class,0);
		}
		public MultiplicativeExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterMultiplicativeExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitMultiplicativeExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitMultiplicativeExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new LiteralExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(141);
				literal();
				}
				break;
			case 2:
				{
				_localctx = new RefExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(142);
				fieldReferenceExpr(0);
				}
				break;
			case 3:
				{
				_localctx = new ParenthesizedExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(143);
				match(LEFT_PAREN);
				setState(144);
				expr(0);
				setState(145);
				match(RIGHT_PAREN);
				}
				break;
			case 4:
				{
				_localctx = new SignedExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(147);
				sign();
				setState(148);
				expr(4);
				}
				break;
			case 5:
				{
				_localctx = new FunctionCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(150);
				functionCall();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(163);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(161);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new MultiplicativeExprContext(new ExprContext(_parentctx, _parentState));
						((MultiplicativeExprContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(153);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(154);
						((MultiplicativeExprContext)_localctx).operator = multiplicativeOperator();
						setState(155);
						((MultiplicativeExprContext)_localctx).right = expr(4);
						}
						break;
					case 2:
						{
						_localctx = new AdditiveExprContext(new ExprContext(_parentctx, _parentState));
						((AdditiveExprContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(157);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(158);
						((AdditiveExprContext)_localctx).operator = additiveOperator();
						setState(159);
						((AdditiveExprContext)_localctx).right = expr(3);
						}
						break;
					}
					} 
				}
				setState(165);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public NumericLiteralContext numericLiteral() {
			return getRuleContext(NumericLiteralContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public BooleanLiteralContext booleanLiteral() {
			return getRuleContext(BooleanLiteralContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_literal);
		try {
			setState(169);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MINUS:
			case DECIMAL_VALUE:
			case INTEGER_VALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				numericLiteral();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				stringLiteral();
				}
				break;
			case TRUE:
			case FALSE:
				enterOuterAlt(_localctx, 3);
				{
				setState(168);
				booleanLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionCallContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(VirtualFieldPathParser.LEFT_PAREN, 0); }
		public TerminalNode RIGHT_PAREN() { return getToken(VirtualFieldPathParser.RIGHT_PAREN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_functionCall);
		int _la;
		try {
			setState(187);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(171);
				identifier();
				setState(172);
				match(LEFT_PAREN);
				setState(173);
				match(RIGHT_PAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(175);
				identifier();
				setState(176);
				match(LEFT_PAREN);
				setState(177);
				expr(0);
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(178);
					match(T__1);
					setState(179);
					expr(0);
					}
					}
					setState(184);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(185);
				match(RIGHT_PAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumericLiteralContext extends ParserRuleContext {
		public NumericLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericLiteral; }
	 
		public NumericLiteralContext() { }
		public void copyFrom(NumericLiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DecimalLiteralContext extends NumericLiteralContext {
		public TerminalNode DECIMAL_VALUE() { return getToken(VirtualFieldPathParser.DECIMAL_VALUE, 0); }
		public TerminalNode MINUS() { return getToken(VirtualFieldPathParser.MINUS, 0); }
		public DecimalLiteralContext(NumericLiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterDecimalLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitDecimalLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitDecimalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntegerLiteralContext extends NumericLiteralContext {
		public TerminalNode INTEGER_VALUE() { return getToken(VirtualFieldPathParser.INTEGER_VALUE, 0); }
		public TerminalNode MINUS() { return getToken(VirtualFieldPathParser.MINUS, 0); }
		public IntegerLiteralContext(NumericLiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterIntegerLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitIntegerLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericLiteralContext numericLiteral() throws RecognitionException {
		NumericLiteralContext _localctx = new NumericLiteralContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_numericLiteral);
		int _la;
		try {
			setState(197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				_localctx = new DecimalLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(190);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(189);
					match(MINUS);
					}
				}

				setState(192);
				match(DECIMAL_VALUE);
				}
				break;
			case 2:
				_localctx = new IntegerLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(194);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(193);
					match(MINUS);
					}
				}

				setState(196);
				match(INTEGER_VALUE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SignContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(VirtualFieldPathParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(VirtualFieldPathParser.MINUS, 0); }
		public SignContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sign; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterSign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitSign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitSign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignContext sign() throws RecognitionException {
		SignContext _localctx = new SignContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_sign);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			_la = _input.LA(1);
			if ( !(_la==PLUS || _la==MINUS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(VirtualFieldPathParser.STRING, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanLiteralContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(VirtualFieldPathParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(VirtualFieldPathParser.FALSE, 0); }
		public BooleanLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterBooleanLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitBooleanLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitBooleanLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanLiteralContext booleanLiteral() throws RecognitionException {
		BooleanLiteralContext _localctx = new BooleanLiteralContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_booleanLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
	 
		public VariableContext() { }
		public void copyFrom(VariableContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ContextVariableContext extends VariableContext {
		public TerminalNode CONTEXT_VARIABLE() { return getToken(VirtualFieldPathParser.CONTEXT_VARIABLE, 0); }
		public ContextVariableContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterContextVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitContextVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitContextVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PredicateCurrentVariableContext extends VariableContext {
		public TerminalNode CURRENT_VARIABLE() { return getToken(VirtualFieldPathParser.CURRENT_VARIABLE, 0); }
		public PredicateCurrentVariableContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterPredicateCurrentVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitPredicateCurrentVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitPredicateCurrentVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_variable);
		try {
			setState(207);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONTEXT_VARIABLE:
				_localctx = new ContextVariableContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(205);
				match(CONTEXT_VARIABLE);
				}
				break;
			case CURRENT_VARIABLE:
				_localctx = new PredicateCurrentVariableContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(206);
				match(CURRENT_VARIABLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonOperatorContext extends ParserRuleContext {
		public TerminalNode EQUALITY_OP() { return getToken(VirtualFieldPathParser.EQUALITY_OP, 0); }
		public TerminalNode INEQUALITY_OP() { return getToken(VirtualFieldPathParser.INEQUALITY_OP, 0); }
		public TerminalNode LT_OP() { return getToken(VirtualFieldPathParser.LT_OP, 0); }
		public TerminalNode GT_OP() { return getToken(VirtualFieldPathParser.GT_OP, 0); }
		public TerminalNode LTE_OP() { return getToken(VirtualFieldPathParser.LTE_OP, 0); }
		public TerminalNode GTE_OP() { return getToken(VirtualFieldPathParser.GTE_OP, 0); }
		public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterComparisonOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitComparisonOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitComparisonOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUALITY_OP) | (1L << INEQUALITY_OP) | (1L << LT_OP) | (1L << GT_OP) | (1L << LTE_OP) | (1L << GTE_OP))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MultiplicativeOperatorContext extends ParserRuleContext {
		public TerminalNode ASTERISK() { return getToken(VirtualFieldPathParser.ASTERISK, 0); }
		public TerminalNode DIVISION_OP() { return getToken(VirtualFieldPathParser.DIVISION_OP, 0); }
		public TerminalNode MOD_OP() { return getToken(VirtualFieldPathParser.MOD_OP, 0); }
		public MultiplicativeOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterMultiplicativeOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitMultiplicativeOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitMultiplicativeOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeOperatorContext multiplicativeOperator() throws RecognitionException {
		MultiplicativeOperatorContext _localctx = new MultiplicativeOperatorContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_multiplicativeOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ASTERISK) | (1L << DIVISION_OP) | (1L << MOD_OP))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdditiveOperatorContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(VirtualFieldPathParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(VirtualFieldPathParser.MINUS, 0); }
		public AdditiveOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterAdditiveOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitAdditiveOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitAdditiveOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveOperatorContext additiveOperator() throws RecognitionException {
		AdditiveOperatorContext _localctx = new AdditiveOperatorContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_additiveOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			_la = _input.LA(1);
			if ( !(_la==PLUS || _la==MINUS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(VirtualFieldPathParser.IDENTIFIER, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof VirtualFieldPathListener ) ((VirtualFieldPathListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof VirtualFieldPathVisitor ) return ((VirtualFieldPathVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return fieldReferenceExpr_sempred((FieldReferenceExprContext)_localctx, predIndex);
		case 7:
			return predicate_sempred((PredicateContext)_localctx, predIndex);
		case 9:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean fieldReferenceExpr_sempred(FieldReferenceExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean predicate_sempred(PredicateContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 3);
		case 7:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3&\u00dc\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\3\2\3\3\5\3\62\n"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\5\5>\n\5\3\5\5\5A\n\5\3\5\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7R\n\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\7\7\\\n\7\f\7\16\7_\13\7\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\7\tw\n\t\f\t\16\tz\13\t\3\t\3\t\5\t~\n\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t"+
		"\u0086\n\t\f\t\16\t\u0089\13\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u009a\n\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\7\13\u00a4\n\13\f\13\16\13\u00a7\13\13\3\f\3\f\3\f\5\f"+
		"\u00ac\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u00b7\n\r\f\r\16\r"+
		"\u00ba\13\r\3\r\3\r\5\r\u00be\n\r\3\16\5\16\u00c1\n\16\3\16\3\16\5\16"+
		"\u00c5\n\16\3\16\5\16\u00c8\n\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3"+
		"\22\5\22\u00d2\n\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\2\5\f"+
		"\20\24\27\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*\2\6\3\2\21\22"+
		"\3\2\37 \3\2\31\36\3\2\16\20\2\u00e3\2,\3\2\2\2\4\61\3\2\2\2\6\66\3\2"+
		"\2\2\b9\3\2\2\2\nE\3\2\2\2\fG\3\2\2\2\16`\3\2\2\2\20}\3\2\2\2\22\u008a"+
		"\3\2\2\2\24\u0099\3\2\2\2\26\u00ab\3\2\2\2\30\u00bd\3\2\2\2\32\u00c7\3"+
		"\2\2\2\34\u00c9\3\2\2\2\36\u00cb\3\2\2\2 \u00cd\3\2\2\2\"\u00d1\3\2\2"+
		"\2$\u00d3\3\2\2\2&\u00d5\3\2\2\2(\u00d7\3\2\2\2*\u00d9\3\2\2\2,-\5\b\5"+
		"\2-.\5\4\3\2./\7\2\2\3/\3\3\2\2\2\60\62\5\n\6\2\61\60\3\2\2\2\61\62\3"+
		"\2\2\2\62\63\3\2\2\2\63\64\5\f\7\2\64\65\7\2\2\3\65\5\3\2\2\2\66\67\5"+
		"\f\7\2\678\7\2\2\38\7\3\2\2\29:\7\7\2\2:;\7\3\2\2;=\7\5\2\2<>\5\n\6\2"+
		"=<\3\2\2\2=>\3\2\2\2>@\3\2\2\2?A\5\f\7\2@?\3\2\2\2@A\3\2\2\2AB\3\2\2\2"+
		"BC\7\6\2\2CD\7\b\2\2D\t\3\2\2\2EF\5\16\b\2F\13\3\2\2\2GH\b\7\1\2HI\5\""+
		"\22\2I]\3\2\2\2JK\f\6\2\2KL\7\f\2\2L\\\5*\26\2MN\f\5\2\2NQ\7\7\2\2OR\5"+
		"\26\f\2PR\5\30\r\2QO\3\2\2\2QP\3\2\2\2RS\3\2\2\2ST\7\b\2\2T\\\3\2\2\2"+
		"UV\f\4\2\2VW\7\7\2\2WX\7\r\2\2X\\\7\b\2\2YZ\f\3\2\2Z\\\5\16\b\2[J\3\2"+
		"\2\2[M\3\2\2\2[U\3\2\2\2[Y\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\r\3"+
		"\2\2\2_]\3\2\2\2`a\7\7\2\2ab\7\13\2\2bc\7\5\2\2cd\5\20\t\2de\7\6\2\2e"+
		"f\7\b\2\2f\17\3\2\2\2gh\b\t\1\2h~\5 \21\2ij\5\24\13\2jk\5$\23\2kl\5\24"+
		"\13\2l~\3\2\2\2m~\5\22\n\2no\7\27\2\2o~\5\22\n\2pq\5\24\13\2qr\7\30\2"+
		"\2rs\7\7\2\2sx\5\26\f\2tu\7\4\2\2uw\5\26\f\2vt\3\2\2\2wz\3\2\2\2xv\3\2"+
		"\2\2xy\3\2\2\2y{\3\2\2\2zx\3\2\2\2{|\7\b\2\2|~\3\2\2\2}g\3\2\2\2}i\3\2"+
		"\2\2}m\3\2\2\2}n\3\2\2\2}p\3\2\2\2~\u0087\3\2\2\2\177\u0080\f\5\2\2\u0080"+
		"\u0081\7\25\2\2\u0081\u0086\5\20\t\6\u0082\u0083\f\4\2\2\u0083\u0084\7"+
		"\26\2\2\u0084\u0086\5\20\t\5\u0085\177\3\2\2\2\u0085\u0082\3\2\2\2\u0086"+
		"\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\21\3\2\2"+
		"\2\u0089\u0087\3\2\2\2\u008a\u008b\7\5\2\2\u008b\u008c\5\20\t\2\u008c"+
		"\u008d\7\6\2\2\u008d\23\3\2\2\2\u008e\u008f\b\13\1\2\u008f\u009a\5\26"+
		"\f\2\u0090\u009a\5\f\7\2\u0091\u0092\7\5\2\2\u0092\u0093\5\24\13\2\u0093"+
		"\u0094\7\6\2\2\u0094\u009a\3\2\2\2\u0095\u0096\5\34\17\2\u0096\u0097\5"+
		"\24\13\6\u0097\u009a\3\2\2\2\u0098\u009a\5\30\r\2\u0099\u008e\3\2\2\2"+
		"\u0099\u0090\3\2\2\2\u0099\u0091\3\2\2\2\u0099\u0095\3\2\2\2\u0099\u0098"+
		"\3\2\2\2\u009a\u00a5\3\2\2\2\u009b\u009c\f\5\2\2\u009c\u009d\5&\24\2\u009d"+
		"\u009e\5\24\13\6\u009e\u00a4\3\2\2\2\u009f\u00a0\f\4\2\2\u00a0\u00a1\5"+
		"(\25\2\u00a1\u00a2\5\24\13\5\u00a2\u00a4\3\2\2\2\u00a3\u009b\3\2\2\2\u00a3"+
		"\u009f\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2"+
		"\2\2\u00a6\25\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00ac\5\32\16\2\u00a9"+
		"\u00ac\5\36\20\2\u00aa\u00ac\5 \21\2\u00ab\u00a8\3\2\2\2\u00ab\u00a9\3"+
		"\2\2\2\u00ab\u00aa\3\2\2\2\u00ac\27\3\2\2\2\u00ad\u00ae\5*\26\2\u00ae"+
		"\u00af\7\5\2\2\u00af\u00b0\7\6\2\2\u00b0\u00be\3\2\2\2\u00b1\u00b2\5*"+
		"\26\2\u00b2\u00b3\7\5\2\2\u00b3\u00b8\5\24\13\2\u00b4\u00b5\7\4\2\2\u00b5"+
		"\u00b7\5\24\13\2\u00b6\u00b4\3\2\2\2\u00b7\u00ba\3\2\2\2\u00b8\u00b6\3"+
		"\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00bb\3\2\2\2\u00ba\u00b8\3\2\2\2\u00bb"+
		"\u00bc\7\6\2\2\u00bc\u00be\3\2\2\2\u00bd\u00ad\3\2\2\2\u00bd\u00b1\3\2"+
		"\2\2\u00be\31\3\2\2\2\u00bf\u00c1\7\22\2\2\u00c0\u00bf\3\2\2\2\u00c0\u00c1"+
		"\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c8\7!\2\2\u00c3\u00c5\7\22\2\2\u00c4"+
		"\u00c3\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c8\7\""+
		"\2\2\u00c7\u00c0\3\2\2\2\u00c7\u00c4\3\2\2\2\u00c8\33\3\2\2\2\u00c9\u00ca"+
		"\t\2\2\2\u00ca\35\3\2\2\2\u00cb\u00cc\7#\2\2\u00cc\37\3\2\2\2\u00cd\u00ce"+
		"\t\3\2\2\u00ce!\3\2\2\2\u00cf\u00d2\7\23\2\2\u00d0\u00d2\7\24\2\2\u00d1"+
		"\u00cf\3\2\2\2\u00d1\u00d0\3\2\2\2\u00d2#\3\2\2\2\u00d3\u00d4\t\4\2\2"+
		"\u00d4%\3\2\2\2\u00d5\u00d6\t\5\2\2\u00d6\'\3\2\2\2\u00d7\u00d8\t\2\2"+
		"\2\u00d8)\3\2\2\2\u00d9\u00da\7$\2\2\u00da+\3\2\2\2\26\61=@Q[]x}\u0085"+
		"\u0087\u0099\u00a3\u00a5\u00ab\u00b8\u00bd\u00c0\u00c4\u00c7\u00d1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}