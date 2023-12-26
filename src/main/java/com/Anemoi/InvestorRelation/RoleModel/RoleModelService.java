package com.Anemoi.InvestorRelation.RoleModel;

import java.sql.SQLException;
import java.util.List;

public interface RoleModelService {

	RoleModelEntity createRoleModel(RoleModelEntity rolemodel) throws RoleModelServiceException;

	RoleModelEntity getRoleModelById(String id) throws RoleModelServiceException;

	RoleModelEntity getRoleModelByRole(String role) throws RoleModelServiceException;

	List<RoleModelEntity> getAllRoleModelDetails() throws RoleModelServiceException;

	RoleModelEntity updateRoleModel(RoleModelEntity rolemodel, String id) throws RoleModelServiceException;

	RoleModelEntity deleteRolemodel(String id) throws RoleModelServiceException;

}
