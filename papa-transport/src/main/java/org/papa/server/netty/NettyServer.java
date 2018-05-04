package org.papa.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.papa.server.PapaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by PaperCut on 2018/4/23.
 */
public class NettyServer extends PapaServer{
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ServerBootstrap server;
    private ChannelFuture future;

    public NettyServer(String bindAddr, int port) {
        super(bindAddr, port);
    }

    @Override
    protected void doInit() {
        this.server = new ServerBootstrap();
        this.server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .option(ChannelOption.SO_REUSEADDR, true)
        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .childOption(ChannelOption.TCP_NODELAY, false)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    protected boolean doStart() throws InterruptedException {
        future = server.bind(new InetSocketAddress(bindAddr, port)).sync();
        return future.isSuccess();
    }

    @Override
    protected void doStop() {
        this.future.channel().close();
        logger.warn("Server stopped.");
    }
}
