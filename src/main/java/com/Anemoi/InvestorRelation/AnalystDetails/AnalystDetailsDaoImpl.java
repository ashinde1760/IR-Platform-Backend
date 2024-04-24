package com.Anemoi.InvestorRelation.AnalystDetails;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemDaoException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetDaoException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetDaoImpl;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowQuaryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.DataIngestion.DataIngestionQueryConstant;
import com.Anemoi.InvestorRelation.RoleModel.RoleModelQuaryContant;
import com.Anemoi.InvestorRelation.ShareHolderContact.ShareHolderContactDaoException;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFormDaoException;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AnalystDetailsDaoImpl implements AnalystDetailsDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(BalanceSheetDaoImpl.class);

	@Inject
	ExcelData excelData;

	private static long analystId;

	@SuppressWarnings("resource")
	@Override
	public AnalystDetailsEntity createAnalystDetails(AnalystDetailsEntity analystDetails, String dataBaseName)
			throws AnalystDetailsDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();
		ResultSet rs = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();

			LOGGER.debug("inserting the data");

			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_ANALSTNAME
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				existingValues.add(resultSet.getString("analystName").trim());
			}

			boolean isMatched = existingValues.stream()
					.anyMatch(analystDetails.getAnalystName().trim()::equalsIgnoreCase);
