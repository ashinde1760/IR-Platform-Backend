package com.Anemoi.InvestorRelation.ClientLineItem;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystDetails;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemControllerException;
import com.Anemoi.InvestorRelation.AnalystLineItem.AnalystLineItemEntity;
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

@Controller("investor/client")
public class ClientLineItemController {

	@Inject
	ClientLineItemService clientLineItemService;
	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	@Post("/add")
	public HttpResponse<ClientLineItemEntity> addClientLineItemDetails(@Body ClientLineItemEntity clientLineitem)
			throws ClientLineItemControllerException {
		try {
			ClientLineItemEntity clientdetails = this.clientLineItemService.createClientLineItem(clientLineitem);
			return HttpResponse.status(HttpStatus.OK).body(clientdetails);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Consumes(value = { "application/json" })
	@Produces(value = { "application/json" })
	@Post("/addMultipleObject")
	public ArrayList<ClientLineItemEntity> addMultipleObjectClientLineItem(
			@Body ArrayList<ClientLineItemEntity> clientLineItem) throws ClientLineItemControllerException {
		try {
			ArrayList<ClientLineItemEntity> entity = this.clientLineItemService
					.addMultipleObjectClientLineItem(clientLineItem);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/{clientLineId}")
	public HttpResponse<ClientLineItemEntity> getClientLineItemById(@PathVariable("clientLineId") String clientLineId)
			throws ClientLineItemControllerException {
		try {
			ClientLineItemEntity entity = this.clientLineItemService.getClientLiteItemById(clientLineId);
			return HttpResponse.status(HttpStatus.OK).body(entity);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Get("/getlist")
	public ArrayList<ClientLineItemEntity> getClientLineItemList() throws ClientLineItemControllerException {
		try {
			ArrayList<ClientLineItemEntity> entitylist = this.clientLineItemService.getclientlineItemList();
			return entitylist;
		} catch (Exception e) {

			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Get("/{clientName}/{masterTableSource}")
	public ArrayList<ClientLineItemEntity> getByClientName(@PathVariable("clientName") String clientName,
			@PathVariable("masterTableSource") String masterTableSource) throws ClientLineItemControllerException {
		try {
			ArrayList<ClientLineItemEntity> entity = this.clientLineItemService.getByClientName(clientName,
					masterTableSource);
			return entity;
		} catch (Exception e) {

			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Patch("/lineitemName")
	public HttpResponse<String> updateLineItemName(@Body ClientDetails clientDetails)
			throws ClientLineItemControllerException {
		try {
			String response = this.clientLineItemService.updateLineItemName(clientDetails);
			return HttpResponse.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Post(uri = "/uploadClientLineItemExcelSheet", consumes = { MediaType.APPLICATION_JSON,
			MediaType.MULTIPART_FORM_DATA })
	public HttpResponse<?> uploadClientLineItemExcelSheet(  @QueryValue String createdBy,
            @Part("file") CompletedFileUpload file)
			throws ClientLineItemControllerException {
		try {

			ArrayList<ClientLineItemEntity> response = this.clientLineItemService.uploadClientLineItemExcelSheet(createdBy,file);
			if(response.isEmpty())
			{
				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "Client line item nomenclature excel sheet upload  successfully");
				return HttpResponse.status(HttpStatus.OK).body(reposneJSON.toString());
			}
			else
			{
			return HttpResponse.status(HttpStatus.FOUND).body(response);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Patch("/updateClientLineItem/{clientLineId}")
	public HttpResponse<ClientLineItemEntity> updateClientLineItem(@Body ClientLineItemEntity clientLineItem,
			@PathVariable("clientLineId") String clientLineId) throws ClientLineItemControllerException {
		try {
			ClientLineItemEntity cliententity = this.clientLineItemService.updateClientLineItem(clientLineItem,
					clientLineId);
			return HttpResponse.status(HttpStatus.OK).body(cliententity);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ClientLineItemControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}
}
