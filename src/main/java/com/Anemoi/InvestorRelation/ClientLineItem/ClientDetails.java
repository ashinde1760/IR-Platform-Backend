package com.Anemoi.InvestorRelation.ClientLineItem;

public class ClientDetails {

	private String lineItemName;
	private String clientName;
	private String clientLineItemName;

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientLineItemName() {
		return clientLineItemName;
	}

	public void setClientLineItemName(String clientLineItemName) {
		this.clientLineItemName = clientLineItemName;
	}

	public ClientDetails(String lineItemName, String clientName, String clientLineItemName) {
		super();
		this.lineItemName = lineItemName;
		this.clientName = clientName;
		this.clientLineItemName = clientLineItemName;
	}

	public ClientDetails() {
		super();
	}

}
