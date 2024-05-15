package com.Anemoi.InvestorRelation.Configuration;

public class InvestorDatabaseTables {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String CREATE_ROLEMODEL_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.rolemodel(id uniqueidentifier NOT NULL,roleName varchar(255) unique,description varchar(255) NOT NULL,status varchar(255) NOT NULL,dashboardAccess varchar(550) NOT NULL,lastEdit bigint NOT NUll,createdOn bigint NOT NULL,CONSTRAINT PK_roleName PRIMARY KEY CLUSTERED(roleName))";

	public static final String CREATE_USER_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.userTable(userid uniqueidentifier NOT NULL,firstName varchar(255) NOT NULL,lastName varchar(255) NOT NULL,email varchar(255) unique,mobileNumber varchar(255) unique,assignedName varchar(255) NOT NULL,roleName varchar(255) NOT NULL foreign key references #$DataBaseName#$.dbo.rolemodel(roleName) on update cascade ,userStatus varchar(20),password varchar(200) ,editedOn bigint NOT NULL,createdOn bigint NOT NULL,createdBy varchar(255),modifiedBy varchar(255),CONSTRAINT userid PRIMARY KEY CLUSTERED(userid))";

	public static final String CREATE_BALANCESHEETFORM_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.balanceSheetform(balanceid uniqueidentifier NOT NULL,lineItem varchar(255),alternativeName varchar(255),createdBy varchar(255),createdOn bigint NOT NULL ,modifiedBy varchar(255),modifiedOn bigint ,CONSTRAINT balanceid PRIMARY KEY CLUSTERED(balanceid))";

	public static final String CREATE_INCOMESTATEMENT_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.incomestatement(incomeid uniqueidentifier NOT NULL,lineItem varchar(255),alternativeName varchar(255),createdBy varchar(255),createdOn bigint NOT NULL ,modifiedBy varchar(255),modifiedOn bigint ,CONSTRAINT incomeid PRIMARY KEY CLUSTERED(incomeid))";

	public static final String CREATE_CASHFLOW_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.cashflow(cashId  uniqueidentifier NOT NULL,lineItem varchar(255),alternativeName varchar(255),createdBy varchar(255),createdOn bigint NOT NULL ,modifiedBy varchar(255),modifiedOn bigint ,CONSTRAINT cashid PRIMARY KEY CLUSTERED(cashid))";

	public static final String CREATE_SHAREHOLDERDATAFORM_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.shareholderdataform(shareId uniqueidentifier NOT NULL,clientName varchar(255) NOT NULL,folio varchar(255) NOT NULL,shareholderName varchar(255) NOT NULL,holdingValue varchar(255) NOT NULL,holdingPercentage varchar(255) NOT NULL,holdingCost varchar(255),minorCode varchar(255) NOT NULL,fundGroup varchar(255) NOT NULL,date bigint ,createdBy varchar(255),createdOn bigint ,modifiedBy varchar(255),modifiedOn bigint,CONSTRAINT shareid PRIMARY KEY CLUSTERED(shareid))";
	public static final String CREATE_SHAREHOLDERCONTACT_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.shareholdercontact(contactid uniqueidentifier NOT NULL,name varchar(255) NOT NULL,poc varchar(255) NOT NULL,email varchar(255) ,address varchar(255) ,contact varchar(255) ,createdBy varchar(255),createdOn bigint,CONSTRAINT contactid PRIMARY KEY CLUSTERED(contactid))";

	public static final String CREATE_FINANCIAL_RATIO_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.financialRatio(financialid uniqueidentifier NOT NULL,clientName varchar(255) NOT NULL,formulaName varchar(255) NOT NULL,formula varchar(255) NOT NULL,createdBy varchar(255),createdOn bigint NOT NULL,CONSTRAINT financialid PRIMARY KEY CLUSTERED(financialid))";

	public static final String CREATE_ANALYST_LINE_ITEM = "CREATE TABLE #$DataBaseName#$.dbo.analystLineItem(analystLineId uniqueidentifier NOT NULL,analystName varchar(255) NOT NULL,lineItemName varchar(255),analystLineItemName varchar(255) NOT NULL,analystTableHeaderName varchar(255) NOT NULL,masterTableSource varchar(255) NOT NULL,createdOn bigint NOT NULL,createdBy varchar(255),CONSTRAINT PK_analystlineId PRIMARY KEY CLUSTERED(analystlineId))";

	public static final String CREATE_CLIENT_LINE_ITEM = "CREATE TABLE #$DataBaseName#$.dbo.clientLineItem(clientLineId uniqueidentifier NOT NULL,clientName varchar(255) NOT NULL,lineItemName varchar(255),clientLineItemName varchar(255) NOT NULL,clientTableHeaderName varchar(255) NOT NULL,masterTableSource varchar(255) NOT NULL,createdOn bigint NOT NULL,createdBy varchar(255),CONSTRAINT PK_clientLineId PRIMARY KEY CLUSTERED(clientLineId))";

	public static final String CREATE_ANALYST_DETAILS = "CREATE TABLE  #$DataBaseName#$.dbo.analystDetails(analystId bigint PRIMARY KEY,analystName varchar(255) NOT NULL,createdOn bigint NOT NULL,modifiedOn bigint,createdBy varchar(255),modifiedBy varchar(255) )";

	public static final String CREATE_ANALYST_CONTACTDETAILS = "CREATE TABLE  #$DataBaseName#$.dbo.analystContactDetails(analystcontactid bigint IDENTITY,analystId bigint FOREIGN KEY REFERENCES #$DataBaseName#$.dbo.analystDetails(analystId) ON DELETE CASCADE ,pocName varchar(255) NOT NULL,pocEmailId varchar(255) NOT NULL)";

	public static final String CREATE_NONPROCESS_FILETABLE = "CREATE TABLE #$DataBaseName#$.dbo.nonprocessFileTable(npFileId INT IDENTITY ,client varchar(255),fileName varchar(255) NOT NULL,fileType varchar(255) NOT NULL,FileData VARBINARY(MAX),hashCode varchar(255),status varchar(255),clientId varchar(255),createdBy varchar(255),createdOn bigint)";

	public static final String CREATE_DATA_INGESTION = "CREATE TABLE #$DataBaseName#$.dbo.dataIngestion(npFileId INT,fileId INT PRIMARY KEY ,client varchar(255) NOT NULL,documentType varchar(255) ,analystName varchar(255),peerName varchar(255),currency varchar(255),units varchar(255),reportType varchar(255) ,reportDate date,fileName varchar(255) NOT NULL,fileType varchar(255) NOT NULL,fileData VARBINARY(MAX),denomination varchar(255),pagenumbers varchar(255),status varchar(255),consolidated_standalone varchar(255),createdBy varchar(255),createdOn bigint NOT NULL,reportYear varchar(255))";

	public static final String CREATE_FORCAST_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.forecast(f_id INT IDENTITY,fileId INT FOREIGN KEY REFERENCES #$DataBaseName#$.dbo.dataIngestion(fileId),CMP_value varchar(255),target_price varchar(255),date varchar(255),rating varchar(255))";

	public static final String CREATE_TABLE_LIST = "CREATE TABLE #$DataBaseName#$.dbo.tableList(fileId INT FOREIGN KEY REFERENCES #$DataBaseName#$.dbo.dataIngestion(fileId),tableId INT PRIMARY KEY,tableName varchar(255),score bigint,tableMapName varchar(255))";

	public static final String CREATE_DATAINGESTION_TABLEMETADATA = "CREATE TABLE #$DataBaseName#$.dbo.dataIngestionaTabelMetaData(field_Id bigint IDENTITY,tableId INT FOREIGN KEY REFERENCES #$DataBaseName#$.dbo.tableList ON DELETE CASCADE ,C1 varchar(MAX) ,C2 varchar(MAX),C3 varchar(MAX),C4 varchar(MAX),C5 varchar(MAX),C6 varchar(MAX),C7 varchar(MAX),C8 varchar(MAX),C9 varchar(MAX),C10 varchar(MAX),C11 varchar(MAX),C12 varchar(MAX),C13 varchar(MAX),C14 varchar(MAX),C15 varchar(MAX),C16 varchar(MAX),C17 varchar(MAX),C18 varchar(MAX),C19 varchar(MAX),C20 varchar(MAX),C21 varchar(MAX),C22 varchar(MAX),C23 varchar(MAX),C24 varchar(MAX),C25 varchar(MAX),CONSTRAINT PK_field_Id PRIMARY KEY CLUSTERED(field_Id))";

	public static final String CREATE_DATAINGESTION_MAPPING_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.dataIngestionMappingtable(mapId uniqueidentifier NOT NULL,fieldId bigint NOT NULL,analyst varchar(255) ,masterLineItem varchar(255),companyName varchar(255) ,tableName varchar(255),documentType varchar(255) ,peerName varchar(255),currency varchar(255),units varchar(255),year varchar(255) NOT NULL,lineItemName varchar(255) NOT NULL,quarter varchar(255) NOT NULL,type varchar(255) NOT NULL,value varchar(255),reportType varchar(255) NOT NULL,reportDate date NOT NULL,date bigint NOT NULL,denomination varchar(255),consolidated_standalone varchar(255),createdBy varchar(255),CONSTRAINT mapId PRIMARY KEY CLUSTERED(mapId))";

	public static final String CREATE_KEYWORDLIST = "CREATE TABLE #$DataBaseName#$.dbo.keywordlist(key_id bigint IDENTITY ,document_type varchar(255),keyword varchar(255),weight int CONSTRAINT PK_key_id PRIMARY KEY CLUSTERED(key_id))";

	// Investor relation phase2
	public static final String CREATE_REPORTTABLEHEADER_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.reportTableHeader(tableHeaderId uniqueidentifier NOT NULL,tableHeaderName varchar(255),description varchar(255),createdOn bigint NOT NULL,modifiedOn bigint,CONSTRAINT tableHeaderId PRIMARY KEY CLUSTERED(tableHeaderId))";

	public static final String CREATE_MEETINGSHEDULAR = "CREATE TABLE #$DataBaseName#$.dbo.meetingshedulartable(meetingSheduleId bigint IDENTITY ,meetingId varchar(max) ,eventId varchar(max) ,joinUrl varchar(max) ,title varchar(255) NOT NULL,agenda varchar(255),participant varchar(255) NOT NULL,meetingDate varchar(255) NOT NULL,startTime varchar(255) NOT NULL,endTime varchar(255) NOT NULL,meetingType varchar(255) NOT NULL,recordAutomatically bit,meetingDataStatus varchar(255),createdBy varchar(255),createdOn bigint NOT NULL,modifiedBy varchar(255),modifiedOn bigint NOT NULL,clientName varchar(255),status varchar(255),fundGroup varchar(255),remark varchar(max))";

	public static final String CREATE_CLIENCODEDETAILS = "CREATE TABLE #$DataBaseName#$.dbo.clientcodedetails(clientCodeId bigint IDENTITY,projectCode int NOT NULL,taskCode int NOT NULL,clientName varchar(255) NOT NULL,clientAddress varchar(255) NOT NULL,CONSTRAINT PK_clientCodeId PRIMARY KEY CLUSTERED(clientCodeId))";

	public static final String CREATE_CLIENTDETAILS = "CREATE TABLE #$DataBaseName#$.dbo.clientDetails(clientId uniqueidentifier NOT NULL,projectCode int NOT NULL,taskCode int NOT NULL,clientName varchar(255) NOT NULL,clientAddress varchar(255) NOT NULL,emailId varchar(255),industry varchar(255) NOT NULL,suggestedPeers varchar(255) ,assignAA varchar(255) NOT NULL,createdBy varchar(255),createdOn bigint NOT NULL ,modifiedBy varchar(255),modifiedOn bigint ,CONSTRAINT PK_clientId PRIMARY KEY CLUSTERED(clientId))";

	public static final String CREATE_WHITELABLING="CREATE TABLE #$DataBaseName#$.dbo.whitelablingtable(whitelableId uniqueidentifier NOT NULL,clientName varchar(255) NOT NULL,filePath varchar(255),fileName varchar(255),fileType varchar(255),fileData VARBINARY(MAX),cssFileName varchar(255) NOT NULL,cssFileType varchar(255) NOT NULL,cssFileData VARBINARY(MAX),createdBy varchar(255),createdOn bigint NOT NULL,CONSTRAINT PK_whitelableId PRIMARY KEY CLUSTERED(whitelableId))";

	public static final String CREATE_SHAREHOLDER_MEETING_TABLE = "CREATE TABLE #$DataBaseName#$.dbo.shareholdermeeting(shareholderid uniqueidentifier NOT NULL,meetingId varchar(max) NOT NULL,date varchar(255) NOT NULL,startTime varchar(255) NOT NULL,endTime varchar(255) NOT NULL,organisation varchar(255) NOT NULL,stakeholderType varchar(255) NOT NULL,meetingType varchar(255) NOT NULL,subject varchar(255) NOT NULL,broker varchar(255) NOT NULL,location varchar(255) NOT NULL,status varchar(255) NOT NULL,comments varchar(max) ,participants varchar(255) NOT NULL,feedback varchar(max) ,summary varchar(max),actionItem varchar(max),investorConcerns varchar(max),analysis varchar(max),uploadedDate bigint NOT NULL,uploadedBy varchar(255),mediaKey varchar(255),momfileName varchar(255),momFileType varchar(255), momFileData VARBINARY(MAX),momStatus varchar(50),clientName varchar(255),fundGroup varchar(255),CONSTRAINT shareholderid PRIMARY KEY CLUSTERED(shareholderid))";

	public static final String CREATE_AUDITHISTORY_TABLE="CREATE TABLE #$DataBaseName#$.dbo.auditHistoryTable(auditId bigint IDENTITY,activity varchar(255),userName varchar(255),createdBy varchar(255),description varchar(255),createdOn bigint CONSTRAINT PK_auditId PRIMARY KEY CLUSTERED(auditId))";

	public static final String CREATE_MINORCODELIST_TABLE="CREATE TABLE #$DataBaseName#$.dbo.minorcodelistTable(minorcodeId bigint IDENTITY,minorCode varchar(255),fullform varchar(255) CONSTRAINT PK_minorcodeId PRIMARY KEY CLUSTERED(minorcodeId))";

   public static final String CREATE_NOTIFICATION_TABLE="CREATE TABLE #$DataBaseName#$.dbo.notificationTable(nId bigint IDENTITY ,message varchar(MAX),users varchar(MAX),createdOn bigint CONSTRAINT PK_nId PRIMARY KEY CLUSTERED(nId))";
   
   public static final String CREATE_MAIL_DATA_TABLE="CREATE TABLE #$DataBaseName#$.dbo.mailDataTable(message varchar(MAX),users varchar(MAX),status varchar(200),Reason varchar(MAX),createdOn bigint)";

}
