package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

/**
 * Every enforcer expression must start with some base element. In the context of this grammar, it will either
 * be
 * - the root on which the path is based on. Represented by "$" in the path.
 * - the current collection element when the expression is within a collection predicate. Represented by "@".
 */
public enum EnforcerBaseElement {
  // "@"
  PREDICATE_CURRENT_ELEMENT,
  // "$"
  ROOT_ELEMENT
}
