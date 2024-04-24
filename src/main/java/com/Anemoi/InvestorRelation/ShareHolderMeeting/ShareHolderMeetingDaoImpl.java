package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import io.micronaut.http.multipart.CompletedFileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Singleton
public class ShareHolderMeetingDaoImpl implements ShareHolderMeetingDao {

	@Inject
	MediaServiceInterface inter;

	@Inject 
	MediaService ser;
	private static final Logger LOGGER = LoggerFactory.getLogger(ShareHolderMeetingDaoImpl.class);

	

	@SuppressWarnings("resource")
	@Override
	public ShareHolderMeetingEntity createNewShareHolderMeeting(ShareHolderMeetingEntity shareholdermeeting,CompletedFileUpload momfile,
			String dataBaseName) throws ShareHolderMeetingDaoException {
		System.out.println("shareholdermeeting:=====> "+shareholdermeeting.getClientName());
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderMeetingQuaryContant.INSERT_INTO_SHAREHOLDERMEETING
					.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Date d = new Date();
			if(shareholdermeeting.getMediakey().equalsIgnoreCase("null"))
			{
				String id=UUID.randomUUID().toString();
				shareholdermeeting.setShareholderid(id);
			}else {
				shareholdermeeting.setShareholderid(shareholdermeeting.getMediakey());
			}
			shareholdermeeting.setUploadedDate(d.getTime());
			pstmt.setString(1, shareholdermeeting.getShareholderid());
			pstmt.setString(2, shareholdermeeting.getMeetingId());
			pstmt.setString(3, shareholdermeeting.getDate());
			pstmt.setString(4, shareholdermeeting.getStartTime());
			pstmt.setString(5, shareholdermeeting.getEndTime());
			pstmt.setString(6, shareholdermeeting.getOrganisation());
			pstmt.setString(7, shareholdermeeting.getStakeholderType());
			pstmt.setString(8, shareholdermeeting.getMeetingType());
			pstmt.setString(9, shareholdermeeting.getSubject());
			pstmt.setString(10, shareholdermeeting.getBroker());
			pstmt.setString(11, shareholdermeeting.getLocation());
			pstmt.setString(12, shareholdermeeting.getStatus());
			pstmt.setString(13, shareholdermeeting.getComments());
			pstmt.setString(14, shareholdermeeting.getParticipants());
			pstmt.setString(15, shareholdermeeting.getFeedback());
			pstmt.setString(16, shareholdermeeting.getSummary());
			pstmt.setString(17, shareholdermeeting.getActionItem());
			pstmt.setString(18, shareholdermeeting.getInvestorConcerns());
			pstmt.setString(19, shareholdermeeting.getAnalysis());
			pstmt.setLong(20, shareholdermeeting.getUploadedDate());
			pstmt.setString(21, shareholdermeeting.getUploadedBy());
			pstmt.setString(22, shareholdermeeting.getMediakey());
			pstmt.setString(23, momfile.getFilename());
			pstmt.setString(24, momfile.getContentType().toString());
			pstmt.setBytes(25, momfile.getBytes());
			pstmt.setString(27, shareholdermeeting.getClientName());
			pstmt.setString(28, shareholdermeeting.getFundGroup());
		
			if( momfile.getFilename().isEmpty())
			{
				pstmt.setString(26, "No");
				
			}
			else
			{
				pstmt.setString(26, "Yes");
			}
			pstmt.setString(27, shareholdermeeting.getClientName());
			pstmt.executeUpdate();
			pstmt = connection.prepareStatement(ShareHolderMeetingQuaryContant.UPDATE_MEETINGDATASTATUS
					.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, "yes");
			pstmt.setString(2, shareholdermeeting.getMeetingId());
			pstmt.executeUpdate();
			return shareholdermeeting;
			
		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new ShareHolderMeetingDaoException("unable to create share holder" + e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ShareHolderMeetingEntity getShareHolderMeetingById(String shareholderid, String dataBaseName)
			throws ShareHolderMeetingDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderMeetingQuaryContant.SELECT__SHAREHOLDERMEETING_BY_ID
					.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, shareholderid);
			result = pstmt.executeQuery();
			while (result.next()) {
				ShareHolderMeetingEntity entity=buildshareholdermeetingDeatils(result);
				return entity;
			}
		} catch (Exception e) {
			LOGGER.error("share holder meeting not found" + e.getMessage());
			throw new ShareHolderMeetingDaoException("unable to get share holder " + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

//	private String convertAudioToText(byte[] audioFile) throws IOException {
//		 try (SpeechClient speechClient = SpeechClient.create()) {
//
//		        // Create the audio input
//		        RecognitionAudio audio = RecognitionAudio.newBuilder()
//		                .setContent(ByteString.copyFrom(audioFile))
//		                .build();
//		        // Configure the speech recognition request
//		        RecognitionConfig config = RecognitionConfig.newBuilder()
//		                .setEncoding(AudioEncoding.LINEAR16)
//		                .setSampleRateHertz(16000)
//		                .setLanguageCode("en-US")
//		                .build();
//		        List<SpeechRecognitionResult> response = speechClient.recognize(config, audio).getResultsList();
//
//		        // Process the transcription results
//		        if (!response.isEmpty()) {
//		            SpeechRecognitionResult result = response.get(0);
//		            if (!result.getAlternativesList().isEmpty()) {
//		                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//		                return alternative.getTranscript();
//		            }
//		        }
//
//		        throw new RuntimeException("Unable to convert audio to text");
//		    }
//
//	}
	private ShareHolderMeetingEntity buildshareholdermeetingDeatils(ResultSet result) throws SQLException, IOException {

		ShareHolderMeetingEntity shareholdermeetingEntity = new ShareHolderMeetingEntity();
		shareholdermeetingEntity.setShareholderid(result.getString(ShareHolderMeetingQuaryContant.HOLDERID));
		shareholdermeetingEntity.setMeetingId(result.getString(ShareHolderMeetingQuaryContant.MEETINGID));
		shareholdermeetingEntity.setDate(result.getString(ShareHolderMeetingQuaryContant.DATE));
		shareholdermeetingEntity.setStartTime(result.getString(ShareHolderMeetingQuaryContant.STARTTIME));
		shareholdermeetingEntity.setEndTime(result.getString(ShareHolderMeetingQuaryContant.ENDTIME));
		shareholdermeetingEntity.setOrganisation(result.getString(ShareHolderMeetingQuaryContant.ORGANISATION));
		shareholdermeetingEntity.setStakeholderType(result.getString(ShareHolderMeetingQuaryContant.STAKEHOLDERTYPE));
		shareholdermeetingEntity.setMeetingType(result.getString(ShareHolderMeetingQuaryContant.MEETINGTYPE));
		shareholdermeetingEntity.setSubject(result.getString(ShareHolderMeetingQuaryContant.SUBJECT));
		shareholdermeetingEntity.setBroker(result.getString(ShareHolderMeetingQuaryContant.BROKER));
		shareholdermeetingEntity.setLocation(result.getString(ShareHolderMeetingQuaryContant.LOCATION));
		shareholdermeetingEntity.setStatus(result.getString(ShareHolderMeetingQuaryContant.STATUS));
		shareholdermeetingEntity.setComments(result.getString(ShareHolderMeetingQuaryContant.COMMENTS));
		shareholdermeetingEntity.setParticipants(result.getString(ShareHolderMeetingQuaryContant.PARTICIPANTS));
		shareholdermeetingEntity.setFeedback(result.getString(ShareHolderMeetingQuaryContant.FEEDBACK));
		shareholdermeetingEntity.setSummary(result.getString(ShareHolderMeetingQuaryContant.SUMMARY));
		shareholdermeetingEntity.setActionItem(result.getString(ShareHolderMeetingQuaryContant.ACTIONITEM));
		shareholdermeetingEntity.setInvestorConcerns(result.getString(ShareHolderMeetingQuaryContant.INVESTORCONCERNS));
		shareholdermeetingEntity.setAnalysis(result.getString(ShareHolderMeetingQuaryContant.ANALYSIS));
		shareholdermeetingEntity.setUploadedDate(result.getLong("uploadedDate"));
		shareholdermeetingEntity.setUploadedBy(result.getString("uploadedBy"));
		shareholdermeetingEntity.setMediakey(result.getString("mediaKey"));
		String mediaKey=result.getString("mediaKey");
		File f=	ser.getMediaFileByKey(mediaKey);
		if(f==null)
		{
			  shareholdermeetingEntity.setAudioVedioFileStatus("Not Present");
		}else {
		   String fileExtension = getFileExtension(f);
		   if (isAudioFile(fileExtension)) {
	            shareholdermeetingEntity.setAudioVedioFileStatus("Audio");
	        } else if (isVideoFile(fileExtension)) {
	            shareholdermeetingEntity.setAudioVedioFileStatus("Video");
	        } 
		}
		shareholdermeetingEntity.setMomfileName(result.getString("momfileName"));
		shareholdermeetingEntity.setMomStatus(result.getString("momStatus"));
		shareholdermeetingEntity.setClientName(result.getString("clientName"));
		shareholdermeetingEntity.setFundGroup(result.getString("fundGroup"));

		return shareholdermeetingEntity;
	}

	private boolean isVideoFile(String fileExtension) {
	    return fileExtension.equalsIgnoreCase("mp4") ||
	               fileExtension.equalsIgnoreCase("avi") ||
	               fileExtension.equalsIgnoreCase("mov");
	}

	private boolean isAudioFile(String fileExtension) {
		return fileExtension.equalsIgnoreCase("mp3") ||
	               fileExtension.equalsIgnoreCase("wav") ||
	               fileExtension.equalsIgnoreCase("aac");
	}

	private String getFileExtension(File f) {
		 String fileName = f.getName();
	        int dotIndex = fileName.lastIndexOf('.');
	        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
	            return fileName.substring(dotIndex + 1);
	        }
	        return "";
}

	@Override
	public List<ShareHolderMeetingEntity> getAllShareHolderMeetingDetails(String dataBaseName)
			throws SQLException, ShareHolderMeetingDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<ShareHolderMeetingEntity> listofshareholdermeetingDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderMeetingQuaryContant.SELECT_SHAREHOLDERMEETING
					.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				ShareHolderMeetingEntity sharemeeting = buildshareholdermeetingDeatils(result);
				listofshareholdermeetingDetails.add(sharemeeting);
			}
			return listofshareholdermeetingDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of share holder meeting" + e.getMessage());
			e.printStackTrace();
			throw new ShareHolderMeetingDaoException("unable to get share holder list" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public ShareHolderMeetingEntity updateShareHolderMeetingDetails(ShareHolderMeetingEntity shareholdermeeting,
			String shareholderid, String dataBaseName)  throws ShareHolderMeetingDaoException{
		Connection connection = null;
		PreparedStatement pstmt = null;
//		LOGGER.info(".in update shareholder meeting database name is ::" + dataBaseName + " holderId is ::" + shareholderid
//				+ " request shareholder meeting is ::" + shareholdermeeting);

		try {
		
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderMeetingQuaryContant.UPDATE_SHAREHOLDERMEETING
					.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Date d = new Date();
			shareholdermeeting.setUploadedDate( d.getTime());
			pstmt.setString(1, shareholdermeeting.getDate());
			pstmt.setString(2, shareholdermeeting.getStartTime());
			pstmt.setString(3, shareholdermeeting.getEndTime());
			pstmt.setString(4, shareholdermeeting.getOrganisation());
			pstmt.setString(5, shareholdermeeting.getStakeholderType());
			pstmt.setString(6, shareholdermeeting.getMeetingType());
			pstmt.setString(7, shareholdermeeting.getSubject());
			pstmt.setString(8, shareholdermeeting.getBroker());
			pstmt.setString(9, shareholdermeeting.getLocation());
			pstmt.setString(10, shareholdermeeting.getStatus());
			pstmt.setString(11, shareholdermeeting.getComments());
			pstmt.setString(12, shareholdermeeting.getParticipants());
			pstmt.setString(13, shareholdermeeting.getFeedback());
			pstmt.setLong(14, shareholdermeeting.getUploadedDate());
			pstmt.setString(15, shareholdermeeting.getUploadedBy());
			pstmt.setString(16, shareholdermeeting.getClientName());
			pstmt.setString(17, shareholderid);
			int executeUpdate = pstmt.executeUpdate();

			LOGGER.info(executeUpdate + " shareholder meeting updated successfully");
			return shareholdermeeting;
			
		} catch (Exception e) {
			throw new ShareHolderMeetingDaoException("unable to update"+e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		
	

	}

	@Override
	public String deleteShareHolderMeeting(String shareholderid, String dataBaseName) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
		String mediakey=	inter.deleteMediaFile(shareholderid);
			pstmt = connection.prepareStatement(ShareHolderMeetingQuaryContant.DELETE_SHAREHOLDERMEETING_BY_ID
					.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, shareholderid);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " shareholder meeting deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject extractmomfile(String meetingId, CompletedFileUpload momfile, String dataBaseName)
			throws ShareHolderMeetingDaoException, IOException {
		byte[] docxFileContent = momfile.getBytes();

	try {
		InputStream inputStream = new ByteArrayInputStream(docxFileContent);

		@SuppressWarnings("resource")

		XWPFDocument document = new XWPFDocument(inputStream);

		StringBuilder contentBuilder = new StringBuilder();

		for (XWPFParagraph paragraph : document.getParagraphs()) {

		String text = paragraph.getText();

		contentBuilder.append(text).append(System.lineSeparator());

		}

		String text = contentBuilder.toString();

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("date", extractValueBetween("Date*:", "Time*:", text));

		jsonObject.put("time", extractValueBetween("Time*:", "Title*:", text));

		jsonObject.put("title", extractValueBetween("Title*:", "Location*:", text));

		jsonObject.put("location", extractValueBetween("Location*:", "Minutes Drafted Date:", text));

		jsonObject.put("organizer", extractValueBetween("Organizer:", "Minutes Drafted Date:", text));

		jsonObject.put("minutesDraftedDate:", extractValueBetween("Minutes Drafted Date:", "Attendees*", text));

		List<String> attendeesList = extractArrayValue(extractValueBetween("Attendees*", "Meeting Agenda", text));

		jsonObject.put("attendees", attendeesList);

		jsonObject.put("meetingAgenda", extractArrayValue(extractValueBetween("Meeting Agenda", "Summary of the Discussion", text)));

		jsonObject.put("summaryoftheDiscussion", extractArrayValue(extractValueBetween("Summary of the Discussion", "Action Items", text)));

		jsonObject.put("actionItems", extractArrayValue(extractValueBetween("Action Items", "Investor Concerns", text)));

		jsonObject.put("investorConcerns", extractArrayValue(extractValueBetween("Investor Concerns", "Feedback", text)));

		jsonObject.put("feedback", extractArrayValue(extractValueBetween("Feedback", "Analysis", text)));

		jsonObject.put("analysis", extractArrayValue(extractValueAfter("Analysis", text)));

		System.out.println(jsonObject.toString());
		return jsonObject;

		} catch (IOException e) {

		e.printStackTrace();

		throw e;

		}

		}

		 

		private static String extractValueBetween(String startHeading, String endHeading, String text) {

		int startIndex = text.indexOf(startHeading);

		int endIndex = text.indexOf(endHeading);

		if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {

		return text.substring(startIndex + startHeading.length(), endIndex).trim();

		}

		return "";

		}

		 

		private static String extractValueAfter(String heading, String text) {

		int startIndex = text.indexOf(heading);

		if (startIndex != -1) {

		return text.substring(startIndex + heading.length()).trim();

		}

		return "";

		}

		private static List<String> extractArrayValue(String value) {

		// Split the value by line separator and remove empty lines

		String[] values = value.split("\\r?\\n");

		List<String> listValues = new ArrayList<>(Arrays.asList(values));

		// Remove empty and whitespace-only lines

//		listValues.removeIf(String::isBlank);

	
		return listValues;
	}

		@Override
		public ShareHolderMeetingEntity getPreviewForMomfile(String shareholderid, String dataBaseName)
				throws ShareHolderMeetingDaoException {
		       Connection con=null;
		       PreparedStatement psta=null;
		       ResultSet rs=null;
		       try
		       {
		    	   con=InvestorDatabaseUtill.getConnection();
		    	   psta=con.prepareStatement(ShareHolderMeetingQuaryContant.SELECT__SHAREHOLDERMEETING_BY_ID.replace(ShareHolderMeetingQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		    	   psta.setString(1, shareholderid);
		    	   rs=psta.executeQuery();
		    	   while(rs.next())
		    	   {
		    		   ShareHolderMeetingEntity  entity=new ShareHolderMeetingEntity();
		    		   entity.setMomfileName(rs.getString("momfileName"));
		    		   entity.setMomFileType(rs.getString("momFileType"));
		    		   entity.setMomFileData(rs.getBytes("momFileData"));
		    		   return entity;
		    	   }
		       }
		       catch (Exception e) {
				throw new ShareHolderMeetingDaoException("unable to get preview for mom file"+e.getMessage());
			}
		       finally {
					LOGGER.info("closing the connections");
					InvestorDatabaseUtill.close(psta, con);
				}
			return null;
		}



}
