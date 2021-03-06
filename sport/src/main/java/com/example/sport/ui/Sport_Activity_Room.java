package com.example.sport.ui;

import static com.example.sport.util.TimeUtil.getCurrentTime;
import static com.example.sport.util.TimeUtil.stringToTime;
import static com.example.sport.util.TimeUtil.timeToString;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselibs.net.BaseResponse;
import com.example.sport.R;
import com.example.sport.db.DbManger;
import com.example.sport.db.DbRecord;
import com.example.baselibs.net.network.ApiService;
import com.example.baselibs.net.network.UploadUtil;
import com.example.sport.record.PathRecord;
import com.example.sport.view.MyProgressButton;
import com.example.sport.view.PickerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sport_Activity_Room extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    //??????
    private TextView tv_1;
    private Chronometer ch_time;
    private Button bt_1;
    private MyProgressButton mButtonEnd;
    private FrameLayout frameLayout;
    private TextView tv_animation;
    private TextView tv_length;
    private Button bt_setLength;


    //??????
    private boolean isStart = true;
    private boolean isFirst = true;
    private boolean isBegin = true;
    //??????
    private long recordTime = 0;
    private int recordStep = 0;
    private int stepStart = 0;
    private PathRecord pathRecord = new PathRecord();
    private DbRecord dbRecord;

    //??????
    private SensorManager sensorManager;
    private Sensor sensor;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_room);
        //???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initControl();

        if (ContextCompat.checkSelfPermission(Sport_Activity_Room.this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Sport_Activity_Room.this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            createAnimation();
        }

        ch_time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ch_time.setText(timeToString(chronometer));
            }
        });

        mButtonEnd.setListener(new MyProgressButton.MyProgressButtonFinishCallback() {
            @Override
            public void isFinish() {
                pathRecord.setDuration(stringToTime(ch_time.getText().toString()));
                pathRecord.setSteps(Integer.parseInt(tv_1.getText().toString()));
                Log.d("TAG", "isFinish: " + pathRecord.getSteps());
                pathRecord.setDistance(pathRecord.getStride() * pathRecord.getSteps() / 100000f);

                dbRecord = new DbRecord();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String format = decimalFormat.format(pathRecord.getDistance());
                DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
                String format1 = decimalFormat1.format(pathRecord.getDuration());

                dbRecord.setRunTime(format1);
                dbRecord.setRunWhen(getCurrentTime());
                dbRecord.setDistance(format);

                initDialog();
            }

            @Override
            public void isCancel() {

            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //???????????????
    private void initControl() {
        tv_1 = findViewById(R.id.tv_sum_leg);
        ch_time = findViewById(R.id.ch_sport_room);
        mButtonEnd = findViewById(R.id.bt_room_end);
        frameLayout = findViewById(R.id.layout_room_animation);
        tv_animation = findViewById(R.id.tv_room_animation);
        tv_length = findViewById(R.id.tv_room_length);
        bt_setLength = findViewById(R.id.bt_room_length);
        bt_setLength.setOnClickListener(this);
        bt_1 = findViewById(R.id.bt_1);
        bt_1.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_1) {
            if (isStart) {
                bt_1.setText("??????");
                mButtonEnd.setVisibility(View.VISIBLE);
                recordTime = SystemClock.elapsedRealtime() - ch_time.getBase();
                //sensorManager.unregisterListener(sport_Activity_Room.this);
                recordStep = Integer.parseInt((String) tv_1.getText());
                ch_time.stop();
                isStart = false;
            } else {
                bt_1.setText("??????");
                sensorManager.registerListener(Sport_Activity_Room.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                mButtonEnd.setVisibility(View.INVISIBLE);
                ch_time.setBase(SystemClock.elapsedRealtime() - recordTime);
                ch_time.start();
                isStart = true;
            }
        } else if (id == R.id.bt_room_length) {
            openDialog();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isFirst) {
            stepStart = (int) sensorEvent.values[0];
            isFirst = false;
        }
        Log.d("TAG", "onSensorChanged: " + stepStart);
        if (isStart) {
            int step = (int) (sensorEvent.values[0] - stepStart);
            tv_1.setText(String.valueOf(step));
            //float value = sensorEvent.values[0];
        } else {
            stepStart = (int) (sensorEvent.values[0] - recordStep);
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
                            Toast.makeText(Sport_Activity_Room.this, "????????????", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }
                createAnimation();
        }
    }

    //?????????dialog
    private void initDialog() {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
        alterDialog.setCancelable(false);
        alterDialog.setTitle("???????????????????????????????????????");
        alterDialog.setNegativeButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                finish();
            }
        });
        alterDialog.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String, String> map = new HashMap<>();
                map.put("runTime", dbRecord.getRunTime());
                map.put("runWhen", dbRecord.getRunWhen());
                map.put("distance", dbRecord.getDistance());
                map.put("uid", "1");
                UploadUtil util = new UploadUtil();
                ApiService postService = util.getPostService();
                postService.sport_postCall("run/addRunRecord", map).enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                        if (response.body() == null) {
                            DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                            Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().isSuccess()) {
                            Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                        DbManger.getInstance(getApplicationContext()).insert(dbRecord).subscribe();
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }
        });
        alterDialog.show();
    }


    //???????????????dialog
    private void openDialog() {
        Dialog dialog = new Dialog(this, R.style.MyDialog);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_item, null);
        PickerView pickerView = inflate.findViewById(R.id.pickerView);
        List<String> data = new ArrayList<>();
        for (int i = 60; i < 90; i++) {
            data.add(i + "cm");
        }
        pickerView.setData(data);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = text.substring(0, text.length() - 2);
                pathRecord.setStride(Float.parseFloat(s));
                tv_length.setText("?????????" + text);
            }
        });
        dialog.setContentView(inflate);
        //??????dialog??????
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.y = 20;
        window.setAttributes(attributes);
        dialog.show();
    }

    //????????????
    private void createAnimation() {
        tv_animation.setText("3");
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(tv_animation, "scaleX", 70f, 20f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(tv_animation, "scaleY", 70f, 20f);
        objectAnimator1.setDuration(1000);
        objectAnimator2.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator1).with(objectAnimator2);
        animatorSet.start();
        AnimatorSet clone1 = animatorSet.clone();
        AnimatorSet clone2 = animatorSet.clone();
        AnimatorSet clone3 = animatorSet.clone();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tv_animation.setText("2");
                clone1.start();
            }
        });
        clone1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tv_animation.setText("1");
                clone2.start();
            }
        });
        clone2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tv_animation.setText("Go");
                clone3.start();
            }
        });
        clone3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                frameLayout.setVisibility(View.GONE);
                bt_1.setVisibility(View.VISIBLE);
                bt_setLength.setVisibility(View.VISIBLE);
                ch_time.setBase(SystemClock.elapsedRealtime());
                isStart = true;
                ch_time.start();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}