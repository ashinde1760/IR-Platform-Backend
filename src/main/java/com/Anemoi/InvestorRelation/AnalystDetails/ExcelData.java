package com.Anemoi.InvestorRelation.AnalystDetails;

import java.io.File;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Singleton;

@Singleton
public class ExcelData {

	private ArrayList<Analyst> analysts = new ArrayList<>();

	private ArrayList<AnalystContactDetails> analystContactDetails = new ArrayList<>();

	public String EexcelToJson(File excelFile, File jsonFile) throws AnalystDetailsDaoException {

		ExcelData jsonGenerator = new ExcelData();

		jsonGenerator.readRows(excelFile);

		jsonGenerator.showRecords();

		jsonGenerator.buildTree();

		String generateJsonString = jsonGenerator.generate(jsonFile);

		System.out.print("\nOutpupt JSON FILE :  " + jsonFile.getAbsolutePath());

		return generateJsonString;

	}

	@SuppressWarnings("resource")
	public void readRows(File file) throws AnalystDetailsDaoException {
		try {
			XSSFWorkbook wb = new XSSFWorkbook(file);
			System.out.println("workbook: " + wb);

			XSSFSheet sheet = wb.getSheetAt(0);
			int lastRowNum = sheet.getLastRowNum();

			int numRows = sheet.getLastRowNum() + 1;
			if (numRows <= 1) {
//				System.out.println("excel has no data");
				throw new AnalystDetailsDaoException("Excel sheet is empty");
			}
			int rownum = 0;
			for (Row row1 : sheet) {
				if (rownum == 0) {
					rownum++;
					continue;
				}
			System.out.println("worksheet: " + sheet + " :: Total row of analyst: " + lastRowNum);
					XSSFRow row = sheet.getRow(rownum);
					Analyst sd = new Analyst();
					sd.convertRow(row);
					analysts.add(sd);
					rownum++;
				}
			
			
			wb.close();
		} catch (Exception e) {
			throw new AnalystDetailsDaoException(e.getMessage());
		
		}
	}

	public String generate(File jsonFile) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			System.out.println("productsList: " + analysts);

			String jsonInString = mapper.writeValueAsString(this.analysts);

			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analysts);
			mapper.writeValue(jsonFile, analysts);
			String convertToDesireJsonData = convertToDesireJsonData(jsonFile);
			return convertToDesireJsonData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String convertToDesireJsonData(File jsonFile) {

		try {
			// Read the input JSON data from a file
			ObjectMapper mapper = new ObjectMapper();
			List<Analyst> analysts = mapper.readValue(jsonFile, new TypeReference<List<Analyst>>() {
			});

			// Group the analyst contact details by analyst name
			Map<String, List<AnalystContactDetails>> groupedContacts = new HashMap<>();
			for (Analyst analyst : analysts) {
				String analystName = analyst.getAnalystName();
				List<AnalystContactDetails> contacts = analyst.getAnalystContactDetails();
				if (groupedContacts.containsKey(analystName)) {
					groupedContacts.get(analystName).addAll(contacts);
				} else {
					groupedContacts.put(analystName, new ArrayList<>(contacts));
				}
			}

			// Create a new list of Analyst objects with the grouped contact details
			List<Analyst> groupedAnalysts = new ArrayList<>();
			for (String analystName : groupedContacts.keySet()) {
				Analyst analyst = new Analyst(analystName);
				analyst.setAnalystContactDetails(groupedContacts.get(analystName));
				groupedAnalysts.add(analyst);
			}

			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(groupedAnalysts);
			System.out.println("JsonInString " + jsonInString);
			return jsonInString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

//	private String convertToDesireJsonData(File jsonFile) {
//		try {
//			// Read the input JSON data from a file
//			ObjectMapper mapper = new ObjectMapper();
//			List<Analyst> analysts = mapper.readValue(jsonFile, new TypeReference<List<Analyst>>() {
//			});
//
//			// Group the analyst contact details by analyst name
//			Map<String, Analyst> groupedAnalysts = new HashMap<>();
//			for (Analyst analyst : analysts) {
//				String analystName = analyst.getAnalystName().toUpperCase();
//				if (groupedAnalysts.containsKey(analystName)) {
//					Analyst existingAnalyst = groupedAnalysts.get(analystName);
//					List<AnalystContactDetails> existingContacts = existingAnalyst.getAnalystContactDetails();
//					List<AnalystContactDetails> newContacts = analyst.getAnalystContactDetails();
//					existingContacts.addAll(newContacts);
//					existingAnalyst.setAnalystContactDetails(existingContacts);
//				} else {
//					groupedAnalysts.put(analystName, analyst);
//				}
//			}
//
//			// Create a new list of Analyst objects with the grouped contact details
//			List<Analyst> groupedList = new ArrayList<>(groupedAnalysts.values());
//
//			// Write the output JSON data to a file
//			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(groupedList);
//			return jsonInString;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	private void showRecords() {
		System.out.print("\n============================ Records from Excel file ============================ "
				+ analysts.size());

		for (int i = 0; i < analysts.size(); i++) {
			Analyst sd = analysts.get(i);
			System.out.print("\nRecord : " + sd.getAnalystName() + " \n" + sd.getAnalystContactDetails());
		}
	}

	public String buildTree() {
		String msg = "";

		for (int i = 0; i < analysts.size(); i++) {
			Analyst sd = analysts.get(i);

			this.analystContactDetails.addAll(sd.getAnalystContactDetails());

			System.out.print("\nRecord of json files : " + i + " = " + sd);
			for (AnalystContactDetails data : sd.getAnalystContactDetails()) {
				System.out.print("\ngetting data=============>: " + data.getPocName() + "\n" + data.getPocEmailId());
				this.analystContactDetails.add(data);

			}

		}
		return msg;
	}

}
