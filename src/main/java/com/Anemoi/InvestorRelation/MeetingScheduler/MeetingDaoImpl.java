package com.Anemoi.InvestorRelation.MeetingScheduler;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.awt.Desktop; 
import java.net.URI;

import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.RoleModel.RoleModelQuaryContant;

import com.azure.identity.UsernamePasswordCredential;
import com.azure.identity.UsernamePasswordCredentialBuilder;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserRequestBuilder;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.TokenResponse;

import jakarta.inject.Singleton;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

@Singleton
public class MeetingDaoImpl implements MeetingDao{

	@Override
	public MSTeamschedule saveTeamMeetingSchedule(MSTeamschedule msts, String dataBaseName) throws SQLException, ClassNotFoundException,DaoException {
		
		Connection con= null;
		PreparedStatement psta=null;
		Date d=new Date();
		try
		{
		  con=InvestorDatabaseUtill.getConnection();
		String meetingUrl =null;
		String meetingId = null;
		final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");
//        String clientId = "2b2cab93-3d96-4200-890d-4b8b02a0c298";
//        String username = "amol.harde@anemoisoftware.com.au";
//        String password = "Anemoi@4444";
		String clientId=ReadPropertiesFile.readMeetingHost("clientId");
		String username=ReadPropertiesFile.readMeetingHost("username");
		String password=ReadPropertiesFile.readMeetingHost("password");
        final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
                .clientId(clientId).username(username).password(password).build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes,
                usernamePasswordCredential);

        @SuppressWarnings("rawtypes")
		final GraphServiceClient graphClient = GraphServiceClient.builder()
                .authenticationProvider(tokenCredentialAuthProvider).buildClient();
        
        // Create a new OnlineMeeting object
        OnlineMeeting onlineMeeting = new OnlineMeeting();
        
 	   OffsetDateTime startDateTime=getstartDateTime(msts.getMeetingDate(),msts.getStartTime());

	   OffsetDateTime endDateTime=getendDateTime(msts.getMeetingDate(),msts.getEndTime());

       

onlineMeeting.startDateTime = startDateTime;
onlineMeeting.endDateTime = endDateTime;
//        onlineMeeting.startDateTime = OffsetDateTime.parse(msts.getStartTime());
//        onlineMeeting.endDateTime = OffsetDateTime.parse(msts.getEndTime());
        onlineMeeting.subject = msts.getTitle();
        onlineMeeting.recordAutomatically=msts.isRecordAutomatically();
        
		try {
			// Create the meeting
			OnlineMeeting createdMeeting = graphClient.me().onlineMeetings().buildRequest().post(onlineMeeting);
			meetingUrl = createdMeeting.joinWebUrl;
			meetingId = createdMeeting.id;

			System.out.println("createdMeeting.id:" + createdMeeting.id);

			// Print the meeting URL
			System.out.println("Meeting URL: " + createdMeeting.joinWebUrl);
		} catch (ClientException ex) {
			System.out.println("Error creating the meeting: " + ex.getMessage());
		}
		
		com.microsoft.graph.models.Event event = new com.microsoft.graph.models.Event();
		// event.subject = "Team Meeting";
		event.subject = msts.getTitle();
		event.start = new DateTimeTimeZone();
		event.start.dateTime = onlineMeeting.startDateTime.toString();
		event.start.timeZone = "Asia/Kolkata";
		event.end = new DateTimeTimeZone();
		event.end.dateTime = onlineMeeting.endDateTime.toString();
		event.end.timeZone = "Asia/Kolkata";

		// Add the meeting URL as a join link in the event description
		event.body = new ItemBody();
		event.body.content = msts.getAgenda() + "\n" + meetingUrl;
		
		
		 // Retrieve the list of email IDs from the MSteam object
        List<String> attendeeEmails = msts.getParticipant();

        // Create a list to hold the attendees
        List<Attendee> attendees = new ArrayList<>();

        // Loop through the email IDs and create an attendee for each
        for (String email : attendeeEmails) {
            Attendee attendee = new Attendee();
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.address = email;
            attendee.emailAddress = emailAddress;
            attendees.add(attendee);
        }

        // Add the attendees to the event
        event.attendees = attendees;


        // Add the event to the user's calendar
        UserRequestBuilder userRequestBuilder = graphClient.me();
        com.microsoft.graph.models.Event createdEvent = userRequestBuilder
                .calendar()
                .events()
                .buildRequest()
                .post(event);
        
        String eventId = createdEvent.id;
      msts.setMeetingId(meetingId);
      msts.setEventId(eventId);
      msts.setJoinUrl(meetingUrl);
      
      String scheduledDateTimeString =endDateTime.toString();
      
      // Parse the scheduled date and time string
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
      OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);

      // Get the current date and time
      OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

