package com.example.sport.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sport.R;
import com.example.sport.ui.Sport_Activity_OutRoom;
import com.example.sport.ui.Sport_Activity_Record;
import com.example.sport.ui.Sport_Activity_Room;
import com.example.sport.view.GradientProgressBar;
import com.example.sport.view.PickerView;

import java.util.ArrayList;
import java.util.List;

public class main_Fragment_sport extends Fragment implements View.OnClickListener{

    private Button mButton_room;
    private Button mButton_out;
    private Button mButton_record;
    private Button mButton_target;
    private GradientProgressBar myProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_activity_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControl(view);
        myProgress = view.findViewById(R.id.myProgress);
        myProgress.updateProgress(10.55);
    }

    //
    private void openDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.MyDialog);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_item, null);
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
    private void initControl(View view) {
        mButton_out = view.findViewById(R.id.run_outRoom);
        mButton_out.setOnClickListener(this);
        mButton_room = view.findViewById(R.id.run_room);
        mButton_room.setOnClickListener(this);
        mButton_record = view.findViewById(R.id.bt_record);
        mButton_record.setOnClickListener(this);
        mButton_target = view.findViewById(R.id.bt_target);
        mButton_target.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.run_outRoom) {
            Intent intent1 = new Intent(getContext(), Sport_Activity_OutRoom.class);
            startActivity(intent1);
        } else if (id == R.id.run_room) {
            Intent intent2 = new Intent(getContext(), Sport_Activity_Room.class);
            startActivity(intent2);
        } else if (id == R.id.bt_target) {
            openDialog();
        } else if (id == R.id.bt_record) {
            Intent intent = new Intent(getContext(), Sport_Activity_Record.class);
            startActivity(intent);
        }
    }

}
