package com.cottagecoders.monitor;

class Nanos {
  long time;

  private Nanos() {
    //can't instantiate this.
  }

  Nanos(long time) {
    this.time = time;
  }

  long getNanos() {
    return time;
  }
}