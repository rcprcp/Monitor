package com.cottagecoders.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Monitor {
  static final Logger LOG = LoggerFactory.getLogger(Monitor.class);
  private static final String ERROR_1 = "Configuration file was not specified.";
  private static final String ERROR_2 = "`java -javaagent:Monitor.jar=<config file location> -jar <your jar file>'";
  private static final String ERROR_3 = "Can't load config file '{}'  exception {}";
  public static Config conf;

  private static Instrumentation instrumentation;

  /**
   * @param args
   * @param inst
   */
  public static void premain(String args, Instrumentation inst) {
    if (args.isEmpty()) {
      // these are important enough to send to the log file and the console.
      System.out.println(ERROR_1);
      LOG.error(ERROR_1);
      System.out.println(ERROR_2);
      LOG.error(ERROR_2);
      System.exit(2);

    } else {
      try {
        conf = new Config();
        System.out.println(conf.toString());
      } catch (IOException ex) {
        LOG.error(ERROR_3, args, ex.getMessage());
      }
    }

    inst.addTransformer(new Transformer());
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