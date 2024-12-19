// Generated from com/linkedin/dataguard/runtime/fieldpaths/virtual/VirtualFieldPath.g4 by ANTLR 4.9.3

    package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class VirtualFieldPathLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACKET", "RIGHT_BRACKET", 
			"LEFT_CURLY", "RIGHT_CURLY", "QUESTION_MARK", "DOT", "COLON", "ASTERISK", 
			"DIVISION_OP", "MOD_OP", "PLUS", "MINUS", "CONTEXT_VARIABLE", "CURRENT_VARIABLE", 
			"AND_OP", "OR_OP", "NOT_OP", "IN_OP", "EQUALITY_OP", "INEQUALITY_OP", 
			"LT_OP", "GT_OP", "LTE_OP", "GTE_OP", "TRUE", "FALSE", "DECIMAL_VALUE", 
			"INTEGER_VALUE", "STRING", "IDENTIFIER", "DIGIT", "LETTER", "UNDERSCORE", 
			"WS", "ANY"
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


	public VirtualFieldPathLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "VirtualFieldPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2&\u00e6\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3"+
		"\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3"+
		"\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\3 \6 \u00a8\n \r \16 \u00a9\3 \3 \7 \u00ae\n "+
		"\f \16 \u00b1\13 \3 \3 \6 \u00b5\n \r \16 \u00b6\5 \u00b9\n \3!\6!\u00bc"+
		"\n!\r!\16!\u00bd\3\"\3\"\3\"\3\"\7\"\u00c4\n\"\f\"\16\"\u00c7\13\"\3\""+
		"\3\"\3#\3#\5#\u00cd\n#\3#\3#\3#\7#\u00d2\n#\f#\16#\u00d5\13#\3$\3$\3%"+
		"\5%\u00da\n%\3&\3&\3\'\6\'\u00df\n\'\r\'\16\'\u00e0\3\'\3\'\3(\3(\2\2"+
		")\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"+
		"= ?!A\"C#E$G\2I\2K\2M%O&\3\2\6\3\2))\3\2\62;\4\2C\\c|\5\2\13\f\17\17\""+
		"\"\2\u00ee\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2M\3\2\2\2\2O\3\2\2\2\3Q\3\2\2\2\5^\3\2\2\2\7`\3\2\2\2\tb\3\2\2\2\13"+
		"d\3\2\2\2\rf\3\2\2\2\17h\3\2\2\2\21j\3\2\2\2\23l\3\2\2\2\25n\3\2\2\2\27"+
		"p\3\2\2\2\31r\3\2\2\2\33t\3\2\2\2\35v\3\2\2\2\37x\3\2\2\2!z\3\2\2\2#|"+
		"\3\2\2\2%~\3\2\2\2\'\u0080\3\2\2\2)\u0083\3\2\2\2+\u0086\3\2\2\2-\u0088"+
		"\3\2\2\2/\u008b\3\2\2\2\61\u008e\3\2\2\2\63\u0091\3\2\2\2\65\u0093\3\2"+
		"\2\2\67\u0095\3\2\2\29\u0098\3\2\2\2;\u009b\3\2\2\2=\u00a0\3\2\2\2?\u00b8"+
		"\3\2\2\2A\u00bb\3\2\2\2C\u00bf\3\2\2\2E\u00cc\3\2\2\2G\u00d6\3\2\2\2I"+
		"\u00d9\3\2\2\2K\u00db\3\2\2\2M\u00de\3\2\2\2O\u00e4\3\2\2\2QR\7o\2\2R"+
		"S\7q\2\2ST\7w\2\2TU\7p\2\2UV\7v\2\2VW\7a\2\2WX\7r\2\2XY\7q\2\2YZ\7k\2"+
		"\2Z[\7p\2\2[\\\7v\2\2\\]\7?\2\2]\4\3\2\2\2^_\7.\2\2_\6\3\2\2\2`a\7*\2"+
		"\2a\b\3\2\2\2bc\7+\2\2c\n\3\2\2\2de\7]\2\2e\f\3\2\2\2fg\7_\2\2g\16\3\2"+
		"\2\2hi\7}\2\2i\20\3\2\2\2jk\7\177\2\2k\22\3\2\2\2lm\7A\2\2m\24\3\2\2\2"+
		"no\7\60\2\2o\26\3\2\2\2pq\7<\2\2q\30\3\2\2\2rs\7,\2\2s\32\3\2\2\2tu\7"+
		"\61\2\2u\34\3\2\2\2vw\7\'\2\2w\36\3\2\2\2xy\7-\2\2y \3\2\2\2z{\7/\2\2"+
		"{\"\3\2\2\2|}\7&\2\2}$\3\2\2\2~\177\7B\2\2\177&\3\2\2\2\u0080\u0081\7"+
		"(\2\2\u0081\u0082\7(\2\2\u0082(\3\2\2\2\u0083\u0084\7~\2\2\u0084\u0085"+
		"\7~\2\2\u0085*\3\2\2\2\u0086\u0087\7#\2\2\u0087,\3\2\2\2\u0088\u0089\7"+
		"K\2\2\u0089\u008a\7P\2\2\u008a.\3\2\2\2\u008b\u008c\7?\2\2\u008c\u008d"+
		"\7?\2\2\u008d\60\3\2\2\2\u008e\u008f\7#\2\2\u008f\u0090\7?\2\2\u0090\62"+
		"\3\2\2\2\u0091\u0092\7>\2\2\u0092\64\3\2\2\2\u0093\u0094\7@\2\2\u0094"+
		"\66\3\2\2\2\u0095\u0096\7>\2\2\u0096\u0097\7?\2\2\u00978\3\2\2\2\u0098"+
		"\u0099\7@\2\2\u0099\u009a\7?\2\2\u009a:\3\2\2\2\u009b\u009c\7v\2\2\u009c"+
		"\u009d\7t\2\2\u009d\u009e\7w\2\2\u009e\u009f\7g\2\2\u009f<\3\2\2\2\u00a0"+
		"\u00a1\7h\2\2\u00a1\u00a2\7c\2\2\u00a2\u00a3\7n\2\2\u00a3\u00a4\7u\2\2"+
		"\u00a4\u00a5\7g\2\2\u00a5>\3\2\2\2\u00a6\u00a8\5G$\2\u00a7\u00a6\3\2\2"+
		"\2\u00a8\u00a9\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab"+
		"\3\2\2\2\u00ab\u00af\7\60\2\2\u00ac\u00ae\5G$\2\u00ad\u00ac\3\2\2\2\u00ae"+
		"\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b9\3\2"+
		"\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b4\7\60\2\2\u00b3\u00b5\5G$\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2"+
		"\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00a7\3\2\2\2\u00b8\u00b2\3\2\2\2\u00b9"+
		"@\3\2\2\2\u00ba\u00bc\5G$\2\u00bb\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd"+
		"\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00beB\3\2\2\2\u00bf\u00c5\7)\2\2\u00c0"+
		"\u00c4\n\2\2\2\u00c1\u00c2\7)\2\2\u00c2\u00c4\7)\2\2\u00c3\u00c0\3\2\2"+
		"\2\u00c3\u00c1\3\2\2\2\u00c4\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6"+
		"\3\2\2\2\u00c6\u00c8\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00c9\7)\2\2\u00c9"+
		"D\3\2\2\2\u00ca\u00cd\5I%\2\u00cb\u00cd\5K&\2\u00cc\u00ca\3\2\2\2\u00cc"+
		"\u00cb\3\2\2\2\u00cd\u00d3\3\2\2\2\u00ce\u00d2\5I%\2\u00cf\u00d2\5G$\2"+
		"\u00d0\u00d2\5K&\2\u00d1\u00ce\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d0"+
		"\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4"+
		"F\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6\u00d7\t\3\2\2\u00d7H\3\2\2\2\u00d8"+
		"\u00da\t\4\2\2\u00d9\u00d8\3\2\2\2\u00daJ\3\2\2\2\u00db\u00dc\7a\2\2\u00dc"+
		"L\3\2\2\2\u00dd\u00df\t\5\2\2\u00de\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2"+
		"\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e3"+
		"\b\'\2\2\u00e3N\3\2\2\2\u00e4\u00e5\13\2\2\2\u00e5P\3\2\2\2\17\2\u00a9"+
		"\u00af\u00b6\u00b8\u00bd\u00c3\u00c5\u00cc\u00d1\u00d3\u00d9\u00e0\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}