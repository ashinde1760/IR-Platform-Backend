package com.Anemoi.InvestorRelation.MeetingScheduler;
 
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
 
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
 
public interface MeetingDao {
 
	MSTeamschedule saveTeamMeetingSchedule(MSTeamschedule msts, String dataBaseName) throws SQLException, ClassNotFoundException,DaoException;
 
	ArrayList<MSTeamschedule> getmeetingShecdulList(String dataBaseName) throws DaoException;
 
	MSTeamschedule saveGoogleMeetingSchedule(MSTeamschedule msts, String dataBaseName) throws DaoException, GeneralSecurityException, IOException;
 
	MSTeamschedule updateTeamsmeetingSchedule(long meetingSheduleId, MSTeamschedule teamschedule,String dataBaseName) throws DaoException;
 
	void deleteScheduleMeetingdetails(String meetingId,String eventId, String dataBaseName) throws DaoException;
 
	MSTeamschedule updategoogleMeetingSchedule(long meetingSheduleId, MSTeamschedule teamschedule, String dataBaseName) throws DaoException;
 
	MSTeamschedule getMeedingDataById(long meetingSheduleId, String dataBaseName) throws DaoException;
 
	void deletegoogleMeetings(String meetingId, String eventId, String dataBaseName) throws DaoException;
 
	MSTeamschedule saveOtherTypeOfMeeting(MSTeamschedule msts, String dataBaseName) throws DaoException;
 
	MSTeamschedule updateOtherTypeOfMeeting(long meetingSheduleId, MSTeamschedule teamschedule, String dataBaseName) throws DaoException;
 
 
	void deleteOtherTypeOFMeeting(long meetingSheduleId, String dataBaseName) throws DaoException;
 
	ArrayList<ClientDetailsEntity> getAnalsytAdminByClientName(String clientName, String dataBaseName) throws DaoException;
 
}