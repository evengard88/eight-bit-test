package com.gmail.ikurenkov88.ebit.test.server;

import com.gmail.ikurenkov88.ebit.test.service.calculation.HumanPyramidCalc;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by eemrcag on 15.06.2015.
 */

@Component("serverHandler")
//@Sharable
//public class ServerHandler extends ChannelHandlerAdapter {
//
//    @Autowired
//    HumanPyramidCalc humanPyramidCalc;
//
//
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }
//
//
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof HttpRequest) {
//            HttpRequest req = (HttpRequest) msg;
//            String level = null;
//            String index = null;
//
//            //if(req.method().equals(HttpMethod.GET))
//            QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
//            if (!decoder.path().equals("/humanEdgeWeight")) {
//                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
//                ctx.channel().write(response).addListener(ChannelFutureListener.CLOSE);
//
//            }
//            List<String> levelList = decoder.parameters().get("level");
//            if (levelList.size() != 1) {
//                //error
//            } else {
//                level = levelList.get(0);
//            }
//            List<String> indexList = decoder.parameters().get("index");
//            if (indexList.size() > 1) {
//                //error
//            } else {
//                index = indexList.get(0);
//            }
//            double result = 0.0;
//
//            result = humanPyramidCalc.getHumanEdgeWeight(Integer.parseInt(level), 0);
//
//            ByteBuf buf = Unpooled.buffer();
//            ByteBufUtil.writeAscii(buf,String.valueOf(result));
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
//            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
//            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
//
//            ctx.channel().write(response).addListener(ChannelFutureListener.CLOSE);
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
public class ServerHandler extends SimpleChannelInboundHandler<HttpRequest> {


    private final HttpHandlerMapping httpHandlerMapping;

    public ServerHandler(HttpHandlerMapping httpHandlerMapping) {
        this.httpHandlerMapping = httpHandlerMapping;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
        HttpRequestHandler httpRequestHandler = httpHandlerMapping.getHandler(httpRequest);
        HttpResponse httpResponse = httpRequestHandler.handle(httpRequest);
        ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        ctx.close();
    }
}