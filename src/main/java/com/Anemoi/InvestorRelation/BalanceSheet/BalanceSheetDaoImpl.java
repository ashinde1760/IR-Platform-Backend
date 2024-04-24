package com.Anemoi.InvestorRelation.BalanceSheet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowQuaryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.IncomeStatement.IncomeStatementQuaryConstatnt;
import com.Anemoi.InvestorRelation.UserModel.UserEntity;
import com.Anemoi.InvestorRelation.UserModel.UserQueryConstant;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton
public class BalanceSheetDaoImpl implements BalanceSheetDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(BalanceSheetDaoImpl.class);

	@SuppressWarnings("resource")
	@Override

	public BalanceSheetEntity createNewBalanceSheetForm(BalanceSheetEntity balanceEntity, String dataBaseName)
			throws BalanceSheetDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		   Set<String> existingValues = new HashSet<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();

			LOGGER.debug("inserting the data");
			pstmt = connection.prepareStatement(BalanceSheetQueryConstant.SELECT_LINEITEM
					.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				existingValues.add(resultSet.getString("lineItem").trim());
			}
	        boolean isDuplicate = existingValues.contains(balanceEntity.getLineItem().trim());
			boolean isMatched = existingValues.stream().anyMatch(balanceEntity.getLineItem()::equalsIgnoreCase);
