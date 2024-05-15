package com.Anemoi.InvestorRelation.DataIngestion;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.config.Config;
import com.Anemoi.Resource.Media;
//import com.azure.ai.formrecognizer.DocumentAnalysisClient;
//import com.azure.ai.formrecognizer.DocumentAnalysisClientBuilder;
//import com.azure.ai.formrecognizer.models.AnalyzeResult;
//import com.azure.ai.formrecognizer.models.AnalyzedDocument;
//import com.azure.ai.formrecognizer.models.DocumentLine;
//import com.azure.ai.formrecognizer.models.DocumentOperationResult;
//import com.azure.ai.formrecognizer.models.DocumentPage;
//import com.azure.ai.formrecognizer.models.DocumentTable;
//import com.azure.ai.formrecognizer.models.DocumentTableCell;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;

import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;

import com.azure.ai.formrecognizer.documentanalysis.models.*;

import com.azure.core.credential.AzureKeyCredential;

import com.azure.core.util.BinaryData;

import com.azure.core.util.polling.SyncPoller;

import io.micronaut.context.ApplicationContext;
import io.micronaut.data.model.query.QueryModel.In;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.json.tree.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DataIngestionDaoImpl implements DataIngestionDao {

	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	@Inject
	ApplicationContext context;

	private static long tableid;
	String CMP_Value = null;
	String target_price = null;
	private static long fileId;

	private static final Logger LOGGER = LoggerFactory.getLogger(DataIngestionDaoImpl.class);
	HSSFWorkbook workbook = new HSSFWorkbook();

	@SuppressWarnings({ "resource", "unused" })
	public DataIngestionModel extractFileByFileId(long fileId, String dataBaseName)
			throws SQLException, IOException, ClassNotFoundException, DataIngestionDaoException {
		DataIngestionModel model = new DataIngestionModel();
		Connection con = InvestorDatabaseUtill.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			System.out.println("get file details by fileid");
			statement = con.prepareStatement(DataIngestionQueryConstant.SELECT_DATAINGESTION_FILEDETAILS_BYFILEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			statement.setLong(1, fileId);
			rs = statement.executeQuery();
			while (rs.next()) {
				model.setFileId(rs.getLong("fileId"));
				model.setClient(rs.getString("client"));
				model.setDocumentType(rs.getString("documentType"));
				model.setAnalystName(rs.getString("analystName"));
				model.setPeerName(rs.getString("peerName"));
				model.setCurrency(rs.getString("currency"));
				model.setUnits(rs.getString("units"));
				model.setReportType(rs.getString("reportType"));
				model.setReportDate(rs.getDate("reportDate"));
				model.setFileName(rs.getString("fileName"));
				model.setFileType(rs.getString("fileType"));
				model.setFileData(rs.getBytes("fileData"));
				model.setDenomination(rs.getString("denomination"));
				model.setPagenumbers(rs.getString("pagenumbers"));
				model.setStatus(rs.getString("status"));

			}
			ArrayList<DataIngestionTableModel> modellist = new ArrayList<>();
			int datecount = 0;

			XSSFRow row;
			String extPattern = "(?<!^)[.]" + (".*");

//		final String endpoint = "https://secondformrecognizer001.cognitiveservices.azure.com/";
//		final String key = "8a2c1038c4ee4c87ace5fb24723f3396";
			final String endpoint = ReadPropertiesFile.readOCrKey("endpoint");
			final String key = ReadPropertiesFile.readOCrKey("key");
			System.out.println("endpoint " + endpoint);
			DocumentAnalysisClient client = new DocumentAnalysisClientBuilder().credential(new AzureKeyCredential(key))
					.endpoint(endpoint).buildClient();
			// String modelId = "54a23747-5d44-475d-a3d1-e7b18f047a90";
			String modelId = "prebuilt-document";
			String fileName = model.getFileName();
			String sectionFileName = sanitizeFileName(fileName);
			File document = new File(sectionFileName);
			XSSFWorkbook workbook = new XSSFWorkbook();
			String replacedFileName = document.getName().replaceAll(extPattern, "");
			byte[] fileContent = model.getFileData();

			try (InputStream targetStream = new ByteArrayInputStream(fileContent)) {
				SyncPoller<OperationResult, AnalyzeResult> analyzeDocumentPoller = client.beginAnalyzeDocument(modelId,
						BinaryData.fromBytes(fileContent));
				List<String> cmpvalue = new ArrayList<>();
				List<String> target = new ArrayList<>();
				AnalyzeResult analyzeResult = analyzeDocumentPoller.getFinalResult();
				List<String> extractedDates = new ArrayList<>();
				try {
					for (int d = 0; d <= 1; d++) {
						// Extract lines from page number 1
						DocumentPage pageResult = analyzeResult.getPages().get(d);
						List<DocumentLine> lines = pageResult.getLines();

						for (DocumentLine lineResult : lines) {
							String data = lineResult.getContent() + " ";
//        System.out.println(data);
							Pattern datePattern = (Pattern.compile("\\b\\p{Lu}{1}[a-z]+ \\d{1,2}, \\d{4}\\b"));
							Matcher matcher = datePattern.matcher(data);
							if (matcher.find()) {
								String dateStr = matcher.group();
								DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("MMMM d, yyyy",
										Locale.ENGLISH);
								DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("MMM d, yyyy",
										Locale.ENGLISH);
								DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								LocalDate date;
								try {
									date = LocalDate.parse(dateStr, inputFormatter1);
								} catch (Exception e) {
									date = LocalDate.parse(dateStr, inputFormatter2);
								}

								String newDateStr = date.format(outputFormatter);
								extractedDates.add(newDateStr);
							}
							if (!matcher.find()) {
								// date formate 1 January 2022
								datePattern = Pattern.compile(
										"\\b\\d{1,2}\\s+(?:January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|JUL|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec)\\s+\\d{4}\\b");
								matcher = datePattern.matcher(data);
								if (matcher.find()) {
									String dateStr = matcher.group();
									String dateString = capitalizeFirstLetter(dateStr);
									DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("d MMMM yyyy",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("d MMM yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date;
									try {
										date = LocalDate.parse(dateString, inputFormatter1);
									} catch (Exception e) {
										date = LocalDate.parse(dateString, inputFormatter2);
									}

									String newDateStr = date.format(outputFormatter);
									extractedDates.add(newDateStr);
								}
							}
							if (!matcher.find()) {
								// date formate 31st March, 2021
								datePattern = Pattern.compile("\\d{1,2}(st|nd|rd|th|h)\\s\\w+,\\s\\d{4}");
								matcher = datePattern.matcher(data);
//					System.out.println("call here");
								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("dateStr" + dateStr);
									String dateString = dateStr;
									System.out.println("dateString" + dateString);
									String dateStringWithoutOrdinal = dateString.replaceAll("(st|nd|rd|th)", "");
									DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("d MMMM, yyyy",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("d MMM, yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date;
									try {
										date = LocalDate.parse(dateStringWithoutOrdinal, inputFormatter1);
									} catch (Exception e) {
										date = LocalDate.parse(dateStringWithoutOrdinal, inputFormatter2);
									}

									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);

								}
							}
							if (!matcher.find()) {
								// date formate 31st March 2021
								datePattern = Pattern.compile("\\d{1,2}(st|nd|rd|th|h)\\s\\w+\\s\\d{4}");
								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									String dateString = dateStr;
									System.out.println("dateString2" + dateString);
									DateTimeFormatter inputFormatter1 = DateTimeFormatter
											.ofPattern("dd['st']['nd']['rd']['th']['h'] MMMM yyyy", Locale.ENGLISH);
									DateTimeFormatter inputFormatter = DateTimeFormatter
											.ofPattern("d['st']['nd']['rd']['th']['h'] MMM yyyy", Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter
											.ofPattern("dd['st']['nd']['rd']['th']['h'] MMM yyyy", Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date;
									try {
										date = LocalDate.parse(dateStr, inputFormatter1);
									} catch (Exception e) {
										try {
											date = LocalDate.parse(dateStr, inputFormatter);
										} catch (Exception ex) {
											date = LocalDate.parse(dateStr, inputFormatter2);
										}
									}
									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);

								}

							}
							if (!matcher.find()) {
								// date formate January 1, 2022

								datePattern = Pattern.compile(
										"\\b\\d{1,2}\\s+(?:January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec),\\s+\\d{4}\\b");
								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("datestr 4" + dateStr);
									String dateString = dateStr;
									DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("d MMMM',' yyyy",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("d MMM',' yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date;
									try {
										date = LocalDate.parse(dateStr, inputFormatter1);
									} catch (Exception e) {
										date = LocalDate.parse(dateStr, inputFormatter2);
									}

									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);
								}
							}
							if (!matcher.find()) {
								// date formate JANUARY 15, 2022
								datePattern = Pattern.compile(
										"\\b(?:JANUARY|FEBRUARY|MARCH|APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER)\\s+[0-9]{1,2},\\s+[0-9]{4}\\b");
								matcher = datePattern.matcher(data);
								DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

								if (matcher.find()) {
									String dateStr = matcher.group();
									String dateString = dateStr;
									System.out.println("dateString" + dateString);
									String[] dateComponents = dateString.split(" ");
									String month = dateComponents[0];
									int day = Integer.parseInt(dateComponents[1].replace(",", ""));
									int year = Integer.parseInt(dateComponents[2]);

									// Create a LocalDate object using the extracted components
									LocalDate date = LocalDate.of(year, getMonthNumber(month), day);

									// Format the parsed date as yyyy-MM-dd
									String formattedDate = date.format(outputFormatter);

									System.out.println("Formatted Date: " + formattedDate);
									extractedDates.add(formattedDate);
								}

							}

							if (!matcher.find()) {

								// date formate 31 March, 2022
								datePattern = Pattern.compile(
										"\\b\\d{1,2}\\s+(?:January|February|March|April|May|June|July|August|September|October|November|December)\\s*,\\s*\\d{4}\\b");

								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("datestr 5" + dateStr);
									String dateString = dateStr;
									DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date = LocalDate.parse(dateStr, inputFormatter);
									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);
								}
							}

							if (!matcher.find()) {
								// date formate January 1 2022
								datePattern = Pattern.compile(
										"\\b(?:January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec)\\s+\\d{2}\\s+\\d{4}\\b");
								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									String dateString = dateStr;
									DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("MMMM dd yyyy",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("MMM dd yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date;
									try {
										date = LocalDate.parse(dateStr, inputFormatter1);
									} catch (Exception e) {
										date = LocalDate.parse(dateStr, inputFormatter2);
									}

									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);
								}
							}

							if (!matcher.find()) {
								// date formate JANUARY 15, 2022
								datePattern = Pattern.compile(
										"\\b(?:JANUARY|FEBRUARY|MARCH|APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER)\\s+[0-9]{1,2},\\s+[0-9]{4}\\b");
								matcher = datePattern.matcher(data);
								DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

								if (matcher.find()) {
									String dateStr = matcher.group();
									String dateString = dateStr;
									System.out.println("dateString" + dateString);
									String[] dateComponents = dateString.split(" ");
									String month = dateComponents[0];
									int day = Integer.parseInt(dateComponents[1].replace(",", ""));
									int year = Integer.parseInt(dateComponents[2]);

									// Create a LocalDate object using the extracted components
									LocalDate date = LocalDate.of(year, getMonthNumber(month), day);

									// Format the parsed date as yyyy-MM-dd
									String formattedDate = date.format(outputFormatter);

									System.out.println("Formatted Date: " + formattedDate);
									extractedDates.add(formattedDate);
								}

							}

							if (!matcher.find()) {

								// date formate 31 March, 2022
								datePattern = Pattern.compile(
										"\\b\\d{1,2}\\s+(?:January|February|March|April|May|June|July|August|September|October|November|December)\\s*,\\s*\\d{4}\\b");

								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("datestr 5" + dateStr);
									String dateString = dateStr;
									DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date = LocalDate.parse(dateStr, inputFormatter);
									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);
								}
							}

							if (!matcher.find()) {

								// date formate 31-March-21
								datePattern = Pattern.compile(
										"\\b\\d{1,2}-(?:January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec)-\\d{2}\\b");
								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("datestr 5" + dateStr);
									String dateString = dateStr;
									DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("dd-MMMM-YY",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("dd-MMM-YY",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter3 = DateTimeFormatter.ofPattern("dd-MMM-yy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date = null;
									try {
										date = LocalDate.parse(dateStr, inputFormatter1);
									} catch (Exception e1) {
										try {
											date = LocalDate.parse(dateStr, inputFormatter2);
										} catch (Exception e2) {
											try {
												date = LocalDate.parse(dateStr, inputFormatter3);
											} catch (Exception e3) {
												// Handle parsing failure
											}
										}
									}

									String newDateStr = date.format(outputFormatter);
									extractedDates.add(newDateStr);
								}
							}
							if (!matcher.find()) {

								// date formate 31-March-2020
								datePattern = Pattern.compile(
										"\\b\\d{1,2}-(?:January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec)-\\d{4}\\b");
								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("datestr 5" + dateStr);
									String dateString = dateStr;
									DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("dd-MMMM-yyyy",
											Locale.ENGLISH);
									DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy",
											Locale.ENGLISH);
									DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									LocalDate date;
									try {
										date = LocalDate.parse(dateStr, inputFormatter1);
									} catch (Exception e) {
										date = LocalDate.parse(dateStr, inputFormatter2);
									}

									String newDateStr = date.format(outputFormatter);

									extractedDates.add(newDateStr);
								}
							}

							if (!matcher.find()) {
								// date formate 31.03.2022

								datePattern = Pattern.compile("\\b\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\b");
								matcher = datePattern.matcher(data);

								if (matcher.find()) {
									String dateStr = matcher.group();
									System.out.println("datestr 6" + dateStr);
									String dateString = dateStr;
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
									LocalDate date = LocalDate.parse(dateString, formatter);
									DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									String newDateStr = date.format(newFormatter);
									extractedDates.add(newDateStr);
								}

							}

							else {
								System.out.println("date not found...!!");

							}

						}
					}
				} catch (Exception e) {
					System.out.println("page not found");
				}
				boolean dateMatcher = context.get("my.config.dateMatcher", Boolean.class).orElse(false);
				if (dateMatcher) {
					System.out.println("extractedDates: " + extractedDates);
					String dateStr1 = model.getReportDate().toString();

					System.out.println("income date" + dateStr1);

					if (extractedDates.contains(dateStr1)) {
						datecount++;
					}

				} else if (!dateMatcher) {
					datecount = 1;
				}

				if (datecount > 0) {
					System.out.println("datecount>0" + datecount);

					// ************************************

					////// this is pwc get cmp target and rating this code comment in anemoi

					AzureKeyCredential credential = new AzureKeyCredential(key);

					DocumentAnalysisClient documentAnalysisClient = new DocumentAnalysisClientBuilder()
							.credential(credential)

							.endpoint(endpoint).buildClient();

					String modelId_CM = "Model_v2.0";

					System.out.println("file name:" + model.getFileName());

					byte[] fileContent_CM = model.getFileData();

					InputStream targetStream_CM = new ByteArrayInputStream(fileContent);

					SyncPoller<OperationResult, AnalyzeResult> analyzeLayoutResultPoller = documentAnalysisClient

							.beginAnalyzeDocument(modelId_CM, BinaryData.fromBytes(fileContent));

					AnalyzeResult analyzeLayoutResult = analyzeLayoutResultPoller.getFinalResult();
					AtomicReference<String> ratingValue = new AtomicReference<>(null);

					String cmp = null;
					String targetPrice = null;
					String rating = null;

					for (int i = 0; i < analyzeLayoutResult.getDocuments().size(); i++) {
						final AnalyzedDocument analyzedDocument = analyzeLayoutResult.getDocuments().get(i);

						if (analyzedDocument.getFields().containsKey("CMP")) {
							cmp = analyzedDocument.getFields().get("CMP").getContent();

						}
						if (analyzedDocument.getFields().containsKey("Target Price")) {
							targetPrice = analyzedDocument.getFields().get("Target Price").getContent();

						}
						if (analyzedDocument.getFields().containsKey("Rating")) {
							rating = analyzedDocument.getFields().get("Rating").getContent();

						}
					}

					// Define a regex pattern to match numeric values
					Pattern pattern_cr = Pattern.compile("\\d+(?:[,.]\\d+)*");
					String numericValueCmp = null;
					String numericValueTargetPrice = null;

					if (cmp != null) {
						// Create a Matcher object
						Matcher matcher = pattern_cr.matcher(cmp);

						// Find and print the numeric value(s)
						while (matcher.find()) {
							numericValueCmp = matcher.group();
						}
					}

					if (targetPrice != null) {
						// Create a Matcher object
						Matcher matcher1 = pattern_cr.matcher(targetPrice);

						// Find and print the numeric value(s)
						while (matcher1.find()) {
							numericValueTargetPrice = matcher1.group();
						}
					}

					System.out.println("Rating:" + rating);
					System.out.println("CMP:" + numericValueCmp);
					System.out.println("Target Price:" + numericValueTargetPrice);

					/// **********************

					String date = model.getReportDate().toString();
					statement = con.prepareStatement(DataIngestionQueryConstant.INSERT_FORCASTTABLE
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
//				statement.setLong(1, fileId);
//				statement.setString(2, null);
//				statement.setString(3, null);
//				statement.setString(4, date);
//				statement.setString(5, null);
					// remove comment in pwc
					statement.setLong(1, fileId);
					statement.setString(2, numericValueCmp);
					statement.setString(3, numericValueTargetPrice);
					statement.setString(4, date);
					if(rating.toUpperCase().contains("BUY"))
					{
					statement.setString(5, "BUY");
					}
					else if(rating.toUpperCase().contains("OUTPERFORM"))
					{
						statement.setString(5, "OUTPERFORM");
					}
					else if(rating.toUpperCase().contains("ADD"))
					{
						statement.setString(5, "ADD");
					}
					else if(rating.toUpperCase().contains("HOLD"))
					{
						statement.setString(5, "HOLD");
					}
					else if(rating.toUpperCase().contains("NEUTRAL"))
					{
						statement.setString(5, "NEUTRAL");
					}
					else if(rating.toUpperCase().contains("REDUCE"))
					{
						statement.setString(5, "REDUCE");
					}
					else if(rating.toUpperCase().contains("SELL"))
					{
						statement.setString(5, "SELL");
					}
					else if(rating.toUpperCase().contains("UNDERPERFORM"))
					{
						statement.setString(5, "UNDERPERFORM");
					}
					else if(rating.toUpperCase().contains("NR"))
					{
						statement.setString(5, "NR");
					}
					else if(rating.toUpperCase().contains("RESTRICTED"))
					{
						statement.setString(5, "RESTRICTED");
					}
					else if(rating.toUpperCase().contains("ACCUMULATE"))
					{
						statement.setString(5, "ACCUMULATE");
					}
					else if(rating.toUpperCase().contains("LONG"))
					{
						statement.setString(5, "LONG");
					}
					else
					{
						if(rating==null)
						{
							statement.setString(5, rating);
						}else
						{
						statement.setString(5, rating.toUpperCase());
						}
					}
					statement.executeUpdate();
					CMP_Value = null;
					int incomeSheetCount = 1;
					int balanceSheetCount = 1;
					int cashFlowCount = 1;
					int sheetcount = 1;

					List<DocumentTable> tables = analyzeResult.getTables();
					for (int i = 0; i < tables.size(); i++) {

						DocumentTable documentTable = tables.get(i);

						System.out.printf("Table %d has %d rows and %d columns.%n", i, documentTable.getRowCount(),
								documentTable.getColumnCount());

						XSSFSheet spreadsheet = workbook.createSheet("sheet");
						XSSFRow newrow = spreadsheet.createRow(0);

						int count = 0;
						int col = 0;

						ArrayList<WeightedWord> keyWords = fetchWeighteWords();

						for (DocumentTableCell documentTableCell : documentTable.getCells()) {
							int rowIndex = documentTableCell.getRowIndex();

							if (count < rowIndex) {
								count++;
								System.out.println(count);
								newrow = spreadsheet.createRow(count);
								col = 0;
							}

							XSSFCell newcell1 = newrow.createCell(col++);
							String keywordContain = documentTableCell.getContent().toString();

							if (keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("101"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("102"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("103"))
									|| keywordContain
											.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("104"))) {
								workbook.setSheetName(i, ReadPropertiesFile.readKeywordProperties("100"));

							}

							else if (keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("201"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("202"))
									|| keywordContain
											.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("203"))) {

								workbook.setSheetName(i, ReadPropertiesFile.readKeywordProperties("200"));
							}

							else if (keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("301"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("302"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("303"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("304"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("305"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("306"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("307"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("308"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("309"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("310"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("311"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("312"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("313"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("314"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("315"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("316"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("317"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("318"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("319"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("320"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("321"))
									|| keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("322"))
									|| keywordContain
											.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("323"))) {

								workbook.setSheetName(i, ReadPropertiesFile.readKeywordProperties("300"));

							}

							newcell1.setCellValue(documentTableCell.getContent().toString());

						}

						for (int c = 0; c < sheetcount; c++) {
							if (workbook.getSheetName(i)
									.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("100"))) {

								workbook.setSheetName(i,
										ReadPropertiesFile.readKeywordProperties("100") + " " + incomeSheetCount++);

							}
							if (workbook.getSheetName(i)
									.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("200"))) {

								workbook.setSheetName(i,
										ReadPropertiesFile.readKeywordProperties("200") + " " + balanceSheetCount++);

							}
							if (workbook.getSheetName(i)
									.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("300"))) {

								workbook.setSheetName(i,
										ReadPropertiesFile.readKeywordProperties("300") + " " + cashFlowCount++);

							}
							if (workbook.getSheetName(i).equalsIgnoreCase("sheet")) {

								workbook.setSheetName(i, "sheet " + sheetcount++);

							}
						}
//				System.out.println("=======================Created a new sheet===========================");
					}
//			System.out.printf("Successfully created %d sheets", tables.size());
					TableList tablelist = new TableList();
					statement = con.prepareStatement(DataIngestionQueryConstant.SELECT_MAX_TABLEID
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					rs = statement.executeQuery();

					while (rs.next()) {

						long newtableId = rs.getLong(1);

						for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
							XSSFSheet sheet = workbook.getSheetAt(i);

							int keywordcount = 0;
							long score = 0;
							for (Row row1 : sheet) {
								for (Cell cell : row1) {

									if (cell.getStringCellValue().isEmpty()) {
										cell.setCellValue("NA");
									}

									String keywordContain = cell.getStringCellValue();

									if (keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("101"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("102"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("103"))
											|| keywordContain.equalsIgnoreCase(
													ReadPropertiesFile.readKeywordProperties("104"))) {
										keywordcount++;
										if (keywordcount <= 4) {
											score = (keywordcount * 100 / 4);
										}
										if (keywordcount > 4) {
											score = 100;
										}
									}
									if (keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("201"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("202"))
											|| keywordContain.equalsIgnoreCase(
													ReadPropertiesFile.readKeywordProperties("203"))) {
										keywordcount++;
										if (keywordcount <= 3) {
											score = (keywordcount * 100 / 3);
										}
										if (keywordcount > 3) {
											score = 100;
										}
									}
									if (keywordContain.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("301"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("302"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("303"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("304"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("305"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("306"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("307"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("308"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("309"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("310"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("311"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("312"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("313"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("314"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("315"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("316"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("317"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("318"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("319"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("320"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("321"))
											|| keywordContain
													.equalsIgnoreCase(ReadPropertiesFile.readKeywordProperties("322"))
											|| keywordContain.equalsIgnoreCase(
													ReadPropertiesFile.readKeywordProperties("323"))) {
										keywordcount++;
										if (keywordcount <= 23) {
											score = (keywordcount * 100 / 23);
										}
										if (keywordcount < 23) {
											score = 100;
										}

									}

								}
							}

							tablelist.setTableId(newtableId + 1);
//				long fileId=dataIngestion.getFileId();
							String Table_name = workbook.getSheetName(i);
							String tablemapName = null;

							statement = con.prepareStatement(DataIngestionQueryConstant.INSERT_TABLELIST
									.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
							statement.setLong(1, fileId);
							statement.setLong(2, tablelist.getTableId());
							statement.setString(3, Table_name);
							statement.setLong(4, score);
							statement.setString(5, tablemapName);
							statement.executeUpdate();

							DataFormatter formatter = new DataFormatter();

							for (Row row1 : sheet) {
								for (Cell cell : row1) {

									if (cell.getStringCellValue().isEmpty()) {
										cell.setCellValue("NA");
									}

									boolean removeComma = context.get("my.config.removeComma", Boolean.class)
											.orElse(false);
									boolean removeBracket = context.get("my.config.removeBracket", Boolean.class)
											.orElse(false);
									boolean removeSpace = context.get("my.config.removeSpace", Boolean.class)
											.orElse(false);

									String cellValue = formatter.formatCellValue(cell);
									Pattern regex = Pattern.compile("\\d+(\\.\\d+)?,\\s\\d+(\\.\\d+)");

									Pattern regex1 = Pattern.compile("\\(\\d+\\s+\\d+\\.\\d+\\)");

									Pattern regex2 = Pattern.compile("-?\\d+(,\\d+)*([,]\\d+)?([,]\\d+)*");

									Pattern regex3 = Pattern.compile("^\\([\\d,.\\s]+\\)$");

									Pattern regex4 = Pattern.compile("\\(\\d{1,4}\\.\\d{1,2}%\\)");

									Pattern regex5 = Pattern.compile("\\s\\(d\\)\\s");

									Pattern regex6 = Pattern.compile("\\(?[\\d,]+(\\.\\d+)?\\)?");

									Matcher match = regex.matcher(cellValue);

									Matcher match1 = regex1.matcher(cellValue);

									Matcher match2 = regex2.matcher(cellValue);

									Matcher match3 = regex3.matcher(cellValue);

									Matcher match4 = regex4.matcher(cellValue);

									Matcher match5 = regex5.matcher(cellValue);

									Matcher match6 = regex6.matcher(cellValue);

									if (match.matches() || match1.matches() || match2.matches() || match3.matches()
											|| match4.matches() || match5.matches() || match6.matches()) {

										if (removeComma) {

											cellValue = cellValue.replace(",", "");
										}

										cell.setCellValue(cellValue);

										if (removeBracket) {
											cellValue = cellValue.replace("(", "-").replace(")", "");
										}

										cell.setCellValue(cellValue);

									}
									String pattern = "\\d+(\\.\\d+)?\\s+\\d+(\\.\\d+)?"; // regular expression for digit

									regex = Pattern.compile(pattern);

									match = regex.matcher(cellValue);

									if (match.find()) {
										if (removeSpace) {
											cellValue = cellValue.replace(" ", "");
										}
										cell.setCellValue(cellValue);
									}

									for (int c = 0; c < 2; c++)

									{

										cell = row1.getCell(c);

										if (cell != null) {

											cellValue = formatter.formatCellValue(cell);

											String removebullet1 = cellValue.replaceAll("^\\([A-Za-z]{1}\\)\\s*", "");

											String removebullet2 = removebullet1.replaceAll("^\\[A-Za-z]{1}\\)\\s*",
													"");

											String removebullet3 = removebullet2
													.replaceAll("^\\([ivxlcdmIVXLCDM]+\\)\\s*", ""); // for (i) (I) like
																										// this

											String removebullet4 = removebullet3.replaceAll("^[ivxlcdmIVXLCDM]+\\)\\s*",
													""); // for i) I) like this

											String removebullet5 = removebullet4.replaceAll("^\\d+\\)\\s*", "");

											String removebullet6 = removebullet5.replaceAll("^\\(d+\\)\\s*", "");

											String removebullet7 = removebullet6.replaceAll("^[A-Za-z]{1}\\.\\s*", "");

											String removebullet8 = removebullet7.replaceAll("^[A-Za-z]\\s+", "");

											String removebullet9 = removebullet8.replaceAll("^\\s*[A-Za-z]{1}\\)\\s*",
													"");

											String removebullet10 = removebullet9.replaceAll("\\$", "");

											String removebullet11 = removebullet10.replaceAll("\\?", "");

											String removebullet12 = removebullet11.replaceAll("\\#", "");

											String removebullet13 = removebullet12.replaceAll("", "");

											String removebullet14 = removebullet13.replaceAll("^\\d+\\.\\s*[A-Za-z]+$",
													""); // for
															// 1.

											String removebullet15 = removebullet14.replaceAll("^\\d+\\)\\s*[A-Za-z]",
													""); // for
															// 1)

											String removebullet16 = removebullet15.replaceAll("^\\(\\d+\\)\\s*[A-Za-z]",
													""); // for (1)

											String removebullet17 = removebullet16.replaceAll("^\\d+\\s*[A-Za-z]", ""); // for
																														// 1

											String removebullet18 = removebullet17.replaceAll("\\•\\s*[A-Za-z]", "");

											String removebullet19 = removebullet18.replaceAll("\\-", "");

											cell.setCellValue(removebullet19);

										}

									}

								}
								String value1 = formatter.formatCellValue(row1.getCell(0));
								String value2 = formatter.formatCellValue(row1.getCell(1));
								String value3 = formatter.formatCellValue(row1.getCell(2));
								String value4 = formatter.formatCellValue(row1.getCell(3));
								String value5 = formatter.formatCellValue(row1.getCell(4));
								String value6 = formatter.formatCellValue(row1.getCell(5));
								String value7 = formatter.formatCellValue(row1.getCell(6));
								String value8 = formatter.formatCellValue(row1.getCell(7));
								String value9 = formatter.formatCellValue(row1.getCell(8));
								String value10 = formatter.formatCellValue(row1.getCell(9));
								String value11 = formatter.formatCellValue(row1.getCell(10));
								String value12 = formatter.formatCellValue(row1.getCell(11));
								String value13 = formatter.formatCellValue(row1.getCell(12));
								String value14 = formatter.formatCellValue(row1.getCell(13));
								String value15 = formatter.formatCellValue(row1.getCell(14));
								String value16 = formatter.formatCellValue(row1.getCell(15));
								String value17 = formatter.formatCellValue(row1.getCell(16));
								String value18 = formatter.formatCellValue(row1.getCell(17));
								String value19 = formatter.formatCellValue(row1.getCell(18));
								String value20 = formatter.formatCellValue(row1.getCell(19));
								String value21 = formatter.formatCellValue(row1.getCell(20));
								String value22 = formatter.formatCellValue(row1.getCell(21));
								String value23 = formatter.formatCellValue(row1.getCell(22));
								String value24 = formatter.formatCellValue(row1.getCell(23));
								String value25 = formatter.formatCellValue(row1.getCell(24));

								statement = con.prepareStatement(
										DataIngestionQueryConstant.INSERT_INTO_DATAINGESTION_TABLE_DATA.replace(
												DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
								statement.setLong(1, tablelist.getTableId());
								statement.setString(2, value1);
								statement.setString(3, value2);
								statement.setString(4, value3);
								statement.setString(5, value4);
								statement.setString(6, value5);
								statement.setString(7, value6);
								statement.setString(8, value7);
								statement.setString(9, value8);
								statement.setString(10, value9);
								statement.setString(11, value10);
								statement.setString(12, value11);
								statement.setString(13, value12);
								statement.setString(14, value13);
								statement.setString(15, value14);
								statement.setString(16, value15);
								statement.setString(17, value16);
								statement.setString(18, value17);
								statement.setString(19, value18);
								statement.setString(20, value19);
								statement.setString(21, value20);
								statement.setString(22, value21);
								statement.setString(23, value22);
								statement.setString(24, value23);
								statement.setString(25, value24);
								statement.setString(26, value25);
								statement.executeUpdate();

							}
							newtableId++;
						}
					}
					LOGGER.info("data extract and write in table");
					statement = con.prepareStatement(DataIngestionQueryConstant.UPDATE_STATUSFILEEXTRACT
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					statement.setString(1, "extract");
					statement.setLong(2, fileId);
					statement.executeUpdate();
					con.close();

					} else {
					System.out.println("date mismatch");
					statement = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_FILEDETAILS
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					statement.setLong(1, fileId);
					statement.executeUpdate();
					System.out.println("delete dataingestion file details");
					throw new DataIngestionDaoException("Unable to extract file due to file's date not being matched");

				}
			}
			model.setStatus("extract");
			return model;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException(e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(statement, con);
		}

	}

	private String sanitizeFileName(String fileName) {
		// Implement proper filename validation and sanitization logic here
		// For example, you can remove characters that may cause issues in a file name
		return fileName.replaceAll("[^a-zA-Z0-9_.-]", "_");
	}

	private int getMonthNumber(String month) {
		String[] months = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
				"OCTOBER", "NOVEMBER", "DECEMBER" };
		for (int i = 0; i < months.length; i++) {
			if (months[i].equalsIgnoreCase(month)) {
				return i + 1; // Months are 1-based in LocalDate
			}
		}
		return -1; // Invalid month name
	}

	private String capitalizeFirstLetter(String input) {

		String[] words = input.split(" ");
		StringBuilder result = new StringBuilder();

		for (String word : words) {
			String lowercase = word.toLowerCase();
			String capitalized = lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
			result.append(capitalized).append(" ");
		}

		return result.toString().trim();
	}

	private ArrayList<WeightedWord> fetchWeighteWords() {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		String dataBaseName = "irdb";
		ResultSet rs = null;
		ArrayList<WeightedWord> list = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_KEYWORD
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {
				WeightedWord word = new WeightedWord();
				word.setKey_id(rs.getInt("key_id"));
				word.setDocument_Type(rs.getString("document_type"));
				word.setKeyword(rs.getString("keyword"));
				word.setWeight(rs.getInt("weight"));
				list.add(word);
			}
			return list;

		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			InvestorDatabaseUtill.close(rs, psta, con);
		}
		return null;
	}

	@Override
	public ArrayList<TableList> getTableIdByFileId(long fileId, String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		ArrayList<TableList> list = new ArrayList<>();
		try {

			connection = InvestorDatabaseUtill.getConnection();

			pstmt = connection.prepareStatement(DataIngestionQueryConstant.SELECT_TABLEID_BYFILEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, fileId);

			result = pstmt.executeQuery();

			while (result.next()) {
				TableList model = new TableList();
				model.setTableId(result.getLong("tableId"));
				model.setTableName(result.getString("tableName"));
				model.setScore(result.getLong("score"));

				list.add(model);
			}

			return list;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionDaoException("unable to get" + e.getMessage());

		} finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ArrayList<DataIngestionTableModel> getTableIngestionTableData(String analystName, long tableId,
			String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		ArrayList<DataIngestionTableModel> list = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.SELECT_DATAINGESTION_TABLEDETAILS
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, analystName);
			result = pstmt.executeQuery();
//			System.out.println("resultset" + result);
			while (result.next()) {

				if (result.getLong("tableId") == tableId) {
					DataIngestionTableModel modeldata = buildData(result);

					list.add(modeldata);
				}
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	private DataIngestionTableModel buildData(ResultSet result) throws SQLException {
		// TODO Auto-generated method stub
		DataIngestionTableModel modeldata = new DataIngestionTableModel();

		modeldata.setField_Id(result.getLong("field_Id"));
		modeldata.setC1(result.getString("C1"));
		modeldata.setC2(result.getString("C2"));
		modeldata.setC3(result.getString("C3"));
		modeldata.setC4(result.getString("C4"));
		modeldata.setC5(result.getString("C5"));
		modeldata.setC6(result.getString("C6"));
		modeldata.setC7(result.getString("C7"));
		modeldata.setC8(result.getString("C8"));
		modeldata.setC9(result.getString("C9"));
		modeldata.setC10(result.getString("C10"));
		modeldata.setC11(result.getString("C11"));
		modeldata.setC12(result.getString("C12"));
		modeldata.setC13(result.getString("C13"));
		modeldata.setC14(result.getString("C14"));
		modeldata.setC15(result.getString("C15"));
		modeldata.setC16(result.getString("C16"));
		modeldata.setC17(result.getString("C17"));
		modeldata.setC18(result.getString("C18"));
		modeldata.setC19(result.getString("C19"));
		modeldata.setC20(result.getString("C21"));
		modeldata.setC21(result.getString("C21"));
		modeldata.setC22(result.getString("C22"));
		modeldata.setC23(result.getString("C23"));
		modeldata.setC24(result.getString("C24"));
		modeldata.setC25(result.getString("C25"));

		if (result.getString("masterLineItem") == null) {
			LOGGER.info("masterLineItem not match");
			modeldata.setMasterLineItem("NA");

		} else {
			LOGGER.info("master Line Item Are match");
			modeldata.setMasterLineItem(result.getString("masterLineItem"));

		}

		return modeldata;

	}

	@Override
	public void deleteTableDataByTableId(long tableId, String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.DELETE_TABLEDETAILS_BYTABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, tableId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to delete table data");
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ArrayList<DataIngestionTableModel> updatedataIngestionTableData(
			ArrayList<DataIngestionTableModel> dataIngestionTableData, long tableId, String dataBaseName)
			throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			System.out.println("check2");
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.UPDATE_DATAINGESTION_TABLEDETAILS
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Iterator it = dataIngestionTableData.iterator();
			while (it.hasNext())

			{
				DataIngestionTableModel model = (DataIngestionTableModel) it.next();

				pstmt.setString(1, model.getC1());
				pstmt.setString(2, model.getC2());
				pstmt.setString(3, model.getC3());
				pstmt.setString(4, model.getC4());
				pstmt.setString(5, model.getC5());
				pstmt.setString(6, model.getC6());
				pstmt.setString(7, model.getC7());
				pstmt.setString(8, model.getC8());
				pstmt.setString(9, model.getC9());
				pstmt.setString(10, model.getC10());
				pstmt.setString(11, model.getC11());
				pstmt.setString(12, model.getC12());
				pstmt.setString(13, model.getC13());
				pstmt.setString(14, model.getC14());
				pstmt.setString(15, model.getC15());
				pstmt.setString(16, model.getC16());
				pstmt.setString(17, model.getC17());
				pstmt.setString(18, model.getC18());
				pstmt.setString(19, model.getC19());
				pstmt.setString(20, model.getC20());
				pstmt.setString(21, model.getC21());
				pstmt.setString(22, model.getC22());
				pstmt.setString(23, model.getC23());
				pstmt.setString(24, model.getC24());
				pstmt.setString(25, model.getC25());

				pstmt.setLong(26, model.getField_Id());

				pstmt.executeUpdate();
			}
			return dataIngestionTableData;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to update" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@Override
	public ArrayList<DataIngestionMappingModel> addDataIngestionMappingTableData(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable, String dataBaseName)
			throws DataIngestionDaoException {
		Connection connection = null;
		PreparedStatement psta = null;
		Date date = new Date();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			psta = connection.prepareStatement(DataIngestionQueryConstant.INSERT_INTO_DATAINGESTION_MAPPINGTABLE
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Iterator ir = dataIngestionMappingTable.iterator();

			while (ir.hasNext()) {

				DataIngestionMappingModel dataIngestionMapping = (DataIngestionMappingModel) ir.next();
				String mapId = UUID.randomUUID().toString();
				dataIngestionMapping.setMapId(mapId);
				psta.setString(1, dataIngestionMapping.getMapId());
				psta.setLong(2, dataIngestionMapping.getFieldId());
				psta.setString(3, dataIngestionMapping.getAnalyst());
				psta.setString(4, dataIngestionMapping.getMasterLineItem());
				psta.setString(5, dataIngestionMapping.getCompanyName());
				psta.setString(6, dataIngestionMapping.getTableName());
				psta.setString(7, dataIngestionMapping.getDocumentType());
				psta.setString(8, dataIngestionMapping.getPeerName());
				psta.setString(9, dataIngestionMapping.getCurrency());
				psta.setString(10, dataIngestionMapping.getUnits());
				psta.setString(11, dataIngestionMapping.getYear());
				psta.setString(12, dataIngestionMapping.getLineItemName());
				psta.setString(13, dataIngestionMapping.getQuarter());
				psta.setString(14, dataIngestionMapping.getType());
				psta.setString(15, dataIngestionMapping.getValue());
				psta.setString(16, dataIngestionMapping.getReportType());
				psta.setDate(17, dataIngestionMapping.getReportDate());
				psta.setLong(18, date.getTime());
				System.out.println("denomination" + dataIngestionMapping.getDenomination());
				psta.setString(19, dataIngestionMapping.getDenomination());
				psta.setString(20, dataIngestionMapping.getConsolidated_standalone());
				psta.setString(21, dataIngestionMapping.getCreatedBy());
				psta.setString(22, dataIngestionMapping.getReportYear());
				if(dataIngestionMapping.getReportType().trim().equals("Annual"))
				{
					psta.setString(23,"FY"+dataIngestionMapping.getReportYear().substring(2, 4));
				}else
				{
					psta.setString(23,dataIngestionMapping.getReportType().trim()+"FY"+dataIngestionMapping.getReportYear().substring(2, 4));
				}
				
				psta.executeUpdate();

			}
			return dataIngestionMappingTable;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to create data Ingestion mapping table" + e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, connection);

		}
	}

	public DataIngestionTableModel getTableDataByFeldId(long field_Id, String dataBaseName)
			throws DataIngestionDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.SELECT_TABLEDATA_BYFIELDID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, field_Id);
			result = pstmt.executeQuery();
			while (result.next()) {
				DataIngestionTableModel model = new DataIngestionTableModel();
				model.setField_Id(result.getLong("field_Id"));
				model.setC1(result.getString("C1"));
				model.setC2(result.getString("C2"));
				model.setC3(result.getString("C3"));
				model.setC4(result.getString("C4"));
				model.setC5(result.getString("C5"));
				model.setC6(result.getString("C6"));
				model.setC7(result.getString("C7"));
				model.setC8(result.getString("C8"));
				model.setC9(result.getString("C9"));
				model.setC10(result.getString("C10"));
				model.setC11(result.getString("C11"));
				model.setC12(result.getString("C12"));
				model.setC13(result.getString("C13"));
				model.setC14(result.getString("C14"));
				model.setC15(result.getString("C15"));
				model.setC16(result.getString("C16"));
				model.setC17(result.getString("C17"));
				model.setC18(result.getString("C18"));
				model.setC19(result.getString("C19"));
				model.setC20(result.getString("C21"));
				model.setC21(result.getString("C21"));
				model.setC22(result.getString("C22"));
				model.setC23(result.getString("C23"));
				model.setC24(result.getString("C24"));
				model.setC25(result.getString("C25"));
				return model;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to get table data bye fieldId" + e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(pstmt, connection);

		}
		return null;
	}

	@Override
	public void deleteTableDataByFieldId(long field_Id, String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.DELETE_TABLEDATA_BY_FIELDID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setLong(1, field_Id);
			pstmt.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to delete table data bye fieldId" + e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(pstmt, connection);

		}

	}

	@Override
	public TableList updateTableNameByTableId(TableList tabledata, long tableId, String dataBaseName)
			throws DataIngestionDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.UPDATE_TABLENAME_BYTABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, tabledata.getTableName());
			pstmt.setLong(2, tableId);
			pstmt.executeUpdate();
			return tabledata;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to update table name" + e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(pstmt, connection);

		}

	}

	@Override
	public DataIngestionModel downloadTableDataBytableId(long tableId, String dataBaseName)
			throws DataIngestionDaoException, IOException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		String tableName = null;
		DataIngestionModel model = new DataIngestionModel();

		ArrayList<DataIngestionTableModel> list = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_TABLENAME
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableId);
			rs = psta.executeQuery();
			while (rs.next()) {

				tableName = rs.getString("tableName");

			}

		} catch (Exception e) {

			throw new DataIngestionDaoException(e.getMessage());
		}

		String sheetname = tableName;
		model.setFileName(tableName);

		XSSFSheet sheet = workbook.createSheet(sheetname);
		String wbname = tableName;

		String home = System.getProperty("user.home");

		final String CSV_LOCATION = (home + "/Downloads/" + wbname + ".xlsx");
		try {
			// PrintWriter pw=new PrintWriter(new FileWriter(CSV_LOCATION));
			connection = InvestorDatabaseUtill.getConnection();
			psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_TABLEDATA_BYTABLEID_DOWNLOAD
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableId);
			rs = psta.executeQuery();
			int rownum = 1;

			XSSFRow row1 = sheet.createRow(0);
			row1.createCell(0).setCellValue("field_Id");
			row1.createCell(1).setCellValue("tableId");
			row1.createCell(2).setCellValue("C1");
			row1.createCell(3).setCellValue("C2");
			row1.createCell(4).setCellValue("C3");
			row1.createCell(5).setCellValue("C4");
			row1.createCell(6).setCellValue("C5");
			row1.createCell(7).setCellValue("C6");
			row1.createCell(8).setCellValue("C7");
			row1.createCell(9).setCellValue("C8");
			row1.createCell(10).setCellValue("C9");
			row1.createCell(11).setCellValue("C10");
			row1.createCell(12).setCellValue("C11");
			row1.createCell(13).setCellValue("C12");
			row1.createCell(14).setCellValue("C13");
			row1.createCell(15).setCellValue("C14");
			row1.createCell(16).setCellValue("C15");
			row1.createCell(17).setCellValue("C16");
			row1.createCell(18).setCellValue("C17");
			row1.createCell(19).setCellValue("C18");
			row1.createCell(20).setCellValue("C19");
			row1.createCell(21).setCellValue("C20");
			row1.createCell(22).setCellValue("C21");
			row1.createCell(23).setCellValue("C22");
			row1.createCell(24).setCellValue("C23");
			row1.createCell(25).setCellValue("C24");
			row1.createCell(26).setCellValue("C25");

			while (rs.next()) {
				int colnum = 0;
				XSSFRow row = sheet.createRow(rownum);
				row = sheet.createRow(rownum++);
				row.createCell(colnum++).setCellValue(rs.getLong("field_Id"));
				row.createCell(colnum++).setCellValue(rs.getLong("tableId"));
				row.createCell(colnum++).setCellValue(rs.getString("C1"));
				row.createCell(colnum++).setCellValue(rs.getString("C2"));
				row.createCell(colnum++).setCellValue(rs.getString("C3"));
				row.createCell(colnum++).setCellValue(rs.getString("C4"));
				row.createCell(colnum++).setCellValue(rs.getString("C5"));
				row.createCell(colnum++).setCellValue(rs.getString("C6"));
				row.createCell(colnum++).setCellValue(rs.getString("C7"));
				row.createCell(colnum++).setCellValue(rs.getString("C8"));
				row.createCell(colnum++).setCellValue(rs.getString("C9"));
				row.createCell(colnum++).setCellValue(rs.getString("C10"));
				row.createCell(colnum++).setCellValue(rs.getString("C11"));
				row.createCell(colnum++).setCellValue(rs.getString("C12"));
				row.createCell(colnum++).setCellValue(rs.getString("C13"));
				row.createCell(colnum++).setCellValue(rs.getString("C14"));
				row.createCell(colnum++).setCellValue(rs.getString("C15"));
				row.createCell(colnum++).setCellValue(rs.getString("C16"));
				row.createCell(colnum++).setCellValue(rs.getString("C17"));
				row.createCell(colnum++).setCellValue(rs.getString("C18"));
				row.createCell(colnum++).setCellValue(rs.getString("C19"));
				row.createCell(colnum++).setCellValue(rs.getString("C20"));
				row.createCell(colnum++).setCellValue(rs.getString("C21"));
				row.createCell(colnum++).setCellValue(rs.getString("C22"));
				row.createCell(colnum++).setCellValue(rs.getString("C23"));
				row.createCell(colnum++).setCellValue(rs.getString("C24"));
				row.createCell(colnum++).setCellValue(rs.getString("C25"));

			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byte[] data = outputStream.toByteArray();
			model.setFileData(data);
			LOGGER.info("download the excel sheet completed");
			return model;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException(e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, connection);
		}

	}

	@Override
	public ArrayList<DataIngestionModel> getfileDetails(String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<DataIngestionModel> list = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_DATAINGESTION_FILEDETAILS
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
//			System.out.println("resultset"+rs);
			while (rs.next()) {
				DataIngestionModel model = builddata(rs);
				list.add(model);
			}
			return list;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to update table name" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, connection);
		}
	}

	@Override
	public ArrayList<KeywordList> getKeyword(String dataBaseName) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<KeywordList> list = new ArrayList<>();
		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection.prepareStatement(DataIngestionQueryConstant.SELECT_KEYWORD
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				KeywordList keyowrd = new KeywordList();
				keyowrd.setKey_id(rs.getLong("key_id"));
				keyowrd.setDocument_type(rs.getString("document_type"));
				keyowrd.setKeyword(rs.getString("keyword"));
				keyowrd.setWeight(rs.getInt("weight"));
				list.add(keyowrd);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}
		return null;

	}

	@Override
	public void uploadExcelSheet(CompletedFileUpload file, long tableId, String dataBaseName)
			throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement statement = null;

		try

		{

			byte[] fileContent = file.getBytes();

			InputStream targetStream = new ByteArrayInputStream(fileContent);

			XSSFWorkbook workbook = new XSSFWorkbook(targetStream);

			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			int rownum = 0;
			for (Row row1 : sheet) {

				if (rownum == 0) {

					rownum++;
					continue;
				}

				String id = formatter.formatCellValue(row1.getCell(0));

				long field_id = Long.parseLong(id);

				String value2 = formatter.formatCellValue(row1.getCell(2));
				String value3 = formatter.formatCellValue(row1.getCell(3));
				String value4 = formatter.formatCellValue(row1.getCell(4));
				String value5 = formatter.formatCellValue(row1.getCell(5));
				String value6 = formatter.formatCellValue(row1.getCell(6));
				String value7 = formatter.formatCellValue(row1.getCell(7));
				String value8 = formatter.formatCellValue(row1.getCell(8));
				String value9 = formatter.formatCellValue(row1.getCell(9));
				String value10 = formatter.formatCellValue(row1.getCell(10));
				String value11 = formatter.formatCellValue(row1.getCell(11));
				String value12 = formatter.formatCellValue(row1.getCell(12));
				String value13 = formatter.formatCellValue(row1.getCell(13));
				String value14 = formatter.formatCellValue(row1.getCell(14));
				String value15 = formatter.formatCellValue(row1.getCell(15));
				String value16 = formatter.formatCellValue(row1.getCell(16));
				String value17 = formatter.formatCellValue(row1.getCell(17));
				String value18 = formatter.formatCellValue(row1.getCell(18));
				String value19 = formatter.formatCellValue(row1.getCell(19));
				String value20 = formatter.formatCellValue(row1.getCell(20));
				String value21 = formatter.formatCellValue(row1.getCell(21));
				String value22 = formatter.formatCellValue(row1.getCell(22));
				String value23 = formatter.formatCellValue(row1.getCell(23));
				String value24 = formatter.formatCellValue(row1.getCell(24));
				String value25 = formatter.formatCellValue(row1.getCell(25));
				String value26 = formatter.formatCellValue(row1.getCell(26));

				connection = InvestorDatabaseUtill.getConnection();
				statement = connection.prepareStatement(DataIngestionQueryConstant.UPDATE_DATAINGESTION_TABLEDETAILS
						.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				statement.setString(1, value2);
				statement.setString(2, value3);
				statement.setString(3, value4);
				statement.setString(4, value5);
				statement.setString(5, value6);
				statement.setString(6, value7);
				statement.setString(7, value8);
				statement.setString(8, value9);
				statement.setString(9, value10);
				statement.setString(10, value11);
				statement.setString(11, value12);
				statement.setString(12, value13);
				statement.setString(13, value14);
				statement.setString(14, value15);
				statement.setString(15, value16);
				statement.setString(16, value17);
				statement.setString(17, value18);
				statement.setString(18, value19);
				statement.setString(19, value20);
				statement.setString(20, value21);
				statement.setString(21, value22);
				statement.setString(22, value23);
				statement.setString(23, value24);
				statement.setString(24, value25);
				statement.setString(25, value26);
				statement.setLong(26, field_id);
				statement.executeUpdate();

				rownum++;
//				System.out.println("rownumber" + rownum);

			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException(e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(statement, connection);
		}

	}

	@Override
	public void deleteMultipleField(FiledData filedData, String dataBaseName) throws DataIngestionDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		try {

			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_TABLEDATA_BY_FIELDID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

			List<Long> paramList = filedData.getFiled_idlist();

			for (long id : paramList) {

				psta.setLong(1, id);
				psta.executeUpdate();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionDaoException("unable to delete " + e.getMessage());
		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public TableList mergeTableDataByTableId(TableList tableList, long tableId, String dataBaseName)
			throws DataIngestionDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.MERGE_TABLE_BYTABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableList.getTableId());
			psta.setLong(2, tableId);
			psta.executeUpdate();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_TABLEID_BYTABLELIST
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableId);
			psta.executeUpdate();
			return tableList;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to merge table" + e.getMessage());
		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public ArrayList<DataIngestionTableModel> leftShiftTableDataByFieldId(ArrayList<DataIngestionTableModel> tableData,
			String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.LEFTSHIFT_TABLEDATA
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Iterator ir = tableData.iterator();
			while (ir.hasNext()) {
				DataIngestionTableModel model = (DataIngestionTableModel) ir.next();

				psta.setString(1, model.getC2());
				psta.setString(2, model.getC3());
				psta.setString(3, model.getC4());
				psta.setString(4, model.getC5());
				psta.setString(5, model.getC6());
				psta.setString(6, model.getC7());
				psta.setString(7, model.getC8());
				psta.setString(8, model.getC9());
				psta.setString(9, model.getC10());
				psta.setString(10, model.getC11());
				psta.setString(11, model.getC12());
				psta.setString(12, model.getC13());
				psta.setString(13, model.getC14());
				psta.setString(14, model.getC15());
				psta.setString(15, model.getC16());
				psta.setString(16, model.getC17());
				psta.setString(17, model.getC18());
				psta.setString(18, model.getC19());
				psta.setString(19, model.getC20());
				psta.setString(20, model.getC21());
				psta.setString(21, model.getC22());
				psta.setString(22, model.getC23());
				psta.setString(23, model.getC24());
				psta.setString(24, model.getC25());
				psta.setLong(25, model.getField_Id());
				psta.executeUpdate();

			}
			return tableData;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to left shift data" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}
	}

	@Override
	public ArrayList<DataIngestionTableModel> rightShiftTableDataByfieldId(ArrayList<DataIngestionTableModel> tableData,
			String dataBaseName) throws DataIngestionDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		try {

			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.RIGHTSHIFT_TABLEDATA
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Iterator ir = tableData.iterator();
			while (ir.hasNext()) {

				DataIngestionTableModel model = (DataIngestionTableModel) ir.next();
				psta.setString(1, "NA");
				psta.setString(2, model.getC1());
				psta.setString(3, model.getC2());
				psta.setString(4, model.getC3());
				psta.setString(5, model.getC4());
				psta.setString(6, model.getC5());
				psta.setString(7, model.getC6());
				psta.setString(8, model.getC7());
				psta.setString(9, model.getC8());
				psta.setString(10, model.getC9());
				psta.setString(11, model.getC10());
				psta.setString(12, model.getC11());
				psta.setString(13, model.getC12());
				psta.setString(14, model.getC13());
				psta.setString(15, model.getC14());
				psta.setString(16, model.getC15());
				psta.setString(17, model.getC16());
				psta.setString(18, model.getC17());
				psta.setString(19, model.getC18());
				psta.setString(20, model.getC19());
				psta.setString(21, model.getC20());
				psta.setString(22, model.getC21());
				psta.setString(23, model.getC22());
				psta.setString(24, model.getC23());
				psta.setString(25, model.getC24());
				psta.setLong(26, model.getField_Id());
				psta.executeUpdate();
			}
			return tableData;
		}

		catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to right shift data" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public void splitTableDetails(long fileId, ArrayList<DataIngestionTableModel> tableDetails, String dataBaseName)
			throws DataIngestionDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			TableList tablelist = new TableList();
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_MAX_TABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			rs = psta.executeQuery();
			while (rs.next()) {

				long newtableId = rs.getLong(1);
				tablelist.setTableId(newtableId + 1);
				String Table_name = "Splited_sheet";
				String tablemapName = null;
				long score = 0;

				psta = con.prepareStatement(DataIngestionQueryConstant.INSERT_TABLELIST
						.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				psta.setLong(1, fileId);
				psta.setLong(2, tablelist.getTableId());
				psta.setString(3, Table_name);
				psta.setLong(4, score);
				psta.setString(5, tablemapName);
				psta.executeUpdate();
			}
//			System.out.println("tableid" + tablelist.getTableId());
			psta = con.prepareStatement(DataIngestionQueryConstant.INSERT_INTO_DATAINGESTION_TABLE_DATA
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Iterator ir = tableDetails.iterator();
			while (ir.hasNext()) {
				DataIngestionTableModel model = (DataIngestionTableModel) ir.next();
				psta.setLong(1, tablelist.getTableId());
				psta.setString(2, model.getC1());
				psta.setString(3, model.getC2());
				psta.setString(4, model.getC3());
				psta.setString(5, model.getC4());
				psta.setString(6, model.getC5());
				psta.setString(7, model.getC6());
				psta.setString(8, model.getC7());
				psta.setString(9, model.getC8());
				psta.setString(10, model.getC9());
				psta.setString(11, model.getC10());
				psta.setString(12, model.getC11());
				psta.setString(13, model.getC12());
				psta.setString(14, model.getC13());
				psta.setString(15, model.getC14());
				psta.setString(16, model.getC15());
				psta.setString(17, model.getC16());
				psta.setString(18, model.getC17());
				psta.setString(19, model.getC18());
				psta.setString(20, model.getC19());
				psta.setString(21, model.getC20());
				psta.setString(22, model.getC21());
				psta.setString(23, model.getC22());
				psta.setString(24, model.getC23());
				psta.setString(25, model.getC24());
				psta.setString(26, model.getC25());
				psta.executeUpdate();
			}

			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_TABLEDATA_BY_FIELDID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			Iterator iterator = tableDetails.iterator();

			while (iterator.hasNext()) {
				DataIngestionTableModel model = (DataIngestionTableModel) iterator.next();
				psta.setLong(1, model.getField_Id());
				psta.executeUpdate();
			}

		} catch (Exception e) {
			throw new DataIngestionDaoException("unable to split table details" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public DataIngestionModel getfileIdByTableId(long tableId, String dataBaseName) throws DataIngestionDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<DataIngestionModel> list = new ArrayList<>();
		DataIngestionModel model = new DataIngestionModel();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_FILEID_BYTABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableId);
			rs = psta.executeQuery();
			while (rs.next()) {
				model.setFileId(rs.getLong("fileId"));

			}
			return model;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public DataIngestionModel getfileDataByFileId(long fileId, String dataBaseName) throws DataIngestionDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_DATAINGESTION_BYFILEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			rs = psta.executeQuery();
			while (rs.next()) {
				DataIngestionModel model = builddata(rs);
				return model;

			}

		} catch (Exception e) {
			throw new DataIngestionDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}
		return null;
	}

	@Override
	public TableList getTablelistByTableId(long tableId, String dataBaseName) throws DataIngestionDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_TABLEDATA_BYTABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableId);
			rs = psta.executeQuery();
			while (rs.next()) {
				TableList tableData = new TableList();
				tableData.setTableId(rs.getLong("tableId"));
				tableData.setTableName(rs.getString("tableName"));
				tableData.setScore(rs.getLong("score"));
				return tableData;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, con);

		}

		return null;

	}

	@SuppressWarnings("resource")
	@Override
	public List<JSONObject> discoversDates(long tableId, String dataBaseName) throws DataIngestionDaoException {
		Connection connection = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		String c2Value = null;
		List<JSONObject> object = new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();
//		DataIngestionTableModel model=null;
		String properValue = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_DISCOVERS_DATES_BYTABLEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, tableId);
			rs = psta.executeQuery();
			while (rs.next()) {
				c2Value = checkRegularExpression(rs.getString("C2"), rs.getString("C3"));
				list.add(c2Value);
			}
			Optional<String> result = list.stream().filter(s -> s != null).findFirst();

			if (result.isPresent()) {
				properValue = result.get();
//		            System.out.println("Found proper value: " + properValue);

				connection = InvestorDatabaseUtill.getConnection();

				psta = connection.prepareStatement(DataIngestionQueryConstant.SELECT_DISCOVERS_DATES_DETAILS
						.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				psta.setString(1, properValue);
				psta.setLong(2, tableId);
				rs = psta.executeQuery();
				while (rs.next()) {

					object = dropDownData(rs);
				}
				return object;
			} else {
				return object;
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to get" + e.getMessage());
		} finally {
			LOGGER.info("closing the connection");
			InvestorDatabaseUtill.close(psta, connection);

		}

	}

	@SuppressWarnings("unchecked")
	private List<JSONObject> dropDownData(ResultSet rs) throws SQLException {
		ArrayList<String> list = new ArrayList<>();
		list.add(rs.getString("C1"));
		list.add(rs.getString("C2"));
		list.add(rs.getString("C3"));
		list.add(rs.getString("C4"));
		list.add(rs.getString("C5"));
		list.add(rs.getString("C6"));
		list.add(rs.getString("C7"));
		list.add(rs.getString("C8"));
		list.add(rs.getString("C9"));
		list.add(rs.getString("C10"));
		list.add(rs.getString("C11"));
		list.add(rs.getString("C12"));
		list.add(rs.getString("C13"));
		list.add(rs.getString("C14"));
		list.add(rs.getString("C15"));
		list.add(rs.getString("C16"));
		list.add(rs.getString("C17"));
		list.add(rs.getString("C18"));
		list.add(rs.getString("C19"));
		list.add(rs.getString("C20"));
		list.add(rs.getString("C21"));
		list.add(rs.getString("C22"));
		list.add(rs.getString("C23"));
		list.add(rs.getString("C24"));
		list.add(rs.getString("C25"));
		List<JSONObject> jsonObjectsList = new ArrayList<>();

		String jsonString = null;
		for (String input : list) {

			JSONObject jsonObject = new JSONObject();
			// pattern for 2QFY22E
			Pattern datePattern = Pattern.compile("\\d{1,2}Q[Ff][Yy]\\d{2}[A-Za-z]{1}$");
			Matcher matcher = datePattern.matcher(input);

			if (matcher.find()) {

				String quarter = "";
				String year = "";
				String type = "";

				input = input.trim();

				if (input.length() == 7) {

					quarter = "Q" + input.substring(0, 1);
					year = "20" + input.substring(4, 6);
					char typeChar = input.charAt(6);
					if (typeChar == 'E' || typeChar == 'e') {
						type = "Estimate";
					} else if (typeChar == ' ') {
						type = "Actual";
					}
				}

				if (type.isEmpty()) {
					type = "Actual";
				}
				jsonObject.put("Quarter", quarter);
				jsonObject.put("Year", year);
				jsonObject.put("Type", type);
				jsonObjectsList.add(jsonObject);
//				 jsonString = jsonObject.toString();
//				System.out.println(jsonString);

			}
			// pattern for Q4FY22E

			datePattern = Pattern.compile("Q[1-4]FY\\d{2}[A-Za-z]{1}$");
			matcher = datePattern.matcher(input);

			if (matcher.find()) {

				String quarter = "";
				String year = "";
				String type = "";

				input = input.trim();

				if (input.length() == 7) {

					quarter = input.substring(0, 2);
					year = "20" + input.substring(4, 6);
					char typeChar = input.charAt(6);
					if (typeChar == 'E' || typeChar == 'e') {
						type = "Estimate";
					} else if (typeChar == ' ') {
						type = "Actual";
					}
				}

				if (type.isEmpty()) {
					type = "Actual";
				}
				jsonObject.put("Quarter", quarter);
				jsonObject.put("Year", year);
				jsonObject.put("Type", type);
				jsonObjectsList.add(jsonObject);

			}
			// pattern for Q2FY22

			datePattern = Pattern.compile("Q[1-4]FY\\d{2}$");
			matcher = datePattern.matcher(input);

			if (matcher.find()) {

				String quarter = "";
				String year = "";
				String type = "";

				input = input.trim();

				if (input.length() == 6) {

					quarter = input.substring(0, 2);
					year = "20" + input.substring(4, 6);
					char typeChar = input.charAt(5);
					if (typeChar == 'E' || typeChar == 'e') {
						type = "Estimate";
					} else if (typeChar == ' ') {
						type = "Actual";
					}
				}

				if (type.isEmpty()) {
					type = "Actual";
				}

				jsonObject.put("Quarter", quarter);
				jsonObject.put("Year", year);
				jsonObject.put("Type", type);
				jsonObjectsList.add(jsonObject);

			}

			// pattern for 2QFY21

			datePattern = Pattern.compile("\\d{1,2}Q[Ff][Yy]\\d{2}(?!\\w)");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "";
				String year = "";
				String type = "";

				input = input.trim();

				if (input.length() == 6) {

					quarter = "Q" + input.substring(0, 1);
					year = "20" + input.substring(4, 6);

					if (type.isEmpty()) {
						type = "Actual";
					}

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);

				}

			}

			// for FY22
			datePattern = Pattern.compile("[Ff][Yy]\\d{2}(?!\\w)");
			matcher = datePattern.matcher(input);

			if (matcher.find()) {

				String quarter = "Null";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 4) {
					year = "20" + input.substring(2, 4);
				}
				if (type.isEmpty()) {
					type = "Actual";
				}
				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);
					jsonString = jsonObject.toString();

				}

			}

			// for FY22E
			datePattern = Pattern.compile("[Ff][Yy]\\d{2}[A-Za-z]{1}(?!\\w)");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "Null";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 5) {

					year = "20" + input.substring(2, 4);
					char typeChar = input.charAt(4);

					if (typeChar == 'e' || typeChar == 'E') {
						type = "Estimated";
					}
					if (typeChar == 'a' || typeChar == 'A') {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);
					jsonString = jsonObject.toString();

				}

			}

			// for Q1'21 pattern

			datePattern = Pattern.compile("^(Q[1-4]|\\dQ)['’]?\\d{2}$");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 5) {

					quarter = input.substring(0, 2);
					year = "20" + input.substring(3, 5);

					if (type.isEmpty()) {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);

				}

			}

			// for 03/2021a
			datePattern = Pattern.compile("^\\d{2}/\\d{4}[a-zA-Z]$");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "null";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 8) {

					// quarter=input.substring(0,2);
					year = input.substring(3, 7);

					char typeChar = input.charAt(7);
					if (typeChar == 'E' || typeChar == 'e') {
						type = "Estimate";
					}
					if (typeChar == 'A' || typeChar == 'a') {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);
					jsonString = jsonObject.toString();

				}

			}

			// for (INR)\n03/2022e
			datePattern = Pattern.compile("\\(INR\\)\\d{2}/\\d{4}[a-zA-Z]");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "null";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 15) {

					// quarter=input.substring(0,2);
					year = input.substring(10, 14);

					char typeChar = input.charAt(14);
					if (typeChar == 'E' || typeChar == 'e') {
						type = "Estimate";
					}
					if (typeChar == 'A' || typeChar == 'a') {
						type = "Actual";
					}
				}

				if (!(year == null)) {
					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
				}
			}
			// for % of Q1'22 sales
			datePattern = Pattern.compile("% of Q\\d+['’]\\d+ sales");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 16) {

					quarter = input.substring(5, 7);
					year = "20" + input.substring(8, 10);

					if (type.isEmpty()) {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);

				}

			}
			// for 2Q20
			datePattern = Pattern.compile("\\d+Q\\d{2}");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 4) {

					quarter = "Q" + input.substring(0, 1);
					year = "20" + input.substring(2, 4);

					if (type.isEmpty()) {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);

				}
			}

			// for Sep-21
			datePattern = Pattern.compile(
					"\\b(?:[Jj]an|[Ff]eb|[Mm]ar|[Aa]pr|[Mm]ay|[Jj]un|[Jj]ul|[Aa]ug|[Ss]ep|[Oo]ct|[Nn]ov|[Dd]ec)-\\d{2}\\b");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "Null";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 6) {

					// quarter="Q"+input.substring(4,1);
					year = "20" + input.substring(4, 6);

					if (type.isEmpty()) {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);

				}

			}
			// for % of fy20
			datePattern = Pattern.compile("% of fy\\d{2}");
			matcher = datePattern.matcher(input);
			if (matcher.find()) {

				String quarter = "Null";
				String year = null;
				String type = "";

				input = input.trim();

				if (input.length() == 9) {

					// quarter="Q"+input.substring(4,1);
					year = "20" + input.substring(7, 9);

					if (type.isEmpty()) {
						type = "Actual";
					}
				}

				if (!(year == null)) {

					jsonObject.put("Quarter", quarter);
					jsonObject.put("Year", year);
					jsonObject.put("Type", type);
					jsonObjectsList.add(jsonObject);

				}

			}
			/*
			 * //for 1HFY22 datePattern = Pattern.compile("1HFY\\d{2}"); matcher =
			 * datePattern.matcher(input); if (matcher.find()) {
			 * 
			 * String quarter = "Q2"; String year = null; String type = "";
			 * 
			 * input = input.trim();
			 * 
			 * if (input.length() == 6) {
			 * 
			 * // quarter="Q"+input.substring(0,1); year = "20" + input.substring(4, 6);
			 * 
			 * if (type.isEmpty()) { type = "Actual"; } }
			 * 
			 * if (!(year == null)) { System.out.println("Quarter: " + quarter);
			 * System.out.println("Year: " + year); System.out.println("Type: " + type);
			 * jsonObject.put("Quarter", quarter); jsonObject.put("Year", year);
			 * jsonObject.put("Type", type); jsonObjectsList.add(jsonObject); // jsonString
			 * = jsonObject.toString(); // System.out.println(jsonString); } // return
			 * jsonObjectsList;
			 * 
			 * } //for 9MFY21 datePattern = Pattern.compile("9MFY\\d{2}"); matcher =
			 * datePattern.matcher(input); if (matcher.find()) {
			 * 
			 * String quarter = "Q3"; String year = null; String type = "";
			 * 
			 * input = input.trim();
			 * 
			 * if (input.length() == 6) {
			 * 
			 * // quarter="Q"+input.substring(0,1); year = "20" + input.substring(4, 6);
			 * 
			 * if (type.isEmpty()) { type = "Actual"; } }
			 * 
			 * if (!(year == null)) { System.out.println("Quarter: " + quarter);
			 * System.out.println("Year: " + year); System.out.println("Type: " + type);
			 * jsonObject.put("Quarter", quarter); jsonObject.put("Year", year);
			 * jsonObject.put("Type", type); jsonObjectsList.add(jsonObject); // jsonString
			 * = jsonObject.toString(); // System.out.println(jsonString);
			 * 
			 * } // return jsonObjectsList; }
			 */

		}
		System.out.println(jsonObjectsList + "******************");
		return jsonObjectsList;
	}

	private String checkRegularExpression(String c2, String c3) {
		// TODO Auto-generated method stub

		Pattern pattern = Pattern.compile("Q[1-4]FY\\d{2}");
		Pattern pattern2 = Pattern.compile("FY\\d{2}");
		Pattern pattern3 = Pattern.compile("FY\\d{2}[A-Za-z]");
		Pattern pattern4 = Pattern.compile("^[1-9]Q[Ff][Yy][-]?[1-9][0-9]?$|^2QFY22$");
		Pattern pattern5 = Pattern.compile("^[1-9]Q[2-9][0-9]$");
		Pattern pattern6 = Pattern.compile("^Q[1-4]'\\d{2}$");
		Pattern pattern7 = Pattern.compile("^% of Q[1-4]'\\d{2} sales$");
		Pattern pattern8 = Pattern.compile("^[A-Z][a-z]{2}'\\d{2}$");
		Pattern pattern9 = Pattern.compile("^[A-Z][a-z]{2}-\\d{2}$");
		Pattern pattern10 = Pattern.compile("[Qq]\\d[Ff][Yy]\\d{2}[A-Za-z]");
		Pattern pattern11 = Pattern.compile(
				"(?:[Jj]an|[Ff]eb|[Mm]ar|[Aa]pr|[Mm]ay|[Jj]un|[Jj]ul|[Aa]ug|[Ss]ep|[Oo]ct|[Nn]ov|[Dd]ec)\\s’\\d{2}");
		Pattern pattern12 = Pattern.compile("%\\sof\\sFY\\d{2}");
		Pattern pattern13 = Pattern.compile("^\\d{2}/\\d{4}[a-zA-Z]{1}$");

		if (pattern.matcher(c2).matches() || pattern.matcher(c3).matches() || pattern2.matcher(c2).matches()
				|| pattern3.matcher(c2).matches() || pattern4.matcher(c2).matches() || pattern5.matcher(c2).matches()
				|| pattern6.matcher(c2).matches() || pattern7.matcher(c2).matches() || pattern8.matcher(c2).matches()
				|| pattern8.matcher(c3).matches() || pattern9.matcher(c2).matches() || pattern9.matcher(c3).matches()
				|| pattern10.matcher(c2).matches() || pattern11.matcher(c2).matches() || pattern12.matcher(c2).matches()
				|| pattern13.matcher(c2).matches() || pattern13.matcher(c3).matches()) {
			return c2;
		} else {
			return null;
		}

	}

	@Override
	public ArrayList<DataIngestionTableModel> getTableIngestionTableDataByClientName(String clientName, long tableId,
			String dataBaseName) throws DataIngestionDaoException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		ArrayList<DataIngestionTableModel> list = new ArrayList<>();

		try {
			connection = InvestorDatabaseUtill.getConnection();
			pstmt = connection
					.prepareStatement(DataIngestionQueryConstant.SELECT_DATAINGESTION_TABLEDETAILSBY_CLIENTNAME
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			pstmt.setString(1, clientName);
			result = pstmt.executeQuery();
//			System.out.println("resultset" + result);
			while (result.next()) {

				if (result.getLong("tableId") == tableId) {
					DataIngestionTableModel modeldata = buildData(result);

					list.add(modeldata);
				}
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionDaoException("unable to get" + e.getMessage());
		} finally {

			InvestorDatabaseUtill.close(pstmt, connection);
		}

	}

	@SuppressWarnings("resource")
	@Override
	public void deletefiledetailsbyFileid(long fileId, String dataBaseName) throws DataIngestionDaoException {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement psta = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_FORCAST
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_TABLELIST
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
			psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_FILEDETAILS
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setLong(1, fileId);
			psta.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataIngestionDaoException("unable to delete" + e.getMessage());
		}finally {

			InvestorDatabaseUtill.close(psta, con);
		}

	}

	@Override
	public NonProcessFilesDetails saveMultipleFiles(NonProcessFilesDetails model, String dataBaseName)
			throws DataIngestionDaoException {
		Connection connection = null;
		PreparedStatement psta = null;

		try {
			connection = InvestorDatabaseUtill.getConnection();
			psta = connection.prepareStatement(DataIngestionQueryConstant.INSERT_INTO_NONPROCESSFILETABLES
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, model.getClient());
			psta.setString(2, model.getFileName());
			psta.setString(3, model.getFileType());
			psta.setBytes(4, model.getFileData());
			psta.setString(5, model.getHashCode());
			psta.setString(6, "new");
			psta.setString(7, model.getClientId());
			psta.setString(8, model.getCreatedBy());
			psta.setLong(9, model.getCreatedOn());
			psta.executeUpdate();
			return model;
		} catch (Exception e) {
			throw new DataIngestionDaoException(e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, connection);
		}

	}

	@Override
	public ArrayList<NonProcessFilesDetails> getFileIds(String client, String dataBaseName)
			throws DataIngestionDaoException {

		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<NonProcessFilesDetails> modelList = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_NONPROCESS_FILEIDS
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setString(1, client);
			psta.setString(2, "new");
			rs = psta.executeQuery();
			while (rs.next()) {
				NonProcessFilesDetails model = buildnpfileids(rs);
				modelList.add(model);

			}
			return modelList;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionDaoException("unable to get " + e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}
	}

	private NonProcessFilesDetails buildnpfileids(ResultSet rs) throws SQLException {

		NonProcessFilesDetails details = new NonProcessFilesDetails();
		details.setNpFileId(rs.getInt("npFileId"));
		details.setClient(rs.getString("client"));
		details.setFileName(rs.getString("fileName"));
		details.setFileType(rs.getString("fileType"));
		details.setCreatedBy(rs.getString("createdBy"));
//		details.setFileData(rs.getBytes("fileData"));
		return details;
	}

	private DataIngestionModel builddata(ResultSet rs) throws SQLException {
		DataIngestionModel model = new DataIngestionModel();
		model.setFileId(rs.getLong("fileId"));
		model.setClient(rs.getString("client"));
		model.setDocumentType(rs.getString("documentType"));
		model.setAnalystName(rs.getString("analystName"));
		model.setPeerName(rs.getString("peerName"));
		model.setCurrency(rs.getString("currency"));
		model.setUnits(rs.getString("units"));
		model.setReportType(rs.getString("reportType"));
		model.setReportDate(rs.getDate("reportDate"));
		model.setFileName(rs.getString("fileName"));
		model.setFileType(rs.getString("fileType"));
//		model.setFileData(rs.getBytes("fileData"));
		model.setDenomination(rs.getString("denomination"));
		model.setStatus(rs.getString("status"));
		model.setConsolidated_standalone(rs.getString("consolidated_standalone"));
		model.setCreatedBy(rs.getString("createdBy"));
		model.setCreatedOn(rs.getLong("createdOn"));
		model.setReportYear(rs.getString("reportYear"));

		return model;
	}

	@Override
	public DataIngestionModel getPreviewForNonProcessFile(int npFileId, DataIngestionModel ingestionModel,
			String dataBaseName) throws DataIngestionDaoException {

		try {
			NonProcessFilesDetails details = this.getNopProcessFileDetail(npFileId, dataBaseName);

			ingestionModel.setNpFileId(details.getNpFileId());
			ingestionModel.setFileName(details.getFileName());
			ingestionModel.setFileType(details.getFileType());
			ingestionModel.setFileData(details.getFileData());
			return ingestionModel;

		} catch (Exception e) {

			throw new DataIngestionDaoException("Unable to get preview" + e.getMessage());
		}

	}

	@SuppressWarnings("unused")
	private NonProcessFilesDetails getNopProcessFileDetail(int npFileId, String dataBaseName)
			throws DataIngestionDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_NONPROCESS_FILEIDS_BYNPFILEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			psta.setInt(1, npFileId);
			rs = psta.executeQuery();
			while (rs.next()) {
				NonProcessFilesDetails details = new NonProcessFilesDetails();
				details.setNpFileId(rs.getInt("npFileId"));
				details.setFileName(rs.getString("fileName"));
				details.setFileType(rs.getString("fileType"));
				details.setFileData(rs.getBytes("fileData"));
//				details.setCreatedBy(rs.getString("createdBy"));

				return details;

			}
		} catch (Exception e) {
			throw new DataIngestionDaoException(e.getMessage());
		} finally {
			LOGGER.debug("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

		return null;

	}

	@Override
	public DataIngestionModel getPreviewForSplittedFile(int npFileId, DataIngestionModel ingestionModel,
			String dataBaseName) throws DataIngestionDaoException {

		try {
			NonProcessFilesDetails details = this.getNopProcessFileDetail(npFileId, dataBaseName);
			String name = details.getFileName();
			String fileType = details.getFileType();
			byte[] filedata = details.getFileData();
			PDDocument document = PDDocument.load(filedata);
			ArrayList<Integer> pagesToExtract = new ArrayList<>();
			String input = ingestionModel.getPagenumbers();
			System.out.println("input:" + input);
			String[] inputNumbers = input.split(",");
			for (String number : inputNumbers) {
				number = number.trim();
				// Add the first page (page 0) if it's not already in the list
				if (!pagesToExtract.contains(1)) {
					pagesToExtract.add(1);
				}
				try {
					pagesToExtract.add(Integer.parseInt(number));

				} catch (NumberFormatException e) {

					throw new DataIngestionDaoException(number + " is not a valid page number.");
				}
			}
			PDDocument newDocument = new PDDocument();
			try {
				for (int pageNumber : pagesToExtract) {
					System.out.println("pagesToExtract:" + pagesToExtract);
					PDPage page = document.getPage(pageNumber - 1);
					System.out.println("page" + page);
					newDocument.addPage(page);
				}
			} catch (Exception e) {
				throw new DataIngestionDaoException("page number not found file");
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			newDocument.save(outputStream);
			byte[] splitPdfBytes = outputStream.toByteArray();
//			System.out.println("splitPdfBytes:" + splitPdfBytes);
//			String home = System.getProperty("user.home");
//			newDocument.save(home + "/Downloads/" + name + ".pdf");
			ingestionModel.setNpFileId(npFileId);
			ingestionModel.setFileName(name);
			ingestionModel.setFileType(fileType);
			ingestionModel.setFileData(splitPdfBytes);

			return ingestionModel;
		} catch (Exception e) {

			throw new DataIngestionDaoException(e.getMessage());
		}

	}

	@Override
	public DataIngestionModel savePreviewFileForAllPages(int npFileId, DataIngestionModel ingestionModel,
			String dataBaseName) throws DataIngestionDaoException {
		
		try {
			DataIngestionModel model = this.getPreviewForNonProcessFile(npFileId, ingestionModel, dataBaseName);
			DataIngestionModel response = this.finalSaveInDataIngetionTable(npFileId, model, dataBaseName);
			return response;
		} catch (Exception e) {
			throw new DataIngestionDaoException(e.getMessage());
		}

	}

	@Override
	public DataIngestionModel savePreviewFileForSplited(int npFileId, DataIngestionModel ingestionModel,
			String dataBaseName) throws DataIngestionDaoException {

		
		try {
			DataIngestionModel model = getPreviewForSplittedFile(npFileId, ingestionModel, dataBaseName);
			DataIngestionModel response = this.finalSaveInDataIngetionTable(npFileId, model, dataBaseName);
			return response;
		} catch (Exception e) {
			throw new DataIngestionDaoException(e.getMessage());
		}

	}

	@SuppressWarnings("resource")
	private DataIngestionModel finalSaveInDataIngetionTable(int npFileId, DataIngestionModel model, String dataBaseName)
			throws DataIngestionDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_MAX_FILEID
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));

			rs = psta.executeQuery();
			while (rs.next()) {
				Date d = new Date();
				model.setCreatedOn(d.getTime());
				long maxvalue = rs.getLong(1);
				fileId = maxvalue;
				System.out.println("call here");
				psta = con.prepareStatement(DataIngestionQueryConstant.INSERT_INTO_DATA_INGESTION
						.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				model.setFileId(fileId + 1);
				psta.setInt(1, npFileId);
				psta.setLong(2, model.getFileId());
				psta.setString(3, model.getClient());
				psta.setString(4, model.getDocumentType());
				psta.setString(5, model.getAnalystName());
				psta.setString(6, model.getPeerName());
				psta.setString(7, model.getCurrency());
				psta.setString(8, model.getUnits());
				psta.setString(9, model.getReportType());
				psta.setDate(10, model.getReportDate());
				psta.setString(11, model.getFileName());
				psta.setString(12, model.getFileType());
				psta.setBytes(13, model.getFileData());
				psta.setString(14, model.getDenomination());
				psta.setString(15, model.getPagenumbers());
				psta.setString(16, "extract");
				psta.setString(17, model.getConsolidated_standalone());
				psta.setString(18, model.getCreatedBy());
				psta.setLong(19, model.getCreatedOn());
				psta.setString(20, model.getReportYear());
				if(model.getReportType().trim().equals("Annual"))
				{
					psta.setString(21,"FY"+model.getReportYear().substring(2, 4));
				}else
				{
					psta.setString(21,model.getReportType().trim()+"FY"+model.getReportYear().substring(2, 4));
				}
				
				psta.executeUpdate();
				LOGGER.info("data add in data ingestion file table");
				LOGGER.info("extract the file");

				try {
					this.extractFileByFileId(model.getFileId(), dataBaseName); // call extract method
				} catch (Exception e) {
					// TODO: handle exception
					psta = con.prepareStatement(DataIngestionQueryConstant.DELETE_DATAINGESTION_FILEDETAILS
							.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
					psta.setLong(1, model.getFileId());
					psta.executeUpdate();
					throw new DataIngestionDaoException(e.getMessage());
				}
				model.setStatus("extract");
				psta = con.prepareStatement(DataIngestionQueryConstant.UPDATE_STATUSNONPROCESSFILES
						.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
				psta.setString(1, "extract");
				psta.setInt(2, model.getNpFileId());
				psta.executeUpdate();
				return model;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataIngestionDaoException(e.getMessage());

		} finally {
			LOGGER.info("closing the connections");
			InvestorDatabaseUtill.close(psta, con);
		}

		return null;
	}

	@Override
	public ArrayList<NonProcessFilesDetails> getnonProcessFiles(String dataBaseName) throws DataIngestionDaoException {
		Connection con = null;
		PreparedStatement psta = null;
		ResultSet rs = null;
		ArrayList<NonProcessFilesDetails> list = new ArrayList<>();
		try {
			con = InvestorDatabaseUtill.getConnection();
			psta = con.prepareStatement(DataIngestionQueryConstant.SELECT_NONPROCESSING_FILES
					.replace(DataIngestionQueryConstant.DATA_BASE_PLACE_HOLDER, dataBaseName));
			System.out.println("check 1======>");
			psta.setString(1, "new");
			System.out.println("check 1======>");
			rs = psta.executeQuery();
			while (rs.next()) {
				NonProcessFilesDetails details = buildFilesData(rs);
				list.add(details);

			}
			return list;
		} catch (Exception e) {
			throw new DataIngestionDaoException(e.getMessage());
		} finally {

			InvestorDatabaseUtill.close(psta, con);
		}

	}

	private NonProcessFilesDetails buildFilesData(ResultSet rs) throws SQLException {
		System.out.println("rs.getInt(\"npFileId\")====>" + rs.getInt("npFileId"));
		NonProcessFilesDetails details = new NonProcessFilesDetails();
		details.setNpFileId(rs.getInt("npFileId"));
		details.setClient(rs.getString("client"));
		details.setClientId(rs.getString("clientId"));
		details.setFileName(rs.getString("fileName"));
		details.setFileType(rs.getString("filetype"));

		if (rs.getString("peers") != null) {
			String suggestedPeers = rs.getString("peers");
			suggestedPeers = suggestedPeers.substring(1, suggestedPeers.length() - 1); // Remove the square brackets
			String[] suggestedPeer = suggestedPeers.split(", ");
			ArrayList<String> suggestedPeerlist = new ArrayList<>(Arrays.asList(suggestedPeer));

//			 ArrayList<String> suggestedPeerlist =new ArrayList<>();
//			 suggestedPeerlist.add(suggestedPeers);
			details.setSuggestedPeers(suggestedPeerlist);
		} else {
			details.setSuggestedPeers(null);

		}

		return details;

	}

	@Override
	public ArrayList<DataIngestionMappingModel> getPriviewDataIngestionMappingTableData(
			ArrayList<DataIngestionMappingModel> dataIngestionMappingTable, String dataBaseName)
			throws DataIngestionDaoException {
		String valueString = null;

		Iterator ir = dataIngestionMappingTable.iterator();

		while (ir.hasNext()) {

			DataIngestionMappingModel dataIngestionMapping = (DataIngestionMappingModel) ir.next();
			System.out.println("ataIngestionMapping.getDenomination()" + dataIngestionMapping.getDenomination());
			System.out.println("dataIngestionMapping.getLineItemName()" + dataIngestionMapping.getLineItemName());
			dataIngestionMapping.setOriginalValue(dataIngestionMapping.getValue());
			if (dataIngestionMapping.getLineItemName().contains("%")) {
				System.out.println("dataIngestionMapping" + dataIngestionMapping.getValue());
			} else {
				String regexIgnore = "^[a-zA-Z\\s-]*$";
				switch (dataIngestionMapping.getDenomination()) {

				case "Thousand":
					valueString = dataIngestionMapping.getValue();
					String regexPattern = "[,\\s]+";
					valueString = valueString.replaceAll(regexPattern, "");
					if (valueString.matches(regexIgnore)) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("bps")) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("%")) {
						dataIngestionMapping.setValue(valueString);
					} else {
						valueString = valueString.replaceAll("[a-zA-Z]", "");
						double ThousandValue = Double.parseDouble(valueString);
						ThousandValue *= 1000;
						dataIngestionMapping.setValue(String.valueOf(ThousandValue));
					}
					break;
				case "Lakhs":
					valueString = dataIngestionMapping.getValue();
					String regexPattern1 = "[,\\s]+";
					valueString = valueString.replaceAll(regexPattern1, "");
					if (valueString.matches(regexIgnore)) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("bps")) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("%")) {
						dataIngestionMapping.setValue(valueString);
					} else {
						valueString = valueString.replaceAll("[a-zA-Z]", "");
						double lakhValue = Double.parseDouble(valueString);
						lakhValue *= 100000;
						dataIngestionMapping.setValue(String.valueOf(lakhValue));
					}
					break;
				case "Crore":
					valueString = dataIngestionMapping.getValue();
					String regexPattern2 = "[,\\s]+";
					valueString = valueString.replaceAll(regexPattern2, "");
					if (valueString.matches(regexIgnore)) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("bps")) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("%")) {
						dataIngestionMapping.setValue(valueString);
					} else {
						valueString = valueString.replaceAll("[a-zA-Z]", "");
						double CoreValue = Double.parseDouble(valueString);
						CoreValue *= 10000000;
						dataIngestionMapping.setValue(String.valueOf(CoreValue));
					}
					break;
				case "Million":
					valueString = dataIngestionMapping.getValue();
					String regexPattern3 = "[,\\s]+";
					valueString = valueString.replaceAll(regexPattern3, "");
					if (valueString.matches(regexIgnore)) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("bps")) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("%")) {
						dataIngestionMapping.setValue(valueString);
					} else {
						valueString = valueString.replaceAll("[a-zA-Z]", "");
						double millionValue = Double.parseDouble(valueString);
						millionValue *= 1000000;
						dataIngestionMapping.setValue(String.valueOf(millionValue));
					}
					break;
				case "Billion":
					valueString = dataIngestionMapping.getValue();
					String regexPattern4 = "[,\\s]+";
					valueString = valueString.replaceAll(regexPattern4, "");
					if (valueString.matches(regexIgnore)) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("bps")) {
						dataIngestionMapping.setValue(valueString);
					} else if (valueString.contains("%")) {
						dataIngestionMapping.setValue(valueString);
					} else {
						valueString = valueString.replaceAll("[a-zA-Z]", "");
						System.out.println("value String " + valueString);
						double billionValue = Double.parseDouble(valueString);
						billionValue *= 1000000000;
						dataIngestionMapping.setValue(String.valueOf(billionValue));
					}

					break;
				default:
					System.out.println("Invalid denomination.");
				}
			}
		}

		return dataIngestionMappingTable;
	}

}
