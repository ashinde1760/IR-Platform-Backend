package com.Anemoi.InvestorRelation.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsEntity;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import jakarta.inject.Singleton;

@Singleton
public class UserDaoImpl implements UserDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public UserEntity createNewUser(UserEntity user, String dataBaseName) throws UserModelDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();

			LOGGER.debug("inserting the data");
			pstmt = connection.prepareStatement(UserQueryConstant.INSERT_INTO_USERDETAILS
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

			String userId = UUID.randomUUID().toString();
			user.setUserid(userId);
			String id = user.getUserid();
//			user.setPassword(password);
//			String password1 = user.getPassword();

//			System.out.println(id+" "+user);
			Date date = new Date();


			pstmt.setString(1, id);
			pstmt.setString(2, user.getFirstName());
			pstmt.setString(3, user.getLastName());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getMobileNumber());
			pstmt.setString(6, user.getAssignedName());
			pstmt.setString(7, user.getRoleName());
			pstmt.setString(8, user.getUserStatus());
			pstmt.setString(9, null);
			pstmt.setLong(10, date.getTime());
			pstmt.setLong(11, date.getTime());
			pstmt.setString(12, user.getCreatedBy());
			pstmt.setString(13, null);

			pstmt.executeUpdate();
			return user;

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new UserModelDaoException("unable to create user" + e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public UserEntity getUserById(String userid, String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_USER_BY_ID
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, userid);
			result = pstmt.executeQuery();
			while (result.next()) {
				UserEntity user = buildUser(result);
				return user;
			}
		} catch (Exception e) {
			LOGGER.error("User not found" + e.getMessage());
			throw new UserModelDaoException("unable to get user by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;

	}

	private UserEntity buildUser(ResultSet result) throws SQLException {
		UserEntity user = new UserEntity();
		user.setUserid(result.getString(UserQueryConstant.USERID));
		user.setFirstName(result.getString(UserQueryConstant.FIRST_NAME));
		user.setLastName(result.getString(UserQueryConstant.LAST_NAME));
		user.setEmail(result.getString(UserQueryConstant.EMAIL));
		user.setMobileNumber(result.getString(UserQueryConstant.MOBILE_NUMBER));
		user.setAssignedName(result.getString(UserQueryConstant.ASSIGNEDNAME));
		user.setRoleName(result.getString(UserQueryConstant.ROLENAME));
		user.setUserStatus(result.getString(UserQueryConstant.STATUS));
		user.setPassword(result.getString(UserQueryConstant.PASSWORD));
		user.setCreatedOn(result.getLong(UserQueryConstant.CREATEDON));
		user.setEditedOn(result.getLong(UserQueryConstant.EDITEDON));
		user.setCreatedBy(result.getString(UserQueryConstant.CREATEDBU));
		user.setModifiedBy(result.getString(UserQueryConstant.MODIFIEDBY));

		return user;
	}
	
	private UserEntity buildUser1(ResultSet result) throws SQLException {
		UserEntity user = new UserEntity();
		user.setUserid(result.getString(UserQueryConstant.USERID));
		user.setFirstName(result.getString(UserQueryConstant.FIRST_NAME));
		user.setLastName(result.getString(UserQueryConstant.LAST_NAME));
		user.setEmail(result.getString(UserQueryConstant.EMAIL));
		user.setMobileNumber(result.getString(UserQueryConstant.MOBILE_NUMBER));
		user.setAssignedName(result.getString(UserQueryConstant.ASSIGNEDNAME));
		user.setRoleName(result.getString(UserQueryConstant.ROLENAME));
		user.setUserStatus(result.getString(UserQueryConstant.STATUS));
		user.setPassword(result.getString(UserQueryConstant.PASSWORD));
		user.setCreatedOn(result.getLong(UserQueryConstant.CREATEDON));
		user.setEditedOn(result.getLong(UserQueryConstant.EDITEDON));
		user.setCreatedBy(result.getString(UserQueryConstant.CREATEDBU));
		user.setModifiedBy(result.getString(UserQueryConstant.MODIFIEDBY));

		return user;
	}

	@Override
	public List<UserEntity> getAllUsers(String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<UserEntity> listOfUsers = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(
					UserQueryConstant.SELECT_ALL_USERS.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				UserEntity user = buildUser(result);
				listOfUsers.add(user);
			}
			return listOfUsers;
		} catch (Exception e) {
			LOGGER.error("unble to get list of user" + e.getMessage());
			e.printStackTrace();
			throw new UserModelDaoException("unable to get user list" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public UserEntity updateUser(UserEntity user, String userid, String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
//		LOGGER.info(".in update user database name is ::" + dataBaseName + " userId is ::" + userid
//				+ " request user is ::" + user);

		try {

			System.out.println("check1");
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(
					UserQueryConstant.UPDATE_USER.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Date date = new Date();
			System.out.println("user" + user.getRoleName());
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getMobileNumber());
			pstmt.setString(5, user.getAssignedName());
			pstmt.setString(6, user.getRoleName());
			pstmt.setString(7, user.getUserStatus());
			pstmt.setString(8, user.getPassword());
			pstmt.setLong(9, date.getTime());
			pstmt.setString(10, user.getModifiedBy());
			pstmt.setString(11, userid);

			int executeUpdate = pstmt.executeUpdate();

			System.out.println(executeUpdate);
			LOGGER.info(executeUpdate + " User updated successfully");
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserModelDaoException("unable to update" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public String deleteUser(String userid, String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.DELETE_USER_BY_ID
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, userid);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " user deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;

	}

	@Override
	public ArrayList<UserEntity> getUserbyRoleName(String roleName, String dataBaseName) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<UserEntity> listOfUsers = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_USER_BY_ROLENAME
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, roleName);
			result = pstmt.executeQuery();
			while (result.next()) {
				UserEntity user = buildUser(result);
				listOfUsers.add(user);
			}
			return listOfUsers;
		} catch (Exception e) {
			LOGGER.error("unble to get list of user" + e.getMessage());
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;

	}

	@Override
	public UserEntity getUserByEmail(String email, String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_USERDETAILS_BYEMAILID
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, email);
			result = pstmt.executeQuery();
			while (result.next()) {
				UserEntity user = buildUser(result);
				return user;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new UserModelDaoException("unable to get user details by email id");
		}finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@Override
	public ArrayList<String> getAllEmailForMasterAdmin(UserEntity user, String dataBaseName)
			throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<String> emailList = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_EMAILID_MASTER_ADMIN
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, "Master Admin");
			pstmt.setString(2, "Active");
			result = pstmt.executeQuery();
			while (result.next()) {
				String emailIds = buildeMail(result);
				emailList.add(emailIds);
			}
			return emailList;
		} catch (Exception e) {

			throw new UserModelDaoException("unable to email id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
	}

	private String buildeMail(ResultSet result) throws SQLException {

		String email = result.getString("email");
		return email;
	}

	@Override
	public String getroleForUser(String createdBy, String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("createdBy: "+createdBy);

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_ROLE_FORUSER
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, createdBy);
			rs = pstmt.executeQuery();
			String role = null;
			while (rs.next()) {
				role = rs.getString("roleName");
				System.out.println("inside while==>: "+role);
			}
			System.out.println("role--->: "+role);
			return role;
		} catch (Exception e) {

			throw new UserModelDaoException(e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(rs, pstmt, connection);
		}
	}

	@Override
	public ArrayList<String> getUserNameEmailIds(List<String> emailId, String dataBaseName)
			throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<String> usernamelist = new ArrayList<>();
		try {
			Iterator ir = emailId.iterator();
			while (ir.hasNext()) {
				String email = (String) ir.next();
				System.out.println("email" + email);
				connection = InvestorDatabaseUtill.getConnection();
				pstmt = connection.prepareStatement(UserQueryConstant.SELECT_USERNAME
						.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, email);
				result = pstmt.executeQuery();
				while (result.next()) {
					String firstName = result.getString("firstName");
					String lastName = result.getString("lastName");
					String userName = firstName + " " + " " + lastName;
					usernamelist.add(userName);
				}
			}
			return usernamelist;
		} catch (Exception e) {

			throw new UserModelDaoException(e.getMessage());

		}finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ArrayList<String> getEmailIdsForMasterAdmin(String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_EMAILID_MASTER_ADMIN
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, "Master Admin");
			pstmt.setString(2, "Active");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String email = rs.getString("email");
				list.add(email);
			}
			return list;
		}

		catch (Exception e) {
			throw new UserModelDaoException(e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	@Override
	public ArrayList<String> getEmailIdsForAnalystAdmin(String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_ANALYST_ADMIN
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, "Analyst Admin");
			pstmt.setString(2, "Active");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String email = rs.getString("email");
				list.add(email);
			}
			return list;
		} catch (Exception e) {
			throw new UserModelDaoException(e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	@Override
	public String getemailForCreatedby(String dataBaseName, String email) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String name = null;

		try {
			System.out.println("email id" + email);
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_EMAILFORCREATEDBY
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				name = rs.getString("createdBy");
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(rs, pstmt, connection);
		}
		return null;
	}

	@Override
	public String getroleFormodifiedBy(String modifiedBy, String dataBaseName) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
           pstmt=connection.prepareStatement(UserQueryConstant.SELECT_ROLE_FORMODIFIEDBY.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
           pstmt.setString(1, modifiedBy);
           rs=pstmt.executeQuery();
           String role=null;
           while(rs.next())
           {
        	   role=rs.getString("roleName");
           }
           return role;
	}
           catch (Exception e) {
			
		e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(rs, pstmt, connection);
		}
		return null;
	}

	@Override
	public ArrayList<String> getAllEmailForClientAdmin(UserEntity user, String dataBaseName) throws UserModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<String> emailList = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(UserQueryConstant.SELECT_EMAILID_CLIENT_ADMIN
					.replace(UserQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, "Active");
			result = pstmt.executeQuery();
			while (result.next()) {
				String emailIds = buildeMail(result);
				emailList.add(emailIds);
			}
			return emailList;
		} catch (Exception e) {

			throw new UserModelDaoException("unable to email id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
	}
	}

