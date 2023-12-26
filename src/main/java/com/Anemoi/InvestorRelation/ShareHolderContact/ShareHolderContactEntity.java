package com.Anemoi.InvestorRelation.ShareHolderContact;

public class ShareHolderContactEntity {

	private String contactid;

	private String name;

	private String poc;

	private String email;

	private String address;

	private String contact;
	
	private String createdBy;
	
	private long createdOn;

	public String getContactid() {
		return contactid;
	}

	public void setContactid(String contactid) {
		this.contactid = contactid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	
	public ShareHolderContactEntity(String contactid, String name, String poc, String email, String address,
			String contact, String createdBy, long createdOn) {
		super();
		this.contactid = contactid;
		this.name = name;
		this.poc = poc;
		this.email = email;
		this.address = address;
		this.contact = contact;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public ShareHolderContactEntity() {
		super();
	}
	
	
}