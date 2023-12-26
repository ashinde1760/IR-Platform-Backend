package com.Anemoi.InvestorRelation.AnalystLineItem;

public class AnalystLineItemEntity {

	private String analystLineId;
	private String analystName;
	private String lineItemName;
	private String analystLineItemName;
	private String analystTableHeaderName;
	private String masterTableSource;
	private Long createdOn;
	private String createdBy;
	public String getAnalystLineId() {
		return analystLineId;
	}
	public void setAnalystLineId(String analystLineId) {
		this.analystLineId = analystLineId;
	}
	public String getAnalystName() {
		return analystName;
	}
	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}
	public String getLineItemName() {
		return lineItemName;
	}
	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}
	public String getAnalystLineItemName() {
		return analystLineItemName;
	}
	public void setAnalystLineItemName(String analystLineItemName) {
		this.analystLineItemName = analystLineItemName;
	}
	public String getAnalystTableHeaderName() {
		return analystTableHeaderName;
	}
	public void setAnalystTableHeaderName(String analystTableHeaderName) {
		this.analystTableHeaderName = analystTableHeaderName;
	}
	public String getMasterTableSource() {
		return masterTableSource;
	}
	public void setMasterTableSource(String masterTableSource) {
		this.masterTableSource = masterTableSource;
	}
	public Long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public AnalystLineItemEntity(String analystLineId, String analystName, String lineItemName,
			String analystLineItemName, String analystTableHeaderName, String masterTableSource, Long createdOn,
			String createdBy) {
		super();
		this.analystLineId = analystLineId;
		this.analystName = analystName;
		this.lineItemName = lineItemName;
		this.analystLineItemName = analystLineItemName;
		this.analystTableHeaderName = analystTableHeaderName;
		this.masterTableSource = masterTableSource;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}
	public AnalystLineItemEntity() {
		super();
	}

	
}