package com.Anemoi.InvestorRelation.ClientDetails;

import java.util.List;

public class ClientDetailsEntity {

	private String clientId;
	private int projectCode;
	private int taskCode;
	private String clientName;
	private String clientAddress;

	private List<String> emailId;
	private String industry;
	private List<String> suggestedPeers;
	private List<String> assignAA;  //analyst admin
   private String createdBy;
	
	private long createdOn;
	
	private String modifiedBy;
	
	private long modifiedOn;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(int projectCode) {
		this.projectCode = projectCode;
	}

	public int getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(int taskCode) {
		this.taskCode = taskCode;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public List<String> getEmailId() {
		return emailId;
	}

	public void setEmailId(List<String> emailId) {
		this.emailId = emailId;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public List<String> getSuggestedPeers() {
		return suggestedPeers;
	}

	public void setSuggestedPeers(List<String> suggestedPeers) {
		this.suggestedPeers = suggestedPeers;
	}

	public List<String> getAssignAA() {
		return assignAA;
	}

	public void setAssignAA(List<String> assignAA) {
		this.assignAA = assignAA;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public ClientDetailsEntity(String clientId, int projectCode, int taskCode, String clientName, String clientAddress,
			List<String> emailId, String industry, List<String> suggestedPeers, List<String> assignAA, String createdBy,
			long createdOn, String modifiedBy, long modifiedOn) {
		super();
		this.clientId = clientId;
		this.projectCode = projectCode;
		this.taskCode = taskCode;
		this.clientName = clientName;
		this.clientAddress = clientAddress;
		this.emailId = emailId;
		this.industry = industry;
		this.suggestedPeers = suggestedPeers;
		this.assignAA = assignAA;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
	}

	public ClientDetailsEntity() {
		super();
	}
	

}