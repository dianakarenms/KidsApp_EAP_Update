package com.angelicaflores.eap.C_CPTP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.angelicaflores.Utils.Constants;
import com.angelicaflores.eap.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;
import com.angelicaflores.Utils.storeDataInLocalTxt;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.angelicaflores.Utils.Constants.getExerciseHeader;


public class JuegoCPTPActivity extends AppCompatActivity {
    int curTime = 800;

    Timer timer;

    long startTime, ellapsedTime;
    long estimatedTime;

    private ImageView imageSwitcher;
    Button btn;
    final String exerciseId = "2";
    String userData = getExerciseHeader(Integer.valueOf(exerciseId));
    Context context;

    WebView wv;

    private boolean endFlag;
    private  int[] figures = {
            R.drawable.puerco, R.drawable.mujer, R.drawable.paleta, R.drawable.sol, R.drawable.helado,R.drawable.flor};
    private int[] gallery = {
            5,0,1,2,3,4, 1,5,4,0,2,3, 1,5,2,3,4,0, 5,3,1,0,2,4, 5,3,1,4,2,0,
            2,4,3,0,1,5, 2,4,1,3,5,0, 5,0,3,1,4,2, 4,0,2,3,5,1, 0,3,5,1,4,2,
            4,1,2,0,5,3, 2,5,4,3,0,1, 5,2,3,4,1,0, 1,4,0,3,5,2, 3,4,5,0,1,2,
            1,0,3,4,2,5, 0,3,1,4,5,2, 1,2,5,0,4,3, 1,4,3,2,0,5, 2,0,3,5,1,4,

            5,0,1,2,3,4, 1,5,4,0,2,3, 1,5,2,3,4,0, 5,3,1,0,2,4, 5,3,1,4,2,0,
            2,4,3,0,1,5, 2,4,1,3,5,0, 5,0,3,1,4,2, 4,0,2,3,5,1, 0,3,5,1,4,2,
            4,1,2,0,5,3, 2,5,4,3,0,1, 5,2,3,4,1,0, 1,4,0,3,5,2, 3,4,5,0,1,2,
            1,0,3,4,2,5, 0,3,1,4,5,2, 1,2,5,0,4,3, 1,4,3,2,0,5, 2,0,3,5,1,4
    };

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cptp_juego);
        context = this;

        imageSwitcher = findViewById(R.id.imageSwitcher);
        imageSwitcher.setVisibility(View.INVISIBLE);
        imageSwitcher.setTag("0");

        btn = findViewById(R.id.button2);
        btn.setOnClickListener(onClick);

        SharedPreferences prefs = this.getSharedPreferences(
                Constants.prefsName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();
        startTimer();
    }

    @Override
    public void onBackPressed() {
        if(!endFlag) {
            timer.cancel();
            position = -1;

            Intent i = new Intent(context, ElegirEjercicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            finish();

            storeDataInLocalTxt store = new storeDataInLocalTxt(context);
            store.saveData(userData);
        }
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button2:
                    btn.setClickable(false);
                    ellapsedTime = System.currentTimeMillis() - startTime;

                    if(position != -1) {
                        int nCorrida = (int) Math.ceil((position + 1) / 6);

                        userData += gallery[position];
                        userData += nCorrida;
                        userData += (int) ellapsedTime;
                        userData += position;
                    }
                    break;
            }
        }
    };

    public void startTimer() {
        startTime = System.currentTimeMillis();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                estimatedTime += 50;
                if (estimatedTime == curTime) {
                    Log.d("ellapsed", String.valueOf(estimatedTime));
                    estimatedTime = 0;

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (curTime == 800 /*|| curTime == 2000*/) { //inicia feedback
                                btn.setClickable(true);
                                startTime = System.currentTimeMillis();
                                if (position < gallery.length-1) {
                                    position++;

                                    if (position == gallery.length)
                                        position = gallery.length - 1;

                                    imageSwitcher.setImageResource(figures[gallery[position]]);
                                    if (figures[gallery[position]] == R.drawable.puerco)
                                        imageSwitcher.setTag("1");
                                    else
                                        imageSwitcher.setTag("0");
                                    imageSwitcher.setVisibility(View.VISIBLE);
                                    curTime = 750; // presentación de imagen
                                }
                            } else {
                                // Time between stimulus
                                imageSwitcher.setImageResource(R.drawable.cruz_blanca);
                                curTime = 800;
                                /*imageSwitcher.setVisibility(View.INVISIBLE);
                                if ((position + 1) % 6 == 0)
                                    curTime = 2000;
                                else
                                    curTime = 800;*/
                            }

                            // End of exercise
                            if(position == gallery.length-1 && curTime == 800){
                                endFlag = true;
                                wv = findViewById(R.id.webView);
                                // mostrar gif de fuegos artificiales
                                wv.setVisibility(View.VISIBLE);
                                wv.loadUrl("file:///android_asset/gifs/index.html");
                                // esperar 3000ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
                                Handler handler = new Handler();
                                handler.postDelayed(runnable, 3000);

                                cancel();
                                position = -1;
                            }

                        }
                    });
                }
            }

        }, 0, 50);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(context, ElegirEjercicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            finish();

            storeDataInLocalTxt store = new storeDataInLocalTxt(context);
            store.saveData(userData.toString());
        }
    };
}