package com.Anemoi.InvestorRelation.AnalystDetails;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class AnalystDetailsControllerException extends GlobalException {
	private static final long serialVirsionUID = 1L;

	public AnalystDetailsControllerException(int errorCode, String developerMessage, String message) {

		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.message = message;
	}

	public AnalystDetailsControllerException(String message, Throwable cause, int errorCode, String developerMessage) {

		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.message = message;
	}

}