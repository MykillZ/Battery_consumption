package com.example.michael.battery_consumption;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/26/14.
 */
public class WifiScanner {
    private Logger wifiLogger;
    private BatteryMain main;
    private WifiManager wManager;
    private List<ScanResult> rList;
    private WifiReceiver wReceiver;
    private boolean defaultWifiState;

    public WifiScanner(Logger wifiScanner, BatteryMain batteryMain) {
        wifiLogger = wifiScanner;
        main = batteryMain;
        wManager = (WifiManager) main.getSystemService(Context.WIFI_SERVICE);
        defaultWifiState = wManager.isWifiEnabled();
        wReceiver = new WifiReceiver();
    }

    public void startScan() {
        if(!wManager.isWifiEnabled()) {
            defaultWifiState = false;
            while(!wManager.setWifiEnabled(true)){
                wifiLogger.log(Level.INFO,"Try to activate WiFi");
            }
        }else{
            defaultWifiState = true;
        }
        main.registerReceiver(wReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wManager.startScan();
        wifiLogger.log(Level.INFO, "Start Wifi Scanner");
    }

    public void stopScan() {
        wifiLogger.log(Level.INFO, "Stop Wifi Scanner");
        wManager.setWifiEnabled(defaultWifiState);
        main.unregisterReceiver(wReceiver);
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
             rList = wManager.getScanResults();
            String resultList = "";
            for(int i=0; i<rList.size(); i++){
                resultList = resultList + rList.get(i).toString() + "\\n ";
            }
            wifiLogger.log(Level.INFO, "Scanresult: " + resultList);
        }
    }
}
