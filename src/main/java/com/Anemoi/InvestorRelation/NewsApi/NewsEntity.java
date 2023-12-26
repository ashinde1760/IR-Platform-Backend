package com.Anemoi.InvestorRelation.NewsApi;

public class NewsEntity {
	
	private String qnTilte;
	private String fromDate;
	public NewsEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NewsEntity(String qnTilte, String fromDate) {
		super();
		this.qnTilte = qnTilte;
		this.fromDate = fromDate;
	}
	public String getQnTilte() {
		return qnTilte;
	}
	public void setQnTilte(String qnTilte) {
		this.qnTilte = qnTilte;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	

}
