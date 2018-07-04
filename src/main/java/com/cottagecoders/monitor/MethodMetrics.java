package com.cottagecoders.monitor;

public class MethodMetrics implements Comparable<MethodMetrics> {
  private String methodName;
  private String attributes;
  private Nanos createTime;
  private long numHits;

  public MethodMetrics(String name) {
    System.out.println("MethodMetrics: " + name);
    this.methodName = name;
    createTime = new Nanos(System.nanoTime());
    numHits = 1;
  }

  private MethodMetrics() {
    // can't instantiate this
  }

  public String toString() {
    return "MethodMetrics " + methodName + " createTime " + createTime.getNanos() + " numHits " + numHits;
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

  public Nanos getCreateTime() {
    return createTime;
  }

  public long getNumHits() {
    return numHits;
  }

  public void incrementNumHits() {
    numHits++;
  }
}
