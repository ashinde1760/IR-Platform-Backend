package com.Anemoi.InvestorRelation.MeetingScheduler;

public class MeetingShedularQueryConstant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String INSERT_INTO_MEETINGSHUDULAR = "INSERT INTO #$DataBaseName#$.dev.meetingshedulartable values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_SHEDULEMEETINGDETAILS = "SELECT * FROM #$DataBaseName#$.dev.meetingshedulartable ORDER BY (meetingDate) ASC ,RAND() ";

//	public static final String SELECT_SHEDULEMEETINGDETAILS = "  SELECT *, FORMAT(DATEADD(SECOND, DATEDIFF(SECOND, '1970-01-01', CONVERT(DATETIME, [meetingDate], 103)), '19700101'), 'yyyy-MM-dd') AS [createdOnFormatted] FROM #$DataBaseName#$.dev.meetingshedulartable ORDER BY [meetingDate] ASC, RAND()";

	
	public static final String SELECT_SHEDULEMEETINGDETAILS_BYID = "SELECT * FROM #$DataBaseName#$.dev.meetingshedulartable where meetingSheduleId=?";

	
	public static final String UPDATE_MEETINGSCHEDULE= "UPDATE #$DataBaseName#$.dev.meetingshedulartable SET title=?,agenda=?,participant=?,meetingDate=?,startTime=?,endTime=?,meetingType=?,recordAutomatically=?,modifiedBy=?,modifiedOn=?,clientName=?,status=?,fundGroup=?,remark=? WHERE meetingSheduleId=?";

	public static final String UPDATE_GOOGLEMEETING= "UPDATE #$DataBaseName#$.dev.meetingshedulartable SET joinUrl=?,title=?,agenda=?,participant=?,meetingDate=?,startTime=?,endTime=?,meetingType=?,recordAutomatically=? ,modifiedBy=?,modifiedOn=?,clientName=?,status=?,fundGroup=?,remark=? WHERE meetingSheduleId=?";

	public static final String DELETE_MEETINGSCHEDULE= "DELETE #$DataBaseName#$.dev.meetingshedulartable WHERE meetingId=?";

	public static final String SELECT_MEETINGID_EVENTID = "SELECT meetingId,eventId,meetingType FROM #$DataBaseName#$.dev.meetingshedulartable where meetingSheduleId=? ";

	public static final String UPDATE_OTHERTYPOFMEETING= "UPDATE #$DataBaseName#$.dev.meetingshedulartable SET title=?,agenda=?,participant=?,meetingDate=?,startTime=?,endTime=?,meetingType=?,recordAutomatically=? ,modifiedBy=?,modifiedOn=?,clientName=?,status=?,fundGroup=?,remark=? WHERE meetingSheduleId=?";

	public static final String DELETE_OTHERMEEITNG= "DELETE #$DataBaseName#$.dev.meetingshedulartable WHERE meetingSheduleId=?";

	public static final String UPDATE_MEETINGSTATUS="UPDATE #$DataBaseName#$.dev.meetingshedulartable SET status = 'Completed' WHERE endTime <= ? AND status != 'Completed'";

	public static final String SELECT_ANALYSTNAME_BYCLIENTNAME = "SELECT emailId,assignAA FROM #$DataBaseName#$.dev.clientDetails where clientName=? ";

}
