grammar TMSPath;

// Package the generated classes appropriately
@header {
  package com.linkedin.dataguard.runtime.fieldpaths.tms;
}

tmsPath : fieldPath EOF;

fieldPath
  : selector                      #baseRef
  // accessors for struct field, array element, and map key/value are preceded with "."
  | fieldPath DOT selector        #selectorRef
  // union type selector applies without "."
  | fieldPath typeSelector        #unionSelectorRef
  // The base of the field path can not be a union type selector
  ;

selector
  : typeSelector
  | fieldSelector
  | keySelector
  | valueSelector
  ;

fieldSelector: fieldName;
typeSelector: '[type=' typeName ']';
keySelector: '[key=' typeName ']';
valueSelector: '[value=' typeName ']';

// The reason for allowing the special characters is documented here.:
// https://docs.google.com/document/d/1JQPhuU5uaYkVgqO-c2ZX5GCNo6A6Rhknz9K-gTWnKDc/edit
// We will evolve this (make stricter or looser) as we go.
typeName
  : (LETTER | DIGIT | DOT | '_' | '<' | '>' | '-' | '$' | ':' | ',' | ' ')+
  ;

fieldName
  : (LETTER | DIGIT | '_' | '@' | '$' | '#' | '|' | ':' | ' ' | '-')+;

LETTER
  : [A-Za-z];

DIGIT
  : [0-9];

DOT: '.';
