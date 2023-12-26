package com.Anemoi.InvestorRelation.RoleModel;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class RoleModelControllerException extends GlobalException {

	private static final long SerialVersionUID = 1L;

	public RoleModelControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public RoleModelControllerException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}