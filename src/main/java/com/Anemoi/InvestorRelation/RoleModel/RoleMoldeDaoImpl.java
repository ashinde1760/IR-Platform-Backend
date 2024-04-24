package com.Anemoi.InvestorRelation.RoleModel;

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

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import jakarta.inject.Singleton;

@Singleton
public class RoleMoldeDaoImpl implements RoleModelDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleMoldeDaoImpl.class);

	@Override
	public RoleModelEntity createNewRolemodel(RoleModelEntity rolemodel, String dataBaseName)
			throws RoleModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			LOGGER.debug("inserting the data");

			pstmt = connection.prepareStatement(RoleModelQuaryContant.INSERT_INTO_ROLEMODEL
					.replace(RoleModelQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			String roleid = UUID.randomUUID().toString();
			Date date = new Date();
			rolemodel.setId(roleid);
			String id = rolemodel.getId();
			System.out.println(id + " " + rolemodel);
			pstmt.setString(1, id);
			pstmt.setString(2, rolemodel.getRoleName());
			pstmt.setString(3, rolemodel.getDescription());
			pstmt.setString(4, rolemodel.getStatus());
			pstmt.setString(5, rolemodel.getDashboardAccess().toString());
			pstmt.setLong(6, date.getTime());
			pstmt.setLong(7, date.getTime());

			pstmt.executeUpdate();
			return rolemodel;

		} catch (Exception e) {
			LOGGER.error("unable to  created *******************:");
			throw new RoleModelDaoException("unable to  create role " + e.getMessage());
		}

		finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public RoleModelEntity getRoleModelById(String id, String dataBaseName) throws RoleModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(RoleModelQuaryContant.SELECT_ROLEMODEL_BY_ID
					.replace(RoleModelQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, id);
			result = pstmt.executeQuery();
			while (result.next()) {
				RoleModelEntity rolemodelEntity = buildRoleModelDeatils(result);
				return rolemodelEntity;
			}
		} catch (Exception e) {
			LOGGER.error("role model not found" + e.getMessage());
			throw new RoleModelDaoException("unable to get role model by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	@Override
	public RoleModelEntity getRoleModelByRole(String role, String dataBaseName) throws RoleModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(RoleModelQuaryContant.SELECT_ROLEMODEL_BY_ROLE
					.replace(RoleModelQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, role);
			result = pstmt.executeQuery();
			while (result.next()) {

				RoleModelEntity rolemodelEntity = buildRoleModelDeatils(result);

				return rolemodelEntity;
			}
		} catch (Exception e) {
			LOGGER.error("role model not found" + e.getMessage());
			throw new RoleModelDaoException("unable to role model by rolename" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings("resource")
	@Override
	public List<RoleModelEntity> getAllRoleModelDetails(String dataBaseName) throws RoleModelDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<RoleModelEntity> listofrolemodelDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(RoleModelQuaryContant.SELECT_ROLEMODEL
					.replace(RoleModelQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				RoleModelEntity shareholder = buildRoleModelDeatils(result);
				listofrolemodelDetails.add(shareholder);
			}

			return listofrolemodelDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of role model" + e.getMessage());
			e.printStackTrace();
			throw new RoleModelDaoException("unable to get role model list" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	private RoleModelEntity buildRoleModelDeatils(ResultSet result) throws SQLException {

		RoleModelEntity rolemodeEntity = new RoleModelEntity();
		rolemodeEntity.setId(result.getString(RoleModelQuaryContant.ID));
		rolemodeEntity.setRoleName(result.getString(RoleModelQuaryContant.ROLENAME));
		rolemodeEntity.setDescription(result.getString(RoleModelQuaryContant.DESCRIPTION));
		rolemodeEntity.setStatus(result.getString(RoleModelQuaryContant.STATUS));
		ArrayList<String> vta = new ArrayList<>();
		vta.add(result.getString(RoleModelQuaryContant.DASHBOARDACCESS.toString()));
		int size = vta.size();
		String access = result.getString(RoleModelQuaryContant.DASHBOARDACCESS.toString());

		String[] array = access.split(",");
		// System.out.println("*" +array.length);
		rolemodeEntity.setDashboardAccess(vta);
		rolemodeEntity.setLastEdit(result.getLong(RoleModelQuaryContant.LASTEDIT));
		rolemodeEntity.setCreatedOn(result.getLong(RoleModelQuaryContant.CREATEDON));
		rolemodeEntity.setNoOfUser(result.getString(8));
		rolemodeEntity.setAccessItem(array.length);

		return rolemodeEntity;
	}

	@Override
	public RoleModelEntity updateRoleModelDetails(RoleModelEntity rolemodel, String id, String dataBaseName) {
		Connection connection = null;
		PreparedStatement pstmt = null;
//		LOGGER.info(".in update rolemodel database name is ::" + dataBaseName + " cashId is ::" + id
//				+ " request role model is ::" + rolemodel);

		try {

			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(RoleModelQuaryContant.UPDATE_ROLEMODEL
					.replace(RoleModelQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Date date = new Date();
			pstmt.setString(1, rolemodel.getRoleName());
			pstmt.setString(2, rolemodel.getDescription());
			pstmt.setString(3, rolemodel.getStatus());
			pstmt.setString(4, rolemodel.getDashboardAccess().toString());
			pstmt.setLong(5, date.getTime());

			pstmt.setString(6, id);

			int executeUpdate = pstmt.executeUpdate();

			System.out.println(executeUpdate);
			LOGGER.info(executeUpdate + " role model updated successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return rolemodel;
	}

	@Override
	public String deleteRoleModel(String id, String dataBaseName) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(RoleModelQuaryContant.DELETE_ROLEMODEL_BY_ID
					.replace(RoleModelQuaryContant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, id);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " role model deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

}
