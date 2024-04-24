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

	public static final String INSERT_INTO_NONPROCESSFILETABLES = "INSERT INTO #$DataBaseName#$.dev.nonprocessFileTable values(?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_HASHCODE = "SELECT fileName FROM #$DataBaseName#$.dev.nonprocessFileTable WHERE client=?";

	public static final String SELECT_NPFILEID = "SELECT npFileId FROM #$DataBaseName#$.dev.nonprocessFileTable WHERE  fileName=? and client=?";

	public static final String SELECT_NONPROCESS_FILEIDS = "SELECT * FROM #$DataBaseName#$.dev.nonprocessFileTable WHERE client=? and status=?";

	public static final String DELETE_NONPROCESS_FILEID = "DELETE #$DataBaseName#$.dev.nonprocessFileTable WHERE npFileId=?";

	public static final String SELECT_NONPROCESS_FILEIDS_BYNPFILEID = "SELECT * FROM #$DataBaseName#$.dev.nonprocessFileTable WHERE npFileId=?";

//	public static final String SELECT_NONPROCESSING_FILES = "SELECT * FROM #$DataBaseName#$.dev.nonprocessFileTable WHERE status=?";

	
  	public static final String SELECT_NONPROCESSING_FILES = "select #$DataBaseName#$.dev.nonprocessFileTable.*,#$DataBaseName#$.dev.clientDetails.suggestedPeers AS peers from #$DataBaseName#$.dev.nonprocessFileTable LEFT OUTER  join #$DataBaseName#$.dev.clientDetails on #$DataBaseName#$.dev.nonprocessFileTable.client=#$DataBaseName#$.dev.clientDetails.clientName WHERE status=? ORDER BY createdOn DESC";
	public static final String INSERT_INTO_DATA_INGESTION = "INSERT INTO #$DataBaseName#$.dev.dataIngestion values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_STATUSNONPROCESSFILES = "UPDATE #$DataBaseName#$.dev.nonprocessFileTable SET status=? WHERE npFileId=?";

	public static final String SELECT_OVERRIDE_DETAILS = "SELECT fileId FROM #$DataBaseName#$.dev.dataIngestion WHERE client=? AND documentType=? AND analystName=? OR peerName=? AND reportType=? AND reportDate=?";

	public static final String SELECT_FILEID_OVERWRITE = "SELECT fileId FROM #$DataBaseName#$.dev.dataIngestion WHERE npfileId=?";

	public static final String SELECT_FIELD_ID = "SELECT tableId FROM #$DataBaseName#$.dev.tableList WHERE fileId=?";

	public static final String SELECT_DATAINGESTION_FILEDETAILS = "SELECT * FROM #$DataBaseName#$.dev.dataIngestion ORDER BY (fileId) DESC ,RAND()";

