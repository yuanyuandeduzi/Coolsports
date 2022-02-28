package com.example.baselibs.net;

public class BaseResponse<T> implements IRespose<T> {
    String msg;
    T data;
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
        return false;
    }
}
