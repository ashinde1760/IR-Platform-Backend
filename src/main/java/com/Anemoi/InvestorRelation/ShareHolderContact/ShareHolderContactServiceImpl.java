package com.Anemoi.InvestorRelation.ShareHolderContact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowServiceException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowServiceImpl;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ShareHolderContactServiceImpl implements ShareHolderContactService {

	@Inject
	private ShareHolderContactDao shareholdercontactdao;
	
	@Inject
	private AuditHistoryService auditHistoryService;

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
	public ShareHolderContactEntity createShareHolderContact(ShareHolderContactEntity shareholdercontact)
			throws ShareHolderContactServiceException {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();

			// applyValidation(shareholdercontact);

			ShareHolderContactEntity createNewShareHoldercontact = this.shareholdercontactdao
					.createNewShareHolderContact(shareholdercontact, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entityAuditHistoryEntity=new AuditHistoryEntity();
			entityAuditHistoryEntity.setCreatedBy(shareholdercontact.getCreatedBy());
			entityAuditHistoryEntity.setActivity("add shareHolderContact ");
			entityAuditHistoryEntity.setDescription("add shareHolderContact in application");
			entityAuditHistoryEntity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entityAuditHistoryEntity);
			
			return createNewShareHoldercontact;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactServiceException( e.getMessage());
		}

	}

	private void applyValidation(ShareHolderContactEntity shareholdercontact) throws Exception {
		// TODO Auto-generated method stub
		Pattern pattern;
		if (shareholdercontact.getName() == null || shareholdercontact.getName().isEmpty()) {
			throw new Exception("name should not be null or empty");
		}
		pattern = Pattern.compile("^[A-Z]{1}+[A-Za-z ]*$");
		boolean result = pattern.matcher(shareholdercontact.getName()).matches();
		if (!result) {
			throw new Exception("please enter the valid name formate");
		}

		if (shareholdercontact.getPoc() == null || shareholdercontact.getPoc().isEmpty()) {
			throw new Exception("poc should not be null or empty");
		}

		if (shareholdercontact.getEmail() == null || shareholdercontact.getEmail().isEmpty()) {
			throw new Exception("email should not be null or empty");
		}
		String emailRegex = "([a-zA-Z0-9]+)([\\.{1}])?([a-zA-Z0-9]+)\\@(?:gmail|GMAIL)([\\.])(?:com|COM)";
		pattern = Pattern.compile(emailRegex);
		boolean result3 = pattern.matcher(shareholdercontact.getEmail()).matches();
		if (!result3) {
			throw new Exception(shareholdercontact.getEmail() + "please enter valid email formate");
		}

		if (shareholdercontact.getAddress() == null || shareholdercontact.getAddress().isEmpty()) {
			throw new Exception("address should not be null or empty");
		}

		if (shareholdercontact.getContact() == null || shareholdercontact.getContact().isEmpty()) {
			throw new Exception("contact should not be null or empty");
		}
		pattern = Pattern.compile("[7-9]{1}[0-9]{9}");
		boolean result2 = pattern.matcher(shareholdercontact.getContact()).matches();
		if (!result2) {
			throw new Exception("please enter the valid contact number format");
		}
	}

	@Override
	public ShareHolderContactEntity getShareHolderContactById(String contactid)
			throws SQLException, ShareHolderContactServiceException {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();
			if (contactid == null || contactid.isEmpty()) {
				System.out.print("ShareHoldercontact id must not be null or empty");
			}
			ShareHolderContactEntity shareholdercontactEntity = this.shareholdercontactdao
					.getShareHolderContactById(contactid, dataBaseName);
			return shareholdercontactEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactServiceException("unable to get share holder by id" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareHolderContactEntity> getAllShareholderContactDetails()
			throws SQLException, ShareHolderContactServiceException {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();
			List<ShareHolderContactEntity> shareholdercontactEntity = this.shareholdercontactdao
					.getAllShareHolderContactDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray shareholdercontactJsonData = getJSONFromShareHolderContactList(shareholdercontactEntity);
			object.put(shareholdercontactEntity, shareholdercontactJsonData);
			return shareholdercontactEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactServiceException("unable to get share holder list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromShareHolderContactList(List<ShareHolderContactEntity> shareholdercontactEntity) {
		JSONArray array = new JSONArray();
		for (ShareHolderContactEntity shareholdercontact : shareholdercontactEntity) {
			JSONObject object = buildJsonFromShareHolderContact(shareholdercontact);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromShareHolderContact(ShareHolderContactEntity shareholdercontact) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ShareHolderContactQuaryConstant.CONTACTID, shareholdercontact.getContactid());
		jsonObject.put(ShareHolderContactQuaryConstant.NAME, shareholdercontact.getName());
		jsonObject.put(ShareHolderContactQuaryConstant.POC, shareholdercontact.getPoc());
		jsonObject.put(ShareHolderContactQuaryConstant.EMAIL, shareholdercontact.getEmail());
		jsonObject.put(ShareHolderContactQuaryConstant.ADDRESS, shareholdercontact.getAddress());
		jsonObject.put(ShareHolderContactQuaryConstant.CONTACT, shareholdercontact.getContact());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShareHolderContactEntity updateShareHolderContact(ShareHolderContactEntity shareholdercontact,
			String contactid) throws ShareHolderContactServiceException {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();
			if (contactid == null || contactid.isEmpty()) {
				System.out.println("share holder contact id can't be null or empty ");

			}
			ShareHolderContactEntity shareholder = this.shareholdercontactdao
					.updateShareHolderContactDetails(shareholdercontact, contactid, dataBaseName);
			JSONObject object = new JSONObject();
			JSONObject jsonFromcontact = buildJsonFromUpdatedShareHolderContact(shareholdercontact);
			object.put(shareholdercontact, jsonFromcontact);
			return shareholder;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactServiceException("unable to update share holder" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromUpdatedShareHolderContact(ShareHolderContactEntity updatedshareholdercontact) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ShareHolderContactQuaryConstant.CONTACTID, updatedshareholdercontact.getContactid());
		jsonObject.put(ShareHolderContactQuaryConstant.NAME, updatedshareholdercontact.getName());
		jsonObject.put(ShareHolderContactQuaryConstant.POC, updatedshareholdercontact.getPoc());
		jsonObject.put(ShareHolderContactQuaryConstant.EMAIL, updatedshareholdercontact.getEmail());
		jsonObject.put(ShareHolderContactQuaryConstant.ADDRESS, updatedshareholdercontact.getAddress());

		jsonObject.put(ShareHolderContactQuaryConstant.CONTACT, updatedshareholdercontact.getContact());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShareHolderContactEntity deleteShareHolderContact(String contactid)
			throws ShareHolderContactServiceException {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();
			if (contactid == null || contactid.isEmpty()) {
				System.out.println("share holder contact cannot be null or empty");

			}
			ShareHolderContactEntity shareholderdataform = shareholdercontactdao.getShareHolderContactById(contactid,
					dataBaseName);
			if (shareholderdataform == null) {

			}
			this.shareholdercontactdao.deleteShareHolderContact(contactid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "share holder contact deleted suucessfully");
			return shareholderdataform;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactServiceException("unable to delete share holder" + e.getMessage());
		}

	}

	@Override
	public String uploadShareholderContactExcelSheet(String createdBy, CompletedFileUpload file)
			throws ShareHolderContactServiceException {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();
	String response=	this.shareholdercontactdao.uploadShareholderContactExcelsheet(createdBy,file, dataBaseName);
		
		Date d=new Date();
		AuditHistoryEntity entity =new AuditHistoryEntity();
		entity.setCreatedBy(createdBy);
		entity.setActivity("Contact Details Added");
		entity.setDescription("New Contact Details added in Master ShareHolder Contact");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		
		return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ShareHolderContactServiceException(e.getMessage());
		}
	}
	


	public ArrayList<ShareHolderContactEntity> getCurrentDateData(long startTimestamp, long endTimestamp) {
		try {
			String dataBaseName = ShareHolderContactServiceImpl.dataBaseName();
			ArrayList<ShareHolderContactEntity> list=this.shareholdercontactdao.getCurrentDateDatabydate(startTimestamp,endTimestamp,dataBaseName);
			return list;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