//	public static final String SELECT_DATAINGESTION_FILEDETAILS = "SELECT *, FORMAT(DATEADD(SECOND, [createdOn] / 1000, '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM #$DataBaseName#$.dev.dataIngestion ORDER BY [createdOn] ASC, NEWID()";

	public static final String SELECT_DATAINGESTION_FILEDETAILS_BYFILEID = "SELECT * FROM #$DataBaseName#$.dev.dataIngestion where fileId=?";

	public static final String UPDATE_REPORTDATE = "UPDATE #$DataBaseName#$.dev.dataIngestion SET reportDate=? where fileId=?";

	public static final String UPDATE_STATUSFILEEXTRACT = "UPDATE #$DataBaseName#$.dev.dataIngestion SET status=? WHERE fileId=?";

	public static final String DELETE_DATAINGESTION_FILEDETAILS = "DELETE #$DataBaseName#$.dev.dataIngestion WHERE fileId=?";

	public static final String DELETE_FORCAST = "DELETE #$DataBaseName#$.dev.forecast WHERE fileId=?";

	public static final String DELETE_DATAINGESTION_MAPPING_TABLE = "delete FROM #$DataBaseName#$.dev.dataIngestionMappingtable WHERE fieldId IN (SELECT field_Id FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE tableId IN (select tableId from #$DataBaseName#$.dev.tableList where fileId=?))";

	public static final String DELETE_DATAINGESTION_TBALE_METADATA= "DELETE FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE tableId IN (SELECT tableId FROM #$DataBaseName#$.dev.tableList WHERE fileId = ?);";
	
	public static final String DELETE_TABLELIST = "DELETE #$DataBaseName#$.dev.tableList WHERE fileId=?";

	public static final String SELECT_DATAINGESTION_BYFILEID = "SELECT * FROM #$DataBaseName#$.dev.dataIngestion where fileId=?";

	public static final String SELECT_MAX_FILEID = "select MAX(fileId) from #$DataBaseName#$.dev.dataIngestion";

	public static final String SELECT_MAX_BROKENID = "select MAX(brokenFileId) from #$DataBaseName#$.dev.brokenfiledetails";

	public static final String SELECT_TABLENAME = "SELECT tableName from #$DataBaseName#$.dev.tableList where tableId=?";

	public static final String INSERT_FORCASTTABLE = "INSERT INTO #$DataBaseName#$.dev.forecast values(?,?,?,?,?)";

	public static final String INSERT_TABLELIST = "INSERT INTO #$DataBaseName#$.dev.tableList values(?,?,?,?,?)";

	public static final String SELECT_TABLEID = "SELECT * FROM #$DataBaseName#$.dev.tableList";

	public static final String SELECT_TABLEDATA_BYTABLEID = "SELECT * FROM #$DataBaseName#$.dev.tableList WHERE tableId=?";

	// DataIngestion Table queries

	public static final String INSERT_INTO_DATAINGESTION_TABLE_DATA = "INSERT INTO #$DataBaseName#$.dev.dataIngestionaTabelMetaData values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_TABLEID_BYFILEID = "SELECT DISTINCT tableId,tableName,score from #$DataBaseName#$.dev.tableList where fileId=?";

	public static final String SELECT_MAX_SCORE = "SELECT MAX(score) FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String SELECT_DISCOVERS_DATES_BYTABLEID = "SELECT C2,C3 FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String SELECT_DISCOVERS_DATES_DETAILS = "SELECT * FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE C2=? and tableId=?";

	public static final String SELECT_DATAINGESTION_TABLEDETAILS = "select #$DataBaseName#$.dev.dataIngestionaTabelMetaData.*,#$DataBaseName#$.dev.analystLineItem.lineItemName AS masterLineItem from #$DataBaseName#$.dev.dataIngestionaTabelMetaData LEFT OUTER  join #$DataBaseName#$.dev.analystLineItem on #$DataBaseName#$.dev.dataIngestionaTabelMetaData.C1=#$DataBaseName#$.dev.analystLineItem.analystLineItemName and analystName=?";

	public static final String SELECT_DATAINGESTION_TABLEDETAILSBY_CLIENTNAME = "select #$DataBaseName#$.dev.dataIngestionaTabelMetaData.*,#$DataBaseName#$.dev.clientLineItem.lineItemName AS masterLineItem from #$DataBaseName#$.dev.dataIngestionaTabelMetaData LEFT OUTER  join #$DataBaseName#$.dev.clientLineItem on #$DataBaseName#$.dev.dataIngestionaTabelMetaData.C1=#$DataBaseName#$.dev.clientLineItem.clientLineItemName and clientName=?";

	public static final String DELETE_TABLEDETAILS_BYTABLEID = "DELETE #$DataBaseName#$.dev.tableList WHERE tableId=?";

	public static final String UPDATE_DATAINGESTION_TABLEDETAILS = "UPDATE #$DataBaseName#$.dev.dataIngestionaTabelMetaData  SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=?,C25=? WHERE field_Id=?";

	public static final String SELECT_MAX_TABLEID = "select MAX(tableId) from #$DataBaseName#$.dev.tableList";

	public static final String SELECT_TABLEDATA_BYFIELDID = "SELECT * FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE field_Id=?";

	public static final String DELETE_TABLEDATA_BY_FIELDID = "DELETE #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE field_Id=?";

	public static final String UPDATE_TABLENAME_BYTABLEID = "UPDATE #$DataBaseName#$.dev.tableList SET tableName=? where tableId=?";

	public static final String SELECT_KEYWORD = "SELECT * FROM #$DataBaseName#$.dev.keywordlist";

	public static final String SELECT_TABLEDATA_BYTABLEID_DOWNLOAD = "SELECT * FROM #$DataBaseName#$.dev.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String MERGE_TABLE_BYTABLEID = "UPDATE #$DataBaseName#$.dev.dataIngestionaTabelMetaData  SET tableId=? where tableId=?";

	public static final String LEFTSHIFT_TABLEDATA = "UPDATE #$DataBaseName#$.dev.dataIngestionaTabelMetaData  SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=? WHERE field_Id=?";

	public static final String RIGHTSHIFT_TABLEDATA = "UPDATE #$DataBaseName#$.dev.dataIngestionaTabelMetaData SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=?,C25=? WHERE field_Id=?";

	public static final String DELETE_TABLEID_BYTABLELIST = "DELETE #$DataBaseName#$.dev.tableList WHERE tableId=?";

	public static final String SELECT_FILEID_BYTABLEID = "SELECT DISTINCT #$DataBaseName#$.dev.dataIngestion.fileId from #$DataBaseName#$.dev.dataIngestion join #$DataBaseName#$.dev.tableList on  #$DataBaseName#$.dev.dataIngestion.fileId=#$DataBaseName#$.dev.tableList.fileId join #$DataBaseName#$.dev.dataIngestionaTabelMetaData on #$DataBaseName#$.dev.tableList.tableId=#$DataBaseName#$.dev.dataIngestionaTabelMetaData.tableId where #$DataBaseName#$.dev.dataIngestionaTabelMetaData.tableId=?";
	// DataIngestion Mapping table queries

	public static final String INSERT_INTO_DATAINGESTION_MAPPINGTABLE = "INSERT INTO #$DataBaseName#$.dev.dataIngestionMappingtable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_SHAREHOLDER_MEETING = "INSERT INTO #$DataBaseName#$.dev.shareholdermeeting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_SHAREHOLDER_CONTACT_TABLE = "INSERT INTO #$DataBaseName#$.dev.shareholderdataform values(?,?,?,?,?,?,?,?,?)";

}
