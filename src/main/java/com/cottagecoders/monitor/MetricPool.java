package com.cottagecoders.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class MetricPool {
  private static MetricPool metricPool = new MetricPool();
  private Map<String, MethodMetrics> theMetrics = new ConcurrentHashMap<>();

  private MetricPool() {
    // can't instantiate.
  }

  static MetricPool instance() {
    return metricPool;
  }

  void add(final MethodMetrics metric) {
    if (theMetrics.containsKey(metric.getMethodName())) {
      System.out.println("MetricPool(): add got here for " + metric.getMethodName());
      System.out.println("MetricPool(): size " + theMetrics.size());
      for (Map.Entry<String, MethodMetrics> ent : theMetrics.entrySet()) {
        System.out.println("Method Metrics: " + ent.getKey() + " " + ent.getValue()
            .getMethodName() + "  " + ent.getValue().getCreateTime() + " " + ent.getValue().getNumHits());
      }
      MethodMetrics m = theMetrics.get(metric.getMethodName());
      m.incrementNumHits();
      theMetrics.put(metric.getMethodName(), m);
      metric.incrementNumHits();
    } else {
      theMetrics.put(metric.getMethodName(), metric);
    }
  }
}
