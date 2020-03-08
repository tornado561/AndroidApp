package com.android.internal.util.a2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static MainActivity mainActivity = null;
    private TextView Score;
    public static int score = 0;//Aktualny wynik
    private TextView maxScore;
    private ImageView share;
    private Button restart;
    private Button back;
    private Button pause;
    private GameView gameView;

    public MainActivity() {
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Score = (TextView) findViewById(R.id.Score);
        maxScore = (TextView) findViewById(R.id.maxScore);
        maxScore.setText(getSharedPreferences("pMaxScore", MODE_PRIVATE).getInt("maxScore", 0) + ""); //domyślny tryb, w którym do utworzonego pliku można uzyskać dostęp tylko przez aplikację wywołującą (lub wszystkie aplikacje o tym samym identyfikatorze użytkownika)
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String s = "Moj wynik w grze “2048” to " + maxScore.getText();
                shareIntent.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(Intent.createChooser(shareIntent, "Pochwal sie"));
            }
        });
        gameView = (GameView)findViewById(R.id.gameView);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView.startGame();
            }
        });
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameView.hasTouched ) {
                    score = gameView.score;
                    showScore();
                    for(int y=0;y<4;++y) {
                        for(int x=0;x<4;++x) {
                            gameView.cards[y][x].setNum(gameView.num[y][x]);
                        }
                    }
                }
            }
        });
        pause = (Button)findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }



    // Wyczyść wynik
    public void clearScore() {
        score = 0;
//        SharedPreferences pref = getSharedPreferences("pMaxScore", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putInt("maxScore", 0);
//        editor.commit();
//        maxScore.setText(pref.getInt("maxScore", 0) + "");
        showScore();
    }

    //dodaj wynik
    public void addScore(int i) {

        score += i;
        showScore();
        SharedPreferences pref = getSharedPreferences("pMaxScore", MODE_PRIVATE);


        // Jeśli bieżący wynik przekracza najwyższy rekord, zaktualizuj go
        if (score > pref.getInt("maxScore", 0)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("maxScore", score);
            editor.commit();
            maxScore.setText(pref.getInt("maxScore", 0) + "");

        }

    }

    // Pokaż aktualny wynik
    public void showScore() {
        Score.setText(score + "");
    }

    @Override
    public void onBackPressed() {
        createExitTipDialog();
    }

    private void createExitTipDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Czy napewno chcesz to zrobic ?")
                .setTitle("Wskazowka")
                .setIcon(R.drawable.tip)
                .setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}
