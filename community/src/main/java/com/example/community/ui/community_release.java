package com.example.community.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.adapter.Rc_Adapter_release;
import com.example.community.bean.ImagePreviewLoader;
import com.previewlibrary.ZoomMediaLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

public class community_release extends AppCompatActivity {

    //控件
    private Button mButton_release;
    private RecyclerView mRecyclerView_release;
    private Rc_Adapter_release mRecyclerView_Adapter;

    //记录
    private List<Uri> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_release);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        applyForPermission();

        ZoomMediaLoader.getInstance().init(new ImagePreviewLoader());

        initControl();
    }


    private void initControl() {
        mButton_release = findViewById(R.id.button_release);
        mRecyclerView_release = findViewById(R.id.recyclerView_community);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView_release.setLayoutManager(gridLayoutManager);
        mRecyclerView_Adapter = new Rc_Adapter_release(community_release.this,mList);
        mRecyclerView_release.setAdapter(mRecyclerView_Adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override      //接收返回的地址
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            mList.addAll(Matisse.obtainResult(data));
            mRecyclerView_Adapter.notifyDataSetChanged();
        }
    }

    //初始化Matisse
    private void initMatisse() {
        Matisse.from(community_release.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                //.addFilter(new (320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.album_item_height))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                //.showPreview(true) // Default is `true`
                .forResult(23);
    }

    //动态申请权限
    private void applyForPermission() {
        if (ContextCompat.checkSelfPermission(community_release.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(community_release.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(community_release.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(community_release.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            initMatisse();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(community_release.this, "", Toast.LENGTH_SHORT).show();
                            //finish();
                            return;
                        }
                    }
                }
               initMatisse();
        }
    }
}