package helloandroid.m2dl.marslander;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;


import utils.Sensors;

public class MainActivity extends Activity implements SensorEventListener {
    private Handler handler;
    private int counter_time;
    private Sensors sensors;
    private GameView gameView;
    private CircularProgressBar circularProgressBar;
    private View menuLayout;

    private Runnable count = () -> {
        this.counter_time--;
        float counterMax = (float) getResources().getInteger(R.integer.counter_max);
        float progressPourcentage = ((counterMax - this.counter_time) / counterMax) * 100;
        this.circularProgressBar.setProgressWithAnimation(progressPourcentage, 1000l);

        if (this.counter_time == 0) {
            this.fadeMenu();
        } else {
            this.handler.postDelayed(this.count, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        this.gameView = new GameView(this, width, height);
        setContentView(R.layout.activity_main);

        FrameLayout gameLayout = (FrameLayout) findViewById(R.id.app_layout);
        gameLayout.addView(gameView);

        LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        menuLayout.bringToFront();
        this.handler = new Handler();
        this.menuLayout = (View) findViewById(R.id.menu_layout);
        this.circularProgressBar = findViewById(R.id.circularProgressBar);
//        this.startCounter();
        this.sensors = new Sensors(((SensorManager) getSystemService(Context.SENSOR_SERVICE)), gameView, this);
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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startCounter() {
        this.handler.removeCallbacks(this.count);
        this.counter_time = getResources().getInteger(R.integer.counter_max);
        this.circularProgressBar.setProgressWithAnimation(0, 100l);
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