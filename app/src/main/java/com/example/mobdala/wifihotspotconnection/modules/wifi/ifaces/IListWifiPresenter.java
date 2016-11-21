package com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces;

import android.net.wifi.ScanResult;

public interface IListWifiPresenter extends IListWifiView {

    void unregisterReceiver();

    void getWifis();

    void connectToWifi(ScanResult wifi);
}
