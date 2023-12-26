package com.Anemoi.InvestorRelation.ReportTableHeader;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ReportTableHeaderServiceImpl implements ReportTableHeaderService {

	@Inject
	ReportTableHeaderDao dao;

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	private static String DATABASENAME = "databasename";

	private static String dataBaseName() {
		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;
	}

	@Override
	public ReportTableHeaderEntity addReportTableHeaderDetails(ReportTableHeaderEntity entity)
			throws ReportTableHeaderServiceException {
		try {
			String dataBaseName = ReportTableHeaderServiceImpl.dataBaseName();
			ReportTableHeaderEntity reportEntity = this.dao.addReportTableHeader(entity, dataBaseName);
			return reportEntity;

		} catch (Exception e) {
			throw new ReportTableHeaderServiceException("unable to add " + e.getMessage());
		}

	}

	@Override
	public ArrayList<ReportTableHeaderEntity> gettableHeaderDetails() throws ReportTableHeaderServiceException {
		try {
			String dataBaseName = ReportTableHeaderServiceImpl.dataBaseName();
			ArrayList<ReportTableHeaderEntity> response = this.dao.getTableHeaderDetails(dataBaseName);
			return response;
		} catch (Exception e) {

			throw new ReportTableHeaderServiceException("unable to get list " + e.getMessage());
		}
	}

	@Override
	public ReportTableHeaderEntity getTableHeaderDetailsByid(String tableHeaderId)
			throws ReportTableHeaderServiceException {
		if (tableHeaderId.isEmpty() || tableHeaderId == null) {
			throw new ReportTableHeaderServiceException("tableHeaderId should not be null or empty");
		}
		try {
			String dataBaseName = ReportTableHeaderServiceImpl.dataBaseName();
			ReportTableHeaderEntity response = this.dao.getTableHeaderDetailsById(tableHeaderId, dataBaseName);
			return response;
		} catch (Exception e) {

			throw new ReportTableHeaderServiceException("unable to get table header details by id" + e.getMessage());

		}
	}

	@Override
	public ArrayList<ReportTableHeaderEntity> addMultipleObject(ArrayList<ReportTableHeaderEntity> entityObject)
			throws ReportTableHeaderServiceException {
		try {
			String dataBaseName = ReportTableHeaderServiceImpl.dataBaseName();
			ArrayList<ReportTableHeaderEntity> responselist = this.dao.addMultipleObject(entityObject, dataBaseName);
			return responselist;
		} catch (Exception e) {

			throw new ReportTableHeaderServiceException("unable to add table header details" + e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteTableHeaderDetails(String tableHeaderId) throws ReportTableHeaderServiceException {
		// TODO Auto-generated method stub
		if (tableHeaderId.isEmpty() || tableHeaderId == null) {
			throw new ReportTableHeaderServiceException("tableHeaderId should not be empty od null");
		}
		try {
			String dataBaseName = ReportTableHeaderServiceImpl.dataBaseName();
			this.dao.deleteTableHeaderDetails(tableHeaderId, dataBaseName);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(STATUS, SUCCESS);
			jsonObject.put(MSG, "Table header details delete successfully");
			return jsonObject.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new ReportTableHeaderServiceException("unable to delete" + e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateTableHeaderDetails(ReportTableHeaderEntity entity, String tableHeaderId)
			throws ReportTableHeaderServiceException {
		if (tableHeaderId.isEmpty() || tableHeaderId == null) {
			System.out.println("id should not be null or empty");
		}
		try {
			String dataBaseName = ReportTableHeaderServiceImpl.dataBaseName();
			this.dao.updateTableHeaderDetails(entity, tableHeaderId, dataBaseName);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(STATUS, SUCCESS);
			jsonObject.put(MSG, "table header details update successfully");
			return jsonObject.toString();
		} catch (Exception e) {

			throw new ReportTableHeaderServiceException("unable to update" + e.getMessage());

		}
	}

}
