package com.Anemoi.InvestorRelation.whitelabeling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import io.micronaut.http.annotation.Get;
import jakarta.inject.Singleton;

@Singleton
public class WhitelableingDaoImpl implements WhiteLableingDao {

	@Override
	public WhiteLableingEntity addcssFileAndLogoFile(WhiteLableingEntity entity, String dataBaseName) throws DaoException {
		
		Connection con=null;
		PreparedStatement psta=null;
		try
		{
			 boolean existslogo = this.checkLogoForClientName( entity.getClientName(),dataBaseName);
			 System.out.println("existslogo"+existslogo);
			con=InvestorDatabaseUtill.getConnection();
			if(existslogo)
			{
				System.out.println("update");
				Date d=new Date();
				psta=con.prepareStatement(WhiteLableingQueryConstant.UPDATE_DEATAILS.replace(WhiteLableingQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			   psta.setString(1, entity.getFileName());	
			   psta.setString(2, entity.getFileType());
			   psta.setBytes(3, entity.getFileData());
			   psta.setString(4, entity.getCssFileName());
			   psta.setString(5, entity.getCssFileType());
			   psta.setBytes(6, entity.getCssFileData());
			   psta.setString(7, entity.getCreatedBy());
			   psta.setLong(8, d.getTime());
			   psta.setString(9, entity.getClientName());
			   psta.executeUpdate();
				
			}
			else {
			
			psta=con.prepareStatement(WhiteLableingQueryConstant.INSERT_INTO_WHITELABLEING_TABLE.replace(WhiteLableingQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			String id=UUID.randomUUID().toString();
			entity.setWhitelableId(id);
			Date d=new Date();
			entity.setCreatedOn(d.getTime());
        psta.setString(1, id);	
        psta.setString(2, entity.getClientName());
       psta.setString(3,null);
       psta.setString(4, entity.getFileName());
       psta.setString(5, entity.getFileType());
       psta.setBytes(6, entity.getFileData());
        psta.setString(7, entity.getCssFileName());
        psta.setString(8, entity.getCssFileType());
        psta.setBytes(9, entity.getCssFileData());
        psta.setString(10, entity.getCreatedBy());
        psta.setLong(11, entity.getCreatedOn());
        psta.executeUpdate();
			}
        return entity;
		}catch (Exception e) {
		throw new DaoException("unable to add css file and logo"+e.getMessage());
		}
		 finally {
				
				InvestorDatabaseUtill.close(psta, con);
			}
		
	}

	private boolean checkLogoForClientName(String clientName, String dataBaseName) throws DaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			
			connection=InvestorDatabaseUtill.getConnection();
			pstmt=connection.prepareStatement(WhiteLableingQueryConstant.SELECT_LOGOFORCLIENT.replace(WhiteLableingQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, clientName);
           rs=pstmt.executeQuery();
           if(rs.next())
           {
        	   int count = rs.getInt(1);
        	   System.out.println("count"+count);
               return count > 0;
	            
	        }
	        return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());

		} finally {
			
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	@Override
	public ArrayList<WhiteLableingEntity> getlist(String dataBaseName) throws DaoException {

        Connection con=null;
        PreparedStatement psta=null;
        ResultSet rs=null;
        ArrayList<WhiteLableingEntity> list=new ArrayList<>();
        try
        {
        	con=InvestorDatabaseUtill.getConnection();
        	psta=con.prepareStatement(WhiteLableingQueryConstant.SELECT_WHITELABLEING_LIST.replace(WhiteLableingQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
        	rs=psta.executeQuery();
        	while(rs.next())
        	{
        		WhiteLableingEntity entity=this.buildData(rs);
        		list.add(entity);	
        	}
        	return list;
        }
        catch (Exception e) {

   throw new DaoException("unable to get"+e.getMessage());
		}
        finally {
			
			InvestorDatabaseUtill.close(psta, con);
		}
	}

	private WhiteLableingEntity buildData(ResultSet rs) throws SQLException {
		WhiteLableingEntity entity=new WhiteLableingEntity();
		entity.setWhitelableId(rs.getString("whitelableId"));
		entity.setClientName(rs.getString("clientName"));
	    entity.setFilePath(rs.getString("filePath"));
//		entity.setLogoFileData(rs.getBytes("logoFileData"));
	    entity.setFileName(rs.getString("fileName"));
	    entity.setFileType(rs.getString("fileType"));
	    entity.setFileData(rs.getBytes("fileData"));
		entity.setCssFileName(rs.getString("cssFileName"));
		entity.setCssFileType(rs.getString("cssFileType"));
		entity.setCreatedBy(rs.getString("createdBy"));
		entity.setCreatedOn(rs.getLong("createdOn"));
//		entity.setCssFileData(rs.getBytes("cssFileData"));
		return entity;
		
	}

	@Override
	public WhiteLableingEntity getById(String clientName, String dataBaseName) throws DaoException {
		Connection con=null;
        PreparedStatement psta=null;
        ResultSet rs=null;
      
        try
        {
        	con=InvestorDatabaseUtill.getConnection();
        	psta=con.prepareStatement(WhiteLableingQueryConstant.SELECT_WHITELABLEING_BYID.replace(WhiteLableingQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
        psta.setString(1, clientName);
        	rs=psta.executeQuery();
        	while(rs.next())
        	{
        		WhiteLableingEntity entity=this.buildData(rs);
        		return entity;
        	}
       
        }
        catch (Exception e) {
        	 throw new DaoException("unable to get"+e.getMessage());
	}finally {

		InvestorDatabaseUtill.close(psta, con);
	}
		return null;
	}
}
	


