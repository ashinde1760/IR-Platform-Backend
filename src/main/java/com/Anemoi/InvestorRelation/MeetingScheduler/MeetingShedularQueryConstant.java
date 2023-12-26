package com.Anemoi.InvestorRelation.MeetingScheduler;

public class MeetingShedularQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INSERT_INTO_MEETINGSHUDULAR = "INSERT INTO INV_RELATIONS.dev.meetingshedulartable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_SHEDULEMEETINGDETAILS = "SELECT * FROM INV_RELATIONS.dev.meetingshedulartable ORDER BY (meetingDate) ASC ,RAND() ";

//	public static final String SELECT_SHEDULEMEETINGDETAILS = "  SELECT *, FORMAT(DATEADD(SECOND, DATEDIFF(SECOND, '1970-01-01', CONVERT(DATETIME, [meetingDate], 103)), '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM INV_RELATIONS.dev.meetingshedulartable ORDER BY [meetingDate] ASC, RAND()";

	
	public static final String SELECT_SHEDULEMEETINGDETAILS_BYID = "SELECT * FROM INV_RELATIONS.dev.meetingshedulartable where meetingSheduleId=?";

	
	public static final String UPDATE_MEETINGSCHEDULE= "UPDATE INV_RELATIONS.dev.meetingshedulartable SET title=?,agenda=?,participant=?,meetingDate=?,startTime=?,endTime=?,meetingType=?,recordAutomatically=?,modifiedBy=?,modifiedOn=?,clientName=?,status=?,fundGroup=?,remark=? WHERE meetingSheduleId=?";

	public static final String UPDATE_GOOGLEMEETING= "UPDATE INV_RELATIONS.dev.meetingshedulartable SET joinUrl=?,title=?,agenda=?,participant=?,meetingDate=?,startTime=?,endTime=?,meetingType=?,recordAutomatically=? ,modifiedBy=?,modifiedOn=?,clientName=?,status=?,fundGroup=?,remark=? WHERE meetingSheduleId=?";

	public static final String DELETE_MEETINGSCHEDULE= "DELETE INV_RELATIONS.dev.meetingshedulartable WHERE meetingId=?";

	public static final String SELECT_MEETINGID_EVENTID = "SELECT meetingId,eventId,meetingType FROM INV_RELATIONS.dev.meetingshedulartable where meetingSheduleId=? ";

	public static final String UPDATE_OTHERTYPOFMEETING= "UPDATE INV_RELATIONS.dev.meetingshedulartable SET title=?,agenda=?,participant=?,meetingDate=?,startTime=?,endTime=?,meetingType=?,recordAutomatically=? ,modifiedBy=?,modifiedOn=?,clientName=?,status=?,fundGroup=?,remark=? WHERE meetingSheduleId=?";

	public static final String DELETE_OTHERMEEITNG= "DELETE INV_RELATIONS.dev.meetingshedulartable WHERE meetingSheduleId=?";

	public static final String UPDATE_MEETINGSTATUS="UPDATE INV_RELATIONS.dev.meetingshedulartable SET status = 'Completed' WHERE endTime <= ? AND status != 'Completed'";

	public static final String SELECT_ANALYSTNAME_BYCLIENTNAME = "SELECT emailId,assignAA FROM INV_RELATIONS.dev.clientDetails where clientName=? ";

}
