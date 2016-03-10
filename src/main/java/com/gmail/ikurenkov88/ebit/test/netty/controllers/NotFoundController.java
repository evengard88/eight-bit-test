package com.gmail.ikurenkov88.ebit.test.netty.controllers;

import com.gmail.ikurenkov88.ebit.test.netty.HttpRequestHandler;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.RouteResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Created by ilia on 10.03.16.
 */
@Component
public class NotFoundController implements HttpRequestHandler {
    @Override
    public HttpResponse handle(HttpRequest httpRequest, RouteResult<Class> router) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        defaultFullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        defaultFullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, defaultFullHttpResponse.content().readableBytes());
        return defaultFullHttpResponse;
    }
}
