package com.Anemoi.InvestorRelation.whitelabeling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryService;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WhiteLableingServiceImpl implements WhitelableingService {

	
	@Inject
	WhiteLableingDao dao;
	
	@Inject
	private AuditHistoryService auditHistoryService;
	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	private static String DATABASENAME = "databaseName";

	private static String dataBaseName() {

		List<String> tenentList = ReadPropertiesFile.getAllTenant();
		for (String tenent : tenentList) {
			DATABASENAME = ReadPropertiesFile.dataBaseName(tenent);
		}
		return DATABASENAME;

	}
	@Override
	public WhiteLableingEntity addLogoAndCssFile(String clientName,String createdBy,CompletedFileUpload logofile, CompletedFileUpload cssfile) throws ServiceException{
		
		String dataBaseName=WhiteLableingServiceImpl.dataBaseName();
		try
		{
			System.out.println();
			WhiteLableingEntity entity=new WhiteLableingEntity();
			entity.setClientName(clientName);
			String fileName1=cssfile.getFilename();
			String fileType1=cssfile.getContentType().toString();
			byte[] fileData1=cssfile.getBytes();
			entity.setCssFileName(fileName1);
			entity.setCssFileType(fileType1);
			entity.setCssFileData(fileData1);
			entity.setFileName(logofile.getFilename());
			entity.setFileType(logofile.getContentType().toString());
			entity.setFileData(logofile.getBytes());
//			entity.setFilePath(filePath);
			entity.setCreatedBy(createdBy);
			WhiteLableingEntity response=this.dao.addcssFileAndLogoFile(entity,dataBaseName);
			
			Date d=new Date();
			AuditHistoryEntity entityAuditHistoryEntity=new AuditHistoryEntity();
			entityAuditHistoryEntity.setCreatedBy(createdBy);
			entityAuditHistoryEntity.setActivity("add whitelableing");
			entityAuditHistoryEntity.setDescription("add whitelabling in application");
			entityAuditHistoryEntity.setCreatedOn(d.getTime());
			this.auditHistoryService.addAuditHistory(entityAuditHistoryEntity);
			
			return response;
		}
		catch (Exception e) {
		throw new ServiceException(e.getMessage());
		}
		
	}
	@Override
	public ArrayList<WhiteLableingEntity> getlist() throws ServiceException {
	   
		String dataBaseName=WhiteLableingServiceImpl.dataBaseName();
		try
		{
			ArrayList<WhiteLableingEntity>  responselist=this.dao.getlist(dataBaseName);
			return responselist;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public WhiteLableingEntity getbyId(String clientName) throws ServiceException {
		String dataBaseName=WhiteLableingServiceImpl.dataBaseName();
		try
		{
			WhiteLableingEntity  response=this.dao.getById(clientName,dataBaseName);
			return response;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
