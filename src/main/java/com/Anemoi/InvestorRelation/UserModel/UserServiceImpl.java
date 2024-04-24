package com.Anemoi.InvestorRelation.UserModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.Session;
import javax.mail.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationService;
import com.Anemoi.MailSession.MailService;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class UserServiceImpl implements UserService {

	@Inject
	private UserDao userdao;
	
	@Inject
	private AuditHistoryService auditHistoryService;

	@Inject
	private MailService mailService;
	
	@Inject
	private NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
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
	public UserEntity createNewUser(UserEntity user) throws UserModelServiceException {
//		Transport transport = null;

		try {

			String dataBaseName = UserServiceImpl.dataBaseName();
			// applyValidation(user);
            // add user details in database
			UserEntity createNewUser = this.userdao.createNewUser(user, dataBaseName);
			
			//add userDetails in audit history
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setActivity("User Added");
			entity.setCreatedBy(user.getCreatedBy());
			entity.setDescription("'"+user.getCreatedBy()+"'"+ " created user "+"  "+"'"+user.getEmail() +"'"+"  " +"successfully");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			

			///mail code
			String role = this.userdao.getroleForUser(user.getCreatedBy(), dataBaseName);
			System.out.println("role" + role);
			if (role.equalsIgnoreCase("Master Admin")) // check role for created by
			{
				System.out.println("role is Master Admin");
				if (user.getRoleName().equalsIgnoreCase("Master Admin")) {
//					Session session = com.Anemoi.MailSession.MailSessionInstance.getMailSession();
//					transport = session.getTransport();
//					transport.connect();

					// mail send to master Admin
					ArrayList<String> emailList = this.userdao.getAllEmailForMasterAdmin(user, dataBaseName);
					if (!emailList.isEmpty()) {
						mailService.emailSendToAllMasterAdmin(user, emailList);
					} else {
						System.out.println("skip");
					}
					//notification code
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Addition of new Master Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				}

				else if (user.getRoleName().equalsIgnoreCase("Analyst Admin")) {
			
					ArrayList<String> emailList = this.userdao.getAllEmailForMasterAdmin(user, dataBaseName);

					mailService.emailSendToAnalstAdminAndCCAllMasterAdmin( user, emailList);
					
					//notification code
					emailList.add(user.getEmail());
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Addition of new Analyst Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				}
				else if (user.getRoleName().equalsIgnoreCase("Client Admin"))
				{
					
					ArrayList<String> emailList = this.userdao.getAllEmailForClientAdmin(user, dataBaseName);

					mailService.updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins( user, emailList);

					//notification code
					emailList.add(user.getEmail());
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Modification of Analyst Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				}
				
			}
			else if(role.equalsIgnoreCase("Analyst Admin"))
			{
				System.out.println("role is Analyst Admin");
				if (user.getRoleName().equalsIgnoreCase("Analyst Admin"))
				{

					ArrayList<String> emailList = this.userdao.getAllEmailForMasterAdmin(user, dataBaseName);
		
             
					mailService.emailSendToAnalstAdminAndCCAllMasterAdmin(user, emailList);
					
					//notification code
					emailList.add(user.getEmail());
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Addition of new Analyst Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
                 
				}
			}

			return createNewUser;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new UserModelServiceException("unable to create user " + e.getMessage());
		}

	}



	
	private void applyValidation(UserEntity user) throws Exception {
		// TODO Auto-generated method stub
		Pattern pattern;
		if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
			throw new Exception("first name should not be null or empty");
		}
		pattern = Pattern.compile("[A-Z]{1}[a-z]{2,14}");
		boolean result = pattern.matcher(user.getFirstName()).matches();
		if (!result) {
			throw new Exception("please enter the valid firstname formate");
		}

		if (user.getLastName() == null || user.getLastName().isEmpty()) {
			throw new Exception("last name should not be null or empty");
		}
		pattern = Pattern.compile("[A-Z]{1}[a-z]{2,14}");
		boolean result1 = pattern.matcher(user.getLastName()).matches();
		if (!result1) {
			throw new Exception("please enter the valid lastname formate");
		}

		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new Exception("Email cannot be null or empty");
		}
		String emailRegex = "([a-zA-Z0-9]+)([\\.{1}])?([a-zA-Z0-9]+)\\@(?:gmail|GMAIL)([\\.])(?:com|COM)";
		pattern = Pattern.compile(emailRegex);
		boolean result3 = pattern.matcher(user.getEmail()).matches();
		if (!result3) {
			throw new Exception(user.getEmail() + " is invalid email formate");
		}

		if (user.getMobileNumber() == null || user.getMobileNumber().isEmpty()) {
			throw new Exception("mobileNumber cannot be null or empty");
		}
		pattern = Pattern.compile("[6-9]{1}[0-9]{9}");
		boolean result2 = pattern.matcher(user.getMobileNumber()).matches();
		if (!result2) {
			throw new Exception("invalid mobile number formate");
		}

		if (user.getAssignedName() == null || user.getAssignedName().isEmpty()) {
			throw new Exception("assigned Name cannot be null or empty");
		}
		if (user.getRoleName() == null || user.getRoleName().isEmpty()) {
			throw new Exception("role name cannot be null or empty");
		}
		if (user.getUserStatus() == null || user.getUserStatus().isEmpty()) {
			throw new Exception("status cannot be null or empty");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new Exception("password cannot be null or empty");
		}

	}

	@Override
	public UserEntity getUserById(String userid) throws UserModelServiceException {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			if (userid == null || userid.isEmpty()) {
				System.out.print("User id must not be null or empty");
			}
			UserEntity user = this.userdao.getUserById(userid, dataBaseName);
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelServiceException("unable to get user by id" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserEntity> getAllUsers() throws UserModelServiceException {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			List<UserEntity> users = this.userdao.getAllUsers(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray userJsonData = getJSONFromUserList(users);
			object.put(users, userJsonData);
			return users;

		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelServiceException("unable to get user list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromUserList(List<UserEntity> users) {
		JSONArray array = new JSONArray();
		for (UserEntity user : users) {
			JSONObject object = buildJsonFromUser(user);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromUser(UserEntity user) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(UserQueryConstant.USERID, user.getUserid());
		jsonObject.put(UserQueryConstant.FIRST_NAME, user.getFirstName());
		jsonObject.put(UserQueryConstant.LAST_NAME, user.getLastName());
		jsonObject.put(UserQueryConstant.EMAIL, user.getEmail());
		jsonObject.put(UserQueryConstant.MOBILE_NUMBER, user.getMobileNumber());
	
		jsonObject.put(UserQueryConstant.ASSIGNEDNAME, user.getAssignedName());
		jsonObject.put(UserQueryConstant.ROLENAME, user.getRoleName());
		jsonObject.put(UserQueryConstant.STATUS, user.getUserStatus());
		jsonObject.put(UserQueryConstant.CREATEDON, user.getCreatedOn());
		jsonObject.put(UserQueryConstant.EDITEDON, user.getEditedOn());
		jsonObject.put(UserQueryConstant.CREATEDBU, user.getCreatedBy());
		jsonObject.put(UserQueryConstant.MODIFIEDBY, user.getModifiedBy());
//		jsonObject.put(UserQueryConstant.EDITEDMODIFIEDON, user.getEditedOnFormatted());
		return jsonObject;

	}

	@SuppressWarnings("unchecked")
	@Override
	public UserEntity updateUser(UserEntity user, String userid) throws UserModelServiceException {
		Transport transport = null;
	
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
		
			UserEntity getUserDetails = this.userdao.getUserById(userid, dataBaseName);
		
			UserEntity updatedUser = this.userdao.updateUser(user, userid, dataBaseName);
		
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setActivity("User Updated");
			entity.setCreatedBy(user.getModifiedBy());
			entity.setDescription("'"+user.getModifiedBy()+"'"+ " updated user "+"  "+"'"+user.getEmail() +"'"+"  " +"successfully");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			String role = this.userdao.getroleFormodifiedBy(user.getModifiedBy(), dataBaseName);
			System.out.println("role" + role);
			if (role.equalsIgnoreCase("Master Admin")) // check role for created by
			{
				System.out.println("creted by role is  master admin");
				if (getUserDetails.getRoleName().equalsIgnoreCase("Master Admin"))
				{
					System.out.println("update master admin");
//					Session session = com.Anemoi.MailSession.MailSessionInstance.getMailSession();
//					transport = session.getTransport();
//					transport.connect();

					ArrayList<String> emailList = this.userdao.getAllEmailForMasterAdmin(user, dataBaseName);
					emailList.remove(user.getEmail());
					mailService.updateMasterAdminsendMailToconcernMasterAdmin( user, emailList);
					
					//notification code
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Modification of Master Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				 }

				else if (getUserDetails.getRoleName().equalsIgnoreCase("Analyst Admin"))
				{
					
					ArrayList<String> emailList = this.userdao.getAllEmailForMasterAdmin(user, dataBaseName);

					mailService.updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins( user, emailList);

					//notification code
					emailList.add(user.getEmail());
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Modification of Analyst Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				}
				else if (getUserDetails.getRoleName().equalsIgnoreCase("Client Admin"))
				{
					
					ArrayList<String> emailList = this.userdao.getAllEmailForClientAdmin(user, dataBaseName);

					mailService.updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins( user, emailList);

					//notification code
					emailList.add(user.getEmail());
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Modification of Analyst Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				}
			}
			else if(role.equalsIgnoreCase("Analyst Admin"))	//check role for created by
			{
			
				if (getUserDetails.getRoleName().equalsIgnoreCase("Analyst Admin"))
				{
					
					ArrayList<String> emailList = this.userdao.getAllEmailForMasterAdmin(user, dataBaseName);

					mailService.updateAnalystAdminmailtoAnalsytAdminAndAllMasterAdmins( user, emailList);

					//notification code
					emailList.add(user.getEmail());
					Date date=new Date();
					NotificationEntity nentity=new NotificationEntity();
					nentity.setMessage("Modification of Analyst Admin");
					nentity.setUsers(emailList);
					nentity.setCreatedOn(date.getTime());
					this.notificationService.addNotificationHistory(nentity);
				}
			}
			
			JSONObject object = new JSONObject();
			JSONObject jsonFromUser = buildJsonFromUpdatedUser(updatedUser);
			object.put(updatedUser, jsonFromUser);
			return updatedUser;

		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelServiceException("unable to update user" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonFromUpdatedUser(UserEntity updatedUser) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(UserQueryConstant.FIRST_NAME, updatedUser.getFirstName());
		jsonObject.put(UserQueryConstant.LAST_NAME, updatedUser.getLastName());
		jsonObject.put(UserQueryConstant.EMAIL, updatedUser.getEmail());
		jsonObject.put(UserQueryConstant.MOBILE_NUMBER, updatedUser.getMobileNumber());
	
		jsonObject.put(UserQueryConstant.ASSIGNEDNAME, updatedUser.getAssignedName());
		jsonObject.put(UserQueryConstant.ROLENAME, updatedUser.getRoleName());
		jsonObject.put(UserQueryConstant.STATUS, updatedUser.getUserStatus());
		jsonObject.put(UserQueryConstant.CREATEDON, updatedUser.getCreatedOn());
		jsonObject.put(UserQueryConstant.EDITEDON, updatedUser.getEditedOn());
		jsonObject.put(UserQueryConstant.CREATEDBU, updatedUser.getCreatedBy());
		jsonObject.put(UserQueryConstant.MODIFIEDBY, updatedUser.getModifiedBy());
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserEntity deleteUser(String userid,String createdBy) throws UserModelServiceException {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			if (userid == null || userid.isEmpty()) {
				System.out.println("userId cannot be null or empty");

				// throw new UserServiceException("userId cannot be null or empty ");
			}
			UserEntity user = userdao.getUserById(userid, dataBaseName);
			if (user == null) {

				 throw new UserModelServiceException("user not found");
			}
			this.userdao.deleteUser(userid, dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entity=new AuditHistoryEntity();
			entity.setActivity(" User Deleted");
			entity.setCreatedBy(createdBy);
			entity.setDescription("'"+user.getCreatedBy()+"'"+ " deleted user "+"  "+"'"+user.getEmail() +"'"+"  " +"successfully");
			entity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entity);
			
			
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "User deleted successfully");
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelServiceException("unable to delete user" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserEntity> getUserbyRolename(String roleName) {

		String dataBaseName = UserServiceImpl.dataBaseName();
		if (roleName == null || roleName.isEmpty()) {
			System.out.println("roleName cannot be null or empty");

			// throw new Exception("userId cannot be null or empty ");
		}
		ArrayList<UserEntity> user = this.userdao.getUserbyRoleName(roleName, dataBaseName);
		JSONObject object = new JSONObject();
		JSONArray userJsonData = getJSONFromUserList(user);
		object.put(user, userJsonData);
		return user;
	}

	@Override
	public UserEntity getUserByEmail(String email) throws UserModelServiceException {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			ArrayList<String> emailList = new ArrayList<>();
			if (email == null || email.isEmpty()) {
				System.out.println("email cannot be null or empty");
				throw new Exception("userId cannot be null or empty ");
			}
			List<UserEntity> allUsers = this.userdao.getAllUsers(dataBaseName);
			for (UserEntity userEntity : allUsers) {
				emailList.add(userEntity.getEmail());

			}
			System.out.println("list" + emailList);
			if (emailList.contains(email)) {
				UserEntity user = this.userdao.getUserByEmail(email, dataBaseName);
				return user;
			} else {
				throw new Exception("email id not found ");
			}
		} catch (Exception e) {
			throw new UserModelServiceException("unable to get user details by email id" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String AuditForLogIn(String email) throws UserModelServiceException {
		
		Date d=new Date();
		AuditHistoryEntity entity=new AuditHistoryEntity();
		entity.setActivity("User Logged In");
		entity.setCreatedBy(email);
		entity.setDescription("User "+"'"+email +"'" +" Logged in successfully");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		JSONObject reposneJSON = new JSONObject();
		reposneJSON.put(STATUS, SUCCESS);
		reposneJSON.put(MSG, "User login successfully");
		return reposneJSON.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String AuditForUserLogOut(String email) throws UserModelServiceException {
		
		Date d=new Date();
		AuditHistoryEntity entity=new AuditHistoryEntity();
		entity.setActivity("User Logged Out");
		entity.setCreatedBy(email);
		entity.setDescription("User "+"'"+email +"'" +" Logged out successfully");
		entity.setCreatedOn(d.getTime());
		this.auditHistoryService.addAuditHistory(entity);
		JSONObject reposneJSON = new JSONObject();
		reposneJSON.put(STATUS, SUCCESS);
		reposneJSON.put(MSG, "User Logout successfully");
		return reposneJSON.toString();
	}

	@Override
	public ArrayList<AuditHistoryEntity> getAuditHistoryDetails() throws UserModelServiceException {
           try {
		ArrayList<AuditHistoryEntity> list=this.auditHistoryService.getAllAuditHistory();
		return list;
           }
           catch (Exception e) {
        	   throw new UserModelServiceException("unable to get"+e.getMessage());
		}
	}

	@Override
	public ArrayList<String> getemailIdForMasterAdmin() throws UserModelServiceException {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			ArrayList<String> list=this.userdao.getEmailIdsForMasterAdmin(dataBaseName);
			return list;
		}
		catch (Exception e) {
			throw new UserModelServiceException(e.getMessage());
		}
	}

	@Override
	public ArrayList<String> getemailIdForAnalystAdmin() throws UserModelServiceException {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			ArrayList<String> list=this.userdao.getEmailIdsForAnalystAdmin(dataBaseName);
			return list;
		}
		catch (Exception e) {
			throw new UserModelServiceException(e.getMessage());
		}
	}

	@Override
	public String getemailForCreatedBy(String createdBy) {
		try {
			String dataBaseName = UserServiceImpl.dataBaseName();
			String name=this.userdao.getemailForCreatedby(dataBaseName,createdBy);
			return name;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<NotificationEntity> getNotificationHistory() {

       try
       {
    	   ArrayList<NotificationEntity> responseList=this.notificationService.getNotificationHistory();
    	   return responseList;
       }
       catch (Exception e) {
		// TODO: handle exception
    	   e.printStackTrace();
	}
	return null;
	}

	

	}
