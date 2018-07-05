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
      debugPrint();
      MethodMetrics m = theMetrics.get(name);
      m.addElapsedNanos(elapsed);
      m.incrementNumHits();
      theMetrics.put(name, m);
    } else {
      MethodMetrics metric = new MethodMetrics(name, elapsed);
      theMetrics.put(name, metric);
    }
  }

  private void debugPrint() {
    for (Map.Entry<String, MethodMetrics> ent : theMetrics.entrySet()) {
      System.out.println("Method Metrics: " + ent.getKey() + " " + ent.getValue()
          .getMethodName() + " nanos: " + ent.getValue().getElapsedNanos().getNanos() + " hits: " + ent.getValue().getNumHits());
    }
  }
}
