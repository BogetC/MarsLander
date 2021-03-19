package elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import helloandroid.m2dl.marslander.R;
import models.Direction;
import models.Position;
import models.Speed;

public class Rover {
    private int size;

    private Position position;
    private Speed speed;

    private Bitmap bmpRover, bmpRoverSide, bmpRoverCorner;

    private List<Bitmap> bitmaps;
    public Rover(Position initialPosition, int size, Context context) {
        this.size = size;
        this.position = initialPosition;
        this.speed = new Speed(0, 0);

        this.bitmaps = new ArrayList<Bitmap>();


        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover0));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover1));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover2));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover3));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover4));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover5));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover6));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover7));
        bitmaps.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rover));
    }

    public void draw(Canvas canvas) {
        Rect rect = new Rect(this.position.getX(),
                this.position.getY(),
                this.position.getX() + this.size,
                this.position.getY() + this.size);

        Bitmap toDraw = null;
        switch (this.getDirection()) {
            case UP:
                toDraw = this.bitmaps.get(0);
                break;
            case UPRIGHT:
                toDraw = this.bitmaps.get(1);
                break;
            case RIGHT:
                toDraw = this.bitmaps.get(2);
                break;
            case DOWNRIGHT:
                toDraw = this.bitmaps.get(3);
                break;
            case DOWN:
                toDraw = this.bitmaps.get(4);
                break;
            case DOWNLEFT:
                toDraw = this.bitmaps.get(5);
                break;
            case LEFT:
                toDraw = this.bitmaps.get(6);
                break;
            case UPLEFT:
                toDraw = this.bitmaps.get(7);
                break;
            case NONE:
                toDraw = this.bitmaps.get(8);
                break;
            default:
                toDraw = bmpRover;
        }
        canvas.drawBitmap(toDraw, new Rect(0,0,toDraw.getWidth(),toDraw.getHeight()), rect, null);

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        float treshold = 30f;
        if (this.speed.getX() > treshold) {
            if (this.speed.getY() > treshold) {
                return Direction.DOWNRIGHT;
            } else if (this.speed.getY() < treshold * -1) {
                return Direction.UPRIGHT;
            } else {
                return Direction.RIGHT;
            }
        } else if (this.speed.getX() < treshold * -1) {
            if (this.speed.getY() > treshold) {
                return Direction.DOWNLEFT;
            } else if (this.speed.getY() < treshold * -1){
                return Direction.UPLEFT;
            } else {
                return Direction.LEFT;
            }
        } else if (this.speed.getY() > treshold) {
            return Direction.DOWN;
        } else if (this.speed.getY() < treshold * -1) {
            return Direction.UP;
        } else {
            return Direction.NONE;
        }
    }

}
