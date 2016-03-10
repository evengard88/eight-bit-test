package com.gmail.ikurenkov88.ebit.test.netty.controllers;

import com.gmail.ikurenkov88.ebit.test.netty.HttpRequestHandler;
import com.gmail.ikurenkov88.ebit.test.netty.exception.ExceptionMessages;
import com.gmail.ikurenkov88.ebit.test.netty.exception.HttpBadRequestException;
import com.gmail.ikurenkov88.ebit.test.netty.exception.HttpMethodNotAllowedException;
import com.gmail.ikurenkov88.ebit.test.service.calculation.HumanPyramidCalc;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.RouteResult;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpHumanEdgeWeightController implements HttpRequestHandler {
	
	private static final String LEVEL = "level";
	private static final String INDEX = "index";
	private static final int INDEX_DEFAULT = 0;
	
	private final HumanPyramidCalc humanPyramidCalc;
	
	public HttpHumanEdgeWeightController(HumanPyramidCalc humanPyramidCalc) {
		this.humanPyramidCalc = humanPyramidCalc;
	}

	@Override
	public HttpResponse handle(HttpRequest httpRequest, RouteResult<Class> router) {
		try {
			Map<String, String> stringStringMap = router.pathParams();
			Map<String, List<String>> stringListMap = router.queryParams();
			Map<String, List<String>> params;
			if(!stringStringMap.isEmpty()){
				params = stringStringMap.entrySet()
						.stream()
						.collect(Collectors.toMap(Map.Entry::getKey,
								e -> Collections.singletonList(e.getValue())));
			}
			else params = stringListMap;
			return doHandle(params);
		} catch (HttpBadRequestException | NumberFormatException e) {
			return newHttpResponse(HttpResponseStatus.BAD_REQUEST, e.toString());
		}
	}

	private HttpResponse doHandle(Map<String, List<String>> requestParameters) throws HttpBadRequestException {
		int level = parseInt(LEVEL, requestParameters);
		int index = parseOptionalInt(INDEX, requestParameters).orElse(INDEX_DEFAULT);
		throwIfParametersAreNotConsistent(level, index);

		return newHttpResponse(HttpResponseStatus.OK, Double.toString(humanPyramidCalc.getHumanEdgeWeight(level, index)));
	}
	
	private void throwIfHttpMethodIsNotSupported(HttpMethod httpMethod) throws HttpMethodNotAllowedException {
		if (!httpMethod.equals(HttpMethod.GET)) {
			throw new HttpMethodNotAllowedException(ExceptionMessages.HTTP_METHOD_NOT_SUPPORTED.format(httpMethod));
		}
	}
	
	private int parseInt(String parameter, Map<String, List<String>> queryStringParameters) throws HttpBadRequestException {
		
		List<String> parameterValues = queryStringParameters.get(parameter);
		if (parameterValues == null) {
			throw new HttpBadRequestException(ExceptionMessages.MISSING_PARAMETER.format(parameter));
		}
		if (parameterValues.size() > 1) {
			throw new HttpBadRequestException(ExceptionMessages.MULTIPLE_VALUES.format(parameter, parameterValues.size()));
		}
		
		BigInteger parameterAsBigInteger = new BigInteger(parameterValues.get(0));
		if (parameterAsBigInteger.compareTo(BigInteger.ZERO) < 0 || parameterAsBigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			throw new HttpBadRequestException(ExceptionMessages.OUT_OF_RANGE_VALUE.format(parameter, parameterAsBigInteger));
		}
		
		return parameterAsBigInteger.intValue();
	}
	
	private Optional<Integer> parseOptionalInt(String parameter, Map<String, List<String>> queryStringParameters) throws HttpBadRequestException {
		return queryStringParameters.containsKey(parameter) ? Optional.of(parseInt(parameter, queryStringParameters)) : Optional.empty();
	}
	
	private void throwIfParametersAreNotConsistent(int level, int index) throws HttpBadRequestException {
		if (index > level) {
			throw new HttpBadRequestException(ExceptionMessages.INCONSISTENT_VALUES.format(INDEX, index, LEVEL, level));
		}
	}
	
	private HttpResponse newHttpResponse(HttpResponseStatus httpResponseStatus, String content) {
		DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus, Unpooled.copiedBuffer(content, StandardCharsets.UTF_8));
		defaultFullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
		defaultFullHttpResponse.headers().set(HttpHeaders.Names.CONTENT_LENGTH, defaultFullHttpResponse.content().readableBytes());
		return defaultFullHttpResponse;
	}
}
