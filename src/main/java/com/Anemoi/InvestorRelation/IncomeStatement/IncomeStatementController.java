package com.Anemoi.InvestorRelation.IncomeStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.owasp.encoder.Encode;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Inject;

@Controller("/investor/incomestatement")
public class IncomeStatementController {
	
	@Inject
	private IncomeStatementService service;
	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";

	@Post("/add")
	public HttpResponse<IncomeStatementEntity> addIncomeStatementDetails(
			@Body IncomeStatementEntity incomestatementEntity) throws SQLException, IncomeStatementControllerException {
		try {
			IncomeStatementEntity incomestatement = this.service.createIncomeStatement(incomestatementEntity);
			return HttpResponse.status(HttpStatus.OK).body(incomestatement);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Get("/{incomeid}")
	public HttpResponse<IncomeStatementEntity> getDataById(@PathVariable("incomeid") String incomeid)
			throws IncomeStatementControllerException {
		try {
			IncomeStatementEntity incomestatement = this.service.getIncomeStatementById(incomeid);
			return HttpResponse.status(HttpStatus.OK).body(incomestatement);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public List<IncomeStatementEntity> getDetails() throws SQLException, IncomeStatementControllerException {
		try {
			List<IncomeStatementEntity> incomestatement = this.service.getAllIncomeStatementDetails();
			 for (IncomeStatementEntity item : incomestatement) {
		            item.setAlternativeName(escapeHtml(item.getAlternativeName())); // Properly escape HTML
		        }
			return incomestatement;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }
	@Patch("/{incomeid}")
	public HttpResponse<IncomeStatementEntity> updaterIncomeStatement(@Body IncomeStatementEntity incomestatementEntity,
			@PathVariable("incomeid") String incomeid) throws IncomeStatementControllerException {
		try {
			IncomeStatementEntity incomestatement = this.service.updateIncomeStatement(incomestatementEntity, incomeid);
			return HttpResponse.status(HttpStatus.OK).body(incomestatement);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Delete("/{incomeid}")
	public HttpResponse<IncomeStatementEntity> deleteIncomeStatement(@PathVariable("incomeid") String incomeid)
			throws IncomeStatementControllerException {
		try {
			IncomeStatementEntity response = this.service.deleteIncomeStatement(incomeid);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Consumes(value = { "application/json" })
	@Produces(value = { "application/json" })
	@Post("/addobject")
	public ArrayList<IncomeStatementEntity> addIncomeStatementObject(
			@Body ArrayList<IncomeStatementEntity> incomeentity) throws IncomeStatementControllerException {
		try {
			ArrayList<IncomeStatementEntity> entity = this.service.addIncomestatmentObject(incomeentity);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Post(uri = "/uploadExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public MutableHttpResponse<?> uploadExcelSheetinIncomeTable(  @QueryValue String createdBy,
            @Part("file") CompletedFileUpload file)
			throws IncomeStatementControllerException {
		try {
			ArrayList<IncomeStatementEntity> response = this.service.uploadExcelSheetIncomeTable(createdBy,file);
			if(response.isEmpty())
			{
				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "Income statement excel sheet upload successfully");
				return HttpResponse.status(HttpStatus.OK).body(reposneJSON.toString());
			}else
			{
				return HttpResponse.status(HttpStatus.FOUND).body(response);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new IncomeStatementControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

}
