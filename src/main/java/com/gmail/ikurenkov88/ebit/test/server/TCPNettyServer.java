package com.gmail.ikurenkov88.ebit.test.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import io.netty.bootstrap.ServerBootstrap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by ilia on 28.02.16.
 */

@Component("serverNettyImpl")
public class TCPNettyServer {

    @Autowired
    @Qualifier("serverInitializer")
    private ServerInitializer serverInitializer;

    @Autowired
    @Qualifier("bossGroup")
    private NioEventLoopGroup bossGroup;

    @Autowired
    @Qualifier("workerGroup")
    private NioEventLoopGroup workerGroup;

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress tcpSocketAddress;
    ServerBootstrap bootstrap;
    private Channel channel;

    @PostConstruct
    public void start() {
        bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(serverInitializer);

        try {
            channel = bootstrap.bind(tcpSocketAddress).sync().channel().closeFuture().channel();
            System.out.println("Server started");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void restart() {
        this.stop();
        this.start();
    }


    public void stop() {
        this.channel.close();
        this.bootstrap.group().shutdownGracefully();
        this.bootstrap.childGroup().shutdownGracefully();
    }
}