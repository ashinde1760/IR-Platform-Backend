package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.crypto.spec.PSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemDaoException;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemQueryConstant;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemServiceException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton

public class ShareHolderDataFromDaoImpl implements ShareHoderDataFromDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShareHolderDataFromDaoImpl.class);

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	private static String DATABASENAME = "databaseName";

	@SuppressWarnings("resource")
	@Override
	public ShareHolderDataFromEntity createNewShareHolderDataForm(ShareHolderDataFromEntity shareholderdataform,
			String dataBaseName) throws ShareHolderDataFormDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			LOGGER.debug("inserting the data");
			String inputDate = shareholderdataform.getInputDate();

			DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = inputFormat.parse(inputDate);
			String formattedDate = outputFormat.format(date);
			System.out.println("formattedDate" + formattedDate);
			Date dd = outputFormat.parse(formattedDate);
			// Convert the parsed date to a Unix timestamp in milliseconds
			long unixTimestampMillis = dd.getTime();
			System.out.println("Unix Timestamp: " + unixTimestampMillis);

			pstmt = connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_SHAREHOLDERDATAFORM
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			String shareid = UUID.randomUUID().toString();

			shareholderdataform.setShareId(shareid);
			Date d = new Date();
			pstmt.setString(1, shareid);
			pstmt.setString(2, shareholderdataform.getClientName());
			pstmt.setString(3, shareholderdataform.getFolio());
			pstmt.setString(4, shareholderdataform.getShareholderName());
			pstmt.setString(5, shareholderdataform.getHoldingValue());
			pstmt.setString(6, shareholderdataform.getHoldingPercentage());
			pstmt.setString(7, shareholderdataform.getHoldingCost());
			pstmt.setString(8, shareholderdataform.getMinorCode());
			pstmt.setString(9, shareholderdataform.getFundGroup());
			pstmt.setLong(10, unixTimestampMillis);
			pstmt.setString(11, shareholderdataform.getCreatedBy());
			pstmt.setLong(12, d.getTime());
			pstmt.setString(13, null);
			pstmt.setLong(14, d.getTime());
			pstmt.executeUpdate();

			return shareholderdataform;
		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new ShareHolderDataFormDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ShareHolderDataFromEntity getShareHolderDataFormById(String shareid, String dataBaseName)
			throws ShareHolderDataFormDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT__SHAREHOLDERDATAFORM_BY_ID
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, shareid);
			result = pstmt.executeQuery();
			while (result.next()) {
				ShareHolderDataFromEntity shareholderEntity = buildshareholderdataformDeatils(result);
				return shareholderEntity;
			}
		} catch (Exception e) {
			LOGGER.error("share holder data form not found" + e.getMessage());
			throw new ShareHolderDataFormDaoException("unable to get share holder data" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	private ShareHolderDataFromEntity buildshareholderdataformDeatils(ResultSet result) throws SQLException {

		ShareHolderDataFromEntity shareholderdataformEntity = new ShareHolderDataFromEntity();
		shareholderdataformEntity.setShareId(result.getString(ShareHolderDataFromQuaryConstant.SHAREID));
		shareholderdataformEntity.setClientName(result.getString(ShareHolderDataFromQuaryConstant.CLIENTNAME));
		shareholderdataformEntity.setFolio(result.getString(ShareHolderDataFromQuaryConstant.FOLIO));
		shareholderdataformEntity
				.setShareholderName(result.getString(ShareHolderDataFromQuaryConstant.SHAREHOLDERNAME));
		shareholderdataformEntity.setHoldingValue(result.getString(ShareHolderDataFromQuaryConstant.HOLDINGVALUE));
		shareholderdataformEntity
				.setHoldingPercentage(result.getString(ShareHolderDataFromQuaryConstant.HOLDINGPERCENTAGE));
		shareholderdataformEntity.setHoldingCost(result.getString(ShareHolderDataFromQuaryConstant.HOLDINGCOST));
		shareholderdataformEntity.setMinorCode(result.getString(ShareHolderDataFromQuaryConstant.MINORCODE));
		shareholderdataformEntity.setFundGroup(result.getString(ShareHolderDataFromQuaryConstant.FUNDGROUP));
		shareholderdataformEntity.setDate(result.getLong(ShareHolderDataFromQuaryConstant.DATE));
		shareholderdataformEntity.setCreatedBy(result.getString("createdBy"));
		shareholderdataformEntity.setCreatedOn(result.getLong("createdOn"));
		shareholderdataformEntity.setModifiedBy(result.getString("modifiedBy"));
		shareholderdataformEntity.setModifiedOn(result.getLong("modifiedOn"));
//		shareholderdataformEntity.setCreatedOnFormatted(result.getString("createdOnFormatted"));

		return shareholderdataformEntity;
	}

	@Override
	public List<ShareHolderDataFromEntity> getAllShareHolderDataFormDetails(String dataBaseName)
			throws SQLException, ShareHolderDataFormDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<ShareHolderDataFromEntity> listofshareholderdataformDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_SHAREHOLDERDATAFORM
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				ShareHolderDataFromEntity cashflow = buildshareholderdataformDeatils(result);
				listofshareholderdataformDetails.add(cashflow);
			}
			return listofshareholderdataformDetails;
		} catch (Exception e) {
			LOGGER.error("unable to get list of share holder data form" + e.getMessage());
			e.printStackTrace();
			throw new ShareHolderDataFormDaoException("unable to get share holder data" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public ShareHolderDataFromEntity updateShareHolderDAtaFormDetails(ShareHolderDataFromEntity shareholderdataform,
			String shareid, String dataBaseName) {
		Connection connection = null;
		PreparedStatement pstmt = null;
//		LOGGER.info(".in update shareholder dataform database name is ::" + dataBaseName + " cashId is ::" + shareid
//				+ " request cash flow is ::" + shareholderdataform);

		try {

			connection = InvestorDatabaseUtill.getConnection();

			String inputDate = shareholderdataform.getInputDate();

			DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = inputFormat.parse(inputDate);
			String formattedDate = outputFormat.format(date);
			System.out.println("formattedDate" + formattedDate);
			Date dd = outputFormat.parse(formattedDate);
			// Convert the parsed date to a Unix timestamp in milliseconds
			long unixTimestampMillis = dd.getTime();
			System.out.println("Unix Timestamp: " + unixTimestampMillis);
			pstmt = connection.prepareStatement(ShareHolderDataFromQuaryConstant.UPDATE_SHAREHOLDERDATAFORM
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Date d = new Date();
			pstmt.setString(1, shareholderdataform.getClientName());
			pstmt.setString(2, shareholderdataform.getFolio());
			pstmt.setString(3, shareholderdataform.getShareholderName());
			pstmt.setString(4, shareholderdataform.getHoldingValue());
			pstmt.setString(5, shareholderdataform.getHoldingPercentage());
			pstmt.setString(6, shareholderdataform.getHoldingCost());
			pstmt.setString(7, shareholderdataform.getMinorCode());
			pstmt.setString(8, shareholderdataform.getFundGroup());
			pstmt.setLong(9, unixTimestampMillis);
			pstmt.setString(10, shareholderdataform.getModifiedBy());
			pstmt.setLong(11, d.getTime());
			pstmt.setString(12, shareid);

			int executeUpdate = pstmt.executeUpdate();

			System.out.println(executeUpdate);
			LOGGER.info(executeUpdate + " shareholder datform updated successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return shareholderdataform;
	}

	@Override
	public String deleteShareHolderDataForm(String shareid, String dataBaseName) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderDataFromQuaryConstant.DELETE_SHAREHOLDERDATAFORM_BY_ID
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, shareid);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " shareholder dataform deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public String uploadShareHolderDataExcelSheet(String createdBy, CompletedFileUpload file, String dataBaseName)
			throws ShareHolderDataFormDaoException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			DataFormatter formatter = new DataFormatter();
			byte[] fileContent = file.getBytes();

			ByteArrayInputStream targetStream = new ByteArrayInputStream(fileContent);

			XSSFWorkbook workbook = new XSSFWorkbook(targetStream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			int numRows = sheet.getLastRowNum() + 1;
			if (numRows <= 1) {
				throw new Exception("Excel sheet is empty");
			}

			int rownum1 = 0;

			for (Row row1 : sheet) {

				if (rownum1 == 0) {

					rownum1++;
					continue;
				}

				String clientName = (formatter.formatCellValue(row1.getCell(0)).trim());
				String HoldingPercentage = formatter.formatCellValue(row1.getCell(4));
				HoldingPercentage = HoldingPercentage.replaceAll("%", "");
				System.out.println("HoldingPercentage: ====>" + HoldingPercentage);
				double percentageValue = Double.parseDouble(HoldingPercentage);
				if (percentageValue == 100 || percentageValue > 100 || percentageValue < 0) {
					throw new ShareHolderDataFormDaoException("% to EQT must be greater than 0 and less than 100. ");

				} else {
					System.out.println("skip");
				}

				if (clientName.isEmpty()) {

					throw new ShareHolderDataFormDaoException("values Should not be empty or null ");

				}
				

				statement = connection.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_CLIENTNAME
						.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				rs = statement.executeQuery();
				List<String> analystnameList = new ArrayList<>();
				while (rs.next()) {
					analystnameList.add(rs.getString("clientName"));
				}
				boolean isMatched = analystnameList.stream().anyMatch(clientName::equalsIgnoreCase);
				if (isMatched == false && !analystnameList.contains(clientName)) {
					throw new ShareHolderDataFormDaoException(clientName + "," + "not present in client details table");
				}
			}

			int rownum = 0;

			for (Row row1 : sheet) {
				if (rownum == 0) {
					rownum++;
					continue;
				}

				String clientName = formatter.formatCellValue(row1.getCell(0));
				String Folio = formatter.formatCellValue(row1.getCell(1));
				String ShareholderName = formatter.formatCellValue(row1.getCell(2));
				String HoldingValue = formatter.formatCellValue(row1.getCell(3));
				String HoldingPercentage = formatter.formatCellValue(row1.getCell(4));
//				String holdingCost=formatter.formatCellValue(row1.getCell(5));
				String holdingCost = "1";
				String MinorCode = formatter.formatCellValue(row1.getCell(5));
				String fundgroup = formatter.formatCellValue(row1.getCell(6));
				Cell dateCell = row1.getCell(7);
				Date inputDate = formatRawDate(dateCell);
				applyValidation(clientName, Folio, ShareholderName, HoldingValue, HoldingPercentage, MinorCode,
						 fundgroup);

				String regex = "^[1-9]\\d*$";

				if (!HoldingValue.replaceAll(",", "").matches(regex)) {
					System.out.println("HoldingValue===>" + HoldingValue.replaceAll(",", ""));
					throw new ShareHolderDataFormDaoException(
							"Number of share should not be accept zero,alphabhet and decimal values");
				}

				
				long unixTimestampMillis = inputDate.getTime();
				System.out.println("Unix Timestamp: " + unixTimestampMillis);

				Date d = new Date();
				statement = connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_SHAREHOLDERDATAFORM
						.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String shareid = UUID.randomUUID().toString();
				statement.setString(1, shareid);
				statement.setString(2, clientName);
				statement.setString(3, Folio);
				statement.setString(4, ShareholderName);
				statement.setString(5, HoldingValue);
				statement.setString(6, HoldingPercentage);
				statement.setString(7, holdingCost);
				statement.setString(8, MinorCode);
				statement.setString(9, fundgroup);
				statement.setLong(10, unixTimestampMillis);
				statement.setString(11, createdBy);
				statement.setLong(12, d.getTime());
				statement.setString(13, null);
				statement.setLong(14, d.getTime());
				statement.executeUpdate();
				rownum++;

			}
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "share holder data excel sheet uploaded successfully");

			return reposneJSON.toString();
		} catch (Exception e) {
			throw new ShareHolderDataFormDaoException(e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(statement, connection);
		}

	}
	
	
	
//	public String uploadShareHolderDataExcelSheet(String createdBy, CompletedFileUpload file, String dataBaseName)
//	        throws ShareHolderDataFormDaoException {
//	    Connection connection = null;
//	    PreparedStatement statement = null;
//	    ResultSet rs = null;
//	    try {
//	    	connection = InvestorDatabaseUtill.getConnection();
//			DataFormatter formatter = new DataFormatter();
//			byte[] fileContent = file.getBytes();
//
//			ByteArrayInputStream targetStream = new ByteArrayInputStream(fileContent);
//
//			XSSFWorkbook workbook = new XSSFWorkbook(targetStream);
//
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			System.out.println("check 1");
//			int numRows = sheet.getLastRowNum() + 1;
//			if (numRows <= 1) {
//				throw new Exception("Excel sheet is empty");
//			}
//			System.out.println("check 2");
//
//			int rownum1 = 0;
//
//			for (Row row1 : sheet) {
//
//				if (rownum1 == 0) {
//
//					rownum1++;
//					continue;
//				}
//				System.out.println("check 3");
//
//	        String clientName = (formatter.formatCellValue(row1.getCell(0)).trim());
//			String HoldingPercentage = formatter.formatCellValue(row1.getCell(4));
//			HoldingPercentage = HoldingPercentage.replaceAll("%", "");
//			System.out.println("HoldingPercentage: ====>" + HoldingPercentage);
//			double percentageValue = Double.parseDouble(HoldingPercentage);
//			if (percentageValue == 100 || percentageValue > 100 || percentageValue < 0) {
//				System.out.println("check 4");
//
//				throw new ShareHolderDataFormDaoException("% to EQT must be greater than 0 and less than 100. ");
//
//			} else {
//				System.out.println("skip");
//			}
//			System.out.println("check 5");
//
//			if (clientName.isEmpty()) {
//				System.out.println("check 6");
//
//				throw new ShareHolderDataFormDaoException("values Should not be empty or null ");
//
//			}
//			System.out.println("check 7");
//
//
//			statement = connection.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_CLIENTNAME
//					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//			rs = statement.executeQuery();
//			List<String> analystnameList = new ArrayList<>();
//			while (rs.next()) {
//				System.out.println("check 8");
//
//				analystnameList.add(rs.getString("clientName"));
//			}
//			boolean isMatched = analystnameList.stream().anyMatch(clientName::equalsIgnoreCase);
//			if (isMatched == false && !analystnameList.contains(clientName)) {
//				System.out.println("check 9");
//
//				throw new ShareHolderDataFormDaoException(clientName + "," + "not present in client details table");
//			}
//		
//	        int batchSize = 0;
//	        statement = connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_SHAREHOLDERDATAFORM
//	                .replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//	        for (Row row2 : sheet) {
//				System.out.println("check 10");
//
//	            if (batchSize == 0) {
//	    			System.out.println("check 11");
//
//	                connection.setAutoCommit(false); // Start batch processing
//	            }
//	            if (batchSize == 10000) {
//	    			System.out.println("check 12");
//
//	                statement.executeBatch(); // Execute batch
//	                connection.commit(); // Commit batch
//	                batchSize = 0;
//	            }
//	            if (batchSize == 0) {
//	    			System.out.println("check 13");
//
//	                statement.clearBatch(); // Clear batch
//	            }
//	            if (batchSize == 0) { // Set parameters only for the first statement in batch
//	    			System.out.println("check 14");
//
//	                 clientName = formatter.formatCellValue(row2.getCell(0));
//	                String Folio = formatter.formatCellValue(row2.getCell(1));
//	                String ShareholderName = formatter.formatCellValue(row2.getCell(2));
//	                String HoldingValue = formatter.formatCellValue(row2.getCell(3));
//	                 HoldingPercentage = formatter.formatCellValue(row2.getCell(4));
//	                String holdingCost = "1"; // Assuming this is constant for each row
//	                String MinorCode = formatter.formatCellValue(row2.getCell(5));
//	                String fundgroup = formatter.formatCellValue(row2.getCell(6));
//	                Cell dateCell = row2.getCell(7);
//	                Date inputDate = formatRawDate(dateCell);
//	                applyValidation(clientName, Folio, ShareholderName, HoldingValue, HoldingPercentage, MinorCode,
//	                        fundgroup);
//	                String regex = "^[1-9]\\d*$";
//	                if (!HoldingValue.replaceAll(",", "").matches(regex)) {
//	        			System.out.println("check 15");
//
//	                    throw new ShareHolderDataFormDaoException(
//	                            "Number of share should not be accept zero,alphabhet and decimal values");
//	                }
//	                long unixTimestampMillis = inputDate.getTime();
//	                Date d = new Date();
//	                statement.setString(1, UUID.randomUUID().toString());
//	                statement.setString(2, clientName);
//	                statement.setString(3, Folio);
//	                statement.setString(4, ShareholderName);
//	                statement.setString(5, HoldingValue);
//	                statement.setString(6, HoldingPercentage);
//	                statement.setString(7, holdingCost);
//	                statement.setString(8, MinorCode);
//	                statement.setString(9, fundgroup);
//	                statement.setLong(10, unixTimestampMillis);
//	                statement.setString(11, createdBy);
//	                statement.setLong(12, d.getTime());
//	                statement.setString(13, null);
//	                statement.setLong(14, d.getTime());
//	                statement.addBatch(); // Add statement to batch
//	                batchSize++;
//	            }
//	        
//	        }
//	        if (batchSize > 0) { // Execute remaining statements in batch
//				System.out.println("check 16");
//
//	            statement.executeBatch();
//	            connection.commit();
//	        }
//			}
//			System.out.println("check 17");
//
//	        JSONObject responseJSON = new JSONObject();
//	        responseJSON.put(STATUS, SUCCESS);
//	        responseJSON.put(MSG, "share holder data excel sheet uploaded successfully");
//	        return responseJSON.toString();
//	    } catch (Exception e) {
//	        throw new ShareHolderDataFormDaoException(e.getMessage());
//	    } finally {
//	        LOGGER.debug("closing the connections");
//	        InvestorDatabaseUtill.close(statement, connection);
//	    }
//	}
	
	

//	private Date formatRawDate(Cell dateCell) throws ShareHolderDataFormDaoException {
//		SimpleDateFormat standardFormat = new SimpleDateFormat("MM-dd-yyyy");
//		Date dateField = null;
//		if(dateCell == null)
//			{
//			System.out.print("\n Date Value is NULL ::: " );
//			throw new ShareHolderDataFormDaoException("Date value should not be null or empty");
//
//			}
//		else if (dateCell.getCellType() == CellType.STRING) {
//			dateField = formatDate(dateCell.getStringCellValue());
//			} 
//		else if (dateCell.getCellType() == CellType.NUMERIC) {
//			dateField = dateCell.getDateCellValue();
//			}
//
//		if (dateField != null)
//			System.out.print("\n Date Value read successfully ::: " 
//						+ standardFormat.format(dateField));	
//	
//		return dateField ;
//	
//	}

//	public String uploadShareHolderDataExcelSheet(String createdBy, CompletedFileUpload file, String dataBaseName)
//	        throws ShareHolderDataFormDaoException {
//		
//	    try (Connection connection = InvestorDatabaseUtill.getConnection();
//	         PreparedStatement selectStatement = connection.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_CLIENTNAME
//	                 .replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//	         PreparedStatement insertStatement = connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_SHAREHOLDERDATAFORM
//	                 .replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName))) {
//
//	        DataFormatter formatter = new DataFormatter();
//	        byte[] fileContent = file.getBytes();
//	        ByteArrayInputStream targetStream = new ByteArrayInputStream(fileContent);
//	        XSSFWorkbook workbook = new XSSFWorkbook(targetStream);
//	        XSSFSheet sheet = workbook.getSheetAt(0);
//
//	        List<String> analystnameList = new ArrayList<>();
//
//	        int rownum = 0;
//	        for (Row row : sheet) {
//	            if (rownum == 0) {
//	                rownum++;
//	                continue;
//	            }
//
//	            String clientName = formatter.formatCellValue(row.getCell(0));
//	            String HoldingPercentage = formatter.formatCellValue(row.getCell(4)).replaceAll("%", "");
//	            double percentageValue = Double.parseDouble(HoldingPercentage);
//	            if (percentageValue == 100 || percentageValue > 100 || percentageValue < 0) {
//	                throw new ShareHolderDataFormDaoException("% to EQT must be greater than 0 and less than 100.");
//	            }
//
//	            if (clientName.isEmpty()) {
//	                throw new ShareHolderDataFormDaoException("Values should not be empty or null.");
//	            }
//
//	            analystnameList.clear();
//	            selectStatement.clearParameters();
//	            ResultSet rs = selectStatement.executeQuery();
//	            while (rs.next()) {
//	                analystnameList.add(rs.getString("clientName"));
//	            }
//	            rs.close();
//
//	            if (!analystnameList.stream().anyMatch(clientName::equalsIgnoreCase)) {
//	                throw new ShareHolderDataFormDaoException(clientName + " not present in client details table.");
//	            }
//
//	            // Set parameters for insert statement
//	            insertStatement.setString(1, UUID.randomUUID().toString());
//	            insertStatement.setString(2, clientName);
//	            insertStatement.setString(3, formatter.formatCellValue(row.getCell(1)));
//	            insertStatement.setString(4, formatter.formatCellValue(row.getCell(2)));
//	            insertStatement.setString(5, formatter.formatCellValue(row.getCell(3)));
//	            insertStatement.setString(6, HoldingPercentage);
//	            insertStatement.setString(7, "1");
//	            insertStatement.setString(8, formatter.formatCellValue(row.getCell(5)));
//	            insertStatement.setString(9, formatter.formatCellValue(row.getCell(6)));
//	            insertStatement.setLong(10, formatRawDate(row.getCell(7)).getTime());
//	            insertStatement.setString(11, createdBy);
//	            long currentTimeMillis = System.currentTimeMillis();
//	            insertStatement.setLong(12, currentTimeMillis);
//	            insertStatement.setString(13, null);
//	            insertStatement.setLong(14, currentTimeMillis);
//
//	            // Add batch
//	            insertStatement.addBatch();
//
//	            rownum++;
//	        }
//
//	        // Execute batch
//	        insertStatement.executeBatch();
//
//	        JSONObject responseJSON = new JSONObject();
//	        responseJSON.put(STATUS, SUCCESS);
//	        responseJSON.put(MSG, "Shareholder data Excel sheet uploaded successfully.");
//	        return responseJSON.toString();
//
//	    } catch (Exception e) {
//	        throw new ShareHolderDataFormDaoException(e.getMessage());
//	    }
//	}

	
	private Date formatDate(String value) throws ShareHolderDataFormDaoException {
		String[] dateFormats = {"dd/MM/yyyy","MM/dd/yyyy","dd.MM.yyyy","MM.dd.yyyy","dd-MM-yyyy","MM-dd-yyyy"};
		Date myDate;
		for (String format : dateFormats) {
			 SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				myDate = sdf.parse(value);
				return myDate;
			} catch (ParseException e) {
			
				throw new ShareHolderDataFormDaoException("Date parsing failed "+format);

			}
	}
		System.out.print("\nNo formats matched ::: :" + value);
		return null;
	}
	private Date formatRawDate(Cell dateCell) throws ShareHolderDataFormDaoException {
	    if (dateCell == null) {
	        return null;
	    }
	    
	    if (dateCell.getCellType() == CellType.NUMERIC) {
	    	System.out.println("check 18");
	        return dateCell.getDateCellValue();
	    } else if (dateCell.getCellType() == CellType.STRING) {
	    	System.out.println("check 19");
	        String value = dateCell.getStringCellValue();
	        String[] dateFormats = {"dd/MM/yyyy","MM/dd/yyyy","dd.MM.yyyy","MM.dd.yyyy","dd-MM-yyyy","MM-dd-yyyy"};
	        for (String format : dateFormats) {
	        	System.out.println("check 20");
	            SimpleDateFormat sdf = new SimpleDateFormat(format);
	            try {
	                return sdf.parse(value);
	            } catch (ParseException e) {
	                // Continue to try other formats
	            }
	        }
	        System.out.println("check 21");
	        throw new ShareHolderDataFormDaoException("Date parsing failed for value: " + value);
	    } else {
	    	System.out.println("check 22");
	        // Handle other cell types if necessary
	        return null;
	    }
	}

	
	private void applyValidation(String clientName, String folio, String shareholderName, String holdingValue,
			String holdingPercentage, String minorCode,  String fundgroup) throws Exception {
		// TODO Auto-generated method stub

		Pattern pattern;
		if (clientName == null || clientName.isEmpty() || folio == null || folio.isEmpty() || shareholderName == null
				|| shareholderName.isEmpty() || holdingValue == null || holdingValue.isEmpty()
				|| holdingPercentage == null || holdingPercentage.isEmpty() || minorCode == null || minorCode.isEmpty()
				||  fundgroup == null || fundgroup.isEmpty()) {
			throw new Exception("value should not be null or empty");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String addMinorCode(MinorCodeEntity codeEntity, String dataBaseName) throws ShareHolderDataFormDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderDataFromQuaryConstant.INSERT_INTO_MINORCODETABLE
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, codeEntity.getMinorCode());
			pstmt.setString(2, codeEntity.getFullform());
			pstmt.executeUpdate();

			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "Minor code list uploaded successfully");

			return reposneJSON.toString();
		} catch (Exception e) {
			throw new ShareHolderDataFormDaoException("minor code uploaded successfully" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	@Override
	public ArrayList<MinorCodeEntity> getMinorCodeList(String dataBaseName) throws ShareHolderDataFormDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<MinorCodeEntity> list = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_MINORCODE_LIST
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				MinorCodeEntity entity = buildData(rs);
				list.add(entity);

			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ShareHolderDataFormDaoException("unable to get minor code" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	private MinorCodeEntity buildData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		MinorCodeEntity entity = new MinorCodeEntity();
		entity.setMinorcodeId(rs.getLong("minorcodeId"));
		entity.setMinorCode(rs.getString("minorCode"));
		entity.setFullform(rs.getString("fullform"));
		return entity;
	}

	@Override
	public ArrayList<ShareHolderDataFromEntity> getCurretDateData(long startTimestamp, long endTimestamp,
			String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<ShareHolderDataFromEntity> list = new ArrayList<>();

		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_CURRENTDATE_DATA
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, startTimestamp);
			psta.setLong(2, endTimestamp);
			rs = psta.executeQuery();

			while (rs.next()) {
				ShareHolderDataFromEntity entity = new ShareHolderDataFromEntity();
				entity.setClientName(rs.getString("clientName"));

				list.add(entity);

			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	@Override
	public ArrayList<String> getMinorCodeNames(String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<>();

		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ShareHolderDataFromQuaryConstant.SELECT_MINORCODE_LIST
					.replace(ShareHolderDataFromQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				String minorCode = rs.getString("minorCode");
				list.add(minorCode);

			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}
}
