package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

public class ShareHolderDataFromQuaryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String SHAREID = "shareid";

	public static final String CLIENTNAME = "clientName";

	public static final String PORTFOLIOID = "portfolioid";

	public static final String FOLIO = "folio";

	public static final String SHAREHOLDERNAME = "shareholdername";

	public static final String HOLDINGVALUE = "holdingvalue";

	public static final String HOLDINGPERCENTAGE = "holdingpercentage";
	
	public static final String HOLDINGCOST="holdingCost";

	public static final String MINORCODE = "minorcode";
	
	public static final String FUNDGROUP="fundGroup";

	public static final String DATE = "date";

	public static final String INSERT_INTO_SHAREHOLDERDATAFORM = "INSERT INTO #$DataBaseName#$.dbo.shareholderdataform values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_CLIENTNAME= "SELECT  clientName FROM  #$DataBaseName#$.dbo.clientDetails";

	public static final String SELECT_SHAREHOLDERNAME= "SELECT  name FROM  #$DataBaseName#$.dbo.shareholdercontact";

	public static final String SELECT__SHAREHOLDERDATAFORM_BY_ID = "SELECT * FROM #$DataBaseName#$.dbo.shareholderdataform where shareid=?";

	public static final String SELECT_SHAREHOLDERDATAFORM = "SELECT *FROM #$DataBaseName#$.dbo.shareholderdataform";

//	public static final String SELECT_SHAREHOLDERDATAFORM = "SELECT *, FORMAT(DATEADD(SECOND, [date] / 1000, '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM #$DataBaseName#$.dbo.shareholderdataform";
	
	public static final String SELECT_DATE = "SELECT date FROM #$DataBaseName#$.dbo.shareholderdataform where clientName=?";

	public static final String UPDATE_SHAREHOLDERDATAFORM = "UPDATE #$DataBaseName#$.dbo.shareholderdataform SET clientName=?,  folio=?,shareholdername=?,holdingvalue=?,holdingpercentage=?,holdingCost=?,minorcode=?,fundGroup=?,date=?,modifiedBy=?,modifiedOn=? WHERE shareid=?";

	public static final String DELETE_SHAREHOLDERDATAFORM_BY_ID = "DELETE #$DataBaseName#$.dbo.shareholderdataform WHERE shareid=?";

	
	public static final String INSERT_INTO_MINORCODETABLE = "INSERT INTO #$DataBaseName#$.dbo.minorcodelistTable values(?,?)";

	public static final String SELECT_MINORCODE_LIST="SELECT * FROM #$DataBaseName#$.dbo.minorcodelistTable";
	public static final String SELECT_CURRENTDATE_DATA = "SELECT * FROM #$DataBaseName#$.dbo.shareholderdataform WHERE createdOn >= ? AND createdOn < ?";

	public static final String INSERT_INTO_MAILDATA = "INSERT INTO #$DataBaseName#$.dbo.mailDataTable values(?,?,?,?,?)";

	
}
