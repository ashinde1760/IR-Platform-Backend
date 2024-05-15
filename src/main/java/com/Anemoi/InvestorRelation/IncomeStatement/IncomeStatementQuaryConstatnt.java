package com.Anemoi.InvestorRelation.IncomeStatement;

public class IncomeStatementQuaryConstatnt {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INCOMEID = "incomeid";

	public static final String LINEITEM = "lineItem";

	public static final String ALTERNATIVENAME = "alternativeName";

	public static final String INSERT_INTO_INCOMESTATEMENT = "INSERT INTO #$DataBaseName#$.dbo.incomestatement values(?,?,?,?,?,?,?)";

	public static final String SELECT_INCOMESTATEMENT_BY_ID = "SELECT * FROM #$DataBaseName#$.dbo.incomestatement where incomeid=?";

	public static final String SELECT_INCOMESTATEMENT = "SELECT *FROM #$DataBaseName#$.dbo.incomestatement";

	public static final String UPDATE_INCOMESTATEMENT = "UPDATE #$DataBaseName#$.dbo.incomestatement SET lineItem=?, alternativeName=? ,modifiedBy=?,modifiedOn=? WHERE incomeid=?";

	public static final String DELETE_INCOMESTATEMENT_BY_ID = "DELETE #$DataBaseName#$.dbo.incomestatement WHERE incomeid=?";

	public static final String SELECT_LINEITEM = "SELECT lineItem FROM #$DataBaseName#$.dbo.incomestatement";

	public static final String SELECT_LINEITEM2 = "SELECT lineItem FROM #$DataBaseName#$.dbo.incomestatement  WHERE NOT incomeid = ?";

	
	public static final String SELECT_LINEITEMFORNOTIFICATION = "SELECT * FROM #$DataBaseName#$.dbo.incomestatement WHERE createdOn >= ? AND createdOn < ?";

}
