package com.linkedin.dataguard.runtime.fieldpaths.tms;

public class TMSParsingException extends Exception {

  public TMSParsingException(String message) {
    super(message);
  }

  public TMSParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
