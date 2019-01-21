package com.cottagecoders.monitor;

import java.util.concurrent.atomic.AtomicLong;

public class Metrics {

  private static AtomicLong absoluteSequence = new AtomicLong(0);

  /**
   * Don't need a constructor.
   */
  private Metrics() {
    // don't need a constructor.
  }

  /**
   * Publish one occurrence of a metric.
   * @param name
   * @param startingSequence
   * @param elapsed
   */
  static void sendMetrics(String name, long startingSequence, long elapsed) {
    System.out.println("sendMetrics: got here");
    Monitor.publisher.send("elapsed" + Monitor.DELIM + Monitor.PREFIX +
        Monitor.DELIM + name + Monitor.DELIM + elapsed + Monitor.DELIM + startingSequence + Monitor.DELIM + incrementSequence());
  }

  /**
   * increments and returns the global sequence number.  This should stay as <code>public</code>
   * since it is dynamically added to the routines we're instrumenting.
   * IllegalAccessError will occur, otherwise.
   * @return  newly incremented sequence number.
   */
  public static long incrementSequence() {
    return absoluteSequence.addAndGet(1L);
  }
}
