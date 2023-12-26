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

	public static final String INSERT_INTO_NONPROCESSFILETABLES = "INSERT INTO INV_RELATIONS.dev.nonprocessFileTable values(?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_HASHCODE = "SELECT hashCode FROM INV_RELATIONS.dev.nonprocessFileTable";

	public static final String SELECT_NPFILEID = "SELECT npFileId FROM INV_RELATIONS.dev.nonprocessFileTable WHERE  hashCode=?";

	public static final String SELECT_NONPROCESS_FILEIDS = "SELECT * FROM INV_RELATIONS.dev.nonprocessFileTable WHERE client=? and status=?";

	public static final String DELETE_NONPROCESS_FILEID = "DELETE INV_RELATIONS.dev.nonprocessFileTable WHERE npFileId=?";

	public static final String SELECT_NONPROCESS_FILEIDS_BYNPFILEID = "SELECT * FROM INV_RELATIONS.dev.nonprocessFileTable WHERE npFileId=?";

//	public static final String SELECT_NONPROCESSING_FILES = "SELECT * FROM INV_RELATIONS.dev.nonprocessFileTable WHERE status=?";

	
  	public static final String SELECT_NONPROCESSING_FILES = "select INV_RELATIONS.dev.nonprocessFileTable.*,#$DataBaseName#$.dev.clientDetails.suggestedPeers AS peers from INV_RELATIONS.dev.nonprocessFileTable LEFT OUTER  join INV_RELATIONS.dev.clientDetails on INV_RELATIONS.dev.nonprocessFileTable.client=#$DataBaseName#$.dev.clientDetails.clientName WHERE status=?";

	
	public static final String INSERT_INTO_DATA_INGESTION = "INSERT INTO INV_RELATIONS.dev.dataIngestion values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_STATUSNONPROCESSFILES = "UPDATE INV_RELATIONS.dev.nonprocessFileTable SET status=? WHERE npFileId=?";

	public static final String SELECT_OVERRIDE_DETAILS = "SELECT fileId FROM INV_RELATIONS.dev.dataIngestion WHERE client=? AND documentType=? AND analystName=? OR peerName=? AND reportType=? AND reportDate=?";

	public static final String SELECT_FILEID_OVERWRITE = "SELECT fileId FROM INV_RELATIONS.dev.dataIngestion WHERE npfileId=?";

	public static final String SELECT_DATAINGESTION_FILEDETAILS = "SELECT * FROM INV_RELATIONS.dev.dataIngestion ORDER BY (fileId) DESC ,RAND()";