      // Check if the current date and time is after the scheduled meeting date and time
      if (currentDateTime.isAfter(scheduledDateTime)) {
          System.out.println("The meeting has been completed.");
          msts.setStatus("Completed");
          // Set the meeting status to completed
          // (You can implement your own logic to update the meeting status in your application)
      } else {
          System.out.println("The meeting is yet to be completed.");
          msts.setStatus("Schedule");
      }
        psta=con.prepareStatement(MeetingShedularQueryConstant.INSERT_INTO_MEETINGSHUDULAR.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
        psta.setString(1, meetingId);
        psta.setString(2, eventId);
        psta.setString(3, meetingUrl);
        psta.setString(4, msts.getTitle());
        psta.setString(5, msts.getAgenda());
        psta.setString(6, msts.getParticipant().toString());
        psta.setString(7, msts.getMeetingDate());
        psta.setString(8, startDateTime.toString());
        psta.setString(9, endDateTime.toString());
        psta.setString(10, msts.getMeetingType());
        psta.setBoolean(11,msts.isRecordAutomatically());
        psta.setString(12, "No");
        psta.setString(13, msts.getCreatedBy());
        psta.setLong(14, d.getTime());
        psta.setString(15, null);
        psta.setLong(16,d.getTime());
        psta.setString(17, msts.getClientName());
        psta.setString(18, msts.getStatus());
        psta.setString(19, msts.getFundGroup().toString());
        psta.setString(20, msts.getRemark());
        psta.executeUpdate();
        return msts;
 
        
	}
	catch (Exception e) {
		// TODO: handle exception
		throw new  DaoException(e.getMessage());
	}
		finally {
		
			InvestorDatabaseUtill.close(psta, con);

		}
	
	}

	@Override
	public ArrayList<MSTeamschedule> getmeetingShecdulList(String dataBaseName) throws DaoException {
              Connection con=null;
              PreparedStatement psta=null;
              ResultSet rs=null;
              ArrayList<MSTeamschedule> list=new ArrayList<>();
              try
              {
            	  con=InvestorDatabaseUtill.getConnection();
            	  psta=con.prepareStatement(MeetingShedularQueryConstant.SELECT_SHEDULEMEETINGDETAILS.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
            	  rs=psta.executeQuery();
            	  while(rs.next())
            	  {
            		  MSTeamschedule response=this.bulidData(rs);
            		  list.add(response);
            		  
            		  
            	  }
            	  return list;
              }
              catch (Exception e) {
				// TODO: handle exception
            	  throw new  DaoException(e.getMessage());
			}

      		finally {
      		
      			InvestorDatabaseUtill.close(psta, con);

      		}
              

	}

	private MSTeamschedule bulidData(ResultSet rs) throws SQLException {
		MSTeamschedule schedule=new MSTeamschedule();
		schedule.setMeetingSheduleId(rs.getLong("meetingSheduleId"));
		schedule.setMeetingId(rs.getString("meetingId"));
		schedule.setEventId(rs.getString("eventId"));
		schedule.setJoinUrl(rs.getString("joinUrl"));
		schedule.setTitle(rs.getString("title"));
		schedule.setAgenda(rs.getString("agenda"));
		
		 String participantField = rs.getString("participant");
		    participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
		    String[] participants = participantField.split(", ");
		    ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));
		    schedule.setParticipant(participantList);

		schedule.setMeetingDate(rs.getString("meetingDate"));
		schedule.setStartTime(rs.getString("startTime"));
		schedule.setEndTime(rs.getString("endTime"));
//		 String scheduledDateTimeString =schedule.getEndTime();
//	        
//	        // Parse the scheduled date and time string
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
//	        OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);
//
//	        // Get the current date and time
//	        OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);
//
//	        // Check if the current date and time is after the scheduled meeting date and time
//	        if (currentDateTime.isAfter(scheduledDateTime)) {
//	            System.out.println("The meeting has been completed.");
//	            schedule.setStatus("completed");
//	            // Set the meeting status to completed
//	            // (You can implement your own logic to update the meeting status in your application)
//	        } else {
//	            System.out.println("The meeting is yet to be completed.");
//	            schedule.setStatus("schedule");
//	        }
		schedule.setMeetingType(rs.getString("meetingType"));
		schedule.setRecordAutomatically(rs.getBoolean("recordAutomatically"));
		schedule.setMeetingDataStatus(rs.getString("meetingDataStatus"));
		schedule.setCreatedBy(rs.getString("createdBy"));
		schedule.setCreatedOn(rs.getLong("createdOn"));
		schedule.setModifiedBy(rs.getString("modifiedBy"));
		schedule.setModifiedOn(rs.getLong("modifiedOn"));
		schedule.setClientName(rs.getString("clientName"));
		schedule.setStatus(rs.getString("status"));
		 String participantFiel = rs.getString("fundGroup");
		    participantFiel = participantFiel.substring(1, participantFiel.length() - 1); // Remove the square brackets
		    String[] fundGroup = participantFiel.split(", ");
		    ArrayList<String> fundGrouplist = new ArrayList<>(Arrays.asList(fundGroup));
		    schedule.setFundGroup(fundGrouplist);
		schedule.setRemark(rs.getString("remark"));
		return schedule;
		
		
	}

	@Override
	public MSTeamschedule saveGoogleMeetingSchedule(MSTeamschedule msts, String dataBaseName) throws DaoException, GeneralSecurityException, IOException {
	

		Connection con= null;
		PreparedStatement psta=null;
		  String meetingLink=null;
		try
		{
		
		  con=InvestorDatabaseUtill.getConnection();
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

	    // Authorize the user
	   // Credential credential = authorize();
	    
	    Credential credential = authorize2(httpTransport);
	 //   String accessToken = "ya29.a0AbVbY6N5sop6fNaX7_MjuHFZlqfZlb0Rj7X1DAE41L9B2T1jPFyR-Wsrz2pmrrLfMBldy4EYm0h85-6bRDeApAwwRWGc-gd1W4NslZTqcwMdj66KoVpgZR54yxCYW2G20kde3sU0w4pPnQtdxqDyUqU8Dgj3aCgYKAR0SARESFQFWKvPlLDl0pkZIJd9LSUpszPXqRA0163";

	    // Create the GoogleCredential using the access token
//	    @SuppressWarnings("deprecation")
//		GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

	    // Create a new Calendar service
	    Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credential)
	            .setApplicationName("My Calendar App")
	            .build();
	    
	 	   OffsetDateTime startDateTime=getstartDateTime(msts.getMeetingDate(),msts.getStartTime());

		   OffsetDateTime endDateTime=getendDateTime(msts.getMeetingDate(),msts.getEndTime());
		   
        Event event = new Event()
        		.setSummary(msts.getTitle())
                .setDescription(msts.getAgenda());
             // Set the start date and time
                EventDateTime startDateTime1 = new EventDateTime()
                    .setDateTime(new DateTime(startDateTime.toInstant().toString()))
                    .setTimeZone("Asia/Kolkata");
                event.setStart(startDateTime1);

                // Set the end date and time
                EventDateTime endDateTime1 = new EventDateTime()
                    .setDateTime(new DateTime(endDateTime.toInstant().toString()))
                    .setTimeZone("Asia/Kolkata");
                event.setEnd(endDateTime1);
//                .setStart(new EventDateTime().setDateTime(new DateTime(msts.getStartTime())))
//                .setEnd(new EventDateTime().setDateTime(new DateTime(msts.getEndTime())));
               
        // Set conference details
        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        createConferenceRequest.setRequestId(UUID.randomUUID().toString());

        
        
        // Specify Google Meet as the conference solution
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey()
                .setType("hangoutsMeet");
        createConferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);
        
  

        conferenceData.setCreateRequest(createConferenceRequest);
        event.setConferenceData(conferenceData);

