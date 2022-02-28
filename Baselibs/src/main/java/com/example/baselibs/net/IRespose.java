package com.example.baselibs.net;

public interface IRespose<T> {
    public String getMsg();
    public T getData();

    public int getCode();
    public boolean isSuccess();
}
