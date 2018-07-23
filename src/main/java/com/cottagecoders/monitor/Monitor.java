package com.cottagecoders.monitor;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Monitor {
  static final String MONITOR_PROPERTIES = "MONITOR_PROPERTIES";
  static final String WHEREAMI = "whereAmI";
  static final String INCLUDE_LIST = "includeList";
  static final String HTTP_PORT = "httpPort";
  static final int DEFAULT_PORT = 1128;
  public static Config conf;

  private static Instrumentation instrumentation;

  /**
   * @param args any args after the javaagent jar and the =
   * @param inst
   */
  public static void premain(String args, Instrumentation inst) {
    String fileName = System.getenv(MONITOR_PROPERTIES);
    if (StringUtils.isEmpty(fileName)) {
      // important enough to go to both console and log.
      System.out.println("invalid value set for " + MONITOR_PROPERTIES);
      System.exit(27);
    }
    try {
      conf = new Config(fileName);
    } catch (IOException ex) {
      System.out.println("Can't load config file " + System.getenv("MONITOR_PROPERTIES") + " message " + ex
          .getMessage());
    }

    Transformer transformer = new Transformer();
    transformer.init();

    inst.addTransformer(transformer);

    // find port; start a thread for NanoHttpd server
    int port = Monitor.conf.getAsInt(Monitor.HTTP_PORT);
    final int LOW_PORT_NUM = 1024;
    final int HIGH_PORT_NUM = 65536;
    if (port <= LOW_PORT_NUM || port >= HIGH_PORT_NUM) {
      System.out.println("Invalid port number: " + port + " using default " + DEFAULT_PORT);
      port = DEFAULT_PORT;
    }
    (new Thread(new HttpServer(port))).start();

  }

  /**
   * @param args
   * @param inst
   */
  public static void agentmain(String args, Instrumentation inst) {
    System.out.println("agentmain(): got here");  // TODO: delete me.
    instrumentation = inst;
    instrumentation.addTransformer(new Transformer());
  }

  /**
   * Programmatic hook to dynamically load javaagent at runtime.
   * Various examples have it, it is not implemented yet.
   */
  public static void initialize() {
    System.out.println("initialize(): got here - now what?");    // TODO: delete me.
    if (instrumentation == null) {
      // TODO: Enhance me.
      System.out.println("instrumentation is null, dynamic agent loading is not supported.  :( ");
    }
  }

  /**
   * This main() is used for testing and debugging.  Perhaps we'll delete it someday?
   *
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("oops.  in dummy main class");
  }

}