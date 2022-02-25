package com.example.sport.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sport.R;
import com.example.sport.adapter.Location_Adapter_Rc;
import com.example.sport.db.AppDataBase;
import com.example.sport.db.DbManger;
import com.example.sport.db.DbRecord;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class sport_fragment_location extends Fragment {

    private ImageView im_checkAll;
    private RecyclerView recyclerView;
    private Location_Adapter_Rc location_adapter_rc;
    private List<DbRecord> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sport_fragment_location,container,false);
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
                location_adapter_rc.notifyDataSetChanged();
            }
        });
    }

    private void initControl(View view) {
        im_checkAll = view.findViewById(R.id.im_checkAll);
        recyclerView = view.findViewById(R.id.rv_fragment_location);
        location_adapter_rc = new Location_Adapter_Rc(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(location_adapter_rc);
    }

}
