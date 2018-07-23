package com.cottagecoders.monitor;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
  Properties props;

  public Config(String fileName) throws IllegalArgumentException, IOException {

    if (StringUtils.isEmpty(fileName)) {
      System.out.println("failed to get value of configuration fileName");
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
    if (!StringUtils.isEmpty(props.getProperty(propName))) {
      try {
        num = Integer.parseInt(props.getProperty(propName));
      } catch (NumberFormatException ex) {
        System.out.print("Config: invalid value for property " + propName + " value " + props.getProperty(propName));
      }
    }
    return num;
  }

  public String[] getAsArray(String propName) {
    if (StringUtils.isEmpty(props.getProperty(propName))) {
      return new String[0];
    }
    String[] parts = props.getProperty(propName).split(",");
    return parts;
  }

  public boolean getAsBoolean(String propName) {
    if (getString(propName).equalsIgnoreCase("true")) {
      return true;
    }
    return false;
  }

  public String toString() {
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

  public String toHtml() {
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
