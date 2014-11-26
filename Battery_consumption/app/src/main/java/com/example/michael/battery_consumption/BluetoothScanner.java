package com.example.michael.battery_consumption;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/26/14.
 */
public class BluetoothScanner {
    private Logger bluetoothLogger;
    private BluetoothAdapter blue;
    private static int REQUEST_ENABLE_BT = 50;
    private boolean bluetoothEnabled = false;
    private ArrayList<String> mArrayAdapter;
    private BatteryMain main;

    public BluetoothScanner(Logger l, BatteryMain c){
        super();
        bluetoothLogger = l;
        blue = BluetoothAdapter.getDefaultAdapter();
        main = c;
    }

    public void activateScanner(){
        if(blue != null){
            if(!blue.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                main.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else {
                bluetoothEnabled = true;
            }

            if(bluetoothEnabled){
                // Register the BroadcastReceiver
                bluetoothLogger.log(Level.INFO,"Bluetooth enabled.");
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                main.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
                blue.startDiscovery();
            }

        }else{
            bluetoothLogger.log(Level.INFO, "The device has no Bluetooth");
        }
    }

    public void deactivateScanner(){
            if(bluetoothEnabled){
                main.unregisterReceiver(mReceiver);
                blue.disable();
                bluetoothEnabled = false;
                bluetoothLogger.log(Level.INFO, "Disable Bluetooth");
            }else{
                bluetoothLogger.log(Level.INFO, "Bluetooth is already disabled");
            }
    }

    public void setEnable(){
        bluetoothEnabled = true;
    }

    public void setDisabled(){
        bluetoothEnabled = false;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                bluetoothLogger.log(Level.INFO,"Found Bluetooth device: " + device.getName() +
                        " " + device.getAddress());
            }
        }
    };
}
