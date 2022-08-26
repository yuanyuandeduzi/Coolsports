package com.example.baselibs.net.network;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.bean.Message;
import com.example.baselibs.net.network.bean.Record_upLoad;
import com.example.baselibs.net.network.bean.Token;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiService {

    String str = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDcyNDcyMTYsImV4cCI6MTY0ODQ1NjgxNiwidXNlcklkIjoiMSJ9.QrbTyrWZ2N7uAces2p0f4PcycWWKMxW8KAuBasD88uM";

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

    @Multipart
    @POST
    @Headers("token: " + str)
    Call<BaseResponse<String>> community_postCallUploadPhoto(@Url String url, @Part List<MultipartBody.Part> list);

    //识热量
    @POST("token?grant_type=client_credentials&client_id=GMjmqqOG1GSahCNxQBL8Si4A&client_secret=0Q1Ge4HZtw2HbikZ0FRMmkgmPNce79xH")
    Call<Token> plan_discern_postForToken();

    @POST("dish")
    @FormUrlEncoded
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<Message> plan_discern_postForMessage(@FieldMap Map<String, String> map);

    @POST("dish")
    @FormUrlEncoded
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<Object> plan_discern_postForMessage2(@FieldMap Map<String, String> map);

}