package com.Anemoi.InvestorRelation.AnalystLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface AnalystLineItemService {

	AnalystLineItemEntity createAnalystLineItem(AnalystLineItemEntity analystlineItem)
			throws AnalystLineItemServiceException;

	AnalystLineItemEntity getAnalystLineItemById(String aid) throws AnalystLineItemServiceException;

	List<AnalystLineItemEntity> getAllAnalystLineItemDetails() throws AnalystLineItemServiceException;

	ArrayList<AnalystLineItemEntity> getbyAnalystName(String analystName, String analystTableHeaderName)
			throws AnalystLineItemServiceException;

	String updateAnalystLineItem(AnalystDetails analystDetails) throws AnalystLineItemServiceException;

	ArrayList<AnalystLineItemEntity> uploadAnalstLineItemExcelSheet( String createdBy,CompletedFileUpload file) throws AnalystLineItemServiceException;

	ArrayList<AnalystLineItemEntity> addMultipleObject(ArrayList<AnalystLineItemEntity> analystLineItem)
			throws AnalystLineItemServiceException;

	AnalystLineItemEntity updateAnalystLineItem(AnalystLineItemEntity analystLineItem, String analystLineId)
			throws AnalystLineItemServiceException;

	Set<String> getCurrentDateMapingAnalystLineItem(long  startTimestamp,long endTimestamp);

	Set<String> getCurrentDateAddingAnalystLineItem(long startTimestamp, long endTimestamp);
}
