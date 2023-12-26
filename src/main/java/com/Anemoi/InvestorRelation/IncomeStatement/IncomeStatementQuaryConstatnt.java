package com.Anemoi.InvestorRelation.IncomeStatement;

public class IncomeStatementQuaryConstatnt {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INCOMEID = "incomeid";

	public static final String LINEITEM = "lineItem";

	public static final String ALTERNATIVENAME = "alternativeName";

	public static final String INSERT_INTO_INCOMESTATEMENT = "INSERT INTO INV_RELATIONS.dev.incomestatement values(?,?,?,?,?,?,?)";

	public static final String SELECT_INCOMESTATEMENT_BY_ID = "SELECT * FROM INV_RELATIONS.dev.incomestatement where incomeid=?";

	public static final String SELECT_INCOMESTATEMENT = "SELECT *FROM INV_RELATIONS.dev.incomestatement";

	public static final String UPDATE_INCOMESTATEMENT = "UPDATE INV_RELATIONS.dev.incomestatement SET lineItem=?, alternativeName=? ,modifiedBy=?,modifiedOn=? WHERE incomeid=?";

	public static final String DELETE_INCOMESTATEMENT_BY_ID = "DELETE INV_RELATIONS.dev.incomestatement WHERE incomeid=?";

	public static final String SELECT_LINEITEM = "SELECT lineItem FROM INV_RELATIONS.dev.incomestatement";

	public static final String SELECT_LINEITEM2 = "SELECT lineItem FROM INV_RELATIONS.dev.incomestatement  WHERE NOT incomeid = ?";

	
	public static final String SELECT_LINEITEMFORNOTIFICATION = "SELECT * FROM INV_RELATIONS.dev.incomestatement WHERE createdOn >= ? AND createdOn < ?";

}
