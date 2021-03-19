package helloandroid.m2dl.marslander;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

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

    private int screenWidth;
    private int screenHeight;

    private Context context;

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight + 70;


        int roverSize = 120;
        int xRover = screenWidth / 2 - roverSize / 2;
        int yRover = (int) (screenHeight / 1.61 - roverSize / 2);

        this.rover = new Rover(new Position(xRover, yRover), roverSize, context);
        this.gameThread = new GameThread(getHolder(), this);
        this.paused = true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
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
        this.aX = x * 10;
        this.aY = y * 10;
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
