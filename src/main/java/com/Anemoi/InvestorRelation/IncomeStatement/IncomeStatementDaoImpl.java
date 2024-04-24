package com.Anemoi.InvestorRelation.IncomeStatement;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.AnalystDetails.AnalystDetailsDaoException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetDaoException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowQuaryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.DataIngestion.DataIngestionQueryConstant;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton
public class IncomeStatementDaoImpl implements IncomeStatementDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(IncomeStatementDaoImpl.class);

	@SuppressWarnings("resource")
	@Override
	public IncomeStatementEntity createIncomeStatement(IncomeStatementEntity incomestatement, String dataBaseName)
			throws IncomeStatementDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			LOGGER.debug("inserting the data");

			pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_LINEITEM
					.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				existingValues.add(resultSet.getString("lineItem").trim());
			}

			boolean isMatched = existingValues.stream().anyMatch(incomestatement.getLineItem().trim()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(incomestatement.getLineItem().trim())) {
				Date d=new Date();
				pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.INSERT_INTO_INCOMESTATEMENT
						.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String id1 = UUID.randomUUID().toString();
				incomestatement.setIncomeid(id1);
				pstmt.setString(1, incomestatement.getIncomeid());
				pstmt.setString(2, incomestatement.getLineItem().trim());
				pstmt.setString(3, incomestatement.getAlternativeName());
				pstmt.setString(4, incomestatement.getCreatedBy());
				pstmt.setLong(5, d.getTime());
				pstmt.setString(6, null);
				pstmt.setLong(7, d.getTime());
				pstmt.executeUpdate();
			} else {
				throw new IncomeStatementDaoException("Cannot insert Duplicate values");
			}
			return incomestatement;

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new IncomeStatementDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public IncomeStatementEntity getIncomeStatementById(String incomeid, String dataBaseName)
			throws IncomeStatementDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_INCOMESTATEMENT_BY_ID
					.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, incomeid);
			result = pstmt.executeQuery();
			while (result.next()) {
				IncomeStatementEntity incomestatementEntity = buildIncomeStatementDeatils(result);
				return incomestatementEntity;
			}
		} catch (Exception e) {
			LOGGER.error("incomestatement id not found" + e.getMessage());
			throw new IncomeStatementDaoException("unable to get income statement by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	private IncomeStatementEntity buildIncomeStatementDeatils(ResultSet result) throws SQLException {
		IncomeStatementEntity incomestatementEntity = new IncomeStatementEntity();
		incomestatementEntity.setIncomeid(result.getString(IncomeStatementQuaryConstatnt.INCOMEID));
		incomestatementEntity.setLineItem(result.getString(IncomeStatementQuaryConstatnt.LINEITEM));
		incomestatementEntity.setAlternativeName(result.getString(IncomeStatementQuaryConstatnt.ALTERNATIVENAME));
		incomestatementEntity.setCreatedBy(result.getString("createdBy"));
		incomestatementEntity.setCreatedOn(result.getLong("createdOn"));
		incomestatementEntity.setModifiedBy(result.getString("modifiedBy"));
		incomestatementEntity.setModifiedOn(result.getLong("modifiedOn"));
		return incomestatementEntity;
	}

	@Override
	public List<IncomeStatementEntity> getAllIncomeStatementDetails(String dataBaseName)
			throws SQLException, IncomeStatementDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<IncomeStatementEntity> listofincomestatementDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_INCOMESTATEMENT
					.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				IncomeStatementEntity incomestatement = buildIncomeStatementDeatils(result);
				listofincomestatementDetails.add(incomestatement);
			}
			return listofincomestatementDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of incomestatemnt" + e.getMessage());
			e.printStackTrace();
			throw new IncomeStatementDaoException("unable to get income statement" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public IncomeStatementEntity updateIncomeStatementDetails(IncomeStatementEntity incomestatement, String incomeid,
			String dataBaseName) throws IncomeStatementDaoException {

		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();
//		LOGGER.info(".in update incomestatement database name is ::" + dataBaseName + " incomeId is ::" + incomeid
//				+ " request role model is ::" + incomestatement);

		try {

			connection = InvestorDatabaseUtill.getConnection();

			pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_LINEITEM2
					.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, incomeid);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				existingValues.add(resultSet.getString("lineItem").trim());
			}
			boolean isMatched = existingValues.stream().anyMatch(incomestatement.getLineItem().trim()::equalsIgnoreCase);
			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(incomestatement.getLineItem().trim())) {
				Date d=new Date();
				pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.UPDATE_INCOMESTATEMENT
						.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
				pstmt.setString(1, incomestatement.getLineItem().trim());
				pstmt.setString(2, incomestatement.getAlternativeName());
				pstmt.setString(3, incomestatement.getModifiedBy());
                pstmt.setLong(4, d.getTime());
				pstmt.setString(5, incomeid);
				pstmt.executeUpdate();
			} else {
				throw new IncomeStatementDaoException("Cannot add Duplicate values");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementDaoException(e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return incomestatement;

	}

	@Override
	public String deleteIncomeStatement(String incomeid, String dataBaseName) throws IncomeStatementDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.DELETE_INCOMESTATEMENT_BY_ID
					.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, incomeid);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " income statement deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementDaoException("unable to delete" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<IncomeStatementEntity> addIncomeStatementObject(ArrayList<IncomeStatementEntity> incomeentity,
			String dataBaseName) throws IncomeStatementDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		String id = null;

		boolean allExistInDatabase = true;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			Iterator it = incomeentity.iterator();
			while (it.hasNext()) {
				IncomeStatementEntity entity = (IncomeStatementEntity) it.next();
				System.out.println("lineitem" + entity.getLineItem());
				pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_LINEITEM
						.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
				ResultSet resultSet = pstmt.executeQuery();
				List<String> existingValues = new ArrayList<>();
				while (resultSet.next()) {

					existingValues.add(resultSet.getString("lineItem"));
				}
				boolean isMatched = existingValues.stream().anyMatch(entity.getLineItem()::equalsIgnoreCase);
				System.out.println("value" + existingValues);
				if (isMatched == false && !existingValues.contains(entity.getLineItem())) {
					Date d=new Date();
					pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.INSERT_INTO_INCOMESTATEMENT
							.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
					String bid = UUID.randomUUID().toString();
					id = bid;
					entity.setIncomeid(id);
					pstmt.setString(1, entity.getIncomeid());
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
				throw new IncomeStatementDaoException("All values already exist in database. ");

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new IncomeStatementDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return incomeentity;

	}

	@Override
	public ArrayList<IncomeStatementEntity> uploadExcelSheetIncomeTable(String createdBy,CompletedFileUpload file, String dataBaseName)
			throws IncomeStatementDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ArrayList<String> list = new ArrayList<>();
         ArrayList<IncomeStatementEntity> alreadyPreasentList=new ArrayList<>();
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

					throw new IncomeStatementDaoException("value should not empty or null ");

				}
				pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_LINEITEM
						.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
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
						throw new IncomeStatementDaoException("value should not empty or null ");
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

					pstmt = connection.prepareStatement(IncomeStatementQuaryConstatnt.INSERT_INTO_INCOMESTATEMENT
							.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
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
					IncomeStatementEntity entity=new IncomeStatementEntity();
					entity.setLineItem(lineItemName);
					alreadyPreasentList.add(entity);
				}
			}

			if (allExistInDatabase) {
				System.out.println("All values already exist in database.");
				throw new IncomeStatementDaoException("All values already exist in database. ");

			}
			return alreadyPreasentList;
		} catch (Exception e) {
			// TODO: handle exception
			throw new IncomeStatementDaoException(e.getMessage());
		}
		 finally {
				LOGGER.info("closing the connections");
				InvestorDatabaseUtill.close(pstmt, connection);
			}
	}

	@Override
	public ArrayList<IncomeStatementEntity> getIncomeLineItems(long startTimestamp, long endTimestamp,
			String dataBaseName) {
		Connection con=null;
		PreparedStatement psta=null;
		ResultSet rs=null;
		ArrayList<IncomeStatementEntity> lineItemList=new ArrayList<>();
	
		try
		{
			con=InvestorDatabaseUtill.getConnection();
			psta=con.prepareStatement(IncomeStatementQuaryConstatnt.SELECT_LINEITEMFORNOTIFICATION.replace(IncomeStatementQuaryConstatnt.DATA_BASE_PLACE_HOLDER, dataBaseName));
		psta.setLong(1, startTimestamp);
		psta.setLong(2, endTimestamp);
			rs=psta.executeQuery();
		
			while(rs.next())
			{
				IncomeStatementEntity entity=new IncomeStatementEntity();
				entity.setLineItem(rs.getString("lineItem"));
				entity.setCreatedBy(rs.getString("createdBy"));
				lineItemList.add(entity);
				
			}
			return lineItemList;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	}
