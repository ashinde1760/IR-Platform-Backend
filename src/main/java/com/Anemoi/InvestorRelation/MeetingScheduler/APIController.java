package com.Anemoi.InvestorRelation.MeetingScheduler;


import java.util.ArrayList;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/investor/meetingShedular")
public class APIController {
	
	@Inject
	ServiceInterface si;
	
	
	@Post("/scheduleMSTeamMeeting")
	public MSTeamschedule schedulemsteam(MSTeamschedule msts) throws ControllerException
	{
		try
		{

		MSTeamschedule r=this.si.schdeulemsteammeeting(msts);
		return r;
		
		}catch (Exception e) {
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
		
	}
	
	  @Get("/update-meetingStatus")
	    public String updateMeetingStatuses() throws ControllerException {
	     try
	     {
	 
	    	 
	    	 String response=this.si.updateMeetingStatus();
	    	 return response;
	     }
	     catch (Exception e) {
			// TODO: handle exception
	    	 throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
						e.getMessage());
		}
	    }
	
	@Get("/getmeetingDetails")
  public ArrayList<MSTeamschedule> getMeetingSchedueList() throws ControllerException
  {
		try
		{
			ArrayList<MSTeamschedule> responseList=this.si.getmeedingSheduleList();
			return responseList;
		}
		catch (Exception e) {
			
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
  }
	
	@Get("meetingGetById/{meetingSheduleId}")
	public MSTeamschedule getMeetingDetailsById(@PathVariable("meetingSheduleId") long meetingSheduleId) throws ControllerException
	{
		try
		{
			MSTeamschedule response=this.si.getMeetingById(meetingSheduleId);
			return response;
		}
		catch (Exception e) {
			
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Patch("/updateMeetingSchedule/{meetingSheduleId}")
	public MSTeamschedule updateMeetingSchedule(@PathVariable("meetingSheduleId") long meetingSheduleId,@Body MSTeamschedule teamschedule) throws ControllerException
	{
		try
		{
			MSTeamschedule response=this.si.updateMeetingSchedule(meetingSheduleId,teamschedule);
			return response;
		}
		catch (Exception e) {
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Delete("/deleteMeetingSchedule/{meetingSheduleId}")
	public String deleteScheduleMeetingData(@PathVariable("meetingSheduleId") long meetingSheduleId,String createdBy) throws ControllerException
	{
		try
		{
		
			String response=this.si.deleteScheduleMeeting(meetingSheduleId,createdBy);
			return response;
	
		}
		catch (Exception e) {
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	
	}
	

}
