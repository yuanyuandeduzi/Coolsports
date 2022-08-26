package com.example.coolsports.ui;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baselibs.net.network.ApiService;
import com.example.baselibs.net.network.bean.Message;
import com.example.baselibs.net.network.bean.Token;
import com.example.coolsports.R;
import com.example.coolsports.util.Plan_discernUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Plan_Activity_Discern extends AppCompatActivity {

    //控件
    private TextView mTv_1;
    private TextView mTv_2;
    private ImageView mIm_1;
    private Button mBt_1;

    private String token = "";
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_discern);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getToken();
        initControl();

        applyForPermission();


    }

    private void initControl() {
        mBt_1 = findViewById(R.id.plan_bt_discern_1);
        mBt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String path = Plan_discernUtils.getRealFilePath(getApplicationContext(), Uri.parse("content://media/external/images/media/398990"));
                String path = Plan_discernUtils.getRealFilePath(getApplicationContext(), uri);
                String s = Plan_discernUtils.imageToBase64(path);

                getMassage(s);
            }
        });
        mIm_1 = findViewById(R.id.plan_im_discern_1);
        mIm_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });
        mTv_1 = findViewById(R.id.plan_tv_discern_1);
        mTv_2 = findViewById(R.id.plan_tv_discern_2);
    }

    //初始化Matisse
    private void initMatisse() {
        Matisse.from(Plan_Activity_Discern.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.example.community.fileprovider"))
                //.addFilter(new (320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(com.example.community.R.dimen.album_item_height))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                //.showPreview(true) // Default is `true`
                .forResult(23);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override      //接收返回的地址
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            uri = list.get(0);
            Log.d("TAG", "onActivityResult: " + uri);
            Glide.with(this).load(uri).into(mIm_1);

        }
    }

    //动态申请权限
    @SuppressLint({"NotifyDataSetChanged", "CheckResult"})
    private void applyForPermission() {
        if (ContextCompat.checkSelfPermission(Plan_Activity_Discern.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(Plan_Activity_Discern.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(Plan_Activity_Discern.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Plan_Activity_Discern.this,
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
                            return;
                        }
                    }
                }
                initMatisse();
        }
    }

    //获取token
    private void getToken() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://aip.baidubce.com/oauth/2.0/")
                .build();
        mRetrofit.create(ApiService.class).plan_discern_postForToken().enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                assert response.body() != null;
                token = response.body().getAccess_token();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }

    private Message.Data result;

    //获取卡路里信息
    private void getMassage(String s) {
        Retrofit mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://aip.baidubce.com/rest/2.0/image-classify/v2/")
                .build();
        Map<String, String> map = new HashMap<>();
        map.put("image", s);
        map.put("filter_threshold", "0.8");
        map.put("access_token", token);
        map.put("top_num", "1");
        /*mRetrofit.create(ApiService.class).plan_discern_postForMessage2(map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("TAG", "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });*/

        mRetrofit.create(ApiService.class).plan_discern_postForMessage(map).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                assert response.body() != null;
                Message.Data result = response.body().getResult()[0];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTv_1.setText("菜名：" + result.getName());
                        mTv_2.setText("热量：" + result.getCalorie() + "ka");
                        Log.d("TAG", "run: ");
                    }
                });
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }
}