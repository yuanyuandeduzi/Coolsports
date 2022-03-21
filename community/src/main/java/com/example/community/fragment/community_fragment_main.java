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

    public community_fragment_main() {
    }

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
        for (int i = 0; i < 5; i++) {
            Data_rc data = new Data_rc();
            data.setName("圆圆的肚子" + i);
            data.setContent("圆圆的肚子" + "\t大家破案的" + "joamoimoimoimoihdsfsdfsadfsadfafasdfsod\tnkjlcs");
            data.setHead("content://media/external/images/media/284647");
            mList.add(data);
        }
    }
}
