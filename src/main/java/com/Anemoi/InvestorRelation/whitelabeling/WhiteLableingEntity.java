package com.Anemoi.InvestorRelation.whitelabeling;

public class WhiteLableingEntity {
	
	private String whitelableId;
	
	private String clientName;
  private String filePath;
  private String fileName;
  private String fileType;
  private byte[] fileData;
	private String cssFileName;
	private String cssFileType;
	private byte[] cssFileData;
	private String createdBy;
  private long createdOn;
public String getWhitelableId() {
	return whitelableId;
}
public void setWhitelableId(String whitelableId) {
	this.whitelableId = whitelableId;
}
public String getClientName() {
	return clientName;
}
public void setClientName(String clientName) {
	this.clientName = clientName;
}
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public String getFileType() {
	return fileType;
}
public void setFileType(String fileType) {
	this.fileType = fileType;
}
public byte[] getFileData() {
	return fileData;
}
public void setFileData(byte[] fileData) {
	this.fileData = fileData;
}
public String getCssFileName() {
	return cssFileName;
}
public void setCssFileName(String cssFileName) {
	this.cssFileName = cssFileName;
}
public String getCssFileType() {
	return cssFileType;
}
public void setCssFileType(String cssFileType) {
	this.cssFileType = cssFileType;
}
public byte[] getCssFileData() {
	return cssFileData;
}
public void setCssFileData(byte[] cssFileData) {
	this.cssFileData = cssFileData;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public long getCreatedOn() {
	return createdOn;
}
public void setCreatedOn(long createdOn) {
	this.createdOn = createdOn;
}
public WhiteLableingEntity(String whitelableId, String clientName, String filePath, String fileName, String fileType,
		byte[] fileData, String cssFileName, String cssFileType, byte[] cssFileData, String createdBy, long createdOn) {
	super();
	this.whitelableId = whitelableId;
	this.clientName = clientName;
	this.filePath = filePath;
	this.fileName = fileName;
	this.fileType = fileType;
	this.fileData = fileData;
	this.cssFileName = cssFileName;
	this.cssFileType = cssFileType;
	this.cssFileData = cssFileData;
	this.createdBy = createdBy;
	this.createdOn = createdOn;
}
public WhiteLableingEntity() {
	super();
}
  
  
}
