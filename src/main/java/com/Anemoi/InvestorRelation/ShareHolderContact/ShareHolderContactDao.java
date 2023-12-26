package com.Anemoi.InvestorRelation.ShareHolderContact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface ShareHolderContactDao {

	ShareHolderContactEntity createNewShareHolderContact(ShareHolderContactEntity shareholdercontact,
			String dataBaseName) throws ShareHolderContactDaoException;

	ShareHolderContactEntity getShareHolderContactById(String contactid, String dataBaseName)
			throws ShareHolderContactDaoException;

	List<ShareHolderContactEntity> getAllShareHolderContactDetails(String dataBaseName)
			throws SQLException, ShareHolderContactDaoException;

	ShareHolderContactEntity updateShareHolderContactDetails(ShareHolderContactEntity shareholdercontact,
			String contactid, String dataBaseName) throws ShareHolderContactDaoException;

	String deleteShareHolderContact(String contactid, String dataBaseName) throws SQLException;

	String uploadShareholderContactExcelsheet(String createdBy, CompletedFileUpload file, String dataBaseName) throws ShareHolderContactDaoException;
	ArrayList<ShareHolderContactEntity> getCurrentDateDatabydate(long startTimestamp, long endTimestamp,
			String dataBaseName);

}
