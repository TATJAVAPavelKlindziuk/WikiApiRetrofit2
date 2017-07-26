package com.klindziuk.retrofit.service.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.klindziuk.retrofit.model.WikiResponse;
import com.klindziuk.retrofit.service.ServiceManagerHelper;
import com.klindziuk.retrofit.service.WikiApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class SyncServiceManager {
	private static final Logger logger = Logger.getLogger(SyncServiceManager.class);
	private static final String BASE_URL = "https://en.wikipedia.org/";
	private static final String REQUEST_PARSE = "Parsing request - ";
	private static final String IO_RESPONSE_ERROR_MESSAGE = "Cannot get data from - ";
	private static List<Map<String, String>> requestList;
	private static List<WikiResponse> responseList;
	private static SyncServiceManager instance = null;
	
	private SyncServiceManager() {}
	
	public static SyncServiceManager getInstance() {
		if (instance == null) {
			requestList = ServiceManagerHelper.initialize();
			instance = new SyncServiceManager();
		}
		return instance;
	}

	public static List<WikiResponse> getResponseList() {
		return responseList;
	}

	public void sendRequest() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
				.build();
		WikiApiService service = retrofit.create(WikiApiService.class);
		responseList = new ArrayList<>();
		for (Map<String, String> paramMap : requestList) {
			Call<ResponseBody> call = service.getResponse(paramMap);
			Response<ResponseBody> response = null;
			WikiResponse wikiResponse = null;
			try {
				logger.info(REQUEST_PARSE + call.request());
				response = call.execute();
				wikiResponse = fillWikiResponse(response);
				responseList.add(wikiResponse);
			} catch (IOException ioex) {
				logger.error(IO_RESPONSE_ERROR_MESSAGE + call.request());
				ioex.printStackTrace();
			}
			logger.info(wikiResponse);
		}
	}

	private WikiResponse fillWikiResponse(Response<ResponseBody> response) throws IOException {
		WikiResponse wikiresponse = new WikiResponse();
		wikiresponse.setCode(response.code());
		// wikiresponse.setBody(response.body().string());
		// we receive a very huge string, so just take only size
		wikiresponse.setBody(String.valueOf(response.body().string().length()));
		wikiresponse.setMessage(response.message());
		wikiresponse.setHeaders(response.headers().toMultimap());
		return wikiresponse;
	}
}
