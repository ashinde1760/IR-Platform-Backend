package com.Anemoi.InvestorRelation.DataIngestion;

import java.util.List;

public class NonProcessFilesDetails {

	private int npFileId;

	private String client;
	
	private String clientId;

	private String fileName;

	private String fileType;

	private byte[] FileData;
	
    private String hashCode;
    
    private String createdBy;
    
    private long createdOn;

   	private List<String> suggestedPeers;

	public NonProcessFilesDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NonProcessFilesDetails(int npFileId, String client, String clientId, String fileName, String fileType,
			byte[] fileData, String hashCode, String createdBy, long createdOn, List<String> suggestedPeers) {
		super();
		this.npFileId = npFileId;
		this.client = client;
		this.clientId = clientId;
		this.fileName = fileName;
		this.fileType = fileType;
		FileData = fileData;
		this.hashCode = hashCode;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.suggestedPeers = suggestedPeers;
	}

	/**
	 * @return the npFileId
	 */
	public int getNpFileId() {
		return npFileId;
	}

	/**
	 * @param npFileId the npFileId to set
	 */
	public void setNpFileId(int npFileId) {
		this.npFileId = npFileId;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the fileData
	 */
	public byte[] getFileData() {
		return FileData;
	}

	/**
	 * @param fileData the fileData to set
	 */
	public void setFileData(byte[] fileData) {
		FileData = fileData;
	}

	/**
	 * @return the hashCode
	 */
	public String getHashCode() {
		return hashCode;
	}

	/**
	 * @param hashCode the hashCode to set
	 */
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
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
	 * @return the suggestedPeers
	 */
	public List<String> getSuggestedPeers() {
		return suggestedPeers;
	}

	/**
	 * @param suggestedPeers the suggestedPeers to set
	 */
	public void setSuggestedPeers(List<String> suggestedPeers) {
		this.suggestedPeers = suggestedPeers;
	}

    

	}