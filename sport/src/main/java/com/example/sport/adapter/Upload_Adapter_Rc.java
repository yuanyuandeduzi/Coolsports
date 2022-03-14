package com.example.sport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sport.R;
import com.example.baselibs.net.network.bean.Record_upLoad;

import java.util.List;

public class Upload_Adapter_Rc extends RecyclerView.Adapter<Upload_Adapter_Rc.ViewHolder>{

    private List<Record_upLoad> mList;

    public Upload_Adapter_Rc(List<Record_upLoad> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recrview_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record_upLoad record_upLoad = mList.get(position);
        holder.tv_time.setText(record_upLoad.getRunWhen());
        holder.tv_useTime.setText(record_upLoad.getRunTime());
        holder.tv_distance.setText(record_upLoad.getDistance());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_distance;
        private final TextView tv_useTime;
        private final TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_distance = itemView.findViewById(R.id.tv_distance_location);
            tv_useTime = itemView.findViewById(R.id.tv_useTime_location);
            tv_time = itemView.findViewById(R.id.tv_time_location);
        }
    }
}
