package com.example.coolsports.ui;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.example.baselibs.MyView.MessageChange;
import com.example.baselibs.MyView.MyEditText;
import com.example.baselibs.net.network.ApiService;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.Message;
import com.example.baselibs.net.network.bean.Token;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.baselibs.room.bean.PlanCalorieTargetByDay;
import com.example.coolsports.R;
import com.example.coolsports.bean.DishesData;
import com.example.coolsports.util.Plan_discernUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Route(path = "/plan/discern")
public class Plan_Activity_Discern extends AppCompatActivity {

    //控件
    private TextView mTv_1;
    private TextView mTv_2;
    private ImageView mIm_1;
    private Button mBt_1;

    private Boolean isFinish = false;
    private String token = "";
    private Uri uri;
    private String date;

    private Context mContext;
    private DishesData dishesData = new DishesData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_discern);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mContext = this;
        date = getIntent().getStringExtra("date");
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
                isFinish = false;
                mBt_1.setEnabled(false);
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
        findViewById(R.id.plan_bt_discern_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(dishesData);
            }
        });
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
                //.gridExpectedSize(getResources().getDimensionPixelSize(com.example.community.R.dimen.album_item_height))
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
                Log.d("TAG111", "onResponse: " + token);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }

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

        mRetrofit.create(ApiService.class).plan_discern_postForMessage(map).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message.Data result = response.body().getResult()[0];
                runOnUiThread(() -> {
                    mTv_1.setText("菜名：" + result.getName());
                    mTv_2.setText("热量：" + result.getCalorie() + "千卡/100克");
                });
                dishesData.setDishes_Name(result.getName());
                dishesData.setDishes_calorie(Integer.parseInt(result.getCalorie()));
                isFinish = true;
                mBt_1.setEnabled(true);
                Toast.makeText(mContext, "请求成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog(DishesData dishesData) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.dishes_calorie, null);
        Dialog dialog = new Dialog(this, R.style.Base_Theme_AppCompat_Dialog_Alert);
        dialog.setContentView(rootView);
        TextView tv_name = rootView.findViewById(R.id.tv_name);
        TextView tv_calorie = rootView.findViewById(R.id.tv_calorie);
        MyEditText editText = rootView.findViewById(R.id.ed_num);
        editText.afterTextChanged(new MessageChange() {
            @Override
            public void afterChanged(String s) {
                if (!s.equals("") && Integer.parseInt(s) >= 999) {
                    editText.setText("1000");
                }
                editText.setSelection(Objects.requireNonNull(editText.getText()).length());
            }
        });
        Button bt_confirm = rootView.findViewById(R.id.bt_confirm);
        tv_name.setText(dishesData.getDishes_Name());
        tv_calorie.setText(dishesData.getDishes_calorie() + "千卡/100克");
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!text.equals("") && Integer.parseInt(text) != 0) {
                    float num = Float.parseFloat(text);
                    num = num / 100 * dishesData.getDishes_calorie();
                    PlanCalorieTargetByDay calorieTarget = new PlanCalorieTargetByDay();
                    calorieTarget.setTargetWhen(date);
                    calorieTarget.setPhone(UploadUtil.user.getPhone());
                    PlanCalorieTargetByDay planSportTargetByDay = AppDataBase.getInstance(mContext).getPlanCalorieTargetDao().queryByDate(date, UploadUtil.user.getPhone());
                    if (planSportTargetByDay != null) {
                        calorieTarget.setNowInput((int) num + planSportTargetByDay.getNowInput());
                        calorieTarget.setTarget(planSportTargetByDay.getTarget());
                    }
                    AppDataBase.getInstance(mContext).getPlanCalorieTargetDao().delete(date, UploadUtil.user.getPhone());
                    AppDataBase.getInstance(mContext).getPlanCalorieTargetDao().insert(calorieTarget);
                }
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }
}