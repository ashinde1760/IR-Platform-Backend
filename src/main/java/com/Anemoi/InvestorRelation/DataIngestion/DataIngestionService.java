package com.Anemoi.InvestorRelation.DataIngestion;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.Anemoi.Resource.Media;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.multipart.CompletedFileUpload;

public interface DataIngestionService {

//	public DataIngestionModel saveDataIngestion(DataIngestionModel dataIngestionModel,CompletedFileUpload file) throws DataIngestionServiceException;

	public ArrayList<TableList> getTableIdByFileId(long fileId) throws DataIngestionServiceException;

	public ArrayList<DataIngestionTableModel> getDataIngestionTableDetails(String analystName, long tableId)
			throws DataIngestionServiceException;

	public ArrayList<DataIngestionTableModel> updateeDataIngestionTabledata(
			ArrayList<DataIngestionTableModel> dataIngestionTableData, long tableId)
			throws DataIngestionServiceException;

	public String deleteTableDatabyTableID(long tableId) throws DataIngestionServiceException;

	// DataIngestion Mapping table
	public ArrayList<DataIngestionMappingModel> addDataIngestionMappingTable(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable) throws DataIngestionServiceException;

	public DataIngestionTableModel getTableDataByFieldId(long field_Id) throws DataIngestionServiceException;

	public String deleteTableDataByFieldId(long field_Id) throws DataIngestionServiceException;

	public TableList updateTableNameByTableId(TableList tabledata, long tableId) throws DataIngestionServiceException;

	public DataIngestionModel downloadTableDataByTableId(long tableId) throws DataIngestionServiceException;

	public ArrayList<DataIngestionModel> getfileDetails() throws DataIngestionServiceException;

	public ArrayList<KeywordList> getKeyword();

	public String uploadExcelSheetBytableId(CompletedFileUpload file, long tableId) throws DataIngestionServiceException;

	public String deleteMultipleField(FiledData filedData) throws DataIngestionServiceException;

	public TableList mergeTableByTableId(TableList tableList, long tableId) throws DataIngestionServiceException;

	public ArrayList<DataIngestionTableModel> leftShiftTableDataByfieldId(ArrayList<DataIngestionTableModel> tableData)
			throws DataIngestionServiceException;

	public ArrayList<DataIngestionTableModel> rightShiftDataByFieldId(ArrayList<DataIngestionTableModel> tableData)
			throws DataIngestionServiceException;

	public String splitTableDetails(long fileId, ArrayList<DataIngestionTableModel> tableDetails)
			throws DataIngestionServiceException;

	public DataIngestionModel getfileIdByTableId(long tableId) throws DataIngestionServiceException;

	public DataIngestionModel getfileDataByFileId(long fileId) throws DataIngestionServiceException;

	public TableList getTablelistData(long tableId) throws DataIngestionServiceException;

	public List<JSONObject> discoversDates(long tableId) throws DataIngestionServiceException;

	public ArrayList<DataIngestionTableModel> getDataIngestionTableDetailsByClientName(String clientName, long tableId)
			throws DataIngestionServiceException;

	
//	public DataIngestionModel extractfilebyfileId(long fileId) throws DataIngestionServiceException;

	public ArrayList<NonProcessFilesDetails> saveMultipleFiles(ArrayList<NonProcessFilesDetails> modelList, NonProcessFilesDetails details)
			throws DataIngestionServiceException, ClassNotFoundException;

	public ArrayList<NonProcessFilesDetails> getFileIdsByClientName(String client) throws DataIngestionServiceException;


	public HttpResponse<DataIngestionModel> getPreviewForNonProcessFiles(int npFileId,
			DataIngestionModel ingestionModel) throws DataIngestionServiceException;

	public HttpResponse<DataIngestionModel> savePreviewFileDetailsInTable(int npFileId,
			DataIngestionModel ingestionModel) throws DataIngestionServiceException;

	public String overwriteFile(NonProcessFilesDetails file) throws DataIngestionServiceException;

	public String overWriteMultipleFiles(ArrayList<NonProcessFilesDetails> nonProcessFilesList) throws DataIngestionServiceException;

	public ArrayList<NonProcessFilesDetails> getNonProcessingFiles() throws DataIngestionServiceException;

	public ArrayList<DataIngestionMappingModel> getPreviewDataIngestionMappingTable(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable) throws DataIngestionServiceException;

}
