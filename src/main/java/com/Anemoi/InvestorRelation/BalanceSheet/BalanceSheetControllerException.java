package com.Anemoi.InvestorRelation.BalanceSheet;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

@SuppressWarnings("serial")
public class BalanceSheetControllerException extends GlobalException {

	@SuppressWarnings("unused")
	private static final long serialVirsionUID = 1L;

	public BalanceSheetControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public BalanceSheetControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
