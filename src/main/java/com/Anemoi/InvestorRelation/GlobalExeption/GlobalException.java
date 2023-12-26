package com.Anemoi.InvestorRelation.GlobalExeption;

public class GlobalException extends Exception {

	private static final long serialVersionUID = 1L;

	public int errorCode;
	public String developerMessage;
	public String message;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GlobalException(int errorCode, String developerMessage, String message) {
		super(message);
		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.message = message;
	}

	public GlobalException(String message, Throwable cause, int errorCode, String developerMessage) {
		super(message, cause);
		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
		this.message = message;
	}

	public GlobalException() {
		super();
	}

}
