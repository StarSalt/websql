package org.singledog.websql.web.result;

public interface ReturnResult {

	int httpCode();

	int code();

	String message();

}