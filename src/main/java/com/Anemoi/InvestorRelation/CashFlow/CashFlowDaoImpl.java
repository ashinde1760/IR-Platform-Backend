package com.Anemoi.InvestorRelation.CashFlow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.hssf.record.common.FormatRun;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetDaoException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.IncomeStatement.IncomeStatementQuaryConstatnt;
import com.Anemoi.InvestorRelation.UserModel.UserQueryConstant;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton
public class CashFlowDaoImpl implements CashFlowDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(CashFlowDaoImpl.class);

	@SuppressWarnings("resource")
	@Override
	public CashFlowEntity createNewCashFlow(CashFlowEntity CashFlowEntity, String dataBaseName)
			throws CashFlowDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			LOGGER.debug("inserting the data");

			pstmt = connection.prepareStatement(CashFlowQuaryConstant.SELECT_LINEITEM
					.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				existingValues.add(resultSet.getString("lineItem").trim());
			}

			boolean isMatched = existingValues.stream().anyMatch(CashFlowEntity.getLineItem().trim()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(CashFlowEntity.getLineItem().trim())) {
				Date d=new Date();
				pstmt = connection.prepareStatement(CashFlowQuaryConstant.INSERT_INTO_CASHFLOW
						.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String cashid = UUID.randomUUID().toString();
				CashFlowEntity.setCashId(cashid);
				pstmt.setString(1, CashFlowEntity.getCashId());
				pstmt.setString(2, CashFlowEntity.getLineItem().trim());
				pstmt.setString(3, CashFlowEntity.getAlternativeName());
                 pstmt.setString(4, CashFlowEntity.getCreatedBy());
                 pstmt.setLong(5, d.getTime());
                 pstmt.setString(6, null);
                 pstmt.setLong(7, d.getTime());
				pstmt.executeUpdate();
			} else {
				throw new CashFlowDaoException("Cannot Insert Duplicate Line Item");
			}
			return CashFlowEntity;

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new CashFlowDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public CashFlowEntity getCashFlowById(String cashid, String dataBaseName) throws CashFlowDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(CashFlowQuaryConstant.SELECT__CASHFLOW_BY_ID
					.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, cashid);
			result = pstmt.executeQuery();
			while (result.next()) {
				CashFlowEntity cashFlowEntity = buildCashFlowDeatils(result);
				return cashFlowEntity;
			}
		} catch (Exception e) {
			LOGGER.error("Cash Flow Data not found" + e.getMessage());
			throw new CashFlowDaoException("unable to get cash flow by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	private CashFlowEntity buildCashFlowDeatils(ResultSet result) throws SQLException {

		CashFlowEntity cashFlowEntity = new CashFlowEntity();
		cashFlowEntity.setCashId(result.getString(CashFlowQuaryConstant.CASHID));
		cashFlowEntity.setLineItem(result.getString(CashFlowQuaryConstant.LINE_ITEM));
		cashFlowEntity.setAlternativeName(result.getString(CashFlowQuaryConstant.ALTERNATIVE_NAME));
        cashFlowEntity.setCreatedBy(result.getString("createdBy"));
        cashFlowEntity.setCreatedOn(result.getLong("createdOn"));
        cashFlowEntity.setModifiedBy(result.getString("modifiedBy"));
        cashFlowEntity.setModifiedOn(result.getLong("modifiedOn"));
		return cashFlowEntity;
	}

	@Override
	public List<CashFlowEntity> getAllCashFlowDetails(String dataBaseName) throws SQLException, CashFlowDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<CashFlowEntity> listOfcashFlowDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(CashFlowQuaryConstant.SELECT_CASHFLOW
					.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				CashFlowEntity balanceSheet = buildCashFlowDeatils(result);
				listOfcashFlowDetails.add(balanceSheet);
			}
			return listOfcashFlowDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of cash flow" + e.getMessage());
			e.printStackTrace();
			throw new CashFlowDaoException("unable to get cash flow list" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public CashFlowEntity updateCashFlowDetails(CashFlowEntity cashFlowEntity, String cashid, String dataBaseName)
			throws CashFlowDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();
//		LOGGER.info(".in update cash flow database name is ::" + dataBaseName + " cashId is ::" + cashid
//				+ " request cash flow is ::" + cashFlowEntity);

		try {

			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(CashFlowQuaryConstant.SELECT_LINEITEM2
					.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, cashid);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				existingValues.add(resultSet.getString("lineItem").trim());
			}

			boolean isMatched = existingValues.stream().anyMatch(cashFlowEntity.getLineItem().trim()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(cashFlowEntity.getLineItem().trim())) {
				pstmt = connection.prepareStatement(CashFlowQuaryConstant.UPDATE_CASHFLOW
						.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				Date date = new Date();
				pstmt.setString(1, cashFlowEntity.getLineItem().trim());
				pstmt.setString(2, cashFlowEntity.getAlternativeName());
				pstmt.setString(3, cashFlowEntity.getModifiedBy());
				pstmt.setLong(4, date.getTime());
				pstmt.setString(5, cashid);
				pstmt.executeUpdate();

				LOGGER.info(" Cash Flow updated successfully");
			} else {
				throw new CashFlowDaoException("Cannot Insert Duplicate line Item");
			}
			return cashFlowEntity;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowDaoException(e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public String deleteCashFlow(String cashid, String dataBaseName) throws CashFlowDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(CashFlowQuaryConstant.DELETE_CASHFLOW_BY_ID
					.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, cashid);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowDaoException("enter valid request" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;

	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<CashFlowEntity> addCashFlowEntityObject(ArrayList<CashFlowEntity> cashFlowEntity,
			String dataBaseName) throws CashFlowDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		String id = null;

		boolean allExistInDatabase = true;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			Iterator it = cashFlowEntity.iterator();
			while (it.hasNext()) {
				Date d=new Date();
				CashFlowEntity entity = (CashFlowEntity) it.next();
				System.out.println("lineitem" + entity.getLineItem());
				pstmt = connection.prepareStatement(CashFlowQuaryConstant.SELECT_LINEITEM
						.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				ResultSet resultSet = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				while (resultSet.next()) {

					existingValues.add(resultSet.getString("lineItem"));
				}
				boolean isMatched = existingValues.stream().anyMatch(entity.getLineItem()::equalsIgnoreCase);
				System.out.println("value" + existingValues);
				if (isMatched == false && !existingValues.contains(entity.getLineItem())) {
					System.out.println("rs is null and not constais in list");
					pstmt = connection.prepareStatement(CashFlowQuaryConstant.INSERT_INTO_CASHFLOW
							.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String bid = UUID.randomUUID().toString();
					id = bid;
					entity.setCashId(id);
					pstmt.setString(1, entity.getCashId());
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
				throw new CashFlowDaoException("All values already exist in database. ");

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CashFlowDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return cashFlowEntity;

	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<CashFlowEntity> uploadCashFlowExcelSheet(String createdBy,CompletedFileUpload file, String dataBaseName) throws CashFlowDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ArrayList<String> list = new ArrayList<>();
     ArrayList<CashFlowEntity> alreadypresentList=new ArrayList<>();
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

					throw new CashFlowDaoException("value should not be empty or null ");

				}
				pstmt = connection.prepareStatement(CashFlowQuaryConstant.SELECT_LINEITEM
						.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				ResultSet resultSet = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				System.out.println("check3");
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
						throw new CashFlowDaoException("value should not be empty or null ");
					} else {
						list.add(alter1);
					}
					String alter2 = formatter.formatCellValue(row1.getCell(2));
					if (alter2.isEmpty()) {
					} else {
						list.add(alter2);
					}
					String alter3 = formatter.formatCellValue(row1.getCell(3));
					if (alter3.isEmpty()) {
					} else {
						list.add(alter3);
					}
					String alter4 = formatter.formatCellValue(row1.getCell(4));
					if (alter4.isEmpty()) {
					} else {
						list.add(alter4);
					}
					String alter5 = formatter.formatCellValue(row1.getCell(5));
					if (alter5.isEmpty()) {
					} else {
						list.add(alter5);
					}
					String alter6 = formatter.formatCellValue(row1.getCell(6));
					if (alter6.isEmpty()) {
					} else {
						list.add(alter6);
					}

					pstmt = connection.prepareStatement(CashFlowQuaryConstant.INSERT_INTO_CASHFLOW
							.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
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
					CashFlowEntity cash=new CashFlowEntity();
					cash.setLineItem(lineItemName);
					alreadypresentList.add(cash);
				}
			}

			if (allExistInDatabase) {
				System.out.println("All values already exist in database.");
				throw new CashFlowDaoException("All values already exist in database. ");

			}
			return alreadypresentList;
		} catch (Exception e) {
			// TODO: handle exception
			throw new CashFlowDaoException(e.getMessage());

		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(pstmt, connection);
			}

	}

	

	@Override
	public ArrayList<CashFlowEntity> getLineItemListForToday(long startTimestamp, long endTimestamp,
			String dataBaseName) {
		// TODO Auto-generated method stub
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		ArrayList<CashFlowEntity> lineItemList=new ArrayList<>();
		System.out.println("cash flow");
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(CashFlowQuaryConstant.SELECT_LINEITEMFORNOTIFICATION.replace(CashFlowQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
				CashFlowEntity entity=new CashFlowEntity();
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
