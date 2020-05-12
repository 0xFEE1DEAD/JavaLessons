package com.example.surfaceviewlesson;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Rectangle extends Figure {
    int _w;
    int _h;

    Rectangle (int startX,
               int startY,
               Paint paint,
               int powerX,
               int powerY,
               int w,
               int h) {
        super(startX, startY, paint, powerX, powerY);
        _w = w;
        _h = h;
    }

    @Override
    public void renderMethod(Canvas canvas) {
        canvas.drawRect(_c_x, _c_y, _c_x + _w, _c_y + _h, _paint);
    }

    public int getWidth() {
        return _w;
    }

    public int getHeight() {
        return _h;
    }

    public void setCurrentX(int x) {
        _c_x = x;
    }

    public void setCurrentY(int y) {
        _c_y = y;
    }
}
