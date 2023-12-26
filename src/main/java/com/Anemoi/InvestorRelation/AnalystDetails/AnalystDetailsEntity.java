package com.Anemoi.InvestorRelation.AnalystDetails;

import java.util.ArrayList;
import java.util.List;

public class AnalystDetailsEntity {

	private long analystId;

	private String analystName;

   private List<AnalystContactDetails> analystContactDetails;
   

	private long createdOn;

	private long modifiedOn;
	
	  private String createdBy;
	  
	  private String modifiedBy;

	public long getAnalystId() {
		return analystId;
	}

	public void setAnalystId(long analystId) {
		this.analystId = analystId;
	}

	public String getAnalystName() {
		return analystName;
	}

	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}

	public List<AnalystContactDetails> getAnalystContactDetails() {
		return analystContactDetails;
	}

	public void setAnalystContactDetails(List<AnalystContactDetails> analystContactDetails) {
		this.analystContactDetails = analystContactDetails;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public AnalystDetailsEntity(long analystId, String analystName, List<AnalystContactDetails> analystContactDetails,
			String createdBy, long createdOn, String modifiedBy, long modifiedOn) {
		super();
		this.analystId = analystId;
		this.analystName = analystName;
		this.analystContactDetails = analystContactDetails;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
	}

	public AnalystDetailsEntity() {
		super();
	}
	
	

}