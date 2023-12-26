package com.Anemoi.Resource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHoderDataFromDao;
import com.Anemoi.InvestorRelation.ShareHolderDataFrom.ShareHoderDataFromService;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ResourceServiceImpl implements ResourceSerice {

	
	@Inject
	private ShareHoderDataFromService share;
	
	@Override
	public Media getAnalystDetailsFile(String fileType) {
		try {
			Media media = new Media();
			if (fileType.contentEquals("Master Database")) {

				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.analyst.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;

			} else if (fileType.contentEquals("Details")) {
				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.Details.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;
			}

			else if (fileType.contentEquals("Analyst Line Item Nomenclature")) {

				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.Nomenclature.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;
			} else if (fileType.contentEquals("Client Line Item Nomenclature")) {

				String analystDetailsfile = ReadPropertiesFile
						.readRequestProperty("media.resource.ClientLineItemNomenclature.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;
			}

			else if (fileType.contentEquals("Shareholder Data")) {

				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.Shareholderdata.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				ArrayList<String> micodeNames=this.share.getMinorCodeNames();
				if(micodeNames.isEmpty())
				{
					media.setMediaName(analystDetailsfile);
					media.setMediaInputStream(is);;
				}
				else {
				   String[] dropdownValues = micodeNames.toArray(new String[0]);
				 
				    XSSFWorkbook workbook = new XSSFWorkbook(is);
				    XSSFSheet sheet = workbook.getSheetAt(0);
				applyDataValidation(sheet, dropdownValues, 1, 1048575, 5);
				ByteArrayOutputStream updatedWorkbookStream = new ByteArrayOutputStream();
				workbook.write(updatedWorkbookStream);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(new ByteArrayInputStream(updatedWorkbookStream.toByteArray()));
				
				}
				return media;
				
			} else if (fileType.contentEquals("Meeting Data")) {

				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.meetingdata.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				System.out.println("is" + is);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;
			}else if (fileType.contentEquals("Shareholder Contact")) {

				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.Shareholdercontact.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				System.out.println("is" + is);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;
			}
			else if (fileType.contentEquals("Mom file")) {

				String analystDetailsfile = ReadPropertiesFile.readRequestProperty("media.resource.MomFile.file");
				System.out.println(analystDetailsfile);
				InputStream is = getClass().getClassLoader().getResourceAsStream(analystDetailsfile);
				System.out.println("is" + is);
				media.setMediaName(analystDetailsfile);
				media.setMediaInputStream(is);
				return media;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private void applyDataValidation(XSSFSheet sheet, String[] dropdownValues, int i, int j, int k) {
		// TODO Auto-generated method stub
		DataValidationHelper validationHelper = sheet.getDataValidationHelper();

	    DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(dropdownValues);

	    CellRangeAddressList addressList = new CellRangeAddressList(i,j,k,k);

	    DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);

	    dataValidation.setShowErrorBox(true);

	    sheet.addValidationData(dataValidation);

 

	}
	}


