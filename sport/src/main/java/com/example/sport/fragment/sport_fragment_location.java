package com.example.sport.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baselibs.net.network.bean.DbRecord;
import com.example.sport.R;
import com.example.sport.adapter.Location_Adapter_Rc;
import com.example.sport.db.DbManger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class sport_fragment_location extends Fragment{

    private RecyclerView recyclerView;
    private Location_Adapter_Rc location_adapter_rc;
    private List<DbRecord> mList = new ArrayList<>();
    private TextView tv_noRecord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sport_fragment_location, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControl(view);

        DbManger.getInstance(getContext()).getAll().subscribe(new Consumer<List<DbRecord>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void accept(List<DbRecord> list) throws Exception {
                mList.clear();
                mList.addAll(list);
                if(list.size() > 0) {
                    tv_noRecord.setVisibility(View.INVISIBLE);
                }else {
                    tv_noRecord.setVisibility(View.VISIBLE);
                }
                location_adapter_rc.notifyDataSetChanged();
            }
        });

    }

    private void initControl(View view) {

        tv_noRecord = view.findViewById(R.id.tv_fragment_location);

        recyclerView = view.findViewById(R.id.rv_fragment_location);
        recyclerView.setItemViewCacheSize(20);
        location_adapter_rc = new Location_Adapter_Rc(mList, this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(location_adapter_rc);
    }
}
