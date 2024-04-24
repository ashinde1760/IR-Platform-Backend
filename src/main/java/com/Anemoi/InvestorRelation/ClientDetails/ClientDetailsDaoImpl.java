package com.Anemoi.InvestorRelation.ClientDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemDaoImpl;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemQueryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import jakarta.inject.Singleton;

@Singleton
public class ClientDetailsDaoImpl implements ClientDetailsDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalystLineItemDaoImpl.class);

	@SuppressWarnings("resource")
	@Override
	public ClientDetailsEntity addClientDetails(ClientDetailsEntity detailsEntity, String dataBaseName) throws ClientDaoException{
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet result = null;
		List<String> existingValues = new ArrayList<>();
		try {
			System.out.println("Client Name: ======> "+detailsEntity.getClientName());
			con = InvestorDatabaseUtill.getConnection();
			String clientName = detailsEntity.getClientName();
			psta = con.prepareStatement(ClientDetailsQueryConstant.SELECT_CLIENTNAME
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = psta.executeQuery();
			while (result.next()) {
				existingValues.add(result.getString("clientName"));
			}
			  boolean isDuplicate = existingValues.contains(clientName);
				boolean isMatched = existingValues.stream().anyMatch(clientName::equalsIgnoreCase);
	             if(!isDuplicate && isMatched == false)
	             {
	            	 Date d=new Date();
			psta = con.prepareStatement(ClientDetailsQueryConstant.INSERT_INTO_CLIENTDETAILS
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			String id = UUID.randomUUID().toString();
			detailsEntity.setClientId(id);
			psta.setString(1, detailsEntity.getClientId());
			psta.setInt(2, detailsEntity.getProjectCode());
			psta.setInt(3, detailsEntity.getTaskCode());
			psta.setString(4, detailsEntity.getClientName().trim());
			psta.setString(5, detailsEntity.getClientAddress());
			System.out.println("Client Name: ======> "+detailsEntity.getClientName()+detailsEntity.getEmailId());

			if(detailsEntity.getEmailId()==null)
			{
				psta.setString(6, null);
			}
			else {
			psta.setString(6, detailsEntity.getEmailId().toString());
			}
	
			psta.setString(7, detailsEntity.getIndustry());
			System.out.println("detailsEntity.getSuggestedPeers(=-=-=-=-=-="+detailsEntity.getSuggestedPeers());
			if(detailsEntity.getSuggestedPeers()==null)
			{
				System.out.println("inside if88888888888");
				psta.setString(8, null);
			}else {
			psta.setString(8, detailsEntity.getSuggestedPeers().toString());
			}
			psta.setString(9, detailsEntity.getAssignAA().toString());
			psta.setString(10, detailsEntity.getCreatedBy());
			psta.setLong(11, d.getTime());
			psta.setString(12, null);
			psta.setLong(13, d.getTime());
			psta.executeUpdate();
			}
			else
			{
				throw new ClientDaoException("Cannot insert Duplicate ClientName");
			}
	             System.out.println("execution of query---===>");
			return detailsEntity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientDaoException(e.getMessage());
		}
		finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, con);

		}
	

	}

	@Override
	public ArrayList<ClientDetailsEntity> getAllClientDetails(String dataBaseName) throws ClientDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<ClientDetailsEntity> clientDetailsList = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientDetailsQueryConstant.SELECT_CLIENTDETAILS
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				ClientDetailsEntity entity = buildData(rs);
				clientDetailsList.add(entity);
			}
			return clientDetailsList;
		} catch (Exception e) {
			throw new ClientDaoException("unable to get client details"+e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(psta, con);
			}
	
	}

	private ClientDetailsEntity buildData(ResultSet rs) throws SQLException {
		ClientDetailsEntity entity = new ClientDetailsEntity();
		entity.setClientId(rs.getString("clientId"));
		entity.setProjectCode(rs.getInt("projectCode"));
		entity.setTaskCode(rs.getInt("taskCode"));
		entity.setClientName(rs.getString("clientName"));
		entity.setClientAddress(rs.getString("clientAddress"));
		
		if(rs.getString("emailId")!=null) {
		 String participantField = rs.getString("emailId");
		    participantField = participantField.substring(1, participantField.length() - 1); // Remove the square brackets
		    String[] participants = participantField.split(", ");
		    ArrayList<String> participantList = new ArrayList<>(Arrays.asList(participants));
		    entity.setEmailId(participantList);
		}
		else {
			entity.setEmailId(null);
		}
		entity.setIndustry(rs.getString("industry"));
		
		if(rs.getString("suggestedPeers")!=null) {
		 String suggestedPeers = rs.getString("suggestedPeers");
		 suggestedPeers = suggestedPeers.substring(1, suggestedPeers.length() - 1); // Remove the square brackets
		    String[] suggestedPeer = suggestedPeers.split(", ");
		    ArrayList<String> suggestedPeerlist = new ArrayList<>(Arrays.asList(suggestedPeer));
		    entity.setSuggestedPeers(suggestedPeerlist);
		}else {
		    entity.setSuggestedPeers(null);

		}
		    String assignAA = rs.getString("assignAA");
		    assignAA = assignAA.substring(1, assignAA.length() - 1); // Remove the square brackets
			    String[] assignAAs = assignAA.split(", ");
			    ArrayList<String> assignAAlist = new ArrayList<>(Arrays.asList(assignAAs));
			    entity.setAssignAA(assignAAlist);
			    entity.setCreatedBy(rs.getString("createdBy"));
			    entity.setCreatedOn(rs.getLong("createdOn"));
			    entity.setModifiedBy(rs.getString("modifiedBy"));
			    entity.setModifiedOn(rs.getLong("modifiedOn"));
		return entity;

	}

	@Override
	public ClientDetailsEntity getclientDetailsByprojectCode(int projectCode,String dataBaseName) throws ClientDaoException{

		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientDetailsQueryConstant.SELECT_CLIENTDETAILS_BYPROJECTCODE
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setInt(1, projectCode);
			
			rs = psta.executeQuery();
			while (rs.next()) {
				ClientDetailsEntity enity = buildClientDetailsByprojectCode(rs);
				return enity;
			}

		} catch (Exception e) {
			throw new ClientDaoException("unable to get project code and task code"+e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(psta, con);
			}
		return null;
	}

	private ClientDetailsEntity buildClientDetailsByprojectCode(ResultSet rs) throws SQLException {
		ClientDetailsEntity entity = new ClientDetailsEntity();
		entity.setProjectCode(rs.getInt("projectCode"));
		entity.setTaskCode(rs.getInt("taskCode"));
		entity.setClientName(rs.getString("clientName"));
		entity.setClientAddress(rs.getString("clientAddress"));
		return entity;
	}

	@Override
	public ClientDetailsEntity addProjectCodeFor(ClientDetailsEntity clientDetailsEntity, String dataBaseName) {

		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientDetailsQueryConstant.INSERT_CLIENTDETAILS_BYPROJECTCODE
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setInt(1, clientDetailsEntity.getProjectCode());
			psta.setInt(2, clientDetailsEntity.getTaskCode());
			psta.setString(3, clientDetailsEntity.getClientName());
			psta.setString(4, clientDetailsEntity.getClientAddress());
			psta.executeUpdate();
			return clientDetailsEntity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(psta, con);
			}
		return null;

	}

	@SuppressWarnings("resource")
	@Override
	public ClientDetailsEntity updateClientDetails(String clientId, ClientDetailsEntity clientDetailsEntity,
			String dataBaseName) throws ClientDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet result = null;
		List<String> existingValues = new ArrayList<>();
		Date d=new Date();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientDetailsQueryConstant.UPDATE_CLIENTDETAILS
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientDetailsEntity.getClientAddress());
		
			if(clientDetailsEntity.getEmailId()==null) {
				psta.setString(2, null);
			}
			else {
				psta.setString(2, clientDetailsEntity.getEmailId().toString());
			}
			psta.setString(3, clientDetailsEntity.getIndustry());
			if(clientDetailsEntity.getSuggestedPeers()==null)
			{
				psta.setString(4, null);
			}else {
			psta.setString(4, clientDetailsEntity.getSuggestedPeers().toString());
			}
			psta.setString(5, clientDetailsEntity.getAssignAA().toString());
			psta.setString(6, clientDetailsEntity.getModifiedBy());
			psta.setLong(7, d.getTime());
			psta.setString(8, clientId);
			psta.executeUpdate();
		
			return clientDetailsEntity;
	}catch (Exception e) {
		// TODO: handle exception
		throw new ClientDaoException(e.getMessage());
	}
		finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, con);

		}
	
	

}

	@Override
	public void deleteClientDetails(String clientId, String dataBaseName) throws ClientDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientDetailsQueryConstant.DELETE_CLIENTDETAILS
					.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setString(1, clientId);
		psta.executeUpdate();
	}catch (Exception e) {
		throw new ClientDaoException("unable to delete client details"+e.getMessage());
	}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(psta, con);
			}
}

	@Override
	public ClientDetailsEntity getClientDataByclientId(String clientId, String dataBaseName) throws ClientDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs=null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(ClientDetailsQueryConstant.SELECT_CLIENTDETAILS_BYCLIENTID.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientId);
			rs=psta.executeQuery();
			while(rs.next())
			{
			
				ClientDetailsEntity entity=this.buildData(rs);
				return entity;
			}
		}catch (Exception e) {
			throw new ClientDaoException("unable to get client details by client id"+e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(psta, con);
			}
		return null;
	}

	@Override
	public ClientDetailsEntity getDetailsByClientName(String clientName, String dataBaseName)
			throws ClientDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs=null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(ClientDetailsQueryConstant.SELECT_CLIENTDETAILS_BYCLIENTNAME.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientName);
			rs=psta.executeQuery();
			while(rs.next())
			{
			
				ClientDetailsEntity entity=this.buildData(rs);
				return entity;
			}
		}catch (Exception e) {
			throw new ClientDaoException("unable to get client details by client name"+e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(psta, con);
			}
		return null;
	}

	@Override
	public String getclientNameByid(String clientId, String dataBaseName) throws ClientDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		String clientName=null;
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(ClientDetailsQueryConstant.SELECT_CLIENTNAME_BYCLIENTNAME.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientId);
			rs=psta.executeQuery();
			while(rs.next())
			{
				clientName=rs.getString("clientName");
			}
			return clientName;
		}
		catch (Exception e) {
			throw new ClientDaoException("unable to get clientname"+e.getMessage());
		}
		 finally {

				InvestorDatabaseUtill.close(psta, con);
			}
		
	}

	@Override
	public void deleteClientForNonProcessingtable(String clientName, String dataBaseName) throws ClientDaoException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
           try
           {
        	con=InvestorDatabaseUtill.getConnection();
        	psta=con.prepareStatement(ClientDetailsQueryConstant.DELETE_CLIENTDETAILSFOR_NNPROCESS.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
        	psta.setString(1, clientName);
        	psta.executeUpdate();
           }
           catch (Exception e) {
   			throw new ClientDaoException("unable to delete"+e.getMessage());
   		} finally {

			InvestorDatabaseUtill.close(psta, con);
		}
   
	}
	public ArrayList<String> getAnalystAdminForClientName(String clientName, String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet result = null;
	ArrayList<String> assignAAlist=new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientDetailsQueryConstant.SELECT_ANALSYSTADMIN.replace(ClientDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
	   psta.setString(1, clientName);
	   result=psta.executeQuery();
	   while(result.next())
	   {
		   String assignAA = result.getString("assignAA");
		    assignAA = assignAA.substring(1, assignAA.length() - 1); // Remove the square brackets
			    String[] assignAAs = assignAA.split(", ");
			   assignAAlist = new ArrayList<>(Arrays.asList(assignAAs));
			
		    }
	   return assignAAlist;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
		
	}
	
	}

