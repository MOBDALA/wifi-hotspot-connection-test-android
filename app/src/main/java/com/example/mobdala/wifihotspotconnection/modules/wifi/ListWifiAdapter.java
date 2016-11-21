package com.example.mobdala.wifihotspotconnection.modules.wifi;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobdala.wifihotspotconnection.R;
import com.example.mobdala.wifihotspotconnection.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListWifiAdapter extends RecyclerView.Adapter<ListWifiAdapter.ViewHolder> {

    private List<ScanResult> itemList;
    private OnItemClickListener listener;

    public ListWifiAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ---------------------------------------------------------------------------------------------

    // RecyclerView.Adapter

    // ---------------------------------------------------------------------------------------------

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(position, listener);
    }

    @Override
    public int getItemCount() {
        return (itemList == null) ? 0 : itemList.size();
    }

    // ---------------------------------------------------------------------------------------------

    // Public Methods

    // ---------------------------------------------------------------------------------------------

    public void updateItems(List<ScanResult> items) {

        if (items != null) {
            itemList = new ArrayList<>(items);
            notifyDataSetChanged();
        }
    }

    // ---------------------------------------------------------------------------------------------

    // OnClickListener

    // ---------------------------------------------------------------------------------------------

    public interface OnItemClickListener {
        void onItemClick(ScanResult item, int position);
    }

    private View.OnClickListener onClickListener(final int position, final OnItemClickListener listener, final ScanResult item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item, position);
            }
        };
    }

    // ---------------------------------------------------------------------------------------------

    // ViewHolder

    // ---------------------------------------------------------------------------------------------

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvBSSID_value)
        TextView tvBSSID_value;
        @BindView(R.id.tvSSID_value)
        TextView tvSSID_value;
        @BindView(R.id.tvCapabilities_value)
        TextView tvCapabilities_value;
        @BindView(R.id.tvCenterFreq0_value)
        TextView tvCenterFreq0_value;
        @BindView(R.id.tvCenterFreq1_value)
        TextView tvCenterFreq1_value;
        @BindView(R.id.tvChannelWidth_value)
        TextView tvChannelWidth_value;
        @BindView(R.id.tvFrequency_value)
        TextView tvFrequency_value;
        @BindView(R.id.tvLevel_value)
        TextView tvLevel_value;
        @BindView(R.id.tvOperatorFriendlyName_value)
        TextView tvOperatorFriendlyName_value;
        @BindView(R.id.tvTimestamp_value)
        TextView tvTimestamp_value;
        @BindView(R.id.tvVenueName_value)
        TextView tvVenueName_value;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindItem(final int position, final OnItemClickListener listener) {

            try {

                final ScanResult scanResult = itemList.get(position);

                tvBSSID_value.setText(scanResult.BSSID);
                tvSSID_value.setText(scanResult.SSID);
                tvCapabilities_value.setText(scanResult.capabilities);
//                tvCenterFreq0_value.setText(String.valueOf(scanResult.centerFreq0)); // Api 23
//                tvCenterFreq1_value.setText(String.valueOf(scanResult.centerFreq1)); // Api 23
//                tvChannelWidth_value.setText(String.valueOf(scanResult.channelWidth)); // APi 23
                tvFrequency_value.setText(String.valueOf(scanResult.frequency));
                tvLevel_value.setText(String.valueOf(scanResult.level));
//                tvOperatorFriendlyName_value.setText(String.valueOf(scanResult.operatorFriendlyName)); // Api 23
//                tvTimestamp_value.setText(String.valueOf(scanResult.timestamp)); // Api 23
//                tvVenueName_value.setText(String.valueOf(scanResult.venueName)); // Api 23

                itemView.setOnClickListener(onClickListener(position, listener, scanResult));

            } catch (Exception ex) {

                Log.e(Constants.LOG_TAG, "ListWifiAdapter::bindItem", ex);
            }
        }
    }
}
