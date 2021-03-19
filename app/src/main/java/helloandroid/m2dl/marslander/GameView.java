package helloandroid.m2dl.marslander;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import elements.Rover;
import models.Position;
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

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.rover = new Rover(new Position(50, 50), 120);
        this.gameThread = new GameThread(getHolder(), this);
        this.paused = true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            this.rover.draw(canvas);
            if (!paused) {
                Position newPosition = Physics.updatePosition(rover.getPosition(), rover.getSpeed(), aX, aY, 0.1f);
                Speed newSpeed = Physics.updateSpeed(rover.getSpeed(), aX, aY, 0.1f);

                rover.setPosition(newPosition);
                rover.setSpeed(newSpeed);
            }
        }

        try {
            this.gameThread.sleep(1000/100);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        try {
            gameThread.setRunning(false);
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateAcceleration(int x, int y) {
        this.aX = x * 10;
        this.aY = y * 10;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
