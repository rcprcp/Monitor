package com.cottagecoders.monitor;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

final class Monitor {
  static final long START_TIME = System.currentTimeMillis();
  static String hostName;
  static final String DELIM = "\t";
  static Publisher publisher;
  static String PREFIX;

  static Configuration conf;
  private static Instrumentation instrumentation;

  /**
   * @param args -- there are no args - currently everything is in the config file
   * which is pointed to by the environment variable (MONITOR_PROPERTIES)
   * @param inst -- Instrumentation object.
   */
  public static void premain(String args, Instrumentation inst) {
System.out.println("PREMAIN GOT HERE");
    // get host name - if that fails, try for the ip address.
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      try {
        hostName = InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
        System.out.println("cannot get hostname or ipaddress.");
        System.exit(4);
      }
    }

    // check environment variable for name of the configuration file.
    String fileName = System.getenv(Configuration.MONITOR_PROPERTIES);
    if (StringUtils.isEmpty(fileName)) {
      // important enough to go to both console and log.
      System.out.println("invalid value set for " + Configuration.MONITOR_PROPERTIES);
      System.exit(27);
    }

    // read configuration file - create configuration object .
    try {
      conf = new Configuration(fileName);
    } catch (IOException ex) {
      System.out.println("Can't load config file " + System.getenv(Configuration.MONITOR_PROPERTIES) + " message " + ex.getMessage());
      System.exit(27);
    }

    // TODO: delete me.
    System.out.println("after config file " + conf.toString());
    // destination should not be blank - can use "localhost"
    if (StringUtils.isEmpty(conf.getAsString(Configuration.DBWRITER_HOSTNAME))) {
      System.out.println("Destination Hostname cannot be blank");
      System.exit(27);
    }

    // check if the port is in the non-privileged port range:
    if (conf.getAsInt(Configuration.DBWRITER_PORT) < 1025 || conf.getAsInt(Configuration.DBWRITER_PORT) > 65536) {
      System.out.println("Invalid destination port " + conf.getAsInt(Configuration.DBWRITER_PORT));
      System.exit(27);
    }

    // create publisher object to send to monitor_server.
    // under the hood this might be a socket or...
    // TODO: someday we'll need a queue.
    try {
      publisher = new Publisher(conf.getAsString(Configuration.DBWRITER_HOSTNAME),
          conf.getAsInt(Configuration.DBWRITER_PORT)
      );
    } catch (IOException ex) {
      System.out.println("cannot connect to remote server " + ex.getMessage());
      ex.printStackTrace();
      System.exit(27);
    }

    // create record prefix:
    PREFIX = Configuration.APPNAME + DELIM + START_TIME;

    // send appname + startime record.
    publisher.send("start" + DELIM + PREFIX + DELIM + hostName);

    // send the configuration information for this run.
    publisher.send("configfile" + DELIM + PREFIX + DELIM + Configuration.MONITOR_PROPERTIES + DELIM + System.getenv(
        Configuration.MONITOR_PROPERTIES));

    Set<String> names = conf.props.stringPropertyNames();
    for (String n : names) {
      publisher.send("configitem" + DELIM + PREFIX + DELIM + n + DELIM + conf.getAsString(n));
    }

    Transformer transformer = new Transformer();
    transformer.init();

    // register callback function
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