package com.example.sport.fragment;

import static android.content.Context.POWER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.net.network.bean.DbRecord;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.sport.R;
import com.example.sport.db.AppDataBaseLocation;
import com.example.sport.record.TargetDistance;
import com.example.sport.ui.Sport_Activity_OutRoom;
import com.example.sport.ui.Sport_Activity_Record;
import com.example.sport.ui.Sport_Activity_Room;
import com.example.sport.view.GradientProgressBar;
import com.example.sport.view.PickerView;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/sport/sport1")
public class sport_Fragment_main extends Fragment implements View.OnClickListener {

    private Button mButton_room;
    private Button mButton_out;
    private Button mButton_record;
    private Button mButton_target;
    private GradientProgressBar myProgress;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_activity_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        initControl(view);
        myProgress = view.findViewById(R.id.myProgress);
        getSportTargetAndDistance();
    }

    private double d;

    //创建Dialog
    private void openDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.MyDialog);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_item, null);
        PickerView pickerView = inflate.findViewById(R.id.pickerView);
        List<String> data = new ArrayList<>();
        for (int i = 1; i < 10000; i = i * 2) {
            data.add(i + "km");
        }
        pickerView.setData(data);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = text.substring(0, text.length() - 2);
                d = Double.parseDouble(s);
            }
        });
        Button button = inflate.findViewById(R.id.button_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProgress.setSumProgress(d);
                updateTarget(d);
                dialog.cancel();
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
//            if (getContext() != null && !isTrue(getContext())) {
//                new DialogUtils().initDialog(getContext());
//            } else {
            Intent intent1 = new Intent(getContext(), Sport_Activity_OutRoom.class);
            startActivity(intent1);
//            }
        } else if (id == R.id.run_room) {
//            if (getContext() != null && !isTrue(getContext())) {
//                new DialogUtils().initDialog(getContext());
//            } else {
            Intent intent2 = new Intent(getContext(), Sport_Activity_Room.class);
            startActivity(intent2);
//            }
        } else if (id == R.id.bt_target) {
            openDialog();
        } else if (id == R.id.bt_record) {
            Intent intent = new Intent(getContext(), Sport_Activity_Record.class);
            startActivity(intent);
        }
    }

    private boolean isTrue(Context mContext) {
        PowerManager powerManager = (PowerManager) mContext.getSystemService(POWER_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return powerManager.isIgnoringBatteryOptimizations(mContext.getPackageName());
        }
        return false;
    }

    //更新总目标
    private void updateTarget(Double d) {
        AppDataBaseLocation instance = AppDataBaseLocation.getInstance(mContext);
        instance.getTargetDao().delete(UploadUtil.user.getPhone());
        instance.getTargetDao().insert(new TargetDistance(d, UploadUtil.user.getPhone()));

        /*Map<String, String> map = new HashMap<>();
        map.put("uid", UploadUtil.uid);
        map.put("target", "" + d);
        UploadUtil.sentPostService().sport_postCallForUpdateTarget("run/addSportTarget", map).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });*/
    }

    //获取总目标
    private void getSportTargetAndDistance() {
        TargetDistance result = AppDataBaseLocation.getInstance(mContext).getTargetDao().query(UploadUtil.user.getPhone());
        if(result != null) {
            myProgress.setSumProgress(result.getTargetDistance());
        }
        List<DbRecord> queryResult = AppDataBase.getInstance(mContext).getDbRecordDao().queryByPhone(UploadUtil.user.getPhone());
        double sum = 0;
        for (DbRecord dbRecord : queryResult) {
            sum += Double.parseDouble(dbRecord.getDistance());
        }
        myProgress.updateProgress(sum);
        /*      Map<String, String> map = new HashMap<>();
        map.put("uid", UploadUtil.uid);
        UploadUtil.sentPostService().sport_postCallForgetTarget("run/getSportTarget", map).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    String str = response.body().getData();
                    myProgress.setSumProgress(Double.parseDouble(str));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });

        UploadUtil.sentPostService().sport_postCallForgetSumDistance("run/getRunSumDistance", map).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    String str = response.body().getData();
                    myProgress.updateProgress(Double.parseDouble(str));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });*/

    }
}
