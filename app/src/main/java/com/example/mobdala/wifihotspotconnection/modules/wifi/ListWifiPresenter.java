package com.example.mobdala.wifihotspotconnection.modules.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiPresenter;
import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiView;
import com.example.mobdala.wifihotspotconnection.utils.Constants;
import com.example.mobdala.wifihotspotconnection.utils.Utils;
import com.example.mobdala.wifihotspotconnection.utils.WifiConnectionReceiver;
import com.example.mobdala.wifihotspotconnection.utils.WifiScanReceiver;

import java.util.List;

public class ListWifiPresenter implements IListWifiPresenter {

    private IListWifiView view = null;
    private WifiManager wifiManager = null;
    private WifiScanReceiver ScanReceiver = null;

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
    public void unregisterReceiver() {

        view.getContextActivity().unregisterReceiver(ScanReceiver);
    }

    @Override
    public void getWifis() {

        try {

            view.showLoading();
            wifiManager = (WifiManager) view.getContextActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Utils.enableWifi(wifiManager);
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            ScanReceiver = new WifiScanReceiver(this, wifiManager);
            view.getContextActivity().registerReceiver(ScanReceiver, filter);
            wifiManager.startScan();

        } catch (Throwable th) {
            Log.e(Constants.LOG_TAG, "ListWifiPresenter::getWifis", th);
            view.showEmpty();
            view.hideLoading();
        }
    }

    @Override
    public void connectToWifi(ScanResult wifi) {

        try {

            Log.i(Constants.LOG_TAG, "Trying to connect to Wifi = " + wifi.SSID);

            Utils.enableWifi(wifiManager);

            if (Utils.isWifiAvailable(view.getContextActivity())) {

                showLoading();

                String networkSSID = wifi.SSID;
                String networkPass = Utils.getProperty("wifi.mobdala.pwd", view.getContextActivity());

                String security = Utils.getScanResultSecurity(wifi);

                WifiConfiguration conf = new WifiConfiguration();
                conf.SSID = "\"" + networkSSID + "\"";
                Utils.setupWifiSecurity(conf, security, networkPass);

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
                intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

                WifiConnectionReceiver connectionReceiver = new WifiConnectionReceiver(this, wifiManager, networkSSID);
                getContextActivity().registerReceiver(connectionReceiver, intentFilter);
                int netId = wifiManager.addNetwork(conf);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();

            } else {

                showWifiDisabledDialog();
            }

        } catch (Exception ex) {
            Log.e(Constants.LOG_TAG, "ListWifiPresenter::connectToWifi", ex);
        }
    }

    @Override
    public void showWifiDisabledDialog() {

        view.showWifiDisabledDialog();
    }
}