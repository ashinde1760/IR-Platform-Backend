package com.Anemoi.InvestorRelation.DataIngestion;

public class DataIngestionQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	// public static final String INGESTIONID = "fileId";

	public static final String CLIENT = "client";

	public static final String DOCUMENTTYPE = "documentType";

	public static final String ANALYST = "analystName";

	public static final String REPORTFROM = "reportFrom";

	public static final String REPORTTO = "reportTo";

	public static final String FILENAME = "fileName";

	public static final String FILETYPE = "fileType";

	public static final String INSERT_INTO_NONPROCESSFILETABLES = "INSERT INTO #$DataBaseName#$.dbo.nonprocessFileTable values(?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_HASHCODE = "SELECT fileName FROM #$DataBaseName#$.dbo.nonprocessFileTable WHERE client=?";

	public static final String SELECT_NPFILEID = "SELECT npFileId FROM #$DataBaseName#$.dbo.nonprocessFileTable WHERE  fileName=? and client=?";

	public static final String SELECT_NONPROCESS_FILEIDS = "SELECT * FROM #$DataBaseName#$.dbo.nonprocessFileTable WHERE client=? and status=?";

	public static final String DELETE_NONPROCESS_FILEID = "DELETE #$DataBaseName#$.dbo.nonprocessFileTable WHERE npFileId=?";

	public static final String SELECT_NONPROCESS_FILEIDS_BYNPFILEID = "SELECT * FROM #$DataBaseName#$.dbo.nonprocessFileTable WHERE npFileId=?";

//	public static final String SELECT_NONPROCESSING_FILES = "SELECT * FROM #$DataBaseName#$.dbo.nonprocessFileTable WHERE status=?";

	
  	public static final String SELECT_NONPROCESSING_FILES = "select #$DataBaseName#$.dbo.nonprocessFileTable.*,#$DataBaseName#$.dbo.clientDetails.suggestedPeers AS peers from #$DataBaseName#$.dbo.nonprocessFileTable LEFT OUTER  join #$DataBaseName#$.dbo.clientDetails on #$DataBaseName#$.dbo.nonprocessFileTable.client=#$DataBaseName#$.dbo.clientDetails.clientName WHERE status=? ORDER BY createdOn DESC";
	public static final String INSERT_INTO_DATA_INGESTION = "INSERT INTO #$DataBaseName#$.dbo.dataIngestion values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_STATUSNONPROCESSFILES = "UPDATE #$DataBaseName#$.dbo.nonprocessFileTable SET status=? WHERE npFileId=?";

	public static final String SELECT_OVERRIDE_DETAILS = "SELECT fileId FROM #$DataBaseName#$.dbo.dataIngestion WHERE client=? AND documentType=? AND analystName=? OR peerName=? AND reportType=? AND reportDate=?";

	public static final String SELECT_FILEID_OVERWRITE = "SELECT fileId FROM #$DataBaseName#$.dbo.dataIngestion WHERE npfileId=?";

	public static final String SELECT_FIELD_ID = "SELECT tableId FROM #$DataBaseName#$.dbo.tableList WHERE fileId=?";

	public static final String SELECT_DATAINGESTION_FILEDETAILS = "SELECT * FROM #$DataBaseName#$.dbo.dataIngestion ORDER BY (fileId) DESC ,RAND()";

