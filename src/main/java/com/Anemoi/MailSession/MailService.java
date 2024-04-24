package com.Anemoi.MailSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.mail.Transport;

import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.DataIngestion.DataIngestionModel;
import com.Anemoi.InvestorRelation.MeetingScheduler.MSTeamschedule;
import com.Anemoi.InvestorRelation.UserModel.UserEntity;

public interface MailService {

   //Master Admin
	void updateMasterAdminsendMailToconcernMasterAdmin( UserEntity user,ArrayList<String> emailList); //update master admin 
	
	void emailSendToAllMasterAdmin(UserEntity user, ArrayList<String> emailList);  //done for all master

	//Analyst Admin
	void emailSendToAnalstAdminAndCCAllMasterAdmin(UserEntity user, ArrayList<String> emailList);

	void updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins( UserEntity user,ArrayList<String> emailList);
	
	// Client Admin
	
	

	//master database
	void sendLineItemAddNotification(Transport transport, String createdBy, String tableName, List<String> lineItems);

	void MasterDatabaseMail( ArrayList<String> analystAdminEmails,
			ArrayList<String> masterAdminEmails, ArrayList<String> tablesName);


	//new meeting schedule
	void newMeetingScheduleMailSendToClientAdmin(Transport transport, MSTeamschedule msts, List<String> clientAdmins,
			List<String> analystAdmins);

	void updateMeetingMailSendToClientAdmin(Transport transport, MSTeamschedule teamschedule, List<String> clientAdmins,
			List<String> analystAdmins);

	//file upload in dataingestion
	void uploadFileMailSendToClientAdminCCAnalystAdmin( DataIngestionModel ingestionModel,
			List<String> clientAdmins, List<String> analystAdmins);



	void clientAdminCCmasterAdminandAnalystAdmin(ClientDetailsEntity detailsEntity,
			ArrayList<String> usernameList);

	void clientAdminCCAnalystAdmin( ClientDetailsEntity detailsEntity,
			ArrayList<String> usernameList);

	void updateClientAdminCCMasterAdminAndAnalystAdmin( ClientDetailsEntity clientDetailsEntity,
			ArrayList<String> usernameList);

	void updateclientAdminCCAnalystAdmin( ClientDetailsEntity clientDetailsEntity,
			ArrayList<String> usernameList);

	void lineItemAddAnalystLineItem( ArrayList<String> analystAdminEmails,
			ArrayList<String> masterAdminEmails, Set<String> masterTableName);

	void addclientlineItemnotification(String clientName, String email,
			ArrayList<String> analystAdminForthisClient, Set<String> masterName);

	void uploadShareHolderDataExcelSheetToSendMail(String msg,String createdBy,String status,String region) throws ClassNotFoundException;

	void uploadShareHolderDataExcelSheetNOTUploadToSendMail(String msg, String createdBy, String status, String region);

	

	





	
}
