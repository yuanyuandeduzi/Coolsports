package com.example.sport.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sport.R;
import com.example.sport.db.DbRecord;
import com.example.sport.util.DeleteUtil;

import java.util.List;

public class Location_Adapter_Rc extends RecyclerView.Adapter<Location_Adapter_Rc.ViewHolder> {

    private List<DbRecord> mList;

    public Location_Adapter_Rc(List<DbRecord> mList) {
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
        holder.tv_time.setText(mList.get(position).getRunWhen());
        holder.tv_useTime.setText(mList.get(position).getRunTime());
        holder.tv_distance.setText(mList.get(position).getDistance());
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DeleteUtil.getInstance().setVisibility();
                return true;
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteUtil.getInstance().setInVisibility();
            }
        });
        DeleteUtil.getInstance().addView(holder,mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_distance;
        private final TextView tv_useTime;
        private final TextView tv_time;
        public CheckBox checkBox;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_distance = itemView.findViewById(R.id.tv_distance_location);
            tv_useTime = itemView.findViewById(R.id.tv_useTime_location);
            tv_time = itemView.findViewById(R.id.tv_time_location);
            checkBox = itemView.findViewById(R.id.check_Location);
            view = itemView;
        }
    }
}
