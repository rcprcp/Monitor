package com.cottagecoders.monitor;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.lang.management.ManagementFactory;

final class ClassCounter {

  static MBeanServer server;
  static MBeanInfo info;
  static ObjectName objName;
  static MBeanAttributeInfo[] arr;

  ClassCounter() {
    server = ManagementFactory.getPlatformMBeanServer();
    try {
      objName = new ObjectName("java.lang:type=ClassLoading");
    } catch (MalformedObjectNameException ex) {
      ex.printStackTrace();
    }
    try {
      info = server.getMBeanInfo(objName);
      arr = info.getAttributes();
    } catch (InstanceNotFoundException | IntrospectionException | ReflectionException ex) {
      ex.printStackTrace();
    }
  }

  public Counts getClassCounts() {

    long totalLoadedClassCount = 0;
    int loadedClassCount = 0;
    long unloadedClassCount = 0;

    try {
      for (MBeanAttributeInfo a : arr) {
        if (a.isReadable()) {
          if (a.getName().equals("TotalLoadedClassCount")) {
            totalLoadedClassCount = (Long) server.getAttribute(objName, a.getName());
          } else if (a.getName().equals("LoadedClassCount")) {
            loadedClassCount = (Integer) server.getAttribute(objName, a.getName());
          } else if (a.getName().equals("UnloadedClassCount")) {
            unloadedClassCount = (Long) server.getAttribute(objName, a.getName());
          }
        }
      }
    } catch (MBeanException | InstanceNotFoundException | ReflectionException | AttributeNotFoundException ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
    }
    return new Counts(totalLoadedClassCount, loadedClassCount, unloadedClassCount);
  }


}
