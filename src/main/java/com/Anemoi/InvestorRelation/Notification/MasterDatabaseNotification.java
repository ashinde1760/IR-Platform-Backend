package com.Anemoi.InvestorRelation.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemEntity;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemService;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetService;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowService;
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsService;
import com.Anemoi.InvestorRelation.ClientLineItem.ClientLineItemEntity;
import com.Anemoi.InvestorRelation.ClientLineItem.ClientLineItemService;
import com.Anemoi.InvestorRelation.IncomeStatement.IncomeStatementEntity;
import com.Anemoi.InvestorRelation.IncomeStatement.IncomeStatementService;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationService;
import com.Anemoi.InvestorRelation.ShareHolderContact.ShareHolderContactEntity;
import com.Anemoi.InvestorRelation.ShareHolderContact.ShareHolderContactService;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHoderDataFromService;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromEntity;
import com.Anemoi.InvestorRelation.UserModel.UserModelServiceException;
import com.Anemoi.InvestorRelation.UserModel.UserService;
import com.Anemoi.MailSession.MailService;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class MasterDatabaseNotification {

	
	@Inject 
	private UserService userservice;
	
	@Inject
	private NotificationService notificationService;
	
   @Inject
   private ClientDetailsService clientDetailsService;
	
	private final CashFlowService cashFlowService; // Inject your repository here
	private final BalanceSheetService balanceSheetService;
	private final IncomeStatementService incomeStatementService;
	private final ShareHoderDataFromService dataFromService;
	private final ShareHolderContactService contactService;
	private final AnalystLineItemService analystLineItemService;
	private final ClientLineItemService clientLineItemService;
    private final MailService mailService; // Inject your notification service here

    public MasterDatabaseNotification(CashFlowService cashFlowService, MailService mailService,BalanceSheetService balanceSheetService, IncomeStatementService incomeStatementService, ShareHoderDataFromService dataFromService, ShareHolderContactService contactService, AnalystLineItemService analystLineItemService, ClientLineItemService clientLineItemService) {
        this.cashFlowService = cashFlowService;
		this.incomeStatementService = incomeStatementService;
		this.dataFromService = dataFromService;
		this.contactService = contactService;
		this.analystLineItemService = analystLineItemService;
		this.clientLineItemService = clientLineItemService;
        this.mailService = mailService;
        this.balanceSheetService=balanceSheetService;
 
    }
    
    @Scheduled(cron = "0 20 10 * * ?") // Runs every day at 11:00 PM
	void sendLineItemNotification() throws MessagingException, UserModelServiceException {
		Transport transport = null;
		Date today = new Date();
		long startTimestamp = getStartOfDayTimestamp(today);
		long endTimestamp = getStartOfDayTimestamp(new Date(today.getTime() + 24 * 60 * 60 * 1000));
		System.out.println("startTimestamp schedule" + startTimestamp);
		System.out.println("endTimestamp" + endTimestamp);

//		Session session = com.Anemoi.MailSession.MailSessionInstance.getMailSession();
//		transport = session.getTransport();
//		transport.connect();
		ArrayList<String> tablesName = new ArrayList<>();
		
		
		

		// Master database addition modification
		ArrayList<CashFlowEntity> list = this.cashFlowService.getlineItemForToday(startTimestamp, endTimestamp);
		if (!list.isEmpty()) {
			System.out.println("check 1");
			tablesName.add("Cash Flow");
		}
		ArrayList<BalanceSheetEntity> balanceList = this.balanceSheetService.getBalanceSheetLineItem(startTimestamp,
				endTimestamp);
		if (!balanceList.isEmpty()) {
			System.out.println("check 2");

			tablesName.add("Balance Sheet");
		}
		ArrayList<IncomeStatementEntity> incomelist = this.incomeStatementService
				.getIncomesheetLineItems(startTimestamp, endTimestamp);
		if (!incomelist.isEmpty()) {
			System.out.println("check 3");

			tablesName.add("Income Statement");
		}
		ArrayList<ShareHolderDataFromEntity> getCurrentDateData = this.dataFromService
				.getCurrentDateData(startTimestamp, endTimestamp);
		if (!getCurrentDateData.isEmpty()) {
			tablesName.add("Shareholder Data");
		}
		ArrayList<ShareHolderContactEntity> getdata = this.contactService.getCurrentDateData(startTimestamp,
				endTimestamp);
		if (!getdata.isEmpty()) {
			tablesName.add("Shareholder Contact Details");
		}
		ArrayList<String> masterAdminEmails = this.userservice.getemailIdForMasterAdmin();
		ArrayList<String> analystAdminEmails = this.userservice.getemailIdForAnalystAdmin();
  
		if(!tablesName.isEmpty())
	    {
		mailService.MasterDatabaseMail(analystAdminEmails, masterAdminEmails, tablesName);
       }
		//notification code
		List<String> notificationList=new ArrayList<>();
		notificationList.addAll(masterAdminEmails);
		notificationList.addAll(analystAdminEmails);
		Date date=new Date();
		NotificationEntity nentity=new NotificationEntity();
		nentity.setMessage("Addition/Modification of Master database");
		nentity.setUsers(notificationList);
		nentity.setCreatedOn(date.getTime());
		this.notificationService.addNotificationHistory(nentity);
		
		
		
		// AnalysT SETUP add line item in lineItem table mail send
		Set<String> masterTableName = this.analystLineItemService.getCurrentDateAddingAnalystLineItem(startTimestamp,
				endTimestamp);
       if(!masterTableName.isEmpty())
       {
		mailService.lineItemAddAnalystLineItem( analystAdminEmails, masterAdminEmails, masterTableName);
       }
       
     //notification code
     		NotificationEntity anlystenatity=new NotificationEntity();
     		anlystenatity.setMessage("Addition/Modfication made in Analyst Setup");
     		anlystenatity.setUsers(notificationList);
     		anlystenatity.setCreatedOn(date.getTime());
     		this.notificationService.addNotificationHistory(anlystenatity);
       
       
		// AnalysT SETUP mapp line item in lineItem table mail send
		Set<String> mappingMasterTable = this.analystLineItemService.getCurrentDateMapingAnalystLineItem(startTimestamp,
				endTimestamp);
     if(!mappingMasterTable.isEmpty())
     {
		mailService.lineItemAddAnalystLineItem(analystAdminEmails, masterAdminEmails, mappingMasterTable);
     }
   //notification code
		NotificationEntity anlystenatity1=new NotificationEntity();
		anlystenatity1.setMessage("Mapping made in Analyst Setup");
		anlystenatity1.setUsers(notificationList);
		anlystenatity1.setCreatedOn(date.getTime());
		this.notificationService.addNotificationHistory(anlystenatity1);
		
		
		// Client Setup add line item in clientLineitem Table

		Set<String> clientNamelist = this.clientLineItemService.getclientNameClientLineItemTable(startTimestamp,
				endTimestamp);

		for (String clientName : clientNamelist) {
			System.out.println(clientName);

			ArrayList<String> analystAdminForthisClient = this.clientDetailsService.getAnalystAdminlist(clientName);
			for (String analystAdmin : analystAdminForthisClient) {
				System.out.println("analystAdmin" + analystAdmin);
			}

			// get how many master table add for this clientName //when we add lineItem
			ArrayList<ClientLineItemEntity> addMasterTableinclientLineItemTable = this.clientLineItemService
					.getMasterTableListWhenClinetLineItemAddition(startTimestamp, endTimestamp, clientName);

			if (!addMasterTableinclientLineItemTable.isEmpty()) {
				System.out.println("call here");
				Map<String, List<String>> createdByToLineItemsMap = new HashMap<>();

				// Group lineItem names by createdBy
				for (ClientLineItemEntity detailsEntity : addMasterTableinclientLineItemTable) {
					String createdBy = detailsEntity.getCreatedBy();
					String mastertableName = detailsEntity.getMasterTableSource();

					createdByToLineItemsMap.computeIfAbsent(createdBy, k -> new ArrayList<>()).add(mastertableName);
				}

				for (Map.Entry<String, List<String>> entry : createdByToLineItemsMap.entrySet()) {
					String createdBy = entry.getKey();
					List<String> masterTableNames = entry.getValue();
					System.out.println("createdBy" + createdBy);
					String email = this.userservice.getemailForCreatedBy(createdBy);
					System.out.println("email" + email);
					Set<String> masterName = new HashSet<>();
					System.out.println("matsertable" + masterTableNames);
					masterName.addAll(masterTableNames);
					mailService.addclientlineItemnotification( clientName, email, analystAdminForthisClient,
							masterName);
					
					//notification code
					List<String> clientnotificationlist=new ArrayList<>();
					clientnotificationlist.add(email);
					clientnotificationlist.addAll(analystAdminForthisClient);
					Date d=new Date();
					NotificationEntity e=new NotificationEntity();
					e.setMessage("Addition/Modfication made in Client Setup");
					e.setUsers(clientnotificationlist);
					e.setCreatedOn(d.getTime());
					this.notificationService.addNotificationHistory(e);
					
				}
			} else {
				System.out.println("skip");
			}
			// when we mapping the line Item
			ArrayList<ClientLineItemEntity> mappingMasterTableinclientLineItemTable = this.clientLineItemService
					.getMasterTableNameWhenMappingClientLineItem(startTimestamp, endTimestamp, clientName);

			if (!mappingMasterTableinclientLineItemTable.isEmpty()) {
				System.out.println("call here");
				Map<String, List<String>> createdByToLineItemsMap = new HashMap<>();

				// Group lineItem names by createdBy
				for (ClientLineItemEntity detailsEntity : mappingMasterTableinclientLineItemTable) {
					String createdBy = detailsEntity.getCreatedBy();
					String mastertableName = detailsEntity.getMasterTableSource();

					createdByToLineItemsMap.computeIfAbsent(createdBy, k -> new ArrayList<>()).add(mastertableName);
				}

				for (Map.Entry<String, List<String>> entry : createdByToLineItemsMap.entrySet()) {
					String createdBy = entry.getKey();
					List<String> masterTableNames = entry.getValue();
					System.out.println("createdBy" + createdBy);
					String email = this.userservice.getemailForCreatedBy(createdBy);
					System.out.println("email" + email);
					Set<String> masterName = new HashSet<>();
					System.out.println("matsertable" + masterTableNames);
					masterName.addAll(masterTableNames);
					mailService.addclientlineItemnotification( clientName, email, analystAdminForthisClient,
							masterName);
					
					//notification code
					List<String> ea=new ArrayList<>();
					ea.add(email);
					ea.addAll(analystAdminForthisClient);
					Date d=new Date();
					NotificationEntity e=new NotificationEntity();
					e.setMessage("Mapping made in Client Setup");
					e.setUsers(ea);
					e.setCreatedOn(d.getTime());
					this.notificationService.addNotificationHistory(e);
				}
			} else {
				System.out.println("skip");
			}
		}

	}

	private long getStartOfDayTimestamp(Date date) {
		// TODO Auto-generated method stub
        return date.toInstant().toEpochMilli() - date.getTime() % (24 * 60 * 60 * 1000);

	}
}
