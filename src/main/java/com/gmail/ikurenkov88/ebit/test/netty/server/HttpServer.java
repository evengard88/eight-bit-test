package com.gmail.ikurenkov88.ebit.test.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HttpServer {
	
	private final ServerBootstrap serverBootstrap;
	@Value("${tcp.port}")
	private Integer serverPort;
	private final EventLoopGroup workerGroup;
	private final EventLoopGroup bossGroup;

	private Channel channel;
	@Autowired
	public HttpServer(ServerBootstrap serverBootstrap, EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        this.serverBootstrap = serverBootstrap;
		this.bossGroup = bossGroup;
		this.workerGroup = workerGroup;
	}
    
    public void start() throws InterruptedException {
		try {
			Channel ch = serverBootstrap.bind(serverPort).sync().channel();
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
    }
    
    public void stop() throws InterruptedException {
    	this.channel.close();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
    }
    
}
