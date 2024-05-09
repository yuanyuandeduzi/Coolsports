package com.example.baselibs.net.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.baselibs.net.network.bean.User;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadUtil {

    public static String uid = "";
    public static MutableLiveData<Boolean> isLogin = new MutableLiveData<>(false);
    public static User user = new User();

    //锁对象
    private static final Object object = new Object();

    private static volatile ApiService inStance_send = null;
    private static volatile ApiService instance_login = null;

    private static final String baseUrl = "http://39.105.117.193/";

    private static String token = "";

    private static final OkHttpClient client_login = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Response proceed = chain.proceed(chain.request());
            token = proceed.header("token");
            Log.d("TAG", "intercept: " + token);
            return proceed;
        }
    }).build();

    private static final OkHttpClient client_send = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("token", token).build();
            return chain.proceed(request);
        }
    }).build();

    //带token发送请求
    public static ApiService sentPostService() {

        if (inStance_send == null) {
            synchronized (object) {
                if (inStance_send == null) {
                    Retrofit mRetrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client_send)
                            .build();
                    inStance_send = mRetrofit.create(ApiService.class);
                }
            }
        }
        return inStance_send;
    }

    //登录获取token
    public static ApiService loginPostService() {
        if (instance_login == null) {
            synchronized (object) {
                if (instance_login == null) {
                    Retrofit mRetrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client_login)
                            .build();
                    instance_login = mRetrofit.create(ApiService.class);
                }
            }
        }
        return instance_login;
    }
}
