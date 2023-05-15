package de.nikolas.einsteinmedia.commons.httpserver.http;

import io.netty.util.internal.ObjectUtil;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
@Data
public class Cookie implements Comparable<Cookie> {

  private final String name;
  private String value;
  private String domain;
  private String path;
  private long maxAge;
  private boolean secure;
  private boolean httpOnly;

  public Cookie(String name, String value) {
    this.name = ObjectUtil.checkNotNull(name, "null_cookie_name").trim();
    if (name.isEmpty()) {
      throw new IllegalArgumentException("empty cookie name");
    }
    this.value = value;
    this.maxAge = Long.MIN_VALUE;
  }

  @Override
  public int compareTo(Cookie o) {
    return 0;
  }
}
