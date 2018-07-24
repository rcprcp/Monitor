package com.cottagecoders.monitor;

public class Counts {
  private long totalLoadedClassCount;
  private int loadedClassCount;
  private long unloadedClassCount;

  Counts(long totalLoadedClassCount, int loadedClassCount, long unloadedClassCount) {
    this.totalLoadedClassCount = totalLoadedClassCount;
    this.loadedClassCount = loadedClassCount;
    this.unloadedClassCount = unloadedClassCount;
  }

  public String htmlTdElements() {
    StringBuilder sb = new StringBuilder();

    sb.append("<td>");
    sb.append(totalLoadedClassCount);
    sb.append("</td>");

    sb.append("<td>");
    sb.append(loadedClassCount);
    sb.append("</td>");

    sb.append("<td>");
    sb.append(unloadedClassCount);
    sb.append("</td>");

    return sb.toString();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(" TotalLoadedClassCount: ");
    sb.append(totalLoadedClassCount);
    sb.append(" LoadedClassCount: ");
    sb.append(loadedClassCount);
    sb.append(" UnloadedClassCount: ");
    sb.append(unloadedClassCount);
    return sb.toString();
  }

}
