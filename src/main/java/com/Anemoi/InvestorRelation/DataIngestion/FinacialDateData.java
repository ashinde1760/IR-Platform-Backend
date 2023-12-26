package com.Anemoi.InvestorRelation.DataIngestion;

public class FinacialDateData {
	private String year;
	private String quater;
	private String type;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuater() {
		return quater;
	}

	public void setQuater(String quater) {
		this.quater = quater;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FinacialDateData(String year, String quater, String type) {
		super();
		this.year = year;
		this.quater = quater;
		this.type = type;
	}

	public FinacialDateData() {
		super();
	}

	@Override
	public String toString() {
		return "FinacialDateData [year=" + year + ", quater=" + quater + ", type=" + type + "]";
	}

}
