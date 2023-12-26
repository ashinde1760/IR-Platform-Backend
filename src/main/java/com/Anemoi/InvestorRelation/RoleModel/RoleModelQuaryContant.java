package com.Anemoi.InvestorRelation.RoleModel;

public class RoleModelQuaryContant {

	public static final String DATA_BASE_PLACE_HOLDER = "#$DataBaseName#$";

	public static final String ID = "id";

	public static final String ROLENAME = "roleName";

	public static final String DESCRIPTION = "description";

	public static final String STATUS = "status";

	public static final String DASHBOARDACCESS = "dashboardAccess";

	public static final String LASTEDIT = "lastEdit";

	public static final String CREATEDON = "createdOn";

	public static final String noOfuser = "noOfUser";

	public static final String INSERT_INTO_ROLEMODEL = "INSERT INTO INV_RELATIONS.dev.rolemodel values(?,?,?,?,?,?,?)";

	public static final String SELECT_ROLEMODEL_BY_ID = "select  INV_RELATIONS.dev.rolemodel.*,count(#$DataBaseName#$.dev.userTable.roleName) as number_of_user from INV_RELATIONS.dev.rolemodel left join INV_RELATIONS.dev.userTable on (#$DataBaseName#$.dev.rolemodel.roleName=#$DataBaseName#$.dev.userTable.roleName) where id=?  group by INV_RELATIONS.dev.rolemodel.id,#$DataBaseName#$.dev.rolemodel.roleName,  INV_RELATIONS.dev.rolemodel.description,#$DataBaseName#$.dev.rolemodel.status,#$DataBaseName#$.dev.rolemodel.dashboardAccess,#$DataBaseName#$.dev.rolemodel.lastEdit,#$DataBaseName#$.dev.rolemodel.createdOn";

	public static final String SELECT_ROLEMODEL_BY_ROLE = "select  INV_RELATIONS.dev.rolemodel.*,count(#$DataBaseName#$.dev.userTable.roleName) as number_of_user from INV_RELATIONS.dev.rolemodel left join INV_RELATIONS.dev.userTable on (#$DataBaseName#$.dev.rolemodel.roleName=#$DataBaseName#$.dev.userTable.roleName) where INV_RELATIONS.dev.rolemodel.roleName=?  group by INV_RELATIONS.dev.rolemodel.id,#$DataBaseName#$.dev.rolemodel.roleName,  INV_RELATIONS.dev.rolemodel.description,#$DataBaseName#$.dev.rolemodel.status,#$DataBaseName#$.dev.rolemodel.dashboardAccess,#$DataBaseName#$.dev.rolemodel.lastEdit,#$DataBaseName#$.dev.rolemodel.createdOn";

	public static final String SELECT_ROLEMODEL = "select  INV_RELATIONS.dev.rolemodel.*,count(#$DataBaseName#$.dev.userTable.roleName) as number_of_user from INV_RELATIONS.dev.rolemodel left join INV_RELATIONS.dev.userTable on (#$DataBaseName#$.dev.rolemodel.roleName=#$DataBaseName#$.dev.userTable.roleName)  group by INV_RELATIONS.dev.rolemodel.id,#$DataBaseName#$.dev.rolemodel.roleName,  INV_RELATIONS.dev.rolemodel.description,#$DataBaseName#$.dev.rolemodel.status,#$DataBaseName#$.dev.rolemodel.dashboardAccess,#$DataBaseName#$.dev.rolemodel.lastEdit,#$DataBaseName#$.dev.rolemodel.createdOn";

	public static final String UPDATE_ROLEMODEL = "UPDATE INV_RELATIONS.dev.rolemodel SET roleName=?, description=?, status=?,dashboardAccess=?,lastEdit=? WHERE id=?";

	public static final String DELETE_ROLEMODEL_BY_ID = " DELETE INV_RELATIONS.dev.rolemodel where id=?";

	// public static final String SELECT_NOOFUSER_BYROLE= "SELECT
	// roleName,COUNT(roleName)as numberofUser from INV_RELATIONS.dev.user1 group
	// by roleName";

}
