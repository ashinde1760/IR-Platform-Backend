package com.Anemoi.InvestorRelation.ReportTableHeader;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ReportTableHeaderControllerException extends GlobalException {

	@SuppressWarnings("unused")
	private static final long serialVirsionUID = 1L;

	public ReportTableHeaderControllerException(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public ReportTableHeaderControllerException(String message, Throwable cause, int errorCode,
			String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}
