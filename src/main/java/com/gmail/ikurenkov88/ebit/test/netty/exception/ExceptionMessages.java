package com.gmail.ikurenkov88.ebit.test.netty.exception;

import java.text.MessageFormat;

public enum ExceptionMessages {
	
	MISSING_PARAMETER("Expected {0} parameter."),
	MULTIPLE_VALUES("Expected exactly one value for {0} parameter, found {1}."),
	OUT_OF_RANGE_VALUE("Expected value between 0 and 2147483647 for {0} parameter, found {1}."),
	INCONSISTENT_VALUES("Expected {0} parameter to be less than or equal to the {2} parameter, found {0}={1} {2}={3}"),
	HTTP_METHOD_NOT_SUPPORTED("Expected {0} HTTP method, found {1}.");
	
	private final String pattern;
	
	private ExceptionMessages(String pattern) {
		this.pattern = pattern;
	}
	
	public String format(Object... arguments) {
		return MessageFormat.format(pattern, arguments);
	}
	
}
