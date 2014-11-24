package com.example.michael.battery_consumption;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by michael on 11/22/14.
 */
public class MeasureSensors implements SensorEventListener{

    private SensorManager sManager;
    private Logger sensorLogger;
    private List<Sensor> availableSensors;

    MeasureSensors(SensorManager sm, Logger l){
        sManager = sm;
        sensorLogger = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorLogger.log(Level.INFO,"Sensor send " + event.sensor.getName());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        sensorLogger.log(Level.INFO,"Sensor " + sensor.getName() + " changed accuracy " + accuracy);
    }

    public void registerSensors(){
        availableSensors = sManager.getSensorList(Sensor.TYPE_ALL);
        Sensor s;
        for(int i=0; i< availableSensors.size();i++){
            s = availableSensors.get(i);
            sensorLogger.log(Level.INFO,"Add Sensor " + s.getName() + " Vendor: " + s.getVendor());
            sManager.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void unregisterSensors(){
        sManager.unregisterListener(this);
    }
}
