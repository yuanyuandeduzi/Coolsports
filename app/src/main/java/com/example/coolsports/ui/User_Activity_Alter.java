package com.example.coolsports.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.example.baselibs.MyView.MessageChange;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.User;
import com.example.coolsports.ViewModel_activity_user;
import com.example.coolsports.databinding.ActivityUserAlterBinding;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

@Route(path = "/user/user2")
public class User_Activity_Alter extends AppCompatActivity {

    private ActivityUserAlterBinding viewBinding;
    private ViewModel_activity_user viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityUserAlterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        Log.d("TAG", "onCreate11: " + UploadUtil.user.toString());

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        viewModel = new ViewModelProvider(this).get(ViewModel_activity_user.class);
        viewModel.setUser(UploadUtil.user);
        initMessage();

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

        viewBinding.btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.updateInfo();
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
                            viewBinding.btFinish.setEnabled(viewModel.checkMessage());
                        } else {
                            viewBinding.btFinish.setEnabled(false);
                        }
                    }
                });
            }
        });

        viewModel.getFinish().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if ("修改成功".equals(s)) {
                    EventBus.getDefault().postSticky(viewModel.getUser().getValue());
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_SHORT).show();
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
            //viewModel.setHeadUrl(list.get(0).toString());
        }
    }


    public void initMatisse() {
        Matisse.from(User_Activity_Alter.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.example.login.ui.Login_register"))
                //.addFilter(new (320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(com.example.login.R.dimen.album_item_height))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                //.showPreview(true) // Default is `true`
                .forResult(23);
    }

    //动态申请权限
    @SuppressLint({"NotifyDataSetChanged", "CheckResult"})
    private void applyForPermission() {
        if (ContextCompat.checkSelfPermission(User_Activity_Alter.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(User_Activity_Alter.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(User_Activity_Alter.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(User_Activity_Alter.this,
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
                            Toast.makeText(User_Activity_Alter.this, "权限未申请", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                initMatisse();
        }
    }

    private void initMessage() {
        User value = viewModel.getUser().getValue();
        viewBinding.edTvAge.setText(value.getAge());
        viewBinding.edTvEmail.setText(value.getEmail());
        viewBinding.edTvGender.setText(value.getGender());
        viewBinding.edTvName.setText(value.getUserName());
    }


}