package com.example.michael.battery_consumption;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class BatteryMain extends Activity {
    private FlashLight light;
    private Brightness brightness;
    private MeasureSensors measureSensors;
    private Location location;
    private CheckBox flashLightCheckBox;
    private CheckBox brightnessCheckBox;
    private CheckBox sensorCheckBox;
    private CheckBox locationCheckBox;
    private LogManager logManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //initialiase log Manager
        logManager = LogManager.getLogManager();
        logManager.addLogger(Logger.getLogger("FlashLight"));

        //FlashLight
        flashLightCheckBox = (CheckBox) findViewById(R.id.flashLight);
        light = new FlashLight(logManager.getLogger("FlashLight"), this);
        flashLightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    light.flashLightOn();
                }else{
                    light.flashLightOff();
                }
            }
        });

        //MeasureSensors
        logManager.addLogger(Logger.getLogger("Sensors"));
        sensorCheckBox = (CheckBox) findViewById(R.id.sensorCheckBox);
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        measureSensors = new MeasureSensors(mSensorManager,logManager.getLogger("Sensors"));
        sensorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    measureSensors.registerSensors();
                } else {
                    measureSensors.unregisterSensors();
                }
            }
        });

        //Display Brightness
        logManager.addLogger(Logger.getLogger("Brightness"));
        brightnessCheckBox = (CheckBox) findViewById(R.id.brightness);
        brightness = new Brightness(this, logManager.getLogger("Brightness"));
        brightnessCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    brightness.changeBrightness(1);
                } else {
                    brightness.changeBrightness(0);
                }
            }
        });

        //use Location Services
        logManager.addLogger(Logger.getLogger("Location"));
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationCheckBox = (CheckBox) findViewById(R.id.locationCheckBox);
        location = new Location(locationManager,logManager.getLogger("Location"), this);
        locationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    location.enableLocation();
                } else {
                    location.disableLocation();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flashLightCheckBox.isChecked()){
            light.flashLightOn();
        }
        if(brightnessCheckBox.isChecked()){
            brightness.changeBrightness(1);
        }
        if(sensorCheckBox.isChecked()){
            measureSensors.registerSensors();
        }
        if(locationCheckBox.isChecked()){
            location.enableLocation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (flashLightCheckBox.isChecked()) {
            light.flashLightOff();
        }
        if (brightnessCheckBox.isChecked()) {
            brightness.changeBrightness(0);
        }
        if (sensorCheckBox.isChecked()) {
            measureSensors.unregisterSensors();
        }
        if (locationCheckBox.isChecked()) {
            location.disableLocation();
        }
    }
}
