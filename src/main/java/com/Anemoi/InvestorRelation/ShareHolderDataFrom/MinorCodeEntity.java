package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

public class MinorCodeEntity {
	
	private long minorcodeId;
	
	private  String minorCode;
	
	private String fullform;

	public long getMinorcodeId() {
		return minorcodeId;
	}

	public void setMinorcodeId(long minorcodeId) {
		this.minorcodeId = minorcodeId;
	}

	public String getMinorCode() {
		return minorCode;
	}

	public void setMinorCode(String minorCode) {
		this.minorCode = minorCode;
	}

	public String getFullform() {
		return fullform;
	}

	public void setFullform(String fullform) {
		this.fullform = fullform;
	}

	public MinorCodeEntity(long minorcodeId, String minorCode, String fullform) {
		super();
		this.minorcodeId = minorcodeId;
		this.minorCode = minorCode;
		this.fullform = fullform;
	}

	public MinorCodeEntity() {
		super();
	}
	
	

}
