package com.Anemoi.InvestorRelation.DataIngestion;

import java.sql.Date;

public class DataIngestionMappingModel {

	private String mapId;

	private long fieldId;

	private String analyst;

	private String masterLineItem;

	private String companyName;

	private String tableName;

	private String documentType;

	private String peerName;

	private String currency;

	private String units;

	private String year;

	private String lineItemName;

	private String quarter;

	private String type;

	private String value;

	private String reportType;

	private Date reportDate;

	private long date;

	private String denomination;
	
	private String consolidated_standalone;
	
	private String createdBy;
	
	private String originalValue;

	public DataIngestionMappingModel() {
		super();
	}

	public DataIngestionMappingModel(String mapId, long fieldId, String analyst, String masterLineItem,
			String companyName, String tableName, String documentType, String peerName, String currency, String units,
			String year, String lineItemName, String quarter, String type, String value, String reportType,
			Date reportDate, long date, String denomination, String consolidated_standalone, String createdBy,
			String originalValue) {
		super();
		this.mapId = mapId;
		this.fieldId = fieldId;
		this.analyst = analyst;
		this.masterLineItem = masterLineItem;
		this.companyName = companyName;
		this.tableName = tableName;
		this.documentType = documentType;
		this.peerName = peerName;
		this.currency = currency;
		this.units = units;
		this.year = year;
		this.lineItemName = lineItemName;
		this.quarter = quarter;
		this.type = type;
		this.value = value;
		this.reportType = reportType;
		this.reportDate = reportDate;
		this.date = date;
		this.denomination = denomination;
		this.consolidated_standalone = consolidated_standalone;
		this.createdBy = createdBy;
		this.originalValue = originalValue;
	}

	/**
	 * @return the mapId
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	/**
	 * @return the fieldId
	 */
	public long getFieldId() {
		return fieldId;
	}

	/**
	 * @param fieldId the fieldId to set
	 */
	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * @return the analyst
	 */
	public String getAnalyst() {
		return analyst;
	}

	/**
	 * @param analyst the analyst to set
	 */
	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	/**
	 * @return the masterLineItem
	 */
	public String getMasterLineItem() {
		return masterLineItem;
	}

	/**
	 * @param masterLineItem the masterLineItem to set
	 */
	public void setMasterLineItem(String masterLineItem) {
		this.masterLineItem = masterLineItem;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the lineItemName
	 */
	public String getLineItemName() {
		return lineItemName;
	}

	/**
	 * @param lineItemName the lineItemName to set
	 */
	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	/**
	 * @return the quarter
	 */
	public String getQuarter() {
		return quarter;
	}

	/**
	 * @param quarter the quarter to set
	 */
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
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
	 * @return the originalValue
	 */
	public String getOriginalValue() {
		return originalValue;
	}

	/**
	 * @param originalValue the originalValue to set
	 */
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	
	

}