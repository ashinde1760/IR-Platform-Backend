package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetServiceImpl;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowServiceException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowServiceImpl;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.MailSession.MailService;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton

public class ShareHolderDataFromServiceImpl implements ShareHoderDataFromService {

	@Inject
	private ShareHoderDataFromDao shareholderdataformdao;
	
	@Inject
	private AuditHistoryService auditHistoryService;
	
	@Inject
	private MailService mailService;

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
	public ShareHolderDataFromEntity CreateShareHoderDataForm(ShareHolderDataFromEntity sharehoderdataform)
			throws SQLException, ShareHolderDataFromServiceException {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();

			// applyValidation(sharehoderdataform);

			ShareHolderDataFromEntity createNewShareHolderDataForm = this.shareholderdataformdao
					.createNewShareHolderDataForm(sharehoderdataform, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entityAuditHistoryEntity=new AuditHistoryEntity();
			entityAuditHistoryEntity.setCreatedBy(sharehoderdataform.getCreatedBy());
			entityAuditHistoryEntity.setActivity("add shareholderData");
			entityAuditHistoryEntity.setDescription("add shareholderData in application");
			entityAuditHistoryEntity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entityAuditHistoryEntity);
			
			return createNewShareHolderDataForm;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderDataFromServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unused")
	private void applyValidation(ShareHolderDataFromEntity sharehoderdataform) throws Exception {
		// TODO Auto-generated method stub
		if (sharehoderdataform.getClientName() == null || sharehoderdataform.getClientName().isEmpty()) {
			throw new Exception("client id should not be null or empty");
		}
	
		if (sharehoderdataform.getFolio() == null || sharehoderdataform.getFolio().isEmpty()) {
			throw new Exception("folio should not be null or empty");
		}
		if (sharehoderdataform.getShareholderName() == null || sharehoderdataform.getShareholderName().isEmpty()) {
			throw new Exception("ShareHolder name should not be null or empty");
		}
		if (sharehoderdataform.getHoldingValue() == null || sharehoderdataform.getHoldingValue().isEmpty()) {
			throw new Exception("holding value should not be null or empty");
		}
		if (sharehoderdataform.getHoldingPercentage() == null || sharehoderdataform.getHoldingPercentage().isEmpty()) {
			throw new Exception("Holding percentage should not be null or empty");
		}
		if (sharehoderdataform.getMinorCode() == null || sharehoderdataform.getMinorCode().isEmpty()) {
			throw new Exception("Minor code should not be null or empty");
		}
	}

	@Override
	public ShareHolderDataFromEntity getShareHolderDataFormById(String shareid)
			throws SQLException, ShareHolderDataFromServiceException {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			if (shareid == null || shareid.isEmpty()) {
				System.out.print("ShareHolderdataForm id must not be null or empty");
			}
			ShareHolderDataFromEntity cashFlowEntity = this.shareholderdataformdao.getShareHolderDataFormById(shareid,
					dataBaseName);
			return cashFlowEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderDataFromServiceException("unable to share holder data" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareHolderDataFromEntity> getShareHolderDataFormDetails()
			throws SQLException, ShareHolderDataFromServiceException {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			List<ShareHolderDataFromEntity> shareholderdataformEntity = this.shareholderdataformdao
					.getAllShareHolderDataFormDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray shareholderdataformJsonData = getJSONFromShareHolderDataFormList(shareholderdataformEntity);
			object.put(shareholderdataformEntity, shareholderdataformJsonData);
			return shareholderdataformEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderDataFromServiceException("unable to get share holder list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromShareHolderDataFormList(List<ShareHolderDataFromEntity> shareholderdataformEntity) {
		JSONArray array = new JSONArray();
		for (ShareHolderDataFromEntity shareholder : shareholderdataformEntity) {
			JSONObject object = buildJsonFromShareHolderDataForm(shareholder);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromShareHolderDataForm(ShareHolderDataFromEntity shareholder) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ShareHolderDataFromQuaryConstant.SHAREID, shareholder.getShareId());
		jsonObject.put(ShareHolderDataFromQuaryConstant.CLIENTNAME, shareholder.getClientName());
		jsonObject.put(ShareHolderDataFromQuaryConstant.FOLIO, shareholder.getFolio());
		jsonObject.put(ShareHolderDataFromQuaryConstant.SHAREHOLDERNAME, shareholder.getShareholderName());
		jsonObject.put(ShareHolderDataFromQuaryConstant.HOLDINGVALUE, shareholder.getHoldingValue());
		jsonObject.put(ShareHolderDataFromQuaryConstant.HOLDINGPERCENTAGE, shareholder.getHoldingPercentage());
		jsonObject.put(ShareHolderDataFromQuaryConstant.MINORCODE, shareholder.getMinorCode());
		jsonObject.put(ShareHolderDataFromQuaryConstant.DATE, shareholder.getDate());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShareHolderDataFromEntity updateShareHolderDataForm(ShareHolderDataFromEntity sharehoderdataform,
			String shareid) throws ShareHolderDataFromServiceException {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			if (shareid == null || shareid.isEmpty()) {
				System.out.println("share holder data form id can't be null or empty ");

			}
			ShareHolderDataFromEntity updatedshareholder = this.shareholderdataformdao
					.updateShareHolderDAtaFormDetails(sharehoderdataform, shareid, dataBaseName);
			JSONObject object = new JSONObject();
			JSONObject jsonFromUser = buildJsonFromUpdatedShareHolderDataForm(updatedshareholder);
			object.put(updatedshareholder, jsonFromUser);
			return updatedshareholder;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderDataFromServiceException("unable to update share holder data" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromUpdatedShareHolderDataForm(ShareHolderDataFromEntity updatedshareholderdataform) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ShareHolderDataFromQuaryConstant.SHAREID, updatedshareholderdataform.getShareId());
		jsonObject.put(ShareHolderDataFromQuaryConstant.CLIENTNAME, updatedshareholderdataform.getClientName());
		jsonObject.put(ShareHolderDataFromQuaryConstant.FOLIO, updatedshareholderdataform.getFolio());
		jsonObject.put(ShareHolderDataFromQuaryConstant.SHAREHOLDERNAME,
				updatedshareholderdataform.getShareholderName());

		jsonObject.put(ShareHolderDataFromQuaryConstant.HOLDINGVALUE, updatedshareholderdataform.getHoldingValue());

		jsonObject.put(ShareHolderDataFromQuaryConstant.HOLDINGPERCENTAGE,
				updatedshareholderdataform.getHoldingPercentage());

		jsonObject.put(ShareHolderDataFromQuaryConstant.MINORCODE, updatedshareholderdataform.getMinorCode());

		jsonObject.put(ShareHolderDataFromQuaryConstant.DATE, updatedshareholderdataform.getDate());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShareHolderDataFromEntity deleteShareHoderDataForm(String shareid)
			throws ShareHolderDataFromServiceException {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			if (shareid == null || shareid.isEmpty()) {
				System.out.println("share holder data form cannot be null or empty");

			}
			ShareHolderDataFromEntity shareholderdataform = shareholderdataformdao.getShareHolderDataFormById(shareid,
					dataBaseName);
			if (shareholderdataform == null) {

			}
			this.shareholderdataformdao.deleteShareHolderDataForm(shareid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "share holder data form deleted suucessfully");
			return shareholderdataform;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderDataFromServiceException("unable to delete share holder data" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String uploadShareHolderDataExcelSheet(String createdBy,CompletedFileUpload file) throws ShareHolderDataFromServiceException, ClassNotFoundException {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			String list=this.shareholderdataformdao.uploadShareHolderDataExcelSheet(createdBy,file, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entityAuditHistoryEntity=new AuditHistoryEntity();
			entityAuditHistoryEntity.setCreatedBy(createdBy);
			entityAuditHistoryEntity.setActivity("add shareholderData");
			entityAuditHistoryEntity.setDescription("add shareholderData using template");
			entityAuditHistoryEntity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entityAuditHistoryEntity);
			String status="success";
			String region="no reason";
				String msg="The 'Shareholder data' file which you have uploaded has processed successfully, kindly refresh the application and check.";
			mailService.uploadShareHolderDataExcelSheetToSendMail(msg,createdBy,status,region);
			
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			String status="fail";
			String region=e.getMessage();
			String msg="The 'Shareholder data' file which you have uploaded has failed.";
			mailService.uploadShareHolderDataExcelSheetNOTUploadToSendMail(msg,createdBy,status,region);
			throw new ShareHolderDataFromServiceException(e.getMessage());
		}
	}

	@Override
	public String addMinorCode(MinorCodeEntity codeEntity) throws ShareHolderDataFromServiceException {

            try
            {
            	String dataBaseName=ShareHolderDataFromServiceImpl.dataBaseName();
            	String response=this.shareholderdataformdao.addMinorCode(codeEntity,dataBaseName);
            	return response;
            }
            catch (Exception e) {
            	
            	throw new ShareHolderDataFromServiceException(e.getMessage());
			}
	}

	@Override
	public ArrayList<MinorCodeEntity> getMinorCodeList() throws ShareHolderDataFromServiceException {
   try
   {
		String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
		ArrayList<MinorCodeEntity> list=this.shareholderdataformdao.getMinorCodeList(dataBaseName);
		return list;
   }
   catch (Exception e) {
	   
	 	throw new ShareHolderDataFromServiceException(e.getMessage());
}
   
	}


	public ArrayList<ShareHolderDataFromEntity> getCurrentDateData(long startTimestamp, long endTimestamp) {
		
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			ArrayList<ShareHolderDataFromEntity> list=this.shareholderdataformdao.getCurretDateData(startTimestamp,endTimestamp,dataBaseName);
			System.out.println("list."+list.toString());
			return list;
			
	}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<String> getMinorCodeNames() {
		try {
			String dataBaseName = ShareHolderDataFromServiceImpl.dataBaseName();
			ArrayList<String> list=this.shareholderdataformdao.getMinorCodeNames(dataBaseName);
			return list;
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		return null;
	}
}


