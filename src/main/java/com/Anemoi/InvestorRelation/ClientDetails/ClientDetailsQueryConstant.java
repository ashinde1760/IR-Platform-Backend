package com.Anemoi.InvestorRelation.ClientDetails;

public class ClientDetailsQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INSERT_INTO_CLIENTDETAILS = "INSERT INTO #$DataBaseName#$.dev.clientDetails values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_CLIENTDETAILS = "SELECT * FROM #$DataBaseName#$.dev.clientDetails";

	public static final String SELECT_CLIENTDETAILS_BYCLIENTID = "SELECT * FROM #$DataBaseName#$.dev.clientDetails where clientId=?";

	public static final String SELECT_CLIENTDETAILS_BYCLIENTNAME = "SELECT * FROM #$DataBaseName#$.dev.clientDetails where clientName=?";

	public static final String INSERT_CLIENTDETAILS_BYPROJECTCODE = "INSERT INTO #$DataBaseName#$.dev.clientcodedetails values(?,?,?,?)";

	public static final String SELECT_CLIENTDETAILS_BYPROJECTCODE = "SELECT * FROM #$DataBaseName#$.dev.clientcodedetails WHERE projectCode=? ";

	public static final String UPDATE_CLIENTDETAILS = "UPDATE #$DataBaseName#$.dev.clientDetails SET clientAddress=?,emailId=?,industry=?,suggestedPeers=?,assignAA=?,modifiedBy=?,modifiedOn=? where clientId=?";

	public static final String DELETE_CLIENTDETAILS = "DELETE #$DataBaseName#$.dev.clientDetails where clientId=?";

	public static final String SELECT_CLIENTNAME= "SELECT  clientName FROM  #$DataBaseName#$.dev.clientDetails";

	public static final String SELECT_INDUSRY_FORUPDATE= "SELECT  industry FROM  #$DataBaseName#$.dev.clientDetails where clientName=?  AND clientId <> ?";
	
	public static final String SELECT_CLIENTNAME_BYCLIENTNAME= "SELECT  clientName FROM  #$DataBaseName#$.dev.clientDetails where clientId=?" ;

	public static final String DELETE_CLIENTDETAILSFOR_NNPROCESS = "DELETE #$DataBaseName#$.dev.nonprocessFileTable where client=?";

	public static final String SELECT_USERNAME= "SELECT  firstName,lastName FROM  #$DataBaseName#$.dev.userTable WHERE email=?";

	public static final String SELECT_ANALSYSTADMIN= "SELECT  assignAA FROM  #$DataBaseName#$.dev.clientDetails where clientName=? ";

}
