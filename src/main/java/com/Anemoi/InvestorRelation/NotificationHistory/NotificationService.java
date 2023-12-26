package com.Anemoi.InvestorRelation.NotificationHistory;

import java.util.ArrayList;

public interface NotificationService {
	
	public void addNotificationHistory(NotificationEntity entity);

	public ArrayList<NotificationEntity> getNotificationHistory();

	public String clearNotificationById(long nId, String userEmail);

	public String clearAllNotification(String userEmail);

	public long getCount(String userEmail);

}
