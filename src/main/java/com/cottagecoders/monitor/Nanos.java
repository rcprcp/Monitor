package com.cottagecoders.monitor;

class Nanos {
  long time;

  private Nanos() {
    //can't instantiate this.
  }

  /**
   * wrapper class to reduce confusion between epoch, epochMS and relative time in nanos.
   */
  Nanos(long time) {
    this.time = time;
  }

  long getNanos() {
    return time;
  }

  void addNanos(long nanos) {
    time += nanos;
  }
}