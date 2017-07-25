package com.klindziuk.retrofit.service;

import java.util.Map;

import com.klindziuk.retrofit.model.WikiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WikiApiService {
			 
	    @GET("w/api.php")
	    Call<WikiResponse> getResponse(@QueryMap Map<String, String> options);
	}
	 

