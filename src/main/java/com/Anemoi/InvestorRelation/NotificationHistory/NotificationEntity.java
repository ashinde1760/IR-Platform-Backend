package com.Anemoi.InvestorRelation.NotificationHistory;

import java.util.List;

public class NotificationEntity {

    private long nId;
	
	private String message;
	
	private List<String> users;
	
	private long createdOn;

	public long getnId() {
		return nId;
	}

	public void setnId(long nId) {
		this.nId = nId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public NotificationEntity(long nId, String message, List<String> users, long createdOn) {
		super();
		this.nId = nId;
		this.message = message;
		this.users = users;
		this.createdOn = createdOn;
	}

	public NotificationEntity() {
		super();
	}
	
	 

}
