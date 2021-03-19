package models;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addX(int distance) {
        this.x += distance;
    }

    public void addY(int distance) {
        this.y += distance;
    }

    public void substractX(int distance) {
        this.x -= distance;
    }

    public void subsctractY(int distance) {
        this.y -= distance;
    }

    public boolean isBetween(int x1, int x2, int y1, int y2) {
        return (this.x >= x1 && this.x <= x2) && (this.y >= y1 && this.y <= y2);
    }
}
