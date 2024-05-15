package com.Anemoi.InvestorRelation.ShareHolderMeeting;

public class ShareHolderMeetingQuaryContant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String HOLDERID = "shareholderid";

	public static final String DATE = "date";
	
	public static final String MEETINGID="meetingId";

	public static final String STARTTIME = "startTime";

	public static final String ENDTIME = "endTime";

	public static final String ORGANISATION = "organisation";

	public static final String STAKEHOLDERTYPE = "stakeholderType";

	public static final String MEETINGTYPE = "meetingType";

	public static final String SUBJECT = "subject";

	public static final String BROKER = "broker";

	public static final String LOCATION = "location";

	public static final String STATUS = "status";

	public static final String COMMENTS = "comments";

	public static final String PARTICIPANTS = "participants";

	public static final String FEEDBACK = "feedback";
	
	public static final String SUMMARY = "summary";
	
	public static final String ACTIONITEM="actionItem";
	
	public static final String INVESTORCONCERNS="investorConcerns";
	
	public static final String ANALYSIS="analysis";
	
	public static final String INSERT_INTO_SHAREHOLDERMEETING = "INSERT INTO #$DataBaseName#$.dbo.shareholdermeeting values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String SELECT__SHAREHOLDERMEETING_BY_ID = "SELECT * FROM #$DataBaseName#$.dbo.shareholdermeeting where shareholderid=?";

	public static final String SELECT_SHAREHOLDERMEETING = "SELECT *FROM #$DataBaseName#$.dbo.shareholdermeeting ORDER BY (uploadedDate) DESC ,RAND()";

//	public static final String SELECT_SHAREHOLDERMEETING = "  SELECT *,FORMAT(DATEADD(SECOND, DATEDIFF(SECOND, '1970-01-01', CONVERT(DATETIME, [date], 103)), '19700101'), 'yyyy-MM-dd') AS [dateFormatted],FORMAT(DATEADD(SECOND, [uploadedDate] / 1000, '19700101'), 'yyyy-MM-dd') AS [uploadedDateFormatted] FROM #$DataBaseName#$.dbo.shareholdermeeting ORDER BY (uploadedDate) DESC ,RAND()";

	public static final String UPDATE_SHAREHOLDERMEETING = "UPDATE #$DataBaseName#$.dbo.shareholdermeeting SET date=?, startTime=?, endTime=?,organisation=?,stakeholderType=?,meetingType=? ,subject=?,broker=?,location=?,status=?,comments=?,participants=?,feedback=?,uploadedDate=?,uploadedBy=?, clientName=? WHERE shareholderid=?";

	public static final String DELETE_SHAREHOLDERMEETING_BY_ID = "DELETE #$DataBaseName#$.dbo.shareholdermeeting WHERE shareholderid=?";

	public static final String UPDATE_MEETINGDATASTATUS= "UPDATE #$DataBaseName#$.dbo.meetingshedulartable SET meetingDataStatus=? WHERE meetingId=?";


}
