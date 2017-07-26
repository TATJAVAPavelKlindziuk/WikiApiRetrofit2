package com.klindziuk.retrofit.service.async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.klindziuk.retrofit.model.WikiResponse;
import com.klindziuk.retrofit.service.ServiceManagerHelper;
import com.klindziuk.retrofit.service.WikiApiService;
import com.klindziuk.retrofit.service.sync.SyncServiceManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class ASyncServiceManager implements Runnable {
	private static final Logger logger = Logger.getLogger(SyncServiceManager.class);
	private static final String BASE_URL = "https://en.wikipedia.org/";
	private static final String RESPONSE_PARSE = "Parsing request - ";
	private static final String ERROR_RESPONSE_MESSAGE = "Cannot receive response.";
	private static final String IO_RESPONSE_ERROR_MESSAGE = "Error during reading data from ";
	private List<Map<String, String>> requestList;
	private List<WikiResponse> responseList;

	public ASyncServiceManager() {
		this.requestList = ServiceManagerHelper.initialize();
	}

	@Override
	public void run() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
				.build();
		WikiApiService service = retrofit.create(WikiApiService.class);
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
						} catch (IOException ioex) {
							logger.error(IO_RESPONSE_ERROR_MESSAGE + response + response.hashCode());
							ioex.printStackTrace();
						}
					}
				}

				private WikiResponse fillWikiResponse(Response<ResponseBody> response) throws IOException {
					WikiResponse wikiresponse = new WikiResponse();
					wikiresponse.setCode(response.code());
					// wikiresponse.setBody(response.body().string());
					// we receive a very huge string, so just take only
					// size
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

	public List<WikiResponse> getResult() {
		return responseList;
	}

	public void setResult(List<WikiResponse> result) {
		this.responseList = result;
	}
	
}