package com.cottagecoders.monitor;

import java.util.concurrent.atomic.AtomicLong;

public class Metrics {

  public static AtomicLong absoluteSequence = new AtomicLong(0);

  private Metrics() {

  }

  public static void sendMetrics(String name, long startingSequence, long elapsed) {
    System.out.println("sendMetrics: got here");
    Monitor.publisher.send("elapsed" + Monitor.DELIM + Monitor.PREFIX +
        Monitor.DELIM + name + Monitor.DELIM + elapsed + Monitor.DELIM + startingSequence + Monitor.DELIM + incrementSequence());
  }

  public static long incrementSequence() {
    return absoluteSequence.addAndGet(1L);
  }
}
