package com.Anemoi.InvestorRelation.whitelabeling;

public class WhiteLableingQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";


	
	public static final String INSERT_INTO_WHITELABLEING_TABLE = "INSERT INTO #$DataBaseName#$.dev.whitelablingtable values(?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_WHITELABLEING_LIST="SELECT * FROM #$DataBaseName#$.dev.whitelablingtable";

	public static final String SELECT_WHITELABLEING_BYID="SELECT * FROM #$DataBaseName#$.dev.whitelablingtable WHERE clientName=?";

	public static final String SELECT_LOGOFORCLIENT="SELECT COUNT(*) FROM #$DataBaseName#$.dev.whitelablingtable WHERE clientName=? ";

	public static final String UPDATE_DEATAILS="UPDATE #$DataBaseName#$.dev.whitelablingtable SET fileName=?,fileType=?,fileData=?,cssFileName=?,cssFileType=?,cssFileData=?,createdBy=?,createdOn=? WHERE clientName=?";

}
