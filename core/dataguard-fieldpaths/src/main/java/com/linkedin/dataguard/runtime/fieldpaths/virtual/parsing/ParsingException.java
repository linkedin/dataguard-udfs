package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;

public class ParsingException extends RuntimeException {

  public ParsingException(String message) {
    super(message);
  }

  public ParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}

