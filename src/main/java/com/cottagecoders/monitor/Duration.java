package com.cottagecoders.monitor;

public class Duration {
  Nanos start;
  Nanos end;

  private Duration() {
    // cant instantiate.
  }

  public Duration(Nanos start) {
    this.start = start;
    this.end = start;
  }
}
