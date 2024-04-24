package com.Anemoi.InvestorRelation.NotificationHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.RoleModel.RoleModelQuaryContant;

import jakarta.inject.Singleton;

@Singleton
public class NotificationDaoImli implements NotificationDao {

	@Override
	public void addNotification(NotificationEntity entity, String dataBaseName) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(NotificationQueryConstant.INSERT_NOTIFICATION_TABLE
					.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, entity.getMessage());
			psta.setString(2, entity.getUsers().toString());
			psta.setLong(3, entity.getCreatedOn());
			psta.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		finally {
			InvestorDatabaseUtill.close(psta, con);
		}
	}

	@Override
	public ArrayList<NotificationEntity> getNotificationHistory(String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<NotificationEntity> list = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(NotificationQueryConstant.SELECT_NOTIFICATION_HISTORY
					.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				NotificationEntity entity = bulidData(rs);
				list.add(entity);

			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	private NotificationEntity bulidData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		NotificationEntity entity = new NotificationEntity();
		entity.setnId(rs.getLong("nId"));
		entity.setMessage(rs.getString("message"));

		String participantField = rs.getString("users");
		participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
		String[] participants = participantField.split(", ");
		ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));
		entity.setUsers(participantList);

		entity.setCreatedOn(rs.getLong("createdOn"));
		return entity;
	}

	@Override
	public String clearById(long nId, String userEmail, String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;

		try {
			System.out.println("userEmail" + userEmail);

			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(NotificationQueryConstant.SELECT_ARRAYLIST_USER
					.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, nId);
			rs = psta.executeQuery();
			while (rs.next()) {
				ArrayList<String> userName = new ArrayList<>();
				userName = this.buldResultSet(rs);
				System.out.println("userName" + userName);
				userName.remove(userEmail);
				System.out.println("userName2" + userName);
				psta = con.prepareStatement(NotificationQueryConstant.UPDATE_UAERLIST
						.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				psta.setString(1, userName.toString());
				psta.setLong(2, nId);
				psta.executeUpdate();
			}

			return "Clear Notification";
		} catch (Exception e) {
			// TODO: handle exception
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;

	}

	private ArrayList<String> buldResultSet(ResultSet rs) throws SQLException {
		String participantField = rs.getString("users");
		participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
		String[] participants = participantField.split(", ");
		ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));
		return participantList;

	}

	@Override
	public String clearall(String userEmail, String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;

		try {
			System.out.println("userEmail" + userEmail);
			System.out.println("dataBaseName" + dataBaseName);

			con = InvestorDatabaseUtill.getConnection();
//			psta = con.prepareStatement(NotificationQueryConstant.SELECT_NOTIFICATION_HISTORY
//					.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta = con.prepareStatement(NotificationQueryConstant.UPDATE_UAERLIST
					.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1,userEmail);
			psta.setString(2,"%"+userEmail+"%");
			psta.executeUpdate();

//			rs = psta.executeQuery();
//			while (rs.next()) {
//
//				this.removeUser(rs, userEmail, psta, con, dataBaseName);
//			}

			return "Clear All Notification";
		} catch (Exception e) {
			// TODO: handle exception
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	private void removeUser(ResultSet rs, String userEmail, PreparedStatement psta, Connection con, String dataBaseName)
			throws SQLException {
		try {
		long nid = rs.getLong("nId");
		String participantField = rs.getString("users");
		participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
		String[] participants = participantField.split(", ");
		ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));

		System.out.println("participantList" + participantList);
		System.out.println("nid" + nid);
		participantList.remove(userEmail);
		System.out.println("after remove" + participantList);
		psta = con.prepareStatement(NotificationQueryConstant.UPDATE_UAERLIST
				.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setString(1, participantList.toString());
		psta.setLong(2, nid);
		psta.executeUpdate();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public long getnotificationCount(String userEmail, String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<>();
		long count = 0;
		try {
			System.out.println("userEmail" + userEmail);

			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(NotificationQueryConstant.SELECT_NOTIFICATION_HISTORY
					.replace(NotificationQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {

				list = this.getcount(rs, userEmail, psta, con, dataBaseName);

				if (list.contains(userEmail)) {
					count++;
				}
			}
			System.out.println("count" + count);
			return count;

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			InvestorDatabaseUtill.close(psta, con);
		}

		return 0;

	}

	private ArrayList<String> getcount(ResultSet rs, String userEmail, PreparedStatement psta, Connection con,
			String dataBaseName) throws SQLException {

		String participantField = rs.getString("users");
		participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
		String[] participants = participantField.split(", ");
		ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));
		return participantList;

	}
}
