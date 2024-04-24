package com.Anemoi.InvestorRelation.IncomeStatement;

public class IncomeStatementQuaryConstatnt {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INCOMEID = "incomeid";

	public static final String LINEITEM = "lineItem";

	public static final String ALTERNATIVENAME = "alternativeName";

	public static final String INSERT_INTO_INCOMESTATEMENT = "INSERT INTO #$DataBaseName#$.dev.incomestatement values(?,?,?,?,?,?,?)";

	public static final String SELECT_INCOMESTATEMENT_BY_ID = "SELECT * FROM #$DataBaseName#$.dev.incomestatement where incomeid=?";

	public static final String SELECT_INCOMESTATEMENT = "SELECT *FROM #$DataBaseName#$.dev.incomestatement";

	public static final String UPDATE_INCOMESTATEMENT = "UPDATE #$DataBaseName#$.dev.incomestatement SET lineItem=?, alternativeName=? ,modifiedBy=?,modifiedOn=? WHERE incomeid=?";

	public static final String DELETE_INCOMESTATEMENT_BY_ID = "DELETE #$DataBaseName#$.dev.incomestatement WHERE incomeid=?";

	public static final String SELECT_LINEITEM = "SELECT lineItem FROM #$DataBaseName#$.dev.incomestatement";

	public static final String SELECT_LINEITEM2 = "SELECT lineItem FROM #$DataBaseName#$.dev.incomestatement  WHERE NOT incomeid = ?";

	
	public static final String SELECT_LINEITEMFORNOTIFICATION = "SELECT * FROM #$DataBaseName#$.dev.incomestatement WHERE createdOn >= ? AND createdOn < ?";

}
