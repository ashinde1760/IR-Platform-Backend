package com.Anemoi.InvestorRelation.AnalystLineItem;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class AnalystLineItemControllerException extends GlobalException {
	private static final long serialVirsionUID = 1L;

	public AnalystLineItemControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public AnalystLineItemControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
