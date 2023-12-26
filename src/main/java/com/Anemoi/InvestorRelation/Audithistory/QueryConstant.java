package com.Anemoi.InvestorRelation.Audithistory;

public class QueryConstant {
	

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";
	
	public static final String SELECT_USERNAME="SELECT firstName,lastName FROM  INV_RELATIONS.dev.userTable WHERE createdBy=?";

	public static final String INSERT_AUDITHISTORY_TABLE = "INSERT INTO INV_RELATIONS.dev.auditHistoryTable values(?,?,?,?,?)";

	public static final String SELECT_AUDITHISTORY="SELECT * FROM  INV_RELATIONS.dev.auditHistoryTable ";
	
//	public static final String SELECT_AUDITHISTORY="SELECT *, FORMAT(DATEADD(SECOND, [createdOn] / 1000, '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM  INV_RELATIONS.dev.auditHistoryTable";


}
