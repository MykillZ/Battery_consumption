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
    private BluetoothScanner blueScanner;
    private MediaPlayerBackground mediaPlayer;
    private WifiScanner wifiScanner;
    private CheckBox bluetoothCheckBox;
    private CheckBox flashLightCheckBox;
    private CheckBox brightnessCheckBox;
    private CheckBox sensorCheckBox;
    private CheckBox locationCheckBox;
    private CheckBox mediaPlayerCheckBox;
    private CheckBox wifiScannerCheckBox;
    private LogManager logManager;
    private static int REQUEST_ENABLE_BT=50;
    private static int REQUEST_SCAN_ALWAYS_AVAILABLE = 2;
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
            @Override
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
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    location.enableLocation();
                } else {
                    location.disableLocation();
                }
            }
        });

        //use Bluetooth
        logManager.addLogger(Logger.getLogger("Bluetooth"));
        bluetoothCheckBox = (CheckBox) findViewById(R.id.bluetoothCheckBox);
        blueScanner = new BluetoothScanner(logManager.getLogger("Bluetooth"), this);
        bluetoothCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    blueScanner.activateScanner();
                }else{
                    blueScanner.deactivateScanner();
                }
            }
        });

        //MediaPlayer
        logManager.addLogger(Logger.getLogger("MediaPlayer"));
        mediaPlayerCheckBox = (CheckBox) findViewById(R.id.mediaPlayerCheckBox);
        mediaPlayer = new MediaPlayerBackground(Logger.getLogger("MediaPlayer"),this);
        mediaPlayerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mediaPlayer.playMovie();
                }else{
                    mediaPlayer.stopMovie();
                }
            }
        });

        //WifiScanner
        logManager.addLogger(Logger.getLogger("WifiScanner"));
        wifiScannerCheckBox = (CheckBox) findViewById(R.id.wifiScannercheckBox);
        wifiScanner = new WifiScanner(Logger.getLogger("WifiScanner"),this);
        wifiScannerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiScanner.startScan();
                }else{
                    wifiScanner.stopScan();
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
        if(bluetoothCheckBox.isChecked()){
            blueScanner.activateScanner();
        }
        if(mediaPlayerCheckBox.isChecked()){
            mediaPlayer.playMovie();
        }
        if(wifiScannerCheckBox.isChecked()){
            wifiScanner.startScan();
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
        if(bluetoothCheckBox.isChecked()){
            blueScanner.deactivateScanner();
        }
        if(mediaPlayerCheckBox.isChecked()){
            mediaPlayer.stopMovie();
        }
        if(wifiScannerCheckBox.isChecked()){
            wifiScanner.stopScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ENABLE_BT == requestCode){
            switch(resultCode){
                case Activity.RESULT_OK:
                    blueScanner.setEnable();
                    break;
                case Activity.RESULT_CANCELED:
                    blueScanner.setDisabled();
                    break;
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
