package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface ShareHoderDataFromDao {

	ShareHolderDataFromEntity createNewShareHolderDataForm(ShareHolderDataFromEntity shareholderdataform,
			String dataBaseName) throws ShareHolderDataFormDaoException;

	ShareHolderDataFromEntity getShareHolderDataFormById(String shareid, String dataBaseName)
			throws ShareHolderDataFormDaoException;

	List<ShareHolderDataFromEntity> getAllShareHolderDataFormDetails(String dataBaseName)
			throws SQLException, ShareHolderDataFormDaoException;

	ShareHolderDataFromEntity updateShareHolderDAtaFormDetails(ShareHolderDataFromEntity shareholderdataform,
			String shareid, String dataBaseName);

	String deleteShareHolderDataForm(String shareid, String dataBaseName) throws SQLException;

	String uploadShareHolderDataExcelSheet(String createdBy,CompletedFileUpload file, String dataBaseName) throws ShareHolderDataFormDaoException;

	String addMinorCode(MinorCodeEntity codeEntity, String dataBaseName) throws ShareHolderDataFormDaoException;

	ArrayList<MinorCodeEntity> getMinorCodeList(String dataBaseName) throws ShareHolderDataFormDaoException;

	
	public ArrayList<String> getMinorCodeNames(String dataBaseName);
	ArrayList<ShareHolderDataFromEntity> getCurretDateData(long startTimestamp, long endTimestamp, String dataBaseName);

}
