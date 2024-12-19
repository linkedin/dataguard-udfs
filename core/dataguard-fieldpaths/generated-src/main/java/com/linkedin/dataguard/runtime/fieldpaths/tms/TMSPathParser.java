// Generated from com/linkedin/dataguard/runtime/fieldpaths/tms/TMSPath.g4 by ANTLR 4.9.3

  package com.linkedin.dataguard.runtime.fieldpaths.tms;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TMSPathParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, LETTER=16, 
		DIGIT=17, DOT=18;
	public static final int
		RULE_tmsPath = 0, RULE_fieldPath = 1, RULE_selector = 2, RULE_fieldSelector = 3, 
		RULE_typeSelector = 4, RULE_keySelector = 5, RULE_valueSelector = 6, RULE_typeName = 7, 
		RULE_fieldName = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"tmsPath", "fieldPath", "selector", "fieldSelector", "typeSelector", 
			"keySelector", "valueSelector", "typeName", "fieldName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'[type='", "']'", "'[key='", "'[value='", "'_'", "'<'", "'>'", 
			"'-'", "'$'", "':'", "','", "' '", "'@'", "'#'", "'|'", null, null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "LETTER", "DIGIT", "DOT"
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
	public String getGrammarFileName() { return "TMSPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TMSPathParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class TmsPathContext extends ParserRuleContext {
		public FieldPathContext fieldPath() {
			return getRuleContext(FieldPathContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TMSPathParser.EOF, 0); }
		public TmsPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tmsPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterTmsPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitTmsPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitTmsPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TmsPathContext tmsPath() throws RecognitionException {
		TmsPathContext _localctx = new TmsPathContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tmsPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			fieldPath(0);
			setState(19);
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

	public static class FieldPathContext extends ParserRuleContext {
		public FieldPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldPath; }
	 
		public FieldPathContext() { }
		public void copyFrom(FieldPathContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SelectorRefContext extends FieldPathContext {
		public FieldPathContext fieldPath() {
			return getRuleContext(FieldPathContext.class,0);
		}
		public TerminalNode DOT() { return getToken(TMSPathParser.DOT, 0); }
		public SelectorContext selector() {
			return getRuleContext(SelectorContext.class,0);
		}
		public SelectorRefContext(FieldPathContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterSelectorRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitSelectorRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitSelectorRef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnionSelectorRefContext extends FieldPathContext {
		public FieldPathContext fieldPath() {
			return getRuleContext(FieldPathContext.class,0);
		}
		public TypeSelectorContext typeSelector() {
			return getRuleContext(TypeSelectorContext.class,0);
		}
		public UnionSelectorRefContext(FieldPathContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterUnionSelectorRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitUnionSelectorRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitUnionSelectorRef(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BaseRefContext extends FieldPathContext {
		public SelectorContext selector() {
			return getRuleContext(SelectorContext.class,0);
		}
		public BaseRefContext(FieldPathContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterBaseRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitBaseRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitBaseRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldPathContext fieldPath() throws RecognitionException {
		return fieldPath(0);
	}

	private FieldPathContext fieldPath(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FieldPathContext _localctx = new FieldPathContext(_ctx, _parentState);
		FieldPathContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_fieldPath, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new BaseRefContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(22);
			selector();
			}
			_ctx.stop = _input.LT(-1);
			setState(31);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(29);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						_localctx = new SelectorRefContext(new FieldPathContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_fieldPath);
						setState(24);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(25);
						match(DOT);
						setState(26);
						selector();
						}
						break;
					case 2:
						{
						_localctx = new UnionSelectorRefContext(new FieldPathContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_fieldPath);
						setState(27);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(28);
						typeSelector();
						}
						break;
					}
					} 
				}
				setState(33);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
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

	public static class SelectorContext extends ParserRuleContext {
		public TypeSelectorContext typeSelector() {
			return getRuleContext(TypeSelectorContext.class,0);
		}
		public FieldSelectorContext fieldSelector() {
			return getRuleContext(FieldSelectorContext.class,0);
		}
		public KeySelectorContext keySelector() {
			return getRuleContext(KeySelectorContext.class,0);
		}
		public ValueSelectorContext valueSelector() {
			return getRuleContext(ValueSelectorContext.class,0);
		}
		public SelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectorContext selector() throws RecognitionException {
		SelectorContext _localctx = new SelectorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_selector);
		try {
			setState(38);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(34);
				typeSelector();
				}
				break;
			case T__4:
			case T__7:
			case T__8:
			case T__9:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case LETTER:
			case DIGIT:
				enterOuterAlt(_localctx, 2);
				{
				setState(35);
				fieldSelector();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				keySelector();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 4);
				{
				setState(37);
				valueSelector();
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

	public static class FieldSelectorContext extends ParserRuleContext {
		public FieldNameContext fieldName() {
			return getRuleContext(FieldNameContext.class,0);
		}
		public FieldSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterFieldSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitFieldSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitFieldSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldSelectorContext fieldSelector() throws RecognitionException {
		FieldSelectorContext _localctx = new FieldSelectorContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_fieldSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			fieldName();
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

	public static class TypeSelectorContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TypeSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterTypeSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitTypeSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitTypeSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSelectorContext typeSelector() throws RecognitionException {
		TypeSelectorContext _localctx = new TypeSelectorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typeSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(T__0);
			setState(43);
			typeName();
			setState(44);
			match(T__1);
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

	public static class KeySelectorContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public KeySelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keySelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterKeySelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitKeySelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitKeySelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeySelectorContext keySelector() throws RecognitionException {
		KeySelectorContext _localctx = new KeySelectorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_keySelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			match(T__2);
			setState(47);
			typeName();
			setState(48);
			match(T__1);
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

	public static class ValueSelectorContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ValueSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterValueSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitValueSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitValueSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueSelectorContext valueSelector() throws RecognitionException {
		ValueSelectorContext _localctx = new ValueSelectorContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_valueSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(T__3);
			setState(51);
			typeName();
			setState(52);
			match(T__1);
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

	public static class TypeNameContext extends ParserRuleContext {
		public List<TerminalNode> LETTER() { return getTokens(TMSPathParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(TMSPathParser.LETTER, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(TMSPathParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(TMSPathParser.DIGIT, i);
		}
		public List<TerminalNode> DOT() { return getTokens(TMSPathParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TMSPathParser.DOT, i);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_typeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(54);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << LETTER) | (1L << DIGIT) | (1L << DOT))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(57); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << LETTER) | (1L << DIGIT) | (1L << DOT))) != 0) );
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

	public static class FieldNameContext extends ParserRuleContext {
		public List<TerminalNode> LETTER() { return getTokens(TMSPathParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(TMSPathParser.LETTER, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(TMSPathParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(TMSPathParser.DIGIT, i);
		}
		public FieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).enterFieldName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TMSPathListener ) ((TMSPathListener)listener).exitFieldName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TMSPathVisitor ) return ((TMSPathVisitor<? extends T>)visitor).visitFieldName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldNameContext fieldName() throws RecognitionException {
		FieldNameContext _localctx = new FieldNameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_fieldName);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(60); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(59);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << LETTER) | (1L << DIGIT))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(62); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
		case 1:
			return fieldPath_sempred((FieldPathContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean fieldPath_sempred(FieldPathContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\24C\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3 \n\3\f\3\16\3#\13\3\3\4\3\4\3\4\3"+
		"\4\5\4)\n\4\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3"+
		"\t\6\t:\n\t\r\t\16\t;\3\n\6\n?\n\n\r\n\16\n@\3\n\2\3\4\13\2\4\6\b\n\f"+
		"\16\20\22\2\4\4\2\7\16\22\24\5\2\7\7\n\f\16\23\2@\2\24\3\2\2\2\4\27\3"+
		"\2\2\2\6(\3\2\2\2\b*\3\2\2\2\n,\3\2\2\2\f\60\3\2\2\2\16\64\3\2\2\2\20"+
		"9\3\2\2\2\22>\3\2\2\2\24\25\5\4\3\2\25\26\7\2\2\3\26\3\3\2\2\2\27\30\b"+
		"\3\1\2\30\31\5\6\4\2\31!\3\2\2\2\32\33\f\4\2\2\33\34\7\24\2\2\34 \5\6"+
		"\4\2\35\36\f\3\2\2\36 \5\n\6\2\37\32\3\2\2\2\37\35\3\2\2\2 #\3\2\2\2!"+
		"\37\3\2\2\2!\"\3\2\2\2\"\5\3\2\2\2#!\3\2\2\2$)\5\n\6\2%)\5\b\5\2&)\5\f"+
		"\7\2\')\5\16\b\2($\3\2\2\2(%\3\2\2\2(&\3\2\2\2(\'\3\2\2\2)\7\3\2\2\2*"+
		"+\5\22\n\2+\t\3\2\2\2,-\7\3\2\2-.\5\20\t\2./\7\4\2\2/\13\3\2\2\2\60\61"+
		"\7\5\2\2\61\62\5\20\t\2\62\63\7\4\2\2\63\r\3\2\2\2\64\65\7\6\2\2\65\66"+
		"\5\20\t\2\66\67\7\4\2\2\67\17\3\2\2\28:\t\2\2\298\3\2\2\2:;\3\2\2\2;9"+
		"\3\2\2\2;<\3\2\2\2<\21\3\2\2\2=?\t\3\2\2>=\3\2\2\2?@\3\2\2\2@>\3\2\2\2"+
		"@A\3\2\2\2A\23\3\2\2\2\7\37!(;@";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}