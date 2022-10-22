package com.example.login.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.baselibs.BuildConfig;
import com.example.baselibs.MyView.MessageChange;
import com.example.baselibs.net.network.UploadUtil;
import com.example.login.R;
import com.example.login.databinding.LoginActivityMainBinding;
import com.example.login.utils.CountUtil;
import com.example.login.viewModel.ViewModel_MainActivity;

import java.util.Objects;

@Route(path = "/login/login1")
public class Login_MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivityMainBinding viewBinding;
    private ViewModel_MainActivity viewModel;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = LoginActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(getApplication());

        mContext = getBaseContext();
        viewModel = new ViewModelProvider(this).get(ViewModel_MainActivity.class);
        viewBinding.buttonStartCode.setOnClickListener(this);
        viewBinding.login.setOnClickListener(this);

        viewModel.getMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //未注册验证码不正确  未注册验证码正确
                switch (s) {
                    case "已注册验证码不正确":
                    case "未注册验证码不正确":
                        sendToast("电话号或验证码不正确！！");
                        break;
                    case "未注册验证码正确":
                        Intent intent = new Intent(Login_MainActivity.this, Login_register.class);
                        intent.putExtra("phone", Objects.requireNonNull(viewModel.getPeople().getValue()).getPhone());
                        startActivity(intent);
                        break;
                    case "已注册验证码正确":
                        if (UploadUtil.isStart) {
                            ARouter.getInstance().build("/app/main").navigation();
                        }
                        UploadUtil.isLogin.setValue(true);
                        UploadUtil.isStart = false;
                        finish();
                        break;
                }
            }
        });

        viewBinding.loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/app/main").navigation();
                UploadUtil.isStart = false;
                finish();
            }
        });

        viewModel.getPeople().observe(this, new Observer<ViewModel_MainActivity.person>() {
            @Override
            public void onChanged(ViewModel_MainActivity.person person) {
                boolean start = Objects.requireNonNull(viewModel.getPeople().getValue()).isStart();
                viewBinding.login.setEnabled(start);
            }
        });

        viewBinding.username.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setPhone(s);
            }
        });

        viewBinding.password.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setCode(s);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("TAG", "onBackPressed: ");
    }

    public static void sendToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {
            viewModel.checkCode();
        } else if (id == R.id.button_startCode) {
            new CountUtil(60 * 1000, 1000, viewBinding.buttonStartCode).start();
            viewModel.getCode();
        }
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}