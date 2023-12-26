package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONObject;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface ShareHolderMeetingService {

	ShareHolderMeetingEntity createShareHolderMeeting(ShareHolderMeetingEntity sharehodermeeting, CompletedFileUpload momfile)
			throws SQLException, ShareHolderMeetingServiceException;

	ShareHolderMeetingEntity getShareHolderMeetingById(String shareholderid)
			throws SQLException, ShareHolderMeetingServiceException;

	List<ShareHolderMeetingEntity> getShareHolderMeetingDetails()
			throws SQLException, ShareHolderMeetingServiceException;

	ShareHolderMeetingEntity updateShareHolderMeeting(ShareHolderMeetingEntity sharehodermeeting, String holderid)
			throws ShareHolderMeetingServiceException;

	String deleteShareHoderMeeting(String shareholderid) throws ShareHolderMeetingServiceException;

	JSONObject extractMomFiledetails(String meetingId, CompletedFileUpload momfile) throws ShareHolderMeetingServiceException;

	ShareHolderMeetingEntity getPreviewForMomFile(String shareholderid) throws ShareHolderMeetingServiceException;

}
