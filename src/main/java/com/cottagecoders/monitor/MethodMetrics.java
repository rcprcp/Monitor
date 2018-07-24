package com.cottagecoders.monitor;

import java.util.UUID;

public class MethodMetrics implements Comparable<MethodMetrics> {
  private String methodName;
  private String attributes;
  private Nanos elapsedNanos;
  private long numHits;
  private UUID uuid;
  private Counts counts;

  public MethodMetrics(String name, long elapsed, Counts c) {
    this.methodName = name;
    elapsedNanos = new Nanos(elapsed);
    numHits = 0;
    uuid = UUID.randomUUID();
    counts = c;
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
    sb.append(counts.toString());

    sb.append(" UUID: ");
    sb.append(uuid);

    return sb.toString();
  }

  String htmlTableRow() {
    StringBuilder sb = new StringBuilder() ;
    sb.append("<tr><td>");
    sb.append(methodName.replace('.',' '));
    sb.append("</td>");

    sb.append("<td>");
    sb.append(elapsedNanos.getNanos());
    sb.append("</td>");

    sb.append("<td>");
    sb.append(numHits);
    sb.append("</td>");

    sb.append(counts.htmlTdElements());

    sb.append("<td>");
    sb.append(uuid);
    sb.append("</td></tr>");

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

  public void setCounts(Counts c) {
    counts = c;
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
