package de.nikolas.einsteinmedia.commons.httpserver.http;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
@Data
public class HttpResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  private HttpStatus statusCode;
  private byte[] body;
  private String contentType;
  private Map<String, List<String>> headers;

  public HttpResponse() {
    this(HttpStatus.OK);
  }

  public HttpResponse(HttpStatus status) {
    this(status, "ERROR".getBytes(StandardCharsets.UTF_8));
  }

  public HttpResponse(HttpStatus status, byte[] body) {
    this(status, HttpNameConstants.ContentTypes.TEXT_UTF_8, body);
  }

  public HttpResponse(HttpStatus status, String contentType, byte[] body) {
    this.statusCode = status;
    this.contentType = contentType;
    this.body = body;
  }
}
