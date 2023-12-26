package com.Anemoi.InvestorRelation.ShareHolderMeeting;

import java.io.File;

import io.micronaut.http.multipart.CompletedFileUpload;

public interface MediaServiceInterface {
	
	void upload(String key,CompletedFileUpload file);

	File getMediaFileByKey(String mediakey);
	
	String deleteMediaFile(String mediakey);

	String uploadlogoFile(String logoKey, CompletedFileUpload logofile);

}
