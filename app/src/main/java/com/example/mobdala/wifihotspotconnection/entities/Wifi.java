package com.example.mobdala.wifihotspotconnection.entities;

import android.net.wifi.ScanResult;

public class Wifi {

    private ScanResult scanResult = null;

    public Wifi() {
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }
}
