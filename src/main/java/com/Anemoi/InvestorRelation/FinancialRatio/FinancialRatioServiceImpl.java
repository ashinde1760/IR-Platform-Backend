package com.Anemoi.InvestorRelation.FinancialRatio;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class FinancialRatioServiceImpl implements FinancialRatioService {

	@Inject
	private FinancialRatioDao financialratiodao;

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	private static String DATABASENAME = "databaseName";

	private static String dataBaseName() {

		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;

	}

	@Override
	public FinancialRatioEntity CreateFinancialRatio(FinancialRatioEntity finacialEntity)
			throws FinancialRatioServiceException {
		try {
			String dataBaseName = FinancialRatioServiceImpl.dataBaseName();

			// applyValidation(finacialEntity);

			FinancialRatioEntity createNewFinancialRatio = this.financialratiodao
					.createNewFinancialRatio(finacialEntity, dataBaseName);
			return createNewFinancialRatio;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinancialRatioServiceException(e.getMessage());
		}

	}

	private void applyValidation(FinancialRatioEntity finacialEntity) throws Exception {

		if (finacialEntity.getClientName() == null || finacialEntity.getClientName().isEmpty()) {
			throw new Exception("client name should not be null or empty");
		}
		if (finacialEntity.getFormulaName() == null || finacialEntity.getFormulaName().isEmpty()) {
			throw new Exception("formula name should not be null or empty");
		}

		if (finacialEntity.getFormula() == null || finacialEntity.getFormula().isEmpty()) {
			throw new Exception("formula  should not be null or empty");
		}


	}

	@Override
	public FinancialRatioEntity getFinancialRatioById(String financialid) throws FinancialRatioServiceException {
		try {
			String dataBaseName = FinancialRatioServiceImpl.dataBaseName();
			if (financialid == null || financialid.isEmpty()) {

				throw new FinancialRatioServiceException("FinancialRatio id must not be null or empty");
			}
			FinancialRatioEntity financialratioEntity = this.financialratiodao.getFianancialRatioById(financialid,
					dataBaseName);
			return financialratioEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new FinancialRatioServiceException( e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialRatioEntity> getAllFinancialRatioDetails() throws FinancialRatioServiceException {
		try {
			String dataBaseName = FinancialRatioServiceImpl.dataBaseName();
			List<FinancialRatioEntity> financialratioEntity = this.financialratiodao
					.getAllFinancialRatioDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray financialratioJsonData = getJSONFromFinancialRatioList(financialratioEntity);
			object.put(financialratioEntity, financialratioJsonData);
			return financialratioEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new FinancialRatioServiceException("unable to get finacial ratio entity list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromFinancialRatioList(List<FinancialRatioEntity> financialratioEntity) {
		JSONArray array = new JSONArray();
		for (FinancialRatioEntity balance : financialratioEntity) {
			JSONObject object = buildJsonFromfinancialratio(balance);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromfinancialratio(FinancialRatioEntity financialratioEntity) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(FinancialRatioQuaryConstant.FINANCIALID, financialratioEntity.getFinancialid());
		jsonObject.put(FinancialRatioQuaryConstant.FORMULANAME, financialratioEntity.getFormulaName());
		jsonObject.put(FinancialRatioQuaryConstant.FORMULA, financialratioEntity.getFormula());
		jsonObject.put(FinancialRatioQuaryConstant.CREATEDBY, financialratioEntity.getCreatedBy());
		return jsonObject;
	}

	
	

	@SuppressWarnings({ "uncheked", "unchecked" })
	@Override
	public FinancialRatioEntity deleteFinancialRatio(String financialid) throws FinancialRatioServiceException {
		try {
			String dataBaseName = FinancialRatioServiceImpl.dataBaseName();
			if (financialid == null || financialid.isEmpty()) {
				System.out.println("Financial Ratio cannot be null or empty");

			}
			FinancialRatioEntity financialratio = financialratiodao.getFianancialRatioById(financialid, dataBaseName);
			if (financialratio == null) {

			}
			this.financialratiodao.deleteFinancialRatio(financialid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "Financial Ratio deleted suucessfully");
			return financialratio;

		} catch (Exception e) {
			e.printStackTrace();
			throw new FinancialRatioServiceException("unable to delete finacial ratio by id" + e.getMessage());
		}

	}

}
