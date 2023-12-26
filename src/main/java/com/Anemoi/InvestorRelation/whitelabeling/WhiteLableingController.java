package com.Anemoi.InvestorRelation.whitelabeling;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.Anemoi.InvestorRelation.ClientDetails.ClientDetailsControllerException;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.ShareHolderMeeting.MediaServiceInterface;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Inject;

@Controller("/investor/whitelableing")
public class WhiteLableingController {
	
	@Inject
	WhitelableingService service;
	
	@Inject
	private MediaServiceInterface serviceInterface;
	
	@Post(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA)
	public WhiteLableingEntity addFiles( String clientName,String createdBy,CompletedFileUpload logofile,CompletedFileUpload cssfile) throws ControllerException
	{
		try
		{
	String k=UUID.randomUUID().toString();
	String key=k.substring(0, 8);
	String logoKey=key+logofile.getFilename();
//	String filePath=this.serviceInterface.uploadlogoFile(logoKey,logofile);
//		WhiteLableingEntity response=this.service.addLogoAndCssFile(clientName,createdBy,filePath,cssfile);
	WhiteLableingEntity response=this.service.addLogoAndCssFile(clientName,createdBy,logofile,cssfile);

		return response;
		}
		catch (Exception e) {
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}

	}
	@Get("/getlist")
	public ArrayList<WhiteLableingEntity> getlist() throws ControllerException
	{
		try
		{
			ArrayList<WhiteLableingEntity> responseList=this.service.getlist();
			return responseList;
		}
		catch (Exception e) {
			throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}
	
	@Get("getbyClientName/{clientName}")
	public MutableHttpResponse<StreamedFile> getDataById(@PathVariable("clientName") String clientName) throws ControllerException
	{ try
	{
		WhiteLableingEntity response=this.service.getbyId(clientName);
		byte[] byteArray = response.getFileData();
		InputStream inputStream = new ByteArrayInputStream(byteArray);
		StreamedFile streamedFile = new StreamedFile(inputStream, MediaType.IMAGE_JPEG_TYPE);

		return HttpResponse.ok()
		        .contentType(MediaType.IMAGE_JPEG_TYPE)
		        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + response.getFileName())
		        .body(streamedFile);
	
	}
	catch (Exception e) {
		throw new ControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
				e.getMessage());
	}
	}
}
