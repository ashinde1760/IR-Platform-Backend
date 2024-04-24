package com.Anemoi.InvestorRelation.FinancialRatio;

import java.util.List;

import org.owasp.encoder.Encode;

import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/investor/financialratio")
public class FinancialRatioController {

	@Inject
	private FinancialRatioService service;

	@Post("/add")
	public HttpResponse<FinancialRatioEntity> addFinancialRatioDetails(@Body FinancialRatioEntity financialratioEntity)
			throws FinancialRatioControllerException {
		try {
			FinancialRatioEntity newfinancialratio = this.service.CreateFinancialRatio(financialratioEntity);
			return HttpResponse.status(HttpStatus.OK).body(newfinancialratio);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new FinancialRatioControllerException(ReadPropertiesFile.readResponseProperty("403"), e, 406,
					e.getMessage());

		}

	}

	@Get("/{financialid}")
	public HttpResponse<FinancialRatioEntity> getDataById(@PathVariable("financialid") String financialid)
			throws FinancialRatioControllerException {
		try {
			FinancialRatioEntity financialratioEntity = this.service.getFinancialRatioById(financialid);
			return HttpResponse.status(HttpStatus.OK).body(financialratioEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinancialRatioControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public List<FinancialRatioEntity> getDetails() throws FinancialRatioControllerException {
		try {
			List<FinancialRatioEntity> cashflowData = this.service.getAllFinancialRatioDetails();
			 for (FinancialRatioEntity item : cashflowData) {
		            item.setClientName(escapeHtml(item.getClientName())); // Properly escape HTML
		        }
			return cashflowData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new FinancialRatioControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }

	@Delete("/{financialid}")
	public HttpResponse<FinancialRatioEntity> deleteUser(@PathVariable("financialid") String financialid)
			throws FinancialRatioControllerException {
		try {
			FinancialRatioEntity response = this.service.deleteFinancialRatio(financialid);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new FinancialRatioControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

}
