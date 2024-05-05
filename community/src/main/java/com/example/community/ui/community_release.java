package com.example.community.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.adapter.Rc_Adapter_release;
import com.example.community.bean.Data_rc;
import com.example.community.bean.ImagePreviewLoader;
import com.example.community.db.AppDatabase;
import com.previewlibrary.ZoomMediaLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class community_release extends AppCompatActivity {

    //控件
    private Button mButton_release;
    private RecyclerView mRecyclerView_release;
    private Rc_Adapter_release mRecyclerView_Adapter;
    private EditText mEditText;
    private ImageView im_back;

    //记录
    private List<String> mList = new ArrayList<>();
    private Data_rc data;

    @SuppressLint("CheckResult")
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
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initControl();

        applyForPermission();

        ZoomMediaLoader.getInstance().init(new ImagePreviewLoader());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        initDialog();
        return false;
    }

    //初始化控件
    private void initControl() {
        //发布空间
        mButton_release = findViewById(R.id.button_release);
        mButton_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        im_back = findViewById(R.id.im_Back);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialog();
            }
        });

        mRecyclerView_release = findViewById(R.id.recyclerView_community_release);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView_release.setLayoutManager(gridLayoutManager);
        mRecyclerView_Adapter = new Rc_Adapter_release(community_release.this, mList);
        mRecyclerView_release.setAdapter(mRecyclerView_Adapter);

        mEditText = findViewById(R.id.editText_community_release);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override      //接收返回的地址
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            for (Uri uri : list) {
                mList.add(uri.toString());
                Log.d("TAG444", "onActivityResult: " + uri);
            }
            mRecyclerView_Adapter.notifyDataSetChanged();
        }
    }

    //初始化Dialog
    private void initDialog() {
        if(mEditText.getText().length() == 0 && mList.size() == 0) {
            finish();
        }else {
            AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
            alterDialog.setTitle("是否保存草稿");
            alterDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Data_rc data = new Data_rc();
                    data.setContent(mEditText.getText().toString());
                    data.setList(mList);
                    saveData(data);
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },100);
                }
            });
            alterDialog.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppDatabase.getInstance(getApplicationContext()).getDataDao().deleteAll();
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },100);
                }
            });
            alterDialog.show();
        }
    }

    //初始化Matisse
    private void initMatisse() {
        Matisse.from(community_release.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.example.community.fileprovider"))
                //.addFilter(new (320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.album_item_height))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                //.showPreview(true) // Default is `true`
                .forResult(23);
    }

    //保存数据
    private void saveData(Data_rc data) {
        AppDatabase.getInstance(getApplicationContext()).getDataDao().deleteAll();
        AppDatabase.getInstance(getApplicationContext()).getDataDao().insert(data);
    }


    //动态申请权限
    @SuppressLint({"NotifyDataSetChanged", "CheckResult"})
    private void applyForPermission() {
        if (ContextCompat.checkSelfPermission(community_release.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(community_release.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(community_release.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(community_release.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            List<Data_rc> list = AppDatabase.getInstance(getApplicationContext()).getDataDao().loadAll();

            if (list.size() != 0) {
                data = list.get(0);
                mList.addAll(data.getList());
                String content = data.getContent();
                mEditText.setText(content);
            } else {
                initMatisse();
            }
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