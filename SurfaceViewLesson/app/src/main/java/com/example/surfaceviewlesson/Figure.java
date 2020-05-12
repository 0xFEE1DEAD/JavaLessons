package com.example.surfaceviewlesson;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Figure {
    protected int     _c_x;
    protected int     _c_y;
    protected int     _power_y;
    protected int     _power_x;
    protected Paint _paint;

    Figure(int startX, int startY, Paint paint, int powerX, int powerY) {
        _c_x = startX;
        _c_y = startY;
        _paint = paint;
        _power_x = powerX;
        _power_y = powerY;
    }

    static void swapPower(Figure fig1, Figure fig2) {
        int fig1px = fig1._power_x;
        int fig1py = fig1._power_y;

        fig1._power_x = fig2._power_x;
        fig1._power_y = fig2._power_y;

        fig2._power_y = fig1py;
        fig2._power_x = fig1px;
    }


    public abstract void renderMethod(Canvas canvas);
    public void computeTick() {
        _c_x += _power_x;
        _c_y += _power_y;
    }
    public void reversYPower() {
        _power_y = -_power_y;
    }
    public void reversXPower() {
        _power_x = - _power_x;
    }

    public int getCurrentX() {
        return _c_x;
    }
    public  int getCurrentY() {
        return _c_y;
    }

}
