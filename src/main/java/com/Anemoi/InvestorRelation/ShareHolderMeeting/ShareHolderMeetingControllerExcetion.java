package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import com.Anemoi.InvestorRelation.GlobalExeption.GlobalException;

public class ShareHolderMeetingControllerExcetion extends GlobalException {

	private static final long SerialVertionUID = 1L;

	public ShareHolderMeetingControllerExcetion(int errorCode, String developerMessage, String message) {
		super(errorCode, developerMessage, message);
		// TODO Auto-generated constructor stub
	}

	public ShareHolderMeetingControllerExcetion(String message, Throwable cause, int errorCode,
			String developerMessage) {
		super(message, cause, errorCode, developerMessage);
		// TODO Auto-generated constructor stub
	}

}