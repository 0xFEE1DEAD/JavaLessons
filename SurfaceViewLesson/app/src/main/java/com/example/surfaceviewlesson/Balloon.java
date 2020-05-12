package com.example.surfaceviewlesson;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class Balloon extends Figure {
    protected int _radius;
    protected ArrayList<Integer> _colors;

    Balloon(int startX, int startY, int ballonRadius, Paint paint, int powerX, int powerY) {
        super(startX, startY, paint, powerX, powerY);
        _radius = ballonRadius;

        _colors = new ArrayList<>();
        _colors.add(Color.BLUE);
        _colors.add(Color.WHITE);
        _colors.add(Color.GREEN);
        _colors.add(Color.YELLOW);
        _colors.add(Color.RED);
        _colors.add(Color.CYAN);
        _colors.add(Color.MAGENTA);
    }

    @Override
    public void renderMethod(Canvas canvas) {
        canvas.drawCircle(_c_x, _c_y, _radius, _paint);
    }

    public void changeColor() {
        Random r = new Random();
        _paint.setColor(_colors.get(r.nextInt(_colors.size())));
    }

    public int getRadius() {
        return _radius;
    }
}
