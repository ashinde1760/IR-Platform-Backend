package com.Anemoi.InvestorRelation.DataIngestion;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class DataIngestionModel {

	private long fileId;

	private int npFileId;

	private String client;

	private String documentType;

	private String analystName;

	private String reportType;

	private Date reportDate;

	private String peerName;

	private String currency;

	private String units;

	private String fileName;

	private String fileType;

	private byte[] fileData;

	private String denomination;

	private String pagenumbers;

	private String status;
	
	private String consolidated_standalone;
	
	private String createdBy;
	
	private long createdOn;
	

	public DataIngestionModel() {
		super();
		// TODO Auto-generated constructor stub
	}


	public DataIngestionModel(long fileId, int npFileId, String client, String documentType, String analystName,
			String reportType, Date reportDate, String peerName, String currency, String units, String fileName,
			String fileType, byte[] fileData, String denomination, String pagenumbers, String status,
			String consolidated_standalone, String createdBy, long createdOn) {
		super();
		this.fileId = fileId;
		this.npFileId = npFileId;
		this.client = client;
		this.documentType = documentType;
		this.analystName = analystName;
		this.reportType = reportType;
		this.reportDate = reportDate;
		this.peerName = peerName;
		this.currency = currency;
		this.units = units;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileData = fileData;
		this.denomination = denomination;
		this.pagenumbers = pagenumbers;
		this.status = status;
		this.consolidated_standalone = consolidated_standalone;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}


	/**
	 * @return the fileId
	 */
	public long getFileId() {
		return fileId;
	}


	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(long fileId) {
		this.fileId = fileId;
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
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}


	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}


	/**
	 * @return the analystName
	 */
	public String getAnalystName() {
		return analystName;
	}


	/**
	 * @param analystName the analystName to set
	 */
	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}


	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}


	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}


	/**
	 * @return the reportDate
	 */
	public Date getReportDate() {
		return reportDate;
	}


	/**
	 * @param reportDate the reportDate to set
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}


	/**
	 * @return the peerName
	 */
	public String getPeerName() {
		return peerName;
	}


	/**
	 * @param peerName the peerName to set
	 */
	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}


	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}


	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}


	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}


	/**
	 * @param units the units to set
	 */
	public void setUnits(String units) {
		this.units = units;
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
		return fileData;
	}


	/**
	 * @param fileData the fileData to set
	 */
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}


	/**
	 * @return the denomination
	 */
	public String getDenomination() {
		return denomination;
	}


	/**
	 * @param denomination the denomination to set
	 */
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}


	/**
	 * @return the pagenumbers
	 */
	public String getPagenumbers() {
		return pagenumbers;
	}


	/**
	 * @param pagenumbers the pagenumbers to set
	 */
	public void setPagenumbers(String pagenumbers) {
		this.pagenumbers = pagenumbers;
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
	 * @return the consolidated_standalone
	 */
	public String getConsolidated_standalone() {
		return consolidated_standalone;
	}


	/**
	 * @param consolidated_standalone the consolidated_standalone to set
	 */
	public void setConsolidated_standalone(String consolidated_standalone) {
		this.consolidated_standalone = consolidated_standalone;
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

	
	}