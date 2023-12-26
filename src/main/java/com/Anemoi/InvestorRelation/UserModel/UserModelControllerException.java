package com.Anemoi.InvestorRelation.UserModel;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class UserModelControllerException extends GlobalException {

	private static final long SerialVertionUID = 1L;

	public UserModelControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public UserModelControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
