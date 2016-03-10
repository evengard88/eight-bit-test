package com.gmail.ikurenkov88.ebit.test.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.BadClientSilencer;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.router.Router;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpRouterServerInitializer extends ChannelInitializer<SocketChannel> {
    private final HttpRouterServerHandler handler;
    private final BadClientSilencer       badClientSilencer = new BadClientSilencer();

    @Autowired
    public HttpRouterServerInitializer(HttpRouterServerHandler handler) {
        this.handler = handler;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(handler)
                .addLast(badClientSilencer);
    }
}

