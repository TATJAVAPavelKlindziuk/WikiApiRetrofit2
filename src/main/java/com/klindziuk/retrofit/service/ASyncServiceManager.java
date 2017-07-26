package com.klindziuk.retrofit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.klindziuk.retrofit.model.WikiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class ASyncServiceManager  {
	private static final Logger logger = Logger.getLogger(SyncServiceManager.class);
	private static final String BASE_URL = "https://en.wikipedia.org/";
	private static final String RESPONSE_PARSE = "Parsing response - ";
	private static final String ERROR_RESPONSE_MESSAGE = "Cannot receive response.";
	private static final String IO_RESPONSE_ERROR_MESSAGE = "Error during reading data from ";
	private List<Map<String, String>> requestList;
	private List<WikiResponse> responseList;
								
	public ASyncServiceManager() {
		this.requestList = ServiceManagerHelper.initialize();
	}

	public List<WikiResponse> getResponseList() {
		return responseList;
	}

	public void sendRequest()  {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
				.build();
		WikiApiService service = retrofit.create(WikiApiService.class);
		final CountDownLatch latch = new CountDownLatch(1);
		responseList = new ArrayList<>();
		for (Map<String, String> paramMap : requestList) {
			Call<ResponseBody> call = service.getResponse(paramMap);
			call.enqueue(new Callback<ResponseBody>() {
				
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					logger.info(RESPONSE_PARSE + Thread.currentThread().getName());
					if (response.isSuccess()) {
					WikiResponse wikiResponse = null;
					try {
						wikiResponse = fillWikiResponse(response);
						responseList.add(wikiResponse);
						latch.countDown();
					} catch (IOException ioex) {
						logger.error(IO_RESPONSE_ERROR_MESSAGE + response + response.hashCode());
						ioex.printStackTrace();
					}
					logger.info(wikiResponse);
				  }
		    	}

				private WikiResponse fillWikiResponse(Response<ResponseBody> response) throws IOException {
					WikiResponse wikiresponse = new WikiResponse();
					wikiresponse.setCode(response.code());
				  //wikiresponse.setBody(response.body().string());
					// we receive a very huge string, so just take only size
					wikiresponse.setBody(String.valueOf(response.body().string().length()));
					wikiresponse.setMessage(response.message());
					wikiresponse.setHeaders(response.headers().toMultimap());
					return wikiresponse;
				}

				public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				logger.error(ERROR_RESPONSE_MESSAGE, throwable);
				 }
			});
			try {
				latch.await(5, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("FINISHED - " + Thread.currentThread().getName());
	}

	
}
