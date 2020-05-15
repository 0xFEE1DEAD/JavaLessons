package com.example.memorycanvas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

class Card {
    Paint p = new Paint();

    public Card(float x, float y, float width, float height, int color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int color, backColor = Color.DKGRAY;
    boolean isOpen = false; // цвет карты
    float x, y, width, height;
    public void draw(Canvas c) {
        // нарисовать карту в виде цветного прямоугольника
        if (isOpen) {
            p.setColor(color);
        } else p.setColor(backColor);
        c.drawRect(x,y, x+width, y+height, p);
    }
    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        } else return false;
    }

}

public class TilesView extends View {
    // пауза для запоминания карт
    final int PAUSE_LENGTH = 2; // в секундах
    boolean isOnPauseNow = false;

    // число открытых карт
    int openedCard = 0;

    ArrayList<Card> cards;
    Card firstOpenedCard;
    boolean cardsCoordsComputed;

    int width, height; // ширина и высота канвы

    public TilesView(Context context) {
        super(context);
    }

    protected ArrayList<Integer> getCardsColorsArray(int qPairs) {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);

        ArrayList<Integer> cardsColors = new ArrayList<>();

        Random r = new Random();

        for(int i = 0; i < qPairs && colors.size() > 0; ++i) {
            int nColor = r.nextInt(colors.size());
            int color = colors.get(nColor);
            cardsColors.add(color);
            cardsColors.add(color);
            colors.remove(nColor);
        }

        for(int i = 0; i < cardsColors.size(); ++i) {
            int posOne = r.nextInt(cardsColors.size());
            int posTwo = r.nextInt(cardsColors.size());

            int tempColor = cardsColors.get(posOne);
            cardsColors.set(posOne, cardsColors.get(posTwo));
            cardsColors.set(posTwo, tempColor);
        }

        return cardsColors;
    }

    protected ArrayList<Card> computeCoordsCards(
            ArrayList<Integer> cardsColors,
            int offsets,
            int cardWidth,
            int cardHeight,
            int fieldWidth
    ) {
        ArrayList<Card> cards_ = new ArrayList<>();
        int fullCardWidth = (cardWidth + offsets);
        int qCardsPerRow = (fieldWidth / fullCardWidth);
        qCardsPerRow = qCardsPerRow < cardsColors.size() ? qCardsPerRow : cardsColors.size();

        int firstCardOffsetW = (fieldWidth - (qCardsPerRow * fullCardWidth)) / 2;

        int offsetWidth = offsets / 2 + firstCardOffsetW;
        int offsetHeight = offsets;

        for(int i = 0; i < cardsColors.size(); ++i) {
            if(i % qCardsPerRow == 0 && i > 0) {
                offsetWidth = offsets / 2 + firstCardOffsetW;
                offsetHeight += cardHeight + offsets;
            }
            cards_.add(new Card(offsetWidth, offsetHeight, cardWidth, cardHeight, cardsColors.get(i)));
            offsetWidth += cardWidth + offsets;
        }

        return cards_;
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cardsCoordsComputed = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        if(!cardsCoordsComputed) {
            cards = computeCoordsCards(
                    getCardsColorsArray(7),
                    50,
                    150,
                    200,
                    width
            );
            cardsCoordsComputed = true;
        }
        // 2) отрисовка плиток
        // задать цвет можно, используя кисть
        Paint p = new Paint();
        for (Card c: cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 3) получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // 4) определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            // палец коснулся экрана

            for (Card c: cards) {

                if (openedCard == 0) {
                    if (c.flip(x, y)) {
                        firstOpenedCard = c;
                        Log.d("mytag", "card flipped: " + openedCard);
                        openedCard ++;
                        invalidate();
                        return true;
                    }
                }

                if (openedCard == 1) {


                    // перевернуть карту с задержкой
                    if (c.flip(x, y)) {
                        openedCard ++;
                        // 1) если открылис карты одинакового цвета, удалить их из списка
                        // например написать функцию, checkOpenCardsEqual
                        if(firstOpenedCard.color == c.color) {
                            cards.remove(firstOpenedCard);
                            cards.remove(c);
                        }
                        // 2) проверить, остались ли ещё карты
                        // иначе сообщить об окончании игры
                        if(cards.size() == 0) {
                            Context context = getContext();
                            Intent intent = new Intent(context, FinishGameActivity.class);
                            context.startActivity(intent);
                        }

                        // если карты открыты разного цвета - запустить задержку
                        invalidate();
                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);
                        isOnPauseNow = true;
                        return true;
                    }
                }

            }
        }


         // заставляет экран перерисоваться
        return true;
    }

    public void newGame() {
        // запуск новой игры
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаём число секунд ожидания
            } catch (InterruptedException e) {}
            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно


        @Override
        protected void onPostExecute(Void aVoid) {
            for (Card c: cards) {
                if (c.isOpen) {
                    c.isOpen = false;
                }
            }
            openedCard = 0;
            isOnPauseNow = false;
            invalidate();
        }
    }
}