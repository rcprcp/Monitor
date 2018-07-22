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

public class ClassCounter {

  static MBeanServer server;
  static MBeanInfo info;
  static ObjectName objName;
  static MBeanAttributeInfo[] arr;

  private long totalLoadedClassCount;
  private int loadedClassCount;
  private long unloadedClassCount;

  ClassCounter() {
    server = ManagementFactory.getPlatformMBeanServer();
    try {
      objName = new ObjectName("java.lang:type=ClassLoading");
    } catch (MalformedObjectNameException ex) {
      ex.printStackTrace();
    }
    //   server.queryNames(objName, null)
    try {
      info = server.getMBeanInfo(objName);
      arr = info.getAttributes();
    } catch (InstanceNotFoundException | IntrospectionException | ReflectionException ex) {
      ex.printStackTrace();
    }
  }

  public void updateClassCounts() {

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
      //TODO: remove debugging code:
      System.out.println("updateClassCounts(): " + this.toString());

    } catch (MBeanException | InstanceNotFoundException | ReflectionException | AttributeNotFoundException ex) {
      ex.printStackTrace();
    }
  }

  public long getTotalLoadedClassCount() {
    return totalLoadedClassCount;
  }

  public int getLoadedClassCount() {
    return loadedClassCount;
  }

  public long getUnloadedClassCount() {
    return unloadedClassCount;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(" TotalLoadedClassCount: ");
    sb.append(totalLoadedClassCount);
    sb.append(" LoadedClassCount: ");
    sb.append(loadedClassCount);
    sb.append(" UnloadedClassCount: ");
    sb.append(unloadedClassCount);
    return sb.toString();
  }

}
