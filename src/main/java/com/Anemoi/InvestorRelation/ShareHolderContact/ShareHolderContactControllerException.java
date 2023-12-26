package com.Anemoi.InvestorRelation.ShareHolderContact;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ShareHolderContactControllerException extends GlobalException {

	private static final long SerialVertionUID = 1L;

	public ShareHolderContactControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public ShareHolderContactControllerException(String message, Throwable cause, int errorCode,
			String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
