package com.linkedin.dataguard.runtime.fieldpaths.tms;

public interface TMSParser {
  ParsedTMSPath parsePath(String tmsPath) throws TMSParsingException;
}