//	public static final String SELECT_DATAINGESTION_FILEDETAILS = "SELECT *, FORMAT(DATEADD(SECOND, [createdOn] / 1000, '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM INV_RELATIONS.dev.dataIngestion ORDER BY [createdOn] ASC, NEWID()";

	public static final String SELECT_DATAINGESTION_FILEDETAILS_BYFILEID = "SELECT * FROM INV_RELATIONS.dev.dataIngestion where fileId=?";

	public static final String UPDATE_REPORTDATE = "UPDATE INV_RELATIONS.dev.dataIngestion SET reportDate=? where fileId=?";

	public static final String UPDATE_STATUSFILEEXTRACT = "UPDATE INV_RELATIONS.dev.dataIngestion SET status=? WHERE fileId=?";

	public static final String DELETE_DATAINGESTION_FILEDETAILS = "DELETE INV_RELATIONS.dev.dataIngestion WHERE fileId=?";

	public static final String DELETE_FORCAST = "DELETE INV_RELATIONS.dev.forecast WHERE fileId=?";

	public static final String DELETE_TABLELIST = "DELETE INV_RELATIONS.dev.tableList WHERE fileId=?";

	public static final String SELECT_DATAINGESTION_BYFILEID = "SELECT * FROM INV_RELATIONS.dev.dataIngestion where fileId=?";

	public static final String SELECT_MAX_FILEID = "select MAX(fileId) from INV_RELATIONS.dev.dataIngestion";

	public static final String SELECT_MAX_BROKENID = "select MAX(brokenFileId) from INV_RELATIONS.dev.brokenfiledetails";

	public static final String SELECT_TABLENAME = "SELECT tableName from INV_RELATIONS.dev.tableList where tableId=?";

	public static final String INSERT_FORCASTTABLE = "INSERT INTO INV_RELATIONS.dev.forecast values(?,?,?,?,?)";

	public static final String INSERT_TABLELIST = "INSERT INTO INV_RELATIONS.dev.tableList values(?,?,?,?,?)";

	public static final String SELECT_TABLEID = "SELECT * FROM INV_RELATIONS.dev.tableList";

	public static final String SELECT_TABLEDATA_BYTABLEID = "SELECT * FROM INV_RELATIONS.dev.tableList WHERE tableId=?";

	// DataIngestion Table queries

	public static final String INSERT_INTO_DATAINGESTION_TABLE_DATA = "INSERT INTO INV_RELATIONS.dev.dataIngestionaTabelMetaData values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_TABLEID_BYFILEID = "SELECT DISTINCT tableId,tableName,score from INV_RELATIONS.dev.tableList where fileId=?";

	public static final String SELECT_MAX_SCORE = "SELECT MAX(score) FROM INV_RELATIONS.dev.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String SELECT_DISCOVERS_DATES_BYTABLEID = "SELECT C2,C3 FROM INV_RELATIONS.dev.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String SELECT_DISCOVERS_DATES_DETAILS = "SELECT * FROM INV_RELATIONS.dev.dataIngestionaTabelMetaData WHERE C2=? and tableId=?";

	public static final String SELECT_DATAINGESTION_TABLEDETAILS = "select INV_RELATIONS.dev.dataIngestionaTabelMetaData.*,#$DataBaseName#$.dev.analystLineItem.lineItemName AS masterLineItem from INV_RELATIONS.dev.dataIngestionaTabelMetaData LEFT OUTER  join INV_RELATIONS.dev.analystLineItem on INV_RELATIONS.dev.dataIngestionaTabelMetaData.C1=#$DataBaseName#$.dev.analystLineItem.analystLineItemName and analystName=?";

	public static final String SELECT_DATAINGESTION_TABLEDETAILSBY_CLIENTNAME = "select INV_RELATIONS.dev.dataIngestionaTabelMetaData.*,#$DataBaseName#$.dev.clientLineItem.lineItemName AS masterLineItem from INV_RELATIONS.dev.dataIngestionaTabelMetaData LEFT OUTER  join INV_RELATIONS.dev.clientLineItem on INV_RELATIONS.dev.dataIngestionaTabelMetaData.C1=#$DataBaseName#$.dev.clientLineItem.clientLineItemName and clientName=?";

	public static final String DELETE_TABLEDETAILS_BYTABLEID = "DELETE INV_RELATIONS.dev.tableList WHERE tableId=?";

	public static final String UPDATE_DATAINGESTION_TABLEDETAILS = "UPDATE INV_RELATIONS.dev.dataIngestionaTabelMetaData  SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=?,C25=? WHERE field_Id=?";

	public static final String SELECT_MAX_TABLEID = "select MAX(tableId) from INV_RELATIONS.dev.tableList";

	public static final String SELECT_TABLEDATA_BYFIELDID = "SELECT * FROM INV_RELATIONS.dev.dataIngestionaTabelMetaData WHERE field_Id=?";

	public static final String DELETE_TABLEDATA_BY_FIELDID = "DELETE INV_RELATIONS.dev.dataIngestionaTabelMetaData WHERE field_Id=?";

	public static final String UPDATE_TABLENAME_BYTABLEID = "UPDATE INV_RELATIONS.dev.tableList SET tableName=? where tableId=?";

	public static final String SELECT_KEYWORD = "SELECT * FROM INV_RELATIONS.dev.keywordlist";

	public static final String SELECT_TABLEDATA_BYTABLEID_DOWNLOAD = "SELECT * FROM INV_RELATIONS.dev.dataIngestionaTabelMetaData WHERE tableId=?";

	public static final String MERGE_TABLE_BYTABLEID = "UPDATE INV_RELATIONS.dev.dataIngestionaTabelMetaData  SET tableId=? where tableId=?";

	public static final String LEFTSHIFT_TABLEDATA = "UPDATE INV_RELATIONS.dev.dataIngestionaTabelMetaData  SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=? WHERE field_Id=?";

	public static final String RIGHTSHIFT_TABLEDATA = "UPDATE INV_RELATIONS.dev.dataIngestionaTabelMetaData SET C1=?,C2=?,C3=?,C4=?,C5=?,C6=?,C7=?,C8=?,C9=?,C10=?,C11=?,C12=?,C13=?,C14=?,C15=?,C16=?,C17=?,C18=?,C19=?,C20=?,C21=?,C22=?,C23=?,C24=?,C25=? WHERE field_Id=?";

	public static final String DELETE_TABLEID_BYTABLELIST = "DELETE INV_RELATIONS.dev.tableList WHERE tableId=?";

	public static final String SELECT_FILEID_BYTABLEID = "SELECT DISTINCT INV_RELATIONS.dev.dataIngestion.fileId from INV_RELATIONS.dev.dataIngestion join INV_RELATIONS.dev.tableList on  INV_RELATIONS.dev.dataIngestion.fileId=#$DataBaseName#$.dev.tableList.fileId join INV_RELATIONS.dev.dataIngestionaTabelMetaData on INV_RELATIONS.dev.tableList.tableId=#$DataBaseName#$.dev.dataIngestionaTabelMetaData.tableId where INV_RELATIONS.dev.dataIngestionaTabelMetaData.tableId=?";
	// DataIngestion Mapping table queries

	public static final String INSERT_INTO_DATAINGESTION_MAPPINGTABLE = "INSERT INTO INV_RELATIONS.dev.dataIngestionMappingtable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_SHAREHOLDER_MEETING = "INSERT INTO INV_RELATIONS.dev.shareholdermeeting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_SHAREHOLDER_CONTACT_TABLE = "INSERT INTO INV_RELATIONS.dev.shareholderdataform values(?,?,?,?,?,?,?,?,?)";

}
