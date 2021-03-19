package elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import helloandroid.m2dl.marslander.R;
import models.Direction;
import models.Position;
import models.Speed;

public class Rover {
    private int size;

    private Position position;
    private Speed speed;

    private Bitmap bmpRover, bmpRoverSide, bmpRoverCorner;

    public Rover(Position initialPosition, int size, Context context) {
        this.size = size;
        this.position = initialPosition;
        this.speed = new Speed(0, 0);

        this.bmpRover = BitmapFactory.decodeResource(context.getResources(), R.drawable.rover);
        this.bmpRoverSide = BitmapFactory.decodeResource(context.getResources(), R.drawable.rover_side);
        this.bmpRoverCorner = BitmapFactory.decodeResource(context.getResources(), R.drawable.rover_corner);
    }

    public void draw(Canvas canvas) {
        Rect rect = new Rect(this.position.getX() - this.size/2,
                this.position.getY() - this.size/2,
                this.position.getX() + this.size/2,
                this.position.getY() + this.size/2);

        canvas.drawBitmap(bmpRover, new Rect(0,0,bmpRover.getWidth(),bmpRover.getHeight()), rect, null);

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
        float treshold = 0.0f;
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
        } else {
            return Direction.UP;
        }
    }

}
