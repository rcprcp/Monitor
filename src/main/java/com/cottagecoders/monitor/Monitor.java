package com.cottagecoders.monitor;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Monitor {
  static final String MONITOR_PROPERTIES = "MONITOR_PROPERTIES";

  private static final Logger LOG = LoggerFactory.getLogger(Monitor.class);
  private static final String ERROR_1 = "Can't load config file '{}'  exception {}";
  public static Config conf;

  private static Instrumentation instrumentation;

  /**
   * @param args any args after the javaagent jar and the =
   * @param inst
   */
  public static void premain(String args, Instrumentation inst) {
    String fileName = System.getenv(MONITOR_PROPERTIES);
    if(StringUtils.isEmpty(fileName)) {
      // important enough to go to both console and log.
      System.out.println("invalid value set for " + MONITOR_PROPERTIES);
      LOG.error("invalid value set for  {} ", MONITOR_PROPERTIES);
      System.exit(27);
    }
      try {
        conf = new Config(fileName);
        System.out.println(conf.toString());
      } catch (IOException ex) {
        LOG.error(ERROR_1, System.getenv("MONITOR_PROPERTIES"), ex.getMessage());
      }
      Transformer transformer = new Transformer();
    transformer.init();

    inst.addTransformer(transformer);
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