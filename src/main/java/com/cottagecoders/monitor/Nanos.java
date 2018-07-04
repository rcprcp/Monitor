package com.cottagecoders.monitor;

public class Nanos {
  long time;

  private Nanos() {
    //can't instantiate this.
  }

  public Nanos(long time) {
    this.time = time;
  }
}