package com.Anemoi.InvestorRelation.DataIngestion;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Session;
import javax.mail.Transport;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoImpl;
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.MeetingScheduler.MeetingDao;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationService;
import com.Anemoi.MailSession.MailService;
import com.Anemoi.Resource.Media;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DataIngestionServiceImpl implements DataIngestionService {

	@Inject
	DataIngestionDao dataIngestionDao;

	@Inject
	private AuditHistoryService auditHistoryService;

	@Inject
	private MailService mailService;

	@Inject
	private MeetingDao meetingDao;

	@Inject
	private NotificationService notificationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DataIngestionServiceImpl.class);

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	private static String DATABASENAME = "databaseName";

	private static String dataBaseName() {

		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;

	}

	@Override
	public ArrayList<TableList> getTableIdByFileId(long fileId) throws DataIngestionServiceException {
		// TODO Auto-generated method stub

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<TableList> tableid = this.dataIngestionDao.getTableIdByFileId(fileId, dataBaseName);
			return tableid;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionServiceException("unable ot get" + e.getMessage());
		}

	}

	@Override
	public ArrayList<DataIngestionTableModel> getDataIngestionTableDetails(String analystName, long tableId)
			throws DataIngestionServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionTableModel> tableData = this.dataIngestionDao.getTableIngestionTableData(analystName,
					tableId, dataBaseName);
			return tableData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionServiceException("unable to get" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteTableDatabyTableID(long tableId) throws DataIngestionServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			this.dataIngestionDao.deleteTableDataByTableId(tableId, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "table data deleted suucessfully");
			return reposneJSON.toString();

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to delete table data");
		}

	}

	@Override
	public ArrayList<DataIngestionTableModel> updateeDataIngestionTabledata(
			ArrayList<DataIngestionTableModel> dataIngestionTableData, long tableId)
			throws DataIngestionServiceException {
		try {
			System.out.println("check1");
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionTableModel> response = this.dataIngestionDao
					.updatedataIngestionTableData(dataIngestionTableData, tableId, dataBaseName);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to update dataIngestion table data " + e.getMessage());
		}
	}

	@Override
	public ArrayList<DataIngestionMappingModel> addDataIngestionMappingTable(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable) throws DataIngestionServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			String createdBy = null;
			ArrayList<DataIngestionMappingModel> mappingModel = this.dataIngestionDao
					.addDataIngestionMappingTableData(dataIngestionMappingTable, dataBaseName);
			for (DataIngestionMappingModel m : mappingModel) {
				createdBy = m.getCreatedBy();
			}

			java.util.Date d = new java.util.Date();
			AuditHistoryEntity entity = new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("File mapping");
			entity.setDescription("file Mapping in dataIngestion module");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);

			return mappingModel;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException(
					"unable to add dataingestion mapping data in table " + e.getMessage());
		}
	}

	@Override
	public DataIngestionTableModel getTableDataByFieldId(long field_Id) throws DataIngestionServiceException {

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			DataIngestionTableModel model = this.dataIngestionDao.getTableDataByFeldId(field_Id, dataBaseName);
			return model;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to get dataingestion table field data " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteTableDataByFieldId(long field_Id) throws DataIngestionServiceException {

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			DataIngestionTableModel modelData = this.dataIngestionDao.getTableDataByFeldId(field_Id, dataBaseName);
			if (modelData == null) {

				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "data not delete because fieldId is null");
			}
			this.dataIngestionDao.deleteTableDataByFieldId(field_Id, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "table field  deleted suucessfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to delete " + e.getMessage());
		}
	}

	@Override
	public TableList updateTableNameByTableId(TableList tabledata, long tableId) throws DataIngestionServiceException {
		// TODO Auto-generated method stub

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			TableList model = this.dataIngestionDao.updateTableNameByTableId(tabledata, tableId, dataBaseName);
			return model;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to update " + e.getMessage());
		}
	}

	@Override
	public DataIngestionModel downloadTableDataByTableId(long tableId) throws DataIngestionServiceException {
		try {
			System.out.println("check2");
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			DataIngestionModel response = this.dataIngestionDao.downloadTableDataBytableId(tableId, dataBaseName);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException(e.getMessage());
		}

	}

	@Override
	public ArrayList<DataIngestionModel> getfileDetails() throws DataIngestionServiceException {
		// TODO Auto-generated method stub

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionModel> list = this.dataIngestionDao.getfileDetails(dataBaseName);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unableto get  " + e.getMessage());
		}
	}

	@Override
	public ArrayList<KeywordList> getKeyword() {
		// TODO Auto-generated method stub

		String dataBaseName = DataIngestionServiceImpl.dataBaseName();
		ArrayList<KeywordList> list = this.dataIngestionDao.getKeyword(dataBaseName);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String uploadExcelSheetBytableId(CompletedFileUpload file, long tableId)
			throws DataIngestionServiceException {

		try {

			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			this.dataIngestionDao.uploadExcelSheet(file, tableId, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "upload excel sheet  successfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			throw new DataIngestionServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteMultipleField(FiledData filedData) throws DataIngestionServiceException {
		// TODO Auto-generated method stub
		try {
			System.out.println("check2");
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			this.dataIngestionDao.deleteMultipleField(filedData, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "multiple delete deleted suucessfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionServiceException("unable to delete multiple field id  " + e.getMessage());
		}

	}

	@Override
	public TableList mergeTableByTableId(TableList tableList, long tableId) throws DataIngestionServiceException {

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			TableList dataIngestion = dataIngestionDao.mergeTableDataByTableId(tableList, tableId, dataBaseName);
			return dataIngestion;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to merge table  " + e.getMessage());
		}
	}

	@Override
	public ArrayList<DataIngestionTableModel> leftShiftTableDataByfieldId(ArrayList<DataIngestionTableModel> tableData)
			throws DataIngestionServiceException {

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionTableModel> tableDatadetails = this.dataIngestionDao
					.leftShiftTableDataByFieldId(tableData, dataBaseName);
			return tableDatadetails;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to left shift table data  " + e.getMessage());
		}
	}

	@Override
	public ArrayList<DataIngestionTableModel> rightShiftDataByFieldId(ArrayList<DataIngestionTableModel> tableData)
			throws DataIngestionServiceException {
		try {

			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionTableModel> response = this.dataIngestionDao.rightShiftTableDataByfieldId(tableData,
					dataBaseName);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to right shift table data  " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String splitTableDetails(long fileId, ArrayList<DataIngestionTableModel> tableDetails)
			throws DataIngestionServiceException {
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			this.dataIngestionDao.splitTableDetails(fileId, tableDetails, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "table split successfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			throw new DataIngestionServiceException("unable to split table details" + e.getMessage());
		}

	}

	@Override
	public DataIngestionModel getfileIdByTableId(long tableId) throws DataIngestionServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			DataIngestionModel model = this.dataIngestionDao.getfileIdByTableId(tableId, dataBaseName);
			return model;
		} catch (Exception e) {
			throw new DataIngestionServiceException("unable to get" + e.getMessage());
		}
	}

	@Override
	public DataIngestionModel getfileDataByFileId(long fileId) throws DataIngestionServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			DataIngestionModel model = this.dataIngestionDao.getfileDataByFileId(fileId, dataBaseName);
			return model;
		} catch (Exception e) {
			throw new DataIngestionServiceException("unable to get" + e.getMessage());
		}
	}

	@Override
	public TableList getTablelistData(long tableId) throws DataIngestionServiceException {
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			TableList list = this.dataIngestionDao.getTablelistByTableId(tableId, dataBaseName);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to get" + e.getMessage());
		}
	}

	@Override
	public List<JSONObject> discoversDates(long tableId) throws DataIngestionServiceException {
		try {
			System.out.println("check2");
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();

			List<JSONObject> tableData = this.dataIngestionDao.discoversDates(tableId, dataBaseName);
			return tableData;
		} catch (Exception e) {
			throw new DataIngestionServiceException("unable to get" + e.getMessage());
		}
	}

	@Override
	public ArrayList<DataIngestionTableModel> getDataIngestionTableDetailsByClientName(String clientName, long tableId)
			throws DataIngestionServiceException {
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionTableModel> tableData = this.dataIngestionDao
					.getTableIngestionTableDataByClientName(clientName, tableId, dataBaseName);
			return tableData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionServiceException("unable to get" + e.getMessage());
		}
	}

