package com.Anemoi.InvestorRelation.whitelabeling;

import java.util.ArrayList;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface WhitelableingService {

//	WhiteLableingEntity addLogoAndCssFile(String clientName,String createdBy,String filePath, CompletedFileUpload cssfile) throws ServiceException;

	WhiteLableingEntity addLogoAndCssFile(String clientName,String createdBy,CompletedFileUpload logofile, CompletedFileUpload cssfile) throws ServiceException;

	
	ArrayList<WhiteLableingEntity> getlist() throws ServiceException;


	WhiteLableingEntity getbyId(String clientName) throws ServiceException;

}
