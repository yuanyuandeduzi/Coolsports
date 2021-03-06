package com.example.sport.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sport.R;
import com.example.sport.fragment.sport_fragment_location;
import com.example.sport.fragment.sport_fragment_upload;
import com.example.sport.util.DeleteUtil;
import com.google.android.material.tabs.TabLayout;

public class Sport_Activity_Record extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private Button bt_delete;
    private ImageView im_checkAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_record);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initControl();
        replaceFragment(new sport_fragment_upload());

        DeleteUtil.getInstance().setListener(new DeleteUtil.DeleteListener() {
            @Override
            public void isVisibility() {
                bt_delete.setVisibility(View.VISIBLE);
                im_checkAll.setVisibility(View.VISIBLE);
                im_checkAll.setImageResource(R.drawable.im_check_1);
                tabLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void isUnVisibility() {
                bt_delete.setVisibility(View.GONE);
                im_checkAll.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void setImageView(int n, boolean bool) {
                im_checkAll.setImageResource(n);
                //DeleteUtil.getInstance().setCheckAll(bool);
                DeleteUtil.getInstance().setCheck(bool);
            }
        });

    }

    private void initControl() {
        tabLayout = findViewById(R.id.tabLayout_record);
        tabLayout.addTab(tabLayout.newTab().setText("已上传"));
        tabLayout.addTab(tabLayout.newTab().setText("待上传"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int id = tab.getPosition();
                if(id == 0) {
                    replaceFragment(new sport_fragment_upload());
                }else {
                    replaceFragment(new sport_fragment_location());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        im_checkAll = findViewById(R.id.im_checkAll);
        bt_delete = findViewById(R.id.bt_delete_location);
        im_checkAll.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();		//获取FragmentManager对象
        FragmentTransaction transaction = fragmentManager.beginTransaction();	//开启一个事务
        transaction.replace(R.id.frameLayout, fragment);	//向容器中添加或者替换碎片
        transaction.commit();	//提交事物
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_delete_location) {
            DeleteUtil.getInstance().deleteChecked(this);
            DeleteUtil.getInstance().setInVisibility();
        } else if (id == R.id.im_checkAll) {
            if (!DeleteUtil.getInstance().isCheck()) {
                im_checkAll.setImageResource(R.drawable.im_check_2);
                DeleteUtil.getInstance().setCheckAll(true);
                DeleteUtil.getInstance().setCheck(true);
            } else {
                im_checkAll.setImageResource(R.drawable.im_check_1);
                DeleteUtil.getInstance().setCheckAll(false);
                DeleteUtil.getInstance().setCheck(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DeleteUtil.getInstance().setMapClear();
        return super.onKeyDown(keyCode, event);
    }
}