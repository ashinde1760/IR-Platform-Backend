package com.Anemoi.InvestorRelation.ClientLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemEntity;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemServiceException;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemServiceImpl;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ClientLineItemServiceimpl implements ClientLineItemService {

	@Inject
	ClientLineItemDao clientLineItemDao;

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
	public ClientLineItemEntity createClientLineItem(ClientLineItemEntity clientLineitem)
			throws ClientLineItemServiceException {

		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();

			// applyValidation(analystlineItem);

			ClientLineItemEntity clientDeatils = this.clientLineItemDao.createClientLineItem(clientLineitem,
					dataBaseName);

			return clientDeatils;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemServiceException(e.getMessage());

		}

	}

	@Override
	public ClientLineItemEntity getClientLiteItemById(String clientLineId) throws ClientLineItemServiceException {
		// TODO Auto-generated method stub
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
			ClientLineItemEntity entity = this.clientLineItemDao.getClientLineItemById(clientLineId, dataBaseName);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemServiceException("unable to get" + e.getMessage());
		}
	}

	@Override
	public ArrayList<ClientLineItemEntity> getclientlineItemList() throws ClientLineItemServiceException {

		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
			ArrayList<ClientLineItemEntity> enititylist = this.clientLineItemDao
					.getclientLineItemListDetails(dataBaseName);
			return enititylist;
		} catch (Exception e) {
			throw new ClientLineItemServiceException("unable to get" + e.getMessage());

		}
	}

	@Override
	public ArrayList<ClientLineItemEntity> addMultipleObjectClientLineItem(
			ArrayList<ClientLineItemEntity> clientLineItem) throws ClientLineItemServiceException {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();

			// applyValidation(analystlineItem);

			ArrayList<ClientLineItemEntity> newcreateClinetLineItem = this.clientLineItemDao
					.addMultipleObject(clientLineItem, dataBaseName);
			return newcreateClinetLineItem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemServiceException(e.getMessage());
		}
	}

	@Override
	public ArrayList<ClientLineItemEntity> getByClientName(String clientName, String masterTableSource)
			throws ClientLineItemServiceException {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
			ArrayList<ClientLineItemEntity> cliententity = this.clientLineItemDao.getByClientNameandTable(clientName,
					masterTableSource, dataBaseName);
			return cliententity;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemServiceException(
					"unbale to get client line item by client name and master table source" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateLineItemName(ClientDetails clientDetails) throws ClientLineItemServiceException {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();

			String entity = this.clientLineItemDao.updatelineItemName(clientDetails, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "line item Update Sucessfully");
			return reposneJSON.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemServiceException("unable to update  line item Name" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ClientLineItemEntity> uploadClientLineItemExcelSheet(String createdBy,CompletedFileUpload file) throws ClientLineItemServiceException {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
		ArrayList<ClientLineItemEntity> list=	this.clientLineItemDao.uploadClientLineItem(createdBy,file, dataBaseName);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemServiceException(e.getMessage());
		}
	}

	@Override
	public ClientLineItemEntity updateClientLineItem(ClientLineItemEntity clientLineItem, String clientLineId)
			throws ClientLineItemServiceException {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
			ClientLineItemEntity clientEntity = this.clientLineItemDao.updateClientLineItem(clientLineItem,
					clientLineId, dataBaseName);
			return clientEntity;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemServiceException(e.getMessage());

		}
	}

	

	@Override
	public Set<String> getclientNameClientLineItemTable(long startTimestamp, long endTimestamp) {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
			Set<String> clientItemName=this.clientLineItemDao.getCurrentDateClientItemName(startTimestamp,endTimestamp,dataBaseName);
			return clientItemName;
		}
		catch (Exception e) {
		e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientLineItemEntity> getMasterTableListWhenClinetLineItemAddition(long startTimestamp, long endTimestamp,String clientName) {
		// TODO Auto-generated method stub
		try
		{
		String dataBaseName = ClientLineItemServiceimpl.dataBaseName();
		ArrayList<ClientLineItemEntity> mastertableListAddtionForClientlineItem=this.clientLineItemDao.getMasterTablenameWhenAddClientLineitem(startTimestamp,endTimestamp,dataBaseName,clientName);
		return mastertableListAddtionForClientlineItem;
	}
	catch (Exception e) {
	e.printStackTrace();
	}
	return null;
	}

	@Override
	public ArrayList<ClientLineItemEntity> getMasterTableNameWhenMappingClientLineItem(long startTimestamp, long endTimestamp,
			String clientName) {
		try {
			String dataBaseName = ClientLineItemServiceimpl.dataBaseName();

			// applyValidation(analystlineItem);

			ArrayList<ClientLineItemEntity> tableName = this.clientLineItemDao.gettablenameWhenMappingClientLineItem(startTimestamp,endTimestamp,dataBaseName,clientName);
			return tableName;

		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}

}
