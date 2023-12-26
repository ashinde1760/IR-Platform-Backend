package com.Anemoi.InvestorRelation.CashFlow;

public class CashFlowQuaryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String CASHID = "cashId";

	public static final String LINE_ITEM = "lineItem";

	public static final String ALTERNATIVE_NAME = "alternativeName";

	public static final String INSERT_INTO_CASHFLOW = "INSERT INTO INV_RELATIONS.dev.cashflow values(?,?,?,?,?,?,?)";

	public static final String SELECT__CASHFLOW_BY_ID = "SELECT * FROM INV_RELATIONS.dev.cashflow where cashId=?";

	public static final String SELECT_CASHFLOW = "SELECT *FROM INV_RELATIONS.dev.cashflow";

	public static final String UPDATE_CASHFLOW = "UPDATE INV_RELATIONS.dev.cashflow SET lineItem=?, alternativeName=?,modifiedBy=?,modifiedOn=? WHERE cashId=?";

	public static final String DELETE_CASHFLOW_BY_ID = "DELETE INV_RELATIONS.dev.cashflow WHERE cashId=?";

	public static final String SELECT_LINEITEM = "SELECT lineItem FROM INV_RELATIONS.dev.cashflow";

	public static final String SELECT_LINEITEM2 = "SELECT lineItem FROM INV_RELATIONS.dev.cashflow WHERE NOT cashId = ?";

	
	public static final String SELECT_LINEITEMFORNOTIFICATION = "SELECT * FROM INV_RELATIONS.dev.cashflow WHERE createdOn >= ? AND createdOn < ?";

}
