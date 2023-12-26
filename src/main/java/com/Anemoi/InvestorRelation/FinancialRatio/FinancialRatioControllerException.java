package com.Anemoi.InvestorRelation.FinancialRatio;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class FinancialRatioControllerException extends GlobalException {

	private static final long SerialVersionUID = 1L;

	public FinancialRatioControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public FinancialRatioControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
