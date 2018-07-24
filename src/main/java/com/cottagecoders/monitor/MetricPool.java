package com.cottagecoders.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MetricPool {
  private static MetricPool metricPool = new MetricPool();
  private static Map<String, MethodMetrics> theMetrics = new ConcurrentHashMap<>();

  private MetricPool() {
    // can't instantiate.
  }

//  public static MetricPool instance() {
//    return metricPool;
//  }

  public static void add(String name, long elapsed) {
    if (theMetrics.containsKey(name)) {
//      System.out.println("MetricPool(): duplicate  " + name + " pool size " + theMetrics.size());
      MethodMetrics m = theMetrics.get(name);
      m.addElapsedNanos(elapsed);
      m.incrementNumHits();
      theMetrics.put(name, m);
    } else {
      MethodMetrics metric = new MethodMetrics(name, elapsed, new ClassCounter().getClassCounts());
      theMetrics.put(name, metric);
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, MethodMetrics> entry : theMetrics.entrySet()) {
      sb.append(entry.getValue().toString());
      sb.append("\n");
    }
    return sb.toString();
  }

  public static String htmlTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("<table border=1 style={width: 100%;margin: 30px; border: 30px;}>");
    sb.append("<tr><th>Method Name</th><th>elapsed Nanos</th><th>Hits</th>");
    sb.append("<th>Total Classes</th><th>Loaded Classed</th><th>Unloaded Classes</th><th>UUID</th></tr>");

    for(Map.Entry<String, MethodMetrics> entry : theMetrics.entrySet()) {
      sb.append(entry.getValue().htmlTableRow());
    }

    sb.append("</table>");
    return sb.toString();
  }

  public static int size(){
    return theMetrics.size();
  }

}
