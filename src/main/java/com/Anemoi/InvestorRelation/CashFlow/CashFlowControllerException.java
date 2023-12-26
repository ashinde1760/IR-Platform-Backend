package com.Anemoi.InvestorRelation.CashFlow;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class CashFlowControllerException extends GlobalException {

	private static final long SerialVertionUID = 1L;

	public CashFlowControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public CashFlowControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}