//			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(analystDetails.getAnalystName().trim())) {

				pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_MAX_ANALYSTID
						.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

				rs = pstmt.executeQuery();
				while (rs.next()) {

					long maxvalue = rs.getLong(1);
					analystId = maxvalue;
					pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.INSERT_INTO_ANALYSTDETAILS
							.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					analystDetails.setAnalystId(analystId + 1);
					Date d = new Date();
					pstmt.setLong(1, analystDetails.getAnalystId());
					pstmt.setString(2, analystDetails.getAnalystName().trim());
					pstmt.setLong(3, d.getTime());
					pstmt.setLong(4, d.getTime());
					pstmt.setString(5, analystDetails.getCreatedBy());
					pstmt.setString(6, null);
					pstmt.executeUpdate();
				}
				List<AnalystContactDetails> contactDetailsList = analystDetails.getAnalystContactDetails();
				for (AnalystContactDetails contactDetails : contactDetailsList) {
					pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.INSERT_INTO_ANALYSTCONTACTDETAILS
							.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					pstmt.setLong(1, analystDetails.getAnalystId());
					pstmt.setString(2, contactDetails.getPocName());
					pstmt.setString(3, contactDetails.getPocEmailId());
					pstmt.executeUpdate();
				}
			} else {
				throw new AnalystDetailsDaoException("Can not insert duplicate Brokarage house details");
			}
			return analystDetails;

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new AnalystDetailsDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public AnalystDetailsEntity getAnalystDetailsById(long analystId, String dataBaseName)
			throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_ANALYSTDETAILS_BY_ID
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, analystId);
			result = pstmt.executeQuery();
			while (result.next()) {
				AnalystDetailsEntity analystEntity = buildAnalystDeatils(result, dataBaseName);
				return analystEntity;
			}
		} catch (Exception e) {
			LOGGER.error("Analyst Details Data not found" + e.getMessage());
			e.printStackTrace();
			throw new AnalystDetailsDaoException("unable to get analyst details by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;

	}

	private AnalystDetailsEntity buildAnalystDeatils(ResultSet result, String dataBaseName)
			throws SQLException, ClassNotFoundException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		try {
		// TODO Auto-generated method stub
		List<AnalystContactDetails> contactDetailsList = new ArrayList<>();
		AnalystDetailsEntity analystentity = new AnalystDetailsEntity();
		analystentity.setAnalystId(result.getLong(AnalystDetailsQueryConstant.ANALYSTID));
		analystentity.setAnalystName(result.getString(AnalystDetailsQueryConstant.ANALYSTNAME));
		analystentity.setCreatedOn(result.getLong(AnalystDetailsQueryConstant.CREATEDON));
		analystentity.setModifiedOn(result.getLong("modifiedOn"));
		analystentity.setCreatedBy(result.getString("createdBy"));
		analystentity.setModifiedBy(result.getString("modifiedBy"));
		 connection = InvestorDatabaseUtill.getConnection();
		 pstmt = connection
				.prepareStatement(AnalystDetailsQueryConstant.SELECT_ANALYSTCONTACTDETIALS_BYID
						.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
		 pstmt.setLong(1, analystentity.getAnalystId());
		 rs = pstmt.executeQuery();
		while (rs.next()) {
			AnalystContactDetails d = new AnalystContactDetails();
			d.setAnalystcontactid(rs.getLong("analystcontactid"));
			d.setPocName(rs.getString("pocName"));
			d.setPocEmailId(rs.getString("pocEmailId"));
			contactDetailsList.add(d);

		}
		analystentity.setAnalystContactDetails(contactDetailsList);
		return  analystentity;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {

			InvestorDatabaseUtill.close(rs, pstmt, connection);		}
		return null;
		

	}

	private AnalystDetailsModified buildModifiedAnalystDeatils(ResultSet result, String dataBaseName)
			throws SQLException, ClassNotFoundException {

		// TODO Auto-generated method stub

		AnalystDetailsModified analystentity = new AnalystDetailsModified();
		analystentity.setAnalystId(result.getLong(AnalystDetailsQueryConstant.ANALYSTID));
		analystentity.setAnalystName(result.getString(AnalystDetailsQueryConstant.ANALYSTNAME));
		analystentity.setCreatedOn(result.getLong(AnalystDetailsQueryConstant.CREATEDON));
		analystentity.setModifiedOn(result.getLong("modifiedOn"));
		analystentity.setCreatedBy(result.getString("createdBy"));
		analystentity.setModifiedBy(result.getString("modifiedBy"));
		analystentity.setAnalystcontactid(result.getLong("analystcontactid"));
		analystentity.setPocName(result.getString("pocName"));
		analystentity.setPocEmailId(result.getString("pocEmailId"));

		return analystentity;

	}

	@Override
	public ArrayList<AnalystDetailsModified> getAllAnalystDetails(String dataBaseName)
			throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		ArrayList<AnalystDetailsModified> listAnalystDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_ANALYSTDETAILS
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
//				AnalystDetailsEntity analystEntity = buildAnalystDeatils(result,dataBaseName);
				AnalystDetailsModified analystEntity = buildModifiedAnalystDeatils(result, dataBaseName);
				listAnalystDetails.add(analystEntity);
			}
			return listAnalystDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of balance Sheet" + e.getMessage());
			e.printStackTrace();
			throw new AnalystDetailsDaoException("unable to get analyst details " + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public AnalystDetailsEntity updateAnalystDetails(AnalystDetailsEntity analystDetails, long analystId,
			String dataBaseName) throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();
		LOGGER.info(".in AnalystDetails database name is ::" + dataBaseName + " AnalystId is ::" + analystId
				+ " request AnalystDetails is ::" + analystDetails);

		try {

			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_ANALSTNAME2
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, analystId);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				existingValues.add(resultSet.getString("analystName"));
			}
			boolean isMatched = existingValues.stream().anyMatch(analystDetails.getAnalystName()::equalsIgnoreCase);
//			System.out.println("value" + existingValues);
			if (isMatched == false && !existingValues.contains(analystDetails.getAnalystName())) {
				pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.UPDATE_ANALYSTDETAILS
						.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				Date date = new Date();
				pstmt.setString(1, analystDetails.getAnalystName());
				pstmt.setLong(2, date.getTime());
				pstmt.setString(3, analystDetails.getModifiedBy());
				pstmt.setLong(4, analystId);

				pstmt.executeUpdate();

				List<AnalystContactDetails> contactDetailsList = analystDetails.getAnalystContactDetails();
				for (AnalystContactDetails contactDetails : contactDetailsList) {
					if (contactDetails.getAnalystcontactid() == 0) {
						pstmt = connection
								.prepareStatement(AnalystDetailsQueryConstant.INSERT_INTO_ANALYSTCONTACTDETAILS
										.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						pstmt.setLong(1, analystId);
						pstmt.setString(2, contactDetails.getPocName());
						pstmt.setString(3, contactDetails.getPocEmailId());
						pstmt.executeUpdate();
					} else {
						pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.UPDATE_ANALYSTCONTACTDETAILS
								.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						System.out
								.println("contactDetails.getAnalystcontactid()" + contactDetails.getAnalystcontactid());
						pstmt.setString(1, contactDetails.getPocName());
						pstmt.setString(2, contactDetails.getPocEmailId());
						pstmt.setLong(3, contactDetails.getAnalystcontactid());
						pstmt.executeUpdate();
					}

				}

			} else {
				throw new AnalystDetailsDaoException("Cannot insert duplicate Values");
			}
			return analystDetails;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsDaoException(e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public String deleteAnalystDetails(long analystId, String dataBaseName) throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.DELETE_ANALYSTDETAILS_BY_ID
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, analystId);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " analystDetails deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsDaoException("unable to delete analyst details by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings("resource")
	@Override
	public ArrayList<AnalystDetailsEntity> uploadAnalystDetailsExcelSheet(String createdBy, CompletedFileUpload file,
			String dataBaseName) throws AnalystDetailsDaoException, Exception {

		Connection connection = null;
		PreparedStatement pstmt = null;
		List<String> existingValues = new ArrayList<>();
		boolean allExistInDatabase = true;
		ArrayList<AnalystDetailsEntity> presentAnalystNameList = new ArrayList<>();
		Date d = new Date();
		AnalystDetailsEntity details = new AnalystDetailsEntity();
		try {

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
			List<String> encounteredEmails = new ArrayList<>();
			for (Row row1 : sheet) {

				if (rownum1 == 0) {

					rownum1++;
					continue;
				}

				String email = (formatter.formatCellValue(row1.getCell(2)));

				boolean exists = this.checkEmailExistsInDatabase(email, dataBaseName);
				System.out.println("exists" + exists);
				if (exists) {

					throw new AnalystDetailsServiceException(email + " email already exists in the database");

				} else {
					System.out.println("Email '" + email + "' does not exist in the database.");
				}

				if (!encounteredEmails.contains(email)) {
					// If not a duplicate, add it to the List
					encounteredEmails.add(email);

					// Process the row here (e.g., insert into the database)
				} else {
					// Handle the duplicate email as needed (e.g., skip the row)
					System.out.println("Duplicate email found: " + email);
					throw new AnalystDetailsDaoException("Duplicate email found in exel sheet");

				}

			}
			connection = InvestorDatabaseUtill.getConnection();

			String filename = file.getFilename();
			String home = System.getProperty("user.home");
			String path = home + "\\" + filename;
			byte[] data = file.getBytes();

			File excel = new File(path);
			FileOutputStream fout = new FileOutputStream(excel);
			fout.write(data);
			fout.close();

			File json = new File(home + "\\output" + ".json");
			String testGenerateJsonFromExcelTemplate = this.excelData.EexcelToJson(excel, json);
			JSONArray jsonArray = new JSONArray(testGenerateJsonFromExcelTemplate);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String analystName = jsonObject.getString("analystName");
				JSONArray analystContactDetails = jsonObject.getJSONArray("analystContactDetails");
				System.out.println("Analyst Name: " + analystName);
				pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_ANALSTNAME
						.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				ResultSet resultSet = pstmt.executeQuery();
				while (resultSet.next()) {
					existingValues.add(resultSet.getString("analystName"));
				}

				boolean isMatched = existingValues.stream().anyMatch(analystName::equalsIgnoreCase);
				System.out.println("value------->" + existingValues);
				if (isMatched == false && !existingValues.contains(analystName)) {
					pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_MAX_ANALYSTID
							.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

					ResultSet rs = pstmt.executeQuery();
					System.out.println("check1");

					while (rs.next()) {
						long maxvalue = rs.getLong(1);

						analystId = maxvalue;
						System.out.println("max value===>"+analystId);

						pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.INSERT_INTO_ANALYSTDETAILS
								.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						details.setAnalystId(analystId + 1);
						pstmt.setLong(1, details.getAnalystId());
						pstmt.setString(2, analystName);
						pstmt.setLong(3, d.getTime());
						pstmt.setLong(4, d.getTime());
						pstmt.setString(5, createdBy);
						pstmt.setString(6, null);
						pstmt.executeUpdate();
						allExistInDatabase = false;
						System.out.println("check2");

					}
					for (int j = 0; j < analystContactDetails.length(); j++) {
						JSONObject contactDetails = analystContactDetails.getJSONObject(j);
						pstmt = connection
								.prepareStatement(AnalystDetailsQueryConstant.INSERT_INTO_ANALYSTCONTACTDETAILS
										.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
						System.out.println("get");
						pstmt.setLong(1, details.getAnalystId());
						pstmt.setString(2, contactDetails.getString("pocName"));
						pstmt.setString(3, contactDetails.getString("pocEmailId"));
						pstmt.executeUpdate();
						allExistInDatabase = false;
						System.out.println("check2");

					}
				} else {

					System.out.println("skip");
					AnalystDetailsEntity detailsEntity = new AnalystDetailsEntity();
					detailsEntity.setAnalystName(analystName);
					presentAnalystNameList.add(detailsEntity);
				}
			}
			if (allExistInDatabase) {
				throw new AnalystDetailsDaoException("Brokerage house value is already exist");
			}

			return presentAnalystNameList;
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystDetailsDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	@Override
	public String deleteAnalystContactDetails(long analystId, long analystcontactid, String dataBaseName)
			throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.DELETE_ANALYSTCONTACTDETAILS
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, analystId);
			pstmt.setLong(2, analystcontactid);
			pstmt.executeUpdate();
			return "analyst contact details deleted suucessfully";
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public boolean checkEmailExistsInDatabase(String email, String dataBaseName) throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_POCEMAILID
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("count" + count);
				return count > 0;

			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	@Override
	public boolean checkEmailExists(String email, long analystId, String dataBaseName)
			throws AnalystDetailsDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(AnalystDetailsQueryConstant.SELECT_POCEMAILIDEXEPETEMAIL
					.replace(AnalystDetailsQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, email);
			pstmt.setLong(2, analystId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("count" + count);
				return count > 0;

			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}
}
