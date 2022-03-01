package com.example.sport.network;

import com.example.baselibs.net.BaseResponse;

import java.util.Map;

import io.reactivex.Flowable;
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
    @Headers("token: eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDYxMjE2OTAsImV4cCI6MTY0NzMzMTI5MCwidXNlcklkIjoiMSJ9.sX4f3fSAgY4_F3mjpoDc2OetOQYe5-ICb7_Ea9L95Nk")
    Call<BaseResponse<String>> postCall(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDYxMjE2OTAsImV4cCI6MTY0NzMzMTI5MCwidXNlcklkIjoiMSJ9.sX4f3fSAgY4_F3mjpoDc2OetOQYe5-ICb7_Ea9L95Nk")
    Call<BaseResponse<Record_upLoad[]>> getCall1(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    @Headers("token: eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDYxMjE2OTAsImV4cCI6MTY0NzMzMTI5MCwidXNlcklkIjoiMSJ9.sX4f3fSAgY4_F3mjpoDc2OetOQYe5-ICb7_Ea9L95Nk")
    Flowable<BaseResponse<Record_upLoad[]>> getCall2(@Url String url, @FieldMap Map<String, String> map);
}