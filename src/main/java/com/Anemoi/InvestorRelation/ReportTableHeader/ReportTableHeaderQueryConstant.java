package com.Anemoi.InvestorRelation.ReportTableHeader;

public class ReportTableHeaderQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String TABLEHEADERID = "tableHeaderId";

	public static final String TABLEHEADER_NAME = "tableHeaderName";

	public static final String DESCRIPTION = "description";

	public static final String TYPE = "type";

	public static final String INSERT_INTO_REPORTTABLE_HEADERTABLE = "INSERT INTO INV_RELATIONS.dev.reportTableHeader values(?,?,?,?,?)";

	public static final String SELECT_REPORTTABLE_HEADERLIST = "SELECT * FROM INV_RELATIONS.dev.reportTableHeader ORDER BY (createdOn) DESC ,RAND()";

	public static final String SELECT_TABLEHEADER_DETAILSBY_ID = "SELECT * FROM INV_RELATIONS.dev.reportTableHeader where tableHeaderId=?";

	public static final String DELETE_TABLEHEADER_DETAILSBY_ID = "DELETE INV_RELATIONS.dev.reportTableHeader where tableHeaderId=?";

	public static final String UPDATE_TABLEHEADER_DETAILSBY_ID = "UPDATE INV_RELATIONS.dev.reportTableHeader SET tableHeaderName=?,description=?,modifiedOn=? where tableHeaderId=?";

}
