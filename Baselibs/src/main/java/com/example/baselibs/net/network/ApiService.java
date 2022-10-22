package com.example.baselibs.net.network;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.bean.Message;
import com.example.baselibs.net.network.bean.Record_upLoad;
import com.example.baselibs.net.network.bean.Token;
import com.example.baselibs.net.network.bean.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> sport_postCall(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Record_upLoad[]>> sport_postCall1(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> sport_postCallForUpdateTarget(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> sport_postCallForgetTarget(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> sport_postCallForgetSumDistance(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Record_upLoad[]>> plan_postCallForRecord(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> plan_postCallForTarget(@Url String url, @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> plan_postCallForUpdateTarget(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> plan_postCallForSumTime(@Url String url, @FieldMap Map<String, String> map);

    @Multipart
    @POST
    Call<BaseResponse<String>> community_postCallUploadPhoto(@Url String url, @Part List<MultipartBody.Part> list);

    //登录注册

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Boolean>> login_postGetCode(@Url String url, @Field("phone") String phone);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<User>> login_postCheckCode(@Url String url, @Field("phone") String phone, @Field("code") String code);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Boolean>> login_postAddInfo(@Url String url,@FieldMap Map<String,String> map);


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