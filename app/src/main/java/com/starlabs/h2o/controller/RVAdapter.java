package com.starlabs.h2o.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.WaterReport;

import java.util.List;

/**
 * Created by Rishi on 3/1/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CustomViewHolder>{
    private List<WaterReport> waterReports;

    public RVAdapter(List<WaterReport> waterReports){
        this.waterReports = waterReports;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public CustomViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_view_reports, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        WaterReport waterReport = waterReports.get(position);

    }

    @Override
    public int getItemCount() {
        return waterReports.size();
    }
}
