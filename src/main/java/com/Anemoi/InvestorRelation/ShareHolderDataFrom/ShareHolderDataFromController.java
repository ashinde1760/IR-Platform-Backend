package com.Anemoi.InvestorRelation.ShareHolderDataFrom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.owasp.encoder.Encode;

import com.Anemoi.InvestorRelation.CashFlow.CashFlowControllerException;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
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
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Controller("/investor/shareholderdataform")
public class ShareHolderDataFromController {

	@Inject
	private ShareHoderDataFromService shareholderservice;
	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the number of threads as needed

	
	@Post("/add")
	public HttpResponse<ShareHolderDataFromEntity> addshareholderdataformDetails(
			@Body ShareHolderDataFromEntity shareholderEntity)
			throws SQLException, ShareHolderDataFormControllerException {
		try {
			ShareHolderDataFromEntity newshareholder = this.shareholderservice
					.CreateShareHoderDataForm(shareholderEntity);
			return HttpResponse.status(HttpStatus.OK).body(newshareholder);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Get("/{shareid}")
	public HttpResponse<ShareHolderDataFromEntity> getDataById(@PathVariable("shareid") String shareid)
			throws ShareHolderDataFormControllerException {
		try {
			ShareHolderDataFromEntity shareholderEntity = this.shareholderservice.getShareHolderDataFormById(shareid);
			return HttpResponse.status(HttpStatus.OK).body(shareholderEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Get("/list")
	public List<ShareHolderDataFromEntity> getDetails() throws SQLException, ShareHolderDataFormControllerException {
		try {
			List<ShareHolderDataFromEntity> shareholderData = this.shareholderservice.getShareHolderDataFormDetails();
			 for (ShareHolderDataFromEntity item : shareholderData) {
		            item.setClientName(escapeHtml(item.getClientName())); // Properly escape HTML
		        }
			return shareholderData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }
	@Patch("/{shareid}")
	public HttpResponse<ShareHolderDataFromEntity> updateshareholder(@Body ShareHolderDataFromEntity shareholderEntity,
			@PathVariable("shareid") String shareid) throws ShareHolderDataFormControllerException {
		try {
			ShareHolderDataFromEntity updatedcashflow = this.shareholderservice
					.updateShareHolderDataForm(shareholderEntity, shareid);
			return HttpResponse.status(HttpStatus.OK).body(updatedcashflow);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Delete("/{shareid}")
	public HttpResponse<ShareHolderDataFromEntity> deleteshareholder(@PathVariable("shareid") String shareid)
			throws ShareHolderDataFormControllerException {
		try {
			ShareHolderDataFromEntity response = this.shareholderservice.deleteShareHoderDataForm(shareid);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
//	@Async
//	@SuppressWarnings("unchecked")
//	@Post(uri = "/uploadShareholderDataExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
//	public void uploadExcelSheetshareHolderData(  @QueryValue String createdBy,
//            @Part("file") CompletedFileUpload file)
//			throws ShareHolderDataFormControllerException {
//		try {
//		
//			String response = this.shareholderservice.uploadShareHolderDataExcelSheet(createdBy,file);
////			return response;
//		} catch (Exception e) {
//			// TODO: handle exception
//			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
//					e.getMessage());
//		}
//	}
	
//	@SuppressWarnings("unchecked")
//	@Post(uri = "/uploadShareholderDataExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
//	public String uploadExcelSheetshareHolderData(  @QueryValue String createdBy,
//            @Part("file") CompletedFileUpload file)
//			throws ShareHolderDataFormControllerException {
//		try {
//		
//			String response = this.shareholderservice.uploadShareHolderDataExcelSheet(createdBy,file);
//			return response;
//		} catch (Exception e) {
//			// TODO: handle exception
//			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
//					e.getMessage());
//		}
//	}
	

	 @Post(uri = "/uploadShareholderDataExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	    public String uploadExcelSheetshareHolderData(@QueryValue String createdBy, @Part("file") CompletedFileUpload file) throws ShareHolderDataFormControllerException {
	        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
	            try {
	            	System.out.println("createdBy "+createdBy);
	                String response = this.shareholderservice.uploadShareHolderDataExcelSheet(createdBy, file);
	                // Optionally, process the response if needed
	                return response;
	            } catch (Exception e) {
	                // Handle exceptions
	                e.printStackTrace();
	                throw new RuntimeException("Error processing upload", e);
	            }
	        }, executorService);

	        future.exceptionally(ex -> {
	            // Handle exceptions occurred in the asynchronous task
	            ex.printStackTrace();
	            return null;
	        });

	        // Return a CompletableFuture<String>
	        return future.join(); // This will block until the future completes and return the response
	    }
	
//	@SuppressWarnings("unchecked")
//	@Post(uri = "/uploadShareholderDataExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
//	public Mono<String> uploadExcelSheetShareHolderData(
//	        @QueryValue String createdBy,
//	        @Part("file") CompletedFileUpload file) throws ShareHolderDataFormControllerException {
//	    return Mono.fromCallable(() -> {
//	        try {
//	            // Synchronous operation (e.g., calling a service method)
//	            String response = this.shareholderservice.uploadShareHolderDataExcelSheet(createdBy, file);
//	            return response;
//	        } catch (Exception e) {
//	            // Log the exception or handle it as needed
//	            // You might want to log the exception for debugging purposes
//	            e.printStackTrace();
//	            throw new RuntimeException("Error processing upload", e);
//	        }
//	    });
//	}

	
	@Post("/addMinorCode")
	public String addMinorCode(@Body MinorCodeEntity codeEntity)  throws ShareHolderDataFormControllerException
	{
		try
		{
			String response=this.shareholderservice.addMinorCode(codeEntity);
			return response;
		}
		catch (Exception e) {
			
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Get("/getMinoCodeList")
	public ArrayList<MinorCodeEntity> getMinorCodeList() throws ShareHolderDataFormControllerException
	{
		try
		{
			ArrayList<MinorCodeEntity> list=this.shareholderservice.getMinorCodeList();
			return list;
			
		}
		catch (Exception e) {
			
			throw new ShareHolderDataFormControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
}
