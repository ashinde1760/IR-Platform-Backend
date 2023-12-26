package com.Anemoi.InvestorRelation.FinancialRatio;

import java.sql.SQLException;
import java.util.List;

public interface FinancialRatioService {

	FinancialRatioEntity CreateFinancialRatio(FinancialRatioEntity finacialEntity)
			throws FinancialRatioServiceException;

	FinancialRatioEntity getFinancialRatioById(String financialid) throws FinancialRatioServiceException;

	List<FinancialRatioEntity> getAllFinancialRatioDetails() throws FinancialRatioServiceException;

	FinancialRatioEntity deleteFinancialRatio(String financialid) throws FinancialRatioServiceException;

}
