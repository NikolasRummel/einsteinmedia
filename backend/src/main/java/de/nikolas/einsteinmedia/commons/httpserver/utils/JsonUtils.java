package de.nikolas.einsteinmedia.commons.httpserver.utils;

import com.alibaba.fastjson.JSON;
import java.util.List;

/**
 * @author Nikolas Rummel
 * @since 06.10.2021
 */
public class JsonUtils {

  public static String toJsonString(Object bean) {
    return JSON.toJSONString(bean);
  }

  public static <T> T toBean(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }

  public static <T> List<T> toList(String json, Class<T> clazz) {
    return JSON.parseArray(json, clazz);
  }
}
