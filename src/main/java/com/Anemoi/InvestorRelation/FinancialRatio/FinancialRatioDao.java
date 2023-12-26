package com.Anemoi.InvestorRelation.FinancialRatio;

import java.sql.SQLException;
import java.util.List;

public interface FinancialRatioDao {

	FinancialRatioEntity createNewFinancialRatio(FinancialRatioEntity financialratioEntity, String dataBaseName)
			throws FinancialRatioDaoException;

	FinancialRatioEntity getFianancialRatioById(String financialid, String dataBaseName)
			throws FinancialRatioDaoException;

	List<FinancialRatioEntity> getAllFinancialRatioDetails(String dataBaseName) throws FinancialRatioDaoException;


	String deleteFinancialRatio(String financialid, String dataBaseName) throws FinancialRatioDaoException;

}
