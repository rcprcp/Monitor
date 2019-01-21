package com.cottagecoders.monitor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

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
  // static final Logger LOG = LoggerFactory.getLogger(Transformer.class);

  private static final String[] classesToInstrument = Monitor.conf.getAsArray(Configuration.INCLUDE_LIST);
  private static final String[] classesToSkip = Monitor.conf.getAsArray(Configuration.EXCLUDE_LIST);

  private static List<Pattern> patterns = new ArrayList<>();
  private static List<Pattern> patternsToSkip = new ArrayList<>();

  public void init() {
    // nothing specified in include list.
    if (classesToInstrument.length == 0) {
      System.out.println("No Classes specified in the config file");
    }

    for (String cl : classesToInstrument) {
      Pattern pattern = Pattern.compile(cl);
      patterns.add(pattern);
    }

    for (String cl : classesToSkip) {
      Pattern pattern = Pattern.compile(cl);
      patternsToSkip.add(pattern);
    }
  }

  /**  Important to note:
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
       // check if this is a class we should skip.
    for (Pattern p : patternsToSkip) {
      if (p.matcher(className).matches()) {
        return classfileBuffer;
      }
    }

    // deep copy to return in case something goes wrong.
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
            method.addLocalVariable("cottagecoders_monitor_starting_sequence", CtClass.longType);
            String code = before(method.getLongName());
            method.insertBefore(code);

            code = after(method.getLongName());
            method.insertAfter(code);

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
          return classfileBuffer;   // byteCode must be mangled.  :(
        }
        ctClass.detach();

        // classname matched - don't check this one any more.
        break;
      }
    }
    return byteCode;
  }

  String before(String name) {
    StringBuilder sb = new StringBuilder(200);
    // start a new block.
    sb.append("{");
    if (Monitor.conf.getAsBoolean(Configuration.WHEREAMI)) {
      sb.append(whereAmI(name));
    }
    sb.append(" cottagecoders_monitor_start = System.nanoTime(); " +
  "cottagecoders_monitor_starting_sequence = com.cottagecoders.monitor.Metrics.incrementSequence(); }");
    return sb.toString();
  }

  String whereAmI(String name) {
    return "System.out.println(\"whereAmI?  got here: " + name + "\");";
  }

  String after(String name) {
    StringBuilder sb = new StringBuilder(200);
    sb.append("{ ");
    sb.append("com.cottagecoders.monitor.Metrics.sendMetrics(\"");
    sb.append(name);
    sb.append("\", cottagecoders_monitor_starting_sequence, System.nanoTime() - cottagecoders_monitor_start); }");
    return sb.toString();
  }
}
