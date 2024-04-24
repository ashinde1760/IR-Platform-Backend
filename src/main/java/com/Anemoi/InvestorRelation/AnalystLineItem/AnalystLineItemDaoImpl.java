package com.Anemoi.InvestorRelation.AnalystLineItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetDaoException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoException;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton
public class AnalystLineItemDaoImpl implements AnalystLineItemDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalystLineItemDaoImpl.class);

	@SuppressWarnings("resource")
	@Override
	public AnalystLineItemEntity createAnalystLineItem(AnalystLineItemEntity lineItem, String dataBaseName)
			throws AnalystLineItemDaoException {

		Connection connection = null;
		PreparedStatement psta = null;
		ResultSet result = null;
		Date date = new Date();
		List<String> existingValues = new ArrayList<>();
		try {
			String analystName = lineItem.getAnalystName();
			connection = InvestorDatabaseUtill.getConnection();

			psta = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINETEMNAME
					.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, analystName);
			result = psta.executeQuery();
			while (result.next()) {
				existingValues.add(result.getString("analystLineItemName"));
			}
			boolean isMatched = existingValues.stream().anyMatch(lineItem.getAnalystLineItemName()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(lineItem.getAnalystLineItemName())) {

				psta = connection.prepareStatement(AnalystLineItemQueryConstant.INSERT_INTO_ANALYSTLINE_ITEM
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String aid = UUID.randomUUID().toString();
				lineItem.setAnalystLineId(aid);
				String id = lineItem.getAnalystLineId();
				psta.setString(1, id);
				psta.setString(2, lineItem.getAnalystName());
				psta.setString(3, lineItem.getLineItemName());
				psta.setString(4, lineItem.getAnalystLineItemName());
				psta.setString(5, lineItem.getAnalystTableHeaderName());
				psta.setString(6, lineItem.getMasterTableSource());
				psta.setLong(7, date.getTime());
               psta.setString(8, lineItem.getCreatedBy());
				psta.executeUpdate();

			} else {
//				System.out.println("duplidate");
				throw new AnalystLineItemDaoException("Cannot insert Duplicate values");

			}
			

			return lineItem;

		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemDaoException(e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, connection);

		}

	}

	@Override
	public AnalystLineItemEntity getAnalystLineItemById(String aid, String dataBaseName)
			throws AnalystLineItemDaoException {
//		System.out.println("welcome" + aid);
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
//			System.out.println("welcome" + connection);
			pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINEITEM_BY_ID
					.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, aid);
			result = pstmt.executeQuery();
			while (result.next()) {
				AnalystLineItemEntity analystLineItem = buildAnalystLineItemDeatils(result);
				return analystLineItem;
			}
		} catch (Exception e) {
			LOGGER.error("Analyst line Item contact not found" + e.getMessage());
			throw new AnalystLineItemDaoException("unable to get analyst line item by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	private AnalystLineItemEntity buildAnalystLineItemDeatils(ResultSet result) throws SQLException {
		AnalystLineItemEntity analystlineItem = new AnalystLineItemEntity();
		analystlineItem.setAnalystLineId(result.getString(AnalystLineItemQueryConstant.ANALYSTLINEID));
		analystlineItem.setAnalystName(result.getString(AnalystLineItemQueryConstant.ANALYSTNAME));
		analystlineItem.setLineItemName(result.getString(AnalystLineItemQueryConstant.LINEITEMNAME));
		analystlineItem.setAnalystLineItemName(result.getString(AnalystLineItemQueryConstant.ANALYSTLINEITEMNAME));
		analystlineItem
				.setAnalystTableHeaderName(result.getString(AnalystLineItemQueryConstant.ANALYSTTABLEHEADERNAME));
		analystlineItem.setMasterTableSource(result.getString(AnalystLineItemQueryConstant.MASTERTABLESOURCE));
		analystlineItem.setCreatedOn(result.getLong(AnalystLineItemQueryConstant.CREATEDON));
		analystlineItem.setCreatedBy(result.getString(AnalystLineItemQueryConstant.CREATEDBY));
//		System.out.println("analystlineItem" + analystlineItem);
		return analystlineItem;
	}

	@Override
	public List<AnalystLineItemEntity> getAllAnalystLineItem(String dataBaseName) throws AnalystLineItemDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<AnalystLineItemEntity> listofanlystlineItemDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINEITEM
					.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				AnalystLineItemEntity analystLineitem = buildAnalystLineItemDeatils(result);
				listofanlystlineItemDetails.add(analystLineitem);
			}
			return listofanlystlineItemDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of analyst line item" + e.getMessage());
			e.printStackTrace();
			throw new AnalystLineItemDaoException("unbale to get analyst line item " + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public ArrayList<AnalystLineItemEntity> getByanalystnameandTable(String analystName, String masterTableSource,
			String dataBaseName) throws AnalystLineItemDaoException {
		// TODO Auto-generated method stub

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<AnalystLineItemEntity> listNalaystlineitem = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINEITEM_BY_ANALYSTNAME
					.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, analystName);
			pstmt.setString(2, masterTableSource);
			result = pstmt.executeQuery();
			while (result.next()) {
				AnalystLineItemEntity analystLineitem = buildAnalystLineItemDeatils(result);
				listNalaystlineitem.add(analystLineitem);
			}
			return listNalaystlineitem;
		} catch (Exception e) {
			LOGGER.error("unble to get list of analyst line item" + e.getMessage());
			e.printStackTrace();
			throw new AnalystLineItemDaoException(
					"unable to get analyst line item details by analyst name and master table source" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public String updateAnalystlineItem(AnalystDetails analystDetails, String dataBaseName)
			throws AnalystLineItemDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.UPDATE_ANALYSTLINEITEM
					.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, analystDetails.getLineItemName());
			pstmt.setString(2, analystDetails.getAnalystName());
			pstmt.setString(3, analystDetails.getAnalystLineItemName());

			pstmt.executeUpdate();

			return "update sucessfully";

		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemDaoException("unable to update" + e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(pstmt, connection);
			}
	}

	@SuppressWarnings({ "resource", "unused" })
	@Override
	public ArrayList<AnalystLineItemEntity> uploadAnalystLineItem( String createdBy,CompletedFileUpload file, String dataBaseName)
			throws AnalystLineItemDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String lineItemName;
		
    ArrayList<AnalystLineItemEntity> alreadypresentList=new ArrayList<>();
		boolean allExistInDatabase = true;
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

				String Analystname = (formatter.formatCellValue(row1.getCell(0)).trim());
				if(Analystname.isEmpty())
				{
					throw new AnalystLineItemServiceException(" value should not be empty");

				}
			
				

				pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTNAME
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				rs = pstmt.executeQuery();
				List<String> analystnameList = new ArrayList<>();
				while (rs.next()) {
					analystnameList.add(rs.getString("analystName"));
				}

				if (!analystnameList.contains(Analystname)) {
//						System.out.println(Analystname+ "this analystname not present in analyst details table");
					throw new AnalystLineItemServiceException(" analystname not present in analyst details table");
				}
			}

			int rownum = 0;

			for (Row row1 : sheet) {
				List<String> analystlineItemList = new ArrayList<>();

				if (rownum == 0) {

					rownum++;
					continue;
				}
				String Analystname = (formatter.formatCellValue(row1.getCell(0)).trim());

				String analystTableheaderName = (formatter.formatCellValue(row1.getCell(1)).trim());
				if (analystTableheaderName.isEmpty()) {

					throw new AnalystLineItemDaoException("analystTableheaderName Should not be empty or null");

				}
				
				String analystLineItemName = (formatter.formatCellValue(row1.getCell(2)).trim());
				if (analystLineItemName.isEmpty()) {

					throw new AnalystLineItemDaoException("analystLineItemName Should not be empty or null ");

				}

				String masterTableSource = (formatter.formatCellValue(row1.getCell(3)).trim());
				if (masterTableSource.isEmpty()) {

					throw new AnalystLineItemDaoException("masterTableSource Should not be empty or null ");

				}
				String lineName = (formatter.formatCellValue(row1.getCell(4)).trim());
				if (!lineName.isEmpty()) {
					lineItemName = lineName;
				} else {
					lineItemName = null;
				}
				Date d = new Date();

				pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINETEMNAME
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, Analystname);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					analystlineItemList.add(rs.getString("analystLineItemName"));
				}
				pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINETEMNAME
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, Analystname);
				rs = pstmt.executeQuery();
				if (rs.next() == false) {
//					System.out.println("result set is empty data add");
					pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.INSERT_INTO_ANALYSTLINE_ITEM
							.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String id = UUID.randomUUID().toString();
					pstmt.setString(1, id);
					pstmt.setString(2, Analystname);
					pstmt.setString(3, lineItemName);
					pstmt.setString(4, analystLineItemName);
					pstmt.setString(5, analystTableheaderName);
					pstmt.setString(6, masterTableSource);
					pstmt.setLong(7, d.getTime());
					pstmt.setString(8, createdBy);
					pstmt.executeUpdate();
					rownum++;
					allExistInDatabase = false;
//				System.out.println("row count check1" + rownum);
				} else {
//		        	System.out.println("result set is not empty");
					boolean isMatched = analystlineItemList.stream().anyMatch(analystLineItemName::equalsIgnoreCase);
//					System.out.println("value" + analystlineItemList);
					if (isMatched == false && !analystlineItemList.contains(analystLineItemName)) {

//					 System.out.println("rs is not empey and analystlineItemList not contais in list so add  ");
						pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.INSERT_INTO_ANALYSTLINE_ITEM

								.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						String id = UUID.randomUUID().toString();
						pstmt.setString(1, id);
						pstmt.setString(2, Analystname);
						pstmt.setString(3, lineItemName);
						pstmt.setString(4, analystLineItemName);
						pstmt.setString(5, analystTableheaderName);
						pstmt.setString(6, masterTableSource);
						pstmt.setLong(7, d.getTime());
						pstmt.setString(8, createdBy);
						pstmt.executeUpdate();
						rownum++;
						allExistInDatabase = false;

					} else {
						System.out.println("skip");
						AnalystLineItemEntity entity=new AnalystLineItemEntity();
						entity.setAnalystName(Analystname);
						entity.setAnalystLineItemName(analystLineItemName);
						alreadypresentList.add(entity);
					}

				}
			}
			if (allExistInDatabase) {
//				System.out.println("All values already exist in database.");
				throw new AnalystLineItemDaoException("All values already exist in database. ");

			}
			return alreadypresentList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemDaoException(e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(pstmt, connection);
			}

	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<AnalystLineItemEntity> addMultipleObject(ArrayList<AnalystLineItemEntity> analystLineItem,
			String dataBaseName) throws AnalystLineItemDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		boolean allExistInDatabase = true;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			Iterator it = analystLineItem.iterator();
			while (it.hasNext()) {
				AnalystLineItemEntity entity = (AnalystLineItemEntity) it.next();
				pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINETEMNAME
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, entity.getAnalystName());
//				System.out.println("entity.getAnalystName()"+entity.getAnalystName());
				result = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				while (result.next()) {
					existingValues.add(result.getString("analystLineItemName"));
				}
