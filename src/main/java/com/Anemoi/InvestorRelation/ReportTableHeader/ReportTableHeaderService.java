package com.Anemoi.InvestorRelation.ReportTableHeader;

import java.util.ArrayList;

public interface ReportTableHeaderService {

	ReportTableHeaderEntity addReportTableHeaderDetails(ReportTableHeaderEntity entity)
			throws ReportTableHeaderServiceException;

	ArrayList<ReportTableHeaderEntity> gettableHeaderDetails() throws ReportTableHeaderServiceException;

	ReportTableHeaderEntity getTableHeaderDetailsByid(String tableHeaderId) throws ReportTableHeaderServiceException;

	ArrayList<ReportTableHeaderEntity> addMultipleObject(ArrayList<ReportTableHeaderEntity> entityObject)
			throws ReportTableHeaderServiceException;

	String deleteTableHeaderDetails(String tableHeaderId) throws ReportTableHeaderServiceException;

	String updateTableHeaderDetails(ReportTableHeaderEntity entity, String tableHeaderId)
			throws ReportTableHeaderServiceException;

}
