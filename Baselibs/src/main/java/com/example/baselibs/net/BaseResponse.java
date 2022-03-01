package com.example.baselibs.net;

import com.google.gson.annotations.SerializedName;

import java.nio.charset.StandardCharsets;

public class BaseResponse<T> implements IRespose<T> {
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    T data;
    @SerializedName("status")
    String code;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public int getCode() {
        return Integer.parseInt(code);
    }

    @Override
    public boolean isSuccess() {
        return code.equals("0");
    }
}
