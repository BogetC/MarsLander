package utils;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import helloandroid.m2dl.marslander.GameView;

public class Sensors {
    private SensorManager sensorManager;
    private GameView gameView;

    private Sensor lightSensor;
    private Sensor accelerometerSensor;
    private float[] maxValuesLightSensor;
    private boolean canJump;
    private Handler handler;

    private Runnable setCanJump = () -> { canJump = true; };
    private long lastUpdateLightSensor;

    public Sensors(SensorManager sensorManager, GameView gameView) {
        this.sensorManager = sensorManager;
        this.gameView = gameView;
        this.maxValuesLightSensor = new float[]{0, 0, 0, 0, 0};
        this.lastUpdateLightSensor = 0;
        this.canJump = true;
        this.handler = new Handler();
    }

    public boolean canJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public Sensor getAccelerometerSensor() {
        return accelerometerSensor;
    }

    public void setAccelerometerSensor(Sensor accelerometerSensor) {
        this.accelerometerSensor = accelerometerSensor;
    }

    public Sensor getLightSensor() {
        return lightSensor;
    }

    public void setLightSensor(Sensor lightSensor) {
        this.lightSensor = lightSensor;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    private int getMinPosition(float[] t) {
        if(t != null && t.length >= 0) {
            int min = 0;
            for(int i = 0 ; i < t.length ; i++) {
                if (t[i] < t[min]) {
                    min = i;
                }
            }
            return min;
        }
        return -1;
    }

    private float getMaxValueLightSensor() {
        return maxValuesLightSensor[getMinPosition(maxValuesLightSensor)];
    }


    private void checkMaxValueLightSensor(float value) {
        int minPosition = getMinPosition(maxValuesLightSensor);
        if (value > maxValuesLightSensor[minPosition]) {
            maxValuesLightSensor[minPosition] = value;
        }
    }

    public void updateLightSensor(float value) {
        checkMaxValueLightSensor(value);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateLightSensor > 1000) {
            lastUpdateLightSensor = System.currentTimeMillis();
        }
    }

    public void updateAccelerometerSensor(float[] values) {
        float magicNumber = 1.5f;
        float x = values[0] * magicNumber;
        float y = values[1] * magicNumber;

        this.gameView.updateAcceleration((int) y, (int) x);
    }
}
