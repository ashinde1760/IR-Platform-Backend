package com.Anemoi.InvestorRelation.CashFlow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CashFlowServiceImpl implements CashFlowService {

	@Inject
	private CashFlowDao cashflowdao;
	
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
	public CashFlowEntity createCashFlow(CashFlowEntity cashflow) throws SQLException, CashFlowServiceException {
		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();

			// applyValidation(cashflow);

			CashFlowEntity createNewCashFlow = this.cashflowdao.createNewCashFlow(cashflow, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity =new AuditHistoryEntity();
			entity.setCreatedBy(cashflow.getCreatedBy());
			entity.setActivity("Line Item Added");
			entity.setDescription("New Line Item Added in Master Cash flow");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			
			return createNewCashFlow;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowServiceException(e.getMessage());
		}

	}

	private void applyValidation(CashFlowEntity cashflow) throws Exception {
		// TODO Auto-generated method stub
		if (cashflow.getLineItem() == null || cashflow.getLineItem().isEmpty()) {
			throw new Exception("line item should not be null or empty");
		}
		if (cashflow.getAlternativeName() == null || cashflow.getAlternativeName().isEmpty()) {
			throw new Exception("alternative name should not be null or empty");
		}
		
	}

	@Override
	public CashFlowEntity getCashFlowById(String cashid) throws SQLException, CashFlowServiceException {
		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
			if (cashid == null || cashid.isEmpty()) {
				System.out.print("Cash Flow id must not be null or empty");
			}
			CashFlowEntity cashFlowEntity = this.cashflowdao.getCashFlowById(cashid, dataBaseName);
			return cashFlowEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowServiceException("unable to cash flow by id" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CashFlowEntity> getAllCashFlowDetails() throws SQLException, CashFlowServiceException {

		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
			List<CashFlowEntity> cashFlowEntity = this.cashflowdao.getAllCashFlowDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray cashFlowJsonData = getJSONFromCashFlowList(cashFlowEntity);
			object.put(cashFlowEntity, cashFlowJsonData);
			return cashFlowEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowServiceException("unable to get cash flow list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromCashFlowList(List<CashFlowEntity> cashFlowEntity) {
		JSONArray array = new JSONArray();
		for (CashFlowEntity balance : cashFlowEntity) {
			JSONObject object = buildJsonFromcashFlow(balance);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromcashFlow(CashFlowEntity cashflow) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CashFlowQuaryConstant.CASHID, cashflow.getCashId());
		jsonObject.put(CashFlowQuaryConstant.LINE_ITEM, cashflow.getLineItem());
		jsonObject.put(CashFlowQuaryConstant.ALTERNATIVE_NAME, cashflow.getAlternativeName());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CashFlowEntity updateCashFlow(CashFlowEntity cashflow, String cashid) throws CashFlowServiceException {
		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
			if (cashid == null || cashid.isEmpty()) {
				System.out.println("cash flow id can't be null or empty ");

			}
			CashFlowEntity updatedcashflow = this.cashflowdao.updateCashFlowDetails(cashflow, cashid, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setActivity("Update Cash Flow");
			entity.setCreatedBy(cashflow.getModifiedBy());
			entity.setDescription("Update Cash Flow details in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			JSONObject object = new JSONObject();
			JSONObject jsonFromUser = buildJsonFromUpdatedCashFlow(updatedcashflow);
			object.put(updatedcashflow, jsonFromUser);
			return updatedcashflow;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromUpdatedCashFlow(CashFlowEntity updatedcashflow) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CashFlowQuaryConstant.CASHID, updatedcashflow.getCashId());
		jsonObject.put(CashFlowQuaryConstant.LINE_ITEM, updatedcashflow.getLineItem());
		jsonObject.put(CashFlowQuaryConstant.ALTERNATIVE_NAME, updatedcashflow.getAlternativeName());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CashFlowEntity deleteCashFlow(String cashid) throws CashFlowServiceException {
		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
			if (cashid == null || cashid.isEmpty()) {
				System.out.println("cash flow cannot be null or empty");

			}
			CashFlowEntity user = cashflowdao.getCashFlowById(cashid, dataBaseName);
			if (user == null) {

			}
			this.cashflowdao.deleteCashFlow(cashid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "cash flow deleted successfully");
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowServiceException("unable to delete cash flow details" + e.getMessage());
		}

	}

	@Override
	public ArrayList<CashFlowEntity> addCashFlowObject(ArrayList<CashFlowEntity> cashFlowEntity)
			throws CashFlowServiceException {
		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
			ArrayList<CashFlowEntity> cashEntity = this.cashflowdao.addCashFlowEntityObject(cashFlowEntity, dataBaseName);
		
			String createdBy=null;
			for(CashFlowEntity cash:cashEntity)
			{
				createdBy=cash.getCreatedBy();
			}
			Date d=new Date();
			AuditHistoryEntity entity =new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("Line Item Added");
			entity.setDescription("New Line Item Added in Master Cash flow");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			
			return cashEntity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new CashFlowServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<CashFlowEntity> uploadCashFlowExcelSheet(String createdBy,CompletedFileUpload file) throws CashFlowServiceException {
		// TODO Auto-generated method stub

		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
		ArrayList<CashFlowEntity> responslist=	this.cashflowdao.uploadCashFlowExcelSheet(createdBy,file, dataBaseName);
		
		Date d=new Date();
		AuditHistoryEntity entity =new AuditHistoryEntity();
		entity.setCreatedBy(createdBy);
		entity.setActivity("Line Item Added");
		entity.setDescription("New Line Item Added in Master Cash flow");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		
		return responslist;
		} catch (Exception e) {
			// TODO: handle exception
			throw new CashFlowServiceException(e.getMessage());
		}
	}

	@Override
	public ArrayList<CashFlowEntity> getlineItemForToday(long startTimestamp, long endTimestamp) {
		try {
			String dataBaseName = CashFlowServiceImpl.dataBaseName();
			ArrayList<CashFlowEntity> list=this.cashflowdao.getLineItemListForToday(startTimestamp,endTimestamp,dataBaseName);
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
