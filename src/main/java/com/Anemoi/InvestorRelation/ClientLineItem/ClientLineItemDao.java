package com.Anemoi.InvestorRelation.ClientLineItem;

import java.util.ArrayList;
import java.util.Set;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface ClientLineItemDao {

	ClientLineItemEntity createClientLineItem(ClientLineItemEntity clientLineitem, String dataBaseName)
			throws ClientLineItemDaoException;

	ClientLineItemEntity getClientLineItemById(String clientLineId, String dataBaseName)
			throws ClientLineItemDaoException;

	ArrayList<ClientLineItemEntity> getclientLineItemListDetails(String dataBaseName) throws ClientLineItemDaoException;

	ArrayList<ClientLineItemEntity> addMultipleObject(ArrayList<ClientLineItemEntity> clientLineItem,
			String dataBaseName) throws ClientLineItemDaoException;

	ArrayList<ClientLineItemEntity> getByClientNameandTable(String clientName, String masterTableSource,
			String dataBaseName) throws ClientLineItemDaoException;

	String updatelineItemName(ClientDetails clientDetails, String dataBaseName) throws ClientLineItemDaoException;

	ArrayList<ClientLineItemEntity> uploadClientLineItem(String createdBy,CompletedFileUpload file, String dataBaseName) throws ClientLineItemDaoException;

	ClientLineItemEntity updateClientLineItem(ClientLineItemEntity clientLineItem, String clientLineId,
			String dataBaseName) throws ClientLineItemDaoException;



	Set<String> getCurrentDateClientItemName(long startTimestamp, long endTimestamp, String dataBaseName);

	ArrayList<ClientLineItemEntity> getMasterTablenameWhenAddClientLineitem(long startTimestamp, long endTimestamp, String dataBaseName, String clientName);

	ArrayList<ClientLineItemEntity> gettablenameWhenMappingClientLineItem(long startTimestamp, long endTimestamp, String dataBaseName,
			String clientName);

}
