package com.Anemoi.InvestorRelation.RoleModel;

import java.sql.SQLException;
import java.util.List;

import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Controller("/investor/rolemodel")
public class RoleModelController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleModelController.class);

	@Inject
	private RoleModelService service;

	@Post("/add")
	public HttpResponse<RoleModelEntity> addRoleModelDetails(@Body RoleModelEntity rolemodelEntity)
			throws RoleModelControllerException {
		try {
			RoleModelEntity newrolemodel = this.service.createRoleModel(rolemodelEntity);

			return HttpResponse.status(HttpStatus.CREATED).body(newrolemodel);

		} catch (Exception e) {

			throw new RoleModelControllerException(ReadPropertiesFile.readResponseProperty("403"), e, 406,
					e.getMessage());
//			 return HttpResponse.status(HttpStatus.NOT_FOUND);
		}

	}

	@Get("/{id}")
	public HttpResponse<RoleModelEntity> getDataById(@PathVariable("id") String id)
			throws RoleModelControllerException {
		try {
			RoleModelEntity rolemodelEntity = this.service.getRoleModelById(id);
			return HttpResponse.status(HttpStatus.OK).body(rolemodelEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/getByRole/{role}")
	public HttpResponse<RoleModelEntity> getDataByRole(@PathVariable("role") String role)
			throws RoleModelControllerException {
		System.out.println(role + " !!!!!");
		try {
			RoleModelEntity rolemodelEntity = this.service.getRoleModelByRole(role);
			return HttpResponse.status(HttpStatus.OK).body(rolemodelEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RoleModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Get("/list")
	public List<RoleModelEntity> getDetails() throws SQLException, RoleModelControllerException {
		try {
			List<RoleModelEntity> shareholdercontact = this.service.getAllRoleModelDetails();
			 for (RoleModelEntity item : shareholdercontact) {
		            item.setRoleName(escapeHtml(item.getRoleName())); // Properly escape HTML
		        }
			return shareholdercontact;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RoleModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}
	 private static String escapeHtml(String input) {
	        // Using OWASP Java Encoder
	        return Encode.forHtmlContent(input);
	    }
	@Patch("/{id}")
	public HttpResponse<RoleModelEntity> updateRoleModel(@Body RoleModelEntity rolemodelEntity,
			@PathVariable("id") String id) throws RoleModelControllerException {
		try {
			RoleModelEntity updatedrolemodel = this.service.updateRoleModel(rolemodelEntity, id);
			return HttpResponse.status(HttpStatus.OK).body(updatedrolemodel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RoleModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

	@Delete("/{id}")
	public HttpResponse<RoleModelEntity> deleteRoleModelById(@PathVariable("id") String id)
			throws RoleModelControllerException {
		try {
			RoleModelEntity response = this.service.deleteRolemodel(id);
			return HttpResponse.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RoleModelControllerException(ReadPropertiesFile.readResponseProperty("101"), e, 400,
					e.getMessage());
		}

	}

}