//	public static final String SELECT_DATAINGESTION_FILEDETAILS = "SELECT *, FORMAT(DATEADD(SECOND, [createdOn] / 1000, '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM #$DataBaseName#$.dbo.dataIngestion ORDER BY [createdOn] ASC, NEWID()";

	public static final String SELECT_DATAINGESTION_FILEDETAILS_BYFILEID = "SELECT * FROM #$DataBaseName#$.dbo.dataIngestion where fileId=?";

	public static final String UPDATE_REPORTDATE = "UPDATE #$DataBaseName#$.dbo.dataIngestion SET reportDate=? where fileId=?";

	public static final String UPDATE_STATUSFILEEXTRACT = "UPDATE #$DataBaseName#$.dbo.dataIngestion SET status=? WHERE fileId=?";

	public static final String DELETE_DATAINGESTION_FILEDETAILS = "DELETE #$DataBaseName#$.dbo.dataIngestion WHERE fileId=?";

	public static final String DELETE_FORCAST = "DELETE #$DataBaseName#$.dbo.forecast WHERE fileId=?";

	public static final String DELETE_DATAINGESTION_MAPPING_TABLE = "delete FROM #$DataBaseName#$.dbo.dataIngestionMappingtable WHERE fieldId IN (SELECT field_Id FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE tableId IN (select tableId from #$DataBaseName#$.dbo.tableList where fileId=?))";

	public static final String DELETE_DATAINGESTION_TBALE_METADATA= "DELETE FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE tableId IN (SELECT tableId FROM #$DataBaseName#$.dbo.tableList WHERE fileId = ?);";
	
	public static final String DELETE_TABLELIST = "DELETE #$DataBaseName#$.dbo.tableList WHERE fileId=?";

	public static final String SELECT_DATAINGESTION_BYFILEID = "SELECT * FROM #$DataBaseName#$.dbo.dataIngestion where fileId=?";

	public static final String SELECT_MAX_FILEID = "select MAX(fileId) from #$DataBaseName#$.dbo.dataIngestion";

	public static final String SELECT_MAX_BROKENID = "select MAX(brokenFileId) from #$DataBaseName#$.dbo.brokenfiledetails";

	public static final String SELECT_TABLENAME = "SELECT tableName from #$DataBaseName#$.dbo.tableList where tableId=?";

	public static final String INSERT_FORCASTTABLE = "INSERT INTO #$DataBaseName#$.dbo.forecast values(?,?,?,?,?)";

	public static final String INSERT_TABLELIST = "INSERT INTO #$DataBaseName#$.dbo.tableList values(?,?,?,?,?)";

	public static final String SELECT_TABLEID = "SELECT * FROM #$DataBaseName#$.dbo.tableList";

	public static final String SELECT_TABLEDATA_BYTABLEID = "SELECT * FROM #$DataBaseName#$.dbo.tableList WHERE tableId=?";

	// DataIngestion Table queries

	public static final String INSERT_INTO_DATAINGESTION_TABLE_DATA = "INSERT INTO #$DataBaseName#$.dbo.dataIngestionaTabelMetaData values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_TABLEID_BYFILEID = "SELECT DISTINCT tableId,tableName,score from #$DataBaseName#$.dbo.tableList where fileId=?";

	public static final String SELECT_MAX_SCORE = "SELECT MAX(score) FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String SELECT_DISCOVERS_DATES_BYTABLEID = "SELECT C2,C3 FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String SELECT_DISCOVERS_DATES_DETAILS = "SELECT * FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE C2=? and tableId=?";

	public static final String SELECT_DATAINGESTION_TABLEDETAILS = "select #$DataBaseName#$.dbo.dataIngestionaTabelMetaData.*,#$DataBaseName#$.dbo.analystLineItem.lineItemName AS masterLineItem from #$DataBaseName#$.dbo.dataIngestionaTabelMetaData LEFT OUTER  join #$DataBaseName#$.dbo.analystLineItem on #$DataBaseName#$.dbo.dataIngestionaTabelMetaData.C1=#$DataBaseName#$.dbo.analystLineItem.analystLineItemName and analystName=?";

	public static final String SELECT_DATAINGESTION_TABLEDETAILSBY_CLIENTNAME = "select #$DataBaseName#$.dbo.dataIngestionaTabelMetaData.*,#$DataBaseName#$.dbo.clientLineItem.lineItemName AS masterLineItem from #$DataBaseName#$.dbo.dataIngestionaTabelMetaData LEFT OUTER  join #$DataBaseName#$.dbo.clientLineItem on #$DataBaseName#$.dbo.dataIngestionaTabelMetaData.C1=#$DataBaseName#$.dbo.clientLineItem.clientLineItemName and clientName=?";

	public static final String DELETE_TABLEDETAILS_BYTABLEID = "DELETE #$DataBaseName#$.dbo.tableList WHERE tableId=?";

	public static final String UPDATE_DATAINGESTION_TABLEDETAILS = "UPDATE #$DataBaseName#$.dbo.dataIngestionaTabelMetaData  SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=?,C25=? WHERE field_Id=?";

	public static final String SELECT_MAX_TABLEID = "select MAX(tableId) from #$DataBaseName#$.dbo.tableList";

	public static final String SELECT_TABLEDATA_BYFIELDID = "SELECT * FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE field_Id=?";

	public static final String DELETE_TABLEDATA_BY_FIELDID = "DELETE #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE field_Id=?";

	public static final String UPDATE_TABLENAME_BYTABLEID = "UPDATE #$DataBaseName#$.dbo.tableList SET tableName=? where tableId=?";

	public static final String SELECT_KEYWORD = "SELECT * FROM #$DataBaseName#$.dbo.keywordlist";

	public static final String SELECT_TABLEDATA_BYTABLEID_DOWNLOAD = "SELECT * FROM #$DataBaseName#$.dbo.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String MERGE_TABLE_BYTABLEID = "UPDATE #$DataBaseName#$.dbo.dataIngestionaTabelMetaData  SET tableId=? where tableId=?";

	public static final String LEFTSHIFT_TABLEDATA = "UPDATE #$DataBaseName#$.dbo.dataIngestionaTabelMetaData  SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=? WHERE field_Id=?";

	public static final String RIGHTSHIFT_TABLEDATA = "UPDATE #$DataBaseName#$.dbo.dataIngestionaTabelMetaData SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=?,C25=? WHERE field_Id=?";

	public static final String DELETE_TABLEID_BYTABLELIST = "DELETE #$DataBaseName#$.dbo.tableList WHERE tableId=?";

	public static final String SELECT_FILEID_BYTABLEID = "SELECT DISTINCT #$DataBaseName#$.dbo.dataIngestion.fileId from #$DataBaseName#$.dbo.dataIngestion join #$DataBaseName#$.dbo.tableList on  #$DataBaseName#$.dbo.dataIngestion.fileId=#$DataBaseName#$.dbo.tableList.fileId join #$DataBaseName#$.dbo.dataIngestionaTabelMetaData on #$DataBaseName#$.dbo.tableList.tableId=#$DataBaseName#$.dbo.dataIngestionaTabelMetaData.tableId where #$DataBaseName#$.dbo.dataIngestionaTabelMetaData.tableId=?";
	// DataIngestion Mapping table queries

	public static final String INSERT_INTO_DATAINGESTION_MAPPINGTABLE = "INSERT INTO #$DataBaseName#$.dbo.dataIngestionMappingtable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_SHAREHOLDER_MEETING = "INSERT INTO #$DataBaseName#$.dbo.shareholdermeeting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_SHAREHOLDER_CONTACT_TABLE = "INSERT INTO #$DataBaseName#$.dbo.shareholderdataform values(?,?,?,?,?,?,?,?,?)";

}