//	@SuppressWarnings("unused")
//	private boolean checkValuesExist(Connection connection, String dataBaseName, DataIngestionModel dataIngestionModel)
//			throws SQLException {
//		try {
//			System.out.println("file existing in database check");
//			PreparedStatement psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_OVERRIDE_DETAILS
//					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//			psta.setString(1, dataIngestionModel.getClient());
//			System.out.println("+++" + dataIngestionModel.getFileId());
//			psta.setString(2, dataIngestionModel.getDocumentType());
//			psta.setString(3, dataIngestionModel.getAnalystName());
//			psta.setString(4, dataIngestionModel.getPeerName());
//			psta.setString(5, dataIngestionModel.getReportType());
//			psta.setDate(6, dataIngestionModel.getReportDate());
//
//			ResultSet rs = psta.executeQuery();
//			if (rs.next()) {
//				return true;
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return false;
//
//	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public String deletefileDetailsByfileId(DataIngestionModel dataIngestionModel, CompletedFileUpload file,
//			long fileId) throws DataIngestionServiceException {
//
//		Connection con = null;
//		PreparedStatement psta = null;
//		try {
//			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
//			con = InvestorDatabaseUtill.getConnection();
//			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_FORCAST
//					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//			psta.setLong(1, fileId);
//			psta.executeUpdate();
//			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_TABLELIST
//					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//			psta.setLong(1, fileId);
//			psta.executeUpdate();
//			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_FILEDETAILS
//					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//			psta.setLong(1, fileId);
//			psta.executeUpdate();
//			saveDataIngestion(dataIngestionModel, file);
//			JSONObject reposneJSON = new JSONObject();
//			reposneJSON.put(STATUS, SUCCESS);
//			reposneJSON.put(MSG, "file overwrite successfully");
//			return reposneJSON.toString();
//		} catch (Exception e) {
//			// TODO: handle exception
//			throw new DataIngestionServiceException("unable to add " + e.getMessage());
//		}
//	}

	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public ArrayList<NonProcessFilesDetails> saveMultipleFiles(ArrayList<NonProcessFilesDetails> modelList,
			NonProcessFilesDetails details) throws DataIngestionServiceException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement psta = null;
		ResultSet rs = null;

		ArrayList<String> existingHashcodes = new ArrayList<>();
		ArrayList<NonProcessFilesDetails> list = new ArrayList<>();
		ArrayList<NonProcessFilesDetails> filesFoundlist = new ArrayList<>();
		connection = InvestorDatabaseUtill.getConnection();
		int count = 0;

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			Iterator ir = modelList.iterator();
			String hashString = null;

			while (ir.hasNext()) {
				NonProcessFilesDetails model = (NonProcessFilesDetails) ir.next();
				System.out.println("filename:=> "+model.getFileName());
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] hashBytes = md.digest(model.getFileData());
				BigInteger hashInt = new BigInteger(1, hashBytes);
				hashString = hashInt.toString(16);
				model.setHashCode(hashString);
				psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_HASHCODE
						.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

				psta.setString(1, details.getClient());
				rs = psta.executeQuery();
				while (rs.next()) {
					String code = rs.getString("fileName");
					System.out.println("existing file: "+rs.getString("fileName"));
					existingHashcodes.add(code);
				}

				if (!existingHashcodes.contains(model.getFileName())) {
					System.out.println("file not present in database");
					count++;
//				        	model.setClient(details.getClient());
					NonProcessFilesDetails modelsave = this.dataIngestionDao.saveMultipleFiles(model, dataBaseName);

				} else {
					System.out.println("file are present in database");
					System.out.println("fileName" + model.getClientId());
					psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_NPFILEID
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					psta.setString(1, model.getFileName());
					psta.setString(2, details.getClient().trim());
					rs = psta.executeQuery();
					while (rs.next()) {
						int npFileId2 = rs.getInt("npFileId");
						model.setNpFileId(npFileId2);
					}

					list.add(model);
				}

			}
			if (count > 0) {
				java.util.Date d = new java.util.Date();
				System.out.println("count " + count);
				AuditHistoryEntity entity = new AuditHistoryEntity();
				entity.setCreatedBy(details.getCreatedBy());
				entity.setActivity("File Upload");
				entity.setDescription(count + " " + "files has uploaded");
				entity.setCreatedOn(d.getTime());
				this.auditHistoryService.addAuditHistory(entity);
			} else {
				System.out.println("skip");
			}

			return list;

		} catch (Exception e) {

			throw new DataIngestionServiceException(e.getMessage());
		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(psta, connection);
		}

	}

	@Override
	public ArrayList<NonProcessFilesDetails> getFileIdsByClientName(String client)
			throws DataIngestionServiceException {

		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<NonProcessFilesDetails> reponse = this.dataIngestionDao.getFileIds(client, dataBaseName);
			return reponse;
		} catch (Exception e) {
			throw new DataIngestionServiceException(e.getMessage());
		}
	}

	@Override
	public HttpResponse<DataIngestionModel> getPreviewForNonProcessFiles(int npFileId,
			DataIngestionModel ingestionModel) throws DataIngestionServiceException {

		String dataBaseName = DataIngestionServiceImpl.dataBaseName();
		try {
			if (ingestionModel.getPagenumbers().equalsIgnoreCase("All")) { // direct get the file from nonprocess table
				System.out.println("get preview for all");
				DataIngestionModel dataIngestionModel = this.dataIngestionDao.getPreviewForNonProcessFile(npFileId,
						ingestionModel, dataBaseName);
				return HttpResponse.status(HttpStatus.OK).body(dataIngestionModel);
			} else {
				// get the file from non process table and split this file and return
				System.out.println("get preview for splited pages");
				DataIngestionModel model = this.dataIngestionDao.getPreviewForSplittedFile(npFileId, ingestionModel,
						dataBaseName);
				return HttpResponse.status(HttpStatus.OK).body(model);
			}
		} catch (Exception e) {

			throw new DataIngestionServiceException(e.getMessage());

		}
	}

	@Override
	public HttpResponse<DataIngestionModel> savePreviewFileDetailsInTable(int npFileId,
			DataIngestionModel ingestionModel) throws DataIngestionServiceException {
//		Transport transport = null;
		try {

			String dataBaseName = DataIngestionServiceImpl.dataBaseName();

			java.util.Date d = new java.util.Date();
			AuditHistoryEntity entity = new AuditHistoryEntity();
			entity.setCreatedBy(ingestionModel.getCreatedBy());
			entity.setActivity("File Configured");
			entity.setDescription("File has Configured Successfully");
			entity.setCreatedOn(d.getTime());

			if (ingestionModel.getPagenumbers().equalsIgnoreCase("All")) {
				// direct get the file from nonprocess table and save in dataingestion table
				System.out.println("save all file in table");
				DataIngestionModel dataIngestionModel = this.dataIngestionDao.savePreviewFileForAllPages(npFileId,
						ingestionModel, dataBaseName);

				this.auditHistoryService.addAuditHistory(entity);

				ArrayList<ClientDetailsEntity> analystAdminList = this.meetingDao
						.getAnalsytAdminByClientName(ingestionModel.getClient(), dataBaseName);
				List<String> clientAdmins = new ArrayList<>();
				List<String> analystAdmins = new ArrayList<>();
				for (ClientDetailsEntity client : analystAdminList) {
					clientAdmins.addAll(client.getEmailId());
					analystAdmins.addAll(client.getAssignAA());
				}

				mailService.uploadFileMailSendToClientAdminCCAnalystAdmin(ingestionModel, clientAdmins, analystAdmins);

				// notification code
				List<String> ea = new ArrayList<>();
				ea.addAll(clientAdmins);
				ea.addAll(analystAdmins);
				NotificationEntity e = new NotificationEntity();
				e.setMessage("New File updated");
				e.setUsers(ea);
				e.setCreatedOn(d.getTime());
				this.notificationService.addNotificationHistory(e);

				return HttpResponse.status(HttpStatus.OK).body(dataIngestionModel);
			} else {
				// get the file from non process table and split this file and save in
				// dataingestion table
				System.out.println("save preview for splited pages");
				DataIngestionModel model = this.dataIngestionDao.savePreviewFileForSplited(npFileId, ingestionModel,
						dataBaseName);

				this.auditHistoryService.addAuditHistory(entity);

				ArrayList<ClientDetailsEntity> analystAdminList = this.meetingDao
						.getAnalsytAdminByClientName(ingestionModel.getClient(), dataBaseName);
				List<String> clientAdmins = new ArrayList<>();
				List<String> analystAdmins = new ArrayList<>();
				for (ClientDetailsEntity client : analystAdminList) {
					clientAdmins.addAll(client.getEmailId());
					analystAdmins.addAll(client.getAssignAA());
				}

				mailService.uploadFileMailSendToClientAdminCCAnalystAdmin(ingestionModel, clientAdmins, analystAdmins);

				// notification code
				List<String> ea = new ArrayList<>();
				ea.addAll(clientAdmins);
				ea.addAll(analystAdmins);
				NotificationEntity e = new NotificationEntity();
				e.setMessage("New File updated");
				e.setUsers(ea);
				e.setCreatedOn(d.getTime());
				this.notificationService.addNotificationHistory(e);

				return HttpResponse.status(HttpStatus.OK).body(model);
			}

		} catch (Exception e) {

			throw new DataIngestionServiceException(e.getMessage());

		}

	}

