package com.klindziuk.retrofit.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.klindziuk.retrofit.model.WikiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class WikiApiServiceManager  {
	private static final Logger logger = LogManager.getRootLogger();
	private static final String BASE_URL = "https://en.wikipedia.org/";
	private static final String RESPONSE_PARSE = "Parsing response - ";
	private static final String ERROR_MESSAGE = "Cannot receive response.";
	private final CountDownLatch latch = new CountDownLatch(1);
	private List<Map<String, String>> requestList;
				
		public void sendRequests() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
				.build();
		WikiApiService service = retrofit.create(WikiApiService.class);
		for (Map<String, String> paramMap : requestList) {
			Call<WikiResponse> call = service.getResponse(paramMap);
			call.enqueue(new Callback<WikiResponse>() {
				public void onResponse(Call<WikiResponse> call, Response<WikiResponse> response) {
					logger.info(RESPONSE_PARSE + response + response.hashCode());
					if (response.isSuccess()) {
					WikiResponse wikiresponse = fillWikiResponse(response);
					logger.info(wikiresponse);
				  }
				}

				private WikiResponse fillWikiResponse(Response<WikiResponse> response) {
					WikiResponse wikiresponse = new WikiResponse();
					wikiresponse.setCode(response.code());
					wikiresponse.setBody(response.body().getBody());
					wikiresponse.setMessage(response.message());
					wikiresponse.setHeaders(response.headers().toMultimap());
					return wikiresponse;
				}

				public void onFailure(Call<WikiResponse> call, Throwable throwable) {
				logger.error(ERROR_MESSAGE, throwable);
				latch.countDown();
			  }
			});
		}
	}
	 
	public void setRequestList(List<Map<String, String>> requestList) {
		this.requestList = requestList;
	}
}
