package com.example.sport.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.sport.R;
import com.example.sport.fragment.sport_fragment_location;
import com.google.android.material.tabs.TabLayout;

public class sport_Activity_Record extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_record);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initControl();
        replaceFragment(new sport_fragment_location());
    }

    private void initControl() {
        tabLayout = findViewById(R.id.tabLayout_record);
        tabLayout.addTab(tabLayout.newTab().setText("已上传"));
        tabLayout.addTab(tabLayout.newTab().setText("待上传"));

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();		//获取FragmentManager对象
        FragmentTransaction transaction = fragmentManager.beginTransaction();	//开启一个事务
        transaction.replace(R.id.frameLayout, fragment);	//向容器中添加或者替换碎片
        transaction.commit();	//提交事物
    }
}