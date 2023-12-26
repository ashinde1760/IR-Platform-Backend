
package com.Anemoi.InvestorRelation.AnalystDetails;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.CashFlow.CashFlowControllerException;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;

@Controller("/investor/analystDetails")
public class AnalystDetailsController {

	@Inject
	AnalystDetailsService service;


	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	
	@Post("/add")
	public HttpResponse<AnalystDetailsEntity> createAnalystDetails(@Body AnalystDetailsEntity entity)
			throws AnalystDetailsControllerException {
		try {
			AnalystDetailsEntity analystentity = this.service.createAnalystDetails(entity);
			return HttpResponse.status(HttpStatus.OK).body(analystentity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}

	}

	@Get("/{analystId}")
	public HttpResponse<AnalystDetailsEntity> getAnalystDetails(@PathVariable("analystId") long analystId)
			throws AnalystDetailsControllerException {
		try {
			AnalystDetailsEntity analystEntity = this.service.getAnalystDetailsById(analystId);
			return HttpResponse.status(HttpStatus.OK).body(analystEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public ArrayList<AnalystDetailsModified> getAllAnalstDetails() throws AnalystDetailsControllerException {
		try {
			ArrayList<AnalystDetailsModified> entity = this.service.getAllAnalystDetails();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Patch("/{analystId}")
	public HttpResponse<AnalystDetailsEntity> updateAnalystDetails(@Body AnalystDetailsEntity analystEntity,
			@PathVariable("analystId") long analystId) throws AnalystDetailsControllerException {
		try {
			AnalystDetailsEntity analyst = this.service.updateAnalystDetails(analystEntity, analystId);
			return HttpResponse.status(HttpStatus.OK).body(analyst);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}

	}

	@Delete("/{analystId}")
	public HttpResponse<AnalystDetailsEntity> deleteAnalystDetails(@PathVariable("analystId")long analystId,String createdBy)
			throws AnalystDetailsControllerException {
		try {
			AnalystDetailsEntity analyst = this.service.deleteAnalystDetails(analystId,createdBy);
			return HttpResponse.status(HttpStatus.OK).body(analyst);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	
	@Delete("/analystContactDetails/{analystId}/{analystcontactid}")
	public String deleteAnalystContactDetails(@PathVariable("analystId") long analystId,@PathVariable("analystcontactid") long analystcontactid) throws AnalystDetailsControllerException
	{
		try {
		String response=this.service.deleteAnalystContactDetails(analystId,analystcontactid);
		return response;
		}
		
		catch (Exception e) {
			
			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Post(uri = "/uploadAnalystDetailsExcelSheet", consumes = { MediaType.APPLICATION_JSON,
			MediaType.MULTIPART_FORM_DATA })
	public MutableHttpResponse<?> uploadExcelSheetAnalystDetilas(  @QueryValue String createdBy,
            @Part("file") CompletedFileUpload file)
			throws AnalystDetailsControllerException {
		try {
			ArrayList<AnalystDetailsEntity> response = this.service.uploadAnalstDetailsExcelSheet(createdBy,file);
			if(response.isEmpty())
			{
				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "Analyst details excel sheet upload successfully");
				return HttpResponse.status(HttpStatus.OK).body(reposneJSON.toString());
			}else
			{
				return HttpResponse.status(HttpStatus.FOUND).body(response);
			}
			
		} catch (Exception e) {

			throw new AnalystDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
}
