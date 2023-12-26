package com.Anemoi.InvestorRelation.UserModel;

public class UserEntity {

	private String userid;
	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;
	private String assignedName;
	private String roleName;
	private String userStatus;
	private String password;
	private Long editedOn;
	private Long createdOn;
	private String createdBy;
	private String modifiedBy;

	public UserEntity() {
		super();
	}

	public UserEntity(String userid, String firstName, String lastName, String email, String mobileNumber,
			String assignedName, String roleName, String userStatus, String password, Long editedOn, Long createdOn,
			String createdBy, String modifiedBy) {
		super();
		this.userid = userid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.assignedName = assignedName;
		this.roleName = roleName;
		this.userStatus = userStatus;
		this.password = password;
		this.editedOn = editedOn;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the assignedName
	 */
	public String getAssignedName() {
		return assignedName;
	}

	/**
	 * @param assignedName the assignedName to set
	 */
	public void setAssignedName(String assignedName) {
		this.assignedName = assignedName;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the userStatus
	 */
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * @param userStatus the userStatus to set
	 */
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the editedOn
	 */
	public Long getEditedOn() {
		return editedOn;
	}

	/**
	 * @param editedOn the editedOn to set
	 */
	public void setEditedOn(Long editedOn) {
		this.editedOn = editedOn;
	}

	/**
	 * @return the createdOn
	 */
	public Long getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


}