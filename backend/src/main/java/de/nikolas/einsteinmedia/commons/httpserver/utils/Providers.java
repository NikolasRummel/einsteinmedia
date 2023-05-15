package de.nikolas.einsteinmedia.commons.httpserver.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
public final class Providers {

  private static Map<Class<?>, Object> singeltons = new HashMap<>();

  private Providers() {}

  public static synchronized <T> T get(Class<?> clazz, Object... parameters) {
    T object = (T) singeltons.get(clazz);
    if (object == null) {
      try {
        object = (T) clazz.getDeclaredConstructor().newInstance(parameters);

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      singeltons.put(clazz, object);
    }
    return object;
  }

  public static synchronized <T> T get(String className) {
    Class<?> clazz = null;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return get(clazz);
  }

  public static synchronized <T> T put(Class<?> clazz, Object object) {
    singeltons.put(clazz, object);
    return get(clazz);
  }

  public static synchronized void remove(Class<?> clazz) {
    singeltons.remove(clazz);
  }

  public static synchronized void destroy() {
    singeltons.clear();
  }
}
