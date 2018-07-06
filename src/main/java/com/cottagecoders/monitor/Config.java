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

  Properties props;

  private static final String ERROR1 = "failed to get value of fileName";

  public Config(String fileName) throws IllegalArgumentException, IOException {

    if(StringUtils.isEmpty(fileName)) {
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

  public String getPropertyString(String propName) {
    if (StringUtils.isEmpty(props.getProperty(propName))) {
      return "";
    }
    return props.getProperty(propName);
  }

  public boolean getPropertyBoolean(String propName) {
    if (getPropertyString(propName).equalsIgnoreCase("true")) {
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
}
