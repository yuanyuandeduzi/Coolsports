package com.example.login.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baselibs.MyView.MessageChange;
import com.example.baselibs.net.network.bean.User;
import com.example.login.R;
import com.example.login.databinding.ActivityLoginRegisterBinding;
import com.example.login.viewModel.ViewModel_register;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

public class Login_register extends AppCompatActivity {

    private ActivityLoginRegisterBinding viewBinding;
    private ViewModel_register viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLoginRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        viewModel = new ViewModelProvider(this).get(ViewModel_register.class);

        Intent intent = getIntent();
        viewModel.setPhone(intent.getStringExtra("phone"));

        //用户名
        viewBinding.edTvName.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setName(s);
            }
        });

        viewBinding.edTvGender.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setGender(s);
            }
        });

        viewBinding.edTvAge.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setAge(s);
            }
        });

        viewBinding.edTvEmail.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setEmail(s);
            }
        });

        viewBinding.edTvPassword1.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                viewModel.setPassword(s);
            }
        });

        viewBinding.edTvPassword2.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                if (!s.equals(viewModel.getPassword())) {
                    viewBinding.edTvPassword2.setError("密码不一致！");
                }
                viewModel.setPassword2(s);
            }
        });

        viewBinding.imHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForPermission();
            }
        });

        viewBinding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.addInfo();
            }
        });
        LifecycleOwner owner = this;
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                viewModel.getPassword2().observe(owner, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals(viewModel.getPassword())) {
                            viewBinding.btRegister.setEnabled(viewModel.checkMessage());
                        } else {
                            viewBinding.btRegister.setEnabled(false);
                        }
                    }
                });
            }
        });

        viewModel.getFinish().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    finish();
                }
            }
        });

    }

    @SuppressLint("CheckResult")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            Glide.with(this).load(list.get(0)).into(viewBinding.imHead);
            viewModel.setHeadUrl(list.get(0).toString());
        }
    }


    public void initMatisse() {
        Matisse.from(Login_register.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.example.login.ui.Login_register"))
                //.addFilter(new (320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.album_item_height))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                //.showPreview(true) // Default is `true`
                .forResult(23);
    }

    //动态申请权限
    @SuppressLint({"NotifyDataSetChanged", "CheckResult"})
    private void applyForPermission() {
        if (ContextCompat.checkSelfPermission(Login_register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(Login_register.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(Login_register.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login_register.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            initMatisse();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(Login_register.this, "权限未申请", Toast.LENGTH_SHORT).show();
                            //finish();
                           // return;
                        }
                    }
                }
                initMatisse();
        }
    }
}