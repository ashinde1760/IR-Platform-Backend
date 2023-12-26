package com.Anemoi.InvestorRelation.ShareHolderContact;

public class ShareHolderContactQuaryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String CONTACTID = "contactid";

	public static final String NAME = "name";

	public static final String POC = "poc";

	public static final String EMAIL = "email";


	public static final String ADDRESS = "address";

	public static final String CONTACT = "contact";

	public static final String INSERT_INTO_SHAREHOLDERCONTACT = "INSERT INTO INV_RELATIONS.dev.shareholdercontact values(?,?,?,?,?,?,?,?)";

	public static final String SELECT__SHAREHOLDERCONTACT_BY_ID = "SELECT * FROM INV_RELATIONS.dev.shareholdercontact where contactid=?";

	public static final String SELECT_SHAREHOLDERCONTACT = "SELECT *FROM INV_RELATIONS.dev.shareholdercontact";

	public static final String UPDATE_SHAREHOLDERCONTACT = "UPDATE INV_RELATIONS.dev.shareholdercontact SET name=?, poc=?, email=?,address=?,contact=? WHERE contactid=?";

	public static final String DELETE_SHAREHOLDERCONTACT_BY_ID = "DELETE INV_RELATIONS.dev.shareholdercontact WHERE contactid=?";
	
	public static final String SELECT_CURRENTDATEDATA= "SELECT * FROM INV_RELATIONS.dev.shareholdercontact WHERE createdOn >= ? AND createdOn < ?";

	public static final String SELECT_EMAIL="SELECT email  FROM INV_RELATIONS.dev.shareholdercontact ";
  
	public static final String SELECT_CONTACT="SELECT contact  FROM INV_RELATIONS.dev.shareholdercontact ";

	public static final String SELECT_EMAILCOUNT="SELECT COUNT(*) FROM INV_RELATIONS.dev.shareholdercontact WHERE email = ? and NOT email='NULL' ";

	public static final String SELECT_CONTACTCOUNT="SELECT COUNT(*) FROM INV_RELATIONS.dev.shareholdercontact WHERE contact = ? and NOT contact='NULL' ";

}
