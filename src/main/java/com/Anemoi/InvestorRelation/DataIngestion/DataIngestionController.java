package com.Anemoi.InvestorRelation.DataIngestion;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.json.simple.JSONObject;

import com.Anemoi.InvestorRelation.Audithistory.AuditHistoryEntity;
import com.Anemoi.InvestorRelation.Configuration.InvestorDatabaseUtill;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.EmptyResponseException;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;
import org.json.simple.*;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Controller("/investor/dataIngestion")
public class DataIngestionController {

	public static final String CONTENT_DISPOSITION_VALUE = "attachment;filename=";
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the number of threads as needed

	@Inject
	DataIngestionService dataingestionService;

//	private boolean status = false;
	
	public DataIngestionController(DataIngestionService dataingestionService) {
		super();
		this.dataingestionService = dataingestionService;
	}
	
	@Post(value = "/addMultipleFiles", consumes = MediaType.MULTIPART_FORM_DATA)
	public CompletableFuture<List<NonProcessFilesDetails>> MultipleFiles(
			@Body NonProcessFilesDetails details, Publisher<CompletedFileUpload> data)
			throws DataIngestionControllerException {
		
		ArrayList<NonProcessFilesDetails> modelList = new ArrayList<>();
		DataIngestionService dataingestionService = this.dataingestionService;

		List<Map<String, Object>> results = new ArrayList<>();

		CompletableFuture<List<NonProcessFilesDetails>> future = new CompletableFuture<>();

		Flux.from(data).subscribeOn(Schedulers.boundedElastic()).subscribe(new Subscriber<CompletedFileUpload>() {
			private Subscription subscription;
			private boolean status=false;
			@Override
			public void onSubscribe(Subscription subscription) {
				this.subscription = subscription;
				this.subscription.request(1);
			}

			@Override
			public void onNext(CompletedFileUpload upload) {
				Map<String, Object> fileData = new LinkedHashMap<>();
				String fileName = upload.getFilename();
				fileData.put("name", upload.getFilename());
				try {
					byte[] bytes = upload.getBytes();
					String contentType = upload.getContentType().toString();
					fileData.put("contentType", contentType);
					NonProcessFilesDetails m = new NonProcessFilesDetails();
					m.setFileName(fileName);
					m.setFileType(contentType);
					m.setFileData(bytes);
					m.setClient(details.getClient());
					m.setClientId(details.getClientId());
					m.setCreatedBy(details.getCreatedBy());
					Date d=new Date();
					m.setCreatedOn(d.getTime());
					modelList.add(m);

				} catch (IOException e) {
					e.printStackTrace();
				}
				results.add(fileData);
				subscription.request(1);
			}

			@Override
			public void onError(Throwable t) {
				future.completeExceptionally(t);
			}
//			ArrayList<NonProcessFilesDetails> clientName = new ArrayList<>();
			
			@Override
			public void onComplete() {
				Map<String, Object> body = new LinkedHashMap<>();
				body.put("files", results);
				try {
					ArrayList<NonProcessFilesDetails> response = dataingestionService.saveMultipleFiles(modelList,
							details);
					if (response.isEmpty()) 
					{
						NonProcessFilesDetails d=new NonProcessFilesDetails();
						d.setClientId(details.getClientId());
						d.setClient(details.getClient());
						d.setCreatedBy(details.getCreatedBy());
						response.add(d);
						future.complete(response);
					} 
					else {
			
						for (int i = 0; i <= response.size(); i++) {
							
							future.complete(response);
				
						}
					
					}

				} catch (DataIngestionServiceException | ClassNotFoundException e) {
					future.completeExceptionally(e);
					
				}
			}
		});
	
		return future;
	}

	

