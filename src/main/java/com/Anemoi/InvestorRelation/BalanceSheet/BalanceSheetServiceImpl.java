package com.Anemoi.InvestorRelation.BalanceSheet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class BalanceSheetServiceImpl implements BalanceSheetService {
	
	@Inject
	private BalanceSheetDao balancesheetdao;
	
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
	public BalanceSheetEntity createNewBalanceSheetForm(BalanceSheetEntity balance)
			throws BalanceSheetServiceException {

		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();

			// applyValidation(balance);

			BalanceSheetEntity createNewBalanceSheetForm = this.balancesheetdao.createNewBalanceSheetForm(balance,
					dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(balance.getCreatedBy());
			entity.setActivity("Line Item Added");
			entity.setDescription("New LineItem Added in Master Balance Sheet");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			return createNewBalanceSheetForm;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetServiceException(e.getMessage());
		}

	}

	private void applyValidation(BalanceSheetEntity balance) throws Exception {
		if (balance.getLineItem() == null || balance.getLineItem().isEmpty()) {
			throw new Exception("line item should not be null or empty");
		}
		if (balance.getAlternativeName() == null || balance.getAlternativeName().isEmpty()) {
			throw new Exception("Alternative name should not be null or empty");
		}

	}

	@Override
	public BalanceSheetEntity getBalancesheetById(String balanceid) throws BalanceSheetServiceException {

		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();
			if (balanceid == null || balanceid.isEmpty()) {
				System.out.print("Balance id must not be null or empty");
			}
			BalanceSheetEntity balanceEntity = this.balancesheetdao.getBalanceById(balanceid, dataBaseName);
			return balanceEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetServiceException("unable to get balance sheet details by id" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BalanceSheetEntity> getAllBalanceSheetDetails() throws BalanceSheetServiceException {

		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();
			List<BalanceSheetEntity> balanceEntity = this.balancesheetdao.getAllBalanceSheetDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray balancesheetJsonData = getJSONFromBalanceSheetList(balanceEntity);
			object.put(balanceEntity, balancesheetJsonData);
			return balanceEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetServiceException("unable to get balance sheet list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromBalanceSheetList(List<BalanceSheetEntity> balanceEntity) {
		JSONArray array = new JSONArray();
		for (BalanceSheetEntity balance : balanceEntity) {
			JSONObject object = buildJsonFrombalanceSheet(balance);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFrombalanceSheet(BalanceSheetEntity balance) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(BalanceSheetQueryConstant.BALANCEID, balance.getBalanceid());
		jsonObject.put(BalanceSheetQueryConstant.LINE_ITEM, balance.getLineItem());
		jsonObject.put(BalanceSheetQueryConstant.ALTERNATIVE_NAME, balance.getAlternativeName());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BalanceSheetEntity updateBalanceSheetForm(BalanceSheetEntity balance, String balanceid)
			throws BalanceSheetServiceException {
		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();
			if (balanceid == null || balanceid.isEmpty()) {
				System.out.println("balance sheet id can't be null or empty ");

			}
			BalanceSheetEntity balancesheet = this.balancesheetdao.updateBalanceSheetDetails(balance, balanceid,
					dataBaseName);
			
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setActivity("Update Balance Sheet");
			entity.setCreatedBy(balance.getModifiedBy());
			entity.setDescription("Update Balance sheet in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			JSONObject object = new JSONObject();
			JSONObject jsonFrombalancesheet = buildJsonFrombalanceSheet(balance);
			object.put(balancesheet, jsonFrombalancesheet);
			return balancesheet;

		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public BalanceSheetEntity deleteBalanceSheet(String balanceid) throws BalanceSheetServiceException {
		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();
			if (balanceid == null || balanceid.isEmpty()) {
				System.out.println("balance sheet cannot be null or empty");

			}
			BalanceSheetEntity incomestatement = balancesheetdao.getBalanceById(balanceid, dataBaseName);
			if (incomestatement == null) {

			}
			this.balancesheetdao.deleteBalanceSheet(balanceid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "balance sheet deleted suucessfully");
			return incomestatement;

		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetServiceException("enter valid request" + e.getMessage());
		}

	}

	@Override
	public ArrayList<BalanceSheetEntity> addObjectBalanceSheetEntity(ArrayList<BalanceSheetEntity> balanceSheetEntity)
			throws BalanceSheetServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();
			ArrayList<BalanceSheetEntity> balanceEntity = this.balancesheetdao.addObjectBalanceSheetEntity(balanceSheetEntity,
					dataBaseName);
			
			String createdBy=null;
			for(BalanceSheetEntity b:balanceEntity)
			{
				createdBy=b.getCreatedBy();
				
			}
			Date d=new Date();
			AuditHistoryEntity entity =new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("Line Item Added");
			entity.setDescription("New Line Item Added in Master Balance Sheet");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			
			
			return balanceEntity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new BalanceSheetServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BalanceSheetEntity> uploadBalanceSheetExcel(String createdBy,CompletedFileUpload file) throws BalanceSheetServiceException {
		// TODO Auto-generated method stub

		try {

//			validate(file);		will work on it		

			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();

		ArrayList<BalanceSheetEntity> presentlist=	this.balancesheetdao.uploadBalanceSheetExcel(createdBy,file, dataBaseName);
		
		
		Date d=new Date();
		AuditHistoryEntity entity =new AuditHistoryEntity();
		entity.setCreatedBy(createdBy);
		entity.setActivity("Line Item Added");
		entity.setDescription("New Line Item Added in Master Balance Sheet");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		
		
			return presentlist;

		} catch (Exception e) {
			// TODO: handle exception
			throw new BalanceSheetServiceException(e.getMessage());
		}

	}

	@Override
	public ArrayList<BalanceSheetEntity> getBalanceSheetLineItem(long startTimestamp, long endTimestamp) {
		try {
			String dataBaseName = BalanceSheetServiceImpl.dataBaseName();
			ArrayList<BalanceSheetEntity> list=this.balancesheetdao.getBalanceSheetLineItem(startTimestamp,endTimestamp,dataBaseName);
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
