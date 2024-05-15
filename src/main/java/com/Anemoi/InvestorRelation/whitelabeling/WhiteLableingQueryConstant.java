package com.Anemoi.InvestorRelation.whitelabeling;

public class WhiteLableingQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";


	
	public static final String INSERT_INTO_WHITELABLEING_TABLE = "INSERT INTO #$DataBaseName#$.dbo.whitelablingtable values(?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_WHITELABLEING_LIST="SELECT * FROM #$DataBaseName#$.dbo.whitelablingtable";

	public static final String SELECT_WHITELABLEING_BYID="SELECT * FROM #$DataBaseName#$.dbo.whitelablingtable WHERE clientName=?";

	public static final String SELECT_LOGOFORCLIENT="SELECT COUNT(*) FROM #$DataBaseName#$.dbo.whitelablingtable WHERE clientName=? ";

	public static final String UPDATE_DEATAILS="UPDATE #$DataBaseName#$.dbo.whitelablingtable SET fileName=?,fileType=?,fileData=?,cssFileName=?,cssFileType=?,cssFileData=?,createdBy=?,createdOn=? WHERE clientName=?";

}
