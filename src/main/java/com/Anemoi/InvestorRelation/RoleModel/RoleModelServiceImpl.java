package com.Anemoi.InvestorRelation.RoleModel;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class RoleModelServiceImpl implements RoleModelService {

	@Inject
	private RoleModelDao rolemodeldao;

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	private static String DATABASENAME = "databaseName";

	private static String dataBaseName() {

		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;

	}

	@Override
	public RoleModelEntity createRoleModel(RoleModelEntity rolemodel) throws RoleModelServiceException {
		try {
			String dataBaseName = RoleModelServiceImpl.dataBaseName();

			// applyValidation(rolemodel);

			RoleModelEntity createNewShareHoldercontact = this.rolemodeldao.createNewRolemodel(rolemodel, dataBaseName);
			return createNewShareHoldercontact;
		} catch (Exception e) {

			throw new RoleModelServiceException("Unable to create role " + e.getMessage());
		}
	}

	private void applyValidation(RoleModelEntity rolemodel) throws Exception {
		// TODO Auto-generated method stub
		if (rolemodel.getRoleName() == null || rolemodel.getRoleName().isEmpty()) {
			throw new Exception("role name should not be null or empty");
		}
		if (rolemodel.getDescription() == null || rolemodel.getDescription().isEmpty()) {
			throw new Exception("description should not be null or empty");
		}
		if (rolemodel.getStatus() == null || rolemodel.getStatus().isEmpty()) {
			throw new Exception("status should not be null or empty");
		}
		if (rolemodel.getDashboardAccess() == null || rolemodel.getDashboardAccess().isEmpty()) {
			throw new Exception("Dashboard access should not be null or empty");
		}

	}

	@Override
	public RoleModelEntity getRoleModelById(String id) throws RoleModelServiceException {
		try {
			String dataBaseName = RoleModelServiceImpl.dataBaseName();
			if (id == null || id.isEmpty()) {
				System.out.print("Rolemodel id must not be null or empty");
			}
			RoleModelEntity rolemodelEntity = this.rolemodeldao.getRoleModelById(id, dataBaseName);
			return rolemodelEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelServiceException("unable to get role model by id" + e.getMessage());
		}

	}

	@Override
	public RoleModelEntity getRoleModelByRole(String role) throws RoleModelServiceException {
		try {
			String dataBaseName = RoleModelServiceImpl.dataBaseName();
			if (role == null || role.isEmpty()) {
				System.out.print("Rolemodel rolename must not be null or empty");
			}
			RoleModelEntity rolemodelEntity = this.rolemodeldao.getRoleModelByRole(role, dataBaseName);
			return rolemodelEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelServiceException("unable to role model by role name" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleModelEntity> getAllRoleModelDetails() throws RoleModelServiceException {
		try {
			String dataBaseName = RoleModelServiceImpl.dataBaseName();
			List<RoleModelEntity> rolemodelEntity = this.rolemodeldao.getAllRoleModelDetails(dataBaseName);
			JSONObject object = new JSONObject();
			JSONArray rolemodelJsonData = getJSONFromRoleModelList(rolemodelEntity);
			object.put(rolemodelEntity, rolemodelJsonData);
			return rolemodelEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelServiceException("unable to get role model list" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private JSONArray getJSONFromRoleModelList(List<RoleModelEntity> rolemodelEntity) {
		JSONArray array = new JSONArray();
		for (RoleModelEntity rolemodel : rolemodelEntity) {
			JSONObject object = buildJsonRoleModel(rolemodel);
			array.add(object);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonRoleModel(RoleModelEntity rolemodel) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(RoleModelQuaryContant.ID, rolemodel.getId());
		jsonObject.put(RoleModelQuaryContant.ROLENAME, rolemodel.getRoleName());
		jsonObject.put(RoleModelQuaryContant.DESCRIPTION, rolemodel.getDescription());
		jsonObject.put(RoleModelQuaryContant.STATUS, rolemodel.getStatus());
		jsonObject.put(RoleModelQuaryContant.DASHBOARDACCESS, rolemodel.getDashboardAccess());
		jsonObject.put(RoleModelQuaryContant.CREATEDON, rolemodel.getCreatedOn());

		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RoleModelEntity updateRoleModel(RoleModelEntity rolemodel, String id) throws RoleModelServiceException {
		try {
			String dataBaseName = RoleModelServiceImpl.dataBaseName();
			if (id == null || id.isEmpty()) {
				System.out.println("role model id can't be null or empty ");

			}
			RoleModelEntity rolemodel1 = this.rolemodeldao.updateRoleModelDetails(rolemodel, id, dataBaseName);
			JSONObject object = new JSONObject();
			JSONObject jsonFromcontact = buildJsonRoleModel(rolemodel1);
			object.put(rolemodel1, jsonFromcontact);
			return rolemodel1;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelServiceException("unable to update role model details" + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public RoleModelEntity deleteRolemodel(String userid) throws RoleModelServiceException {
		try {
			String dataBaseName = RoleModelServiceImpl.dataBaseName();
			if (userid == null || userid.isEmpty()) {
				System.out.println("role model cannot be null or empty");

			}
			RoleModelEntity rolemodel = rolemodeldao.getRoleModelById(userid, dataBaseName);
			if (rolemodel == null) {

			}
			this.rolemodeldao.deleteRoleModel(userid, dataBaseName);
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "role model deleted suucessfully");
			return rolemodel;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelServiceException("unable to delete role model" + e.getMessage());
		}

	}

}
