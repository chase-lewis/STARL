package com.starlabs.h2o.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityCondition;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.user.User;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Rishi
 */

public class ViewPurityReportsAdapter
        extends RecyclerView.Adapter<ViewPurityReportsAdapter.CustomViewHolder> {
    private final List<PurityReport> purityReports;

    /**
     * Constructor that takes in a list of purity reports to view
     *
     * @param purityReports water purity reports to view
     */
    public ViewPurityReportsAdapter(List<PurityReport> purityReports) {
        this.purityReports = purityReports;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.adapter_purity_report, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        PurityReport purityReport = purityReports.get(position);

        holder.reportNumber.setText("Report Number " + purityReport.getReportNumber());
        Date createDate = purityReport.getCreationDate();
        holder.creationDate.setText(createDate.toString());
        holder.reporterUsername.setText("Reported by " + purityReport.getReporterId());
        PurityCondition purCondition = purityReport.getCondition();
        holder.purityCondition.setText("Condition:\n" + purCondition.toString());
        holder.virusPPM.setText("Virus PPM: " + purityReport.getVirusPPM());
        holder.contPPM.setText("Contaminant PPM: " + purityReport.getContPPM());
        holder.linkedWaterReport.setText("Linked Water Report: " + purityReport.getLinkedWaterReportId());

        // Get the user associated with the report
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<User> onUserFound = user -> {
            if (user != null) {
                // Set the profile picture
                holder.reporterPicture.setImageBitmap(User.stringToBitmap(user.getProfilePicture()));
            }
        };
        contentProvider.getSingleUser(onUserFound, purityReport.getReporterId());
    }

    @Override
    public int getItemCount() {
        return purityReports.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private final TextView reportNumber;
        private final TextView creationDate;
        private final TextView reporterUsername;
        private final ImageView reporterPicture;
        private final TextView purityCondition;
        private final TextView virusPPM;
        private final TextView contPPM;
        private final TextView linkedWaterReport;

        /**
         * Custom view holder for viewing purity reports
         *
         * @param view view to hold
         */
        CustomViewHolder(View view) {
            super(view);

            reportNumber = (TextView) view.findViewById(R.id.purity_report_adapter_id);
            creationDate = (TextView) view.findViewById(R.id.purity_report_adapter_date);
            reporterUsername = (TextView) view.findViewById(R.id.purity_report_adapter_username);
            reporterPicture = (ImageView) view.findViewById(R.id.purity_report_adapter_profile_picture);
            purityCondition = (TextView) view.findViewById(R.id.purity_report_adapter_condition);
            virusPPM = (TextView) view.findViewById(R.id.purity_report_adapter_virus);
            contPPM = (TextView) view.findViewById(R.id.purity_report_adapter_contaminant);
            linkedWaterReport = (TextView) view.findViewById(R.id.purity_report_adapter_linked_water_report);
        }
    }
}
