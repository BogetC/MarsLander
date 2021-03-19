package utils;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import helloandroid.m2dl.marslander.GameView;
import helloandroid.m2dl.marslander.MainActivity;

public class Sensors {
    private SensorManager sensorManager;
    private GameView gameView;
    private MainActivity mainActivity;

    private Sensor lightSensor;
    private Sensor accelerometerSensor;
    private float[] maxValuesLightSensor, minValuesLightSensor;
    private float lightSensorCover;


    public Sensors(SensorManager sensorManager, GameView gameView, MainActivity mainActivity) {
        this.sensorManager = sensorManager;
        this.gameView = gameView;
        this.mainActivity = mainActivity;
        this.maxValuesLightSensor = new float[]{0, 0, 0, 0, 0};
        this.minValuesLightSensor = new float[]{0, 0, 0, 0, 0};
        this.lightSensorCover = 0;
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

    private int getMaxPosition(float[] t) {
        if(t != null && t.length >= 0) {
            int max = 0;
            for(int i = 0 ; i < t.length ; i++) {
                if (t[i] > t[max]) {
                    max = i;
                }
            }
            return max;
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

    private void checkMinValueLightSensor(float value) {
        int maxPosition = getMaxPosition(minValuesLightSensor);
        if (value < minValuesLightSensor[maxPosition]) {
            minValuesLightSensor[maxPosition] = value;
        }
    }

    private void updateCoveredLightSensorValues(float value) {
        checkMinValueLightSensor(value);
    }

    public void updateLightSensor(float value) {
        checkMaxValueLightSensor(value);
        calcLightSensorCover(value);
        System.out.println("COVER : " + this.lightSensorCover + "%");
        if(isCoveredLightSensor(value)) {
            updateCoveredLightSensorValues(value);
        } else {
            mainActivity.startCounter();
        }
    }

    public boolean isCoveredLightSensor(float value) {
        return (value < getMaxValueLightSensor()*0.2);
    }

    public void calcLightSensorCover(float value) {
        float cover = 100 - (((value / getMaxValueLightSensor()) - getCoveredValueLightSensor()) * 100);
        this.lightSensorCover = (0 < cover && cover < 94) ? cover : (cover < 0 ? 0 : 94);
    }

    public float getCoveredValueLightSensor() {
        return minValuesLightSensor[getMaxPosition(minValuesLightSensor)];
    }

    public float getLightSensorCover() {
        return lightSensorCover;
    }

    public void updateAccelerometerSensor(float[] values) {
        float magicNumber = 1.5f;
        float x = values[0] * magicNumber;
        float y = values[1] * magicNumber;

        this.gameView.updateAcceleration((int) y, (int) x);
    }
}
