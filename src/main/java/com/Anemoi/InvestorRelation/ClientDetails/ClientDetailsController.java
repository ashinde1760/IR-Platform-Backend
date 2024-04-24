package com.Anemoi.InvestorRelation.ClientDetails;

import java.util.ArrayList;

import com.Anemoi.InvestorRelation.AnalystDetails.AnalystDetailsControllerException;
import com.Anemoi.InvestorRelation.ClientLineItem.ClientLineItemControllerException;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
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

	@Inject
	HttpClient httpClient;


//	    public ClientDetailsController(@Client("https://api-staging.pwcinternal.com:7443/precisionplusbosin/ob/v1") HttpClient httpClient) {
//	        this.httpClient = httpClient;
//	    }

//	    public ClientDetailsController1(@Client("https://api.pwcinternal.com:7443/precisionplusbosin/ob/v1/") HttpClient httpClient) {
//	        this.httpClient = httpClient;
//	    }

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

	// for anemoi
	@Get("/getProjectCode/{projectCode}")
	public ClientDetailsEntity getclientDetailsbyProjectCode(@PathVariable("projectCode") int projectCode)
			throws ClientDetailsControllerException {
		try {
			ClientDetailsEntity response = this.clientDetailsService.getClientDetailsByProjectCode(projectCode);
			return response;
		} catch (Exception e) {
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}

	}

	// for pwc
	@Get("/getCode/{contractNumber}")
	public HttpResponse<String> getCode(String contractNumber) throws ClientDetailsControllerException {
		// Set headers
		try {

			final String apiUrl = ReadPropertiesFile.readOCrKey("apiUrl");
			final String apiKey = ReadPropertiesFile.readOCrKey("apikey");
			final String apiSecret = ReadPropertiesFile.readOCrKey("apikeysecret");
			final String proxyAuthorization = ReadPropertiesFile.readOCrKey("Proxy-Authorization");

			System.out.println(apiUrl + "\n" + apiKey + "\n" + apiSecret + "\n" + proxyAuthorization);

			HttpRequest<?> request = HttpRequest.GET(apiUrl + contractNumber).header("ApiKey", apiKey)
					.header("ApiKeySecret", apiSecret).header("ProxyAuthorization", proxyAuthorization)
					.header("Accept", "application/json");

			// Make GET request
			HttpResponse<String> response = httpClient.toBlocking().exchange(request, String.class);

			return response;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}

	@Post("/add")
	public ClientDetailsEntity addClientDetails(@Body ClientDetailsEntity detailsEntity)
			throws ClientDetailsControllerException {
		try {
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
	public ClientDetailsEntity getClientDataByclientId(@PathVariable("clientId") String clientId)
			throws ClientDetailsControllerException {
		try {
			ClientDetailsEntity response = this.clientDetailsService.getClientData(clientId);
			return response;
		} catch (Exception e) {

			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}

	@Patch("updateClientDetails/{clientId}")
	public ClientDetailsEntity updateClientDetails(@PathVariable("clientId") String clientId,
			@Body ClientDetailsEntity clientDetailsEntity) throws ClientDetailsControllerException {
		try {

			ClientDetailsEntity response = this.clientDetailsService.updateClientDetails(clientId, clientDetailsEntity);
			return response;

		} catch (Exception e) {
			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}

	@Delete("deleteClient/{clientId}")
	public String deleteClientDetails(@PathVariable("clientId") String clientId, String createdBy)
			throws ClientDetailsControllerException {
		try {
			String response = this.clientDetailsService.deleteClientDetails(clientId, createdBy);
			return response;

		} catch (Exception e) {

			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}

	@Get("getClientDatabyClientName/{clientName}")
	public ClientDetailsEntity getClientDataByclientName(@PathVariable("clientName") String clientName)
			throws ClientDetailsControllerException {
		try {
			ClientDetailsEntity response = this.clientDetailsService.getClientDataByClientName(clientName);
			return response;
		} catch (Exception e) {

			throw new ClientDetailsControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 406,
					e.getMessage());
		}
	}
}