	// **
	@Get("/getnpFileidsByClientName/{client}")
	public ArrayList<NonProcessFilesDetails> getFileIdsByClient(@PathVariable("client") String client)
			throws DataIngestionControllerException {
		try {
			ArrayList<NonProcessFilesDetails> reponse = this.dataingestionService.getFileIdsByClientName(client);
			return reponse;
		} catch (Exception e) {

			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	// **
	@Post("/previewForFiles/{npFileId}")
	public MutableHttpResponse<StreamedFile> getPreviewForfiles(@PathVariable("npFileId") int npFileId,
			@Body DataIngestionModel ingestionModel) throws DataIngestionControllerException {
		try {
			HttpResponse<DataIngestionModel> modelResponse = this.dataingestionService
					.getPreviewForNonProcessFiles(npFileId, ingestionModel);
			DataIngestionModel model = modelResponse.body();
			byte[] byteArray = model.getFileData();
			InputStream inputStream = new ByteArrayInputStream(byteArray);
			StreamedFile streamedFile = new StreamedFile(inputStream, MediaType.APPLICATION_PDF_TYPE);

			return HttpResponse.ok().contentType(MediaType.APPLICATION_PDF_TYPE)
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + model.getFileName())
					.body(streamedFile);
		} catch (Exception e) {

			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	// **
	@Post("/savePreviewFileDetails/{npFileId}")
	public HttpResponse<DataIngestionModel> savePreviewFileDetails(@PathVariable("npFileId") int npFileId,
			@Body DataIngestionModel ingestionModel) throws DataIngestionControllerException {
		 CompletableFuture<HttpResponse<DataIngestionModel>> future = CompletableFuture.supplyAsync(() -> {
		try {
			HttpResponse<DataIngestionModel> modelResponse = this.dataingestionService
					.savePreviewFileDetailsInTable(npFileId, ingestionModel);
			return modelResponse;
		 } catch (Exception e) {
             // Handle exceptions
//			 throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
//						e.getMessage());
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
     return future.join();
	}

	@Get("/getdataingestionFileData")
	public ArrayList<DataIngestionModel> getDataIngestionFileDetails() throws DataIngestionControllerException {
		System.out.println("check1");
		try {
			ArrayList<DataIngestionModel> list = this.dataingestionService.getfileDetails();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("/getAllNonProcessingFiles")
	public ArrayList<NonProcessFilesDetails> getNonProcessFiles() throws DataIngestionControllerException
	{
		try
		{
			ArrayList<NonProcessFilesDetails> responseList=this.dataingestionService.getNonProcessingFiles();
			return responseList;
		}
		catch (Exception e) {
			
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	
	@Get("/getfileData/{fileId}")
	public DataIngestionModel getfileDatabyFileId(@PathVariable("fileId") long fileId)
			throws DataIngestionControllerException {
		try {
			DataIngestionModel model = this.dataingestionService.getfileDataByFileId(fileId);
			return model;

		} catch (Exception e) {

			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	// overwrite file
	@Post(uri = "/overwritefile")
	public String overWriteFile(NonProcessFilesDetails file)
			throws DataIngestionControllerException {
		try {
			String response = this.dataingestionService.overwriteFile(file);
			return response;

		} catch (Exception e) {

			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
	@Post("/overWriteMultipleFiles")
	public String overWriteMultipleFiles(@Body ArrayList<NonProcessFilesDetails> nonProcessFilesList) throws DataIngestionControllerException
	{
		try
		{
		
			String response=this.dataingestionService.overWriteMultipleFiles(nonProcessFilesList);
			return response;
		}
		catch (Exception e) {
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

//	// *
//	@Post("/extractFile/{fileId}")
//	public DataIngestionModel extractFileByfileId(@PathVariable("fileId") long fileId)
//			throws DataIngestionControllerException {
//		try {
//
//			DataIngestionModel response = this.dataingestionService.extractfilebyfileId(fileId);
//			return response;
//		} catch (Exception e) {
//
//			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
//					e.getMessage());
//		}
//	}

	@Get("/getTablelistDataByTableId/{tableId}")
	public TableList getTablelistDataByTableId(@PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {
			TableList list = this.dataingestionService.getTablelistData(tableId);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("/file/{fileId}")
	public ArrayList<TableList> getTableId(@PathVariable("fileId") long fileId)
			throws DataIngestionControllerException {

		try {
			ArrayList<TableList> tableid = this.dataingestionService.getTableIdByFileId(fileId);
			return tableid;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	// get the mapping table details for analyst name and tableid
	@Get("table/{analystName}/{tableId}")
	public ArrayList<DataIngestionTableModel> getDataIngestionTableDetails(
			@PathVariable("analystName") String analystName, @PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {

			ArrayList<DataIngestionTableModel> tableData = this.dataingestionService
					.getDataIngestionTableDetails(analystName, tableId);
			return tableData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	// get the mapping table details for client name and tableid
	@Get("tableDetails/{clientName}/{tableId}")
	public ArrayList<DataIngestionTableModel> getDataIngestionTableDetailsByClientName(
			@PathVariable("clientName") String clientName, @PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {

			ArrayList<DataIngestionTableModel> tableData = this.dataingestionService
					.getDataIngestionTableDetailsByClientName(clientName, tableId);
			return tableData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Delete("/deleteDataByTableId/{tableId}")
	public String deleteTableDataByTableId(@PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {
			String response = this.dataingestionService.deleteTableDatabyTableID(tableId);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("/getfileIdBytableId/{tableId}")
	public DataIngestionModel getFileIdBytableId(@PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {
			DataIngestionModel model = this.dataingestionService.getfileIdByTableId(tableId);
			return model;

		} catch (Exception e) {

			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Get("/getTableDataByFieldId/{field_Id}")
	public DataIngestionTableModel getTableDataByFieldId(@PathVariable("field_Id") long field_Id)
			throws DataIngestionControllerException {
		try {
			DataIngestionTableModel model = this.dataingestionService.getTableDataByFieldId(field_Id);
			return model;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Delete("/deleteTableDataByfieldId/{field_Id}")
	public String deleteTableDataByfieldId(@PathVariable("field_Id") long field_Id)
			throws DataIngestionControllerException {
		try {
			System.out.println("check1");
			String response = this.dataingestionService.deleteTableDataByFieldId(field_Id);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Delete("/deletemultiplefield_id")
	public HttpResponse<String> deleteMultipleFiledId(@Body FiledData filedData)
			throws DataIngestionControllerException {
		try {

			String resonse = this.dataingestionService.deleteMultipleField(filedData);
			return HttpResponse.status(HttpStatus.OK).body(resonse);
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Patch("/updateTableName/{tableId}")
	public TableList updateTableNameByTableId(TableList tabledata, @PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {
			TableList model = this.dataingestionService.updateTableNameByTableId(tabledata, tableId);
			return model;

		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Patch("/update/{tableId}")
	public ArrayList<DataIngestionTableModel> updateDataIngestionTableData(
			@Body ArrayList<DataIngestionTableModel> dataIngestionTableData, @PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {

		try {
			ArrayList<DataIngestionTableModel> response = this.dataingestionService
					.updateeDataIngestionTabledata(dataIngestionTableData, tableId);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/downloadTableData/{tableId}")
	public MutableHttpResponse<byte[]> downloadTableDataByTableId(@PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {

		try {
			DataIngestionModel model = this.dataingestionService.downloadTableDataByTableId(tableId);
			return HttpResponse.ok().body(model.getFileData()).header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
					.header(HttpHeaders.CONTENT_DISPOSITION,
							DataIngestionController.CONTENT_DISPOSITION_VALUE + model.getFileName() + ".xlsx");

		} catch (Exception e) {
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Patch(uri = "/uploadExcelSheet/{tableId}", consumes = { MediaType.APPLICATION_JSON,
			MediaType.MULTIPART_FORM_DATA })
	public HttpResponse<String> uploadExcelsheetByTableId(@PathVariable long tableId, CompletedFileUpload file) throws DataIngestionControllerException {
		try {
			System.out.println("check1");
			String response = this.dataingestionService.uploadExcelSheetBytableId(file, tableId);
			return HttpResponse.status(HttpStatus.OK).body(response);
		}
		catch (Exception e) {
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	

	}

	@Patch("/mergeTable/{tableId}")
	public TableList mergeTableByableId(@Body TableList tableList, @PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {
			TableList tableData = this.dataingestionService.mergeTableByTableId(tableList, tableId);
			return tableData;
		} catch (Exception e) {

			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Post("/splitTable/{fileId}")
	public HttpResponse<String> splitTableDetails(@PathVariable("fileId") long fileId,
			@Body ArrayList<DataIngestionTableModel> tableDetails) throws DataIngestionControllerException {
		try {
			String response = this.dataingestionService.splitTableDetails(fileId, tableDetails);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Patch("leftShift")
	public ArrayList<DataIngestionTableModel> leftShiftDataByfieldId(@Body ArrayList<DataIngestionTableModel> tableData)
			throws DataIngestionControllerException {
		try {
			ArrayList<DataIngestionTableModel> response = this.dataingestionService
					.leftShiftTableDataByfieldId(tableData);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Patch("rightShift")
	public ArrayList<DataIngestionTableModel> rightShiftDataByFieldId(
			@Body ArrayList<DataIngestionTableModel> tableData) throws DataIngestionControllerException {
		try {

			ArrayList<DataIngestionTableModel> response = this.dataingestionService.rightShiftDataByFieldId(tableData);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

/////// Data Ingestion Mapping table API get preview
	@Post("/getpreviewDataIngestionMappingTableDetails")
	public ArrayList<DataIngestionMappingModel> getPreviewDataIngestionMappingDetails(
			@Body ArrayList<DataIngestionMappingModel> dataIngestionMappingTable)
			throws DataIngestionControllerException {
		try {

			System.out.println("call1");
			ArrayList<DataIngestionMappingModel> mappingTable = this.dataingestionService
					.getPreviewDataIngestionMappingTable(dataIngestionMappingTable);
			return mappingTable;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}
	
/////// Data Ingestion Mapping table API save table
	@Post("/addDataIngestionMappingTableDetails")
	public ArrayList<DataIngestionMappingModel> addDataIngestionMappingDetails(
			@Body ArrayList<DataIngestionMappingModel> dataIngestionMappingTable)
			throws DataIngestionControllerException {
		try {
		
			ArrayList<DataIngestionMappingModel> mappingTable = this.dataingestionService
					.addDataIngestionMappingTable(dataIngestionMappingTable);
			
			return mappingTable;
		} catch (Exception e) {
			// TODO: handle exception
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("/getkeyword")
	public ArrayList<KeywordList> getKeyword() {
		ArrayList<KeywordList> list = this.dataingestionService.getKeyword();
		return list;
	}

	@Get("/getYearlistBytableId/{tableId}")
	public List<JSONObject> discoverDates(@PathVariable("tableId") long tableId)
			throws DataIngestionControllerException {
		try {
			System.out.println("check1");
			List<JSONObject> tabledates = this.dataingestionService.discoversDates(tableId);
			return tabledates;

		} catch (Exception e) {
			throw new DataIngestionControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

}
