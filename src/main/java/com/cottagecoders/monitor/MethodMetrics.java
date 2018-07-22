package com.cottagecoders.monitor;

import java.util.UUID;

public class MethodMetrics implements Comparable<MethodMetrics> {
  private String methodName;
  private String attributes;
  private Nanos elapsedNanos;
  private long numHits;
  private UUID uuid;
  private ClassCounter classCounter;

  public MethodMetrics(String name, long elapsed) {
    this.methodName = name;
    elapsedNanos = new Nanos(elapsed);
    numHits = 0;
    classCounter = new ClassCounter();
    classCounter.updateClassCounts();
    uuid = UUID.randomUUID();
  }

  private MethodMetrics() {
    // can't instantiate this
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("MethodMetrics: ");

    sb.append(" methodName: ");
    sb.append(methodName);

    sb.append(" elapsedNanos ");
    sb.append(elapsedNanos.getNanos());

    sb.append(" numHits: " );
    sb.append(numHits);

    sb.append(" classCounter Info: ");
    sb.append(classCounter.toString());

    sb.append(" UUID: ");
    sb.append(uuid);

    return sb.toString();
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

  public UUID getUUID() { return uuid;}

  public void incrementNumHits() {
    numHits++;
  }

  public void addElapsedNanos(long elapsed) {
    elapsedNanos.addNanos(elapsed);
  }
}
