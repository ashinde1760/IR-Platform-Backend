package com.Anemoi.InvestorRelation.RoleModel;

import java.sql.SQLException;
import java.util.List;

public interface RoleModelDao {

	RoleModelEntity createNewRolemodel(RoleModelEntity rolemodel, String dataBaseName) throws RoleModelDaoException;

	RoleModelEntity getRoleModelById(String id, String dataBaseName) throws RoleModelDaoException;

	RoleModelEntity getRoleModelByRole(String role, String dataBaseName) throws RoleModelDaoException;

	List<RoleModelEntity> getAllRoleModelDetails(String dataBaseName) throws RoleModelDaoException;

	RoleModelEntity updateRoleModelDetails(RoleModelEntity rolemodel, String id, String dataBaseName);

	String deleteRoleModel(String id, String dataBaseName) throws SQLException;

}
