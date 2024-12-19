package com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics;

public class SemanticException extends RuntimeException {

  public SemanticException(String message) {
    super(message);
  }

  public SemanticException(String message, Throwable cause) {
    super(message, cause);
  }
}
