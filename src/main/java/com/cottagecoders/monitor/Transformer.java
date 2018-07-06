package com.cottagecoders.monitor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;


//this class will be registered with instrumentation agent
public class Transformer implements ClassFileTransformer {
  static final Logger logger = LoggerFactory.getLogger(Transformer.class);

  /**
   * @param loader
   * @param className
   * @param classBeingRedefined
   * @param protectionDomain
   * @param classfileBuffer
   * @return Original byte code - or the modified byte code
   */
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer
  ) {
    byte[] byteCode = classfileBuffer;

    try {
      ClassPool classPool = ClassPool.getDefault();
      classPool.insertClassPath(new LoaderClassPath(loader));

      CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
      CtMethod[] methods = ctClass.getDeclaredMethods();
      for (CtMethod method : methods) {
        try {

          // TODO: is there a problem with Abstract Classes?
          if(Modifier.isAbstract(method.getModifiers())) {
            System.out.println("abstract class " + method.getLongName());
            return byteCode;
          }

          // TODO: include/exclude methods here?
          if(!method.getLongName().toLowerCase().contains("com.cottagecoders.victim")) {
            continue;
          }

          method.addLocalVariable("cottagecoders_monitor_start", CtClass.longType);
          String code = "{";
          if (Monitor.conf.getPropertyBoolean("whereAmI")) {
            code += whereAmI(method.getLongName());
          }
          code += " cottagecoders_monitor_start = System.nanoTime(); }" ;
          method.insertBefore(code);

          code = after(method.getLongName());
          method.insertAfter(code);

          // initialize it and add to the data store (a Map, for now).
          MetricPool.instance().add(method.getLongName(), 0L);

        } catch (CannotCompileException ex) {
          System.out.println("Exception: " + ex.getReason());
          ex.printStackTrace();
        }
      }

      byteCode = ctClass.toBytecode();
      ctClass.detach();

    } catch (Throwable ex) {
      System.out.println("Exception: " + ex);
      ex.printStackTrace();
    }

    return byteCode;
  }

  String whereAmI(String name) {
    return "System.out.println(\"whereAmI?  got here: " + name + "\");";
  }

  String after(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ") ;
    sb.append("com.cottagecoders.monitor.MetricPool.instance().add(\"");
    sb.append(name);
    sb.append("\", System.nanoTime() - cottagecoders_monitor_start); }");
    return sb.toString();
  }
}
