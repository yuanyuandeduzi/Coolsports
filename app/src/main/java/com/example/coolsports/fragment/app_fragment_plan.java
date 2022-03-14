package com.example.coolsports.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coolsports.R;
import com.example.coolsports.adapter.Plan_Fragment_Adapter_Rc;
import com.example.coolsports.bean.Data;
import com.example.coolsports.util.Plan_Fragment_RcUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class app_fragment_plan extends Fragment implements View.OnClickListener {

    private TextView mTv_1;
    private Button mButton_1;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Plan_Fragment_Adapter_Rc plan_fragment_adapter_rc;
    private boolean isUseControl = false;
    private List<Data> mList;
    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            smoothScrollToPosition();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = getList();
        initControl(view);

        Plan_Fragment_RcUtils.getsInstance().setListener(new Plan_Fragment_RcUtils.ChangeListener() {
            @Override
            public void isInVisibility() {
                mButton_1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void isNotToday(Data data) {
                if (!data.getDayAndMonth().contentEquals(mTv_1.getText())) {
                    mButton_1.setVisibility(View.VISIBLE);
                }
                mTv_1.setText(data.getDayAndMonth());
            }
        });

    }

    private void initControl(View view) {
        mTv_1 = view.findViewById(R.id.tv_1);
        mButton_1 = view.findViewById(R.id.bt_1);
        mButton_1.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.plan_fragment_rc);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        plan_fragment_adapter_rc = new Plan_Fragment_Adapter_Rc(mList);
        mRecyclerView.setAdapter(plan_fragment_adapter_rc);
        linearLayoutManager.scrollToPositionWithOffset(30, 0);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_1) {
            linearLayoutManager.scrollToPositionWithOffset(30, 0);
            for (Data data : mList) {
                data.setSelected(false);
            }
            mList.get(30).setSelected(true);
            plan_fragment_adapter_rc.notifyDataSetChanged();
            mTv_1.setText("今日");
            mButton_1.setVisibility(View.INVISIBLE);
        }
    }

    //滑动归位
    private void smoothScrollToPosition() {
        isUseControl = true;
        View view = mRecyclerView.getChildAt(0);
        int width = view.getMeasuredWidth();
        int right = view.getRight();
        if (width != right) {
            if (right >= (width / 2)) {
                mRecyclerView.smoothScrollBy(-(width - right), 0);
            } else {
                mRecyclerView.smoothScrollBy(right, 0);
            }
        }
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
            if (i == 0) {
                data.setDayAndMonth("今日");
                data.setDay("今日");
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
