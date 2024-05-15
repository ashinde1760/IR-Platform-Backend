package com.Anemoi.InvestorRelation.ClientLineItem;

public class ClientLineItemQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String CLIENTID = "clientLineId";

	public static final String CLIENTNAME = "clientName";

	public static final String LINEITEMNAME = "lineItemName";

	public static final String CLIENTLINEITEMNAME = "clientLineItemName";

	public static final String CLIENTTABLEHEADERNAME = "clientTableHeaderName";

	public static final String MASTERTABLESOURCE = "masterTableSource";

	public static final String CREATEDON = "createdOn";
	
	public static final String CREATEDBY = "createdBy";

	public static final String INSERT_INTO_CLIENTLINE_ITEM = "INSERT INTO #$DataBaseName#$.dbo.clientLineItem values(?,?,?,?,?,?,?,?)";

	public static final String SELECT_CLIENTLINE_ITEM_BYID = "SELECT * FROM  #$DataBaseName#$.dbo.clientLineItem WHERE clientLineId=? ";

	public static final String SELECT_CLIENTLINE_LISTDETAILS = "SELECT * FROM  #$DataBaseName#$.dbo.clientLineItem ";

	public static final String SELECT_CLIENTTLINETEMNAME = "SELECT  clientLineItemName FROM  #$DataBaseName#$.dbo.clientLineItem where clientName=?";

	public static final String SELECT_CLIENTNAME = "SELECT  clientName FROM  #$DataBaseName#$.dbo.clientDetails ";

	public static final String SELECT_CLIENTTLINETEMNAME2 = "SELECT  clientLineItemName FROM  #$DataBaseName#$.dbo.clientLineItem where clientName=? and NOT clientLineId=? ";

	public static final String SELECT_CLIENTLINEITEM_BY_CLIENTNAME = "SELECT * FROM #$DataBaseName#$.dbo.clientLineItem where clientName=? and masterTableSource=?";

	public static final String SELECT_LINEITEM_NAME = "SELECT  lineItemName FROM  #$DataBaseName#$.dbo.clientLineItem where clientName=?";

	public static final String UPDATE_LINEITEMNAME_BYEXCELSHEET = "UPDATE #$DataBaseName#$.dbo.clientLineItem SET lineItemName=?,clientTableHeaderName=?,masterTableSource=? where clientName=? and clientLineItemName=?";

	public static final String UPDATE_LINEITEMNAME = "UPDATE #$DataBaseName#$.dbo.clientLineItem SET lineItemName=? where clientName=? and clientLineItemName=?";

	public static final String UPDATE_LINEITEMNAME_NOMECLURE = "UPDATE #$DataBaseName#$.dbo.clientLineItem SET clientName=?,clientLineItemName=?,clientTableHeaderName=?,masterTableSource=? WHERE clientLineId=?";

	public static final String SELECT_CURRENTDATE_CLIENTNAME = "SELECT clientName FROM #$DataBaseName#$.dbo.clientLineItem WHERE createdOn >= ? AND createdOn < ? ";

	  public static final String SELECT_MASTERTABLENAMEWHENADDCLIENTLITEITEMNAME = "SELECT * FROM #$DataBaseName#$.dbo.clientLineItem WHERE createdOn >= ? AND createdOn < ? AND lineItemName IS  NULL AND clientName = ?";

	  public static final String SELECT_WHENMAPPINGCLIENTLINEITEM_SELECTMASTERTABLENAME = "SELECT * FROM #$DataBaseName#$.dbo.clientLineItem WHERE createdOn >= ? AND createdOn < ? AND lineItemName IS NOT NULL AND clientName = ?";

}
