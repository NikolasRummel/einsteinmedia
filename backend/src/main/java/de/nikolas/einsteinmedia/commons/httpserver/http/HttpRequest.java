package de.nikolas.einsteinmedia.commons.httpserver.http;

import de.nikolas.einsteinmedia.commons.httpserver.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import java.io.Serializable;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  private HttpMethod method;
  private String uri;
  private String path;
  private String body;

  private Map<String, List<String>> parameters;
  private Map<String, String> headers;
  private Set<Cookie> cookies;
  private String token;

  private SocketAddress localAdress;
  private SocketAddress remoteAdress;

  public HttpRequest fromContext(ChannelHandlerContext context, FullHttpRequest nettyRequest) {
    setRemoteAdress(context.channel().remoteAddress());
    setLocalAdress(context.channel().localAddress());
    setUri(nettyRequest.uri());
    setMethod(HttpMethod.valueOf(nettyRequest.method().name()));
    return this;
  }

  public void saveTokenFromHeader() {
    if(this.headers.get("Authorization") != null) {
      this.token = headers.get("Authorization").replace("Bearer ", "");
    }
  }

  public String getPathParameter(String parameterName) {
    if (parameters.containsKey(parameterName)) {
      List<String> parameterValues = parameters.get(parameterName);
      if (!parameterValues.isEmpty()) {
        return parameterValues.get(0);
      }
    }
    return null;
  }


  public <T> T getBodyAsObject(Class clazz) {
    return (T) JsonUtils.toBean(body, clazz);
  }
}
