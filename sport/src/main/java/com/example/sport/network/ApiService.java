package com.example.sport.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @POST
    @Headers("token: eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDYwNTc3ODksImV4cCI6MTY0NjA2MTM4OSwidXNlcklkIjoiMSJ9.JUyPHKF7y84Ndpt_y1GdlUf7X7qdWGrBW2nQRr4T5q4")
    Call<RunMessage> postCall(@Url String url, @FieldMap Map<String, String> map);
}