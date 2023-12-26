package com.Anemoi.InvestorRelation.IncomeStatement;

public class IncomeStatementEntity {

	private String incomeid;

	private String lineItem;

	private String alternativeName;
	
	private String createdBy;
	
	private long createdOn;
	
	private String modifiedBy;
	
	private long modifiedOn;

	public String getIncomeid() {
		return incomeid;
	}

	public void setIncomeid(String incomeid) {
		this.incomeid = incomeid;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getAlternativeName() {
		return alternativeName;
	}

	public void setAlternativeName(String alternativeName) {
		this.alternativeName = alternativeName;
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

	public IncomeStatementEntity(String incomeid, String lineItem, String alternativeName, String createdBy,
			long createdOn, String modifiedBy, long modifiedOn) {
		super();
		this.incomeid = incomeid;
		this.lineItem = lineItem;
		this.alternativeName = alternativeName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
	}

	public IncomeStatementEntity() {
		super();
	}
	
	

}