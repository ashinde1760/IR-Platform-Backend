package com.Anemoi.InvestorRelation.AnalystDetails;

public class AnalystDetailsQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String ANALYSTID = "analystId";

	public static final String ANALYSTNAME = "analystName";

	public static final String POCNAME = "pocName";

	public static final String POCEMAILID = "pocEmailId";
	
	public static final String ANALYSTCONTACTDETAILS="analystContactDetails";

	public static final String CREATEDON = "createdOn";

	public static final String INSERT_INTO_ANALYSTDETAILS = "INSERT INTO #$DataBaseName#$.dev.analystDetails values(?,?,?,?,?,?)";

	public static final String INSERT_INTO_ANALYSTCONTACTDETAILS = "INSERT INTO #$DataBaseName#$.dev.analystContactDetails values(?,?,?)";

	public static final String SELECT_MAX_ANALYSTID= "select MAX(analystId) from #$DataBaseName#$.dev.analystDetails";

	public static final String SELECT_ANALYSTDETAILS_BY_ID = "SELECT * FROM #$DataBaseName#$.dev.analystDetails where analystId=?";

	public static final String SELECT_ANALYSTCONTACTDETIALS_BYID = "SELECT * FROM #$DataBaseName#$.dev.analystContactDetails where analystId=?";
//	public static final String SELECT_ANALYSTCONTACTDETIALS_BYID = "SELECT * FROM #$DataBaseName#$.dev.analystContactDetails, #$DataBaseName#$.dev.analystDetails where #$DataBaseName#$.dev.analystContactDetails.analystId=#$DataBaseName#$.dev.analystDetails.analystId where analystId=?  ORDER BY (pocEmailId) ASC ,RAND()";

//	public static final String SELECT_ANALYSTDETAILS = "SELECT * FROM #$DataBaseName#$.dev.analystDetails ORDER BY (createdOn) DESC ,RAND()";
	
	public static final String SELECT_ANALYSTDETAILS = "SELECT * FROM #$DataBaseName#$.dev.analystContactDetails, #$DataBaseName#$.dev.analystDetails where #$DataBaseName#$.dev.analystContactDetails.analystId=#$DataBaseName#$.dev.analystDetails.analystId ORDER BY (analystName) DESC ,RAND()";
	
	
	public static final String DELETE_ANALYSTDETAILS_BY_ID = "DELETE #$DataBaseName#$.dev.analystDetails where analystId=?";

	public static final String DELETE_ANALYSTCONTACTDETAILS = "DELETE #$DataBaseName#$.dev.analystContactDetails where analystId=? AND analystcontactid=?";

	public static final String UPDATE_ANALYSTDETAILS = "UPDATE #$DataBaseName#$.dev.analystDetails SET analystName=? , modifiedOn=? ,modifiedBy=? where analystId=?";

	public static final String UPDATE_ANALYSTCONTACTDETAILS = "UPDATE #$DataBaseName#$.dev.analystContactDetails SET pocName=?,pocEmailId=? where analystcontactid=?";

	public static final String SELECT_ANALSTNAME = "SELECT analystName FROM #$DataBaseName#$.dev.analystDetails";

	public static final String SELECT_ANALSTNAME2 = "SELECT analystName FROM #$DataBaseName#$.dev.analystDetails WHERE NOT analystId = ?";
	
	public static final String SELECT_POCEMAILID="SELECT COUNT(*) FROM #$DataBaseName#$.dev.analystContactDetails WHERE pocEmailId = ? ";

	public static final String SELECT_POCEMAILIDEXEPETEMAIL="SELECT COUNT(*) FROM #$DataBaseName#$.dev.analystContactDetails WHERE pocEmailId = ? AND NOT analystId = ?";
}
