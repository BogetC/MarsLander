package helloandroid.m2dl.marslander;

import android.content.Context;
import android.graphics.Canvas;
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
    private int i;

    private int aX;
    private int aY;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.rover = new Rover(new Position(50, 50), 50);
        this.gameThread = new GameThread(getHolder(), this);
        this.i = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.i++;
        this.rover.draw(canvas);

        Position newPosition = Physics.updatePosition(rover.getPosition(), rover.getSpeed(), aX, aY, 0.1f);
        Speed newSpeed = Physics.updateSpeed(rover.getSpeed(), aX, aY, 0.1f);

        rover.setPosition(new Position(newPosition.getX(), newPosition.getY()));
        rover.setSpeed(newSpeed);

        try {
            this.gameThread.sleep(1000/100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("test", "tezaeazest");
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void updateAcceleration(int x, int y) {
        this.aX = x * 10;
        this.aY = y * 10;
    }
}
