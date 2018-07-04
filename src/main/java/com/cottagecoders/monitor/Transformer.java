package com.cottagecoders.monitor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;


//this class will be registered with instrumentation agent
public class Transformer implements ClassFileTransformer {
  //  static final Logger logger = LoggerFactory.getLogger(Transformer.class);

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
      classPool.insertClassPath(className);

      classPool.insertClassPath(new LoaderClassPath(loader));

      CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
      CtMethod[] methods = ctClass.getDeclaredMethods();
      for (CtMethod method : methods) {
        try {
          method.addLocalVariable("startTime", CtClass.longType);
          String code;
          if (Monitor.conf.getPropertyBoolean("whereAmI")) {
            code = whereAmI(method.getLongName());
            method.insertBefore(code);
          }

          if (method.getName().toLowerCase().contains("main") || method.getName()
              .equals("java.lang.String") || method.getName().equals("run1") || method.getName().equals("run2")) {
            //            String code = before(method.getName());
            //            method.insertBefore(code);

            //            code = after(method.getName());
            //            method.insertAfter(code);
          }

        } catch (CannotCompileException ex) {
          System.out.println("Exception " + ex.getReason() + "  " + ex.getStackTrace());
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

  String before(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("{com.cottagecoders.monitor.TimerPool.start(\"");
    sb.append(name);
    sb.append("\");}");
    return sb.toString();
  }

  String whereAmI(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("{System.out.println(\"bobp: got here: ");
    sb.append(name);
    sb.append("\");}");
    return sb.toString();
  }

  String after(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("{com.cottagecoders.monitor.TimerPool.duration(\"");
    sb.append(name);
    sb.append("\");}");
    return sb.toString();
  }
}
