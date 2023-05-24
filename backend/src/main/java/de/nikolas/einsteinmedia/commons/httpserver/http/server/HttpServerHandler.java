package de.nikolas.einsteinmedia.commons.httpserver.http.server;

import de.nikolas.einsteinmedia.commons.httpserver.http.HttpMethod;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpNameConstants;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpRequest;
import de.nikolas.einsteinmedia.commons.httpserver.http.HttpResponse;
import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

  private static final HttpDataFactory HTTP_DATA_FACTORY =
      new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
  private Logger logger;

  public HttpServerHandler() {
    this.logger = Logger.Factory.createLogger(HttpServerHandler.class);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      if (msg instanceof FullHttpRequest) {
        FullHttpRequest nettyRequest = (FullHttpRequest) msg;
        logger.info("Received new " + nettyRequest.method().name() + " request.");

        System.out.println("++++++++++++++++");
        System.out.println(nettyRequest.toString());
        System.out.println("++++++++++++++++");

        if (HttpHeaders.is100ContinueExpected(nettyRequest)) {
          ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
          return;
        }

        HttpRequest request = new HttpRequest().fromContext(ctx, nettyRequest);

        if (nettyRequest
            .method()
            .name()
            .equalsIgnoreCase(HttpMethod.OPTIONS.name())) {

          FullHttpResponse optionsResponse = new DefaultFullHttpResponse(
                  HttpVersion.HTTP_1_1,
                  HttpResponseStatus.OK,
                  Unpooled.EMPTY_BUFFER
          );

          optionsResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
          optionsResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, optionsResponse.content().readableBytes());
          optionsResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
          optionsResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET,PUT,POST,DELETE");
          optionsResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");
          optionsResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, "3600");
          optionsResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

          ctx.write(optionsResponse);
        }

        if (nettyRequest
                .method()
                .name()
                .equalsIgnoreCase(
                    HttpMethod.POST.name())
            || nettyRequest
                .method()
                .name()
                .equalsIgnoreCase(
                    HttpMethod.PUT.name())
            || nettyRequest
                .method()
                .name()
                .equalsIgnoreCase(
                    HttpMethod.DELETE.name())
            || nettyRequest
                .method()
                .name()
                .equalsIgnoreCase(
                    HttpMethod.PATCH.name())) {
          String contentType = nettyRequest.headers().get(Names.CONTENT_TYPE);
          if (contentType.isBlank()
              || contentType.startsWith("application/json")
              || contentType.startsWith("text/plain")) {
            String body = nettyRequest.content().toString(HttpNameConstants.UTF_8);
            request.setBody(body);
          } else {
            if (!HttpPostRequestDecoder.isMultipart(nettyRequest)) {
              ByteBuf content = nettyRequest.content();
              if (content.getByte(content.writerIndex() - 1) != HttpConstants.LF) {
                content.writeByte(HttpConstants.CR);
                content.writeByte(HttpConstants.LF);
              }
            }

            HttpPostRequestDecoder decoder =
                new HttpPostRequestDecoder(HTTP_DATA_FACTORY, nettyRequest);
            while (decoder.hasNext()) {
              InterfaceHttpData data = decoder.next();
              if (data.getHttpDataType() == HttpDataType.Attribute) {
                Attribute attribute = (Attribute) data;
                if (request.getParameters() == null) {
                  request.setParameters(new HashMap<>());
                }
                request
                    .getParameters()
                    .put(attribute.getName(), Arrays.asList(attribute.getValue()));
              } else if (data.getHttpDataType() == HttpDataType.FileUpload) {
                // TODO: Fileupload
                logger.info("TODO: Create Fileupload");
              }
            }
            decoder.destroy();
          }
        }

        // Cookies
        Set<Cookie> nettyCookies = Collections.emptySet();
        String cookieValue = nettyRequest.headers().get(Names.COOKIE);
        if (cookieValue != null) nettyCookies = ServerCookieDecoder.STRICT.decode(cookieValue);

        Set<de.nikolas.einsteinmedia.commons.httpserver.http.Cookie> cookies = new HashSet<>();
        for (Cookie nettyCookie : nettyCookies) {
          de.nikolas.einsteinmedia.commons.httpserver.http.Cookie cookie =
              new de.nikolas.einsteinmedia.commons.httpserver.http.Cookie(
                  nettyCookie.name(), nettyCookie.value());
          cookie.setDomain(nettyCookie.domain());
          cookie.setHttpOnly(nettyCookie.isHttpOnly());
          cookie.setMaxAge(nettyCookie.maxAge());
          cookie.setPath(nettyCookie.path());
          cookie.setSecure(nettyCookie.isSecure());
          cookies.add(cookie);
        }
        request.setCookies(cookies);

        // Query String Decode
        QueryStringDecoder queryDecoder = new QueryStringDecoder(nettyRequest.uri());
        Map<String, List<String>> parameters = queryDecoder.parameters();
        request.setPath(queryDecoder.path());
        if (request.getParameters() == null) request.setParameters(new HashMap<>());
        request.getParameters().putAll(parameters);

        // Headers & Auth
        request.setHeaders(new HashMap<>());
        for (Entry<String, String> headers : nettyRequest.headers()) {
          request.getHeaders().put(headers.getKey(), headers.getValue());
        }

        request.saveTokenFromHeader();

        HttpResponse response = new HttpResponse();

        // Dispatch
        HttpDispatcher dispatcher = Providers.get(HttpDispatcher.class);
        dispatcher.dispatchRequest(request, response);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        FullHttpResponse nettyResponse =
            new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(response.getStatusCode().value()),
                Unpooled.copiedBuffer(response.getBody()),
                headers, headers);

        boolean keepAlive = HttpHeaders.isKeepAlive(nettyRequest);

        if (keepAlive) {
          nettyResponse
              .headers()
              .set(Names.CONTENT_LENGTH, nettyResponse.content().readableBytes());
          nettyResponse.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
        }

        nettyResponse.headers().set(Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        nettyResponse.headers().set(Names.ACCESS_CONTROL_ALLOW_METHODS, "GET,PUT,POST,DELETE");
        nettyResponse.headers().set(Names.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");

        ctx.write(nettyResponse);


        if (!keepAlive) {
          ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      System.err.println("Error: " + throwable.toString());
      System.err.println("Cause: " + throwable.getCause());
      logger.error("Internal error while request. Is the handler defined?");
      FullHttpResponse response =
          new DefaultFullHttpResponse(
              HttpVersion.HTTP_1_1,
              HttpResponseStatus.INTERNAL_SERVER_ERROR,
              Unpooled.copiedBuffer(
                  "Request could not be handled".getBytes(StandardCharsets.UTF_8)));
      ctx.writeAndFlush(response);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
    logger.error("Fatal exception cought: " + t);
    ctx.close();
  }
}
