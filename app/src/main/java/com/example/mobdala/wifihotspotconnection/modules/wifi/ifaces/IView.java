package com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces;

import android.app.Activity;

public interface IView {

    Activity getContextActivity();

    void showLoading();

    void hideLoading();
}
