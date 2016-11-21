package com.example.mobdala.wifihotspotconnection.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiPresenter;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class WifiConnectionReceiver extends BroadcastReceiver {

    private IListWifiPresenter presenter = null;
    private WifiManager wifiManager = null;
    private String wifiSSID = null;
    private boolean firstTime;

    public WifiConnectionReceiver(IListWifiPresenter presenter, WifiManager wifiManager, String wifiSSID) {
        this.presenter = presenter;
        this.wifiManager = wifiManager;
        this.wifiSSID = wifiSSID;
        this.firstTime = true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) presenter.getContextActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (networkInfo.isConnected()) {
//        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSSID().replace("\"", "").equals(wifiSSID)) {
                Log.i(Constants.LOG_TAG, "Wifi connected to: " + wifiSSID);
                presenter.getContextActivity().unregisterReceiver(this);
            }
        } else {
            if (firstTime) {
                firstTime = false;
                Toast.makeText(presenter.getContextActivity(), "Not connected to " + wifiSSID, Toast.LENGTH_SHORT).show();
            }
        }

        presenter.hideLoading();
    }
}
