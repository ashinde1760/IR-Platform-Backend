package com.Anemoi.Resource;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Inject;

@Controller("/media")
public class ResourceController {

	@Inject
	private ResourceSerice resourceSerice;

	@Get(produces = MediaType.APPLICATION_OCTET_STREAM, uri = "/resource")
	public HttpResponse<StreamedFile> getResource(@QueryValue(value = "fileType") String fileType) {

		Media media = resourceSerice.getAnalystDetailsFile(fileType);

		StreamedFile streamedFile = new StreamedFile(media.getMediaInputStream(),
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		System.out.println("returning the data");
		return HttpResponse.ok(streamedFile).header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + media.getMediaName());

	}

}
