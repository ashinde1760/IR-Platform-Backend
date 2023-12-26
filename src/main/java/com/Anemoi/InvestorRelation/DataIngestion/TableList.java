package com.Anemoi.InvestorRelation.DataIngestion;

public class TableList {

//	private long fileId;
	private long tableId;
	private String tableName;
	private long score;
	private String tableMapName;

	public long getTableId() {
		return tableId;
	}

	public void setTableId(long tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getTableMapName() {
		return tableMapName;
	}

	public void setTableMapName(String tableMapName) {
		this.tableMapName = tableMapName;
	}

	public TableList(long tableId, String tableName, long score, String tableMapName) {
		super();
		this.tableId = tableId;
		this.tableName = tableName;
		this.score = score;
		this.tableMapName = tableMapName;
	}

	public TableList() {
		super();
	}

}