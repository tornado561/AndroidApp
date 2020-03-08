package com.android.internal.util.a2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;


// Kategoria karty (mały kwadrat)
public class Card extends FrameLayout {

    private int num = 0; // numer na karcie
    private TextView lable;
    public Card(Context context) {
        super(context);
        lable = new TextView(getContext());
        lable.setTextSize(32);
        lable.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(lable, lp);
        setNum(0);

    }

    public int getNum() {
        return num;
    }


    // Ustaw numer na karcie
    public void setNum(int num) {

        // Ustaw rozmiar liczby
        this.num = num;
        if (num > 0) {
            lable.setText(num + "");
        } else {
            lable.setText("");
        }

        // Zmień kolor tła karty zgodnie z wartością
        switch (num) {
            case 0: lable.setBackgroundColor(0xffccc0b2); break;
            case 2: lable.setBackgroundColor(0xffeee4da); break;
            case 4: lable.setBackgroundColor(0xffede0c8); break;
            case 8: lable.setBackgroundColor(0xfff2b179); break;
            case 16: lable.setBackgroundColor(0xfff59563); break;
            case 32: lable.setBackgroundColor(0xfff67c5f); break;
            case 64: lable.setBackgroundColor(0xfff65e3b); break;
            case 128: lable.setBackgroundColor(0xffedcf72); break;
            case 256: lable.setBackgroundColor(0xffedc750); break;
            case 512: lable.setBackgroundColor(0xffedc850); break;
            case 1024: lable.setBackgroundColor(0xffecc640); break;
            default: lable.setBackgroundColor(0xffedc22d); break;
        }

    }

    // Sprawdź, czy wartości na dwóch kartach są równe
    public boolean equals(Card card) {
        return getNum()==card.getNum();
    }

}
