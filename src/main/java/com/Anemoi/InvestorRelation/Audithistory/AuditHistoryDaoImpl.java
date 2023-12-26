package com.Anemoi.InvestorRelation.Audithistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import jakarta.inject.Singleton;

@Singleton
public class AuditHistoryDaoImpl implements AuditHistoryDao{

	@SuppressWarnings("resource")
	@Override
	public void addAuditHistoryDetails(AuditHistoryEntity auditHistoryEntity, String dataBaseName) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		Date d=new Date();
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(QueryConstant.SELECT_USERNAME.replace(QueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, auditHistoryEntity.getCreatedBy());
			rs=psta.executeQuery();
			while(rs.next())
			{
//				String firstName=rs.getString("firstName");
//				String lastName=rs.getString("lastName");
			String	userName=rs.getString("firstName")+" "+rs.getString("lastName");
				System.out.println("userName: "+userName);
				auditHistoryEntity.setUserName(userName);
				
			}
			psta=con.prepareStatement(QueryConstant.INSERT_AUDITHISTORY_TABLE.replace(QueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, auditHistoryEntity.getActivity());
			psta.setString(2, auditHistoryEntity.getUserName());
			psta.setString(3, auditHistoryEntity.getCreatedBy());
			psta.setString(4, auditHistoryEntity.getDescription());
			psta.setLong(5, d.getTime());
			psta.executeUpdate();
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 finally {
		
				InvestorDatabaseUtill.close(psta, con);
			}
		
	}

	@Override
	public ArrayList<AuditHistoryEntity> getAuditHistory(String dataBaseName) {
		
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		ArrayList<AuditHistoryEntity> list=new ArrayList<>();
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(QueryConstant.SELECT_AUDITHISTORY.replace(QueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs=psta.executeQuery();
			while(rs.next())
			{
				AuditHistoryEntity entity=buildData(rs);
				list.add(entity);
			}
			return list;
				
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 finally {
				
				InvestorDatabaseUtill.close(psta, con);
			}
		
		
		return null;
	}

	private AuditHistoryEntity buildData(ResultSet rs) throws SQLException {
		AuditHistoryEntity e=new AuditHistoryEntity();
		e.setAuditId(rs.getLong("auditId"));
		e.setActivity(rs.getString("activity"));
		e.setUserName(rs.getString("userName"));
		e.setCreatedBy(rs.getString("createdBy"));
		e.setDescription(rs.getString("description"));
		e.setCreatedOn(rs.getLong("createdOn"));
		return e;
		

	}

}
