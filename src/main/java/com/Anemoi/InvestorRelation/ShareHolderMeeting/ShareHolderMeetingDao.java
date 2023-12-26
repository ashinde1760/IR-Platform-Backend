package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONObject;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface ShareHolderMeetingDao {

	ShareHolderMeetingEntity createNewShareHolderMeeting(ShareHolderMeetingEntity shareholdermeeting,
			 CompletedFileUpload momfile, String dataBaseName) throws ShareHolderMeetingDaoException;

	ShareHolderMeetingEntity getShareHolderMeetingById(String shareholderid, String dataBaseName)
			throws ShareHolderMeetingDaoException;

	List<ShareHolderMeetingEntity> getAllShareHolderMeetingDetails(String dataBaseName)
			throws SQLException, ShareHolderMeetingDaoException;

	ShareHolderMeetingEntity updateShareHolderMeetingDetails(ShareHolderMeetingEntity shareholdermeeting,
			String holderid, String dataBaseName) throws ShareHolderMeetingDaoException;

	String deleteShareHolderMeeting(String holderid, String dataBaseName) throws SQLException;

	JSONObject extractmomfile(String meetingId, CompletedFileUpload momfile, String dataBaseName) throws ShareHolderMeetingDaoException, IOException;

	ShareHolderMeetingEntity getPreviewForMomfile(String shareholderid, String dataBaseName) throws ShareHolderMeetingDaoException;

}
