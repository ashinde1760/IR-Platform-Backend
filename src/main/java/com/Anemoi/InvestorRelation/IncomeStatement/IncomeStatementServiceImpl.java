package com.Anemoi.InvestorRelation.IncomeStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowServiceImpl;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class IncomeStatementServiceImpl implements IncomeStatementService {

	@Inject
	private IncomeStatementDao incomestatementdao;

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
	public IncomeStatementEntity createIncomeStatement(IncomeStatementEntity incomestatement)
			throws SQLException, IncomeStatementServiceException {
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();

			// applyValidation(incomestatement);

			IncomeStatementEntity createIncomeStatement = this.incomestatementdao.createIncomeStatement(incomestatement,
					dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity =new AuditHistoryEntity();
			entity.setCreatedBy(incomestatement.getCreatedBy());
			entity.setActivity("Line Item Added");
			entity.setDescription("New Line Item Added in Master Income statement");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			return createIncomeStatement;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementServiceException(e.getMessage());
		}

	}

	private void applyValidation(IncomeStatementEntity incomestatement) throws Exception {
		// TODO Auto-generated method stub
		if (incomestatement.getLineItem() == null || incomestatement.getLineItem().isEmpty()) {
			throw new Exception("line item should not be null or empty");
		}
		if (incomestatement.getAlternativeName() == null || incomestatement.getAlternativeName().isEmpty()) {
			throw new Exception("alternavtive should not be null or empty");
		}

	}

	@Override
	public IncomeStatementEntity getIncomeStatementById(String incomeid)
			throws SQLException, IncomeStatementServiceException {
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();

			IncomeStatementEntity incomestatementEntity = this.incomestatementdao.getIncomeStatementById(incomeid,
					dataBaseName);
			return incomestatementEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementServiceException("unable to get income statement by id" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeStatementEntity> getAllIncomeStatementDetails()
			throws SQLException, IncomeStatementServiceException {
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();
			List<IncomeStatementEntity> incomestatementEntity = this.incomestatementdao
					.getAllIncomeStatementDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray incomestatementlJsonData = getJSONFromIncomestatemtList(incomestatementEntity);
			object.put(incomestatementEntity, incomestatementlJsonData);
			return incomestatementEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementServiceException("unable to get income statement list" + e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromIncomestatemtList(List<IncomeStatementEntity> incomestatementEntity) {
		JSONArray array = new JSONArray();
		for (IncomeStatementEntity incomestatement : incomestatementEntity) {
			JSONObject object = buildJsonIncomeStatement(incomestatement);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonIncomeStatement(IncomeStatementEntity incomestatement) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(IncomeStatementQuaryConstatnt.INCOMEID, incomestatement.getIncomeid());
		jsonObject.put(IncomeStatementQuaryConstatnt.LINEITEM, incomestatement.getLineItem());
		jsonObject.put(IncomeStatementQuaryConstatnt.ALTERNATIVENAME, incomestatement.getAlternativeName());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IncomeStatementEntity updateIncomeStatement(IncomeStatementEntity incomestatement, String incomeid)
			throws IncomeStatementServiceException {
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();

			IncomeStatementEntity incomestatement1 = this.incomestatementdao
					.updateIncomeStatementDetails(incomestatement, incomeid, dataBaseName);
		
			
			Date d=new Date();
			AuditHistoryEntity entity =new AuditHistoryEntity();
			entity.setCreatedBy(incomestatement.getCreatedBy());
			entity.setActivity("Update LineItem");
			entity.setDescription("Update line Item in  Master Income statement");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			JSONObject object = new JSONObject();
			JSONObject jsonFromincomestatement = buildJsonIncomeStatement(incomestatement);
			object.put(incomestatement1, jsonFromincomestatement);
			return incomestatement1;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public IncomeStatementEntity deleteIncomeStatement(String incomeid) throws IncomeStatementServiceException {
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();

			IncomeStatementEntity incomestatement = incomestatementdao.getIncomeStatementById(incomeid, dataBaseName);
			if (incomestatement == null) {

			}
			this.incomestatementdao.deleteIncomeStatement(incomeid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "income statement deleted suucessfully");
			return incomestatement;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementServiceException("unable to delete income statement" + e.getMessage());
		}

	}

	@Override
	public ArrayList<IncomeStatementEntity> addIncomestatmentObject(ArrayList<IncomeStatementEntity> incomeentity)
			throws IncomeStatementServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();
			ArrayList<IncomeStatementEntity> incomeStatementEntity = this.incomestatementdao
					.addIncomeStatementObject(incomeentity, dataBaseName);
			
			String createdBy=null;
			for( IncomeStatementEntity incomestatement :incomeStatementEntity)
			{
				createdBy=incomestatement.getCreatedBy();
			}
			Date d=new Date();
			AuditHistoryEntity entity =new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("Line Item Added");
			entity.setDescription("New Line Item Added in Master Income statement");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			return incomeStatementEntity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new IncomeStatementServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<IncomeStatementEntity> uploadExcelSheetIncomeTable(String createdBy,CompletedFileUpload file) throws IncomeStatementServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();
		ArrayList<IncomeStatementEntity> list=	this.incomestatementdao.uploadExcelSheetIncomeTable(createdBy,file, dataBaseName);
		
		Date d=new Date();
		AuditHistoryEntity entity =new AuditHistoryEntity();
		entity.setCreatedBy(createdBy);
		entity.setActivity("Line Item Added");
		entity.setDescription("New Line Items Added in Master Income statement");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		
		return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new IncomeStatementServiceException(e.getMessage());
		}
	}

	@Override
	public ArrayList<IncomeStatementEntity> getIncomesheetLineItems(long startTimestamp, long endTimestamp) {
		try {
			String dataBaseName = IncomeStatementServiceImpl.dataBaseName();
			ArrayList<IncomeStatementEntity> list=this.incomestatementdao.getIncomeLineItems(startTimestamp,endTimestamp,dataBaseName);
			System.out.println("list."+list.toString());
			return list;
			
	}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
