grammar VirtualFieldPath;

// Package the generated classes appropriately
@header {
    package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;
}

// labels starting with "#" are added so that they can be handled by visitors
// using specific visitor methods with appropriate context

// This is the expected syntax for the path recieved from Datahub in resolvedCompliance API
// response. This will be derived by concatenating mount point and secondary schema path.
mountedPath: mountPoint rowSelectorAwareFieldPath EOF;

// The secondary schema field path can optionally have a row selector.
rowSelectorAwareFieldPath: rowSelector? fieldReferenceExpr EOF;

// Smallest self-sufficient field representation.
virtualFieldPath: fieldReferenceExpr EOF;

// Here, rowSelector and virtualFieldPath are both optional.
mountPoint
    : LEFT_BRACKET 'mount_point=' LEFT_PAREN rowSelector? fieldReferenceExpr? RIGHT_PAREN RIGHT_BRACKET;

// Special collection-predicate allowed without a fieldReferenceExpr in the front of it.
// Represents filtering of the dataset which can be thought of as an array of records.
rowSelector: collectionPredicate;

fieldReferenceExpr
    : variable                                                                        #fieldRefExprDefault
    | fieldReferenceExpr DOT identifier                                               #memberRef
    // Array/Map Operations
    | fieldReferenceExpr LEFT_BRACKET (literal | functionCall) RIGHT_BRACKET          #lookupRef
    | fieldReferenceExpr LEFT_BRACKET COLON RIGHT_BRACKET                             #streamRef
    | fieldReferenceExpr collectionPredicate                                          #filterRef
    ;

collectionPredicate
    : LEFT_BRACKET QUESTION_MARK LEFT_PAREN predicate RIGHT_PAREN RIGHT_BRACKET;

predicate
    : booleanLiteral                                                              #predicateTrivial
    | leftExpr=expr comparisonOperator rightExpr=expr                             #predicateBase
    | predicateWithParenthesis                                                    #predicateParen
    | NOT_OP predicateWithParenthesis                                             #notPredicate
    | leftPredicate=predicate AND_OP rightPredicate=predicate                     #andPredicate
    | leftPredicate=predicate OR_OP rightPredicate=predicate                      #orPredicate
    | leftExpr=expr IN_OP '[' literal (',' literal)* ']'                          #inPredicate
    ;

predicateWithParenthesis: LEFT_PAREN predicate RIGHT_PAREN;

expr
    : literal                                                               #literalExpr
    | fieldReferenceExpr                                                    #refExpr
    | LEFT_PAREN expr RIGHT_PAREN                                           #parenthesizedExpr
    | sign expr                                                             #signedExpr
    // Similar to java, keep */% at same precedence which is higher than binary +-
    // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html
    | left=expr operator=multiplicativeOperator right=expr                  #multiplicativeExpr
    | left=expr operator=additiveOperator right=expr                        #additiveExpr
    | functionCall                                                          #functionCallExpr
    ;

literal
    : numericLiteral
    | stringLiteral
    | booleanLiteral
    // Not supporting nulls for now. Should we?
    //  | nullLiteral
    ;

functionCall
    : identifier '(' ')'
    | identifier '(' expr (',' expr)* ')';

numericLiteral
    : MINUS? DECIMAL_VALUE  #decimalLiteral
    | MINUS? INTEGER_VALUE  #integerLiteral
    ;

sign: PLUS | MINUS;

stringLiteral: STRING;

//nullLiteral: NULL;

booleanLiteral: TRUE | FALSE;

variable
    : CONTEXT_VARIABLE               #contextVariable
    | CURRENT_VARIABLE               #predicateCurrentVariable
    ;

comparisonOperator: EQUALITY_OP | INEQUALITY_OP | LT_OP | GT_OP | LTE_OP | GTE_OP;

multiplicativeOperator: ASTERISK | DIVISION_OP | MOD_OP;

additiveOperator: PLUS | MINUS;

identifier
    : IDENTIFIER
    ;

LEFT_PAREN: '(';
RIGHT_PAREN: ')';

LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';

LEFT_CURLY: '{';
RIGHT_CURLY: '}';

QUESTION_MARK: '?';
DOT: '.';
COLON: ':';

// Arithmetic Operators
ASTERISK: '*';
DIVISION_OP: '/';
MOD_OP: '%';
PLUS: '+';
MINUS: '-';

// Variables
CONTEXT_VARIABLE: '$';
CURRENT_VARIABLE: '@';

// Logical Operators
AND_OP: '&&';
OR_OP: '||';
NOT_OP: '!';
IN_OP: 'IN';

// Comparison Operators. Not supporting <>, but != is supported
EQUALITY_OP: '==';
INEQUALITY_OP: '!=';
LT_OP: '<';
GT_OP: '>';
LTE_OP: '<=';
GTE_OP: '>=';

TRUE: 'true';
FALSE: 'false';

// TODO do we need scientific notation?
DECIMAL_VALUE
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

INTEGER_VALUE: DIGIT+;

// TODO test this string regex properly. We may need some handling in the syntax tree builder.
STRING: '\'' ( ~'\'' | '\'\'' )* '\'';

// TODO is this sufficient for dataset field names? Or they can have other stuff?
// Need to support quoted identifiers?
IDENTIFIER: (LETTER | UNDERSCORE) (LETTER | DIGIT | UNDERSCORE)*;

fragment DIGIT: [0-9];
fragment LETTER: [a-z] | [A-Z];
fragment UNDERSCORE: '_';

WS: [ \r\n\t]+ -> skip;

ANY: . ;