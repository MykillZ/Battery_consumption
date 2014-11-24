package com.example.michael.battery_consumption;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/25/14.
 */
public class Location implements LocationListener {

    private LocationManager lm;
    private Logger locationLogger;
    private Context main;

    public Location(LocationManager pLocationManager, Logger l, Context c){
        lm = pLocationManager;
        locationLogger = l;
        main = c;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        locationLogger.log(Level.INFO, "Location changed to " + location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        locationLogger.log(Level.INFO, "Location Status changed " + status + " Provider " + provider.toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        locationLogger.log(Level.INFO, "Provider enabled " + provider.toString());
    }

    @Override
    public void onProviderDisabled(String provider) {
        locationLogger.log(Level.INFO, "Provider disabled " + provider.toString());
    }

    public void enableLocation(){
        String GPSProvider = Settings.Secure.getString(main.getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if("".equals(GPSProvider)){
            locationLogger.log(Level.INFO, "GPS disabled");
            Intent locationSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            main.startActivity(locationSettings);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }else{
            locationLogger.log(Level.INFO, "GPS enabled");
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    public void disableLocation(){
        lm.removeUpdates(this);
    }
}
