package com.example.community.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.community.R;
import com.example.community.bean.Data_rc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

public class Rc_Adapter_main extends RecyclerView.Adapter<Rc_Adapter_main.ViewHolder> {

    private List<Data_rc> mList;
    private Context context;
    private List<String> list;
    private Activity activity;

    public Rc_Adapter_main(List<Data_rc> mList) {
        this.mList = mList;
        initList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_2, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data_rc data = mList.get(position);
        Glide.with(context).load(data.getHead()).into(holder.im_head);
        holder.tv_name.setText(data.getName());
        holder.tv_content.setText(data.getContent());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(new Rc_Adapter_main_inner(list));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageFilterView im_head;
        private TextView tv_content;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            im_head = itemView.findViewById(R.id.im_head);
            recyclerView = itemView.findViewById(R.id.recyclerView_community_main_inner);
        }
    }

    private void initList() {
        list = new ArrayList<>();
        list.add("content://media/external/images/media/388611");
        list.add("content://media/external/images/media/388611");
        list.add("content://media/external/images/media/388611");
        list.add("content://media/external/images/media/388611");
        list.add("content://media/external/images/media/388611");

    }
}