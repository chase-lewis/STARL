package com.starlabs.h2o.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.List;

/**
 * Class for representing water reports in views
 *
 * @author Rishi
 */

public class ViewWaterReportsAdapter extends RecyclerView.Adapter<ViewWaterReportsAdapter.CustomViewHolder> {
    private List<WaterReport> waterReports;

    public ViewWaterReportsAdapter(List<WaterReport> waterReports) {
        this.waterReports = waterReports;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.adapter_water_report, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        WaterReport waterReport = waterReports.get(position);

        holder.reporterName.setText("Reported by " + waterReport.getReporterName());
        holder.reportNumber.setText("Report # " + waterReport.getReportNumber());
        holder.reportDate.setText(waterReport.getCreationDate().toString());
        holder.waterLocation.setText("Latitude: " + waterReport.getLatitude()
                + " Longitude: " + waterReport.getLongitude());
        holder.waterType.setText(waterReport.getType().toString());
        holder.waterCondition.setText(waterReport.getCondition().toString());
    }

    @Override
    public int getItemCount() {
        return waterReports.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView reporterName;
        private TextView reportNumber;
        private TextView reportDate;
        private TextView waterLocation;
        private TextView waterType;
        private TextView waterCondition;

        public CustomViewHolder(View view) {
            super(view);
            reporterName = (TextView) view.findViewById(R.id.reporter_name);
            reportNumber = (TextView) view.findViewById(R.id.report_title);
            reportDate = (TextView) view.findViewById(R.id.report_date);
            waterLocation = (TextView) view.findViewById(R.id.water_location);
            waterType = (TextView) view.findViewById(R.id.water_type);
            waterCondition = (TextView) view.findViewById(R.id.water_condition);
        }
    }
}
