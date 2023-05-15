package de.nikolas.einsteinmedia.commons.httpserver.http.server;

import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpNameConstants;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpStatus;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpMapping;
import de.nikolas.einsteinmedia.commons.httpserver.utils.ClassUtils;
import de.nikolas.einsteinmedia.commons.httpserver.utils.IoUtils;
import de.nikolas.einsteinmedia.commons.httpserver.utils.JsonUtils;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Pair;
import de.nikolas.einsteinmedia.commons.httpserver.utils.RestMatcher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import de.nikolas.einsteinmedia.commons.httpserver.http.annotation.HttpController;
import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
public class HttpDispatcher {

  private static final String DEFAULT_ROOT_PATH = "/api/v1"; // TODO: store in HttpConfig
  private final Logger logger = Logger.Factory.createLogger(HttpDispatcher.class);

  private Map<HttpMethod, Map<String, HttpHandler>> handlers;
  private Map<String, String>
      defaultMimeTypes; // https://wiki.selfhtml.org/wiki/MIME-Type/%C3%9Cbersicht
  private Set<String> urlExts;

  public HttpDispatcher() {
    this.handlers = new ConcurrentHashMap<>();
    this.urlExts =
        new HashSet<>() {
          private static final long serialVersionUID = 1L;

          {
            add("json");
            add("do");
            add("action");
          }
        };
    this.defaultMimeTypes =
        new HashMap<>() {
          private static final long serialVersionUID = 1L;

          {
            put("txt", "text/plain");
            put("css", "text/css");
            put("csv", "text/csv");
            put("htm", "text/html");
            put("html", "text/html");
            put("xml", "text/xml");
            put("js", "application/javascript");
            put("xhtml", "application/xhtml+xml");
            put("json", "application/json");
            put("pdf", "application/pdf");
            put("zip", "application/zip");
            put("tar", "application/x-tar");
            put("gif", "image/gif");
            put("jpeg", "image/jpeg");
            put("jpg", "image/jpeg");
            put("tiff", "image/tiff");
            put("tif", "image/tiff");
            put("png", "image/png");
            put("svg", "image/svg+xml");
            put("ico", "image/x-icon");
            put("mp3", "audio/mpeg");
          }
        };
    scanAndRegisterHandlers();
  }

  private static String getPathExtension(String path) {
    int lastDot = path.lastIndexOf('.');
    if (lastDot == -1) {
      return "";
    }
    String extension = path.substring(lastDot + 1).toLowerCase(Locale.ROOT);
    return extension;
  }

  public void dispatchRequest(HttpRequest request, HttpResponse response) throws IOException {
    String path = request.getPath();
    if ("/".equals(path)) {
      path = "/index.html";
    }
    String ext = getPathExtension(path);
    if (ext.isBlank() || urlExts.contains(ext)) {
      Map<String, HttpHandler> handlers = getHandlers(request.getMethod());

      String pathWithoutExt = removePathExtension(path);
      Set<String> mappingPaths = handlers.keySet();
      Pair<String, Map<String, List<String>>> result = RestMatcher.match(pathWithoutExt, mappingPaths);
      String mappedPath = result.getKey();
      if(isBlank(mappedPath)) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.setBody(("No handler found for uri [" + request.getUri() + "] and method ["
                + request.getMethod() + "]").getBytes(HttpNameConstants.UTF_8));
        response.setContentType(HttpNameConstants.ContentTypes.TEXT_UTF_8);
        return;
      } else {
        request.getParameters().putAll(result.getValue());
      }

      HttpHandler handler = handlers.get(mappedPath);
      Object bodyObj = handler.invoke(request, response);
      if(bodyObj != null) {
        response.setBody(JsonUtils.toJsonString(bodyObj).getBytes(HttpNameConstants.UTF_8));
      }
      response.setContentType(HttpNameConstants.ContentTypes.JSON_UTF_8);
    } else {
      InputStream in = HttpDispatcher.class.getResourceAsStream(DEFAULT_ROOT_PATH + path);
      if (in == null) {
        if("/index.html".equals(path)) {
          in = HttpDispatcher.class.getResourceAsStream(path);
        }
        if(in == null) {
          response.setStatusCode(HttpStatus.NOT_FOUND);
          return;
        }
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      IoUtils.copy(in, out);
      response.setContentType(guessMimeType(path));
      response.setBody(out.toByteArray());
      if (in != null) {
        in.close();
      }
    }
  }

  private String guessMimeType(String path) {
    int lastDot = path.lastIndexOf('.');
    if (lastDot == -1) {
      return "";
    }
    String extension = path.substring(lastDot + 1).toLowerCase(Locale.ROOT);
    String mimeType = defaultMimeTypes.get(extension);
    if (mimeType == null) {
      return "";
    }
    return mimeType;
  }

  private String removePathExtension(String path) {
    int lastDot = path.lastIndexOf('.');
    if (lastDot == -1) {
      return path;
    }
    return path.substring(0, lastDot);
  }

  private void registerHandler(HttpMethod httpMethod, String path, HttpHandler handler) {
    if (httpMethod == null || path == null) {
      logger.error("Argument (method or path) cannot be null!");
      throw new IllegalArgumentException("method = " + httpMethod + ", path = " + path);
    }

    path = RestMatcher.trimSlash(path);
    Map<String, HttpHandler> handlerMap = getHandlers(httpMethod);
    if (handlerMap.containsKey(path)) {
      logger.error("Duplicate path mapping");
      throw new IllegalArgumentException("method = " + httpMethod + ", path = " + path);
    }

    putHandler(httpMethod, path, handler);
  }

  private void putHandler(HttpMethod method, String path, HttpHandler handler) {
    Map<String, HttpHandler> h = handlers.get(method);
    if (h == null) {
      h = new ConcurrentHashMap<>();
      handlers.put(method, h);
    }
    handlers.get(method).put(path, handler);
  }

  private Map<String, HttpHandler> getHandlers(HttpMethod method) {
    Map<String, HttpHandler> handlerMap = handlers.get(method);
    if (handlerMap == null) {
      handlerMap = new ConcurrentHashMap<>();
      handlers.put(method, handlerMap);
    }
    return handlerMap;
  }

  public void addUrlExt(String ext) {
    urlExts.add(ext);
  }

  private void scanAndRegisterHandlers() {
    Set<Class<?>> classes = ClassUtils.scanPackageByAnnotation(HttpController.class);
    for (Class<?> clazz : classes) {
      Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        HttpMapping httpMapping = method.getAnnotation(HttpMapping.class);
        if (httpMapping == null) {
          continue;
        }
        HttpMethod[] httpMethods = httpMapping.method();
        String[] httpPaths = httpMapping.path();
        for (HttpMethod httpMethod : httpMethods) {
          for (String path : httpPaths) {
            Object object = ClassUtils.newInstance(clazz);
            HttpHandler handler = new HttpHandler(object, method);
            registerHandler(httpMethod, path, handler);
          }
        }
      }
    }
  }

  private boolean isBlank(String str) {
    int length;

    if ((str == null) || ((length = str.length()) == 0)) {
      return true;
    }

    for (int i = 0; i < length; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }

    return true;
  }
}
