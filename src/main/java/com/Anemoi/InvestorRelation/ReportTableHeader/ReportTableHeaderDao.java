package com.Anemoi.InvestorRelation.ReportTableHeader;

import java.util.ArrayList;

public interface ReportTableHeaderDao {

	ReportTableHeaderEntity addReportTableHeader(ReportTableHeaderEntity entity, String dataBaseName)
			throws ReportTableHeaderDaoException;

	ArrayList<ReportTableHeaderEntity> getTableHeaderDetails(String dataBaseName) throws ReportTableHeaderDaoException;

	ReportTableHeaderEntity getTableHeaderDetailsById(String tableHeaderId, String dataBaseName)
			throws ReportTableHeaderDaoException;

	ArrayList<ReportTableHeaderEntity> addMultipleObject(ArrayList<ReportTableHeaderEntity> entityObject,
			String dataBaseName) throws ReportTableHeaderDaoException;

	void deleteTableHeaderDetails(String tableHeaderId, String dataBaseName) throws ReportTableHeaderDaoException;

	void updateTableHeaderDetails(ReportTableHeaderEntity entity, String tableHeaderId, String dataBaseName)
			throws ReportTableHeaderDaoException;

}
