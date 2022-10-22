package com.example.sport.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baselibs.net.BaseResponse;
import com.example.sport.R;
import com.example.sport.adapter.Upload_Adapter_Rc;
import com.example.baselibs.net.network.bean.Record_upLoad;
import com.example.baselibs.net.network.UploadUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sport_fragment_upload extends Fragment {

    private List<Record_upLoad> mList = new ArrayList<>();
    private RecyclerView rc_upload;
    private TextView tv_upload;
    private Upload_Adapter_Rc rc_Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.sport_fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        upload();
        initControl(view);
    }

    private void initControl(View view) {
        rc_upload = view.findViewById(R.id.rv_fragment_upload);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rc_upload.setLayoutManager(linearLayoutManager);
        rc_Adapter = new Upload_Adapter_Rc(mList);
        rc_upload.setAdapter(rc_Adapter);

        tv_upload = view.findViewById(R.id.tv_fragment_upload);
    }

    @SuppressLint("CheckResult")
    private void upload() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", UploadUtil.uid);
        UploadUtil.sentPostService().sport_postCall1("run/queryRunRecordsByUid", map).enqueue(new Callback<BaseResponse<Record_upLoad[]>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BaseResponse<Record_upLoad[]>> call, Response<BaseResponse<Record_upLoad[]>> response) {
                if (response.body() == null) {
                    Toast.makeText(getContext(), "请求失败！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body().isSuccess()) {
                    Record_upLoad[] data = response.body().getData();
                    Collections.addAll(mList, data);
                    if(mList.size() != 0) {
                        tv_upload.setVisibility(View.INVISIBLE);
                    }
                    rc_Adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "请求成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "请求失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Record_upLoad[]>> call, Throwable t) {
                Toast.makeText(getContext(), "请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
