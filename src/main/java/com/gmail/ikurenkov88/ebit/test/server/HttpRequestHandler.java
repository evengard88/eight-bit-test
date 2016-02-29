package com.gmail.ikurenkov88.ebit.test.server;

/**
 * Created by ilia on 29.02.16.
 */
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface HttpRequestHandler {

    public HttpResponse handle(HttpRequest httpRequest);

}