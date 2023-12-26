package com.Anemoi.InvestorRelation.DataIngestion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.Anemoi.Resource.Media;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.multipart.CompletedFileUpload;

public interface DataIngestionDao {


	ArrayList<TableList> getTableIdByFileId(long fileId, String dataBaseName) throws DataIngestionDaoException;

	ArrayList<DataIngestionTableModel> getTableIngestionTableData(String analystName, long tableId, String dataBaseName)
			throws DataIngestionDaoException;

	ArrayList<DataIngestionTableModel> updatedataIngestionTableData(
			ArrayList<DataIngestionTableModel> dataIngestionTableData, long tableId, String dataBaseName)
			throws DataIngestionDaoException;

	void deleteTableDataByTableId(long tableId, String dataBaseName) throws DataIngestionDaoException;

	ArrayList<DataIngestionMappingModel> addDataIngestionMappingTableData(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable, String dataBaseName)
			throws DataIngestionDaoException;

	DataIngestionTableModel getTableDataByFeldId(long field_Id, String dataBaseName) throws DataIngestionDaoException;

	void deleteTableDataByFieldId(long field_Id, String dataBaseName) throws DataIngestionDaoException;

	TableList updateTableNameByTableId(TableList tabledata, long tableId, String dataBaseName)
			throws DataIngestionDaoException;

	DataIngestionModel downloadTableDataBytableId(long tableId, String dataBaseName)
			throws DataIngestionDaoException, IOException;

	ArrayList<DataIngestionModel> getfileDetails(String dataBaseName) throws DataIngestionDaoException;

	ArrayList<KeywordList> getKeyword(String dataBaseName);

	void uploadExcelSheet(CompletedFileUpload file, long tableId, String dataBaseName) throws DataIngestionDaoException;


//	String saveShareHolderMeetingTables(DataIngestionModel dataIngestionModel, String dataBaseName)
//			throws DataIngestionDaoException;

	void deleteMultipleField(FiledData filedData, String dataBaseName) throws DataIngestionDaoException;

	TableList mergeTableDataByTableId(TableList tableList, long tableId, String dataBaseName)
			throws DataIngestionDaoException;

	ArrayList<DataIngestionTableModel> leftShiftTableDataByFieldId(ArrayList<DataIngestionTableModel> tableData,
			String dataBaseName) throws DataIngestionDaoException;

	ArrayList<DataIngestionTableModel> rightShiftTableDataByfieldId(ArrayList<DataIngestionTableModel> tableData,
			String dataBaseName) throws DataIngestionDaoException;

	void splitTableDetails(long fileId, ArrayList<DataIngestionTableModel> tableDetails, String dataBaseName)
			throws DataIngestionDaoException;

	DataIngestionModel getfileIdByTableId(long tableId, String dataBaseName) throws DataIngestionDaoException;

	DataIngestionModel getfileDataByFileId(long fileId, String dataBaseName) throws DataIngestionDaoException;

	TableList getTablelistByTableId(long tableId, String dataBaseName) throws DataIngestionDaoException;

	List<JSONObject> discoversDates(long tableId, String dataBaseName) throws DataIngestionDaoException;

	ArrayList<DataIngestionTableModel> getTableIngestionTableDataByClientName(String clientName, long tableId,
			String dataBaseName) throws DataIngestionDaoException;

	void deletefiledetailsbyFileid(long fileId, String dataBaseName) throws DataIngestionDaoException;

	NonProcessFilesDetails saveMultipleFiles(NonProcessFilesDetails model,String dataBaseName) throws DataIngestionDaoException;

	ArrayList<NonProcessFilesDetails> getFileIds(String client, String dataBaseName) throws DataIngestionDaoException;


	DataIngestionModel getPreviewForNonProcessFile(int npFileId, DataIngestionModel ingestionModel, String dataBaseName)
			throws DataIngestionDaoException;

	DataIngestionModel getPreviewForSplittedFile(int npFileId, DataIngestionModel ingestionModel, String dataBaseName)
			throws DataIngestionDaoException;

	DataIngestionModel savePreviewFileForAllPages(int npFileId, DataIngestionModel ingestionModel, String dataBaseName)
			throws DataIngestionDaoException;

	DataIngestionModel savePreviewFileForSplited(int npFileId, DataIngestionModel ingestionModel, String dataBaseName)
			throws DataIngestionDaoException;

	ArrayList<NonProcessFilesDetails> getnonProcessFiles(String dataBaseName) throws DataIngestionDaoException;

	ArrayList<DataIngestionMappingModel> getPriviewDataIngestionMappingTableData(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable, String dataBaseName) throws DataIngestionDaoException;

//	DataIngestionModel extractFileByFileId(long fileId, String dataBaseName)
//			throws DataIngestionDaoException, SQLException, IOException, ClassNotFoundException;



}
