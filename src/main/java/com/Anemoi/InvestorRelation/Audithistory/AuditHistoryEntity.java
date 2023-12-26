package com.Anemoi.InvestorRelation.Audithistory;

public class AuditHistoryEntity {
	
	private long auditId;
	private String activity;
	private String userName;
	private String createdBy;
	private String description;
	private long createdOn;
	public AuditHistoryEntity(long auditId, String activity, String userName, String createdBy, String description,
			long createdOn) {
		super();
		this.auditId = auditId;
		this.activity = activity;
		this.userName = userName;
		this.createdBy = createdBy;
		this.description = description;
		this.createdOn = createdOn;
	}
	public AuditHistoryEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the auditId
	 */
	public long getAuditId() {
		return auditId;
	}
	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}
	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the createdOn
	 */
	public long getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}
	
	
	
}