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

	private String reportYear;

	public DataIngestionMappingModel() {
		super();
	}

	public DataIngestionMappingModel(String mapId, long fieldId, String analyst, String masterLineItem,
			String companyName, String tableName, String documentType, String peerName, String currency, String units,
			String year, String lineItemName, String quarter, String type, String value, String reportType,
			Date reportDate, long date, String denomination, String consolidated_standalone, String createdBy,
			String originalValue, String reportYear) {
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
		this.reportYear = reportYear;
	}

	@Override
	public String toString() {
		return "DataIngestionMappingModel [mapId=" + mapId + ", fieldId=" + fieldId + ", analyst=" + analyst
				+ ", masterLineItem=" + masterLineItem + ", companyName=" + companyName + ", tableName=" + tableName
				+ ", documentType=" + documentType + ", peerName=" + peerName + ", currency=" + currency + ", units="
				+ units + ", year=" + year + ", lineItemName=" + lineItemName + ", quarter=" + quarter + ", type="
				+ type + ", value=" + value + ", reportType=" + reportType + ", reportDate=" + reportDate + ", date="
				+ date + ", denomination=" + denomination + ", consolidated_standalone=" + consolidated_standalone
				+ ", createdBy=" + createdBy + ", originalValue=" + originalValue + ", reportYear=" + reportYear + "]";
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public long getFieldId() {
		return fieldId;
	}

	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
	}

	public String getAnalyst() {
		return analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getMasterLineItem() {
		return masterLineItem;
	}

	public void setMasterLineItem(String masterLineItem) {
		this.masterLineItem = masterLineItem;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getConsolidated_standalone() {
		return consolidated_standalone;
	}

	public void setConsolidated_standalone(String consolidated_standalone) {
		this.consolidated_standalone = consolidated_standalone;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getReportYear() {
		return reportYear;
	}

	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	
	

}