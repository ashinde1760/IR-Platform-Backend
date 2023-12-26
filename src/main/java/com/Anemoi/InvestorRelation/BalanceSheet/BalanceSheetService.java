package com.Anemoi.InvestorRelation.BalanceSheet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface BalanceSheetService {

	BalanceSheetEntity createNewBalanceSheetForm(BalanceSheetEntity balance) throws BalanceSheetServiceException;

	BalanceSheetEntity getBalancesheetById(String balanceid) throws BalanceSheetServiceException;

	List<BalanceSheetEntity> getAllBalanceSheetDetails() throws BalanceSheetServiceException;

	BalanceSheetEntity updateBalanceSheetForm(BalanceSheetEntity balance, String balanceid)
			throws BalanceSheetServiceException;

	BalanceSheetEntity deleteBalanceSheet(String balanceid) throws BalanceSheetServiceException;

	ArrayList<BalanceSheetEntity> addObjectBalanceSheetEntity(ArrayList<BalanceSheetEntity> balanceSheetEntity)
			throws BalanceSheetServiceException;

	ArrayList<BalanceSheetEntity> uploadBalanceSheetExcel(String createdBy,CompletedFileUpload file) throws BalanceSheetServiceException;

	ArrayList<BalanceSheetEntity> getBalanceSheetLineItem(long  startTimestamp,long endTimestamp);
	
}