//				System.out.println("existingValues"+existingValues);
				pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINETEMNAME
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, entity.getAnalystName());
				result = pstmt.executeQuery();
				if (result.next() == false) {
//					System.out.println("rs is empty");

					pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.INSERT_INTO_ANALYSTLINE_ITEM
							.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String aid = UUID.randomUUID().toString();
					Date d = new Date();
					String analystid = aid;
					entity.setAnalystLineId(analystid);
					pstmt.setString(1, entity.getAnalystLineId());
					pstmt.setString(2, entity.getAnalystName());
					pstmt.setString(3, entity.getLineItemName());
					pstmt.setString(4, entity.getAnalystLineItemName());
					pstmt.setString(5, entity.getAnalystTableHeaderName());
					pstmt.setString(6, entity.getMasterTableSource());
					pstmt.setLong(7, d.getTime());
                    pstmt.setString(8, entity.getCreatedBy());
					pstmt.executeUpdate();
					allExistInDatabase = false;
				} else {

					boolean isMatched;
					isMatched = existingValues.stream().anyMatch(entity.getAnalystLineItemName()::equalsIgnoreCase);

					if (!existingValues.contains(entity.getAnalystLineItemName()) || isMatched == false) {
//								System.out.println("rs is not null and list not contains this value");
						pstmt = connection.prepareStatement(AnalystLineItemQueryConstant.INSERT_INTO_ANALYSTLINE_ITEM
								.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						String aid = UUID.randomUUID().toString();
						Date d = new Date();
						String analystid = aid;
						entity.setAnalystLineId(analystid);
						pstmt.setString(1, entity.getAnalystLineId());
						pstmt.setString(2, entity.getAnalystName());
						pstmt.setString(3, entity.getLineItemName());
						pstmt.setString(4, entity.getAnalystLineItemName());
						pstmt.setString(5, entity.getAnalystTableHeaderName());
						pstmt.setString(6, entity.getMasterTableSource());
						pstmt.setLong(7, d.getTime());
                         pstmt.setString(8, entity.getCreatedBy());
						pstmt.executeUpdate();
						allExistInDatabase = false;

					} else
						System.out.println("skip");

				}
			}
			if (allExistInDatabase) {
				throw new CashFlowDaoException("All values already exist in database. ");

			}
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return analystLineItem;

	}

	@SuppressWarnings("resource")
	@Override
	public AnalystLineItemEntity updateAnalystLineItem(AnalystLineItemEntity analystLineItem, String analystLineId,
			String dataBaseName) throws AnalystLineItemDaoException {

		Connection connection = null;
		PreparedStatement psta = null;
		ResultSet result = null;
		Date date = new Date();
		List<String> existingValues = new ArrayList<>();
		try {
			String analystName = analystLineItem.getAnalystName();
			connection = InvestorDatabaseUtill.getConnection();

			psta = connection.prepareStatement(AnalystLineItemQueryConstant.SELECT_ANALYSTLINETEMNAME2
					.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, analystName);
			psta.setString(2, analystLineId);

			result = psta.executeQuery();
			while (result.next()) {
				existingValues.add(result.getString("analystLineItemName"));
			}
//			System.out.println("existingValues" + existingValues);
//			System.out.println("analystLineItem.getAnalystLineItemName()" + analystLineItem.getAnalystLineItemName());
			boolean isMatched = existingValues.stream()
					.anyMatch(analystLineItem.getAnalystLineItemName()::equalsIgnoreCase);

			if (isMatched == false && !existingValues.contains(analystLineItem.getAnalystLineItemName())) {

				psta = connection.prepareStatement(AnalystLineItemQueryConstant.UPDATE_ANALYSTLINE_ITEMNOMENCLURE
						.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				psta.setString(1, analystLineItem.getAnalystName());
				psta.setString(2, analystLineItem.getAnalystLineItemName());
				psta.setString(3, analystLineItem.getAnalystTableHeaderName());
				psta.setString(4, analystLineItem.getMasterTableSource());
				psta.setString(5, analystLineId);

				psta.executeUpdate();
			} else {
//				System.out.println("duplidate");
				throw new AnalystLineItemDaoException("Cannot add Duplicate values");

			}

			return analystLineItem;

		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemDaoException(e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, connection);

		}
	}

	@Override
	public Set<String> getCurrentAnalystDetailsMapping(long startTimestamp, long endTimestamp,
			String dataBaseName) {
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		Set<String> uniqueTableNames = new HashSet<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(AnalystLineItemQueryConstant.SELECT_CURRENTDATEMAPPING.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
			
	
			String tableName=rs.getString("masterTableSource");
			uniqueTableNames.add(tableName);
				
			}
			return uniqueTableNames;
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

	@Override
	public Set<String> getCurrentAddlingLineItem(long startTimestamp, long endTimestamp, String dataBaseName) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		Set<String> uniqueTableNames = new HashSet<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(AnalystLineItemQueryConstant.SELECT_CURRENTDATEADDLINEITEM.replace(AnalystLineItemQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
			
	
			String tableName=rs.getString("masterTableSource");
			uniqueTableNames.add(tableName);
				
			}
			return uniqueTableNames;
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

