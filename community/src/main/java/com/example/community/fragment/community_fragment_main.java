package com.example.community.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.community.R;
import com.example.community.adapter.Rc_Adapter_main;
import com.example.community.bean.Data_rc;
import com.example.community.bean.ImagePreviewLoader;
import com.example.community.ui.community_release;
import com.previewlibrary.ZoomMediaLoader;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/community/community1")
public class community_fragment_main extends Fragment {

    private ImageView im_1;
    private RecyclerView recyclerView;
    private List<Data_rc> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_community_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZoomMediaLoader.getInstance().init(new ImagePreviewLoader());
        initControl(view);
    }


    private void initControl(View view) {
        im_1 = view.findViewById(R.id.im_1);
        im_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), community_release.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerVIew_community_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initList();
        recyclerView.setAdapter(new Rc_Adapter_main(mList));
    }

    private void initList() {
        mList = new ArrayList<>();
        Data_rc data = new Data_rc();
        data.setName("圆圆的肚子");
        data.setContent("圆圆的肚子" + "\t闲来无事");
        data.setHead("content://media/external/images/media/1000001277");
        mList.add(data);
        Data_rc data1 = new Data_rc();
        data1.setName("土豆大侠");
        data1.setContent("番薯番薯，我是土豆");
        data1.setHead("content://media/external/images/media/1000000835");
        mList.add(data1);
        Data_rc data2 = new Data_rc();
        data2.setName("一颗包子");
        data2.setContent("包子要想好吃，还得皮包馅多");
        data2.setHead("content://media/external/images/media/1000000833");
        mList.add(data2);
        Data_rc data3 = new Data_rc();
        data3.setName("天上飞鸟");
        data3.setContent("做一只在天上永不下落的飞鸟，直至死去！！！");
        data3.setHead("content://media/external/images/media/1000001277");
        mList.add(data3);
        Data_rc data4 = new Data_rc();
        data4.setName("挖土机");
        data4.setContent("挖掘机技术哪家强，中国山东找蓝翔，学挖掘机技术，认准蓝翔职业技术学院。");
        data4.setHead("content://media/external/images/media/1000001277");
        mList.add(data4);
    }
}
