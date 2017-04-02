package com.starlabs.h2o.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.report.WaterCondition;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.report.WaterType;

import java.util.Date;
import java.util.List;

/**
 * Class for representing water reports in views
 *
 * @author Rishi
 */

public class ViewWaterReportsAdapter
        extends RecyclerView.Adapter<ViewWaterReportsAdapter.CustomViewHolder> {
    private final List<WaterReport> waterReports;

    public ViewWaterReportsAdapter(List<WaterReport> waterReports) {
        this.waterReports = waterReports;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.adapter_water_report, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        WaterReport waterReport = waterReports.get(position);

        holder.reporterName.setText("Reported by " + waterReport.getReporterName());
        holder.reportNumber.setText("Report # " + waterReport.getReportNumber());
        Date createDate = waterReport.getCreationDate();
        holder.reportDate.setText(createDate.toString());
        holder.waterLocation.setText("Latitude: " + waterReport.getLatitude()
                + "\nLongitude: " + waterReport.getLongitude());
        WaterType watType = waterReport.getType();
        holder.waterType.setText(watType.toString());
        WaterCondition watCondition = waterReport.getCondition();
        holder.waterCondition.setText(watCondition.toString());

        List<Integer> linkedWater = waterReport.getLinkedPurityReports();
        if (linkedWater.isEmpty()) {
            holder.linkedPurityReports.setText("No Linked Purity Reports");
        } else {
            holder.linkedPurityReports.setText("Linked Purity Reports: "
                    + linkedWater.toString());
        }
    }

    @Override
    public int getItemCount() {
        return waterReports.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private final TextView reporterName;
        private final TextView reportNumber;
        private final TextView reportDate;
        private final TextView waterLocation;
        private final TextView waterType;
        private final TextView waterCondition;
        private final TextView linkedPurityReports;

        public CustomViewHolder(View view) {
            super(view);
            reporterName = (TextView) view.findViewById(R.id.reporter_name);
            reportNumber = (TextView) view.findViewById(R.id.report_title);
            reportDate = (TextView) view.findViewById(R.id.report_date);
            waterLocation = (TextView) view.findViewById(R.id.water_location);
            waterType = (TextView) view.findViewById(R.id.water_type);
            waterCondition = (TextView) view.findViewById(R.id.water_condition);
            linkedPurityReports = (TextView) view.findViewById(R.id.water_linked_purity_reports);
        }
    }
}
