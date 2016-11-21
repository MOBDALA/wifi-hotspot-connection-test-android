package com.example.mobdala.wifihotspotconnection.modules.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiPresenter;
import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiView;
import com.example.mobdala.wifihotspotconnection.utils.Constants;

import java.util.List;

public class ListWifiPresenter implements IListWifiPresenter {

    private IListWifiView view = null;

    public ListWifiPresenter(IListWifiView view) {
        this.view = view;
    }

    // ---------------------------------------------------------------------------------------------

    // IListWifiPresenter

    // ---------------------------------------------------------------------------------------------

    @Override
    public void getWifis() {

        try {

            view.showLoading();
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            final WifiManager wifiManager = (WifiManager) view.getContextActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            view.getContextActivity().registerReceiver(new BroadcastReceiver() {

                @SuppressLint("UseValueOf")
                @Override
                public void onReceive(Context context, Intent intent) {
                    List<ScanResult> scanList = wifiManager.getScanResults();
                    Log.i(Constants.LOG_TAG, "Wifi connections: " + scanList.size());
                    if (scanList.size() < 1) {
                        view.showEmpty();
                    } else {
                        view.showList(scanList);
                    }
                    view.hideLoading();
                }

            }, filter);
            wifiManager.startScan();
        } catch (Throwable th) {
            Log.e(Constants.LOG_TAG, "ListWifiPresenter::getWifis", th);
            view.showEmpty();
            view.hideLoading();
        }
    }
}
