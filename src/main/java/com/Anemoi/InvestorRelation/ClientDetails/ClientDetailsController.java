package com.Anemoi.InvestorRelation.ClientDetails;

import java.util.ArrayList;

import com.Anemoi.InvestorRelation.AnalystDetails.AnalystDetailsControllerException;
import com.Anemoi.InvestorRelation.ClientLineItem.ClientLineItemControllerException;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;

@Controller("Investor/ClientDetails")
public class ClientDetailsController {

	@Inject
	ClientDetailsService clientDetailsService;
	
	  private final HttpClient httpClient;

	    public ClientDetailsController(@Client("https://vqmlozkqomfmyq9-dev3db.adb.ap-mumbai-1.oraclecloudapps.com") HttpClient httpClient) {
	        this.httpClient = httpClient;
	    }

	@Post("/addprojectCode")
	public ClientDetailsEntity addProjectCodeForClient(@Body ClientDetailsEntity clientDetailsEntity) {
		try {
			ClientDetailsEntity response = this.clientDetailsService.addProjectCodeDetails(clientDetailsEntity);
			return response;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return clientDetailsEntity;
	}

	//for anemoi
		@Get("/getProjectCode/{projectCode}")
		public ClientDetailsEntity getclientDetailsbyProjectCode(@PathVariable("projectCode") int projectCode) throws ClientDetailsControllerException {
			try {
				ClientDetailsEntity response = this.clientDetailsService.getClientDetailsByProjectCode(projectCode);
				return response;
			} catch (Exception e) {
				throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
						e.getMessage());
			}
		
		}
		
		
		//for pwc
		@Get("/getCode/{contractNumber}")
		public HttpResponse<String> getclientDetailsbyProjectCode(String contractNumber) throws ClientDetailsControllerException {
			try {
	            String apiUrl = "/ords/bosin/rest/ob/contract/" + contractNumber;
	            HttpResponse<String> response = httpClient.toBlocking().exchange(apiUrl, String.class);

	            return response;
			} catch (Exception e) {
				throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
						e.getMessage());
			}
		
		}	
	

	@Post("/add")
	public ClientDetailsEntity addClientDetails(@Body ClientDetailsEntity detailsEntity)  throws ClientDetailsControllerException{
		try 
		{
			ClientDetailsEntity response = this.clientDetailsService.addClientDetails(detailsEntity);
			return response;

		} catch (Exception e) {
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	
	}

	@Get("/list")
	public ArrayList<ClientDetailsEntity> getAllClientDetails() throws ClientDetailsControllerException {
		try {
			ArrayList<ClientDetailsEntity> responseList = this.clientDetailsService.getAllClientDetails();
			return responseList;
		} catch (Exception e) {
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	
	}
	
	@Get("getClientData/{clientId}")
	public ClientDetailsEntity getClientDataByclientId(@PathVariable("clientId") String clientId) throws ClientDetailsControllerException
	{
		try
		{
			ClientDetailsEntity response=this.clientDetailsService.getClientData(clientId);
			return response;
		}
		catch (Exception e) {
			
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}
	
	@Patch("updateClientDetails/{clientId}")
	public ClientDetailsEntity updateClientDetails(@PathVariable("clientId") String clientId,@Body ClientDetailsEntity clientDetailsEntity) throws ClientDetailsControllerException 
	{
		try {
		
			ClientDetailsEntity response=this.clientDetailsService.updateClientDetails(clientId,clientDetailsEntity);
			return response;
	
		}
		catch (Exception e) {
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}
	
	@Delete("deleteClient/{clientId}")
	public String deleteClientDetails(@PathVariable("clientId") String clientId,String createdBy) throws ClientDetailsControllerException
	{
		try
		{
			String response=this.clientDetailsService.deleteClientDetails(clientId ,createdBy);
			return response;
			
		}catch (Exception e) {
			
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}
	
	@Get("getClientDatabyClientName/{clientName}")
	public ClientDetailsEntity getClientDataByclientName(@PathVariable("clientName") String clientName) throws ClientDetailsControllerException
	{
		try
		{
			ClientDetailsEntity response=this.clientDetailsService.getClientDataByClientName(clientName);
			return response;
		}
		catch (Exception e) {
			
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}
}
