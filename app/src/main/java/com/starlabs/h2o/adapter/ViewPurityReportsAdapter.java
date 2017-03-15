package com.starlabs.h2o.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.report.PurityReport;

import java.util.List;

/**
 * Created by Rishi on 3/14/2017.
 */

public class ViewPurityReportsAdapter extends RecyclerView.Adapter<ViewPurityReportsAdapter.CustomViewHolder> {
    private List<PurityReport> waterPurityReports;

    public ViewPurityReportsAdapter(List<PurityReport> waterPurityReports) {
        this.waterPurityReports = waterPurityReports;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purityreport_list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        PurityReport waterReport = waterPurityReports.get(position);

        holder.workerName.setText("Reported by " + waterReport.getworkerName());
        holder.reportNumber.setText("Report # " + waterReport.getReportNumber());
        holder.creationDate.setText(waterReport.getCreationDate().toString());
        holder.purityLocation.setText("Latitude: " + waterReport.getLatitude()
                + " Longitude: " + waterReport.getLongitude());
        holder.purityCondition.setText("Condition: " + waterReport.getCondition().toString());
        holder.virusPPM.setText("virusPPM: " + waterReport.getVirusPPM());
        holder.virusPPM.setText("contPPM: " + waterReport.getContPPM());
    }

    @Override
    public int getItemCount() {
        return waterPurityReports.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView workerName;
        TextView reportNumber;
        TextView creationDate;
        TextView purityLocation;
        TextView purityCondition;
        TextView virusPPM;
        TextView contPPM;

        public CustomViewHolder(View view) {
            super(view);
            workerName = (TextView) view.findViewById(R.id.worker_name);
            reportNumber = (TextView) view.findViewById(R.id.purity_report_title);
            creationDate = (TextView) view.findViewById(R.id.purity_report_date);
            purityLocation = (TextView) view.findViewById(R.id.purity_location);
            purityCondition = (TextView) view.findViewById(R.id.purity_condition);
            virusPPM = (TextView) view.findViewById(R.id.virus_PPM);
            contPPM = (TextView) view.findViewById(R.id.cont_PPM);
        }
    }
}
