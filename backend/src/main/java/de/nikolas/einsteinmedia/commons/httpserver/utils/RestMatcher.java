package de.nikolas.einsteinmedia.commons.httpserver.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Nikolas Rummel
 * @since 06.10.2021
 */
public class RestMatcher {

  public static Pair<String, Map<String, List<String>>> match(
      String requestPath, Set<String> mappingPaths) {
    Map<String, Map<String, List<String>>> tmp = new HashMap<String, Map<String, List<String>>>();
    for (String mappingPath : mappingPaths) {
      Pair<Boolean, Map<String, List<String>>> match = matchOne(requestPath, mappingPath);
      if (match.getKey()) {
        tmp.put(mappingPath, match.getValue());
      }
    }

    String[] arr = tmp.keySet().toArray(new String[0]);
    if (arr.length == 0) {
      return new Pair<>(null, null);
    }
    String priMapping = arr[0];
    int maxDeepth = 0;
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr[i].length(); j++) {
        if (arr[i].charAt(j) != requestPath.charAt(j) || j == arr[i].length() - 1) {
          if (j > maxDeepth) {
            maxDeepth = j;
            priMapping = arr[i];
          }
          break;
        }
      }
    }
    Map<String, List<String>> params = tmp.get(priMapping);
    return new Pair<>(priMapping, params);
  }

  private static Pair<Boolean, Map<String, List<String>>> matchOne(
      String requestPath, String mappingPath) {
    // remove starting/ending slashes
    requestPath = trimSlash(requestPath);
    mappingPath = trimSlash(mappingPath);
    String[] requestPaths = requestPath.split("/");
    String[] mappingPaths = mappingPath.split("/");
    if (requestPaths.length != mappingPaths.length) {
      return new Pair<>(false, null);
    }
    Map<String, List<String>> params = new HashMap<>();
    for (int i = 0; i < mappingPaths.length; i++) {
      String p1 = requestPaths[i];
      String p2 = mappingPaths[i];
      if (p1.equals(p2)) {
        continue;
      } else if (p2.startsWith("{") && p2.endsWith("}")) {
        String key = p2.substring(1, p2.length() - 1);
        String value = requestPaths[i];
        if (value != null) {
          if (params.get(key) == null) {
            params.put(key, new ArrayList<>());
          }
          params.get(key).add(value);
        }
      } else {
        return new Pair<>(false, null);
      }
    }
    return new Pair<>(true, params);
  }

  public static String trimSlash(String str) {
    if (str.startsWith("/")) {
      return str.substring(1);
    }
    if (str.endsWith("/")) {
      return str.substring(0, str.length() - 1);
    }
    return str;
  }
}
