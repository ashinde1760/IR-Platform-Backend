
package com.Anemoi.InvestorRelation.GlobalExeption;

import java.util.Date;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = { Exception.class, ExceptionHandler.class })
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<Error>> {

	@Override
	public HttpResponse<Error> handle(@SuppressWarnings("rawtypes") HttpRequest request, Exception exception) {
		// TODO Auto-generated method stub
		return HttpResponse.status(getResponseCodeStatus(exception)).body(buildErrorResponce(exception));
	}

	private Error buildErrorResponce(Exception e) {
		Error error = new Error();
		error.setDate(new Date());
		if (e instanceof GlobalException) {
			GlobalException expException = (GlobalException) e;
			error.setErrorCode(expException.getErrorCode());
			error.setDeveloperMessage(expException.getDeveloperMessage());
			error.setMessage(expException.getMessage());

		} else {
			error.setErrorCode(500);
			error.setDeveloperMessage("Unknown exception :: " + e.getMessage());
			error.setMessage("unknown error ");
		}
		return error;
	}

	private HttpStatus getResponseCodeStatus(Exception e) {
		int responseCode = 500;

		if (e instanceof GlobalException) {
			GlobalException exception = (GlobalException) e;
			responseCode = exception.getErrorCode();
		}
		switch (responseCode) {
		case 200:
			return HttpStatus.OK;

		case 201:
			return HttpStatus.CREATED;

		case 400:
			return HttpStatus.BAD_REQUEST;

		case 406:
			return HttpStatus.NOT_ACCEPTABLE;
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
