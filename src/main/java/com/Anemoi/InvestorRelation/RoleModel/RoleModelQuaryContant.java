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

	public static final String INSERT_INTO_ROLEMODEL = "INSERT INTO #$DataBaseName#$.dev.rolemodel values(?,?,?,?,?,?,?)";

	public static final String SELECT_ROLEMODEL_BY_ID = "select  #$DataBaseName#$.dev.rolemodel.*,count(#$DataBaseName#$.dev.userTable.roleName) as number_of_user from #$DataBaseName#$.dev.rolemodel left join #$DataBaseName#$.dev.userTable on (#$DataBaseName#$.dev.rolemodel.roleName=#$DataBaseName#$.dev.userTable.roleName) where id=?  group by #$DataBaseName#$.dev.rolemodel.id,#$DataBaseName#$.dev.rolemodel.roleName,  #$DataBaseName#$.dev.rolemodel.description,#$DataBaseName#$.dev.rolemodel.status,#$DataBaseName#$.dev.rolemodel.dashboardAccess,#$DataBaseName#$.dev.rolemodel.lastEdit,#$DataBaseName#$.dev.rolemodel.createdOn";

	public static final String SELECT_ROLEMODEL_BY_ROLE = "select  #$DataBaseName#$.dev.rolemodel.*,count(#$DataBaseName#$.dev.userTable.roleName) as number_of_user from #$DataBaseName#$.dev.rolemodel left join #$DataBaseName#$.dev.userTable on (#$DataBaseName#$.dev.rolemodel.roleName=#$DataBaseName#$.dev.userTable.roleName) where #$DataBaseName#$.dev.rolemodel.roleName=?  group by #$DataBaseName#$.dev.rolemodel.id,#$DataBaseName#$.dev.rolemodel.roleName,  #$DataBaseName#$.dev.rolemodel.description,#$DataBaseName#$.dev.rolemodel.status,#$DataBaseName#$.dev.rolemodel.dashboardAccess,#$DataBaseName#$.dev.rolemodel.lastEdit,#$DataBaseName#$.dev.rolemodel.createdOn";

	public static final String SELECT_ROLEMODEL = "select  #$DataBaseName#$.dev.rolemodel.*,count(#$DataBaseName#$.dev.userTable.roleName) as number_of_user from #$DataBaseName#$.dev.rolemodel left join #$DataBaseName#$.dev.userTable on (#$DataBaseName#$.dev.rolemodel.roleName=#$DataBaseName#$.dev.userTable.roleName)  group by #$DataBaseName#$.dev.rolemodel.id,#$DataBaseName#$.dev.rolemodel.roleName,  #$DataBaseName#$.dev.rolemodel.description,#$DataBaseName#$.dev.rolemodel.status,#$DataBaseName#$.dev.rolemodel.dashboardAccess,#$DataBaseName#$.dev.rolemodel.lastEdit,#$DataBaseName#$.dev.rolemodel.createdOn";

	public static final String UPDATE_ROLEMODEL = "UPDATE #$DataBaseName#$.dev.rolemodel SET roleName=?, description=?, status=?,dashboardAccess=?,lastEdit=? WHERE id=?";

	public static final String DELETE_ROLEMODEL_BY_ID = " DELETE #$DataBaseName#$.dev.rolemodel where id=?";

	// public static final String SELECT_NOOFUSER_BYROLE= "SELECT
	// roleName,COUNT(roleName)as numberofUser from #$DataBaseName#$.dev.user1 group
	// by roleName";

}
