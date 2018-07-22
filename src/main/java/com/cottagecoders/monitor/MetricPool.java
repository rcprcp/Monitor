package com.cottagecoders.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MetricPool {
  private static MetricPool metricPool = new MetricPool();
  private Map<String, MethodMetrics> theMetrics = new ConcurrentHashMap<>();

  private MetricPool() {
    // can't instantiate.
  }

  public static MetricPool instance() {
    return metricPool;
  }

  public void add(String name, long elapsed) {
    if (theMetrics.containsKey(name)) {
      System.out.println("MetricPool(): adding " + name + " pool size " + theMetrics.size());
      MethodMetrics m = theMetrics.get(name);
      m.addElapsedNanos(elapsed);
      m.incrementNumHits();
      theMetrics.put(name, m);
    } else {
      MethodMetrics metric = new MethodMetrics(name, elapsed);
      //TODO: remove debug code.
      System.out.println(metric.toString());
      theMetrics.put(name, metric);
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, MethodMetrics> ent : theMetrics.entrySet()) {
      sb.append(ent.getValue().toString());
      sb.append("\n");
    }
    return sb.toString();
  }

  public static String dump() {
    return metricPool.toString();
  }

}
