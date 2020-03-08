package com.android.internal.util.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;


// Kwadrat 4 × 4
public class GameView extends GridLayout {

    public static Card[][] cards = new Card[4][4];                 //4×4=16 kafelki
    private static List<Point> emptyPoints = new ArrayList<Point>();  // Pozycja pustej karty (wartość wynosi 0)
    public int num[][] = new int[4][4];//
    public int score;//
    public boolean hasTouched = false;

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }


    // Zainicjuj układ gry
    private void initGameView() {
        setRowCount(4);
        setColumnCount(4);
        setOnTouchListener(new Listener());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w, h)-10)/4; // Ustaw szerokość karty w oparciu o rozmiar ekranu
        addCards(cardWidth, cardWidth); // kwadrat
        startGame();

    }

    // Zainicjuj kartę
    private void addCards(int cardWidth, int cardHeight) {
        this.removeAllViews();
        Card c;
        for(int y=0;y<4;++y) {
            for(int x = 0;x<4;++x) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cards[x][y] = c;
            }
        }
    }

    // Następnie dodaj kartę, wartość wynosi 2 lub 4 (prawdopodobieństwo różni się między nimi)
    private static void addRandomNum() {
        emptyPoints.clear();//Ponownie zarejestruj puste pozycje karty
        for (int y = 0; y < 4; ++y) {
            for (int x = 0; x < 4; ++x) {
                if (cards[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

       // losowo ustaw wartość pustej karty na 2 lub 4 (współczynnik prawdopodobieństwa wynosi 9: 1)
        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size())); // tutaj zastepujemy wartosc domyslna 0 na 2 lub 4
        cards[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }


    // rozpocznij grę
    public static void startGame() {
        MainActivity.getMainActivity().clearScore();
        for(int y=0;y<4;++y) {
            for(int x=0;x<4;++x) {
                cards[x][y].setNum(0);
            }
        }
        addRandomNum(); // 1 klocek
        addRandomNum(); // 2 klocek

    }


    // przesuń w lewo
    private void swipeLeft() {
        boolean b = false;
      // Każda linia (odcięta to x, rzędna to y)
        for(int y=0;y<4;++y) {
      // Każda kolumna (biorąc pod uwagę, że nie trzeba porównywać ostatniej kolumny, dlatego potrzebne są tylko 3 kolumny)
            for(int x=0;x<3;++x) {
      // Porównaj wartości kart
                for(int x1=x+1;x1<4;++x1) {
                    // karta (x1, y) nie jest pusta, następnie porównaj z (x, y)
                    if (cards[x1][y].getNum()>0) {
                        // Karta (x, y) jest pusta, a następnie przesuń (x1, y) w lewo
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x1][y].getNum());
                            cards[x1][y].setNum(0);
                            --x; // (x1, y) musi kontynuować porównywanie
                            b = true;
                        } else if (cards[x][y].equals(cards[x1][y])) {
                            // Scal karty
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            b = true;
                        }
                        // napotkał niepustą kartę, a następnie (x, y) nie trzeba kontynuować porównywania
                        break;
                    }
                }
            }
        }
// Kiedy karta się zmieni, losowo dodaj kartę o wartości 2 lub 4, aby kontynuować grę
        if (b) {
            addRandomNum();
            checkGameOver(); // Za każdym razem, gdy dodajesz kartę o wartości 2 lub 4, musisz ustalić, czy gra się kończy
            winGame();
        }
    }


    // przesuń w prawo
    private void swipeRight() {
        boolean b = false;
        for(int y=0;y<4;++y) {
            for(int x=3;x>0;--x) {
                for(int x1=x-1;x1>=0;--x1) {
                    if (cards[x1][y].getNum()>0) {
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x1][y].getNum());
                            cards[x1][y].setNum(0);
                            ++x;
                            b = true;
                        } else if (cards[x][y].equals(cards[x1][y])) {
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            b = true;
                        }
                        break;
                    }
                }
            }
        }
        if (b) {
            addRandomNum();
            checkGameOver();
            winGame();
        }
    }

    //Przesuń w górę
    private void swipeUp() {
        boolean b = false;
        for(int x=0;x<4;++x) {
            for(int y=0;y<3;++y) {
                for(int y1=y+1;y1<4;++y1) {
                    if (cards[x][y1].getNum()>0) {
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x][y1].getNum());
                            cards[x][y1].setNum(0);
                            --y;
                            b = true;
                        } else if (cards[x][y].equals(cards[x][y1])) {
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            b = true;
                        }
                        break;
                    }
                }
            }
        }
        if (b) {
            addRandomNum();
            checkGameOver();
            winGame();
        }
    }

    //Przesuń w dół
    private void swipeDown() {
        boolean b = false;
        for(int x=0;x<4;++x) {
            for(int y=3;y>0;--y) {
                for(int y1=y-1;y1>=0;--y1) {
                    if (cards[x][y1].getNum()>0) {
                        if (cards[x][y].getNum()==0) {
                            cards[x][y].setNum(cards[x][y1].getNum());
                            cards[x][y1].setNum(0);
                            ++y;
                            b = true;
                        } else if (cards[x][y].equals(cards[x][y1])) {
                            cards[x][y].setNum(cards[x][y].getNum()*2);
                            cards[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cards[x][y].getNum());
                            b = true;
                        }
                        break;
                    }
                }
            }
        }
        if (b) {
            addRandomNum();
            checkGameOver();
            winGame();
        }
    }

    private void winGame(){
        boolean isOver = true;

        for(int y=0;y<4;++y) {
            for(int x=0;x<4;++x) {
                if (cards[x][y].getNum()==2048) {
                    if(isOver){
                        new AlertDialog.Builder(getContext()).setTitle("Gratulacje udało ci się wygrać! ").setMessage("\n" +
                                "Obecny wynik to "+MainActivity.score+"， Zagraj ponownie ！").setPositiveButton("\n" + " Kliknij tutaj, aby zagrać w kolejną rundę", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startGame();

                            }
                        }).show();
                    }

                }

            }
        }

    }

    private void checkGameOver() {
        boolean isOver = true;

        for(int y=0;y<4;++y) {
            for(int x=0;x<4;++x) {


                /*Warunki kontynuowania gry to:
                   1. Co najmniej jedna pusta karta
                   2.Nie ma pustych kart, ale są dwie sąsiednie karty o tej samej wartości.
                */
                if (cards[x][y].getNum()==0||
                        (x<3&&cards[x][y].getNum()==cards[x+1][y].getNum())||
                        (y<3&&cards[x][y].getNum()==cards[x][y+1].getNum())) {
                    // nie skończone, gra trwa
                    isOver = false;

                }

            }
        }
        //Koniec gry
        if (isOver ){
            new AlertDialog.Builder(getContext()).setTitle("Przepraszamy, gra się skończyła ").setMessage("\n" +
                    "Obecny wynik to "+MainActivity.score+"， Kontynuuj grę aby poprawić wynik ！").setPositiveButton("\n" + " Kliknij tutaj, aby zagrać w kolejną rundę", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startGame();

                }
            }).show();
        }
    }

    //Przesuwny monitor części gry
    class Listener implements View.OnTouchListener {

        private float startX, startY, offsetX, offsetY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (!hasTouched) {
                hasTouched = true;
            }

            score = MainActivity.score;

            for(int y=0;y<4;++y) {
                for(int x=0;x<4;++x) {
                    num[y][x] = cards[y][x].getNum();
                }
            }

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    offsetX = motionEvent.getX()-startX;
                    offsetY = motionEvent.getY()-startY;

                    if (Math.abs(offsetX)>Math.abs(offsetY)) {
                        if (offsetX<-5) {
                            swipeLeft();
                        } else if (offsetX>5) {
                            swipeRight();
                        }
                    } else {
                        if (offsetY<-5) {
                            swipeUp();
                        } else if (offsetY>5) {
                            swipeDown();
                        }
                    }

            }

            return true;

        }

    }

}