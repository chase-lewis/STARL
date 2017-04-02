package com.starlabs.h2o.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.model.report.PurityCondition;
import com.starlabs.h2o.model.report.PurityReport;

import java.util.Date;
import java.util.List;

/**
 * @author Rishi
 */

public class ViewPurityReportsAdapter
        extends RecyclerView.Adapter<ViewPurityReportsAdapter.CustomViewHolder> {
    private final List<PurityReport> waterPurityReports;

    public ViewPurityReportsAdapter(List<PurityReport> waterPurityReports) {
        this.waterPurityReports = waterPurityReports;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.adapter_purity_report, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        PurityReport purityReport = waterPurityReports.get(position);

        holder.workerName.setText("Reported by " + purityReport.getWorkerName());
        holder.reportNumber.setText("Report # " + purityReport.getReportNumber());
        Date createDate = purityReport.getCreationDate();
        holder.creationDate.setText(createDate.toString());
        holder.linkedWaterReport.setText("Linked Water Report: " +
                purityReport.getLinkedWaterReportId());
        PurityCondition purCondition = purityReport.getCondition();
        holder.purityCondition.setText("Condition: " + purCondition.toString());
        holder.virusPPM.setText("virusPPM: " + purityReport.getVirusPPM());
        holder.contPPM.setText("contPPM: " + purityReport.getContPPM());
    }

    @Override
    public int getItemCount() {
        return waterPurityReports.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private final TextView workerName;
        private final TextView reportNumber;
        private final TextView creationDate;
        private final TextView linkedWaterReport;
        private final TextView purityCondition;
        private final TextView virusPPM;
        private final TextView contPPM;

        public CustomViewHolder(View view) {
            super(view);
            workerName = (TextView) view.findViewById(R.id.worker_name);
            reportNumber = (TextView) view.findViewById(R.id.purity_report_title);
            creationDate = (TextView) view.findViewById(R.id.purity_report_date);
            linkedWaterReport = (TextView) view.findViewById(R.id.purity_linked_water_report);
            purityCondition = (TextView) view.findViewById(R.id.purity_condition);
            virusPPM = (TextView) view.findViewById(R.id.virus_PPM);
            contPPM = (TextView) view.findViewById(R.id.cont_PPM);
        }
    }
}
