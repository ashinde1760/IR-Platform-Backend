package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromEntity;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromQuaryConstant;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromServiceImpl;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ShareHolderMeetingServiceImpl implements ShareHolderMeetingService {

	
	
	@Inject
	private ShareHolderMeetingDao sharemeetingdao;
	
	@Inject
	private AuditHistoryService auditHistoryService;


	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";


	private static String DATABASENAME = "databaseName";

	private static String dataBaseName() {

		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ShareHolderMeetingEntity createShareHolderMeeting(ShareHolderMeetingEntity sharehodermeeting,CompletedFileUpload momfile)
			throws SQLException, ShareHolderMeetingServiceException {
		try {
		
			String dataBaseName = ShareHolderMeetingServiceImpl.dataBaseName();
			ShareHolderMeetingEntity createNewShareHolderMeeting = this.sharemeetingdao
					.createNewShareHolderMeeting(sharehodermeeting,momfile, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(sharehodermeeting.getUploadedBy());
			entity.setActivity("Add Meeting data");
			entity.setDescription("User added meeting data in application");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			
		return	createNewShareHolderMeeting;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderMeetingServiceException("unable to create share holder " + e.getMessage());
		}
	
	}


	private void applyValidation(ShareHolderMeetingEntity sharehodermeeting) throws Exception {
		// TODO Auto-generated method stub
		if (sharehodermeeting.getSubject() == null || sharehodermeeting.getSubject().isEmpty()) {
			throw new Exception("subject should not be null or empty");
		}
		if (sharehodermeeting.getOrganisation() == null || sharehodermeeting.getOrganisation().isEmpty()) {
			throw new Exception("organisation should not be null or empty");
		}
		if (sharehodermeeting.getStakeholderType() == null || sharehodermeeting.getStakeholderType().isEmpty()) {
			throw new Exception("StakeHolder type should not be null or empty");
		}
		if (sharehodermeeting.getMeetingType() == null || sharehodermeeting.getMeetingType().isEmpty()) {
			throw new Exception("meeting type should not be null or empty");
		}
		if (sharehodermeeting.getBroker() == null || sharehodermeeting.getBroker().isEmpty()) {
			throw new Exception("Broker should not be null or empty");
		}
		if (sharehodermeeting.getLocation() == null || sharehodermeeting.getLocation().isEmpty()) {
			throw new Exception("Location should not be null or empty");
		}
		if (sharehodermeeting.getStatus() == null || sharehodermeeting.getStatus().isEmpty()) {
			throw new Exception("status should not be null or empty");
		}

	}

	@Override
	public ShareHolderMeetingEntity getShareHolderMeetingById(String shareholderid)
			throws SQLException, ShareHolderMeetingServiceException {
		try {
			String dataBaseName = ShareHolderMeetingServiceImpl.dataBaseName();
			if (shareholderid == null || shareholderid.isEmpty()) {
				System.out.print("ShareHolderMeeting id must not be null or empty");
			}
			ShareHolderMeetingEntity sharemeetingEntity = this.sharemeetingdao.getShareHolderMeetingById(shareholderid,
					dataBaseName);
			return sharemeetingEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderMeetingServiceException( e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareHolderMeetingEntity> getShareHolderMeetingDetails()
			throws SQLException, ShareHolderMeetingServiceException {
		try {
			String dataBaseName = ShareHolderMeetingServiceImpl.dataBaseName();
			List<ShareHolderMeetingEntity> sharehodermeetingEntity = this.sharemeetingdao
					.getAllShareHolderMeetingDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray shareholdermeetingJsonData = getJSONFromShareHolderMeetingList(sharehodermeetingEntity);
			object.put(sharehodermeetingEntity, shareholdermeetingJsonData);
			return sharehodermeetingEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderMeetingServiceException("unable to get share holder list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromShareHolderMeetingList(List<ShareHolderMeetingEntity> shareholdermeetingEntity) {
		JSONArray array = new JSONArray();
		for (ShareHolderMeetingEntity shareholder : shareholdermeetingEntity) {
			JSONObject object = buildJsonFromShareHolderMeetingForm(shareholder);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromShareHolderMeetingForm(ShareHolderMeetingEntity shareholdermeeting) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ShareHolderMeetingQuaryContant.HOLDERID, shareholdermeeting.getShareholderid());
		jsonObject.put(ShareHolderMeetingQuaryContant.DATE, shareholdermeeting.getDate());
		jsonObject.put(ShareHolderMeetingQuaryContant.STARTTIME, shareholdermeeting.getStartTime());
		jsonObject.put(ShareHolderMeetingQuaryContant.ENDTIME, shareholdermeeting.getEndTime());
		jsonObject.put(ShareHolderMeetingQuaryContant.ORGANISATION, shareholdermeeting.getOrganisation());
		jsonObject.put(ShareHolderMeetingQuaryContant.STAKEHOLDERTYPE, shareholdermeeting.getStakeholderType());
		jsonObject.put(ShareHolderMeetingQuaryContant.MEETINGTYPE, shareholdermeeting.getMeetingType());
		jsonObject.put(ShareHolderMeetingQuaryContant.SUBJECT, shareholdermeeting.getSubject());
		jsonObject.put(ShareHolderMeetingQuaryContant.BROKER, shareholdermeeting.getBroker());
		jsonObject.put(ShareHolderMeetingQuaryContant.LOCATION, shareholdermeeting.getLocation());
		jsonObject.put(ShareHolderMeetingQuaryContant.STATUS, shareholdermeeting.getStatus());
		jsonObject.put(ShareHolderMeetingQuaryContant.COMMENTS, shareholdermeeting.getComments());
		jsonObject.put(ShareHolderMeetingQuaryContant.PARTICIPANTS, shareholdermeeting.getParticipants());
		jsonObject.put(ShareHolderMeetingQuaryContant.FEEDBACK, shareholdermeeting.getFeedback());
		

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShareHolderMeetingEntity updateShareHolderMeeting(ShareHolderMeetingEntity sharehodermeeting,
			String shareholderid) throws ShareHolderMeetingServiceException {
		try {
			String dataBaseName = ShareHolderMeetingServiceImpl.dataBaseName();
			if (shareholderid == null || shareholderid.isEmpty()) {
				System.out.println("share holder meeting id can't be null or empty ");

			}
			ShareHolderMeetingEntity updatedshareholdermeeting = this.sharemeetingdao
					.updateShareHolderMeetingDetails(sharehodermeeting, shareholderid, dataBaseName);
			JSONObject object = new JSONObject();
			JSONObject jsonFromUser = buildJsonFromUpdatedShareHolderMeetingDataForm(updatedshareholdermeeting);
			object.put(updatedshareholdermeeting, jsonFromUser);
			return updatedshareholdermeeting;

		} catch (Exception e) {
			throw new ShareHolderMeetingServiceException(e.getMessage());
		}
		
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromUpdatedShareHolderMeetingDataForm(
			ShareHolderMeetingEntity updatedshareholdermeeting) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ShareHolderMeetingQuaryContant.HOLDERID, updatedshareholdermeeting.getShareholderid());
		jsonObject.put(ShareHolderMeetingQuaryContant.DATE, updatedshareholdermeeting.getDate());
		jsonObject.put(ShareHolderMeetingQuaryContant.STARTTIME, updatedshareholdermeeting.getStartTime());
		jsonObject.put(ShareHolderMeetingQuaryContant.ENDTIME, updatedshareholdermeeting.getEndTime());
		jsonObject.put(ShareHolderMeetingQuaryContant.ORGANISATION, updatedshareholdermeeting.getOrganisation());

		jsonObject.put(ShareHolderMeetingQuaryContant.STAKEHOLDERTYPE, updatedshareholdermeeting.getStakeholderType());

		jsonObject.put(ShareHolderMeetingQuaryContant.MEETINGTYPE, updatedshareholdermeeting.getMeetingType());

		jsonObject.put(ShareHolderMeetingQuaryContant.SUBJECT, updatedshareholdermeeting.getSubject());

		jsonObject.put(ShareHolderMeetingQuaryContant.BROKER, updatedshareholdermeeting.getBroker());
		jsonObject.put(ShareHolderMeetingQuaryContant.LOCATION, updatedshareholdermeeting.getLocation());
		jsonObject.put(ShareHolderMeetingQuaryContant.STATUS, updatedshareholdermeeting.getStatus());
		jsonObject.put(ShareHolderMeetingQuaryContant.COMMENTS, updatedshareholdermeeting.getComments());
		jsonObject.put(ShareHolderMeetingQuaryContant.PARTICIPANTS, updatedshareholdermeeting.getParticipants());
		jsonObject.put(ShareHolderMeetingQuaryContant.FEEDBACK, updatedshareholdermeeting.getFeedback());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteShareHoderMeeting(String shareholderid) throws ShareHolderMeetingServiceException {
		try {
			String dataBaseName = ShareHolderMeetingServiceImpl.dataBaseName();
			if (shareholderid == null || shareholderid.isEmpty()) {
				throw new ShareHolderMeetingServiceException("id should not be null or empty");
			}
			ShareHolderMeetingEntity shareholdermeeting = sharemeetingdao.getShareHolderMeetingById(shareholderid,
					dataBaseName);
			if (shareholdermeeting == null || !shareholdermeeting.getShareholderid().equalsIgnoreCase(shareholderid) ) {
				throw new ShareHolderMeetingServiceException("shareholderid are not present in database");
			}
			else {
			this.sharemeetingdao.deleteShareHolderMeeting(shareholderid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "share holder meeting deleted suucessfully");
			return reposneJSON.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderMeetingServiceException( e.getMessage());
		}

	}

	@Override
	public JSONObject extractMomFiledetails(String meetingId, CompletedFileUpload momfile)
			throws ShareHolderMeetingServiceException {
		try
		{
		String dataBaseName = ShareHolderMeetingServiceImpl.dataBaseName();
		if (meetingId == null || meetingId.isEmpty()) {
			System.out.println("meetingId connot be null or empty");

		}
		JSONObject response=this.sharemeetingdao.extractmomfile(meetingId,momfile,dataBaseName);
		return response;
	}
		catch (Exception e) {
			throw new ShareHolderMeetingServiceException(e.getMessage());

		}
	}

	@Override
	public ShareHolderMeetingEntity getPreviewForMomFile(String shareholderid)
			throws ShareHolderMeetingServiceException {
              try
              {
             String dataBaseName=ShareHolderMeetingServiceImpl.dataBaseName();
             if(shareholderid==null || shareholderid.isEmpty())
             {
            	 throw new ShareHolderMeetingServiceException("id should not be emptyor null");
            	 
             }
             else
             {
            	 ShareHolderMeetingEntity response=this.sharemeetingdao.getPreviewForMomfile(shareholderid,dataBaseName);
            	 return response;
             }
              }catch (Exception e) {
            	  
            	  throw new ShareHolderMeetingServiceException(e.getMessage());
			}
	}

}
