package com.Anemoi.InvestorRelation.AnalystDetails;

import java.util.List;

public class AnalystDetailsModified {

	private long analystId;

	private String analystName;

	private long createdOn;

	private long modifiedOn;

	private String createdBy;

	private String modifiedBy;


	
	private long analystcontactid;
	private String pocName;
	private String pocEmailId;
	public AnalystDetailsModified() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AnalystDetailsModified(long analystId, String analystName, long createdOn, long modifiedOn, String createdBy,
			String modifiedBy, long analystcontactid, String pocName, String pocEmailId) {
		super();
		this.analystId = analystId;
		this.analystName = analystName;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.analystcontactid = analystcontactid;
		this.pocName = pocName;
		this.pocEmailId = pocEmailId;
	}
	/**
	 * @return the analystId
	 */
	public long getAnalystId() {
		return analystId;
	}
	/**
	 * @param analystId the analystId to set
	 */
	public void setAnalystId(long analystId) {
		this.analystId = analystId;
	}
	/**
	 * @return the analystName
	 */
	public String getAnalystName() {
		return analystName;
	}
	/**
	 * @param analystName the analystName to set
	 */
	public void setAnalystName(String analystName) {
		this.analystName = analystName;
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
	/**
	 * @return the modifiedOn
	 */
	public long getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
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
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the analystcontactid
	 */
	public long getAnalystcontactid() {
		return analystcontactid;
	}
	/**
	 * @param analystcontactid the analystcontactid to set
	 */
	public void setAnalystcontactid(long analystcontactid) {
		this.analystcontactid = analystcontactid;
	}
	/**
	 * @return the pocName
	 */
	public String getPocName() {
		return pocName;
	}
	/**
	 * @param pocName the pocName to set
	 */
	public void setPocName(String pocName) {
		this.pocName = pocName;
	}
	/**
	 * @return the pocEmailId
	 */
	public String getPocEmailId() {
		return pocEmailId;
	}
	/**
	 * @param pocEmailId the pocEmailId to set
	 */
	public void setPocEmailId(String pocEmailId) {
		this.pocEmailId = pocEmailId;
	}
	
	

	
}
