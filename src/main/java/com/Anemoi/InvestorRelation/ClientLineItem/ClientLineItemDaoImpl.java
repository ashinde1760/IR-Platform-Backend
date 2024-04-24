package com.Anemoi.InvestorRelation.ClientLineItem;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemDaoException;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemDaoImpl;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemEntity;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemQueryConstant;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemServiceException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoException;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.DataIngestion.DataIngestionDaoException;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton
public class ClientLineItemDaoImpl implements ClientLineItemDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientLineItemDaoImpl.class);

	@SuppressWarnings("resource")
	@Override
	public ClientLineItemEntity createClientLineItem(ClientLineItemEntity clientLineitem, String dataBaseName)
			throws ClientLineItemDaoException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet result = null;
		List<String> existingValues = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			String clientName = clientLineitem.getClientName();
			psta = con.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTTLINETEMNAME
					.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientName);
			result = psta.executeQuery();
			while (result.next()) {
				existingValues.add(result.getString("clientLineItemName"));
			}
			boolean isMatched = existingValues.stream()
					.anyMatch(clientLineitem.getClientLineItemName()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(clientLineitem.getClientLineItemName())) {
				psta = con.prepareStatement(ClientLineItemQueryConstant.INSERT_INTO_CLIENTLINE_ITEM
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				Date d = new Date();
				String id = UUID.randomUUID().toString();
				clientLineitem.setClientLineId(id);
				psta.setString(1, id);
				psta.setString(2, clientLineitem.getClientName());
				psta.setString(3, clientLineitem.getLineItemName());
				psta.setString(4, clientLineitem.getClientLineItemName());
				psta.setString(5, clientLineitem.getClientTableHeaderName());
				psta.setString(6, clientLineitem.getMasterTableSource());
				psta.setLong(7, d.getTime());
                psta.setString(8, clientLineitem.getCreatedBy());
				psta.executeUpdate();

			} else {
				System.out.println("duplidate");
				throw new ClientLineItemDaoException("Cannot insert duplicate values");

			}
			return clientLineitem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemDaoException(e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public ClientLineItemEntity getClientLineItemById(String clientLineId, String dataBaseName)
			throws ClientLineItemDaoException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTLINE_ITEM_BYID
					.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientLineId);
			rs = psta.executeQuery();
			while (rs.next()) {
				ClientLineItemEntity entity = bulidData(rs);
				return entity;
			}
		} catch (Exception e) {
			throw new ClientLineItemDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(rs, psta, con);
		}
		return null;
	}

	private ClientLineItemEntity bulidData(ResultSet rs) throws SQLException {
		ClientLineItemEntity clientlineItem = new ClientLineItemEntity();
		clientlineItem.setClientLineId(rs.getString(ClientLineItemQueryConstant.CLIENTID));
		clientlineItem.setClientName(rs.getString(ClientLineItemQueryConstant.CLIENTNAME));
		clientlineItem.setLineItemName(rs.getString(ClientLineItemQueryConstant.LINEITEMNAME));
		clientlineItem.setClientLineItemName(rs.getString(ClientLineItemQueryConstant.CLIENTLINEITEMNAME));
		clientlineItem.setClientTableHeaderName(rs.getString(ClientLineItemQueryConstant.CLIENTTABLEHEADERNAME));
		clientlineItem.setMasterTableSource(rs.getString(ClientLineItemQueryConstant.MASTERTABLESOURCE));
		clientlineItem.setCreatedOn(rs.getLong(ClientLineItemQueryConstant.CREATEDON));
		clientlineItem.setCreatedBy(rs.getString(ClientLineItemQueryConstant.CREATEDBY));
		return clientlineItem;
	}

	@Override
	public ArrayList<ClientLineItemEntity> getclientLineItemListDetails(String dataBaseName)
			throws ClientLineItemDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<ClientLineItemEntity> list = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTLINE_LISTDETAILS
					.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				ClientLineItemEntity entity = bulidData(rs);
				list.add(entity);
			}
			return list;

		} catch (Exception e) {

			throw new ClientLineItemDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(rs, psta, con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<ClientLineItemEntity> addMultipleObject(ArrayList<ClientLineItemEntity> clientLineItem,
			String dataBaseName) throws ClientLineItemDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		boolean allExistInDatabase = true;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			Iterator it = clientLineItem.iterator();
			while (it.hasNext()) {
				ClientLineItemEntity entity = (ClientLineItemEntity) it.next();
				pstmt = connection.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTTLINETEMNAME
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				System.out.println("CLIENT name" + entity.getClientName());
				pstmt.setString(1, entity.getClientName());
				result = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				while (result.next()) {

					existingValues.add(result.getString("clientLineItemName"));
				}
				System.out.println("list existingValues" + existingValues);
				pstmt = connection.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTTLINETEMNAME
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, entity.getClientName());
				result = pstmt.executeQuery();
				if (result.next() == false) {
					System.out.println("rs is null");
					pstmt = connection.prepareStatement(ClientLineItemQueryConstant.INSERT_INTO_CLIENTLINE_ITEM
							.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String aid = UUID.randomUUID().toString();
					Date d = new Date();
					String clientId = aid;
					entity.setClientLineId(clientId);
					pstmt.setString(1, entity.getClientLineId());
					pstmt.setString(2, entity.getClientName());
					pstmt.setString(3, entity.getLineItemName());
					pstmt.setString(4, entity.getClientLineItemName());
					pstmt.setString(5, entity.getClientTableHeaderName());
					pstmt.setString(6, entity.getMasterTableSource());
					pstmt.setLong(7, d.getTime());
                  pstmt.setString(8, entity.getCreatedBy());
					pstmt.executeUpdate();
					allExistInDatabase = false;

				} else {
					boolean isMatched = existingValues.stream()
							.anyMatch(entity.getClientLineItemName()::equalsIgnoreCase);
					if (isMatched == false || !existingValues.contains(entity.getClientLineItemName())) {
						System.out.println("rs is not null and list not contains line item");
						pstmt = connection.prepareStatement(ClientLineItemQueryConstant.INSERT_INTO_CLIENTLINE_ITEM
								.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						String aid = UUID.randomUUID().toString();
						Date d = new Date();
						String clientId = aid;
						entity.setClientLineId(clientId);
						pstmt.setString(1, entity.getClientLineId());
						pstmt.setString(2, entity.getClientName());
						pstmt.setString(3, entity.getLineItemName());
						pstmt.setString(4, entity.getClientLineItemName());
						pstmt.setString(5, entity.getClientTableHeaderName());
						pstmt.setString(6, entity.getMasterTableSource());
						pstmt.setLong(7, d.getTime());
                         pstmt.setString(8, entity.getCreatedBy());
						pstmt.executeUpdate();
						allExistInDatabase = false;

					} else {
						System.out.println("skip");
					}
				}
			}
			if (allExistInDatabase) {
				System.out.println("All values already exist in database.");
				throw new ClientLineItemDaoException("All values already exist in database. ");

			}
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return clientLineItem;

	}

	@Override
	public ArrayList<ClientLineItemEntity> getByClientNameandTable(String clientName, String masterTableSource,
			String dataBaseName) throws ClientLineItemDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<ClientLineItemEntity> clienDetailsList = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTLINEITEM_BY_CLIENTNAME
					.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, clientName);
			pstmt.setString(2, masterTableSource);
			result = pstmt.executeQuery();
			while (result.next()) {
				ClientLineItemEntity clientEntity = bulidData(result);
				clienDetailsList.add(clientEntity);
			}
			return clienDetailsList;
		} catch (Exception e) {
			LOGGER.error("unble to get list of analyst line item" + e.getMessage());
			e.printStackTrace();
			throw new ClientLineItemDaoException(
					"unable to get clinet line item details by client name and master table source" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public String updatelineItemName(ClientDetails clientDetails, String dataBaseName)
			throws ClientLineItemDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ClientLineItemQueryConstant.UPDATE_LINEITEMNAME
					.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, clientDetails.getLineItemName());
			pstmt.setString(2, clientDetails.getClientName());
			pstmt.setString(3, clientDetails.getClientLineItemName());
			pstmt.executeUpdate();
			return "update sucessfully";
		}

		catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemDaoException(e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(pstmt, connection);
			}
	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<ClientLineItemEntity> uploadClientLineItem(String createdBy,CompletedFileUpload file, String dataBaseName) throws ClientLineItemDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String lineItemName;
		List<String> clientlineItemList = new ArrayList<>();
		boolean allExistInDatabase = true;
		ArrayList<ClientLineItemEntity> alreadyPresentList=new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			byte[] fileContent = file.getBytes();

			InputStream targetStream = new ByteArrayInputStream(fileContent);

			XSSFWorkbook workbook = new XSSFWorkbook(targetStream);

			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			int numRows = sheet.getLastRowNum() + 1;
			if (numRows <= 1) {
				System.out.println("excel has no data");
				throw new Exception("Excel sheet is empty");
			}
			int rownum1 = 0;

			for (Row row1 : sheet) {

				if (rownum1 == 0) {

					rownum1++;
					continue;
				}

				String clientName = (formatter.formatCellValue(row1.getCell(0)).trim());
				if (clientName.isEmpty()) {

					throw new ClientLineItemDaoException("values Should not be empty or null ");

				}
				

				pstmt = connection.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTNAME
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				rs = pstmt.executeQuery();
				List<String> clientnameList = new ArrayList<>();
				while (rs.next()) {
					clientnameList.add(rs.getString("clientName"));
				}

				if (!clientnameList.contains(clientName)) {
//						System.out.println(Analystname+ "this analystname not present in analyst details table");
					throw new ClientLineItemDaoException(" ClientName not present in client details table");
				}
			}


			int rownum = 0;

			for (Row row1 : sheet) {
				if (rownum == 0) {
					rownum++;
					continue;
				}
				String Clientname = (formatter.formatCellValue(row1.getCell(0)).trim());
			
				String clientTableheaderName = (formatter.formatCellValue(row1.getCell(1)).trim());
				if (clientTableheaderName.isEmpty()) {

					throw new ClientLineItemDaoException("clientTableheaderName  is empty ");

				}
				String clientLineItemName = (formatter.formatCellValue(row1.getCell(2)).trim());
				if (clientLineItemName.isEmpty()) {

					throw new ClientLineItemDaoException("clientLineItemName  is empty ");

				}

				String masterTableSource = (formatter.formatCellValue(row1.getCell(3)).trim());
				if (masterTableSource.isEmpty()) {

					throw new ClientLineItemDaoException("masterTableSource  is empty ");

				}
				String lineName = (formatter.formatCellValue(row1.getCell(4)).trim());
				if (!lineName.isEmpty()) {
					lineItemName = lineName;
				} else {
					lineItemName = null;
				}
				Date d = new Date();

				

				pstmt = connection.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTTLINETEMNAME
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, Clientname);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					clientlineItemList.add(rs.getString("clientLineItemName"));
				}
				pstmt = connection.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTTLINETEMNAME
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, Clientname);
				rs = pstmt.executeQuery();
				if (rs.next() == false) {
					System.out.println("result set is empty data add");
					pstmt = connection.prepareStatement(ClientLineItemQueryConstant.INSERT_INTO_CLIENTLINE_ITEM
							.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String id = UUID.randomUUID().toString();
					pstmt.setString(1, id);
					pstmt.setString(2, Clientname);
					pstmt.setString(3, lineItemName);
					pstmt.setString(4, clientLineItemName);
					pstmt.setString(5, clientTableheaderName);
					pstmt.setString(6, masterTableSource);
					pstmt.setLong(7, d.getTime());
					pstmt.setString(8, createdBy);
					pstmt.executeUpdate();
					rownum++;
					allExistInDatabase = false;

				} else {
					System.out.println("result set is not empty");
					boolean isMatched = clientlineItemList.stream().anyMatch(clientLineItemName::equalsIgnoreCase);

					if (isMatched == false || !clientlineItemList.contains(clientLineItemName)) {

						System.out.println("rs is not empey and clientlineItemList not contais in list so add  ");
						pstmt = connection.prepareStatement(ClientLineItemQueryConstant.INSERT_INTO_CLIENTLINE_ITEM
								.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						String id = UUID.randomUUID().toString();
						pstmt.setString(1, id);
						pstmt.setString(2, Clientname);
						pstmt.setString(3, lineItemName);
						pstmt.setString(4, clientLineItemName);
						pstmt.setString(5, clientTableheaderName);
						pstmt.setString(6, masterTableSource);
						pstmt.setLong(7, d.getTime());
						pstmt.setString(8, createdBy);
						pstmt.executeUpdate();
						rownum++;
						allExistInDatabase = false;

					} else {
						System.out.println("skip");
						ClientLineItemEntity entity =new ClientLineItemEntity();
						entity.setClientName(Clientname);
						entity.setClientLineItemName(clientLineItemName);
						alreadyPresentList.add(entity);
					}

				}
			}

			if (allExistInDatabase) {
				System.out.println("All values already exist in database.");
				throw new ClientLineItemDaoException("All values already exist in database. ");

			}
			return alreadyPresentList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemDaoException(e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(pstmt, connection);
			}

	}

	@SuppressWarnings("resource")
	@Override
	public ClientLineItemEntity updateClientLineItem(ClientLineItemEntity clientLineItem, String clientLineId,
			String dataBaseName) throws ClientLineItemDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet result = null;
		List<String> existingValues = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			String clientName = clientLineItem.getClientName();
			psta = con.prepareStatement(ClientLineItemQueryConstant.SELECT_CLIENTTLINETEMNAME2
					.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, clientName);
			psta.setString(2, clientLineId);
			result = psta.executeQuery();
			while (result.next()) {
				existingValues.add(result.getString("clientLineItemName"));
			}
			boolean isMatched = existingValues.stream()
					.anyMatch(clientLineItem.getClientLineItemName()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(clientLineItem.getClientLineItemName())) {
				psta = con.prepareStatement(ClientLineItemQueryConstant.UPDATE_LINEITEMNAME_NOMECLURE
						.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				psta.setString(1, clientLineItem.getClientName());
				psta.setString(2, clientLineItem.getClientLineItemName());
				psta.setString(3, clientLineItem.getClientTableHeaderName());
				psta.setString(4, clientLineItem.getMasterTableSource());
				psta.setString(5, clientLineId);

				psta.executeUpdate();

			} else {
				System.out.println("duplidate");
				throw new ClientLineItemDaoException("Cannot insert duplicate values");

			}
			return clientLineItem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemDaoException(e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}
	}

	

	@Override
	public Set<String> getCurrentDateClientItemName(long startTimestamp, long endTimestamp, String dataBaseName) {
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		Set<String> clientnamelist=new HashSet<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(ClientLineItemQueryConstant.SELECT_CURRENTDATE_CLIENTNAME.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
			String clientName=rs.getString("clientName");
			clientnamelist.add(clientName);
			}
		return clientnamelist;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	@Override
	public ArrayList<ClientLineItemEntity> getMasterTablenameWhenAddClientLineitem(long startTimestamp, long endTimestamp,
			String dataBaseName,String clientName) {
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
	ArrayList< ClientLineItemEntity> list=new ArrayList<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(ClientLineItemQueryConstant.SELECT_MASTERTABLENAMEWHENADDCLIENTLITEITEMNAME.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
		psta.setString(3, clientName);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
				ClientLineItemEntity entity=new ClientLineItemEntity();
				entity.setMasterTableSource(rs.getString("masterTableSource"));
				entity.setCreatedBy(rs.getString("createdBy"));
				list.add(entity);
				
			}
			return list;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	@Override
	public ArrayList<ClientLineItemEntity> gettablenameWhenMappingClientLineItem(long startTimestamp, long endTimestamp,
			String dataBaseName, String clientName) {
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
	    ArrayList<ClientLineItemEntity> list=new ArrayList<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(ClientLineItemQueryConstant.SELECT_WHENMAPPINGCLIENTLINEITEM_SELECTMASTERTABLENAME.replace(ClientLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
		psta.setString(3, clientName);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
				ClientLineItemEntity enity=new ClientLineItemEntity();
				enity.setMasterTableSource(rs.getString("masterTableSource"));
				enity.setCreatedBy(rs.getString("createdBy"));
	list.add(enity);
				
			}
			return list;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 finally {

				InvestorDatabaseUtill.close(psta, con);
			}
		return null;
	}
	}
	


