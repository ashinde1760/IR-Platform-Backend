package com.Anemoi.InvestorRelation.AnalystLineItem;

public class AnalystDetails {

	private String lineItemName;
	private String analystName;
	private String analystLineItemName;

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	public String getAnalystName() {
		return analystName;
	}

	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}

	public String getAnalystLineItemName() {
		return analystLineItemName;
	}

	public void setAnalystLineItemName(String analystLineItemName) {
		this.analystLineItemName = analystLineItemName;
	}

	public AnalystDetails(String lineItemName, String analystName, String analystLineItemName) {
		super();
		this.lineItemName = lineItemName;
		this.analystName = analystName;
		this.analystLineItemName = analystLineItemName;
	}

	public AnalystDetails() {
		super();
	}

}
