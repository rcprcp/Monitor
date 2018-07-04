package com.cottagecoders.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimerPool {
  private static Map<MethodName, Duration> timers = new ConcurrentHashMap<>();

  private TimerPool() {
    // cant' instantiate.
  }

  public static Map<MethodName, Duration> instance() {
    return timers;
  }

  public static void start(String name) {
    Duration dur = new Duration(new Nanos(System.nanoTime()));
    timers.put(new MethodName(name), dur);

  }

  /**
   * Returns difference in nanos between now and the saved timer.
   *
   * @param name
   * @return difference in nanos
   */
  public static long duration(String name) {
    if (timers.containsKey(name)) {
      return System.nanoTime() - (Long) timers.get(name).start.time;
    } else {
      return 0;
    }
  }
}
