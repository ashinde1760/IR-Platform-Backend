package com.Anemoi.InvestorRelation.BalanceSheet;

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

@Controller("/investor/balanceSheet")
public class BalanceSheetController {

	@Inject
	private BalanceSheetService service;
	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	

	@Post("/add")
	public HttpResponse<BalanceSheetEntity> addBalanceSheetDetails(@Body BalanceSheetEntity balanceEntity)
			throws BalanceSheetControllerException {
		try {
			BalanceSheetEntity balnacesheet = this.service.createNewBalanceSheetForm(balanceEntity);
			return HttpResponse.status(HttpStatus.OK).body(balnacesheet);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Get("/{balanceid}")
	public HttpResponse<BalanceSheetEntity> getDataById(@PathVariable("balanceid") String balanceid)
			throws BalanceSheetControllerException {
		try {
			BalanceSheetEntity balanceEntity = this.service.getBalancesheetById(balanceid);
			return HttpResponse.status(HttpStatus.OK).body(balanceEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public List<BalanceSheetEntity> getDetails() throws BalanceSheetControllerException {
		try {
			List<BalanceSheetEntity> balancesheetData = this.service.getAllBalanceSheetDetails();
			 for (BalanceSheetEntity item : balancesheetData) {
		            item.setAlternativeName(escapeHtml(item.getAlternativeName())); // Properly escape HTML
		        }
			return balancesheetData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	private static String escapeHtml(String input) {
        // Using OWASP Java Encoder
        return Encode.forHtmlContent(input);
    }
	@Patch("/{balanceid}")
	public HttpResponse<BalanceSheetEntity> updaterBalanceSheet(@Body BalanceSheetEntity balancesheetEntity,
			@PathVariable("balanceid") String balanceid) throws BalanceSheetControllerException {
		try {
			BalanceSheetEntity balancesheet = this.service.updateBalanceSheetForm(balancesheetEntity, balanceid);
			return HttpResponse.status(HttpStatus.OK).body(balancesheet);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Delete("/{balanceid}")
	public HttpResponse<BalanceSheetEntity> deleteincomestatement(@PathVariable("balanceid") String balanceid)
			throws BalanceSheetControllerException {
		try {
			BalanceSheetEntity response = this.service.deleteBalanceSheet(balanceid);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Consumes(value = { "application/json" })
	@Produces(value = { "application/json" })
	@Post("/addobject")
	public ArrayList<BalanceSheetEntity> addObjectBalanceSheet(@Body ArrayList<BalanceSheetEntity> balanceSheetEntity)
			throws BalanceSheetControllerException {
		try {
			ArrayList<BalanceSheetEntity> entity = this.service.addObjectBalanceSheetEntity(balanceSheetEntity);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Post(uri = "/uploadExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public MutableHttpResponse<?> uploadBalanceSheetExcel(  @QueryValue String createdBy,
            @Part("file") CompletedFileUpload file)
			throws BalanceSheetControllerException, BalanceSheetDaoException {
		try {
		
			ArrayList<BalanceSheetEntity> response = this.service.uploadBalanceSheetExcel(createdBy,file);
			if(response.isEmpty())
			{
				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "Balance sheet excel upload successfully");
				return HttpResponse.status(HttpStatus.OK).body(reposneJSON.toString());
			}else
			{
				return HttpResponse.status(HttpStatus.FOUND).body(response);
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new BalanceSheetControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

}
