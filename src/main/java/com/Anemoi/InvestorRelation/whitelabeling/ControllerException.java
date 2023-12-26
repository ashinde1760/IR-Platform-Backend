package com.Anemoi.InvestorRelation.whitelabeling;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ControllerException extends GlobalException {
	
	private static final long SerialVertionUID = 1L;

	public ControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public ControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
