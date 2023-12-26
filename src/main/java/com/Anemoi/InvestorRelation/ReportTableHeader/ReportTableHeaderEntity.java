package com.Anemoi.InvestorRelation.ReportTableHeader;

public class ReportTableHeaderEntity {

	private String tableHeaderId;
	private String tableHeaderName;
	private String description;
	private long createdOn;
	private long modifiedOn;

	public ReportTableHeaderEntity(String tableHeaderId, String tableHeaderName, String description, long createdOn,
			long modifiedOn) {
		super();
		this.tableHeaderId = tableHeaderId;
		this.tableHeaderName = tableHeaderName;
		this.description = description;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
	}

	public ReportTableHeaderEntity() {
		super();
	}

	public String getTableHeaderId() {
		return tableHeaderId;
	}

	public void setTableHeaderId(String tableHeaderId) {
		this.tableHeaderId = tableHeaderId;
	}

	public String getTableHeaderName() {
		return tableHeaderName;
	}

	public void setTableHeaderName(String tableHeaderName) {
		this.tableHeaderName = tableHeaderName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}