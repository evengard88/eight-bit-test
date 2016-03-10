package com.gmail.ikurenkov88.ebit.test.netty.server;

import com.gmail.ikurenkov88.ebit.test.netty.HttpRequestHandler;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Router;
import io.netty.handler.codec.http.router.RouteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class HttpRouterServerHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private final Router<Class> router;
    ApplicationContext appContext;
    @Autowired
    public HttpRouterServerHandler(Router<Class> router, ApplicationContext appContext) {
        this.appContext = appContext;
        this.router = router;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpRequest req) {
        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            return;
        }

        HttpResponse res = createResponse(req, router);
        flushResponse(ctx, req, res);
    }

    private HttpResponse createResponse(HttpRequest req, Router<Class> router) {
        RouteResult<Class> routeResult = router.route(req.getMethod(), req.getUri());
        HttpRequestHandler handler = (HttpRequestHandler)appContext.getBean(routeResult.target());
        return handler.handle(req, routeResult);
    }

    private static ChannelFuture flushResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        if (!HttpHeaders.isKeepAlive(req)) {
            return ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        } else {
            res.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            return ctx.writeAndFlush(res);
        }
    }
}