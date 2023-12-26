package com.Anemoi.InvestorRelation.DataIngestion;

public class WeightedWord {
	private int key_id;
	private String document_Type;
	private String keyword;
	private int weight;

	public int getKey_id() {
		return key_id;
	}

	public void setKey_id(int key_id) {
		this.key_id = key_id;
	}

	public String getDocument_Type() {
		return document_Type;
	}

	public void setDocument_Type(String document_Type) {
		this.document_Type = document_Type;
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

	public WeightedWord(int key_id, String document_Type, String keyword, int weight) {
		super();
		this.key_id = key_id;
		this.document_Type = document_Type;
		this.keyword = keyword;
		this.weight = weight;
	}

	public WeightedWord() {
		super();
	}

}
