package com.Anemoi.InvestorRelation.IncomeStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface IncomeStatementDao {

	IncomeStatementEntity createIncomeStatement(IncomeStatementEntity incomestatement, String dataBaseName)
			throws IncomeStatementDaoException;

	IncomeStatementEntity getIncomeStatementById(String incomeid, String dataBaseName)
			throws IncomeStatementDaoException;

	List<IncomeStatementEntity> getAllIncomeStatementDetails(String dataBaseName)
			throws SQLException, IncomeStatementDaoException;

	IncomeStatementEntity updateIncomeStatementDetails(IncomeStatementEntity incomestatement, String incomeid,
			String dataBaseName) throws IncomeStatementDaoException;

	String deleteIncomeStatement(String incomeid, String dataBaseName) throws IncomeStatementDaoException;

	ArrayList<IncomeStatementEntity> addIncomeStatementObject(ArrayList<IncomeStatementEntity> incomeentity,
			String dataBaseName) throws IncomeStatementDaoException;

	ArrayList<IncomeStatementEntity> uploadExcelSheetIncomeTable(String createdBy,CompletedFileUpload file, String dataBaseName) throws IncomeStatementDaoException;

	ArrayList<IncomeStatementEntity> getIncomeLineItems(long startTimestamp, long endTimestamp, String dataBaseName);

}
