package com.Anemoi.InvestorRelation.NewsApi;

import java.io.IOException;



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
		String response=si.newsAPI(ne);
		
		return response;
	}

}
