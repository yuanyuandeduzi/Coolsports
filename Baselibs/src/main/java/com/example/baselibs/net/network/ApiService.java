package com.example.baselibs.net.network;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.bean.Record_upLoad;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    String str = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDcyNDUwNTEsImV4cCI6MTY0ODQ1NDY1MSwidXNlcklkIjoiMSJ9.75LG1vpm-hJrCqlhj2vhPP30omxAxxwvlu-7NnslB1s";

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> sport_postCall(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<Record_upLoad[]>> sport_postCall1(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> sport_postCallForUpdateTarget(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> sport_postCallForgetTarget(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> sport_postCallForgetSumDistance(@Url String url, @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<Record_upLoad[]>> plan_postCallForRecord(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> plan_postCallForTarget(@Url String url, @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> plan_postCallForUpdateTarget(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> plan_postCallForSumTime(@Url String url, @FieldMap Map<String, String> map);


}