//			System.out.println("value" + existingValues);
//			if (isMatched == false && !existingValues.contains(balanceEntity.getLineItem())) {
             if(!isDuplicate && isMatched == false)
             {
            	 Date d=new Date();
				pstmt = connection.prepareStatement(BalanceSheetQueryConstant.INSERT_INTO_BALANCESHEET_FORM
						.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

				String balanceId = UUID.randomUUID().toString();
				balanceEntity.setBalanceid(balanceId);
				pstmt.setString(1, balanceEntity.getBalanceid());
				pstmt.setString(2, balanceEntity.getLineItem());
				pstmt.setString(3, balanceEntity.getAlternativeName());
				pstmt.setString(4, balanceEntity.getCreatedBy());
				pstmt.setLong(5, d.getTime());
				pstmt.setString(6, null);
				pstmt.setLong(7, d.getTime());
				pstmt.executeUpdate();
			} else {
				throw new BalanceSheetDaoException("Cannot insert Duplicate lineItem");
			}
			return balanceEntity;

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new BalanceSheetDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public BalanceSheetEntity getBalanceById(String balanceid, String dataBaseName) throws BalanceSheetDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(BalanceSheetQueryConstant.SELECT_BALANCESHEET_FORM_BY_ID
					.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, balanceid);
			result = pstmt.executeQuery();
			while (result.next()) {
				BalanceSheetEntity balanceEntity = buildBalanceDeatils(result);
				return balanceEntity;
			}
		} catch (Exception e) {
			LOGGER.error("Balance Sheet Data not found" + e.getMessage());
			throw new BalanceSheetDaoException("unable to get balance sheet by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	private BalanceSheetEntity buildBalanceDeatils(ResultSet result) throws SQLException {

		BalanceSheetEntity balanceEntity = new BalanceSheetEntity();
		balanceEntity.setBalanceid(result.getString(BalanceSheetQueryConstant.BALANCEID));
		balanceEntity.setLineItem(result.getString(BalanceSheetQueryConstant.LINE_ITEM));
		balanceEntity.setAlternativeName(result.getString(BalanceSheetQueryConstant.ALTERNATIVE_NAME));
		balanceEntity.setCreatedBy(result.getString("createdBy"));
		balanceEntity.setCreatedOn(result.getLong("createdOn"));
		balanceEntity.setModifiedBy(result.getString("modifiedBy"));
      balanceEntity.setModifiedOn(result.getLong("modifiedOn"));
		return balanceEntity;
	}

	@Override
	public List<BalanceSheetEntity> getAllBalanceSheetDetails(String dataBaseName) throws BalanceSheetDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<BalanceSheetEntity> listOfbalanceSheetDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(BalanceSheetQueryConstant.SELECT_BALANCESHEET_FORM
					.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				BalanceSheetEntity balanceSheet = buildBalanceDeatils(result);
				listOfbalanceSheetDetails.add(balanceSheet);
			}
			return listOfbalanceSheetDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of balance Sheet" + e.getMessage());
			e.printStackTrace();
			throw new BalanceSheetDaoException("unable to balance sheet details" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public BalanceSheetEntity updateBalanceSheetDetails(BalanceSheetEntity balanceEntity, String balanceid,
			String dataBaseName) throws BalanceSheetDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();
		List<String> valueSame = new ArrayList<>();
//		LOGGER.info(".in update balance sheet database name is ::" + dataBaseName + " incomeId is ::" + balanceid
//				+ " request balance sheet is ::" + balanceEntity);

		try {

			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(BalanceSheetQueryConstant.SELECT_LINEITEM2
					.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, balanceid);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				existingValues.add(resultSet.getString("lineItem").trim());

			}
			System.out.println("value" + existingValues);

			boolean isMatched = existingValues.stream().anyMatch(balanceEntity.getLineItem()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(balanceEntity.getLineItem().trim())) {
             Date d=new Date();
				pstmt = connection.prepareStatement(BalanceSheetQueryConstant.UPDATE_BALANCESHEET_FORM
						.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, balanceEntity.getLineItem().trim());
				pstmt.setString(2, balanceEntity.getAlternativeName());
				pstmt.setString(3, balanceEntity.getModifiedBy());
				pstmt.setLong(4, d.getTime());
				pstmt.setString(5, balanceid);

				int executeUpdate = pstmt.executeUpdate();

				System.out.println(executeUpdate);
				LOGGER.info(executeUpdate + " balance sheet updated successfully");
			}

			else {
				throw new BalanceSheetDaoException("Cannot insert duplicate line Item");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetDaoException(e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return balanceEntity;
	}

	@Override
	public String deleteBalanceSheet(String balanceid, String dataBaseName) throws BalanceSheetDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(BalanceSheetQueryConstant.DELETE_BALANCESHEET_FORM_BY_ID
					.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, balanceid);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " balance sheet deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetDaoException("unable to delete " + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<BalanceSheetEntity> addObjectBalanceSheetEntity(ArrayList<BalanceSheetEntity> balanceSheetEntity,
			String dataBaseName) throws BalanceSheetDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		String id = null;

		boolean allExistInDatabase = true;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			Iterator it = balanceSheetEntity.iterator();
			while (it.hasNext()) {
				Date d=new Date();
				BalanceSheetEntity entity = (BalanceSheetEntity) it.next();
				System.out.println("lineitem" + entity.getLineItem());
				pstmt = connection.prepareStatement(BalanceSheetQueryConstant.SELECT_LINEITEM
						.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				ResultSet resultSet = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				while (resultSet.next()) {

					existingValues.add(resultSet.getString("lineItem"));
				}
				boolean isMatched = existingValues.stream().anyMatch(entity.getLineItem()::equalsIgnoreCase);
				System.out.println("value" + existingValues);
				if (isMatched == false && !existingValues.contains(entity.getLineItem())) {
					System.out.println("rs is null and not constais in list");
					pstmt = connection.prepareStatement(BalanceSheetQueryConstant.INSERT_INTO_BALANCESHEET_FORM
							.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String bid = UUID.randomUUID().toString();
					id = bid;
					entity.setBalanceid(id);
					pstmt.setString(1, entity.getBalanceid());
					pstmt.setString(2, entity.getLineItem());
					pstmt.setString(3, entity.getAlternativeName());
					pstmt.setString(4, entity.getCreatedBy());
					pstmt.setLong(5, d.getTime());
                    pstmt.setString(6, null);
				  pstmt.setLong(7, d.getTime());
					pstmt.executeUpdate();
					allExistInDatabase = false;
				} else {
					System.out.println("skip");
					
				}

			}
			if (allExistInDatabase) {
				System.out.println("All values already exist in database.");
				throw new BalanceSheetDaoException("All values already exist in database. ");

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return balanceSheetEntity;

	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<BalanceSheetEntity> uploadBalanceSheetExcel(String createdBy,CompletedFileUpload file, String dataBaseName) throws BalanceSheetDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ArrayList<String> list = new ArrayList<>();
      ArrayList<BalanceSheetEntity> alreadyPresentList=new ArrayList<>();
		boolean allExistInDatabase = true;
		Date d=new Date();
		try

		{
			connection = InvestorDatabaseUtill.getConnection();

			DataFormatter formatter = new DataFormatter();
			byte[] fileContent = file.getBytes();

			ByteArrayInputStream targetStream = new ByteArrayInputStream(fileContent);

			XSSFWorkbook workbook = new XSSFWorkbook(targetStream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			int numRows = sheet.getLastRowNum() + 1;
			if (numRows <= 1) {
				System.out.println("excel has no data");
				throw new Exception("Excel sheet is empty");
			}

			int rownum = 0;
			for (Row row1 : sheet) {
				if (rownum == 0) {
					rownum++;
					continue;
				}

				String lineItemName = (formatter.formatCellValue(row1.getCell(0)));

				if (lineItemName.isEmpty()) {

					throw new BalanceSheetDaoException("value should not be empty or null");

				}
				pstmt = connection.prepareStatement(BalanceSheetQueryConstant.SELECT_LINEITEM
						.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				ResultSet resultSet = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				while (resultSet.next()) {
					String value = resultSet.getString("lineItem");
					if (value != null) {
						existingValues.add(value);
					}
				}
			
				boolean isMatched = existingValues.stream().anyMatch(lineItemName::equalsIgnoreCase);
			
				if (isMatched == false && !existingValues.contains(lineItemName)) {
					String alter1 = formatter.formatCellValue(row1.getCell(1));

					if (alter1.isEmpty()) {

						throw new BalanceSheetDaoException("value should not be empty or null");
					} else {
						list.add(alter1);
					}
					String alter2 = formatter.formatCellValue(row1.getCell(2));
					if (!alter2.isEmpty()) {

						list.add(alter2);
					}
					String alter3 = formatter.formatCellValue(row1.getCell(3));
					if (!alter3.isEmpty()) {

						list.add(alter3);
					}
					String alter4 = formatter.formatCellValue(row1.getCell(4));
					if (!alter4.isEmpty()) {

						list.add(alter4);
					}
					String alter5 = formatter.formatCellValue(row1.getCell(5));
					if (!alter5.isEmpty()) {

						list.add(alter5);
					}
					String alter6 = formatter.formatCellValue(row1.getCell(6));
					if (!alter6.isEmpty()) {

						list.add(alter6);
					}

					pstmt = connection.prepareStatement(BalanceSheetQueryConstant.INSERT_INTO_BALANCESHEET_FORM
							.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String id = UUID.randomUUID().toString();
					pstmt.setString(1, id);
					pstmt.setString(2, lineItemName);
					pstmt.setString(3, list.toString());
                    pstmt.setString(4, createdBy);
                    pstmt.setLong(5, d.getTime());
                    pstmt.setString(6, null);
                    pstmt.setLong(7, d.getTime());
					pstmt.executeUpdate();
					rownum++;
					list.removeAll(list);

					System.out.println("Inserted value: " + lineItemName);
					allExistInDatabase = false;
				} else {
					System.out.println("Skipped value: " + lineItemName);
					BalanceSheetEntity e =new BalanceSheetEntity();
					e.setLineItem(lineItemName);
					alreadyPresentList.add(e);
					
				}
			}
 
			if (allExistInDatabase) {
				System.out.println("All values already exist in database.");
				throw new BalanceSheetDaoException("All values already exist in database. ");

			}
			
			 return alreadyPresentList;
		} catch (Exception e) {
			// TODO: handle exception

			throw new BalanceSheetDaoException(e.getMessage());
		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ArrayList<BalanceSheetEntity> getBalanceSheetLineItem(long startTimestamp, long endTimestamp,
			String dataBaseName) {
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		ArrayList<BalanceSheetEntity> lineItemList=new ArrayList<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(BalanceSheetQueryConstant.SELECT_LINEITEMFORNOTIFICATION.replace(BalanceSheetQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
				BalanceSheetEntity entity=new BalanceSheetEntity();
				entity.setLineItem(rs.getString("lineItem"));
				entity.setCreatedBy(rs.getString("createdBy"));
				lineItemList.add(entity);
				
			}
			return lineItemList;
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
