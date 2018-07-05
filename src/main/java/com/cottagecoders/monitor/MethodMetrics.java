package com.cottagecoders.monitor;

public class MethodMetrics implements Comparable<MethodMetrics> {
  private String methodName;
  private String attributes;
  private Nanos elapsedNanos;
  private long numHits;

  public MethodMetrics(String name, long elapsed) {
    this.methodName = name;
    elapsedNanos = new Nanos(elapsed);
    numHits = 0;
  }

  private MethodMetrics() {
    // can't instantiate this
  }

  public String toString() {
    return "MethodMetrics " + methodName + " elapsedNanos " + elapsedNanos.getNanos() + " numHits " + numHits;
  }

  @Override
  public int compareTo(MethodMetrics other) {
    if (this.methodName.equals(other.methodName)) {
      return 0;
    } else if (this.methodName.compareTo(other.methodName) > 0) {
      return 1;
    } else {
      return -1;
    }
  }

  public String getMethodName() {
    return methodName;
  }

  public String getAttributes() {
    return attributes;
  }

  public Nanos getElapsedNanos() {
    return elapsedNanos;
  }

  public long getNumHits() {
    return numHits;
  }

  public void incrementNumHits() {
    numHits++;
  }

  public void addElapsedNanos(long elapsed) {
    elapsedNanos.addNanos(elapsed);
  }
}
