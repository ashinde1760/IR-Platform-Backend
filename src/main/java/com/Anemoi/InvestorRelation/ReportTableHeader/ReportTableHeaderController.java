package com.Anemoi.InvestorRelation.ReportTableHeader;

import java.util.ArrayList;

import com.Anemoi.InvestorRelation.BalanceSheet.BalanceSheetControllerException;
import com.Anemoi.InvestorRelation.Configuration.ReadPropertiesFile;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/investor/reportTableHeader")
public class ReportTableHeaderController {

	@Inject
	ReportTableHeaderService reportTableHeaderService;

	@Post("/add")
	public ReportTableHeaderEntity addReportTableHeaderDetails(@Body ReportTableHeaderEntity entity)
			throws ReportTableHeaderControllerException

	{
		try {
			ReportTableHeaderEntity headerEntity = this.reportTableHeaderService.addReportTableHeaderDetails(entity);
			return headerEntity;
		} catch (Exception e) {
			throw new ReportTableHeaderControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Post("/addObject")
	public ArrayList<ReportTableHeaderEntity> addMultipleObjectTableHeader(
			@Body ArrayList<ReportTableHeaderEntity> entityObject) throws ReportTableHeaderControllerException {
		try {
			ArrayList<ReportTableHeaderEntity> reponseList = this.reportTableHeaderService
					.addMultipleObject(entityObject);
			return reponseList;
		} catch (Exception e) {

			throw new ReportTableHeaderControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Get("/list")
	public ArrayList<ReportTableHeaderEntity> getTableHeaderDetails() throws ReportTableHeaderControllerException {
		try {
			ArrayList<ReportTableHeaderEntity> response = this.reportTableHeaderService.gettableHeaderDetails();
			return response;
		} catch (Exception e) {

			throw new ReportTableHeaderControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Get("/{tableHeaderId}")
	public ReportTableHeaderEntity gettableHeaderDetailsByid(@PathVariable("tableHeaderId") String tableHeaderId)
			throws ReportTableHeaderControllerException {
		try {
			ReportTableHeaderEntity response = this.reportTableHeaderService.getTableHeaderDetailsByid(tableHeaderId);
			return response;
		} catch (Exception e) {
			throw new ReportTableHeaderControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}
	}

	@Delete("/{tableHeaderId}")
	public String deleteTableHeaderDetails(@PathVariable("tableHeaderId") String tableHeaderId)
			throws ReportTableHeaderControllerException {
		try {
			String response = this.reportTableHeaderService.deleteTableHeaderDetails(tableHeaderId);
			return response;
		} catch (Exception e) {

			throw new ReportTableHeaderControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}

	@Patch("/update/{tableHeaderId}")
	public String updateTableHeaderDetails(@Body ReportTableHeaderEntity entity,
			@PathVariable("tableHeaderId") String tableHeaderId) throws ReportTableHeaderControllerException {
		try {
			String response = this.reportTableHeaderService.updateTableHeaderDetails(entity, tableHeaderId);
			return response;
		} catch (Exception e) {

			throw new ReportTableHeaderControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());

		}
	}
}