//        // Set attendees
//        EventAttendee attendee1 = new EventAttendee().setEmail("amolharde98@gmail.com");
//        EventAttendee attendee2 = new EventAttendee().setEmail("amol.harde@anemoisoftware.com.au");
//        event.setAttendees(Arrays.asList(attendee1, attendee2));
        
     // Retrieve attendees from the front end
        List<String> attendeeEmails = msts.getParticipant();
        List<EventAttendee> attendees = new ArrayList<>();
        for (String email : attendeeEmails) {
            EventAttendee attendee = new EventAttendee().setEmail(email);
            attendees.add(attendee);
        }
        event.setAttendees(attendees);

        // Schedule the event
        String calendarId = "primary"; // Use "primary" for the user's primary calendar
//        Event createdEvent = service.events().insert(calendarId, event).execute();
        Event createdEvent = service.events().insert(calendarId, event)
	            .setConferenceDataVersion(1)
	            .setSendNotifications(true) // Optional: To send notifications to event attendees
	            .execute();

        
        // Get the conference data from the created event
        ConferenceData createdConferenceData = createdEvent.getConferenceData();

        if (createdConferenceData != null) {
            CreateConferenceRequest createdCreateRequest = createdConferenceData.getCreateRequest();

            if (createdCreateRequest != null) {
                ConferenceSolutionKey createdConferenceSolutionKey = createdCreateRequest.getConferenceSolutionKey();

                if (createdConferenceSolutionKey != null && createdConferenceSolutionKey.getType().equals("hangoutsMeet")) {
                    // Retrieve the meeting link from the event
                     meetingLink = createdEvent.getHangoutLink();
                    
                    System.out.println("Meeting Link: " + meetingLink);
                    // Send the meeting link to attendees via email
                    sendMeetingLinkToAttendees(attendeeEmails, meetingLink);
                }
            }
        }

        System.out.println("Event created: " + createdEvent.getHtmlLink());
        String eventid=createdEvent.getHtmlLink();
        sendMeetingLinkToAttendees(attendeeEmails, eventid);
    
        String eventID = createdEvent.getId();
        String meetingID = createdConferenceData.getConferenceId();
        Date d=new Date();
        System.out.println("eventID:"+eventID);
        System.out.println("meetingID:"+meetingID);
        msts.setMeetingId(meetingID);
        msts.setEventId(eventID);
        msts.setJoinUrl(meetingLink);
        String scheduledDateTimeString =endDateTime.toString();
        
        // Parse the scheduled date and time string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
        OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);

        // Get the current date and time
        OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

        // Check if the current date and time is after the scheduled meeting date and time
        if (currentDateTime.isAfter(scheduledDateTime)) {
            System.out.println("The meeting has been completed.");
            msts.setStatus("Completed");
            // Set the meeting status to completed
            // (You can implement your own logic to update the meeting status in your application)
        } else {
            System.out.println("The meeting is yet to be completed.");
            msts.setStatus("Schedule");
        }
        psta=con.prepareStatement(MeetingShedularQueryConstant.INSERT_INTO_MEETINGSHUDULAR.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
        psta.setString(1, meetingID);
        psta.setString(2, eventID);
        psta.setString(3, meetingLink);
        psta.setString(4, msts.getTitle());
        psta.setString(5, msts.getAgenda());
        psta.setString(6, msts.getParticipant().toString());
        psta.setString(7, msts.getMeetingDate());
        psta.setString(8, startDateTime.toString());
        psta.setString(9, endDateTime.toString());
        psta.setString(10, msts.getMeetingType());
        psta.setBoolean(11,msts.isRecordAutomatically());
        psta.setString(12, "No");
        psta.setString(13, msts.getCreatedBy());
        psta.setLong(14, d.getTime());
        psta.setString(15, null);
        psta.setLong(16, d.getTime());
        psta.setString(17, msts.getClientName());
        psta.setString(18, msts.getStatus());
        psta.setString(19, msts.getFundGroup().toString());
        psta.setString(20, msts.getRemark());
        psta.executeUpdate();
        return msts;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DaoException(e.getMessage());
			
		}

		finally {
		
			InvestorDatabaseUtill.close(psta, con);

		}

	}


	@SuppressWarnings("deprecation")
	private Credential authorize2(NetHttpTransport httpTransport) throws IOException {
		
//		String clientId="612658586170-2tom539ckqsd378ada5l3d0l4tq01fkv.apps.googleusercontent.com";
//		String clientSecrete="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";
//		String refreshToken="1//0gogsYzLxuEuGCgYIARAAGBASNwF-L9IrX0QisgeFZ0rUrBLc6NDhC0T2f0HZpibycuDyOHf2MKIlSMkhdsL198O_KzLloH9J1oc";
		
		String clientId=ReadPropertiesFile.readMeetingHost("clientIdGoogle");
		String clientSecrete=ReadPropertiesFile.readMeetingHost("clientSecrete");
		String refreshToken=ReadPropertiesFile.readMeetingHost("refreshToken");
		
		JsonFactory	JSON_FACTORY = JacksonFactory.getDefaultInstance();
		@SuppressWarnings("deprecation")
		GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecrete)
                .build();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientId, clientSecrete,
                Collections.singleton(CalendarScopes.CALENDAR))
                .build();

        //String refreshToken = "1//0gogsYzLxuEuGCgYIARAAGBASNwF-L9IrX0QisgeFZ0rUrBLc6NDhC0T2f0HZpibycuDyOHf2MKIlSMkhdsL198O_KzLloH9J1oc"; // Obtain this through the authorization process
        credential.setRefreshToken(refreshToken);
        credential.refreshToken();
 
        return credential;
		
	}

	private void sendMeetingLinkToAttendees(List<String> attendeeEmails, String meetingLink) {
		for (String email : attendeeEmails) {
            // Send email with the meeting link to each attendee
            System.out.println("Email sent to: " + email);
        }
		
	}

	private Credential authorize() throws GeneralSecurityException, IOException {
		
		InputStream in = ServiceImp.class.getResourceAsStream("/credentials.json");
		 
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		List<String> scopes = Collections.singletonList(CalendarScopes.CALENDAR);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

		

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport,
				jsonFactory,
		        clientSecrets,
		        scopes)
		        .build();
		
         

       // return credential;
		AuthorizationCodeInstalledApp.DefaultBrowser browser = new AuthorizationCodeInstalledApp.DefaultBrowser();
		String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri("https://investor-relation.anemoi.co.in:8888/callback").build();
		System.out.println("Please open the following address in your browser:");
		System.out.println("authorizationUrl:"+authorizationUrl);
		//browser.browse(authorizationUrl);
		//URI url = new URI(authorizationUrl);

       // Desktop.getDesktop().browse(url);
		// Open the URL in the user's browser
		browser.browse(authorizationUrl);
		LocalServerReceiver receiver = new LocalServerReceiver.Builder()
				.setHost("investor-relation.anemoi.co.in")
				.setPort(8888)
				.setCallbackPath("/callback")
				.build();
		// Now, call the authorize method to wait for the user to grant access and get the credentials
		Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		credential.getAccessToken();
		credential.refreshToken();
		return credential;

