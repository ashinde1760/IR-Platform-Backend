package com.Anemoi.InvestorRelation.MeetingScheduler;
 
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
 
import javax.mail.Session;
import javax.mail.Transport;
 
import org.json.simple.JSONObject;
 
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationService;
import com.Anemoi.MailSession.MailService;
import com.azure.identity.UsernamePasswordCredential;
import com.azure.identity.UsernamePasswordCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserRequestBuilder;
 
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
 
 
@Singleton
public class ServiceImp implements ServiceInterface{
 
	@Inject
	MeetingDao meetingDao;
	@Inject
	private AuditHistoryService auditHistoryService;
	@Inject
	private MailService mailService;
	@Inject 
	private NotificationService notificationService;
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	private static String DATABASENAME = "databasename";
 
	private static String dataBaseName() {
		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;
	}
	@Override
	public MSTeamschedule schdeulemsteammeeting(MSTeamschedule msts) throws ServiceException {
//		Transport transport = null;
		try
		{
		String dataBaseName=ServiceImp.dataBaseName();
//		Session session = com.Anemoi.MailSession.MailSessionInstance.getMailSession();
//		transport = session.getTransport();
//		transport.connect();
		if(msts.getMeetingType().equalsIgnoreCase("Microsoft Teams"))
		{
		MSTeamschedule responseobject=this.meetingDao.saveTeamMeetingSchedule(msts,dataBaseName);
		Date d=new Date();
		AuditHistoryEntity entity=new AuditHistoryEntity();
		entity.setCreatedBy(msts.getCreatedBy());
		entity.setActivity("Meeting Schedule");
		entity.setDescription("User Schedule teams meeting");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
//		ArrayList<ClientDetailsEntity> analystAdminList=this.meetingDao.getAnalsytAdminByClientName(msts.getClientName(),dataBaseName);
//	    List<String> clientAdmins=new ArrayList<>();
//	    List<String> analystAdmins=new ArrayList<>();
//		for (ClientDetailsEntity client : analystAdminList) {
//			clientAdmins.addAll(client.getEmailId());
//			analystAdmins.addAll(client.getAssignAA());
//		}
//	    mailService.newMeetingScheduleMailSendToClientAdmin(transport,msts,clientAdmins,analystAdmins);
		//notification code
		List<String> ea=new ArrayList<>();
		ea.add(msts.getCreatedBy());
		ea.addAll(msts.getParticipant());
		Date d2=new Date();
		NotificationEntity e=new NotificationEntity();
		e.setMessage("New Meeting Scheduled");
		e.setUsers(ea);
		e.setCreatedOn(d2.getTime());
		this.notificationService.addNotificationHistory(e);
		return responseobject;

		}
		else if(msts.getMeetingType().equalsIgnoreCase("Google Meet"))
		{
			MSTeamschedule responsegoogle=this.meetingDao.saveGoogleMeetingSchedule(msts,dataBaseName);
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(msts.getCreatedBy());
			entity.setActivity("Meeting Schedule");
			entity.setDescription("User Schedule google meeting");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
//			ArrayList<ClientDetailsEntity> analystAdminList=this.meetingDao.getAnalsytAdminByClientName(msts.getClientName(),dataBaseName);
//		    List<String> clientAdmins=new ArrayList<>();
//		    List<String> analystAdmins=new ArrayList<>();
//			for (ClientDetailsEntity client : analystAdminList) {
//				clientAdmins.addAll(client.getEmailId());
//				analystAdmins.addAll(client.getAssignAA());
//			}
//		    mailService.newMeetingScheduleMailSendToClientAdmin(transport,msts,clientAdmins,analystAdmins);
			//notification code
			List<String> ea=new ArrayList<>();
			ea.add(msts.getCreatedBy());
			ea.addAll(msts.getParticipant());
			Date d1=new Date();
			NotificationEntity e=new NotificationEntity();
			e.setMessage("New Meeting Scheduled");
			e.setUsers(ea);
			e.setCreatedOn(d1.getTime());
			this.notificationService.addNotificationHistory(e);
			return responsegoogle;
		}
		else {
			MSTeamschedule othermeetingRespo=this.meetingDao.saveOtherTypeOfMeeting(msts,dataBaseName);
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(msts.getCreatedBy());
			entity.setActivity("Meeting Schedule");
			entity.setDescription("User Schedule OtherType of meeting");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);

//			ArrayList<ClientDetailsEntity> analystAdminList=this.meetingDao.getAnalsytAdminByClientName(msts.getClientName(),dataBaseName);
//		    List<String> clientAdmins=new ArrayList<>();
//		    List<String> analystAdmins=new ArrayList<>();
//			for (ClientDetailsEntity client : analystAdminList) {
//				clientAdmins.addAll(client.getEmailId());
//				analystAdmins.addAll(client.getAssignAA());
//			}
//		    mailService.newMeetingScheduleMailSendToClientAdmin(transport,msts,clientAdmins,analystAdmins);
			//notification code
			List<String> ea=new ArrayList<>();
			ea.add(msts.getCreatedBy());
			ea.addAll(msts.getParticipant());
			Date dd=new Date();
			NotificationEntity e=new NotificationEntity();
			e.setMessage("New Meeting Scheduled");
			e.setUsers(ea);
			e.setCreatedOn(dd.getTime());
			this.notificationService.addNotificationHistory(e);
			return  othermeetingRespo;
		     }

		}
		catch (Exception e) {
 
                throw new ServiceException(e.getMessage());
		}

	}
 
	@Override
	public ArrayList<MSTeamschedule> getmeedingSheduleList() throws ServiceException {
        try
        {
        	String dataBaseName=ServiceImp.dataBaseName();
        	ArrayList<MSTeamschedule> responselist=this.meetingDao.getmeetingShecdulList(dataBaseName);
        	return responselist;
        }
    	catch (Exception e) {
 
            throw new ServiceException(e.getMessage());
	}
	}
 
	@Override
	public MSTeamschedule updateMeetingSchedule(long meetingSheduleId, MSTeamschedule teamschedule)
			throws ServiceException {
//		Transport transport=null;
		try
		{
		String dataBaseName=ServiceImp.dataBaseName();
//		Session session = com.Anemoi.MailSession.MailSessionInstance.getMailSession();
//		transport = session.getTransport();
//		transport.connect();
		if(teamschedule.getMeetingType().equalsIgnoreCase("Microsoft Teams"))
		{
		MSTeamschedule responseobject=this.meetingDao.updateTeamsmeetingSchedule(meetingSheduleId,teamschedule,dataBaseName);
		Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(teamschedule.getModifiedBy());
			entity.setActivity("Update Meeting ");
			entity.setDescription("Teams Meeting updated in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);

//			ArrayList<ClientDetailsEntity> analystAdminList=this.meetingDao.getAnalsytAdminByClientName(teamschedule.getClientName(),dataBaseName);
//		    List<String> clientAdmins=new ArrayList<>();
//		    List<String> analystAdmins=new ArrayList<>();
//			for (ClientDetailsEntity client : analystAdminList) {
//				clientAdmins.addAll(client.getEmailId());
//				analystAdmins.addAll(client.getAssignAA());
//			}
//		    mailService.updateMeetingMailSendToClientAdmin(transport,teamschedule,clientAdmins,analystAdmins);
			//notification code
			List<String> ea=new ArrayList<>();
			ea.add(teamschedule.getModifiedBy());
			ea.addAll(teamschedule.getParticipant());
			Date d2=new Date();
			NotificationEntity e=new NotificationEntity();
			e.setMessage("Addition /Modifications in Meeting data");
			e.setUsers(ea);
			e.setCreatedOn(d2.getTime());
			this.notificationService.addNotificationHistory(e);
		return responseobject;
		}
		else if(teamschedule.getMeetingType().equalsIgnoreCase("Google Meet"))
		{
			MSTeamschedule response=this.meetingDao.updategoogleMeetingSchedule(meetingSheduleId,teamschedule,dataBaseName);
			 Date d=new Date();
				AuditHistoryEntity entity=new AuditHistoryEntity();
				entity.setCreatedBy(teamschedule.getModifiedBy());
				entity.setActivity("Update Meeting");
				entity.setDescription("Google Meeting updated in application");
				entity.setCreatedOn(d.getTime());
				this.auditHistoryService.addAuditHistory(entity);
//				ArrayList<ClientDetailsEntity> analystAdminList=this.meetingDao.getAnalsytAdminByClientName(teamschedule.getClientName(),dataBaseName);
//			    List<String> clientAdmins=new ArrayList<>();
//			    List<String> analystAdmins=new ArrayList<>();
//				for (ClientDetailsEntity client : analystAdminList) {
//					clientAdmins.addAll(client.getEmailId());
//					analystAdmins.addAll(client.getAssignAA());
//				}
//			    mailService.updateMeetingMailSendToClientAdmin(transport,teamschedule,clientAdmins,analystAdmins);
				//notification code
				List<String> ea=new ArrayList<>();
				ea.add(teamschedule.getModifiedBy());
				ea.addAll(teamschedule.getParticipant());
				Date d2=new Date();
				NotificationEntity e=new NotificationEntity();
				e.setMessage("Addition /Modifications in Meeting data");
				e.setUsers(ea);
				e.setCreatedOn(d2.getTime());
				this.notificationService.addNotificationHistory(e);
				return response;
		}
		else
		{
			MSTeamschedule response=this.meetingDao.updateOtherTypeOfMeeting(meetingSheduleId,teamschedule,dataBaseName);
		 Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(teamschedule.getModifiedBy());
			entity.setActivity("Update Meeting");
			entity.setDescription("otherType Meeting updated in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
//			ArrayList<ClientDetailsEntity> analystAdminList=this.meetingDao.getAnalsytAdminByClientName(teamschedule.getClientName(),dataBaseName);
//		    List<String> clientAdmins=new ArrayList<>();
//		    List<String> analystAdmins=new ArrayList<>();
//			for (ClientDetailsEntity client : analystAdminList) {
//				clientAdmins.addAll(client.getEmailId());
//				analystAdmins.addAll(client.getAssignAA());
//			}
//		    mailService.updateMeetingMailSendToClientAdmin(transport,teamschedule,clientAdmins,analystAdmins);

			//notification code
			List<String> ea=new ArrayList<>();
			ea.add(teamschedule.getModifiedBy());
			ea.addAll(teamschedule.getParticipant());
			Date d2=new Date();
			NotificationEntity e=new NotificationEntity();
			e.setMessage("Addition /Modifications in Meeting data");
			e.setUsers(ea);
			e.setCreatedOn(d2.getTime());
			this.notificationService.addNotificationHistory(e);
		return response;
		}
		}
		catch (Exception e) {
 
                throw new ServiceException(e.getMessage());
		}
	}
 
	@SuppressWarnings("unchecked")
	@Override
	public String deleteScheduleMeeting(long meetingSheduleId,String createdBy) throws ServiceException {
		String meetingId=null;
		String eventId=null;
		String meetingtype=null;
		ResultSet rs=null;
		Connection con=null;
		PreparedStatement psta=null;
		try
		{
			String dataBaseName=ServiceImp.dataBaseName();
			  con=InvestorDatabaseUtill.getConnection();
			  psta=con.prepareStatement(MeetingShedularQueryConstant.SELECT_MEETINGID_EVENTID.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
		        psta.setLong(1,meetingSheduleId);
		       rs=psta.executeQuery();
		       while(rs.next()) {
		    	   meetingId=rs.getString("meetingId");
		    	   eventId=rs.getString("eventId");
		    	   meetingtype=rs.getString("meetingType");
		       }
	if(meetingtype.equalsIgnoreCase("Microsoft Teams"))
	{
		this.meetingDao.deleteScheduleMeetingdetails(meetingId,eventId,dataBaseName);
		 Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("delete Meeting Scheduled");
			entity.setDescription("delete teams meeting in appliation");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
		}
	else if(meetingtype.equalsIgnoreCase("Google Meet"))
	{
		this.meetingDao.deletegoogleMeetings(meetingId,eventId,dataBaseName);
		 Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("delete Meeting Scheduled");
			entity.setDescription("delete google meeting in appliation");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
	}
	else
	{
		this.meetingDao.deleteOtherTypeOFMeeting(meetingSheduleId,dataBaseName);
		 Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(createdBy);
			entity.setActivity("delete Meeting Scheduled");
			entity.setDescription("delete Other meeting in appliation");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
	}

	JSONObject reposneJSON = new JSONObject();
	reposneJSON.put(STATUS, SUCCESS);
	reposneJSON.put(MSG, "delete meeting schedule");
	return reposneJSON.toString();
		}
		catch (Exception ex) {
			throw new ServiceException(ex.getMessage());
		}
	}
 
	@Override
	public MSTeamschedule getMeetingById(long meetingSheduleId) throws ServiceException {
		// TODO Auto-generated method stub
		String dataBaseName=ServiceImp.dataBaseName();
		try
		{
			MSTeamschedule response=this.meetingDao.getMeedingDataById(meetingSheduleId,dataBaseName);
			return response;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
 
	@Override
	 @Scheduled(fixedDelay = "1m") 
	public String updateMeetingStatus() throws ServiceException, SQLException 
	{
		try
		{
		       Date currentDate = new Date();
 
		       // Define the desired format
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
 
		        // Get the time zone offset
		        TimeZone timeZone = TimeZone.getDefault();
		        int rawOffsetMillis = timeZone.getRawOffset();
		        int hours = rawOffsetMillis / (60 * 60 * 1000);
		        int minutes = Math.abs(rawOffsetMillis / (60 * 1000)) % 60;
		        String timezoneOffset = String.format("%+03d:%02d", hours, minutes);
 
		           // Format the date with the time zone offset
		        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Adjust the desired time zone here
		        String formattedDate = dateFormat.format(currentDate) + timezoneOffset;
 
		        // Print the formatted date
		        System.out.println("formattedDate"+formattedDate);

 
		  String dataBaseName=ServiceImp.dataBaseName();
              Connection con=InvestorDatabaseUtill.getConnection();
              PreparedStatement preparedStatement=con.prepareStatement(MeetingShedularQueryConstant.UPDATE_MEETINGSTATUS.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
              preparedStatement.setObject(1, formattedDate);
	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Meeting statuses updated successfully.");
	            } else {
	                System.out.println("No meetings to update.");
	            }
	            return "update meeting status";
		}
	        catch (Exception e) {
				// TODO: handle exception
	        	throw new ServiceException(e.getMessage());
			}


	}
 
}