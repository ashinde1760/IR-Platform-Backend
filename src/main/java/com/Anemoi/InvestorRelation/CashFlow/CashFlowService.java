package com.Anemoi.InvestorRelation.CashFlow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface CashFlowService {

	CashFlowEntity createCashFlow(CashFlowEntity cashflow) throws SQLException, CashFlowServiceException;

	CashFlowEntity getCashFlowById(String cashid) throws SQLException, CashFlowServiceException;

	List<CashFlowEntity> getAllCashFlowDetails() throws SQLException, CashFlowServiceException;

	CashFlowEntity updateCashFlow(CashFlowEntity cashflow, String cashid) throws CashFlowServiceException;

	CashFlowEntity deleteCashFlow(String cashid) throws CashFlowServiceException;

	ArrayList<CashFlowEntity> addCashFlowObject(ArrayList<CashFlowEntity> cashFlowEntity)
			throws CashFlowServiceException;

	ArrayList<CashFlowEntity> uploadCashFlowExcelSheet(String createdBy,CompletedFileUpload file) throws CashFlowServiceException;

	ArrayList<CashFlowEntity> getlineItemForToday(long  startTimestamp,long endTimestamp);
	
}
