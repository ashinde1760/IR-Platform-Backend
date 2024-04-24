package com.Anemoi.InvestorRelation.CashFlow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.owasp.encoder.Encode;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;
import com.Anemoi.InvestorRelation.IncomeStatement.IncomeStatementControllerException;

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

@Controller("/investor/cashFlow")
public class CashFlowController {

	@Inject
	private CashFlowService service;

	
	private static final Object STATUS = "status";
	private static final Object SUCCESS = "success";
	private static final Object MSG = "msg";
	
	@Post("/add")
	public HttpResponse<CashFlowEntity> addCashFlowDetails(@Body CashFlowEntity cashFlowEntity)
			throws CashFlowControllerException {
		try {
			CashFlowEntity newCashFlow = this.service.createCashFlow(cashFlowEntity);
			return HttpResponse.status(HttpStatus.OK).body(newCashFlow);
		} catch (Exception e) {

			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Get("/{cashid}")
	public HttpResponse<CashFlowEntity> getDataById(@PathVariable("cashid") String cashid)
			throws CashFlowControllerException {
		try {
			CashFlowEntity cashFlowEntity = this.service.getCashFlowById(cashid);
			return HttpResponse.status(HttpStatus.OK).body(cashFlowEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@Get("/list")
	public List<CashFlowEntity> getDetails() throws CashFlowControllerException {
		try {
			List<CashFlowEntity> cashflowData = this.service.getAllCashFlowDetails();
			 for (CashFlowEntity item : cashflowData) {
		            item.setAlternativeName(escapeHtml(item.getAlternativeName())); // Properly escape HTML
		        }
			return cashflowData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }
	@Patch("/{cashid}")
	public HttpResponse<CashFlowEntity> updateCashFlow(@Body CashFlowEntity cashflow,
			@PathVariable("cashid") String cashid) throws CashFlowControllerException {
		try {
			CashFlowEntity updatedcashflow = this.service.updateCashFlow(cashflow, cashid);
			return HttpResponse.status(HttpStatus.OK).body(updatedcashflow);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Delete("/{cashid}")
	public HttpResponse<CashFlowEntity> deleteCashFLow(@PathVariable("cashid") String cashid)
			throws CashFlowControllerException {
		try {
			CashFlowEntity response = this.service.deleteCashFlow(cashid);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Post("/addObject")
	public ArrayList<CashFlowEntity> addCashFlowObject(@Body ArrayList<CashFlowEntity> cashFlowEntity)
			throws CashFlowControllerException {
		try {

			ArrayList<CashFlowEntity> entity = this.service.addCashFlowObject(cashFlowEntity);
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Post(uri = "/uploadCashFlowExcelSheet", consumes = { MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public MutableHttpResponse<?> uploadExcelSheetinIncomeTable(  @QueryValue String createdBy,
            @Part("file") CompletedFileUpload file)
			throws CashFlowControllerException {
		try {
			System.out.println("check1");
			ArrayList<CashFlowEntity> response = this.service.uploadCashFlowExcelSheet(createdBy,file);
			if(response.isEmpty())
			{
				JSONObject reposneJSON = new JSONObject();
				reposneJSON.put(STATUS, SUCCESS);
				reposneJSON.put(MSG, "Cash flow excel sheet upload successfully");
				return HttpResponse.status(HttpStatus.OK).body(reposneJSON.toString());
			}else
			{
				return HttpResponse.status(HttpStatus.FOUND).body(response);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new CashFlowControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

}
