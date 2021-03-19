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

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.Random;

import elements.Rover;
import models.Position;
import models.Score;
import models.Speed;
import threads.GameThread;
import utils.Physics;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private Position roverPosition;
    private Rover rover;
    private GameThread gameThread;

    private boolean paused;

    private int aX;
    private int aY;

    private int waX;
    private int waY;

    private int screenWidth;
    private int screenHeight;

    private Context context;

    private int time;
    private final int maxAltitude = 10000;
    private int currentAltitude = this.maxAltitude;

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        this.waX = 0;
        this.waY = 0;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight + 70;



        int roverSize = 120;
        int xRover = screenWidth / 2 - roverSize / 2;
        int yRover = (int) (screenHeight / 1.61 - roverSize / 2);

        this.rover = new Rover(new Position(xRover, yRover), roverSize, context);
        this.gameThread = new GameThread(getHolder(), this);
        this.paused = true;
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

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            if (this.waX != 0) {
                Typeface tf = ResourcesCompat.getFont(getContext(), R.font.orbitron_medium);
                Paint paint = new Paint();
                paint.setTextSize(30);
                paint.setColor(Color.RED);
                paint.setTypeface(tf);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("CAUTION: WIND", this.screenWidth /2, 45, paint);
            }
            this.rover.draw(canvas);
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
                this.finishGame();
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
        this.gameThread.setRunning(false);
        Intent intent = new Intent(this.context, ScoreActivity.class);
        this.context.startActivity(intent);
    }

    public void crash() {
        this.gameThread.setRunning(false);
        Intent intent = new Intent(this.context, FailToLandActivity.class);
        this.context.startActivity(intent);
    }
}
