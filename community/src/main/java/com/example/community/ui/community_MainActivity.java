package com.example.community.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.baselibs.net.BaseResponse;
import com.example.baselibs.net.network.UploadUtil;
import com.example.community.R;
import com.example.community.adapter.Rc_Adapter_main;
import com.example.community.bean.Data_rc;
import com.example.community.bean.ImagePreviewLoader;
import com.previewlibrary.ZoomMediaLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class community_MainActivity extends AppCompatActivity {

    private ImageView im_1;
    private RecyclerView recyclerView;
    private List<Data_rc> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ZoomMediaLoader.getInstance().init(new ImagePreviewLoader());

        initControl();

    }

    private void initControl() {
        im_1 = findViewById(R.id.im_1);
        im_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(community_MainActivity.this, community_release.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerVIew_community_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        initList();
        recyclerView.setAdapter(new Rc_Adapter_main(mList));
    }

    private void initList() {
        mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Data_rc data = new Data_rc();
            data.setName("圆圆的肚子" + i);
            data.setContent("圆圆的肚子" + "\t大家破案的" + "joamoimoimoimoihdsfsdfsadfsadfafasdfsod\tnkjlcs");
            data.setHead("content://media/external/images/media/284647");
            mList.add(data);
        }
    }

    private void upload() {
        File file = new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_2022-03-03-01-01-01-132_com.miui.home.jpg");
        if (!file.exists()) {
            file.mkdir();
        }
        RequestBody imgBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), imgBody);

        List<MultipartBody.Part> list = new ArrayList<>();
        list.add(filePart);
        list.add(filePart);

        new UploadUtil().getPostService().getPartData("testImg",list).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                Log.d("TAG", "onResponse: " + response.body().getData());
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }
}