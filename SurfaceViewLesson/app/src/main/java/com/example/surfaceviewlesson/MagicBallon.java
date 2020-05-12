package com.example.surfaceviewlesson;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class MagicBallon {
    ArrayList<Integer> pallete;
    Paint _mypaint;
    Balloon b; //Пришлось агрегировать (((((: трэш

    MagicBallon(int startX, int startY, int radius, int powerX, int powerY) {
        _mypaint = new Paint();
        pallete = new ArrayList<Integer>();
        pallete.add(Color.BLUE);
        pallete.add(Color.MAGENTA);
        pallete.add(Color.CYAN);
        pallete.add(Color.RED);
        pallete.add(Color.YELLOW);
        pallete.add(Color.WHITE);
        pallete.add(Color.GREEN);
        changeColor();
        b = new Balloon(startX, startY, radius, _mypaint, powerX, powerY);
    }

    void renderMethod(Canvas c) {
        b.renderMethod(c);
    }

    void computeTick() {
        b.computeTick();
    }

    int getCurrentY() {
        return b.getCurrentY();
    }

    int getRadius() {
        return b.getRadius();
    }

    int getCurrentX() {
        return b.getCurrentX();
    }

    void reversYPower() {
        b.reversYPower();
    }

    void reversXPower() {
        b.reversXPower();
    }

    void changeColor() {
        Random r = new Random();
        int max = pallete.size();
        int col = pallete.get(r.nextInt(max));
        _mypaint.setColor(col);
    }

    int getColor() {
        return _mypaint.getColor();
    }

    static void swapPower(MagicBallon fig1, MagicBallon fig2) {
        Figure.swapPower(fig1.b, fig2.b);
    }
}
