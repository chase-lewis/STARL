package com.starlabs.h2o.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterCondition;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.report.WaterType;
import com.starlabs.h2o.model.user.User;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class for representing water reports in views
 *
 * @author tejun, Rishi
 */

public class ViewWaterReportsAdapter extends RecyclerView.Adapter<ViewWaterReportsAdapter.CustomViewHolder> {
    private final List<WaterReport> waterReports;

    /**
     * Constructor for viewing water purity reports
     *
     * @param waterReports list of water reports to view
     */
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

        holder.reportNumber.setText("Report Number " + waterReport.getReportNumber());
        Date createDate = waterReport.getCreationDate();
        holder.reportDate.setText(createDate.toString());
        holder.reporterUsername.setText("Reported by " + waterReport.getReporterId());
        holder.waterLocation.setText("Latitude: " + waterReport.getLatitude()
                + "\nLongitude: " + waterReport.getLongitude());
        WaterType watType = waterReport.getType();
        holder.waterType.setText("Type: " + watType.toString());
        WaterCondition watCondition = waterReport.getCondition();
        holder.waterCondition.setText("Condition: " + watCondition.toString());

        List<Integer> linkedWater = waterReport.getLinkedPurityReports();
        if (linkedWater.isEmpty()) {
            holder.linkedPurityReports.setText("No Linked Purity Reports");
        } else {
            holder.linkedPurityReports.setText("Linked Purity Reports: " + linkedWater.toString());
        }

        // Get the user associated with the report
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<User> onUserFound = user -> {
            if (user != null) {
                // Set the profile picture
                holder.reporterPicture.setImageBitmap(User.stringToBitmap(user.getProfilePicture()));
            }
        };
        contentProvider.getSingleUser(onUserFound, waterReport.getReporterId());
    }

    @Override
    public int getItemCount() {
        return waterReports.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private final TextView reportNumber;
        private final TextView reportDate;
        private final TextView reporterUsername;
        private final ImageView reporterPicture;
        private final TextView waterType;
        private final TextView waterCondition;
        private final TextView waterLocation;
        private final TextView linkedPurityReports;

        /**
         * Custom view holder for viewing water reports adapter
         *
         * @param view view to hold
         */
        CustomViewHolder(View view) {
            super(view);
            reporterUsername = (TextView) view.findViewById(R.id.water_report_adapter_username);
            reporterPicture = (ImageView) view.findViewById(R.id.water_report_adapter_profile_picture);
            reportNumber = (TextView) view.findViewById(R.id.water_report_adapter_id);
            reportDate = (TextView) view.findViewById(R.id.water_report_adapter_date);
            waterLocation = (TextView) view.findViewById(R.id.water_report_adapter_location);
            waterType = (TextView) view.findViewById(R.id.water_report_adapter_type);
            waterCondition = (TextView) view.findViewById(R.id.water_report_adapter_condition);
            linkedPurityReports = (TextView) view.findViewById(R.id.water_report_adapter_linked_purity_reports);
        }
    }
}
