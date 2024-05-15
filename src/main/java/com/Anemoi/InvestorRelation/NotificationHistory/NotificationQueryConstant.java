package com.Anemoi.InvestorRelation.NotificationHistory;

public class NotificationQueryConstant {
	
    public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";
	
	public static final String INSERT_NOTIFICATION_TABLE="INSERT INTO #$DataBaseName#$.dbo.notificationTable values(?,?,?)";

	public static final String SELECT_NOTIFICATION_HISTORY="SELECT * FROM #$DataBaseName#$.dbo.notificationTable";

//	public static final String SELECT_NOTIFICATION_HISTORY="SELECT TOP 10 * FROM #$DataBaseName#$.dbo.notificationTable ORDER BY nId DESC";

	
	public static final String SELECT_ARRAYLIST_USER="SELECT * FROM #$DataBaseName#$.dbo.notificationTable WHERE nId=?";

//	public static final String UPDATE_UAERLIST = "UPDATE #$DataBaseName#$.dbo.notificationTable SET users=? WHERE nId=?";

	public static final String UPDATE_UAERLIST = " UPDATE #$DataBaseName#$.dbo.notificationTable SET users = REPLACE(users, ?, '') WHERE users LIKE ?;";
	
}
