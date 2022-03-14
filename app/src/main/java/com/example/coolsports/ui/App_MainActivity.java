package com.example.coolsports.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.coolsports.R;
import com.example.coolsports.fragment.app_fragment_community;
import com.example.coolsports.fragment.app_fragment_myself;
import com.example.coolsports.fragment.app_fragment_plan;
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        replaceFragment(new main_Fragment_sport());
        initControl();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuTd = item.getItemId();
                switch (menuTd) {
                    case R.id.tab_1:
                        replaceFragment(new main_Fragment_sport());
                        break;
                    case R.id.tab_2:
                        replaceFragment(new app_fragment_plan());
                        break;
                    case R.id.tab_3:
                        replaceFragment(new app_fragment_community());
                        break;
                    case R.id.tab_4:
                        replaceFragment(new app_fragment_myself());
                        break;
                }
                return true;
            }
        });
    }

    private void initControl() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();		//获取FragmentManager对象
        FragmentTransaction transaction = fragmentManager.beginTransaction();	//开启一个事务
        transaction.replace(R.id.frameLayout_app, fragment);	//向容器中添加或者替换碎片
        transaction.commit();	//提交事物
    }
}