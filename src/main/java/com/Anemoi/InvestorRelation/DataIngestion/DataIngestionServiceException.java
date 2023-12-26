package com.Anemoi.InvestorRelation.DataIngestion;

import io.micronaut.http.HttpResponse;

public class DataIngestionServiceException extends Exception {

	private static final long SerialVertionUID = 1L;

	public DataIngestionServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataIngestionServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DataIngestionServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DataIngestionServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DataIngestionServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public DataIngestionServiceException(String string, HttpResponse<?> errorResponse) {
		// TODO Auto-generated constructor stub
	}

}
