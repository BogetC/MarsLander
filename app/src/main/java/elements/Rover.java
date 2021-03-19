package elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import models.Direction;
import models.Position;
import models.Speed;

public class Rover {
    private int size;

    private Position position;
    private Speed speed;

    public Rover(Position initialPosition, int size) {
        this.size = size;
        this.position = initialPosition;
        this.speed = new Speed(0, 0);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(this.position.getX(), this.position.getY(),
                this.position.getX() + this.size,
                this.position.getY() + this.size, paint
        );
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
        if (Math.abs(this.getSpeed().getX()) > Math.abs(this.getSpeed().getY())) {
            if (this.speed.getX() > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (this.speed.getY() > 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }
}
