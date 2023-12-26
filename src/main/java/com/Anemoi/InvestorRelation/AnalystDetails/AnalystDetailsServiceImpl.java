package com.Anemoi.InvestorRelation.AnalystDetails;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetServiceImpl;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AnalystDetailsServiceImpl implements AnalystDetailsService {

	@Inject
	AnalystDetailsDao analystDao;
	
	@Inject
	private AuditHistoryService auditHistoryService;

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	private static String DATABASENAME = "databasename";

	private static String dataBaseName() {
		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;
	}

	@Override
	public AnalystDetailsEntity createAnalystDetails(AnalystDetailsEntity analystdetails)
			throws AnalystDetailsServiceException {
		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();
			   for (AnalystContactDetails contact : analystdetails.getAnalystContactDetails()) {
	                String email = contact.getPocEmailId();
	                System.out.println("email"+email);
	                boolean exists = this.analystDao.checkEmailExistsInDatabase( email,dataBaseName);
	                System.out.println("exists" +exists);
	                if (exists) {
	                	
	                	throw new AnalystDetailsServiceException(email+" email already exists in the database");

	                } else {
	                    System.out.println("Email '" + email + "' does not exist in the database.");
	                }
			   }
			
			AnalystDetailsEntity analystentity = this.analystDao.createAnalystDetails(analystdetails, dataBaseName);
		
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(analystdetails.getCreatedBy());
			entity.setActivity("Analystdetails Added");
			entity.setDescription(" added Analyst Details add in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
		
			return analystentity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystDetailsServiceException(e.getMessage());

		}

	}

	@Override
	public AnalystDetailsEntity getAnalystDetailsById(long analystId) throws AnalystDetailsServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();
			AnalystDetailsEntity analystEntity = this.analystDao.getAnalystDetailsById(analystId, dataBaseName);
			return analystEntity;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystDetailsServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AnalystDetailsModified> getAllAnalystDetails() throws AnalystDetailsServiceException {

		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();
			ArrayList<AnalystDetailsModified> analystEntity = this.analystDao.getAllAnalystDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray analystDetailsJsonData = getJSONFromAnalystDetailstList(analystEntity);
			object.put(analystEntity, analystDetailsJsonData);
			return analystEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromAnalystDetailstList(ArrayList<AnalystDetailsModified> analystEntity) {
		JSONArray array = new JSONArray();
		for (AnalystDetailsModified analyst : analystEntity) {
			JSONObject object = buildJsonFromAnalystEntity(analyst);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromAnalystEntity(AnalystDetailsModified analyst) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(AnalystDetailsQueryConstant.ANALYSTID, analyst.getAnalystId());
		jsonObject.put(AnalystDetailsQueryConstant.ANALYSTNAME, analyst.getAnalystName());
		jsonObject.put(AnalystDetailsQueryConstant.POCNAME, analyst.getPocName());
//		jsonObject.put(AnalystDetailsQueryConstant.POCNAME, analyst.getPocName());
		jsonObject.put(AnalystDetailsQueryConstant.POCEMAILID, analyst.getPocEmailId());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AnalystDetailsEntity updateAnalystDetails(AnalystDetailsEntity analystDetails, long analystId)
			throws AnalystDetailsServiceException {
		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();
			
			for (AnalystContactDetails contact : analystDetails.getAnalystContactDetails()) {
                String email = contact.getPocEmailId();
                boolean exists = this.analystDao.checkEmailExists( email,analystId,dataBaseName);
                System.out.println("exists" +exists);
                if (exists) {
                	
                	throw new AnalystDetailsServiceException(email+" email already exists in the database");

                } else {
                    System.out.println("Email '" + email + "' does not exist in the database.");
                }
		   }
		
			AnalystDetailsEntity analystEntity = this.analystDao.updateAnalystDetails(analystDetails, analystId,
					dataBaseName);
			
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(analystDetails.getModifiedBy());
			entity.setActivity("Update AnalystDetails");
			entity.setDescription("Analyst Details updated in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			
			JSONObject object = new JSONObject();
//			JSONObject jsonfromAnalyst = buildJsonFromAnalystEntity(analystEntity);
//			object.put(analystEntity, jsonfromAnalyst);
			return analystEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public AnalystDetailsEntity deleteAnalystDetails(long analystId,String createdBy) {
		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();
		
			AnalystDetailsEntity analystEntity = this.analystDao.getAnalystDetailsById(analystId, dataBaseName);
			if (analystEntity == null) {

			}
			this.analystDao.deleteAnalystDetails(analystId, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("delete AnalystDetails");
			entity.setDescription("Analyst Details delete in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "analyst deleted suucessfully");
			return analystEntity;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AnalystDetailsEntity> uploadAnalstDetailsExcelSheet(String createdBy,CompletedFileUpload file) throws AnalystDetailsServiceException,Exception {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();
		ArrayList<AnalystDetailsEntity> list=	this.analystDao.uploadAnalystDetailsExcelSheet(createdBy,file, dataBaseName);
		
		Date d=new Date();
		AuditHistoryEntity entity=new AuditHistoryEntity();
		entity.setCreatedBy(createdBy);
		entity.setActivity("Add AnalystDetails");
		entity.setDescription("Add analyst details using template ");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		
		
		return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystDetailsServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteAnalystContactDetails(long analystId, long analystcontactid)
			throws AnalystDetailsServiceException {
		try {
			String dataBaseName = AnalystDetailsServiceImpl.dataBaseName();

			String response = this.analystDao.deleteAnalystContactDetails(analystId, analystcontactid,dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "analyst contact details deleted successfully");
			return reposneJSON.toString();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystDetailsServiceException(e.getMessage());

		}
	}

}
