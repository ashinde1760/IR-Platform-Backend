package com.Anemoi.InvestorRelation.whitelabeling;

public class WhiteLableingQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";


	
	public static final String INSERT_INTO_WHITELABLEING_TABLE = "INSERT INTO INV_RELATIONS.dev.whitelablingtable values(?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_WHITELABLEING_LIST="SELECT * FROM INV_RELATIONS.dev.whitelablingtable";

	public static final String SELECT_WHITELABLEING_BYID="SELECT * FROM INV_RELATIONS.dev.whitelablingtable WHERE clientName=?";

	public static final String SELECT_LOGOFORCLIENT="SELECT COUNT(*) FROM INV_RELATIONS.dev.whitelablingtable WHERE clientName=? ";

	public static final String UPDATE_DEATAILS="UPDATE INV_RELATIONS.dev.whitelablingtable SET fileName=?,fileType=?,fileData=?,cssFileName=?,cssFileType=?,cssFileData=?,createdBy=?,createdOn=? WHERE clientName=?";

}
