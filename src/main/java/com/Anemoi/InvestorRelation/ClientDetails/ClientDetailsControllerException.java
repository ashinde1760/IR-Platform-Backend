package com.Anemoi.InvestorRelation.ClientDetails;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ClientDetailsControllerException extends GlobalException {

	private static final long serialVirsionUID = 1L;
	public ClientDetailsControllerException(int errorCode, String developerMessage, String message) {

		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.message = message;
	}

	public ClientDetailsControllerException(String message, Throwable cause, int errorCode, String developerMessage) {

		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.message = message;
	}

}
