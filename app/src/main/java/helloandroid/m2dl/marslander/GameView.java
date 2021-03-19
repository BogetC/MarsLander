package helloandroid.m2dl.marslander;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

   private GameThread thread;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);

        setFocusable(true);
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
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            Rect dest = new Rect(100, 100, 600, 600);
            canvas.drawBitmap(bitmap, new Rect(0,0,500,500), dest, null);

            Paint paint = new Paint();
            paint.setColor(Color.rgb(250,0,0));
            canvas.drawRect(dest, paint);
        }
    }
}
