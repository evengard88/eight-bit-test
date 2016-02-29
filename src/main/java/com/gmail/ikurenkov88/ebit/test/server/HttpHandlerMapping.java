package com.gmail.ikurenkov88.ebit.test.server;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpHandlerMapping {

    public static final HttpRequestHandler DEFAULT_HTTP_REQUEST_HANDLER = new HttpRequestHandler() {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer("No resource found.", StandardCharsets.UTF_8));
            defaultFullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
            defaultFullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, defaultFullHttpResponse.content().readableBytes());
            return defaultFullHttpResponse;
        }
    };

    private final Map<String, HttpRequestHandler> pathToHttpRequestHandler;


    public HttpHandlerMapping(Map<String, HttpRequestHandler> pathToHttpRequestHandler) {
        this.pathToHttpRequestHandler = pathToHttpRequestHandler;
    }

    public HttpRequestHandler getHandler(HttpRequest httpRequest) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.getUri());
        return pathToHttpRequestHandler.getOrDefault(queryStringDecoder.path(), DEFAULT_HTTP_REQUEST_HANDLER);
    }

}