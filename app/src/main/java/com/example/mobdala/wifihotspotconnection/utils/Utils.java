package com.example.mobdala.wifihotspotconnection.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    private static final String PSK = "PSK";
    private static final String WEP = "WEP";
    private static final String OPEN = "Open";

    public static String getProperty(String key, Context context) throws IOException {

        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("app.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }

    public static void enableWifi(WifiManager wifiManager) {

        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            Log.i(Constants.LOG_TAG, "Wifi is disable");
            Log.i(Constants.LOG_TAG, "Connecting Wifi...");
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * Get the security type of the wireless network
     *
     * @param scanResult the wifi scan result
     * @return one of WEP, PSK of OPEN
     */
    public static String getScanResultSecurity(ScanResult scanResult) {

        final String cap = scanResult.capabilities;
        final String[] securityModes = {WEP, PSK};
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                Log.i(Constants.LOG_TAG, "Wifi security type: " + securityModes[i]);
                return securityModes[i];
            }
        }
        return OPEN;
    }

    public static void setupWifiSecurity(WifiConfiguration conf, String security, String networkPass) {

        switch (security) {
            case WEP:
                conf.wepKeys[0] = "\"" + networkPass + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case PSK:
                conf.preSharedKey = "\"" + networkPass + "\"";
                break;
            case OPEN:
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
        }
    }

    public static boolean isWifiAvailable(Context context) {

        try {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            boolean wifiOk = activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean wifiOk = networkInfo.isConnected();
            Log.e(Constants.LOG_TAG, "Wifi available? " + wifiOk);
            return wifiOk;

        } catch (Exception ex) {
            Log.e(Constants.LOG_TAG, "Wifi no available", ex);
            return false;
        }
    }
}
