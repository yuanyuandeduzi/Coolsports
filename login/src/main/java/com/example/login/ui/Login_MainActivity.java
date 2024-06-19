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
import com.example.baselibs.net.network.bean.User;
import com.example.baselibs.room.baseroom.UserDataBase;
import com.example.baselibs.room.dao.UserDao;
import com.example.login.R;
import com.example.login.databinding.LoginActivityMainBinding;
import com.example.login.viewModel.ViewModel_MainActivity;

import java.util.Objects;

@Route(path = "/login/login1")
public class Login_MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivityMainBinding viewBinding;
    private ViewModel_MainActivity viewModel;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private UserDao userDao;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDao = UserDataBase.getInstance(mContext).getUserDao();
            }
        }).start();
        viewModel = new ViewModelProvider(this).get(ViewModel_MainActivity.class);
        viewBinding.login.setOnClickListener(this);
        viewBinding.register.setOnClickListener(this);

        viewBinding.loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/app/main").navigation();
                finish();
            }
        });

        viewModel.getPeople().observe(this, new Observer<ViewModel_MainActivity.person>() {
            @Override
            public void onChanged(ViewModel_MainActivity.person person) {
                boolean start = Objects.requireNonNull(viewModel.getPeople().getValue()).isStart();
                viewBinding.login.setEnabled(start);
                boolean enableRegister = viewModel.getPeople().getValue().isEnableRegister();
                viewBinding.register.setEnabled(enableRegister);
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

        viewBinding.register.setOnClickListener(v -> {
            Intent intent = new Intent(Login_MainActivity.this, Login_register.class);
            intent.putExtra("phone", Objects.requireNonNull(viewModel.getPeople().getValue()).getPhone());
            startActivity(intent);
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
            User user = userDao.query(viewModel.getPeople().getValue().getPhone());
            if (user != null && user.getPassword().equals(viewModel.getPeople().getValue().getPassword())) {
                UploadUtil.user = user;
                UploadUtil.isLogin.setValue(true);
                ARouter.getInstance().build("/app/main").navigation();
                sendToast("登录成功");
                finish();
            } else if (user != null && !user.getPassword().equals(viewModel.getPeople().getValue().getPassword())) {
                sendToast("密码错误,请重试！");
            } else {
                sendToast("账号不存在,请先注册!");
                Intent intent = new Intent(Login_MainActivity.this, Login_register.class);
                intent.putExtra("phone", Objects.requireNonNull(viewModel.getPeople().getValue()).getPhone());
                startActivity(intent);
            }
        }
    }


    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}