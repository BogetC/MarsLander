package helloandroid.m2dl.marslander;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import utils.Sensors;

public class MainActivity extends Activity implements SensorEventListener {
    private Handler handler;
    private int counter_time;
    TextView counterTV;
    View menuLayout;
    private Sensors sensors;
    private GameView gameView;
    private Runnable count = () -> {
        this.counter_time--;
        this.counterTV.setText(String.valueOf(this.counter_time));

        if (this.counter_time == 0) {
            this.fadeMenu();
        } else {
            this.handler.postDelayed(this.count, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.gameView = new GameView(this);
        setContentView(R.layout.activity_main);

        FrameLayout gameLayout = (FrameLayout) findViewById(R.id.app_layout);
        gameLayout.addView(gameView);

        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        menuLayout.bringToFront();
        this.handler = new Handler();
        this.counterTV = (TextView) findViewById(R.id.counter_text_view);
        this.menuLayout = (View) findViewById(R.id.menu_layout);
        this.startCounter();
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

    public void startCounter() {
        this.counter_time = getResources().getInteger(R.integer.counter_max);
        this.counterTV.setText(String.valueOf(this.counter_time));
        this.handler.postDelayed(this.count, 1000);
    }

    public void startGame() {
        this.gameView.setPaused(false);
    }

    private void fadeMenu() {
        this.menuLayout.animate()
                .alpha(0.0f)
                .setDuration(getResources().getInteger(R.integer.fade_out_duration))
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.GONE);
                startGame();
            }
        });
    }
}