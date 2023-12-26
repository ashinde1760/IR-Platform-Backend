package com.Anemoi.InvestorRelation.NotificationHistory;

import java.util.ArrayList;

public interface NotificationDao {

	void addNotification(NotificationEntity entity, String dataBaseName);

	ArrayList<NotificationEntity> getNotificationHistory(String dataBaseName);

	String clearById(long nId, String userEmail, String dataBaseName);

	String clearall(String userEmail, String dataBaseName);

	long getnotificationCount(String userEmail, String dataBaseName);
	

}
