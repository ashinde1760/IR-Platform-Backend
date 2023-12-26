package com.Anemoi.InvestorRelation.MeetingScheduler;

import java.util.List;

public class MSTeamschedule {
	private long meetingSheduleId;
	private String meetingId;
	private String eventId;
	private String joinUrl;
	private String title;
	private String agenda;
	private List<String> participant;
	private String meetingDate;
	private String startTime;
	private String endTime;
	private String meetingType;
	private boolean recordAutomatically;
	private String status;
	private String meetingDataStatus;
	
	private String createdBy;
	private long createdOn;
	
	private String modifiedBy;
	private long modifiedOn;
	
	private String clientName;
	private List<String> fundGroup;
	private String remark;
	
	

	public MSTeamschedule() {
		super();
	}



	public MSTeamschedule(long meetingSheduleId, String meetingId, String eventId, String joinUrl, String title,
			String agenda, List<String> participant, String meetingDate, String startTime, String endTime,
			String meetingType, boolean recordAutomatically, String status, String meetingDataStatus, String createdBy,
			long createdOn, String modifiedBy, long modifiedOn, String clientName, List<String> fundGroup,
			String remark) {
		super();
		this.meetingSheduleId = meetingSheduleId;
		this.meetingId = meetingId;
		this.eventId = eventId;
		this.joinUrl = joinUrl;
		this.title = title;
		this.agenda = agenda;
		this.participant = participant;
		this.meetingDate = meetingDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.meetingType = meetingType;
		this.recordAutomatically = recordAutomatically;
		this.status = status;
		this.meetingDataStatus = meetingDataStatus;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
		this.clientName = clientName;
		this.fundGroup = fundGroup;
		this.remark = remark;
	}



	/**
	 * @return the meetingSheduleId
	 */
	public long getMeetingSheduleId() {
		return meetingSheduleId;
	}



	/**
	 * @param meetingSheduleId the meetingSheduleId to set
	 */
	public void setMeetingSheduleId(long meetingSheduleId) {
		this.meetingSheduleId = meetingSheduleId;
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
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}



	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}



	/**
	 * @return the joinUrl
	 */
	public String getJoinUrl() {
		return joinUrl;
	}



	/**
	 * @param joinUrl the joinUrl to set
	 */
	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}



	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}



	/**
	 * @return the agenda
	 */
	public String getAgenda() {
		return agenda;
	}



	/**
	 * @param agenda the agenda to set
	 */
	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}



	/**
	 * @return the participant
	 */
	public List<String> getParticipant() {
		return participant;
	}



	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(List<String> participant) {
		this.participant = participant;
	}



	/**
	 * @return the meetingDate
	 */
	public String getMeetingDate() {
		return meetingDate;
	}



	/**
	 * @param meetingDate the meetingDate to set
	 */
	public void setMeetingDate(String meetingDate) {
		this.meetingDate = meetingDate;
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
	 * @return the recordAutomatically
	 */
	public boolean isRecordAutomatically() {
		return recordAutomatically;
	}



	/**
	 * @param recordAutomatically the recordAutomatically to set
	 */
	public void setRecordAutomatically(boolean recordAutomatically) {
		this.recordAutomatically = recordAutomatically;
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
	 * @return the meetingDataStatus
	 */
	public String getMeetingDataStatus() {
		return meetingDataStatus;
	}



	/**
	 * @param meetingDataStatus the meetingDataStatus to set
	 */
	public void setMeetingDataStatus(String meetingDataStatus) {
		this.meetingDataStatus = meetingDataStatus;
	}



	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}



	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	/**
	 * @return the createdOn
	 */
	public long getCreatedOn() {
		return createdOn;
	}



	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}



	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}



	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}



	/**
	 * @return the modifiedOn
	 */
	public long getModifiedOn() {
		return modifiedOn;
	}



	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
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
	public List<String> getFundGroup() {
		return fundGroup;
	}



	/**
	 * @param fundGroup the fundGroup to set
	 */
	public void setFundGroup(List<String> fundGroup) {
		this.fundGroup = fundGroup;
	}



	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}



	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
	
}