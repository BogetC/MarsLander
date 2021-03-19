package helloandroid.m2dl.marslander;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.Date;
import java.util.TimerTask;
import androidx.core.content.res.ResourcesCompat;

import java.util.Random;

import elements.Rover;
import models.Position;
import models.Score;
import models.Speed;
import threads.GameThread;
import utils.Physics;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private Rover rover;
    private Position roverPosition;
    private GameThread gameThread;

    private boolean paused;

    private int aX;
    private int aY;

    private int waX;
    private int waY;

    private int screenWidth;
    private int screenHeight;

    private Context context;

    private float thrust;
    private long startTime;
    private long endTime;
    private final float maxAltitude = 10000; // m
//    private float currentAltitude = this.maxAltitude; // m
    private Paint altitudePaint = new Paint();

    private float altitude;
    private float v0;
    private float a0;

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        this.waX = 0;
        this.waY = 0;

        this.altitude = 10000;
        this.v0 = 10;
        this.a0 = 10;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight + 70;

        int roverSize = 120;
        int xRover = screenWidth / 2 - roverSize / 2;
        int yRover = (int) (screenHeight / 1.6 - roverSize / 2);

        this.rover = new Rover(new Position(xRover, yRover), roverSize, context, this);
        this.gameThread = new GameThread(getHolder(), this);
        this.paused = true;

        Typeface tf = ResourcesCompat.getFont(this.context, R.font.orbitron_medium);
        altitudePaint.setTextSize(50);
        altitudePaint.setColor(Color.RED);
        altitudePaint.setTypeface(tf);
        altitudePaint.setTextAlign(Paint.Align.LEFT);
    }

    private Runnable resetWind = new Runnable() {
        @Override
        public void run() {
            setWindAcceleration(0, 0);
        }
    };

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.orbitron_medium);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.RED);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            if (this.waX != 0) {
                canvas.drawText("CAUTION: WIND", this.screenWidth /2, 45, paint);
            }

            if (!this.paused) {
                Log.d("1", String.valueOf(this.v0));
                if (this.thrust < 30) {
                    this.a0 = -20;
                } else {
                    this.a0 = 11;
                }
                this.altitude = (int) (this.altitude - this.v0 * 0.1 + 1/2 * this.a0 * Math.sqrt(0.1));
                if (this.altitude > this.maxAltitude) {
                    this.altitude = this.maxAltitude;
                }
                this.v0 = (int) (this.v0 + this.a0 * 0.1);
                Log.d("D", String.valueOf(altitude));
            }

            this.rover.draw(canvas);
            if (!this.paused) {
                this.altitude = this.altitude - 0.1F;
                if (this.altitude <= 0) {
                    if (this.v0 > -2000) {
                        this.finishGame();
                    } else {
                        this.crash();
                    }

                }
                canvas.drawText("Altitude: " + this.altitude + "m", 30, this.screenHeight - 100, altitudePaint);
            }
        }

        try {
            this.gameThread.sleep(1000/100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateRover() {
        if (!paused) {
            Position newPosition = Physics.updatePosition(rover.getPosition(), rover.getSpeed(), aX, aY, 0.1f);
            Speed newSpeed = Physics.updateSpeed(rover.getSpeed(), aX, aY, 0.1f);

            rover.setPosition(newPosition);
            rover.setSpeed(newSpeed);

            if (!this.rover.getPosition().isBetween(0, this.screenWidth - this.rover.getSize(), 0, this.screenHeight - this.rover.getSize())) {
                this.crash();
            }

            // Spawn wind
            float random = (float) Math.random();
            if (random < 0.005 && this.waX == 0) {
                Log.d("1", "GO");
                Random random2 = new Random();
                this.waX = random2.nextInt(15 - 10 + 1) + 10;
                this.waX = random2.nextInt(15 - 10 + 1) + 10;

                if (Math.random() < 0.5) {
                    this.waX = this.waX * -1;
                }

                if (Math.random() < 0.5) {
                    this.waY = this.waY * -1;
                }

                postDelayed(this.resetWind, 2000);
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setRunning(false);
    }

    public void updateAcceleration(int x, int y) {
        // Bring accelerations value in [10;20] range
        this.aX = x * 10 + this.waX;
        this.aY = y * 10 + this.waY;
    }

    public void setWindAcceleration(int x, int y) {
        this.waX = x;
        this.waY = y;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void finishGame() {
        this.endTime = new Date().getTime();
        long score = this.endTime - this.startTime;
        this.gameThread.setRunning(false);
        Intent intent = new Intent(this.context, ScoreActivity.class);
        intent.putExtra("score", score / 1000);
        this.context.startActivity(intent);
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void crash() {
        this.gameThread.setRunning(false);
        Intent intent = new Intent(this.context, FailToLandActivity.class);
        this.context.startActivity(intent);
    }

    public void setThrust(float thrust) {
        this.thrust = thrust;
    }

    public float getMaxAltitude() {
        return this.maxAltitude;
    }

    public float getAltitude() {
        return this.altitude;
    }
}
