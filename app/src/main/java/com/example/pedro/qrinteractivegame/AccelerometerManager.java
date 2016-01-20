package com.example.pedro.qrinteractivegame;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AccelerometerManager {
    static SensorManager sensorManager;
    static Sensor sensor;
    private static Context aContext;
    private static AccelerometerListener listener;

    private static float threshold  = 15.0f;
    private static int interval     = 200;

    public static void set(float threshold, int interval) {
        AccelerometerManager.threshold = threshold;
        AccelerometerManager.interval = interval;
    }

    public static void reset() {
        AccelerometerManager.threshold = 15.0f;
        AccelerometerManager.interval = 200;
    }

    public static void startAccelerometerListening(Context context, AccelerometerListener accelerometerListener) {
        aContext = context;
        sensorManager = (SensorManager) aContext.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER);
        if(!new Boolean(sensors.size() > 0)) {
            Toast toast = Toast.makeText(aContext, "No accelerometer.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            sensor = sensors.get(0);
            //Toast toast = Toast.makeText(aContext, "Ready accelerometer.", Toast.LENGTH_SHORT);
            //toast.show();
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
            listener = accelerometerListener;
        }
    }

    public static void stopAccelerometerListening() {
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(aContext, "Accelerometer can not be stopped.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private static SensorEventListener sensorEventListener = new SensorEventListener() {

        private long now = 0;
        private long timeDiff = 0;
        private long lastUpdate = 0;
        private long lastShake = 0;

        private float x = 0;
        private float y = 0;
        private float z = 0;
        private float lastX = 0;
        private float lastY = 0;
        private float lastZ = 0;
        private float force = 0.0f;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent event) {
            now = event.timestamp;
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            if (lastUpdate == 0) {
                lastUpdate = now;
                lastShake = now;
                lastX = x;
                lastY = y;
                lastZ = z;
            } else {
                timeDiff = now/1000 - lastUpdate/1000;
                if (timeDiff > 10000) {
                    force = Math.abs(x + y + z - lastX - lastY - lastZ);
                    if (Float.compare(force, threshold) > 0) {
                        if (now/1000 - lastShake/1000 >= interval*100) {
                            Toast.makeText(aContext, "" + (now/1000 - lastShake/1000), Toast.LENGTH_SHORT).show();
                            listener.onShake(force);
                        }
                        lastShake = now;
                    }
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                    lastUpdate = now;
                }
            }
            listener.onAccelerationChanged(x, y, z);
        }
    };
}
