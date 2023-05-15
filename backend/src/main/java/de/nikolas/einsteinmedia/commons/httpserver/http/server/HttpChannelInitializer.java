package de.nikolas.einsteinmedia.commons.httpserver.http.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();

    pipeline.addLast("decoder", new HttpRequestDecoder());
    pipeline.addLast("encoder", new HttpResponseEncoder());

    pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
    pipeline.addLast("deflater", new HttpContentCompressor());
    pipeline.addLast("handler", new HttpServerHandler());
  }

}
