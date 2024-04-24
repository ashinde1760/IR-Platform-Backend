package com.Anemoi.InvestorRelation.UserModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;

public interface UserDao {

	UserEntity createNewUser(UserEntity user, String dataBaseName) throws UserModelDaoException;

	UserEntity getUserById(String userid, String dataBaseName) throws UserModelDaoException;

	List<UserEntity> getAllUsers(String dataBaseName) throws UserModelDaoException;

	UserEntity updateUser(UserEntity user, String userid, String dataBaseName) throws UserModelDaoException;

	String deleteUser(String userid, String dataBaseName) throws UserModelDaoException;

	ArrayList<UserEntity> getUserbyRoleName(String roleName, String dataBaseName);

	UserEntity getUserByEmail(String email, String dataBaseName) throws UserModelDaoException;

	ArrayList<String> getAllEmailForMasterAdmin(UserEntity user, String dataBaseName) throws UserModelDaoException;

	String getroleForUser(String createdBy, String dataBaseName) throws UserModelDaoException;

	ArrayList<String> getUserNameEmailIds(List<String> emailId, String dataBaseName) throws UserModelDaoException;


	ArrayList<String> getEmailIdsForMasterAdmin(String dataBaseName) throws UserModelDaoException;

	ArrayList<String> getEmailIdsForAnalystAdmin(String dataBaseName) throws UserModelDaoException;

	String getemailForCreatedby(String dataBaseName, String createdBy);

	String getroleFormodifiedBy(String modifiedBy, String dataBaseName);

	ArrayList<String> getAllEmailForClientAdmin(UserEntity user, String dataBaseName) throws UserModelDaoException;

	}