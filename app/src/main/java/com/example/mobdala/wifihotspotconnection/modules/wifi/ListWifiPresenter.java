package com.example.mobdala.wifihotspotconnection.modules.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiPresenter;
import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiView;
import com.example.mobdala.wifihotspotconnection.utils.Constants;
import com.example.mobdala.wifihotspotconnection.utils.WifiScanReceiver;

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
    public Activity getContextActivity() {
        return view.getContextActivity();
    }

    @Override
    public void showLoading() {
        view.showLoading();
    }

    @Override
    public void hideLoading() {
        view.hideLoading();
    }

    @Override
    public void showList(List<ScanResult> items) {
        view.showList(items);
    }

    @Override
    public void showEmpty() {
        view.showEmpty();
    }

    @Override
    public void getWifis() {

        try {

            view.showLoading();
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            WifiManager wifiManager = (WifiManager) view.getContextActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiScanReceiver receiver = new WifiScanReceiver(this, wifiManager);
            view.getContextActivity().registerReceiver(receiver, filter);
            wifiManager.startScan();
        } catch (Throwable th) {
            Log.e(Constants.LOG_TAG, "ListWifiPresenter::getWifis", th);
            view.showEmpty();
            view.hideLoading();
        }
    }
}
