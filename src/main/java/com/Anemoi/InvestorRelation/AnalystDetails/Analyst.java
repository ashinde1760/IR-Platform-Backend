package com.Anemoi.InvestorRelation.AnalystDetails;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Analyst {

	private String analystName;
//	private List<String> analystName;

	private List<AnalystContactDetails> analystContactDetails;

	public String getAnalystName() {
		return analystName;
	}

	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}

	public List<AnalystContactDetails> getAnalystContactDetails() {
		return analystContactDetails;
	}

	public void setAnalystContactDetails(List<AnalystContactDetails> analystContactDetails) {
		this.analystContactDetails = analystContactDetails;
	}

	public Analyst(String analystName, List<AnalystContactDetails> analystContactDetails) {

		this.analystName = analystName;
		this.analystContactDetails = analystContactDetails;

	}

	public Analyst() {
	}
	
	public Analyst(String analystName) {
		this.analystName = analystName;
	}

	public void convertRow(XSSFRow row) {

		Analyst analyst = new Analyst();
		AnalystContactDetails contactDetails = new AnalystContactDetails();
		ArrayList<AnalystContactDetails> details = new ArrayList<>();
		String pocName;
		String pocEmail;
		String analystName = null;

		Cell cell = row.getCell(0);
		if (cell != null && cell.getCellType() != CellType.BLANK) {
			analystName = cell.getStringCellValue();
			analyst.setAnalystName(analystName);

		}

		cell = row.getCell(1);
		if (cell != null && cell.getCellType() != CellType.BLANK) {
			pocName = cell.getStringCellValue();
			contactDetails.setPocName(pocName);

		}

		cell = row.getCell(2);
		if (cell != null && cell.getCellType() != CellType.BLANK) {
			pocEmail = cell.getStringCellValue();
			contactDetails.setPocEmailId(pocEmail);

		}

		this.analystName = analystName;
		details.add(contactDetails);
		analyst.setAnalystContactDetails(details);
		this.analystContactDetails = details;
	}

}
