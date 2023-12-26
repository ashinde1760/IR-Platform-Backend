package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ShareHolderDataFormControllerException extends GlobalException {

	private static final long SerialVertionUID = 1L;

	public ShareHolderDataFormControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public ShareHolderDataFormControllerException(String message, Throwable cause, int errorCode,
			String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}