//	@Override
//	public DataIngestionModel extractfilebyfileId(long fileId) throws DataIngestionServiceException {
//		try {
//			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
//			DataIngestionModel response = this.dataIngestionDao.extractFileByFileId(fileId, dataBaseName);
//			return response;
//		} catch (Exception e) {
//
//			throw new DataIngestionServiceException(e.getMessage());
//
//		}
//
//	}

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public String overwriteFile(NonProcessFilesDetails file) throws DataIngestionServiceException {
		Connection con = null;
		PreparedStatement psta = null;
		long fileId = 0;
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_FILEID_OVERWRITE
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setInt(1, file.getNpFileId());
			ResultSet rs = psta.executeQuery();
			while (rs.next()) {
				fileId = rs.getLong("fileId");
			}
			
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_FIELD_ID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			ResultSet rs1 = psta.executeQuery();
			String tableId=null;
			while (rs1.next()) {
				tableId=rs1.getString("tableId");
			}
			
			
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_MAPPING_TABLE
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
			
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_TBALE_METADATA
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
			
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_NONPROCESS_FILEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setInt(1, file.getNpFileId());
			psta.executeUpdate();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_FORCAST
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_TABLELIST
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_FILEDETAILS
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();

			this.dataIngestionDao.saveMultipleFiles(file, dataBaseName);
//			saveDataIngestion(dataIngestionModel, file);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "file overwrite successfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException("unable to add " + e.getMessage());
		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String overWriteMultipleFiles(ArrayList<NonProcessFilesDetails> nonProcessFilesList)
			throws DataIngestionServiceException {
		int count = 0;
		String createdBy = null;
		try {
			Iterator<NonProcessFilesDetails> ir = nonProcessFilesList.iterator();
			while (ir.hasNext()) {
				count++;
				NonProcessFilesDetails nonprocessfile = (NonProcessFilesDetails) ir.next();
				createdBy = nonprocessfile.getCreatedBy();
				this.overwriteFile(nonprocessfile);

			}
			System.out.println("count" + count);
			java.util.Date d = new java.util.Date();
			AuditHistoryEntity entity = new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("File Overwrite");
			entity.setDescription(count + " " + "files overwrite successfully");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);

			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "file overwrite successfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException(e.getMessage());
		}

	}

	@Override
	public ArrayList<NonProcessFilesDetails> getNonProcessingFiles() throws DataIngestionServiceException {
		try {
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<NonProcessFilesDetails> reponseList = this.dataIngestionDao.getnonProcessFiles(dataBaseName);
			return reponseList;
		} catch (Exception e) {
			throw new DataIngestionServiceException(e.getMessage());
		}
	}

	@Override
	public ArrayList<DataIngestionMappingModel> getPreviewDataIngestionMappingTable(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable) throws DataIngestionServiceException {
		try {
			System.out.println("call2");
			String dataBaseName = DataIngestionServiceImpl.dataBaseName();
			ArrayList<DataIngestionMappingModel> mappingModel = this.dataIngestionDao
					.getPriviewDataIngestionMappingTableData(dataIngestionMappingTable, dataBaseName);
			return mappingModel;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionServiceException(
					"unable to get priview dataingestion mapping data in table " + e.getMessage());
		}
	}

}
