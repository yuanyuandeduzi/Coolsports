package com.example.sport.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.sport.R;
import com.example.sport.record.PathRecord;
import com.example.sport.view.GradientProgressBar;
import com.example.sport.view.PickerView;

import java.util.ArrayList;
import java.util.List;

public class sport_MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityResultLauncher<Intent> activityLauncher;
    private Button mButton_room;
    private Button mButton_out;
    private Button mButton_record;
    private Button mButton_target;
    private GradientProgressBar myProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_activity_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initControl();

        myProgress = findViewById(R.id.myProgress);
        myProgress.updateProgress(10.55);

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getResultCode() == 1) {
                    PathRecord pathRecord = (PathRecord) result.getData().getParcelableExtra("ahh");

                } else if (result.getData() != null && result.getResultCode() == 2) {
                    String jj = result.getData().getStringExtra("JJ");

                }
            }
        });
    }

    //
    private void openDialog() {
        Dialog dialog = new Dialog(this,R.style.MyDialog);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_item, null);
        PickerView pickerView = inflate.findViewById(R.id.pickerView);
        List<String> data = new ArrayList<>();
        for(int i = 1; i < 10000; i = i * 2) {
            data.add(i + "km");
        }
        pickerView.setData(data);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = text.substring(0, text.length() - 2);
                double d = Double.parseDouble(s);
                myProgress.setSumProgress(d);
            }
        });
        dialog.setContentView(inflate);
        //设置dialog位置
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.y = 20;
        window.setAttributes(attributes);
        dialog.show();
    }


    //初始化控件
    private void initControl() {
        mButton_out = findViewById(R.id.run_outRoom);
        mButton_out.setOnClickListener(this);
        mButton_room = findViewById(R.id.run_room);
        mButton_room.setOnClickListener(this);
        mButton_record = findViewById(R.id.bt_record);
        mButton_record.setOnClickListener(this);
        mButton_target = findViewById(R.id.bt_target);
        mButton_target.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.run_outRoom:
                Intent intent1 = new Intent(sport_MainActivity.this, sport_Activity_OutRoom.class);
                activityLauncher.launch(intent1);
                break;
            case R.id.run_room:
                Intent intent2 = new Intent(sport_MainActivity.this, sport_Activity_Room.class);
                activityLauncher.launch(intent2);
                break;
            case R.id.bt_target:
                openDialog();
                break;
            case R.id.bt_record:

        }
    }
}