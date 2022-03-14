package com.example.sport.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadUtil {

    private final String baseUrl = "http://39.105.117.193/";

    public ApiService getPostService() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit.create(ApiService.class);
    }

}
