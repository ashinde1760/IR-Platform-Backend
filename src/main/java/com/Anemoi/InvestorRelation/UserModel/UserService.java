package com.Anemoi.InvestorRelation.UserModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.NotificationHistory.NotificationEntity;

public interface UserService {

	UserEntity createNewUser(UserEntity user) throws SQLException, UserModelServiceException;

	UserEntity getUserById(String userid) throws SQLException, UserModelServiceException;

	List<UserEntity> getAllUsers() throws SQLException, UserModelServiceException;

	UserEntity updateUser(UserEntity user, String userid) throws UserModelServiceException;

	UserEntity deleteUser(String userid,String createdBy) throws UserModelServiceException;

	ArrayList<UserEntity> getUserbyRolename(String roleName);

	UserEntity getUserByEmail(String email) throws UserModelServiceException;

	String AuditForLogIn(String email) throws UserModelServiceException;

	String AuditForUserLogOut(String email) throws UserModelServiceException;

	ArrayList<AuditHistoryEntity> getAuditHistoryDetails() throws UserModelServiceException;
	
	ArrayList<String> getemailIdForMasterAdmin() throws UserModelServiceException;

	ArrayList<String> getemailIdForAnalystAdmin() throws UserModelServiceException;

	String getemailForCreatedBy(String createdBy);

	ArrayList<NotificationEntity> getNotificationHistory();
}
