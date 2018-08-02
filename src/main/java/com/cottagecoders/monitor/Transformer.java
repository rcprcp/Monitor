package com.cottagecoders.monitor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


//this class will be registered with instrumentation agent
final class Transformer implements ClassFileTransformer {
  //  static final Logger LOG = LoggerFactory.getLogger(Transformer.class);

  private static final String[] classesToInstrument = Monitor.conf.getAsArray("includeList");

  private static List<Pattern> patterns = new ArrayList<>();

  /**
   * initialize the transformer code, particularly the regex processing.
   */
  public void init() {
    // nothing specified in include list.
    if (classesToInstrument.length == 0) {
      System.out.println("No Classes specified in the config file");
    }

    for (String cl : classesToInstrument) {
      Pattern pattern = Pattern.compile(cl);
      patterns.add(pattern);
    }
  }

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
    byte[] byteCode = Arrays.copyOf(classfileBuffer, classfileBuffer.length);


    // check if this is a class we should instrument...
    for (Pattern p : patterns) {
      if (p.matcher(className).matches()) {

        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass;
        try {
          classPool.insertClassPath(new LoaderClassPath(loader));
          ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
        } catch (IOException ex) {
          System.out.println("Exception " + ex.getMessage());
          ex.printStackTrace();
          return classfileBuffer;
        }

        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {

          // TODO: is there a problem with Abstract Classes? Enums?
          if (Modifier.isAbstract(method.getModifiers()) | Modifier.isEnum(method.getModifiers()) | Modifier.isInterface(
              method.getModifiers())) {
            return classfileBuffer;
          }

          try {
            method.addLocalVariable("cottagecoders_monitor_start", CtClass.longType);
            String code = "{";
            if (Monitor.conf.getAsBoolean(Monitor.WHEREAMI)) {
              code += whereAmI(method.getLongName());
            }
            code += " cottagecoders_monitor_start = System.nanoTime(); }";
            method.insertBefore(code);

            code = after(method.getLongName());
            method.insertAfter(code);

            // initialize it and add to the data store.
            MetricPool.add(method.getLongName(), 0L);

          } catch (CannotCompileException ex) {
            System.out.println("Exception " + ex.getMessage());
            ex.printStackTrace();
            return classfileBuffer;
          }
        }

        try {
          byteCode = ctClass.toBytecode();
        } catch (IOException | CannotCompileException ex) {
          System.out.println("Exception " + ex.getMessage());
          ex.printStackTrace();
          return classfileBuffer;   // byteCode must be mangled.
        }
        ctClass.detach();
        // classname matched - don't check any more.
        break;
      }
    }
    return byteCode;
  }

  String whereAmI(String name) {
    return "System.out.println(\"whereAmI?  got here: " + name + "\");";
  }

  String after(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    sb.append("com.cottagecoders.monitor.MetricPool.add(\"");
    sb.append(name);
    sb.append("\", System.nanoTime() - cottagecoders_monitor_start); }");
    return sb.toString();
  }

  String OLDafter(String name) {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    sb.append("System.nanoTime() - cottagecoders_monitor_start; }");
    return sb.toString();
  }
}
