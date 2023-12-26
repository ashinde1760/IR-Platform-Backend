package com.Anemoi.InvestorRelation.ClientDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Session;
import javax.mail.Transport;

import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationService;
import com.Anemoi.InvestorRelation.UserModel.UserDao;
import com.Anemoi.MailSession.MailService;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ClientDetailsServiceImpl implements ClientDetailsService {

	@Inject
	ClientDetailsDao clientDetailsDao;
	
	@Inject
	private AuditHistoryService auditHistoryService;
	
	@Inject
	private NotificationService notificationService;
	
	@Inject
	private UserDao userdao;

	@Inject
	private MailService mailService;

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
	public ClientDetailsEntity addClientDetails(ClientDetailsEntity detailsEntity) throws ClientServiceException {
		Transport transport = null;
		try {
			String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			ClientDetailsEntity response = this.clientDetailsDao.addClientDetails(detailsEntity, dataBaseName);
			
	    
		
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setCreatedBy(detailsEntity.getCreatedBy());
			entity.setActivity("Add Client Details");
			entity.setDescription("Add Client Details in application ");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			System.out.println("detailsEntity.getCreatedBy(:"+detailsEntity.getCreatedBy());
			///mail code
			String role = this.userdao.getroleForUser(detailsEntity.getCreatedBy(), dataBaseName);
			System.out.println("role" + role);

			
			if(detailsEntity.getEmailId().toString()==null)
			{
				System.out.println("skip");
			}
			else {
			//get user name for client id
			ArrayList<String> usernameList=this.userdao.getUserNameEmailIds(detailsEntity.getEmailId(),dataBaseName);
			
			if(role.equalsIgnoreCase("Master Admin"))
			{
			
				mailService.clientAdminCCmasterAdminandAnalystAdmin(detailsEntity,usernameList);
				
			}
			if(role.equalsIgnoreCase("Analyst Admin"))
			{
			
				mailService.clientAdminCCAnalystAdmin(detailsEntity,usernameList);
				
			}
			if(role.equalsIgnoreCase("Client Admin"))
			{
				
				mailService.clientAdminCCAnalystAdmin(detailsEntity,usernameList);
				
			}
			}
			//notification code
			if(detailsEntity.getEmailId()==null)
			{
				   List<String> list=detailsEntity.getAssignAA();
				   Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Addition of new Client Admin");
					nentity.setUsers(list);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
			}
			else {
		   List<String> list=detailsEntity.getEmailId();
		   list.addAll(detailsEntity.getAssignAA());
			Date date=new Date();
			NotificationEntity nentity=new NotificationEntity();
			nentity.setMessage("Addition of new Client Admin");
			nentity.setUsers(list);
			nentity.setCreatedOn(date.getTime());
			this.notificationService.addNotificationHistory(nentity);
			}
			
			return response;
		} catch (Exception e) {

		throw new ClientServiceException(e.getMessage());
		}
	
	}

	@Override
	public ArrayList<ClientDetailsEntity> getAllClientDetails() throws ClientServiceException {
		try {
			String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			ArrayList<ClientDetailsEntity> responseList = this.clientDetailsDao.getAllClientDetails(dataBaseName);
			return responseList;
		} catch (Exception e) {
			throw new ClientServiceException(e.getMessage());
		}

	}

	@Override
	public ClientDetailsEntity getClientDetailsByProjectCode(int projectCode) throws ClientServiceException{

		try {
			String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			ClientDetailsEntity response = this.clientDetailsDao.getclientDetailsByprojectCode(projectCode,dataBaseName);
			return response;

		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientServiceException(e.getMessage());
		}
	
	}

	@Override
	public ClientDetailsEntity addProjectCodeDetails(ClientDetailsEntity clientDetailsEntity) {

		try {
			String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			ClientDetailsEntity response = this.clientDetailsDao.addProjectCodeFor(clientDetailsEntity, dataBaseName);
		
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public ClientDetailsEntity updateClientDetails(String clientId, ClientDetailsEntity clientDetailsEntity)
			throws ClientServiceException {
		Transport transport = null;
		  try
		  {
			  String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			  ClientDetailsEntity response=this.clientDetailsDao.updateClientDetails(clientId,clientDetailsEntity,dataBaseName);
			 
			  Date d=new Date();
				AuditHistoryEntity entity=new AuditHistoryEntity();
				entity.setCreatedBy(clientDetailsEntity.getModifiedBy());
				entity.setActivity("Update Client Details");
				entity.setDescription("Client Details updated in application");
				entity.setCreatedOn(d.getTime());
				this.auditHistoryService.addAuditHistory(entity);
				
				///mail code
				String role = this.userdao.getroleForUser(clientDetailsEntity.getModifiedBy(), dataBaseName);
				System.out.println("role" + role);
			
				
				//get user name for client id
				ArrayList<String> usernameList=this.userdao.getUserNameEmailIds(clientDetailsEntity.getEmailId(),dataBaseName);
				
				if(role.equalsIgnoreCase("Master Admin"))
				{
					mailService.updateClientAdminCCMasterAdminAndAnalystAdmin(clientDetailsEntity,usernameList);
				}
			   if(role.equalsIgnoreCase("Analyst Admin"))
				{
					mailService.updateclientAdminCCAnalystAdmin(clientDetailsEntity,usernameList);
				}
			   if(role.equalsIgnoreCase("Client Admin"))
				{
					mailService.updateclientAdminCCAnalystAdmin(clientDetailsEntity,usernameList);
				}
			   
			 //notification code
			   List<String> list=clientDetailsEntity.getEmailId();
			   list.addAll(clientDetailsEntity.getAssignAA());
				Date date=new Date();
				NotificationEntity nentity=new NotificationEntity();
				nentity.setMessage("Modification of client admin");
				nentity.setUsers(list);
				nentity.setCreatedOn(date.getTime());
				this.notificationService.addNotificationHistory(nentity);
				
			   
			   
				
				
				
			  return response;
		  }
		  catch (Exception e) {
			  throw new ClientServiceException(e.getMessage());
			// TODO: handle exception
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteClientDetails(String clientId,String createdBy) throws ClientServiceException {
		 try
		  {
			  String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			  String clientName=this.clientDetailsDao.getclientNameByid(clientId,dataBaseName);
			  System.out.println("clientName"+clientName);
			  this.clientDetailsDao.deleteClientForNonProcessingtable(clientName,dataBaseName);
			  this.clientDetailsDao.deleteClientDetails(clientId,dataBaseName);
			  
			  Date d=new Date();
				AuditHistoryEntity entity=new AuditHistoryEntity();
				entity.setCreatedBy(createdBy);
				entity.setActivity("delete Client Details");
				entity.setDescription("Client Details delete in application");
				entity.setCreatedOn(d.getTime());
				this.auditHistoryService.addAuditHistory(entity);
				
			  JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "client details deleted suucessfully");
				return reposneJSON.toString();
		  }
		  catch (Exception e) {
			  throw new ClientServiceException(e.getMessage());
			// TODO: handle exception
		}
	}

	@Override
	public ClientDetailsEntity getClientData(String clientId) throws ClientServiceException {
            try
            {
		String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
		ClientDetailsEntity response=this.clientDetailsDao.getClientDataByclientId(clientId,dataBaseName);
		return response;
            }
            catch (Exception e) {
            	
            	 throw new ClientServiceException(e.getMessage());
			}
	}

	@Override
	public ClientDetailsEntity getClientDataByClientName(String clientName) throws ClientServiceException {

            try
            {
            	String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
            	ClientDetailsEntity reponse=this.clientDetailsDao.getDetailsByClientName(clientName,dataBaseName);
            	return reponse;
            }
            catch (Exception e) {
            	 throw new ClientServiceException(e.getMessage());
			}
	}

	@Override
	public ArrayList<String> getAnalystAdminlist(String clientName) {
		try {
			String dataBaseName = ClientDetailsServiceImpl.dataBaseName();
			ArrayList<String> analystAdminList=this.clientDetailsDao.getAnalystAdminForClientName(clientName,dataBaseName);
			return analystAdminList;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
