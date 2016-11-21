package com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces;

public interface IListWifiPresenter extends IListWifiView {

    void getWifis();

    void unregisterReceiver();
}
