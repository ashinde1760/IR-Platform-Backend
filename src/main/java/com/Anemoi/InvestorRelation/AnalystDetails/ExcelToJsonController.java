//package com.Anemoi.InvestorRelation.AnalystDetails;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//
//import io.micronaut.http.MediaType;
//import io.micronaut.http.annotation.Controller;
//import io.micronaut.http.annotation.Post;
//import io.micronaut.http.multipart.CompletedFileUpload;
//import jakarta.inject.Inject;
//
//@Controller("/test")
//public class ExcelToJsonController {
//
//	@Inject
//	ExcelData excelData;
//
//	@Inject
//	AnalystDetailsService service;
//
//	@Post(value = "/", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
//	public String convertExcellToJson(CompletedFileUpload file) throws FileNotFoundException {
//		try {
//
//			if ((file.getFilename() == null || file.getFilename().equals(""))) {
//				return "No File found";
//			}
//			String filename = file.getFilename();
//			String home = System.getProperty("user.home");
//			String path = home + "\\" + filename;
//			byte[] data = file.getBytes();
//
//			File excel = new File(path);
//			FileOutputStream fout = new FileOutputStream(excel);
//			fout.write(data);
//			fout.close();
//
//			File json = new File(home + "\\output" + ".json");
//			String testGenerateJsonFromExcelTemplate = this.excelData.EexcelToJson(excel, json);
//			return testGenerateJsonFromExcelTemplate;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
