package com.Anemoi.InvestorRelation.ClientDetails;

public class ClientDetailsQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INSERT_INTO_CLIENTDETAILS = "INSERT INTO INV_RELATIONS.dev.clientDetails values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_CLIENTDETAILS = "SELECT * FROM INV_RELATIONS.dev.clientDetails";

	public static final String SELECT_CLIENTDETAILS_BYCLIENTID = "SELECT * FROM INV_RELATIONS.dev.clientDetails where clientId=?";

	public static final String SELECT_CLIENTDETAILS_BYCLIENTNAME = "SELECT * FROM INV_RELATIONS.dev.clientDetails where clientName=?";

	public static final String INSERT_CLIENTDETAILS_BYPROJECTCODE = "INSERT INTO INV_RELATIONS.dev.clientcodedetails values(?,?,?,?)";

	public static final String SELECT_CLIENTDETAILS_BYPROJECTCODE = "SELECT * FROM INV_RELATIONS.dev.clientcodedetails WHERE projectCode=? ";

	public static final String UPDATE_CLIENTDETAILS = "UPDATE INV_RELATIONS.dev.clientDetails SET clientAddress=?,emailId=?,industry=?,suggestedPeers=?,assignAA=?,modifiedBy=?,modifiedOn=? where clientId=?";

	public static final String DELETE_CLIENTDETAILS = "DELETE INV_RELATIONS.dev.clientDetails where clientId=?";

	public static final String SELECT_CLIENTNAME= "SELECT  clientName FROM  INV_RELATIONS.dev.clientDetails";

	public static final String SELECT_INDUSRY_FORUPDATE= "SELECT  industry FROM  INV_RELATIONS.dev.clientDetails where clientName=?  AND clientId <> ?";
	
	public static final String SELECT_CLIENTNAME_BYCLIENTNAME= "SELECT  clientName FROM  INV_RELATIONS.dev.clientDetails where clientId=?" ;

	public static final String DELETE_CLIENTDETAILSFOR_NNPROCESS = "DELETE INV_RELATIONS.dev.nonprocessFileTable where client=?";

	public static final String SELECT_USERNAME= "SELECT  firstName,lastName FROM  INV_RELATIONS.dev.userTable WHERE email=?";

	public static final String SELECT_ANALSYSTADMIN= "SELECT  assignAA FROM  INV_RELATIONS.dev.clientDetails where clientName=? ";

}
