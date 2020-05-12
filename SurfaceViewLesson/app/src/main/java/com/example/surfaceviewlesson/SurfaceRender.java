package com.example.surfaceviewlesson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SurfaceRender  extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder _holder;
    Timer _timer;
    ArrayList<MagicBallon> _balloons;
    Rectangle rect;

    public SurfaceRender(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        _timer = new Timer();
        _holder = surfaceHolder;
        _balloons = new ArrayList<MagicBallon>();

        /*REFACTOR IT!!!*/
        int w = getWidth();
        int h = getHeight();
        Random r = new Random();
        for(int i = 0; i < 3; ++i) {
            int radius = 20 + r.nextInt(70);

            //startX
            int sx = r.nextInt(w);
            if(sx < radius) {
                sx = radius + 1;
            }
            if(sx > (w - radius)) {
                sx = w - radius - 1;
            }

            //startY
            int sy = r.nextInt(h);
            if(sy < radius) {
                sy = radius + 1;
            }
            if(sy > (h - radius)) {
                sy = h - radius - 1;
            }

            int px = 5 - r.nextInt(10);
            int py = 5 - r.nextInt(10);

            _balloons.add(new MagicBallon(sx, sy, radius, px, py));
        }
        /*REFACTOR IT!!!*/
        int rectangleW = 150;
        int rectangleH = 50;
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        rect = new Rectangle(
                w/2-rectangleW,
                h/2-rectangleH,
                p,
                5,
                5,
                rectangleW,
                rectangleH);

        TimerTask tsk = new TimerTask() {
            @Override
            public void run() {
                Canvas c = _holder.lockCanvas();
                if (c != null) {
                    surfaceRender(c);
                    _holder.unlockCanvasAndPost(c);
                }
            }
        };
        _timer.scheduleAtFixedRate(tsk, 0, 16);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        _timer.cancel();
        _timer.purge();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        int rw = rect.getWidth();
        int rh = rect.getHeight();

        int tox = x - rw/2;
        int toy = y - rh/2;

        rect.setCurrentX(tox);
        rect.setCurrentY(toy);

        return true;
    }

    void surfaceRender(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        rect.renderMethod(canvas);
        for(int i = 0; i < _balloons.size(); ++i) {
            MagicBallon fig1 = _balloons.get(i);
            fig1.renderMethod(canvas);
            fig1.computeTick();


            if(fig1.getCurrentY() >= (getHeight() - fig1.getRadius()) || fig1.getCurrentY() <= fig1.getRadius()) {
                fig1.reversYPower();
            }
            if(fig1.getCurrentX() >= (getWidth() - fig1.getRadius()) || fig1.getCurrentX() <= fig1.getRadius()) {
                fig1.reversXPower();
            }
            if(ColisionsHandler.BalloonVsRect(fig1, rect)) {
                fig1.reversYPower();
                fig1.reversXPower();
                fig1.changeColor();
            }

            for(int j = i + 1; j < _balloons.size(); ++j) {
                MagicBallon fig2 = _balloons.get(j);
                if(ColisionsHandler.BalloonVsBalloon(fig1, fig2)) {
                    MagicBallon.swapPower(fig1, fig2);
                }
            }

        }
        boolean endGame = false;
        for(int i = 0; i < _balloons.size() - 1; ++i) {
            if(_balloons.get(i).getColor() == _balloons.get(i + 1).getColor()) {
                endGame = true;
            } else {
                endGame = false;
                break;
            }
        }
        if(endGame) {
            Context context = getContext();
            Intent intent = new Intent(context, FinishLayoutActivity.class);
            context.startActivity(intent);
        }
    }
}
