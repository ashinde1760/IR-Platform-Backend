package com.Anemoi.InvestorRelation.NotificationHistory;

import java.util.ArrayList;
import java.util.List;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class NotificationServiceImpli implements NotificationService {

	@Inject
	NotificationDao dao;
	private static String DATABASENAME = "databasename";

	private static String dataBaseName() {
		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;
	}
	
	@Override
	public void addNotificationHistory(NotificationEntity entity) {
		// TODO Auto-generated method stub
		
		String dataBaseName=NotificationServiceImpli.dataBaseName();
		this.dao.addNotification(entity,dataBaseName);
		
		
	}

	@Override
	public ArrayList<NotificationEntity> getNotificationHistory() {

         try
         {
     		String dataBaseName=NotificationServiceImpli.dataBaseName();
     		ArrayList<NotificationEntity> list=this.dao.getNotificationHistory(dataBaseName);
     		return list;
         }
         catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
		}
		return null;
		
		
	}

	@Override
	public String clearNotificationById(long nId, String userEmail) {
		  try
	         {
	     		String dataBaseName=NotificationServiceImpli.dataBaseName();
	     		String respo=this.dao.clearById(nId,userEmail,dataBaseName);
	     		return respo;
	         }
		  catch (Exception e) {
			// TODO: handle exception
			  e.printStackTrace();
		}
		return null;
	}

	@Override
	public String clearAllNotification(String userEmail) {
		 try
         {
     		String dataBaseName=NotificationServiceImpli.dataBaseName();
     		String respo=this.dao.clearall(userEmail,dataBaseName);
     		return respo;
         }
	  catch (Exception e) {
		// TODO: handle exception
		  e.printStackTrace();
	}
	return null;
	}

	@Override
	public long getCount(String userEmail) {
		  try
	         {
	     		String dataBaseName=NotificationServiceImpli.dataBaseName();
	     		long res=this.dao.getnotificationCount(userEmail,dataBaseName);
	     		return res;
	         }
		  catch (Exception e) {
			// TODO: handle exception
			  e.printStackTrace();
		}
		return 0;
	}

}
