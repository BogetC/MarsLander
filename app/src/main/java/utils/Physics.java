package utils;

import models.Position;
import models.Speed;

public final class Physics {
    public static Position updatePosition(Position p, Speed s, int ax, int ay, float t) {
        int x1 = (int) (p.getX() + s.getX() * t + 1/2 * ax * Math.sqrt(t));
        int y1 = (int) (p.getY() + s.getY() * t + 1/2 * ay * Math.sqrt(t));

        return new Position(x1, y1);
    }

    // This is sketchy but it works
    public static Speed updateSpeed(Speed s, int ax, int ay, float t) {
        int Vx = (int) (s.getX() + ax * t);
        int Vy = (int) (s.getY() + ay * t);

        return new Speed(Vx, Vy);
    }
}