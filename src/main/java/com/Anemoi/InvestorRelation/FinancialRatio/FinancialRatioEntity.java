package com.Anemoi.InvestorRelation.FinancialRatio;

public class FinancialRatioEntity {

	private String financialid;
	
	private String clientName;

	private String formulaName;

	private String formula;
	
	private String createdBy;
	
	private long createdOn;

	public String getFinancialid() {
		return financialid;
	}

	public void setFinancialid(String financialid) {
		this.financialid = financialid;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getFormulaName() {
		return formulaName;
	}

	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
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

	public FinancialRatioEntity(String financialid, String clientName, String formulaName, String formula,
			String createdBy, long createdOn) {
		super();
		this.financialid = financialid;
		this.clientName = clientName;
		this.formulaName = formulaName;
		this.formula = formula;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public FinancialRatioEntity() {
		super();
	}
	
	

}