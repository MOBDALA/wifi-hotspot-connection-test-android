package com.example.mobdala.wifihotspotconnection.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiPresenter;

import java.util.ArrayList;
import java.util.List;

public class WifiScanReceiver extends BroadcastReceiver {

    private IListWifiPresenter presenter = null;
    private WifiManager wifiManager = null;

    public WifiScanReceiver(IListWifiPresenter presenter, WifiManager wifiManager) {
        this.presenter = presenter;
        this.wifiManager = wifiManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        List<ScanResult> scanList = wifiManager.getScanResults();
        Log.i(Constants.LOG_TAG, "Wifi connections: " + scanList.size());
        if (scanList.size() < 1) {
            presenter.showEmpty();
        } else {

            filterDuplicates(scanList);
            Log.i(Constants.LOG_TAG, "Wifi filtered connections: " + scanList.size());
            presenter.showList(scanList);
        }
        presenter.hideLoading();
    }

    private void filterDuplicates(List<ScanResult> list) {

        List<ScanResult> listToRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                ScanResult sr = list.get(j);
                if (list.get(i).SSID.equals(sr.SSID)) {
                    listToRemove.add(sr);
                }
            }
        }
        list.removeAll(listToRemove);
    }
}
