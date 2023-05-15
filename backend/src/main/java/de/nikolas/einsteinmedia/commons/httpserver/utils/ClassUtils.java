package de.nikolas.einsteinmedia.commons.httpserver.utils;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Nikolas Rummel
 * @since 06.10.2021
 *
 * Used from Stackoverflow
 */
public class ClassUtils {

  private static final String DOT = ".";
  private static final String SLASH = "/";
  private static final String EMPTY = "";
  private static final String CLASS_EXT = ".class";
  private static final String JAR_FILE_EXT = ".jar";
  private static final String JAR_PATH_EXT = ".jar!";
  private static final String PATH_FILE_PRE = "file:";

  private static final Logger logger = Logger.Factory.createLogger(ClassUtils.class);

  // ----------------------------------- private method start-------------------------------
  private static FileFilter fileFilter =
      pathname -> isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);

  private ClassUtils() {
    // NOOP
  }

  public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
    try {
      return clazz.getMethod(methodName, paramTypes);
    } catch (NoSuchMethodException ex) {
      return findDeclaredMethod(clazz, methodName, paramTypes);
    }
  }

  public static Method findDeclaredMethod(
      Class<?> clazz, String methodName, Class<?>... paramTypes) {
    try {
      return clazz.getDeclaredMethod(methodName, paramTypes);
    } catch (NoSuchMethodException ex) {
      if (clazz.getSuperclass() != null) {
        return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
      }
      return null;
    }
  }

  public static Class<?>[] getClasses(Object... objects) {
    Class<?>[] classes = new Class<?>[objects.length];
    for (int i = 0; i < objects.length; i++) {
      classes[i] = objects[i].getClass();
    }
    return classes;
  }

  public static Set<Class<?>> scanPackage() {
    return scanPackage(EMPTY, null);
  }

  public static Set<Class<?>> scanPackage(String packageName) {
    return scanPackage(packageName, null);
  }

  public static Set<Class<?>> scanPackageByAnnotation(
      String packageName, final Class<? extends Annotation> annotationClass) {
    return scanPackage(
        packageName,
        new ClassFilter() {
          @Override
          public boolean accept(Class<?> clazz) {
            return clazz.isAnnotationPresent(annotationClass);
          }
        });
  }

  public static Set<Class<?>> scanPackageByAnnotation(
      final Class<? extends Annotation> annotationClass) {
    return scanPackageByAnnotation(EMPTY, annotationClass);
  }

  public static Set<Class<?>> scanPackageBySuperClass(
      String packageName, final Class<?> superClass) {
    return scanPackage(
        packageName,
        new ClassFilter() {
          @Override
          public boolean accept(Class<?> clazz) {
            return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
          }
        });
  }

  public static Set<Class<?>> scanPackageBySuperClass(final Class<?> superClass) {
    return scanPackage(
        EMPTY,
        new ClassFilter() {
          @Override
          public boolean accept(Class<?> clazz) {
            return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
          }
        });
  }

  public static Set<Class<?>> scanPackage(String packageName, ClassFilter classFilter) {
    if (isBlank(packageName)) {
      packageName = EMPTY;
    }
    logger.debug("Scan classes from package [{}]...", packageName);
    packageName = getWellFormedPackageName(packageName);

    final Set<Class<?>> classes = new HashSet<Class<?>>();
    try {
      for (String classPath : getClassPaths(packageName)) {
        classPath = URLDecoder.decode(classPath, Charset.defaultCharset().name());
        logger.debug("Scan classpath: [{}]", classPath);
        fillClasses(classPath, packageName, classFilter, classes);
      }

      if (classes.isEmpty()) {
        for (String classPath : getJavaClassPaths()) {
          classPath = URLDecoder.decode(classPath, Charset.defaultCharset().name());

          logger.debug("Scan java classpath: [{}]", classPath);
          fillClasses(classPath, new File(classPath), packageName, classFilter, classes);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return classes;
  }

  public static final Set<String> getMethods(Class<?> clazz) {
    HashSet<String> methodSet = new HashSet<String>();
    Method[] methodArray = clazz.getMethods();
    for (Method method : methodArray) {
      String methodName = method.getName();
      methodSet.add(methodName);
    }
    return methodSet;
  }

  public static Set<String> getClassPathResources() {
    return getClassPaths(EMPTY);
  }

  public static Set<String> getClassPaths(String packageName) {
    String packagePath = packageName.replace(DOT, SLASH);
    Enumeration<URL> resources;
    try {
      resources = getClassLoader().getResources(packagePath);
    } catch (IOException e) {
      throw new ClassUtilException(format("Loading classPath [{}] error!", packagePath), e);
    }
    Set<String> paths = new HashSet<>();
    while (resources.hasMoreElements()) {
      paths.add(resources.nextElement().getPath());
    }
    return paths;
  }

  public static String getClassPath() {
    return getClassPathURL().getPath();
  }

  public static URL getClassPathURL() {
    return getURL(EMPTY);
  }

  public static URL getURL(String resource) {
    return ClassUtils.getClassLoader().getResource(resource);
  }

  public static String[] getJavaClassPaths() {
    String[] classPaths =
        System.getProperty("java.class.path").split(System.getProperty("path.separator"));
    return classPaths;
  }

  public static Class<?> castToPrimitive(Class<?> clazz) {
    if (null == clazz || clazz.isPrimitive()) {
      return clazz;
    }

    BasicType basicType;
    try {
      basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
    } catch (Exception e) {
      return clazz;
    }

    switch (basicType) {
      case BYTE:
        return byte.class;
      case SHORT:
        return short.class;
      case INTEGER:
        return int.class;
      case LONG:
        return long.class;
      case DOUBLE:
        return double.class;
      case FLOAT:
        return float.class;
      case BOOLEAN:
        return boolean.class;
      case CHAR:
        return char.class;
      default:
        return clazz;
    }
  }

  public static ClassLoader getContextClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  public static ClassLoader getClassLoader() {
    ClassLoader classLoader = getContextClassLoader();
    if (classLoader == null) {
      classLoader = ClassUtils.class.getClassLoader();
    }
    return classLoader;
  }

  @SuppressWarnings("unchecked")
  public static <T> T newInstance(String clazz) {
    try {
      return (T) Class.forName(clazz).newInstance();
    } catch (Exception e) {
      throw new ClassUtilException(format("Instance class [{}] error!", clazz), e);
    }
  }

  public static <T> T newInstance(Class<T> clazz) {
    try {
      return (T) clazz.newInstance();
    } catch (Exception e) {
      throw new ClassUtilException(format("Instance class [{}] error!", clazz), e);
    }
  }

  public static <T> T newInstance(Class<T> clazz, Object... params) {
    if (params == null || params.length == 0) {
      return newInstance(clazz);
    }

    try {
      return clazz.getDeclaredConstructor(getClasses(params)).newInstance(params);
    } catch (Exception e) {
      throw new ClassUtilException(format("Instance class [{}] error!", clazz), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<T> loadClass(String className, boolean isInitialized) {
    Class<T> clazz;
    try {
      clazz = (Class<T>) Class.forName(className, isInitialized, getClassLoader());
    } catch (ClassNotFoundException e) {
      throw new ClassUtilException(e);
    }
    return clazz;
  }

  public static <T> Class<T> loadClass(String className) {
    return loadClass(className, true);
  }

  public static <T> T invoke(String classNameDotMethodName, Object... args) {
    return invoke(classNameDotMethodName, false, args);
  }

  public static <T> T invoke(String classNameDotMethodName, boolean isSingleton, Object... args) {
    if (isBlank(classNameDotMethodName)) {
      throw new ClassUtilException("Blank classNameDotMethodName!");
    }
    final int dotIndex = classNameDotMethodName.lastIndexOf('.');
    if (dotIndex <= 0) {
      throw new ClassUtilException(
          format("Invalid classNameDotMethodName [{}]!", classNameDotMethodName));
    }

    final String className = classNameDotMethodName.substring(0, dotIndex);
    final String methodName = classNameDotMethodName.substring(dotIndex + 1);

    return invoke(className, methodName, isSingleton, args);
  }

  public static <T> T invoke(String className, String methodName, Object... args) {
    Class<Object> clazz = loadClass(className);
    try {
      return invoke(clazz.newInstance(), methodName, args);
    } catch (Exception e) {
      throw new ClassUtilException(e);
    }
  }

  public static <T> T invoke(Object obj, String methodName, Object... args) {
    Method method = getDeclaredMethod(obj, methodName, args);
    return invoke(obj, method, args);
  }

  @SuppressWarnings("unchecked")
  public static <T> T invoke(Object obj, Method method, Object... args) {
    if (false == method.isAccessible()) {
      method.setAccessible(true);
    }
    try {
      return (T) method.invoke(isStatic(method) ? null : obj, args);
    } catch (Exception e) {
      throw new ClassUtilException(e);
    }
  }

  public static Method getDeclaredMethod(Object obj, String methodName, Object... args) {
    return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
  }

  public static Method getDeclaredMethod(
      Class<?> clazz, String methodName, Class<?>... parameterTypes) {
    Method method = null;
    for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        method = clazz.getDeclaredMethod(methodName, parameterTypes);
        return method;
      } catch (NoSuchMethodException e) {
      }
    }

    try {
      return Object.class.getDeclaredMethod(methodName, parameterTypes);
    } catch (Exception e) {
      throw new ClassUtilException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T newProxyInstance(
      Class<T> interfaceClass, InvocationHandler invocationHandler) {
    return (T)
        Proxy.newProxyInstance(
            interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, invocationHandler);
  }

  public static boolean isPrimitiveWrapper(Class<?> clazz) {
    if (null == clazz) {
      return false;
    }
    return BasicType.wrapperPrimitiveMap.containsKey(clazz);
  }

  public static boolean isBasicType(Class<?> clazz) {
    if (null == clazz) {
      return false;
    }
    return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
  }

  public static boolean isSimpleTypeOrArray(Class<?> clazz) {
    if (null == clazz) {
      return false;
    }
    return isSimpleValueType(clazz)
        || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
  }

  public static boolean isSimpleValueType(Class<?> clazz) {
    return isBasicType(clazz)
        || clazz.isEnum()
        || CharSequence.class.isAssignableFrom(clazz)
        || Number.class.isAssignableFrom(clazz)
        || Date.class.isAssignableFrom(clazz)
        || clazz.equals(URI.class)
        || clazz.equals(URL.class)
        || clazz.equals(Locale.class)
        || clazz.equals(Class.class);
  }

  public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
    if (null == targetType || null == sourceType) {
      return false;
    }

    if (targetType.isAssignableFrom(sourceType)) {
      return true;
    }
    if (targetType.isPrimitive()) {
      Class<?> resolvedPrimitive = BasicType.wrapperPrimitiveMap.get(sourceType);
      if (resolvedPrimitive != null && targetType.equals(resolvedPrimitive)) {
        return true;
      }
    } else {
      Class<?> resolvedWrapper = BasicType.primitiveWrapperMap.get(sourceType);
      if (resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isPublic(Class<?> clazz) {
    if (null == clazz) {
      throw new NullPointerException("Class to provided is null.");
    }
    return Modifier.isPublic(clazz.getModifiers());
  }

  public static boolean isPublic(Method method) {
    if (null == method) {
      throw new NullPointerException("Method to provided is null.");
    }
    return isPublic(method.getDeclaringClass());
  }

  public static boolean isNotPublic(Class<?> clazz) {
    return false == isPublic(clazz);
  }

  public static boolean isNotPublic(Method method) {
    return false == isPublic(method);
  }

  public static boolean isStatic(Method method) {
    return Modifier.isStatic(method.getModifiers());
  }

  public static Method setAccessible(Method method) {
    if (null != method && ClassUtils.isNotPublic(method)) {
      method.setAccessible(true);
    }
    return method;
  }

  private static String getWellFormedPackageName(String packageName) {
    return packageName.lastIndexOf(DOT) != packageName.length() - 1
        ? packageName + DOT
        : packageName;
  }

  private static void fillClasses(
      String path, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
    int index = path.lastIndexOf(JAR_PATH_EXT);
    if (index != -1) {
      path = path.substring(0, index + JAR_FILE_EXT.length());
      path = removePrefix(path, PATH_FILE_PRE);
      processJarFile(new File(path), packageName, classFilter, classes);
    } else {
      fillClasses(path, new File(path), packageName, classFilter, classes);
    }
  }

  private static void fillClasses(
      String classPath,
      File file,
      String packageName,
      ClassFilter classFilter,
      Set<Class<?>> classes) {
    if (file.isDirectory()) {
      processDirectory(classPath, file, packageName, classFilter, classes);
    } else if (isClassFile(file)) {
      processClassFile(classPath, file, packageName, classFilter, classes);
    } else if (isJarFile(file)) {
      processJarFile(file, packageName, classFilter, classes);
    }
  }

  private static void processDirectory(
      String classPath,
      File directory,
      String packageName,
      ClassFilter classFilter,
      Set<Class<?>> classes) {
    for (File file : directory.listFiles(fileFilter)) {
      fillClasses(classPath, file, packageName, classFilter, classes);
    }
  }

  private static void processClassFile(
      String classPath,
      File file,
      String packageName,
      ClassFilter classFilter,
      Set<Class<?>> classes) {
    if (false == classPath.endsWith(File.separator)) {
      classPath += File.separator;
    }
    String path = file.getAbsolutePath();
    if (isBlank(packageName)) {
      path = removePrefix(path, classPath);
    }
    final String filePathWithDot = path.replace(File.separator, DOT);

    int subIndex = -1;
    if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
      final int endIndex = filePathWithDot.lastIndexOf(CLASS_EXT);

      final String className = filePathWithDot.substring(subIndex, endIndex);
      fillClass(className, packageName, classes, classFilter);
    }
  }

  private static void processJarFile(
      File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
    try {
      for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
        if (isClass(entry.getName())) {
          final String className = entry.getName().replace(SLASH, DOT).replace(CLASS_EXT, EMPTY);
          fillClass(className, packageName, classes, classFilter);
        }
      }
    } catch (Throwable ex) {
      logger.error(ex.getMessage() + ex);
    }
  }

  private static void fillClass(
      String className, String packageName, Set<Class<?>> classes, ClassFilter classFilter) {
    if (className.startsWith(packageName)) {
      try {
        final Class<?> clazz = Class.forName(className, false, getClassLoader());
        if (classFilter == null || classFilter.accept(clazz)) {
          classes.add(clazz);
        }
      } catch (Throwable ex) {
      }
    }
  }

  private static boolean isClassFile(File file) {
    return isClass(file.getName());
  }

  private static boolean isClass(String fileName) {
    return fileName.endsWith(CLASS_EXT);
  }

  private static boolean isJarFile(File file) {
    return file.getName().endsWith(JAR_FILE_EXT);
  }

  private static boolean isBlank(String str) {
    int length;

    if ((str == null) || ((length = str.length()) == 0)) {
      return true;
    }

    for (int i = 0; i < length; i++) {
      if (false == Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  private static String removePrefix(String str, String prefix) {
    if (str.startsWith(prefix)) {
      return str.substring(prefix.length());
    }
    return str;
  }

  private static String format(String template, Object... values) {
    if (values == null || values.length == 0 || isBlank(template)) {
      return template;
    }

    final StringBuilder sb = new StringBuilder();
    final int length = template.length();

    int valueIndex = 0;
    char currentChar;
    for (int i = 0; i < length; i++) {
      if (valueIndex >= values.length) {
        sb.append(template.substring(i, length));
        break;
      }

      currentChar = template.charAt(i);
      if (currentChar == '{') {
        final char nextChar = template.charAt(++i);
        if (nextChar == '}') {
          sb.append(values[valueIndex++]);
        } else {
          sb.append('{').append(nextChar);
        }
      } else {
        sb.append(currentChar);
      }
    }

    return sb.toString();
  }

  private enum BasicType {
    BYTE,
    SHORT,
    INT,
    INTEGER,
    LONG,
    DOUBLE,
    FLOAT,
    BOOLEAN,
    CHAR,
    CHARACTER,
    STRING;

    public static final Map<Class<?>, Class<?>> wrapperPrimitiveMap =
        new HashMap<Class<?>, Class<?>>(8);
    public static final Map<Class<?>, Class<?>> primitiveWrapperMap =
        new HashMap<Class<?>, Class<?>>(8);

    static {
      wrapperPrimitiveMap.put(Boolean.class, boolean.class);
      wrapperPrimitiveMap.put(Byte.class, byte.class);
      wrapperPrimitiveMap.put(Character.class, char.class);
      wrapperPrimitiveMap.put(Double.class, double.class);
      wrapperPrimitiveMap.put(Float.class, float.class);
      wrapperPrimitiveMap.put(Integer.class, int.class);
      wrapperPrimitiveMap.put(Long.class, long.class);
      wrapperPrimitiveMap.put(Short.class, short.class);

      for (Map.Entry<Class<?>, Class<?>> entry : wrapperPrimitiveMap.entrySet()) {
        primitiveWrapperMap.put(entry.getValue(), entry.getKey());
      }
    }
  }

  public static interface ClassFilter {
    boolean accept(Class<?> clazz);
  }
  // -------------------------private method end-----------------------------------------

  private static class ClassUtilException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClassUtilException(String msg) {
      super(msg);
    }

    public ClassUtilException(Throwable cause) {
      super(cause);
    }

    public ClassUtilException(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
}
