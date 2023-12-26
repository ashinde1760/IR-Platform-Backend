package com.Anemoi.InvestorRelation.FinancialRatio;

public class FinancialRatioQuaryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String FINANCIALID = "financialid";
	
	public static final String CLIENTNAME="clientName";

	public static final String FORMULANAME = "formulaName";

	public static final String FORMULA = "formula";

	public static final String CREATEDBY = "createdBy";

	public static final String INSERT_INTO_FINANCIALRATIO = "INSERT INTO INV_RELATIONS.dev.financialRatio values(?,?,?,?,?,?)";

	public static final String SELECT__FINANCIALRATIO_BY_ID = "SELECT * FROM INV_RELATIONS.dev.financialRatio where financialid=?";

	public static final String SELECT__FORMULANAME= "SELECT formulaName FROM INV_RELATIONS.dev.financialRatio where clientName=?";

	public static final String SELECT_FINANCIALRATIO = "SELECT *FROM INV_RELATIONS.dev.financialRatio";

	public static final String UPDATE_FINANCIALRATIO = "UPDATE INV_RELATIONS.dev.financialRatio SET clientName=?,formulaName=?, formula=?,createdBy=?,createdOn=? WHERE financialid=?";

	public static final String DELETE_FINANCIALRATIO_BY_ID = "DELETE INV_RELATIONS.dev.financialRatio WHERE financialid=?";

}
