package de.nikolas.einsteinmedia.commons.httpserver.http.server;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
public class HttpServer {

  private Logger logger;
  private HttpConfig config;
  private EventLoopGroup masterGroup, workerGroup;
  private ChannelFuture channelFuture;

  public HttpServer() {
    this.logger = Logger.Factory.createLogger(HttpServer.class);
    this.config = Providers.get(HttpConfig.class);

    this.masterGroup = new NioEventLoopGroup(config.getMasterThreads());
    this.workerGroup = new NioEventLoopGroup(config.getWorkerThreads());
  }

  public void start() {
    long startTime = System.currentTimeMillis();
    logger.info("Starting webserver ...");

    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap
          .group(masterGroup, workerGroup)
          .childHandler(new HttpChannelInitializer())
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);

      channelFuture = serverBootstrap.bind(config.getPort()).sync();
      logger.info("Listening on " + channelFuture.channel().localAddress());
      logger.info("Webserver started in " + (System.currentTimeMillis() - startTime) + "ms \\o/");

      channelFuture.channel().closeFuture().sync();
    } catch (Exception e) {
      logger.error("Webserver could not be started!\n" + e);
    } finally {
      workerGroup.shutdownGracefully();
      masterGroup.shutdownGracefully();
    }

    logger.info("Listening on " + channelFuture.channel().localAddress());
    logger.info("webserver started in " + (System.currentTimeMillis() - startTime) + "ms \\o/");
  }

  public void stop() {
    logger.info("webserver stopping now ...");
    channelFuture.channel().close();
    logger.info("webserver successfully stopped.");
  }
}
