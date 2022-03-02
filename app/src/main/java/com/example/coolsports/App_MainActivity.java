package com.example.coolsports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.sport.fragment.main_Fragment_sport;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class App_MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        replaceFragment(new main_Fragment_sport());
    }

    private void initControl() {
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();		//获取FragmentManager对象
        FragmentTransaction transaction = fragmentManager.beginTransaction();	//开启一个事务
        transaction.replace(R.id.frameLayout_app, fragment);	//向容器中添加或者替换碎片
        transaction.commit();	//提交事物
    }
}