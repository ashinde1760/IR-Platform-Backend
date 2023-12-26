package com.Anemoi.InvestorRelation.AnalystLineItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AnalystLineItemServiceImpl implements AnalystLineItemService {

	@Inject
	private AnalystLineItemDao analystLineItemDao;
	
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
	public AnalystLineItemEntity createAnalystLineItem(AnalystLineItemEntity analystlineItem)
			throws AnalystLineItemServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();

			// applyValidation(analystlineItem);

			AnalystLineItemEntity newcreateAnalystLineItem = this.analystLineItemDao
					.createAnalystLineItem(analystlineItem, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(analystlineItem.getCreatedBy());
			entity.setActivity("Add Analyst Line Item");
			entity.setDescription("added Analyst Line Item in application ");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			return newcreateAnalystLineItem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemServiceException(e.getMessage());
		}

	}

	private void applyValidation(AnalystLineItemEntity analystlineItem) throws Exception {
		if (analystlineItem.getAnalystLineId() == null || analystlineItem.getAnalystLineId().isEmpty()) {
			throw new Exception("Analyst Id cannot be null or empty");
		}
		if (analystlineItem.getAnalystLineItemName() == null || analystlineItem.getAnalystLineItemName().isEmpty()) {
			throw new Exception("Analyst line item name cannot be null or empty");
		}
		if (analystlineItem.getAnalystTableHeaderName() == null
				|| analystlineItem.getAnalystTableHeaderName().isEmpty()) {
			throw new Exception("Analyst table header name cannot be null or empty");
		}
		if (analystlineItem.getMasterTableSource() == null || analystlineItem.getMasterTableSource().isEmpty()) {
			throw new Exception("Master table source cannot be null or empty");
		}

	}

	@Override
	public AnalystLineItemEntity getAnalystLineItemById(String aid) throws AnalystLineItemServiceException {
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			if (aid == null || aid.isEmpty()) {
				System.out.println("analystLine Item id must not be null or empty");
			}
			AnalystLineItemEntity analystLineItem = this.analystLineItemDao.getAnalystLineItemById(aid, dataBaseName);
			return analystLineItem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemServiceException("unable to get analyst line item by id" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalystLineItemEntity> getAllAnalystLineItemDetails() throws AnalystLineItemServiceException {

		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			List<AnalystLineItemEntity> analystLineItemList = this.analystLineItemDao
					.getAllAnalystLineItem(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray analystLineItemJsondata = getJsonAnalystLineItem(analystLineItemList);
			object.put(analystLineItemList, analystLineItemJsondata);
			return analystLineItemList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemServiceException("unable to get anlayst line item " + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJsonAnalystLineItem(List<AnalystLineItemEntity> analystLineItemList) {
		// TODO Auto-generated method stub
		JSONArray array = new JSONArray();
		for (AnalystLineItemEntity anylystlineitem : analystLineItemList) {
			JSONObject object = buildJsonFromAnaylstlineItem(anylystlineitem);
			array.add(object);
		}
		return array;

	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromAnaylstlineItem(AnalystLineItemEntity anylystlineitem) {
		// TODO Auto-generated method stub
		JSONObject object = new JSONObject();
		object.put(AnalystLineItemQueryConstant.ANALYSTLINEID, anylystlineitem.getAnalystLineId());
		object.put(AnalystLineItemQueryConstant.ANALYSTNAME, anylystlineitem.getAnalystName());
		object.put(AnalystLineItemQueryConstant.ANALYSTLINEITEMNAME, anylystlineitem.getAnalystLineItemName());
		object.put(AnalystLineItemQueryConstant.ANALYSTTABLEHEADERNAME, anylystlineitem.getAnalystTableHeaderName());
		object.put(AnalystLineItemQueryConstant.MASTERTABLESOURCE, anylystlineitem.getMasterTableSource());
		object.put(AnalystLineItemQueryConstant.CREATEDON, anylystlineitem.getCreatedOn());
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AnalystLineItemEntity> getbyAnalystName(String analystName, String masterTableSource)
			throws AnalystLineItemServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			ArrayList<AnalystLineItemEntity> analystlineitem = this.analystLineItemDao
					.getByanalystnameandTable(analystName, masterTableSource, dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray analystLineItemJsondata = getJsonAnalystLineItem(analystlineitem);
			object.put(analystlineitem, analystLineItemJsondata);
			return analystlineitem;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemServiceException(
					"unbale to get analyst line item by analyst name and master table source" + e.getMessage());
		}

	}

	@Override
	public String updateAnalystLineItem(AnalystDetails analystDetails) throws AnalystLineItemServiceException {
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();

			String entity = this.analystLineItemDao.updateAnalystlineItem(analystDetails, dataBaseName);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AnalystLineItemEntity> uploadAnalstLineItemExcelSheet( String createdBy,CompletedFileUpload file) throws AnalystLineItemServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			ArrayList<AnalystLineItemEntity> list=this.analystLineItemDao.uploadAnalystLineItem(createdBy,file, dataBaseName);
		
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("Add Analyst Line Item");
			entity.setDescription("added Analyst Line Item in application ");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemServiceException(e.getMessage());
		}
	}

	@Override
	public ArrayList<AnalystLineItemEntity> addMultipleObject(ArrayList<AnalystLineItemEntity> analystLineItem)
			throws AnalystLineItemServiceException {
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();

			// applyValidation(analystlineItem);

			ArrayList<AnalystLineItemEntity> newcreateAnalystLineItem = this.analystLineItemDao
					.addMultipleObject(analystLineItem, dataBaseName);
			
			String createdBy=null;
			for(AnalystLineItemEntity e:newcreateAnalystLineItem)
			{
				createdBy=e.getCreatedBy();
			}
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("Add Analyst Line Item");
			entity.setDescription("Add Analyst Line Item in application ");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			return newcreateAnalystLineItem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemServiceException(e.getMessage());
		}
	}

	@Override
	public AnalystLineItemEntity updateAnalystLineItem(AnalystLineItemEntity analystLineItem, String analystLineId)
			throws AnalystLineItemServiceException {
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			AnalystLineItemEntity entity = this.analystLineItemDao.updateAnalystLineItem(analystLineItem, analystLineId,
					dataBaseName);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemServiceException(e.getMessage());
		}
	}

	@Override
	public Set<String> getCurrentDateMapingAnalystLineItem(long startTimestamp,
			long endTimestamp) {
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			Set<String> entity = this.analystLineItemDao.getCurrentAnalystDetailsMapping(startTimestamp, endTimestamp,dataBaseName);
					
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<String> getCurrentDateAddingAnalystLineItem(long startTimestamp, long endTimestamp) {
		try {
			String dataBaseName = AnalystLineItemServiceImpl.dataBaseName();
			Set<String> entity = this.analystLineItemDao.getCurrentAddlingLineItem(startTimestamp, endTimestamp,dataBaseName);
					
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
