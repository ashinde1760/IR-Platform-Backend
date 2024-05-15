package com.Anemoi.InvestorRelation.AnalystLineItem;

public class AnalystLineItemQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String ANALYSTLINEID = "analystLineId";

	public static final String ANALYSTNAME = "analystName";

	public static final String LINEITEMNAME = "lineItemName";

	public static final String ANALYSTLINEITEMNAME = "analystLineItemName";

	public static final String ANALYSTTABLEHEADERNAME = "analystTableHeaderName";

	public static final String MASTERTABLESOURCE = "masterTableSource";

	public static final String CREATEDON = "createdOn";
	
	public static final String CREATEDBY = "createdBy";

	public static final String INSERT_INTO_ANALYSTLINE_ITEM = "INSERT INTO #$DataBaseName#$.dbo.analystLineItem values(?,?,?,?,?,?,?,?)";

	public static final String SELECT_ANALYSTLINEITEM_BY_ID = "SELECT * FROM #$DataBaseName#$.dbo.analystLineItem where analystLineId=?";

	public static final String SELECT_ANALYSTLINEITEM = "SELECT * FROM #$DataBaseName#$.dbo.analystLineItem";

	public static final String SELECT_ANALYSTLINEITEM_BY_ANALYSTNAME = "SELECT * FROM #$DataBaseName#$.dbo.analystLineItem where analystName=? and masterTableSource=?";

	public static final String UPDATE_ANALYSTLINEITEM = "UPDATE #$DataBaseName#$.dbo.analystLineItem SET lineItemName=? where analystName=? and analystLineItemName=?";

	public static final String UPDATE_ANALYSTLINEITEM_BYEXELSHEET = "UPDATE #$DataBaseName#$.dbo.analystLineItem SET lineItemName=?,analystTableHeaderName=? ,masterTableSource=? where analystName=? and analystLineItemName=?";

	public static final String SELECT_FOR_MULTIPLE_ANALYSTLINETEM = "SELECT  lineItemName FROM  #$DataBaseName#$.dbo.analystLineItem where analystName=?";

	public static final String SELECT_ANALYSTLINETEMNAME = "SELECT  analystLineItemName FROM  #$DataBaseName#$.dbo.analystLineItem where analystName=?";

	public static final String SELECT_ANALYSTLINETEMNAME2 = "SELECT  analystLineItemName FROM  #$DataBaseName#$.dbo.analystLineItem where analystName=? and NOT analystLineId =?";

	public static final String UPDATE_ANALYSTLINE_ITEMNOMENCLURE = "UPDATE #$DataBaseName#$.dbo.analystLineItem SET analystName=?, analystLineItemName=? ,analystTableHeaderName=? ,masterTableSource=? WHERE analystLineId=?";

	public static final String SELECT_ANALYSTNAME = "SELECT analystName FROM #$DataBaseName#$.dbo.analystDetails ";

	public static final String SELECT_CURRENTDATEMAPPING = "SELECT * FROM #$DataBaseName#$.dbo.analystLineItem WHERE createdOn >= ? AND createdOn < ? AND lineItemName IS NOT NULL";

	public static final String SELECT_CURRENTDATEADDLINEITEM = "SELECT * FROM #$DataBaseName#$.dbo.analystLineItem WHERE createdOn >= ? AND createdOn < ? AND lineItemName IS NULL";

}
