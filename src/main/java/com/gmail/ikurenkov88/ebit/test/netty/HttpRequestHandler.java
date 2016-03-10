package com.gmail.ikurenkov88.ebit.test.netty;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

public interface HttpRequestHandler {

	public HttpResponse handle(HttpRequest httpRequest, RouteResult<Class> router);

}
