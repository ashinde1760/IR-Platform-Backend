package com.Anemoi.InvestorRelation.NotificationHistory;



import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("notificationHistry")
public class NotificationController {

	@Inject
	NotificationService notificationService;
	
	@Delete("clearById/{nId}/{userEmail}")
	public String clearById(@PathVariable("nId")  long nId ,@PathVariable("userEmail") String userEmail)
	{
		try
		{
			
			String response=this.notificationService.clearNotificationById(nId,userEmail);
			return response;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	@Delete("clearAll/{userEmail}")
	public String clearAll(@PathVariable("userEmail") String userEmail)
	{
		try
		{
			
			String response=this.notificationService.clearAllNotification(userEmail);
			return response;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	@Get("getcount/{userEmail}")
	public long getnotification(@PathVariable("userEmail") String userEmail)
	{
		try
		{
			long respo=this.notificationService.getCount(userEmail);
			return respo;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}
	
	@Post("/addNotificationHistory")
	public void addddata (@Body NotificationEntity entity)
	{
		this.notificationService.addNotificationHistory(entity);
	
	}
}
