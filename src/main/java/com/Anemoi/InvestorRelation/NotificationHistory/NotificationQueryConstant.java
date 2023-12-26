package com.Anemoi.InvestorRelation.NotificationHistory;

public class NotificationQueryConstant {
	
    public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";
	
	public static final String INSERT_NOTIFICATION_TABLE="INSERT INTO INV_RELATIONS.dev.notificationTable values(?,?,?)";

	public static final String SELECT_NOTIFICATION_HISTORY="SELECT * FROM INV_RELATIONS.dev.notificationTable";

	
	public static final String SELECT_ARRAYLIST_USER="SELECT * FROM INV_RELATIONS.dev.notificationTable WHERE nId=?";

	public static final String UPDATE_UAERLIST = "UPDATE INV_RELATIONS.dev.notificationTable SET users=? WHERE nId=?";

}
