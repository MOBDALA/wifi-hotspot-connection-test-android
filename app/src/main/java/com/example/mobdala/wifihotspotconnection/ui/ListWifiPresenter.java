package com.example.mobdala.wifihotspotconnection.ui;

import android.net.wifi.ScanResult;

import com.example.mobdala.wifihotspotconnection.ui.ifaces.IListWifiPresenter;
import com.example.mobdala.wifihotspotconnection.ui.ifaces.IListWifiView;
import com.isupatches.wisefy.WiseFy;

import java.util.List;

public class ListWifiPresenter implements IListWifiPresenter {

    IListWifiView view = null;

    public ListWifiPresenter(IListWifiView view) {
        this.view = view;
    }

    // ---------------------------------------------------------------------------------------------

    // IListWifiPresenter

    // ---------------------------------------------------------------------------------------------

    @Override
    public void getWifis() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                view.showLoading();

                try {

                    WiseFy mWiseFy = new WiseFy.withContext(view.getContext()).logging(true).getSmarts();
                    List<ScanResult> nearbyAccessPoints = mWiseFy.getNearbyAccessPoints(true);
                    view.showList(nearbyAccessPoints);

                } catch (Throwable th) {

                    view.showEmpty();
                }

                view.hideLoading();
            }
        }).start();
    }
}
