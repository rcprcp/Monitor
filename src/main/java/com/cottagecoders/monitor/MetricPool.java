package com.cottagecoders.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MetricPool {
  private static final Map<String, MethodMetrics> theMetrics = new ConcurrentHashMap<>(11000);

  private MetricPool() {
    // can't instantiate.
  }


  public static void add(String name, long elapsed) {
    if (theMetrics.containsKey(name)) {
      MethodMetrics m = theMetrics.get(name);
      m.addElapsedNanos(elapsed);
      m.incrementNumHits();
    } else {
      MethodMetrics metric = new MethodMetrics(name, elapsed, new ClassCounter().getClassCounts());
      theMetrics.put(name, metric);
    }
  }

  public static String htmlTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("<table border=1 style={width: 100%;margin: 30px; border: 30px;}>");
    sb.append("<tr><th>Method Name</th><th>elapsed Nanos</th><th>Hits</th>");
    sb.append("<th>Total Classes</th><th>Loaded Classed</th><th>Unloaded Classes</th><th>UUID</th></tr>");

    for (Map.Entry<String, MethodMetrics> entry : theMetrics.entrySet()) {
      sb.append(entry.getValue().htmlTableRow());
    }

    sb.append("</table>");
    return sb.toString();
  }

  public static int size() {
    return theMetrics.size();
  }

}
