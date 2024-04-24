package com.Anemoi.InvestorRelation.ShareHolderContact;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Anemoi.InvestorRelation.CashFlow.CashFlowDaoException;
import com.Anemoi.InvestorRelation.CashFlow.CashFlowEntity;
import com.Anemoi.InvestorRelation.AnalystDetails.AnalystDetailsServiceException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetQueryConstant;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromEntity;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHolderDataFromQuaryConstant;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

@Singleton
public class ShareHolderContactDaoImpl implements ShareHolderContactDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShareHolderContactDaoImpl.class);

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	@SuppressWarnings("resource")
	@Override
	public ShareHolderContactEntity createNewShareHolderContact(ShareHolderContactEntity shareholdercontact,
			String dataBaseName) throws ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> allEmail = new ArrayList<>();
		ArrayList<String> allcontactList = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.SELECT_EMAIL
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String email = rs.getString("email");
				if (email == null) {
					System.out.println("skip");
				} else {
					allEmail.add(email);
				}

			}
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.SELECT_CONTACT
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String contactNumber = rs.getString("contact");
				if (contactNumber == null) {
					System.out.println("skip");
				} else {
					allcontactList.add(contactNumber);
				}

			}

			if (!allEmail.contains(shareholdercontact.getEmail())
					&& !allcontactList.contains(shareholdercontact.getContact())) {

				Date d = new Date();
				pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.INSERT_INTO_SHAREHOLDERCONTACT
						.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String contactid = UUID.randomUUID().toString();
				shareholdercontact.setContactid(contactid);
				pstmt.setString(1, contactid);
				pstmt.setString(2, shareholdercontact.getName().trim());
				pstmt.setString(3, shareholdercontact.getPoc());
				pstmt.setString(4, shareholdercontact.getEmail());
				pstmt.setString(5, shareholdercontact.getAddress());
				pstmt.setString(6, shareholdercontact.getContact());
				pstmt.setString(7, shareholdercontact.getCreatedBy());
				pstmt.setLong(8, d.getTime());
				pstmt.executeUpdate();
			}
			else if(allEmail.contains(shareholdercontact.getEmail()) && allcontactList.contains(shareholdercontact.getContact())) {
				throw new ShareHolderContactDaoException("Cannot insert duplicate email id and contact number");

			}
			else if(allEmail.contains(shareholdercontact.getEmail())){
				throw new ShareHolderContactDaoException("Cannot insert duplicate email id");

			}else if(allcontactList.contains(shareholdercontact.getContact())) {
				throw new ShareHolderContactDaoException("Cannot insert duplicate contact number");

			}
			return shareholdercontact;

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new ShareHolderContactDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ShareHolderContactEntity getShareHolderContactById(String contactid, String dataBaseName)
			throws ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.SELECT__SHAREHOLDERCONTACT_BY_ID
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, contactid);
			result = pstmt.executeQuery();
			while (result.next()) {
				ShareHolderContactEntity shareholdercontactEntity = buildshareholdercontactDeatils(result);
				return shareholdercontactEntity;
			}
		} catch (Exception e) {
			LOGGER.error("share holder contact not found" + e.getMessage());
			throw new ShareHolderContactDaoException("unable to get share holder by id" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}
		return null;
	}

	private ShareHolderContactEntity buildshareholdercontactDeatils(ResultSet result) throws SQLException {

		ShareHolderContactEntity shareholdercontactEntity = new ShareHolderContactEntity();
		shareholdercontactEntity.setContactid(result.getString(ShareHolderContactQuaryConstant.CONTACTID));
		shareholdercontactEntity.setName(result.getString(ShareHolderContactQuaryConstant.NAME));
		shareholdercontactEntity.setPoc(result.getString(ShareHolderContactQuaryConstant.POC));
		shareholdercontactEntity.setEmail(result.getString(ShareHolderContactQuaryConstant.EMAIL));
		shareholdercontactEntity.setAddress(result.getString(ShareHolderContactQuaryConstant.ADDRESS));
		shareholdercontactEntity.setContact(result.getString(ShareHolderContactQuaryConstant.CONTACT));
		shareholdercontactEntity.setCreatedBy(result.getString("createdBy"));
		shareholdercontactEntity.setCreatedOn(result.getLong("createdOn"));
		return shareholdercontactEntity;
	}

	@Override
	public List<ShareHolderContactEntity> getAllShareHolderContactDetails(String dataBaseName)
			throws SQLException, ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<ShareHolderContactEntity> listofshareholderContactDetails = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.SELECT_SHAREHOLDERCONTACT
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			result = pstmt.executeQuery();
			while (result.next()) {
				ShareHolderContactEntity shareholder = buildshareholdercontactDeatils(result);
				listofshareholderContactDetails.add(shareholder);
			}
			return listofshareholderContactDetails;
		} catch (Exception e) {
			LOGGER.error("unble to get list of share holder contact" + e.getMessage());
			e.printStackTrace();
			throw new ShareHolderContactDaoException("unable to get share holder list" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(result, pstmt, connection);
		}

	}

	@Override
	public ShareHolderContactEntity updateShareHolderContactDetails(ShareHolderContactEntity shareholdercontact,
			String contactid, String dataBaseName) throws ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
