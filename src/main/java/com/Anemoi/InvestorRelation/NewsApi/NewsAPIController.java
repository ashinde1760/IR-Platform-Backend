package com.Anemoi.InvestorRelation.NewsApi;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/api")
public class NewsAPIController {
	
	@Inject
	ServiceInterfce si;
	
	
	
	@Post("/news")
	String newsAPI(@Body  NewsEntity ne) throws IOException
	{
		System.out.println("in controller.....");
		if (ne == null || ne.getFromDate() == null || ne.getFromDate().isEmpty()) {
            return "Invalid input";
        }
		String sanitizedContent = sanitizeInput(ne.toString());
		ne=parseFromDecryptedData(sanitizedContent);
		String response=si.newsAPI(ne);
		
		return response;
	}
	private String sanitizeInput(String input) {
        // Implement your input sanitization logic here
        // This is a basic example; you may need to use a library or implement more sophisticated logic
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
	public static NewsEntity parseFromDecryptedData(String decryptedData)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		NewsEntity user = objectMapper.readValue(decryptedData, NewsEntity.class);

		return user;
	}

}
