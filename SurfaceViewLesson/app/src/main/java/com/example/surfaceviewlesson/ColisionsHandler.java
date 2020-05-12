package com.example.surfaceviewlesson;

import android.util.Log;

public class ColisionsHandler {
    static int computeDistance(int x, int y, int x2, int y2) {
        int X = x - x2;
        int Y = y - y2;
        return X*X + Y*Y;
    }

    static public boolean BalloonVsBalloon(MagicBallon fig1, MagicBallon fig2) {
        double r2 = fig1.getRadius() + fig2.getRadius();
        r2 *= r2;

        int X = (fig1.getCurrentX() - fig2.getCurrentX());
        int Y = (fig1.getCurrentY() - fig2.getCurrentY());

        return r2 >= (X * X + Y * Y);
    }

    static public boolean BalloonVsRect(MagicBallon b, Rectangle r) {
        int bx = b.getCurrentX();
        int by = b.getCurrentY();
        int ra = b.getRadius();

        int x = r.getCurrentX();
        int y = r.getCurrentY();
        int x2 = x + r.getWidth();
        int y2 = y + r.getHeight();

        boolean colXT1 = bx + ra >= x && bx + ra <= x2;
        boolean colXT2 = bx - ra <= x2 && bx - ra >= x;
        boolean colYT1 = by + ra >= y && by + ra <= y2;
        boolean colYT2 = by - ra <= y2 && by - ra >= y;

        return (colXT1 | colXT2) && (colYT1 | colYT2);
    }
}
