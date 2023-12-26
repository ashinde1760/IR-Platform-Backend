package com.Anemoi.InvestorRelation.AnalystLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface AnalystLineItemDao {

	AnalystLineItemEntity createAnalystLineItem(AnalystLineItemEntity lineItem, String dataBaseName)
			throws AnalystLineItemDaoException;

	AnalystLineItemEntity getAnalystLineItemById(String aid, String dataBaseName) throws AnalystLineItemDaoException;

	List<AnalystLineItemEntity> getAllAnalystLineItem(String dataBaseName) throws AnalystLineItemDaoException;

	ArrayList<AnalystLineItemEntity> getByanalystnameandTable(String analystName, String analystTableHeaderName,
			String dataBaseName) throws AnalystLineItemDaoException;

	String updateAnalystlineItem(AnalystDetails analystDetails, String dataBaseName) throws AnalystLineItemDaoException;

	ArrayList<AnalystLineItemEntity> uploadAnalystLineItem( String createdBy,CompletedFileUpload file, String dataBaseName) throws AnalystLineItemDaoException;

	ArrayList<AnalystLineItemEntity> addMultipleObject(ArrayList<AnalystLineItemEntity> analystLineItem,
			String dataBaseName) throws AnalystLineItemDaoException;

	AnalystLineItemEntity updateAnalystLineItem(AnalystLineItemEntity analystLineItem, String analystLineId,
			String dataBaseName) throws AnalystLineItemDaoException;

	Set<String> getCurrentAnalystDetailsMapping(long startTimestamp, long endTimestamp,
			String dataBaseName);

	Set<String> getCurrentAddlingLineItem(long startTimestamp, long endTimestamp, String dataBaseName);

}
