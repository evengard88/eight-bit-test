package com.gmail.ikurenkov88.ebit.test.netty;

import com.gmail.ikurenkov88.ebit.test.netty.controllers.HttpHumanEdgeWeightController;
import com.gmail.ikurenkov88.ebit.test.netty.server.HttpRouterServerInitializer;

import com.gmail.ikurenkov88.ebit.test.service.calculation.HumanPyramidCalc;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by ilia on 10.03.16.
 */
@Configuration
public class ServerConfig {
    @Bean
    public ServerBootstrap serverBootstrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup, HttpRouterServerInitializer httpServerInitializer) {
        return new ServerBootstrap()
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE)
                .childOption(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE)
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(httpServerInitializer);
    }

    @Bean
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public HttpHumanEdgeWeightController httpHumanEdgeWeightHandler(HumanPyramidCalc humanPyramidCalc) {
        return new HttpHumanEdgeWeightController(humanPyramidCalc);
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
