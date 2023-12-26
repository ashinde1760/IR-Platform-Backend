package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.sql.Date;

public class ShareHolderMeetingEntity {

	private String shareholderid;
	
	private String meetingId;

	private String date;

	private String startTime;

	private String endTime;

	private String organisation;

	private String stakeholderType;

	private String meetingType;

	private String subject;

	private String broker;

	private String location;

	private String status;

	private String comments;

	private String participants;

	private String feedback;
	
	private String summary;
	
	private String actionItem;
	
	private String investorConcerns;
	
	private String analysis;

	private Long uploadedDate;
	
	private String uploadedBy;
 
	private String mediakey;
	
	private String momfileName;
	
	private String momFileType;
	
	private byte[] momFileData;
	
	private String audioVedioFileStatus;
	
	private String momStatus;

	private String clientName;
	
	private String fundGroup;

	private String dateFormatted;
	


	public ShareHolderMeetingEntity() {
		super();
	}



	public ShareHolderMeetingEntity(String shareholderid, String meetingId, String date, String startTime,
			String endTime, String organisation, String stakeholderType, String meetingType, String subject,
			String broker, String location, String status, String comments, String participants, String feedback,
			String summary, String actionItem, String investorConcerns, String analysis, Long uploadedDate,
			String uploadedBy, String mediakey, String momfileName, String momFileType, byte[] momFileData,
			String audioVedioFileStatus, String momStatus, String clientName, String fundGroup, String dateFormatted) {
		super();
		this.shareholderid = shareholderid;
		this.meetingId = meetingId;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.organisation = organisation;
		this.stakeholderType = stakeholderType;
		this.meetingType = meetingType;
		this.subject = subject;
		this.broker = broker;
		this.location = location;
		this.status = status;
		this.comments = comments;
		this.participants = participants;
		this.feedback = feedback;
		this.summary = summary;
		this.actionItem = actionItem;
		this.investorConcerns = investorConcerns;
		this.analysis = analysis;
		this.uploadedDate = uploadedDate;
		this.uploadedBy = uploadedBy;
		this.mediakey = mediakey;
		this.momfileName = momfileName;
		this.momFileType = momFileType;
		this.momFileData = momFileData;
		this.audioVedioFileStatus = audioVedioFileStatus;
		this.momStatus = momStatus;
		this.clientName = clientName;
		this.fundGroup = fundGroup;
		this.dateFormatted = dateFormatted;
	}



	/**
	 * @return the shareholderid
	 */
	public String getShareholderid() {
		return shareholderid;
	}



	/**
	 * @param shareholderid the shareholderid to set
	 */
	public void setShareholderid(String shareholderid) {
		this.shareholderid = shareholderid;
	}



	/**
	 * @return the meetingId
	 */
	public String getMeetingId() {
		return meetingId;
	}



	/**
	 * @param meetingId the meetingId to set
	 */
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}



	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}



	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}



	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}



	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}



	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	/**
	 * @return the organisation
	 */
	public String getOrganisation() {
		return organisation;
	}



	/**
	 * @param organisation the organisation to set
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}



	/**
	 * @return the stakeholderType
	 */
	public String getStakeholderType() {
		return stakeholderType;
	}



	/**
	 * @param stakeholderType the stakeholderType to set
	 */
	public void setStakeholderType(String stakeholderType) {
		this.stakeholderType = stakeholderType;
	}



	/**
	 * @return the meetingType
	 */
	public String getMeetingType() {
		return meetingType;
	}



	/**
	 * @param meetingType the meetingType to set
	 */
	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}



	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}



	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}



	/**
	 * @return the broker
	 */
	public String getBroker() {
		return broker;
	}



	/**
	 * @param broker the broker to set
	 */
	public void setBroker(String broker) {
		this.broker = broker;
	}



	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}



	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}



	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}



	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}



	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}



	/**
	 * @return the participants
	 */
	public String getParticipants() {
		return participants;
	}



	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(String participants) {
		this.participants = participants;
	}



	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}



	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}



	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}



	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}



	/**
	 * @return the actionItem
	 */
	public String getActionItem() {
		return actionItem;
	}



	/**
	 * @param actionItem the actionItem to set
	 */
	public void setActionItem(String actionItem) {
		this.actionItem = actionItem;
	}



	/**
	 * @return the investorConcerns
	 */
	public String getInvestorConcerns() {
		return investorConcerns;
	}



	/**
	 * @param investorConcerns the investorConcerns to set
	 */
	public void setInvestorConcerns(String investorConcerns) {
		this.investorConcerns = investorConcerns;
	}



	/**
	 * @return the analysis
	 */
	public String getAnalysis() {
		return analysis;
	}



	/**
	 * @param analysis the analysis to set
	 */
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}



	/**
	 * @return the uploadedDate
	 */
	public Long getUploadedDate() {
		return uploadedDate;
	}



	/**
	 * @param uploadedDate the uploadedDate to set
	 */
	public void setUploadedDate(Long uploadedDate) {
		this.uploadedDate = uploadedDate;
	}



	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}



	/**
	 * @param uploadedBy the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}



	/**
	 * @return the mediakey
	 */
	public String getMediakey() {
		return mediakey;
	}



	/**
	 * @param mediakey the mediakey to set
	 */
	public void setMediakey(String mediakey) {
		this.mediakey = mediakey;
	}



	/**
	 * @return the momfileName
	 */
	public String getMomfileName() {
		return momfileName;
	}



	/**
	 * @param momfileName the momfileName to set
	 */
	public void setMomfileName(String momfileName) {
		this.momfileName = momfileName;
	}



	/**
	 * @return the momFileType
	 */
	public String getMomFileType() {
		return momFileType;
	}



	/**
	 * @param momFileType the momFileType to set
	 */
	public void setMomFileType(String momFileType) {
		this.momFileType = momFileType;
	}



	/**
	 * @return the momFileData
	 */
	public byte[] getMomFileData() {
		return momFileData;
	}



	/**
	 * @param momFileData the momFileData to set
	 */
	public void setMomFileData(byte[] momFileData) {
		this.momFileData = momFileData;
	}



	/**
	 * @return the audioVedioFileStatus
	 */
	public String getAudioVedioFileStatus() {
		return audioVedioFileStatus;
	}



	/**
	 * @param audioVedioFileStatus the audioVedioFileStatus to set
	 */
	public void setAudioVedioFileStatus(String audioVedioFileStatus) {
		this.audioVedioFileStatus = audioVedioFileStatus;
	}



	/**
	 * @return the momStatus
	 */
	public String getMomStatus() {
		return momStatus;
	}



	/**
	 * @param momStatus the momStatus to set
	 */
	public void setMomStatus(String momStatus) {
		this.momStatus = momStatus;
	}



	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}



	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}



	/**
	 * @return the fundGroup
	 */
	public String getFundGroup() {
		return fundGroup;
	}



	/**
	 * @param fundGroup the fundGroup to set
	 */
	public void setFundGroup(String fundGroup) {
		this.fundGroup = fundGroup;
	}



	/**
	 * @return the dateFormatted
	 */
	public String getDateFormatted() {
		return dateFormatted;
	}



	/**
	 * @param dateFormatted the dateFormatted to set
	 */
	public void setDateFormatted(String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}
	
	
	
	}