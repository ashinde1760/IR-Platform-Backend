package com.Anemoi.InvestorRelation.RoleModel;

import java.util.ArrayList;

public class RoleModelEntity {

	private String id;
	private String roleName;
	private String description;
	private String status;
	private ArrayList<String> dashboardAccess;
	long lastEdit;
	long createdOn;
	String noOfUser;
	int accessItem;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<String> getDashboardAccess() {
		return dashboardAccess;
	}

	public void setDashboardAccess(ArrayList<String> dashboardAccess) {
		this.dashboardAccess = dashboardAccess;
	}

	public long getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(long lastEdit) {
		this.lastEdit = lastEdit;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public String getNoOfUser() {
		return noOfUser;
	}

	public void setNoOfUser(String noOfUser) {
		this.noOfUser = noOfUser;
	}

	public int getAccessItem() {
		return accessItem;
	}

	public void setAccessItem(int accessItem) {
		this.accessItem = accessItem;
	}

	public RoleModelEntity(String id, String roleName, String description, String status,
			ArrayList<String> dashboardAccess, long lastEdit, long createdOn, String noOfUser, int accessItem) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.description = description;
		this.status = status;
		this.dashboardAccess = dashboardAccess;
		this.lastEdit = lastEdit;
		this.createdOn = createdOn;
		this.noOfUser = noOfUser;
		this.accessItem = accessItem;
	}

	public RoleModelEntity() {
		super();
	}

}
