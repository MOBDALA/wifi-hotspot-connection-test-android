package com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces;

import android.net.wifi.ScanResult;

import java.util.List;

public interface IListWifiView extends IView {

    void showList(List<ScanResult> items);

    void showEmpty();
}