//		InputStream in = new FileInputStream("src/main/resources/credentials.json"); // Path to your credentials.json file
//		InputStream in = MeetingDaoImpl.class.getResourceAsStream("/credentials.json");
//
//
//		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
//
//		List<String> scopes = Collections.singletonList(CalendarScopes.CALENDAR);
//        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//		        GoogleNetHttpTransport.newTrustedTransport(),
//		        JacksonFactory.getDefaultInstance(),
//		        clientSecrets,
//		        scopes)
//		        .build();

		//  return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder()
		//          .setPort(8888)
		//          .setCallbackPath("/callback")
		//          .build())
		//          .authorize("user");
		// Set up the local server receiver to handle the callback

//Set up the local server receiver to handle the callback

	//	String callbackUri = "http://investor-relation.anemoi.co.in/callback";
//		LocalServerReceiver receiver = new LocalServerReceiver.Builder()
//		.setPort(8888)
//		.setHost("investor-relation.anemoi.co.in")
//		.setCallbackPath("/callback")
//		.build();
//		// Perform the OAuth2 authorization process
//		Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
//		.authorize("user");
//		return credential;

        // AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
		// //.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("/path/to/token")))
		// .setAccessType("online")
		// .build();



		// // Generate the authorization URL
		// AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
		// authorizationUrl.setRedirectUri("http://investor-relation.anemoi.co.in/callback");
		// String url = authorizationUrl.build();
		// System.out.println(url);

		// //Open the authorization URL in the user's default browser
		// if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		// Desktop.getDesktop().browse(URI.create(url));
		// } else {
		// System.out.println("Please open the following address in your browser:");
		// System.out.println(url);
		// }

		// // Read the authorization code from the user
		// System.out.println("Enter the authorization code:");
		// java.util.Scanner scanner = new java.util.Scanner(System.in);
		// String authorizationCode = scanner.nextLine();
		// scanner.close();

		// // Exchange the authorization code for credentials and get the Credential object
		// TokenResponse tokenresponse = flow.newTokenRequest(authorizationCode).setRedirectUri("http://investor-relation.anemoi.co.in/callback").execute();
		// Credential credential = flow.createAndStoreCredential(tokenresponse, "amolharde468@gmail.com");
		// return credential;
	}

	@Override
	public MSTeamschedule updateTeamsmeetingSchedule(long meetingSheduleId, MSTeamschedule teamschedule,String dataBaseName)
			throws DaoException {
		Connection con= null;
		PreparedStatement psta=null;
		String e = null;
		try
		{
		  con=InvestorDatabaseUtill.getConnection();
		
        System.out.println("meetingId:"+teamschedule.getMeetingId());
        final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");
        String clientId=ReadPropertiesFile.readMeetingHost("clientId");
		String username=ReadPropertiesFile.readMeetingHost("username");
		String password=ReadPropertiesFile.readMeetingHost("password");
        

	    final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
	            .clientId(clientId).username(username).password(password).build();

	    final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes,
	            usernamePasswordCredential);

	    final GraphServiceClient graphClient = GraphServiceClient.builder()
	            .authenticationProvider(tokenCredentialAuthProvider).buildClient();

	    try {
	        // Retrieve the existing meeting
	        OnlineMeeting existingMeeting = graphClient.me().onlineMeetings(teamschedule.getMeetingId())
	                .buildRequest()
	                .get();
	        
	        System.out.println(teamschedule.getStartTime());
	        System.out.println(teamschedule.getEndTime());

	        // Create a new OnlineMeeting object with the updated details
	        OnlineMeeting updatedMeeting = new OnlineMeeting();
	        
	        
	  	   OffsetDateTime startDateTime=getstartDateTime(teamschedule.getMeetingDate(),teamschedule.getStartTime());

	 	   OffsetDateTime endDateTime=getendDateTime(teamschedule.getMeetingDate(),teamschedule.getEndTime());
	 	   
	        updatedMeeting.startDateTime =startDateTime;
	        updatedMeeting.endDateTime = endDateTime;
	        updatedMeeting.subject = teamschedule.getTitle();
	        // Update the meeting with the modified details
	        OnlineMeeting updatedOnlineMeeting = graphClient.me().onlineMeetings(teamschedule.getMeetingId())
	                .buildRequest()
	                .patch(updatedMeeting);
	        
	      e=updatedOnlineMeeting.joinWebUrl;

	        	String eventId=teamschedule.getEventId();	
	        System.out.println("eventId:"+eventId);
	        com.microsoft.graph.models.Event event = graphClient.me().events(eventId)
	                .buildRequest()
	                .get();
	        
	     // Update the event details
	        event.subject = teamschedule.getTitle();
	        event.start.dateTime = updatedMeeting.startDateTime.toString();
	        event.end.dateTime = updatedMeeting.endDateTime.toString();

	        // Retrieve the list of email IDs from the UpdateMSTeam object
	        List<String> attendeeEmails =teamschedule.getParticipant();

	        // Create a list to hold the attendees
	        List<Attendee> attendees = new ArrayList<>();

	        // Loop through the email IDs and create an attendee for each
	        for (String email : attendeeEmails) {
	            Attendee attendee = new Attendee();
	            EmailAddress emailAddress = new EmailAddress();
	            emailAddress.address = email;
	            attendee.emailAddress = emailAddress;
	            attendees.add(attendee);
	        }

	        // Add the attendees to the event
	        event.attendees = attendees;

	        // Update the event with the modified attendees
	        graphClient.me().events(eventId)
	                .buildRequest()
	                .patch(event);

	        System.out.println("Meeting time and attendees updated successfully.");

	        
	      //  e="Meeting time and attendees updated successfully.";
	        // Set the event on the calendar
	        graphClient.me().calendar().events()
	                .buildRequest()
	                .post(event);
                Date  d=new Date();
                String scheduledDateTimeString =endDateTime.toString();
                
                // Parse the scheduled date and time string
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
                OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);

                // Get the current date and time
                OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

                // Check if the current date and time is after the scheduled meeting date and time
                if (currentDateTime.isAfter(scheduledDateTime)) {
                    System.out.println("The meeting has been completed.");
                    teamschedule.setStatus("Completed");
                    // Set the meeting status to completed
                    // (You can implement your own logic to update the meeting status in your application)
                } else {
                    System.out.println("The meeting is yet to be completed.");
                    teamschedule.setStatus("Schedule");
                }
	        psta=con.prepareStatement(MeetingShedularQueryConstant.UPDATE_MEETINGSCHEDULE.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
	        psta.setString(1, teamschedule.getTitle());
	        psta.setString(2, teamschedule.getAgenda());
	        psta.setString(3, teamschedule.getParticipant().toString());
	        psta.setString(4, teamschedule.getMeetingDate());
	        psta.setString(5, startDateTime.toString());
	        psta.setString(6, endDateTime.toString());
	        psta.setString(7, teamschedule.getMeetingType());
	        psta.setBoolean(8,teamschedule.isRecordAutomatically());
	        psta.setString(9, teamschedule.getModifiedBy());
	        psta.setLong(10, d.getTime());
	        psta.setString(11, teamschedule.getClientName());
	        psta.setString(12, teamschedule.getStatus());
	        psta.setString(13, teamschedule.getFundGroup().toString());
	        psta.setString(14, teamschedule.getRemark());
	        psta.setLong(15, meetingSheduleId);
	        
	        psta.executeUpdate();
	        return teamschedule;
	    } catch (ClientException ex) {
	        System.out.println("An error occurred: " + ex.getMessage());
	    }
		}catch (Exception exception) {
			throw new  DaoException(exception.getMessage());
		}

		finally {
		
			InvestorDatabaseUtill.close(psta, con);

		}
		return null;
	 
	}




	@Override
	public MSTeamschedule updategoogleMeetingSchedule(long meetingSheduleId, MSTeamschedule teamschedule,
			String dataBaseName) throws DaoException {
		Connection con= null;
		PreparedStatement psta=null;
		String e = null;
		  String meetingLink=null;
		try
		{
		  con=InvestorDatabaseUtill.getConnection();
		  NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		  JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		  // Authorize the user

//		  Credential credential = authorize();
		  Credential credential = authorize2(httpTransport);

		  // Create a new Calendar service

		  Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credential)

		  .setApplicationName("My Calendar App")

		  .build();

		  // Get the existing meeting link

		  String existingMeetingLink ="https:/meet.google.com/"+ teamschedule.getMeetingId();

	 	   OffsetDateTime startDateTime=getstartDateTime(teamschedule.getMeetingDate(),teamschedule.getStartTime());

		   OffsetDateTime endDateTime=getendDateTime(teamschedule.getMeetingDate(),teamschedule.getEndTime());
		  Event updatedEvent = new Event();

		  updatedEvent.setSummary(teamschedule.getTitle());

		  updatedEvent.setDescription(teamschedule.getAgenda());

		  EventDateTime startDateTime1 = new EventDateTime()
                  .setDateTime(new DateTime(startDateTime.toInstant().toString()))
                  .setTimeZone("Asia/Kolkata");
		  updatedEvent.setStart(startDateTime1);
		  EventDateTime endDateTime1 = new EventDateTime()
                  .setDateTime(new DateTime(endDateTime.toInstant().toString()))
                  .setTimeZone("Asia/Kolkata");
		  updatedEvent.setEnd(endDateTime1);

		  // Retrieve attendees from the front end
		// // Set conference details

			ConferenceData conferenceData = new ConferenceData();

           CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();

        createConferenceRequest.setRequestId(UUID.randomUUID().toString());
//			Specify Google Meet as the conference solution

			 ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey()

			 .setType("hangoutsMeet");

			 createConferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);

	
			 conferenceData.setCreateRequest(createConferenceRequest);

			 updatedEvent.setConferenceData(conferenceData);


		  List<String> attendeeEmails =teamschedule.getParticipant();
		  List<EventAttendee> attendees = new ArrayList<>();

		  for (String email : attendeeEmails) {

		  EventAttendee attendee = new EventAttendee().setEmail(email);

		  attendees.add(attendee);

		  }

		  updatedEvent.setAttendees(attendees);

		  updatedEvent.setHangoutLink(existingMeetingLink); // Keep the existing meeting link

		  String calendarId="primary";
		  // Update the event

		  Event updatedEvent1 = service.events().update(calendarId, teamschedule.getEventId(), updatedEvent)

		  .setConferenceDataVersion(1)

		  .setSendNotifications(true)

		  .execute();

		  // Get the conference data from the created event

		  ConferenceData createdConferenceData = updatedEvent.getConferenceData();

		  if (createdConferenceData != null) {

		  CreateConferenceRequest createdCreateRequest = createdConferenceData.getCreateRequest();

		  if (createdCreateRequest != null) {

		  ConferenceSolutionKey createdConferenceSolutionKey = createdCreateRequest.getConferenceSolutionKey();

		  if (createdConferenceSolutionKey != null && createdConferenceSolutionKey.getType().equals("hangoutsMeet")) {

		  // Retrieve the meeting link from the event

		  meetingLink = updatedEvent1.getHangoutLink();

		  // Send the meeting link to attendees via email

		  sendMeetingLinkToAttendees(attendeeEmails, meetingLink);
		  }}}
		  System.out.println("Event updated: " + updatedEvent1.getHtmlLink());

		  String eventid1= updatedEvent1.getId();

		  String meetingID = createdConferenceData.getConferenceId();

		  System.out.println("meetingID:"+meetingID);

		  System.out.println("eventid1:"+eventid1);
          Date d=new Date();
          String scheduledDateTimeString =endDateTime.toString();
          
          // Parse the scheduled date and time string
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
          OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);

          // Get the current date and time
          OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

          // Check if the current date and time is after the scheduled meeting date and time
          if (currentDateTime.isAfter(scheduledDateTime)) {
              System.out.println("The meeting has been completed.");
              teamschedule.setStatus("Completed");
              // Set the meeting status to completed
              // (You can implement your own logic to update the meeting status in your application)
          } else {
              System.out.println("The meeting is yet to be completed.");
              teamschedule.setStatus("Schedule");
          }
		  sendMeetingLinkToAttendees(attendeeEmails, teamschedule.getEventId());
		  psta=con.prepareStatement(MeetingShedularQueryConstant.UPDATE_GOOGLEMEETING.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
	     psta.setString(1, meetingLink)  ;
		  psta.setString(2, teamschedule.getTitle());
	        psta.setString(3, teamschedule.getAgenda());
	        psta.setString(4, teamschedule.getParticipant().toString());
	        psta.setString(5, teamschedule.getMeetingDate());
	        psta.setString(6, startDateTime.toString());
	        psta.setString(7, endDateTime.toString());
	        psta.setString(8, teamschedule.getMeetingType());
	        psta.setBoolean(9,teamschedule.isRecordAutomatically());
	        psta.setString(10, teamschedule.getModifiedBy());
	        psta.setLong(11, d.getTime());
	        psta.setString(12, teamschedule.getClientName());
	        psta.setString(13, teamschedule.getStatus());
	        psta.setString(14, teamschedule.getFundGroup().toString());
	        psta.setString(15, teamschedule.getRemark());
	        psta.setLong(16, meetingSheduleId);
	        psta.executeUpdate();
	        teamschedule.setJoinUrl(meetingLink);
		   return teamschedule;

		}
		catch (Exception exce) {
			throw new  DaoException(exce.getMessage());
		}

		finally {
		
			InvestorDatabaseUtill.close(psta, con);

		}
	
	}

	@Override
	public MSTeamschedule getMeedingDataById(long meetingSheduleId, String dataBaseName) throws DaoException {
		 Connection con=null;
         PreparedStatement psta=null;
         ResultSet rs=null;
         try
         {
        	 con=InvestorDatabaseUtill.getConnection();
        	 psta=con.prepareStatement(MeetingShedularQueryConstant.SELECT_SHEDULEMEETINGDETAILS_BYID.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
        	 psta.setLong(1, meetingSheduleId);
        	 rs=psta.executeQuery();
        	 while(rs.next())
        	 {
        		 MSTeamschedule teamschedule=this.bulidData(rs);
        		 return teamschedule;
        		 
        	 }
        	 
         }
         catch (Exception e) {
        	 throw new  DaoException("unable to get meeting details"+e.getMessage());
		}

 		finally {
 		
 			InvestorDatabaseUtill.close(psta, con);

 		}
		return null;
	}

	@Override
	public void deleteScheduleMeetingdetails(String meetingId, String eventId, String dataBaseName)
			throws DaoException {
		final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");
		String clientId=ReadPropertiesFile.readMeetingHost("clientId");
		String username=ReadPropertiesFile.readMeetingHost("username");
		String password=ReadPropertiesFile.readMeetingHost("password");
    	Connection con= null;
		PreparedStatement psta=null;
		String e=null;
	
		try
		{
	
	    final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
	            .clientId(clientId).username(username).password(password).build();

	    final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes,
	            usernamePasswordCredential);

	    final GraphServiceClient graphClient = GraphServiceClient.builder()
	            .authenticationProvider(tokenCredentialAuthProvider).buildClient();


        // Cancel the meeting
        try {
            graphClient.me().onlineMeetings(meetingId)
                    .buildRequest()
                    .delete();
            System.out.println("Meeting cancelled successfully.");
          //  OnlineMeeting meeting = graphClient.me().onlineMeetings(meetingid).buildRequest().get();  
            e="Meeting cancelled successfully.";
        } catch (Exception ex) {
            System.out.println("An error occurred while cancelling the meeting: " + ex.getMessage());
            
            e="An error occurred while cancelling the meeting: ";
        }
        
      
        System.out.println("Event ID: " + eventId);
        
        graphClient.me().events(eventId)
        .buildRequest()
        .delete();
        con=InvestorDatabaseUtill.getConnection();
        psta=con.prepareStatement(MeetingShedularQueryConstant.DELETE_MEETINGSCHEDULE.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
        psta.setString(1,meetingId);
       psta.executeUpdate();
       System.out.println("teams meeting  delete successfully");
		}catch (Exception ex) {
			throw new  DaoException(ex.getMessage());
		}

		finally {
		
			InvestorDatabaseUtill.close(psta, con);

		}
		
	}

	@Override
	public void deletegoogleMeetings(String meetingId, String eventId, String dataBaseName) throws DaoException {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement psta=null;
		
		try
		{
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();


//		Credential credential = authorize();
		Credential credential = authorize2(httpTransport);

		Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credential)

		.setApplicationName("My Calendar App").build();

		String calendarId = "primary";

		// Get the event details

		Event eventDetails1 = service.events().get(calendarId, eventId).execute();

		String updatedEventId = null;

		// Check if conference data exists

		if (eventDetails1.getConferenceData() != null) {

		// Remove the conference data

		eventDetails1.setConferenceData(null);
		Event updatedEvent = service.events().update(calendarId, eventId, eventDetails1).execute();
		System.out.println("Meeting link removed from the event.");

		// Retrieve the updated event ID

		updatedEventId = updatedEvent.getId();

		System.out.println("Updated Event ID: " + updatedEventId);

		System.out.println(eventId);

		} else {

		System.out.println("No meeting link found for the event.");

		}

		 

		service.events().delete(calendarId, eventId).setSendNotifications(true)

		.execute();

		con=InvestorDatabaseUtill.getConnection();
		 psta=con.prepareStatement(MeetingShedularQueryConstant.DELETE_MEETINGSCHEDULE.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
	        psta.setString(1,meetingId);
	       psta.executeUpdate();
		 
  System.out.println("google meeting delete successfully");
		}
		catch (Exception e) {
			throw new  DaoException(e.getMessage());
		}
		finally {
			
			InvestorDatabaseUtill.close(psta, con);

		}
		 
	}

	@Override
	public MSTeamschedule saveOtherTypeOfMeeting(MSTeamschedule msts, String dataBaseName) throws DaoException {
		
		Connection con=null;
		PreparedStatement psta=null;
		try
		{
			  String inputDate = msts.getMeetingDate();
		        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		            Date date = inputFormat.parse(inputDate);
		            String startDateString = outputFormat.format(date);
		            System.out.println("Converted Date: " + startDateString);

		System.out.println("date"+startDateString);

		String startTimeString = msts.getStartTime();

		String endDateString =startDateString;

		String endTimeString = msts.getEndTime();

			    // Parse the start date and time

			    LocalDate startDate = LocalDate.parse(startDateString);

			    LocalTime startTime = LocalTime.parse(startTimeString);

			    ZoneId indiaZone = ZoneId.of("Asia/Kolkata");

			    ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDate, startTime, indiaZone);

			    OffsetDateTime startDateTime = startZonedDateTime.toOffsetDateTime();

			     

			    // Parse the end date and time

			    LocalDate endDate = LocalDate.parse(endDateString);

			    LocalTime endTime = LocalTime.parse(endTimeString);

			    ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDate, endTime, indiaZone);

			    OffsetDateTime endDateTime = endZonedDateTime.toOffsetDateTime();

			     

			    // Print the constructed OffsetDateTime objects

			    System.out.println("Start Date and Time: " + startDateTime);

			    System.out.println("End Date and Time: " + endDateTime);
			    
			      String scheduledDateTimeString =endDateTime.toString();
			      
			      // Parse the scheduled date and time string
			      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
			      OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);

			      // Get the current date and time
			      OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

			      // Check if the current date and time is after the scheduled meeting date and time
			      if (currentDateTime.isAfter(scheduledDateTime)) {
			          System.out.println("The meeting has been completed.");
			          msts.setStatus("Completed");
			          // Set the meeting status to completed
			          // (You can implement your own logic to update the meeting status in your application)
			      } else {
			          System.out.println("The meeting is yet to be completed.");
			          msts.setStatus("Schedule");
			      }
			    Date d=new Date();
			    con=InvestorDatabaseUtill.getConnection();
		psta=con.prepareStatement(MeetingShedularQueryConstant.INSERT_INTO_MEETINGSHUDULAR.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
	        psta.setString(1, null);
	        psta.setString(2, null);
	        psta.setString(3, null);
	        psta.setString(4, msts.getTitle());
	        psta.setString(5, msts.getAgenda());
	        psta.setString(6, msts.getParticipant().toString());
	        psta.setString(7, msts.getMeetingDate());
	        psta.setString(8, startDateTime.toString());
	        psta.setString(9, endDateTime.toString());
	        psta.setString(10, msts.getMeetingType());
	        psta.setBoolean(11,msts.isRecordAutomatically());
	        psta.setString(12, "No");
	        psta.setString(13, msts.getCreatedBy());
	        psta.setLong(14, d.getTime());
	        psta.setString(15, null);
	        psta.setLong(16,d.getTime());
	        psta.setString(17, msts.getClientName());
	        psta.setString(18, msts.getStatus());
	        psta.setString(19, msts.getFundGroup().toString());
	        psta.setString(20, msts.getRemark());
	        psta.executeUpdate();
	        return msts;
		}
		catch (Exception e) {
			throw new  DaoException(e.getMessage());
		}
		finally {
			
			InvestorDatabaseUtill.close(psta, con);

		}
		 
	}

	@Override
	public MSTeamschedule updateOtherTypeOfMeeting(long meetingSheduleId, MSTeamschedule teamschedule,
			String dataBaseName) throws DaoException {
               Connection con=null;
               PreparedStatement psta=null;
               try
               {
            	
            	   OffsetDateTime startDateTime=getstartDateTime(teamschedule.getMeetingDate(),teamschedule.getStartTime());
            	   
            	   OffsetDateTime endDateTime=getendDateTime(teamschedule.getMeetingDate(),teamschedule.getEndTime());

            	   String scheduledDateTimeString =endDateTime.toString();
            	      
            	      // Parse the scheduled date and time string
            	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmxxx");
            	      OffsetDateTime scheduledDateTime = OffsetDateTime.parse(scheduledDateTimeString, formatter);

            	      // Get the current date and time
            	      OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

            	      // Check if the current date and time is after the scheduled meeting date and time
            	      if (currentDateTime.isAfter(scheduledDateTime)) {
            	          System.out.println("The meeting has been completed.");
            	          teamschedule.setStatus("Completed");
            	          // Set the meeting status to completed
            	          // (You can implement your own logic to update the meeting status in your application)
            	      } else {
            	          System.out.println("The meeting is yet to be completed.");
            	          teamschedule.setStatus("Schedule");
            	      }
   			    Date d=new Date();
            	   con=InvestorDatabaseUtill.getConnection();
            	   psta=con.prepareStatement(MeetingShedularQueryConstant.UPDATE_OTHERTYPOFMEETING.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
            	   psta.setString(1, teamschedule.getTitle());
       	        psta.setString(2, teamschedule.getAgenda());
       	        psta.setString(3, teamschedule.getParticipant().toString());
       	        psta.setString(4, teamschedule.getMeetingDate());
       	        psta.setString(5, startDateTime.toString());
       	        psta.setString(6, endDateTime.toString());
       	        psta.setString(7, teamschedule.getMeetingType());
       	        psta.setBoolean(8,teamschedule.isRecordAutomatically());
       	        psta.setString(9, teamschedule.getModifiedBy());
       	        psta.setLong(10, d.getTime());
       	        psta.setString(11, teamschedule.getClientName());
       	        psta.setString(12, teamschedule.getStatus());
       	        psta.setString(13, teamschedule.getFundGroup().toString());
       	        psta.setString(14, teamschedule.getRemark());
       	        psta.setLong(15, meetingSheduleId);
       	        psta.executeUpdate();
       	        return teamschedule;
               }
               catch (Exception e) {
				// TODO: handle exception
	
               }
               finally {
       			
       			InvestorDatabaseUtill.close(psta, con);

       		}
			return null;
	}

	private OffsetDateTime getendDateTime(String meetingDate, String MetingendTime) throws ParseException {
		
		 String inputDate =meetingDate;
	        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
	        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
	            Date date = inputFormat.parse(inputDate);
	            String startDateString = outputFormat.format(date);
	            System.out.println("Converted Date: " + startDateString);

		String endDateString =startDateString;
   		String endTimeString =MetingendTime;
   		
   			    // Parse the end date and time
   			    LocalDate endDate = LocalDate.parse(endDateString);

   			    LocalTime endTime = LocalTime.parse(endTimeString);
   			    
   			    ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
   			    
   			    ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDate, endTime, indiaZone);

   			    OffsetDateTime endDateTime = endZonedDateTime.toOffsetDateTime();

   			    System.out.println("End Date and Time: " + endDateTime);
   			    return endDateTime;
	}

	private OffsetDateTime getstartDateTime(String meetingDate, String meetingStartTime) throws ParseException {
		  String inputDate =meetingDate;
		        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		            Date date = inputFormat.parse(inputDate);
		            String startDateString = outputFormat.format(date);
		            System.out.println("Converted Date: " + startDateString);

		System.out.println("date"+startDateString);

		String startTimeString =meetingStartTime;
		 // Parse the start date and time

		    LocalDate startDate = LocalDate.parse(startDateString);

		    LocalTime startTime = LocalTime.parse(startTimeString);

		    ZoneId indiaZone = ZoneId.of("Asia/Kolkata");

		    ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDate, startTime, indiaZone);

		    OffsetDateTime startDateTime = startZonedDateTime.toOffsetDateTime();
		    
		    System.out.println("Start Date and Time: " + startDateTime);
		    return startDateTime;
	}

	@Override
	public void deleteOtherTypeOFMeeting(long meetingSheduleId, String dataBaseName) throws DaoException {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement psta=null;
		try
		{
		con=InvestorDatabaseUtill.getConnection();
		 psta=con.prepareStatement(MeetingShedularQueryConstant.DELETE_OTHERMEEITNG.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER,dataBaseName));
	        psta.setLong(1,meetingSheduleId);
	       psta.executeUpdate();

		}
		catch (Exception e) {
			throw new  DaoException(e.getMessage());
		}
		finally {
			
			InvestorDatabaseUtill.close(psta, con);

		}
		 
	}

	@Override
	public ArrayList<ClientDetailsEntity> getAnalsytAdminByClientName(String clientName, String dataBaseName) throws DaoException {
		Connection con= null;
		PreparedStatement psta=null;
		ResultSet rs=null;
   ArrayList<ClientDetailsEntity> analystAdminClientAdminList=new ArrayList<>();
		try
		{
		  con=InvestorDatabaseUtill.getConnection();
		  psta=con.prepareStatement(MeetingShedularQueryConstant.SELECT_ANALYSTNAME_BYCLIENTNAME.replace(MeetingShedularQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		  psta.setString(1, clientName);
		  rs=psta.executeQuery();
		  while(rs.next())
		  {
			  ClientDetailsEntity entity=new ClientDetailsEntity();
				
				 String participantField = rs.getString("emailId");
				    participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
				    String[] participants = participantField.split(", ");
				    ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));
				    entity.setEmailId(participantList);
				    
			  String assignAA = rs.getString("assignAA");
			    assignAA = assignAA.substring(1, assignAA.length() - 1); // Remove the square brackets
				    String[] assignAAs = assignAA.split(", ");
				    ArrayList<String> assignAAlist = new ArrayList<>(Arrays.asList(assignAAs));
				    entity.setAssignAA(assignAAlist);
				    analystAdminClientAdminList.add(entity);
		  }
		  return analystAdminClientAdminList;
				  
	}
		catch (Exception e) {
			throw new  DaoException(e.getMessage());
		}
		finally {
			
			InvestorDatabaseUtill.close(psta, con);

		}
	
	}
}



