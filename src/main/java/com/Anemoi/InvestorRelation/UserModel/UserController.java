package com.Anemoi.InvestorRelation.UserModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.owasp.encoder.Encode;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/investor")
public class UserController {

	@Inject
	private UserService userservice;

	@Post("/addUser")
	public HttpResponse<UserEntity> createUser(@Body UserEntity user) throws UserModelControllerException {
		try {
			UserEntity newUser = this.userservice.createNewUser(user);
			return HttpResponse.status(HttpStatus.OK).body(newUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("403"), e, 406,
					e.getMessage());
		}

	}
	
	
	@Get("/{userid}")
	public HttpResponse<UserEntity> getUserById(@PathVariable("userid") String userid)
			throws UserModelControllerException {
		try {
			UserEntity user = this.userservice.getUserById(userid);
			return HttpResponse.status(HttpStatus.OK).body(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public List<UserEntity> getAllUser() throws UserModelControllerException {
		try {
			List<UserEntity> getuser = this.userservice.getAllUsers();
			for (UserEntity item : getuser) {
	            item.setFirstName(escapeHtml(item.getFirstName())); // Properly escape HTML
	        }
		return getuser;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }
	@Patch("/{userid}")
	public HttpResponse<UserEntity> updateUser(@Body UserEntity user, @PathVariable("userid") String userid)
			throws UserModelControllerException {
		try {
			UserEntity updateuser = this.userservice.updateUser(user, userid);
			return HttpResponse.status(HttpStatus.OK).body(updateuser);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("403"), e, 406,
					e.getMessage());
		}

	}

	@Delete("/{userid}")
	public HttpResponse<UserEntity> deleteUser(@PathVariable("userid") String userid,String createdBy)
			throws UserModelControllerException {
		try {
		
			UserEntity response = this.userservice.deleteUser(userid,createdBy);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/getbyroleName/{roleName}")
	public ArrayList<UserEntity> getUserByRoleName(@PathVariable("roleName") String roleName)
			throws UserModelControllerException {
		try {
			ArrayList<UserEntity> user = this.userservice.getUserbyRolename(roleName);
			return user;
		} catch (Exception e) {
			// TODO: handle exception
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("/email/{email}")
	public HttpResponse<UserEntity> getUserByEmail(@PathVariable("email") String email)
			throws UserModelControllerException {
		try {
			UserEntity user = this.userservice.getUserByEmail(email);
			return HttpResponse.status(HttpStatus.OK).body(user);
		} catch (Exception e) {
			// TODO: handle exception
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}
	
	@Get("/logIn/{email}")
	public String UserLogIn(@PathVariable("email") String email) throws UserModelControllerException
	{
		try
		{
			String response=this.userservice.AuditForLogIn(email);
			return response;
		}
		catch (Exception e) {
			
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Get("/logout/{email}")
	public String UserLogOut(@PathVariable("email") String email) throws UserModelControllerException
	{
		try
		{
			String response=this.userservice.AuditForUserLogOut(email);
			return response;
		}
		catch (Exception e) {
			
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Get("/getAuditHistory")
	public ArrayList<AuditHistoryEntity> getAllAuditHistory() throws UserModelControllerException
	{
		try {
			ArrayList<AuditHistoryEntity> responceList=this.userservice.getAuditHistoryDetails();
			return responceList;
			
		}
		catch (Exception e) {
			
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Get("/getNotificationHistory")
	public ArrayList<NotificationEntity> getNotificationHistory() throws UserModelControllerException
	{
		try
		{
			ArrayList<NotificationEntity> response=this.userservice.getNotificationHistory();
			return response;
		}catch (Exception e) {
			
			throw new UserModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	

}
