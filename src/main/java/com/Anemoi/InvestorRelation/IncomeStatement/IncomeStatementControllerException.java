package com.Anemoi.InvestorRelation.IncomeStatement;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class IncomeStatementControllerException extends GlobalException {

	private static final long SerialVersionUID = 1L;

	public IncomeStatementControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public IncomeStatementControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
