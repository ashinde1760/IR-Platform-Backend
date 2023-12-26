package com.Anemoi.InvestorRelation.DataIngestion;

public class KeywordList {

	private long key_id;

	private String document_type;

	private String keyword;

	private int weight;

	public long getKey_id() {
		return key_id;
	}

	public void setKey_id(long key_id) {
		this.key_id = key_id;
	}

	public String getDocument_type() {
		return document_type;
	}

	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public KeywordList(long key_id, String document_type, String keyword, int weight) {
		super();
		this.key_id = key_id;
		this.document_type = document_type;
		this.keyword = keyword;
		this.weight = weight;
	}

	public KeywordList() {
		super();
	}

}
