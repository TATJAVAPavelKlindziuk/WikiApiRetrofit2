package com.klindziuk.retrofit.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.klindziuk.retrofit.model.WikiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class WikiApiServiceManager  {
	private static final Logger logger = LogManager.getRootLogger();
	private static final String BASE_URL = "https://en.wikipedia.org/";
	private static final String RESPONSE_PARSE = "Parsing response - ";
	private static final String ERROR_RESPONSE_MESSAGE = "Cannot receive response.";
	private static final String IO_RESPONSE_ERROR_MESSAGE = "Error during reading data from ";
	private List<Map<String, String>> requestList;
				
		public void sendRequests() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
				.build();
		WikiApiService service = retrofit.create(WikiApiService.class);
		for (Map<String, String> paramMap : requestList) {
			Call<ResponseBody> call = service.getResponse(paramMap);
			call.enqueue(new Callback<ResponseBody>() {
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					logger.info(RESPONSE_PARSE + response + response.hashCode());
					if (response.isSuccess()) {
					WikiResponse wikiresponse = null;
					try {
						wikiresponse = fillWikiResponse(response);
					} catch (IOException ioex) {
						logger.error(IO_RESPONSE_ERROR_MESSAGE + response + response.hashCode());
						ioex.printStackTrace();
					}
					logger.info(wikiresponse);
				  }
				}

				private WikiResponse fillWikiResponse(Response<ResponseBody> response) throws IOException {
					WikiResponse wikiresponse = new WikiResponse();
					wikiresponse.setCode(response.code());
				  //wikiresponse.setBody(response.body().string());
					//we get a very huge string, so just take number of symbols
					wikiresponse.setBody(String.valueOf(response.body().string().length()));
					wikiresponse.setMessage(response.message());
					wikiresponse.setHeaders(response.headers().toMultimap());
					return wikiresponse;
				}

				public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				logger.error(ERROR_RESPONSE_MESSAGE, throwable);
				 }
			});
		}
	}
	 
	public void setRequestList(List<Map<String, String>> requestList) {
		this.requestList = requestList;
	}
}
