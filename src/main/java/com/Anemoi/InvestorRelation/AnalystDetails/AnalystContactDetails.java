package com.Anemoi.InvestorRelation.AnalystDetails;

public class AnalystContactDetails {

	private long analystcontactid; 
	private long analystId;
    private String pocName;
    private String pocEmailId;
	public long getAnalystcontactid() {
		return analystcontactid;
	}
	public void setAnalystcontactid(long analystcontactid) {
		this.analystcontactid = analystcontactid;
	}
	public long getAnalystId() {
		return analystId;
	}
	public void setAnalystId(long analystId) {
		this.analystId = analystId;
	}
	public String getPocName() {
		return pocName;
	}
	public void setPocName(String pocName) {
		this.pocName = pocName;
	}
	public String getPocEmailId() {
		return pocEmailId;
	}
	public void setPocEmailId(String pocEmailId) {
		this.pocEmailId = pocEmailId;
	}
	public AnalystContactDetails(long analystcontactid, long analystId, String pocName, String pocEmailId) {
		super();
		this.analystcontactid = analystcontactid;
		this.analystId = analystId;
		this.pocName = pocName;
		this.pocEmailId = pocEmailId;
	}
	public AnalystContactDetails() {
		super();
	}
    
    
}