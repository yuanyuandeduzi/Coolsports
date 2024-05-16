package com.example.coolsports.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.baselibs.MyView.MessageChange;
import com.example.baselibs.MyView.MyEditText;
import com.example.baselibs.net.network.UploadUtil;
import com.example.baselibs.room.baseroom.AppDataBase;
import com.example.baselibs.room.bean.PlanCalorieTargetByDay;
import com.example.coolsports.R;
import com.example.coolsports.bean.DishesData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(path = "/plan/dishes")
public class Plan_Activity_dishes extends AppCompatActivity {
    private Adapter adapter;
    private List<DishesData> mList = new ArrayList<>();
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_dishes);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        date = getIntent().getStringExtra("date");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initList();
        RecyclerView rc = findViewById(R.id.recyclerVIew_community_main);
        rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(mList, this, date);
        rc.setAdapter(adapter);

        findViewById(R.id.im_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/plan/discern").withString("date", date).navigation();
            }
        });
    }

    private void initList() {
        mList.clear();
        mList.add(new DishesData("牛奶", 58));
        mList.add(new DishesData("馒头", 234));
        mList.add(new DishesData("米饭", 123));
        mList.add(new DishesData("麦片", 332));
        mList.add(new DishesData("饼干", 431));
        mList.add(new DishesData("酸奶", 81));
        mList.add(new DishesData("咖啡", 84));
        mList.add(new DishesData("水煮鸡蛋", 149));
        mList.add(new DishesData("煎鸡胸", 192));
        mList.add(new DishesData("煎鸡蛋", 203));
        mList.add(new DishesData("炒饭", 200));
        mList.add(new DishesData("夹馅面包", 278));
        mList.add(new DishesData("西红柿炒鸡蛋", 84));
        mList.add(new DishesData("茶叶蛋", 149));
        mList.add(new DishesData("烤牛肉", 122));
        mList.add(new DishesData("烤鸡胸", 126));
        mList.add(new DishesData("清炖猪肉", 135));
        mList.add(new DishesData("清炒肉片", 151));
        mList.add(new DishesData("鱼香肉丝", 181));
        mList.add(new DishesData("口水鸡", 174));
        mList.add(new DishesData("辣子鸡", 191));
        mList.add(new DishesData("宫保鸡丁", 207));
        mList.add(new DishesData("烤鸡翅", 217));
        mList.add(new DishesData("奥尔良鸡翅", 240));
        mList.add(new DishesData("糖醋排骨", 343));
        mList.add(new DishesData("油闷大虾", 203));
        mList.add(new DishesData("红烧肉", 484));
        mList.add(new DishesData("红烧肘子", 229));
        mList.add(new DishesData("红烧排骨", 260));
        mList.add(new DishesData("黄焖鸡", 198));
        mList.add(new DishesData("泡椒牛肉", 124));
    }

}

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<DishesData> mList;
    private Context mContext;
    private String date;

    public Adapter(List<DishesData> list, Context context, String date) {
        mList = list;
        mContext = context;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dishes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DishesData dishesData = mList.get(position);
        holder.tv_name.setText(dishesData.getDishes_Name());
        holder.tv_calorie.setText(dishesData.getDishes_calorie() + "千卡/ 100克");
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(dishesData);
            }
        });
    }

    private void openDialog(DishesData dishesData) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dishes_calorie, null);
        Dialog dialog = new Dialog(mContext, R.style.Base_Theme_AppCompat_Dialog_Alert);
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
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_calorie;
        View mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.dish_name);
            tv_calorie = itemView.findViewById(R.id.dish_calorie);
            mLayout = itemView;
        }
    }
}
