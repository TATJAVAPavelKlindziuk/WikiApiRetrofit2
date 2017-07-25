package com.klindziuk.retrofit.service;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WikiApiService {
	@GET("w/api.php")
	Call<ResponseBody> getResponse(@QueryMap Map<String, String> options);
}