//		LOGGER.info(".in update shareholder contact database name is ::" + dataBaseName + " cashId is ::" + contactid
//				+ " request cash flow is ::" + shareholdercontact);

		try {

			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.UPDATE_SHAREHOLDERCONTACT
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Date date = new Date();
			pstmt.setString(1, shareholdercontact.getName());
			pstmt.setString(2, shareholdercontact.getPoc());
			pstmt.setString(3, shareholdercontact.getEmail());
			pstmt.setString(4, shareholdercontact.getAddress());
			pstmt.setString(5, shareholdercontact.getContact());
			pstmt.setString(6, contactid);

			int executeUpdate = pstmt.executeUpdate();

			System.out.println(executeUpdate);
			LOGGER.info(executeUpdate + " shareholder Contact updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactDaoException("unable to update share holder list" + e.getMessage());

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return shareholdercontact;
	}

	@Override
	public String deleteShareHolderContact(String contactid, String dataBaseName) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.DELETE_SHAREHOLDERCONTACT_BY_ID
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, contactid);
			int executeUpdate = pstmt.executeUpdate();
			LOGGER.info(executeUpdate + " shareholder contact deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String uploadShareholderContactExcelsheet(String createdBy, CompletedFileUpload file, String dataBaseName)
			throws ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try

		{
			connection = InvestorDatabaseUtill.getConnection();

			DataFormatter formatter = new DataFormatter();
			byte[] fileContent = file.getBytes();

			ByteArrayInputStream targetStream = new ByteArrayInputStream(fileContent);

			XSSFWorkbook workbook = new XSSFWorkbook(targetStream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			int numRows = sheet.getLastRowNum() + 1;
			System.out.println("numRows" + numRows);
			if (numRows <= 1) {
				System.out.println("excel has no data");
				throw new Exception("Excel sheet is empty");
			}

			int rownum = 0;
			List<String> encounteredEmails = new ArrayList<>();
			List<String> encounteredContact = new ArrayList<>();
			for (Row row1 : sheet) {
				if (rownum == 0) {
					rownum++;
					continue;
				}
				String email = (formatter.formatCellValue(row1.getCell(2)));
				String contact = (formatter.formatCellValue(row1.getCell(4)));
				String poc = (formatter.formatCellValue(row1.getCell(1)));
				System.out.println("poc name" + poc);
				if (!poc.matches("[a-zA-Z ]+")) {
					throw new ShareHolderContactDaoException("Invalid POC name. It should contain only alphabets.");
				}

				if (email.isEmpty() && contact.isEmpty()) {
					throw new ShareHolderContactDaoException("Mandatory to give either Email or Contact number");

				} else {
					boolean existsemail = this.checkEmailExistsInDatabase(email, dataBaseName);
					System.out.println("exists" + existsemail);
					if (existsemail) {

						throw new ShareHolderContactDaoException(email + " email already exists in the database");

					} else {
						System.out.println("Email '" + email + "' does not exist in the database.");
					}

					boolean existscontact = this.checkContactExistsInDatabase(contact, dataBaseName);
					System.out.println("exists" + existscontact);
					if (existscontact) {

						throw new ShareHolderContactDaoException(
								contact + " contact number already exists in the database");

					} else {
						System.out.println("Contact '" + contact + "' does not exist in the database.");
					}

				}
				if (!encounteredEmails.contains(email)) {
					if(!email.isEmpty()) {
					// If not a duplicate, add it to the List
					System.out.println("email" + email);
//					 System.out.println("contact"+contact);

					encounteredEmails.add(email);
					}else {
						System.out.println("skipped email");
					}
//	                    encounteredContact.add(contact);
					// Process the row here (e.g., insert into the database)
				} else {
					// Handle the duplicate email as needed (e.g., skip the row)
					System.out.println("Duplicate email found: " + email);
					throw new ShareHolderContactDaoException("Duplicate email data found in excel sheet");

				}
				if (!encounteredContact.contains(contact)) {
					if(!contact.isEmpty()) {
					System.out.println("contact" + contact);
					encounteredContact.add(contact);
					}else {
						System.out.println("contact skip");
					}
				} else {
					// Handle the duplicate email as needed (e.g., skip the row)
					System.out.println("Duplicate contact found: " + contact);
					throw new ShareHolderContactDaoException("Duplicate contact data found in exel sheet");

				}
			}

			int rownum1 = 0;
			for (Row row1 : sheet) {
				if (rownum1 == 0) {
					rownum1++;
					continue;
				}

				String name = (formatter.formatCellValue(row1.getCell(0)).trim());
				String poc = (formatter.formatCellValue(row1.getCell(1)));
				String email = (formatter.formatCellValue(row1.getCell(2)));
				if (email.isEmpty()) {
					email = "NULL";
				}
				System.out.println("email" + email);
				String address = (formatter.formatCellValue(row1.getCell(3)));
				String contact = (formatter.formatCellValue(row1.getCell(4)));
				if (contact.isEmpty()) {
					contact = "NULL";
				}
				Date d = new Date();
				this.checkempty(name, poc);

				

				pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.INSERT_INTO_SHAREHOLDERCONTACT
						.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				String contactid = UUID.randomUUID().toString();
				pstmt.setString(1, contactid);
				pstmt.setString(2, name);
				pstmt.setString(3, poc);
				pstmt.setString(4, email);
				pstmt.setString(5, address);
				pstmt.setString(6, contact);
				pstmt.setString(7, createdBy);
				pstmt.setLong(8, d.getTime());
				pstmt.executeUpdate();

			}
			JSONObject reposneJSON = new JSONObject();
			reposneJSON.put(STATUS, SUCCESS);
			reposneJSON.put(MSG, "ShareHolder Contact Excel sheet upload successfully");
			return reposneJSON.toString();

		} catch (Exception e) {
			LOGGER.error("unable to  created :");
			e.printStackTrace();
			throw new ShareHolderContactDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	private boolean checkContactExistsInDatabase(String contact, String dataBaseName)
			throws ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.SELECT_CONTACTCOUNT
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, contact);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("count" + count);
				return count > 0;

			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderContactDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	private boolean checkEmailExistsInDatabase(String email, String dataBaseName)
			throws ShareHolderContactDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(ShareHolderContactQuaryConstant.SELECT_EMAILCOUNT
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
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
			throw new ShareHolderContactDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}
	}

	private void checkempty(String name, String poc) throws Exception {
		// TODO Auto-generated method stub
		if (name.isEmpty() || name == null || poc.isEmpty() || poc == null) {

			throw new Exception("value should not be empty or null ");

		}
	}

	@Override
	public ArrayList<ShareHolderContactEntity> getCurrentDateDatabydate(long startTimestamp, long endTimestamp,
			String dataBaseName) {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<ShareHolderContactEntity> list = new ArrayList<>();

		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(ShareHolderContactQuaryConstant.SELECT_CURRENTDATEDATA
					.replace(ShareHolderContactQuaryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, startTimestamp);
			psta.setLong(2, endTimestamp);
			rs = psta.executeQuery();

			while (rs.next()) {
				ShareHolderContactEntity entity = new ShareHolderContactEntity();
				entity.setName(rs.getString("name"));
				entity.setCreatedBy(rs.getString("createdBy"));
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

}
