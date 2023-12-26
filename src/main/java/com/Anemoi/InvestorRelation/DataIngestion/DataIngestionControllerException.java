package com.Anemoi.InvestorRelation.DataIngestion;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class DataIngestionControllerException extends GlobalException {

	private static final long SerialVertionUID = 1L;

	public DataIngestionControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public DataIngestionControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
