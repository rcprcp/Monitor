package com.cottagecoders.monitor;

public class MethodName {
  String methodName;
  private MethodName() { 
    // can't instantiate this
  }
  
  public MethodName(String methodName) {
    this.methodName = methodName;
  }
}
