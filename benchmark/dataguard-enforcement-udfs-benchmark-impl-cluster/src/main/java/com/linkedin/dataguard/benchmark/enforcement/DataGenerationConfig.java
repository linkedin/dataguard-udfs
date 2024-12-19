package com.linkedin.dataguard.benchmark.enforcement;

/**
 * A wrapper class to store configurations needed for data generation
 */
public class DataGenerationConfig {

  private final long numRecords;
  private final int arrayLength;
  private final int mapLength;
  private final int numOutputFiles;

  public DataGenerationConfig(long numRecords, int arrayLength, int mapLength, int numOutputFiles) {
    this.numRecords = numRecords;
    this.arrayLength = arrayLength;
    this.mapLength = mapLength;
    this.numOutputFiles = numOutputFiles;
  }

  public long getNumRecords() {
    return numRecords;
  }

  public int getArrayLength() {
    return arrayLength;
  }

  public int getMapLength() {
    return mapLength;
  }

  public int getNumOutputFiles() {
    return numOutputFiles;
  }
}
