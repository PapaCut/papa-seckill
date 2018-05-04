package org.papa.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.papa.server.PapaServer;

/**
 * Created by PaperCut on 2018/4/23.
 */
public class HttpNettyServer extends PapaServer{
    private ServerBootstrap bootstrap;
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    public HttpNettyServer(String bindAddr, int port) {
        super(bindAddr, port);
    }

    @Override
    protected void doInit() {
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpRequestDecoder());
                        pipeline.addLast(new HttpResponseEncoder());
                        pipeline.addLast(new HttpContentCompressor());
                        pipeline.addLast();
                    }
                });
    }

    @Override
    protected boolean doStart() throws InterruptedException {
        return false;
    }

    @Override
    protected void doStop() {

    }
}
