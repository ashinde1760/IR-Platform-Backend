package com.Anemoi.InvestorRelation.Audithistory;

public class QueryConstant {
	

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";
	
	public static final String SELECT_USERNAME="SELECT firstName,lastName FROM  #$DataBaseName#$.dbo.userTable WHERE createdBy=?";

	public static final String INSERT_AUDITHISTORY_TABLE = "INSERT INTO #$DataBaseName#$.dbo.auditHistoryTable values(?,?,?,?,?)";

	public static final String SELECT_AUDITHISTORY="SELECT * FROM  #$DataBaseName#$.dbo.auditHistoryTable ";
	
//	public static final String SELECT_AUDITHISTORY="SELECT *, FORMAT(DATEADD(SECOND, [createdOn] / 1000, '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM  #$DataBaseName#$.dbo.auditHistoryTable";


}
