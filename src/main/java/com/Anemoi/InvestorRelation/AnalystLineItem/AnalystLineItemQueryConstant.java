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

	public static final String INSERT_INTO_ANALYSTLINE_ITEM = "INSERT INTO INV_RELATIONS.dev.analystLineItem values(?,?,?,?,?,?,?,?)";

	public static final String SELECT_ANALYSTLINEITEM_BY_ID = "SELECT * FROM INV_RELATIONS.dev.analystLineItem where analystLineId=?";

	public static final String SELECT_ANALYSTLINEITEM = "SELECT * FROM INV_RELATIONS.dev.analystLineItem";

	public static final String SELECT_ANALYSTLINEITEM_BY_ANALYSTNAME = "SELECT * FROM INV_RELATIONS.dev.analystLineItem where analystName=? and masterTableSource=?";

	public static final String UPDATE_ANALYSTLINEITEM = "UPDATE INV_RELATIONS.dev.analystLineItem SET lineItemName=? where analystName=? and analystLineItemName=?";

	public static final String UPDATE_ANALYSTLINEITEM_BYEXELSHEET = "UPDATE INV_RELATIONS.dev.analystLineItem SET lineItemName=?,analystTableHeaderName=? ,masterTableSource=? where analystName=? and analystLineItemName=?";

	public static final String SELECT_FOR_MULTIPLE_ANALYSTLINETEM = "SELECT  lineItemName FROM  INV_RELATIONS.dev.analystLineItem where analystName=?";

	public static final String SELECT_ANALYSTLINETEMNAME = "SELECT  analystLineItemName FROM  INV_RELATIONS.dev.analystLineItem where analystName=?";

	public static final String SELECT_ANALYSTLINETEMNAME2 = "SELECT  analystLineItemName FROM  INV_RELATIONS.dev.analystLineItem where analystName=? and NOT analystLineId =?";

	public static final String UPDATE_ANALYSTLINE_ITEMNOMENCLURE = "UPDATE INV_RELATIONS.dev.analystLineItem SET analystName=?, analystLineItemName=? ,analystTableHeaderName=? ,masterTableSource=? WHERE analystLineId=?";

	public static final String SELECT_ANALYSTNAME = "SELECT analystName FROM INV_RELATIONS.dev.analystDetails ";

	public static final String SELECT_CURRENTDATEMAPPING = "SELECT * FROM INV_RELATIONS.dev.analystLineItem WHERE createdOn >= ? AND createdOn < ? AND lineItemName IS NOT NULL";

	public static final String SELECT_CURRENTDATEADDLINEITEM = "SELECT * FROM INV_RELATIONS.dev.analystLineItem WHERE createdOn >= ? AND createdOn < ? AND lineItemName IS NULL";

}
