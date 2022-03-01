package com.example.sport.network;

import com.google.gson.annotations.SerializedName;

public class RunMessage {
    @SerializedName("status")
    private String code;
    @SerializedName("msg")
    private String message;
    @SerializedName("data")
    private String data;

    //private Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
