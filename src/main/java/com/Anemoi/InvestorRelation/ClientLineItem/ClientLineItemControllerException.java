package com.Anemoi.InvestorRelation.ClientLineItem;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ClientLineItemControllerException extends GlobalException {

	private static final long serialVirsionUID = 1L;

	public ClientLineItemControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public ClientLineItemControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
