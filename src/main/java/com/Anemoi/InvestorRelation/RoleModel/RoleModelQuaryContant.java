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

	public static final String INSERT_INTO_ROLEMODEL = "INSERT INTO #$DataBaseName#$.dbo.rolemodel values(?,?,?,?,?,?,?)";

	public static final String SELECT_ROLEMODEL_BY_ID = "select  #$DataBaseName#$.dbo.rolemodel.*,count(#$DataBaseName#$.dbo.userTable.roleName) as number_of_user from #$DataBaseName#$.dbo.rolemodel left join #$DataBaseName#$.dbo.userTable on (#$DataBaseName#$.dbo.rolemodel.roleName=#$DataBaseName#$.dbo.userTable.roleName) where id=?  group by #$DataBaseName#$.dbo.rolemodel.id,#$DataBaseName#$.dbo.rolemodel.roleName,  #$DataBaseName#$.dbo.rolemodel.description,#$DataBaseName#$.dbo.rolemodel.status,#$DataBaseName#$.dbo.rolemodel.dashboardAccess,#$DataBaseName#$.dbo.rolemodel.lastEdit,#$DataBaseName#$.dbo.rolemodel.createdOn";

	public static final String SELECT_ROLEMODEL_BY_ROLE = "select  #$DataBaseName#$.dbo.rolemodel.*,count(#$DataBaseName#$.dbo.userTable.roleName) as number_of_user from #$DataBaseName#$.dbo.rolemodel left join #$DataBaseName#$.dbo.userTable on (#$DataBaseName#$.dbo.rolemodel.roleName=#$DataBaseName#$.dbo.userTable.roleName) where #$DataBaseName#$.dbo.rolemodel.roleName=?  group by #$DataBaseName#$.dbo.rolemodel.id,#$DataBaseName#$.dbo.rolemodel.roleName,  #$DataBaseName#$.dbo.rolemodel.description,#$DataBaseName#$.dbo.rolemodel.status,#$DataBaseName#$.dbo.rolemodel.dashboardAccess,#$DataBaseName#$.dbo.rolemodel.lastEdit,#$DataBaseName#$.dbo.rolemodel.createdOn";

	public static final String SELECT_ROLEMODEL = "select  #$DataBaseName#$.dbo.rolemodel.*,count(#$DataBaseName#$.dbo.userTable.roleName) as number_of_user from #$DataBaseName#$.dbo.rolemodel left join #$DataBaseName#$.dbo.userTable on (#$DataBaseName#$.dbo.rolemodel.roleName=#$DataBaseName#$.dbo.userTable.roleName)  group by #$DataBaseName#$.dbo.rolemodel.id,#$DataBaseName#$.dbo.rolemodel.roleName,  #$DataBaseName#$.dbo.rolemodel.description,#$DataBaseName#$.dbo.rolemodel.status,#$DataBaseName#$.dbo.rolemodel.dashboardAccess,#$DataBaseName#$.dbo.rolemodel.lastEdit,#$DataBaseName#$.dbo.rolemodel.createdOn";

	public static final String UPDATE_ROLEMODEL = "UPDATE #$DataBaseName#$.dbo.rolemodel SET roleName=?, description=?, status=?,dashboardAccess=?,lastEdit=? WHERE id=?";

	public static final String DELETE_ROLEMODEL_BY_ID = " DELETE #$DataBaseName#$.dbo.rolemodel where id=?";

	// public static final String SELECT_NOOFUSER_BYROLE= "SELECT
	// roleName,COUNT(roleName)as numberofUser from #$DataBaseName#$.dbo.user1 group
	// by roleName";

}
