package com.Anemoi.InvestorRelation.BalanceSheet;

public class BalanceSheetQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String BALANCEID = "balanceid";

	public static final String LINE_ITEM = "lineItem";

	public static final String ALTERNATIVE_NAME = "alternativeName";

	public static final String INSERT_INTO_BALANCESHEET_FORM = "INSERT INTO INV_RELATIONS.dev.balanceSheetform values(?,?,?,?,?,?,?)";

	public static final String SELECT_BALANCESHEET_FORM_BY_ID = "SELECT * FROM INV_RELATIONS.dev.balanceSheetform where balanceid=?";

	public static final String SELECT_BALANCESHEET_FORM = "SELECT * FROM INV_RELATIONS.dev.balanceSheetform";

	public static final String UPDATE_BALANCESHEET_FORM = "UPDATE INV_RELATIONS.dev.balanceSheetform SET lineItem=?, alternativeName=? ,modifiedBy=?, modifiedOn=? WHERE balanceid=?";

	public static final String SELECT_LINEITEM2 = "SELECT  lineItem FROM INV_RELATIONS.dev.balanceSheetform WHERE NOT balanceid = ?";

	public static final String DELETE_BALANCESHEET_FORM_BY_ID = "DELETE INV_RELATIONS.dev.balanceSheetform WHERE balanceid=?";

	public static final String SELECT_LINEITEM = "SELECT  lineItem FROM INV_RELATIONS.dev.balanceSheetform";

	public static final String SELECT_LINEITEMFORNOTIFICATION = "SELECT * FROM INV_RELATIONS.dev.balanceSheetform WHERE createdOn >= ? AND createdOn < ?";

}
