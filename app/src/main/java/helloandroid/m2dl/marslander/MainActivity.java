package helloandroid.m2dl.marslander;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import utils.Sensors;

public class MainActivity extends Activity implements SensorEventListener {

    private Sensors sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gameView = new GameView(this);
        setContentView(gameView);

        this.sensors = new Sensors(((SensorManager) getSystemService(Context.SENSOR_SERVICE)), gameView);
        this.sensors.setLightSensor(this.sensors.getSensorManager().getDefaultSensor(Sensor.TYPE_LIGHT));
        this.sensors.setAccelerometerSensor(this.sensors.getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        // register sensors
        this.sensors.getSensorManager().registerListener(
                this,
                this.sensors.getLightSensor(),
                SensorManager.SENSOR_DELAY_FASTEST
        );

        this.sensors.getSensorManager().registerListener(
                this,
                this.sensors.getAccelerometerSensor(),
                SensorManager.SENSOR_DELAY_FASTEST
        );


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // light sensor
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT :
                sensors.updateLightSensor(event.values[0]);
                break;
            case Sensor.TYPE_ACCELEROMETER :
                sensors.updateAccelerometerSensor(event.values);
                break;
            default :
                break;
        }
        // other sensors
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}