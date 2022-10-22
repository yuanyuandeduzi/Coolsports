package com.example.coolsports.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolsports.R;

public class App_FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_first);

        if (isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(getApplication());

        ARouter.getInstance().build("/login/login1").navigation();
        finish();
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}