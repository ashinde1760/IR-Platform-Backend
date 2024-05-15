package com.Anemoi.InvestorRelation.ReportTableHeader;

public class ReportTableHeaderQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String TABLEHEADERID = "tableHeaderId";

	public static final String TABLEHEADER_NAME = "tableHeaderName";

	public static final String DESCRIPTION = "description";

	public static final String TYPE = "type";

	public static final String INSERT_INTO_REPORTTABLE_HEADERTABLE = "INSERT INTO #$DataBaseName#$.dbo.reportTableHeader values(?,?,?,?,?)";

	public static final String SELECT_REPORTTABLE_HEADERLIST = "SELECT * FROM #$DataBaseName#$.dbo.reportTableHeader ORDER BY (createdOn) DESC ,RAND()";

	public static final String SELECT_TABLEHEADER_DETAILSBY_ID = "SELECT * FROM #$DataBaseName#$.dbo.reportTableHeader where tableHeaderId=?";

	public static final String DELETE_TABLEHEADER_DETAILSBY_ID = "DELETE #$DataBaseName#$.dbo.reportTableHeader where tableHeaderId=?";

	public static final String UPDATE_TABLEHEADER_DETAILSBY_ID = "UPDATE #$DataBaseName#$.dbo.reportTableHeader SET tableHeaderName=?,description=?,modifiedOn=? where tableHeaderId=?";

}
