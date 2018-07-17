package com.cottagecoders.monitor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
  static final Logger LOG = LoggerFactory.getLogger(Config.class);
  private static final String[] emptyArr = new String[0];
  private static final String ERROR1 = "failed to get value of fileName";
  Properties props;

  public Config(String fileName) throws IllegalArgumentException, IOException {

    if (StringUtils.isEmpty(fileName)) {
      LOG.error(ERROR1);
      System.out.println(ERROR1);
      System.exit(1);
    }

    try (FileInputStream is = new FileInputStream(fileName)) {
      props = new Properties();
      if (is != null) {
        props.load(is);
      } else {
        throw new FileNotFoundException("missing config file " + fileName);
      }
    }
  }

  public String getString(String propName) {
    if (StringUtils.isEmpty(props.getProperty(propName))) {
      return "";
    }
    return props.getProperty(propName);
  }

  public int getAsInt(String propName) {
    int num = 0;
    if (StringUtils.isEmpty(props.getProperty(propName))) {
      try {
        num = Integer.parseInt(props.getProperty(propName));
      } catch (NumberFormatException ex) {
        // nothing.
      }
    }
    return num;
  }

    public String[] getAsArray (String propName){
      if (StringUtils.isEmpty(props.getProperty(propName))) {
        return emptyArr;
      }
      String[] parts = props.getProperty(propName).split(",");
      return parts;
    }

    public boolean getAsBoolean (String propName){
      if (getString(propName).equalsIgnoreCase("true")) {
        return true;
      }
      return false;
    }

    public String toString () {
      StringBuilder sb = new StringBuilder();
      for (String k : props.stringPropertyNames()) {
        sb.append("Config() key: ");
        sb.append(k);
        sb.append("  value: ");
        sb.append(props.getProperty(k));
        sb.append("\n");
      }
      return sb.toString();
    }

    public String toHtml () {
      StringBuilder sb = new StringBuilder();
      for (String k : props.stringPropertyNames()) {
        sb.append("<br>Config() key: ");
        sb.append(k);
        sb.append("  value: ");
        sb.append(props.getProperty(k));
      }
      return sb.toString();
    }

  }
