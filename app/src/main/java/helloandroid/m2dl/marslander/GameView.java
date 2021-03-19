package helloandroid.m2dl.marslander;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private GameThread thread;
    private Context context;
    private Vibrator vibrator;
    private int i;


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        this.context = context;

        this.setOnTouchListener(this);

        thread = new GameThread(getHolder(), this);

        setFocusable(true);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        i = 0;

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        i = (i + 1) % 1000;
        }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(100);
            canvas.drawText(i + "", 100, 100, textPaint);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        vibrator.vibrate(VibrationEffect.createOneShot(i, VibrationEffect.DEFAULT_AMPLITUDE));
        return true;
    }
}
