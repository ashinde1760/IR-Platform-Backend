package com.Anemoi.InvestorRelation.AnalystLineItem;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.owasp.encoder.Encode;
import com.Anemoi.InvestorRelation.AnalystDetails.AnalystDetailsControllerException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetControllerException;
import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetEntity;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;

@Controller("/investor/analyst")
public class AnalystLineItemController {

	@Inject
	private AnalystLineItemService analystlineitemservice;
	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	@Post("/add")
	public HttpResponse<AnalystLineItemEntity> addAnalystLineItemDetails(@Body AnalystLineItemEntity analystlineitem)
			throws AnalystLineItemControllerException {
		try {
			AnalystLineItemEntity analyst = this.analystlineitemservice.createAnalystLineItem(analystlineitem);
			return HttpResponse.status(HttpStatus.OK).body(analyst);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Consumes(value = { "application/json" })
	@Produces(value = { "application/json" })
	@Post("/addMultipleObject")
	public ArrayList<AnalystLineItemEntity> addMultipleObjectAnalystLineItem(
			@Body ArrayList<AnalystLineItemEntity> analystLineItem) throws AnalystLineItemControllerException {
		try {
			ArrayList<AnalystLineItemEntity> entity = this.analystlineitemservice.addMultipleObject(analystLineItem);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/{aid}")
	public HttpResponse<AnalystLineItemEntity> getAnalystLineItemById(@PathVariable("aid") String aid)
			throws AnalystLineItemControllerException {
		try {
			AnalystLineItemEntity analystlineitem = this.analystlineitemservice.getAnalystLineItemById(aid);
			return HttpResponse.status(HttpStatus.OK).body(analystlineitem);

		} catch (Exception e) {
			e.printStackTrace();

			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public List<AnalystLineItemEntity> getAnalystLineItem() throws AnalystLineItemControllerException {
		try {
			List<AnalystLineItemEntity> analystlineitem = this.analystlineitemservice.getAllAnalystLineItemDetails();
			 for (AnalystLineItemEntity item : analystlineitem) {
		            item.setAnalystLineItemName(escapeHtml(item.getAnalystLineItemName())); // Properly escape HTML
		        }
			return analystlineitem;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
		  return Encode.forHtmlContent(input);
	  }
	@Get("/{analystName}/{masterTableSource}")
	public ArrayList<AnalystLineItemEntity> getbyAnalystName(@PathVariable("analystName") String analystName,
			@PathVariable("masterTableSource") String masterTableSource) throws AnalystLineItemControllerException {
		try {
			ArrayList<AnalystLineItemEntity> analystlineitemlist = this.analystlineitemservice
					.getbyAnalystName(analystName, masterTableSource);
			return analystlineitemlist;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Patch("/lineitemName")
	public HttpResponse<String> updateAnalystLineItem(@Body AnalystDetails analystDetails)
			throws AnalystLineItemControllerException {
		try {
			String response = this.analystlineitemservice.updateAnalystLineItem(analystDetails);
			return HttpResponse.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Post(uri = "/uploadAnalystLineItemExcelSheet", consumes = { MediaType.APPLICATION_JSON,
			MediaType.MULTIPART_FORM_DATA })
	public MutableHttpResponse<?> uploadAnalystLineItemExcelSheet(  @QueryValue String createdBy,
            @Part("file") CompletedFileUpload file)
			throws AnalystLineItemControllerException {
		try {

			ArrayList<AnalystLineItemEntity> response = this.analystlineitemservice.uploadAnalstLineItemExcelSheet(createdBy,file);
			if(response.isEmpty())
			{
				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "Analyst line item nomenclature excel sheet upload  successfully");
				return HttpResponse.status(HttpStatus.OK).body(reposneJSON.toString());
			}else {
			
			return HttpResponse.status(HttpStatus.FOUND).body(response);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Patch("updateAnalystlineItem/{analystLineId}")
	public HttpResponse<AnalystLineItemEntity> updateAnalystlineItem(@Body AnalystLineItemEntity analystLineItem,
			@PathVariable("analystLineId") String analystLineId) throws AnalystLineItemControllerException {
		try {

			AnalystLineItemEntity response = this.analystlineitemservice.updateAnalystLineItem(analystLineItem,
					analystLineId);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			throw new AnalystLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

}
