package com.example.mobdala.wifihotspotconnection.ui.ifaces;

import android.net.wifi.ScanResult;

import java.util.List;

public interface IListWifiView extends IView {

    void showList(List<ScanResult> items);

    void showEmpty();
}
