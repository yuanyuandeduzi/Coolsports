package com.example.login.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.Button;

public class CountUtil extends CountDownTimer {

    private Button mButton;

    public CountUtil(long millisInFuture, long countDownInterval, Button mButton) {
        super(millisInFuture, countDownInterval);
        this.mButton = mButton;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setEnabled(false);
        mButton.setText((millisUntilFinished / 1000) + "秒");
    }

    @Override
    public void onFinish() {
        mButton.setText("重新获取");
        mButton.setEnabled(true);
    }
}
