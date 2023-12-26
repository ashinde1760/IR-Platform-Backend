package com.Anemoi.InvestorRelation.ClientLineItem;

public class ClientLineItemEntity {

	private String clientLineId;
	private String clientName;
	private String lineItemName;
	private String clientLineItemName;
	private String clientTableHeaderName;
	private String masterTableSource;
	private Long createdOn;
    private String createdBy;
	public String getClientLineId() {
		return clientLineId;
	}
	public void setClientLineId(String clientLineId) {
		this.clientLineId = clientLineId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getLineItemName() {
		return lineItemName;
	}
	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}
	public String getClientLineItemName() {
		return clientLineItemName;
	}
	public void setClientLineItemName(String clientLineItemName) {
		this.clientLineItemName = clientLineItemName;
	}
	public String getClientTableHeaderName() {
		return clientTableHeaderName;
	}
	public void setClientTableHeaderName(String clientTableHeaderName) {
		this.clientTableHeaderName = clientTableHeaderName;
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
	public ClientLineItemEntity(String clientLineId, String clientName, String lineItemName, String clientLineItemName,
			String clientTableHeaderName, String masterTableSource, Long createdOn, String createdBy) {
		super();
		this.clientLineId = clientLineId;
		this.clientName = clientName;
		this.lineItemName = lineItemName;
		this.clientLineItemName = clientLineItemName;
		this.clientTableHeaderName = clientTableHeaderName;
		this.masterTableSource = masterTableSource;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}
	public ClientLineItemEntity() {
		super();
	}
    
    
	
}