package com.Anemoi.InvestorRelation.DataIngestion;

import java.sql.Date;

public class ExtractFilePojo {
	private long fileId;
	private Date reportDate;
	private String pageNo;

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public ExtractFilePojo(long fileId, Date reportDate, String pageNo) {
		super();
		this.fileId = fileId;
		this.reportDate = reportDate;
		this.pageNo = pageNo;
	}

	public ExtractFilePojo() {
		super();
	}

}