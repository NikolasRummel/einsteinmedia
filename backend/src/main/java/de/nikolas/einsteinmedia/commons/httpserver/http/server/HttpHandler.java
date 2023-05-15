package de.nikolas.einsteinmedia.commons.httpserver.http.server;

import java.lang.reflect.Method;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.utils.ClassUtils;

/**
 * @author Nikolas Rummel
 * @since 06.10.2021
 */
public class HttpHandler {

  private Object target;
  private Method method;

  public HttpHandler(Object target, Method method) {
    this.target = target;
    this.method = method;
  }

  public Object invoke(HttpRequest request, HttpResponse response) {
    Class<?>[] params = method.getParameterTypes();
    Object[] args = new Object[params.length];
    for (int i = 0; i < params.length; i++) {
      Class<?> param = params[i];
      if (param == HttpRequest.class) {
        args[i] = request;
      }
      if (param == HttpResponse.class) {
        args[i] = response;
      }
    }
    return ClassUtils.invoke(target, method, args);
  }

  public Object getTarget() {
    return target;
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }
}
