package com.example.coolsports.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.baselibs.TimeUtil;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.baselibs.room.bean.PlanCalorieTargetByDay;
import com.example.baselibs.room.bean.PlanSportTargetByDay;
import com.example.coolsports.R;
import com.example.coolsports.adapter.Plan_Fragment_Adapter_Rc1;
import com.example.coolsports.bean.Data;
import com.example.coolsports.myView.MyPlanProgressBar;
import com.example.coolsports.util.Plan_Fragment_RcUtils;
import com.example.sport.adapter.Upload_Adapter_Rc;
import com.example.baselibs.net.network.bean.DbRecord;
import com.example.sport.view.PickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Route(path = "/plan/plan1")
public class app_fragment_plan extends Fragment implements View.OnClickListener {

    //控件
    private TextView mTv_1;
    private Button mButton_1;
    private MyPlanProgressBar sportProgressBar;
    private TextView mTv_2;
    private TextView mSport_time;
    private Button mButton_2;
    private TextView mTv_4;
    private Button mButton_calorie;


    private TextView mCalorie_num;
    private TextView mCalorie_target;
    private MyPlanProgressBar calorieProgress;

    private ConstraintLayout calorie_layout;

    //RecyclerView1
    private RecyclerView mRecyclerView1;
    private LinearLayoutManager linearLayoutManager1;
    private Plan_Fragment_Adapter_Rc1 plan_fragment_adapter_rc1;
    private boolean isUseControl = false;
    private List<Data> mList1;
    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            smoothScrollToPosition();
        }
    };

    //RecyclerView2
    private RecyclerView mRecyclerView2;
    private List<DbRecord> mList2 = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager2;
    private Upload_Adapter_Rc upload_adapter_rc2;

    //记录运动目标
    private int sportTarget = 100;
    private Data data;
    private Data dataNow;
    private AppDataBase appDataBase;

    //记录饮食目标
    private int calorieTarget = 1500;

    //活动跳转
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appDataBase = AppDataBase.getInstance(getContext());
        mList1 = getList();
        dataNow = data;
        initControl(view);
        uploadData(data);
        Plan_Fragment_RcUtils.getsInstance().setListener(new Plan_Fragment_RcUtils.ChangeListener() {
            @Override
            public void isInVisibility() {
                mButton_1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void isNotToday(Data data, int position) {
                if (!data.getDayAndMonth().contentEquals(mTv_1.getText())) {
                    mButton_1.setVisibility(View.VISIBLE);
                }
                mTv_1.setText(data.getDayAndMonth());
                if (position < 30) {
                    mButton_2.setVisibility(View.INVISIBLE);
                    mButton_calorie.setVisibility(View.INVISIBLE);
                } else {
                    mButton_2.setVisibility(View.VISIBLE);
                    mButton_calorie.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void updateMainData(Data data) {
                dataNow = data;
                uploadData(data);
                Toast.makeText(getContext(), "请求成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intentActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //此处是跳转的result回调方法
                        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {

                        } else {

                        }
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        uploadData(dataNow);
    }

    //获取计划当天的记录
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void uploadData(Data data) {
        List<DbRecord> result = appDataBase.getDbRecordDao().queryByDate(data.getDayTime(), UploadUtil.user.getPhone());
        mList2.clear();
        mList2.addAll(result);
        if (mList2.size() != 0) {
            mTv_4.setVisibility(View.GONE);
        } else {
            mTv_4.setVisibility(View.VISIBLE);
        }
        upload_adapter_rc2.notifyDataSetChanged();

        PlanSportTargetByDay planSportTargetByDay = appDataBase.getPlanSportTargetDao().queryByDate(data.getDayTime(), UploadUtil.user.getPhone());
        if (planSportTargetByDay != null) {
            mSport_time.setText("/" + planSportTargetByDay.getTarget() + "分钟");
            sportProgressBar.setProgress(planSportTargetByDay.getTarget());
        } else {
            mSport_time.setText("/" + 30 + "分钟");
            sportProgressBar.setProgress(30);
        }

        float sum = 0f;
        for (DbRecord dbRecord : result) {
            sum += Float.parseFloat(dbRecord.getRunTime());
        }
        sportProgressBar.setCurrentProgress(sum);
        mTv_2.setText((int) sum + "");

        PlanCalorieTargetByDay planCalorieTargetByDay = appDataBase.getPlanCalorieTargetDao().queryByDate(data.getDayTime(), UploadUtil.user.getPhone());
        if (planCalorieTargetByDay != null) {
            mCalorie_num.setText(planCalorieTargetByDay.getNowInput() + "");
            mCalorie_target.setText("/" + planCalorieTargetByDay.getTarget() + "千卡");
            calorieProgress.setCurrentProgress(planCalorieTargetByDay.getNowInput());
            calorieProgress.setProgress(planCalorieTargetByDay.getTarget());
        } else {
            mCalorie_num.setText("0");
            mCalorie_target.setText("/1500千卡");
            calorieProgress.setProgress(30);
            calorieProgress.setCurrentProgress(0);
        }
/*        Map<String, String> map = new HashMap<>();
        //uid
        map.put("uid", UploadUtil.uid);
        map.put("day", data.getDayTime());
        UploadUtil.sentPostService().plan_postCallForRecord("run/getPlanRunRecord", map).enqueue(new Callback<BaseResponse<DbRecord[]>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BaseResponse<DbRecord[]>> call, Response<BaseResponse<DbRecord[]>> response) {
                BaseResponse<DbRecord[]> body = response.body();
                if (body != null && body.isSuccess()) {
                    DbRecord[] data1 = body.getData();
                    mList2.clear();
                    Collections.addAll(mList2, data1);
                    if (mList2.size() != 0) {
                        mTv_4.setVisibility(View.GONE);
                    } else {
                        mTv_4.setVisibility(View.VISIBLE);
                    }
                    upload_adapter_rc2.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DbRecord[]>> call, Throwable t) {
                mTv_4.setVisibility(View.VISIBLE);
            }
        });

        UploadUtil.sentPostService().plan_postCallForTarget("run/getPlanTarget", map).enqueue(new Callback<BaseResponse<String>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                String target = "0";
                if (response.body() != null && response.isSuccessful()) {
                    target = response.body().getData();
                    if (target != null) {
                        mTv_3.setText("/" + target + "分钟");
                        myPlanProgressBar.setProgress(Float.parseFloat(target));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });

        UploadUtil.sentPostService().plan_postCallForSumTime("run/getPlanDaySumTime", map).enqueue(new Callback<BaseResponse<String>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    float sum = Float.parseFloat(response.body().getData());
                    myPlanProgressBar.setCurrentProgress(sum);
                    mTv_2.setText((int) sum + "");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });*/

    }

    //设置今日目标
    private void updateSportTarget(int target) {
        /*UploadUtil util = new UploadUtil();
        Map<String, String> map = new HashMap<>();
        map.put("uid", UploadUtil.uid);
        map.put("day", dataNow.getDayTime());
        map.put("target", target + "");
        util.sentPostService().plan_postCallForUpdateTarget("run/updatePlanTarget", map).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });*/
        PlanSportTargetByDay sportTarget = new PlanSportTargetByDay();
        sportTarget.setTarget(target);
        sportTarget.setTargetWhen(dataNow.getDayTime());
        sportTarget.setPhone(UploadUtil.user.getPhone());
        appDataBase.getPlanSportTargetDao().delete(dataNow.getDayTime(), UploadUtil.user.getPhone());
        appDataBase.getPlanSportTargetDao().insert(sportTarget);
    }

    private void updateCalorieTarget(int target) {
        PlanCalorieTargetByDay calorieTarget = new PlanCalorieTargetByDay();
        calorieTarget.setTarget(target);
        calorieTarget.setTargetWhen(dataNow.getDayTime());
        calorieTarget.setPhone(UploadUtil.user.getPhone());
        PlanCalorieTargetByDay planSportTargetByDay = appDataBase.getPlanCalorieTargetDao().queryByDate(dataNow.getDayTime(), UploadUtil.user.getPhone());
        if (planSportTargetByDay != null) {
            calorieTarget.setNowInput(planSportTargetByDay.getNowInput());
        }
        appDataBase.getPlanCalorieTargetDao().delete(dataNow.getDayTime(), UploadUtil.user.getPhone());
        appDataBase.getPlanCalorieTargetDao().insert(calorieTarget);
    }


    private void initControl(View view) {
        mTv_1 = view.findViewById(R.id.tv_1);
        mButton_1 = view.findViewById(R.id.bt_1);
        mButton_1.setOnClickListener(this);

        mRecyclerView1 = view.findViewById(R.id.plan_fragment_rc1);
        linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView1.setLayoutManager(linearLayoutManager1);
        plan_fragment_adapter_rc1 = new Plan_Fragment_Adapter_Rc1(mList1);
        mRecyclerView1.setAdapter(plan_fragment_adapter_rc1);
        linearLayoutManager1.scrollToPositionWithOffset(30, 0);

        //实现归位
        mRecyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isUseControl) {
                        recyclerView.postDelayed(run, 200);
                    }
                }

                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_SETTLING) {//非自动滑动
                    isUseControl = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mTv_2 = view.findViewById(R.id.tv_current);
        mSport_time = view.findViewById(R.id.tv_sum);
        mTv_4 = view.findViewById(R.id.tv_4);
        sportProgressBar = view.findViewById(R.id.myProgressBar);
        mRecyclerView2 = view.findViewById(R.id.plan_fragment_rc2);
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView2.setLayoutManager(linearLayoutManager2);

        upload_adapter_rc2 = new Upload_Adapter_Rc(mList2);
        mRecyclerView2.setAdapter(upload_adapter_rc2);

        mButton_2 = view.findViewById(R.id.bt_plan_target);
        mButton_2.setOnClickListener(this);

        calorie_layout = view.findViewById(R.id.location_plan_ka);
        calorie_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/plan/dishes")
                        .withString("date", dataNow.getDayTime()).navigation();
            }
        });

        mCalorie_num = view.findViewById(R.id.tv_ka_current);
        mCalorie_target = view.findViewById(R.id.tv_ka_sum);
        calorieProgress = view.findViewById(R.id.myProgressBar_ka);
        calorieProgress.setProgress(30);
        mButton_calorie = view.findViewById(R.id.bt_plan_ka_target);
        mButton_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalorieDialog();
            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_1) {
            linearLayoutManager1.scrollToPositionWithOffset(30, 0);
            for (Data data : mList1) {
                data.setSelected(false);
            }
            mList1.get(30).setSelected(true);
            plan_fragment_adapter_rc1.notifyDataSetChanged();
            mTv_1.setText("今日");
            mButton_1.setVisibility(View.INVISIBLE);
            mButton_2.setVisibility(View.VISIBLE);
            mButton_calorie.setVisibility(View.VISIBLE);
            dataNow = data;
            uploadData(data);
        } else if (view.getId() == R.id.bt_plan_target) {
            openSportDialog();
        }
    }

    //滑动归位
    private void smoothScrollToPosition() {
        isUseControl = true;
        View view = mRecyclerView1.getChildAt(0);
        int width = view.getMeasuredWidth();
        int right = view.getRight();
        if (width != right) {
            if (right >= (width / 2)) {
                mRecyclerView1.smoothScrollBy(-(width - right), 0);
            } else {
                mRecyclerView1.smoothScrollBy(right, 0);
            }
        }
    }

    //调整目标
    private void openSportDialog() {
        Dialog dialog = new Dialog(getContext(), com.example.sport.R.style.MyDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.plan_dialog_item, null);
        Button bt = view.findViewById(R.id.button_1);
        PickerView pickerView = view.findViewById(com.example.sport.R.id.pickerView);
        List<String> data = new ArrayList<>();
        for (int i = 20; i < 180; i += 10) {
            data.add(i + "分钟");
        }
        pickerView.setData(data);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = text.substring(0, text.length() - 2);
                sportTarget = Integer.parseInt(s);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                mSport_time.setText("/" + sportTarget + "分钟");
                updateSportTarget(sportTarget);
                sportProgressBar.setProgress(sportTarget);
                dialog.cancel();
            }
        });

        dialog.setContentView(view);
        //设置dialog位置
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.y = 20;
        window.setAttributes(attributes);
        dialog.show();

    }

    //调整卡路里目标
    private void openCalorieDialog() {
        Dialog dialog = new Dialog(getContext(), com.example.sport.R.style.MyDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.plan_dialog_item, null);
        Button bt = view.findViewById(R.id.button_1);
        PickerView pickerView = view.findViewById(com.example.sport.R.id.pickerView);
        List<String> data = new ArrayList<>();
        for (int i = 1500; i < 3000; i += 200) {
            data.add(i + "千卡");
        }
        pickerView.setData(data);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = text.substring(0, text.length() - 2);
                calorieTarget = Integer.parseInt(s);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                updateCalorieTarget(calorieTarget);
                mCalorie_target.setText("/" + calorieTarget + "千卡");
                calorieProgress.setProgress(calorieTarget);
                dialog.cancel();
            }
        });

        dialog.setContentView(view);
        //设置dialog位置
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.y = 20;
        window.setAttributes(attributes);
        dialog.show();

    }

    //获取时间集合
    private List<Data> getList() {
        Calendar calendar = Calendar.getInstance();
        List<Data> list = new ArrayList<>();
        for (int i = -30; i < 30; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, i);
            Data data = new Data();
            data.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH) + "");
            data.setDay(getWeek(calendar.get(Calendar.DAY_OF_WEEK)));
            data.setDayTime(TimeUtil.getTimeByDate(calendar.getTime()));
            if (i == 0) {
                data.setDayAndMonth("今日");
                data.setDay("今日");
                this.data = data;
                data.setSelected(true);
            } else {
                data.setDayAndMonth(calendar.get(Calendar.MONTH) + 1 + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            }
            list.add(data);
            calendar.add(Calendar.DAY_OF_MONTH, -i);
        }
        return list;
    }

    private String getWeek(int day) {
        switch (day) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }
}
