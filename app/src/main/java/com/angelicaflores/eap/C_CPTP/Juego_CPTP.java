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

import com.angelicaflores.eap.app1.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;
import com.angelicaflores.Utils.storeDataInLocalTxt;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Juego_CPTP extends AppCompatActivity {
    int curTime = 2000;

    Timer timer;

    long startTime, ellapsedTime;
    long estimatedTime;
    ArrayList<Integer> list = new ArrayList<Integer> ();

    private ImageView imageSwitcher;
    Button btn;
    final String exerciseId = "2";
    Context context;

    //MediaPlayer abucheo, aplauso;
    WebView wv;

    private boolean endFlag;
    private  int[] figures = {
            R.drawable.puerco, R.drawable.mujer, R.drawable.paleta, R.drawable.sol, R.drawable.helado,R.drawable.flor};
    private int[] gallery = {
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

        imageSwitcher = (ImageView) findViewById(R.id.imageSwitcher);
        imageSwitcher.setVisibility(View.INVISIBLE);
        imageSwitcher.setTag("0");

        //aplauso = MediaPlayer.create(this, R.raw.aplauso);
        //abucheo = MediaPlayer.create(this, R.raw.abucheo);

        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(onClick);

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();

        /*Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.*/

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

            storeDataInLocalTxt store = new storeDataInLocalTxt();
            store.saveData(list.toString(), context);
        }
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button2:
                    btn.setClickable(false);
                    ellapsedTime = System.currentTimeMillis() - startTime;

                    if (imageSwitcher.getTag() == "1") {
                        //aplauso.start();
                    } else if(imageSwitcher.getTag() == "0"){
                        //abucheo.start();
                    }

                    if(position != -1) {
                        int nCorrida = (int) Math.ceil((position + 1) / 6);

                        list.add(gallery[position]);
                        list.add(nCorrida);
                        list.add((int) ellapsedTime);
                        list.add(position);
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

                        /*totalTime = System.nanoTime() - startTime;
                        double ellapsed = (double)totalTime / 1000000000.0;
                        Log.d("TotalTime (ms) ", String.valueOf(ellapsed));*/

                            if (curTime == 2000 || curTime == 800) { //inicia feedback
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
                                    //startTime = System.currentTimeMillis();
                                    curTime = 750; // presentación de imagen
                                }
                            } else {
                                imageSwitcher.setVisibility(View.INVISIBLE);
                                if ((position + 1) % 6 == 0)
                                    curTime = 2000;
                                else
                                    curTime = 800;
                            }

                            if(position == gallery.length-1 && curTime == 2000){
                                endFlag = true;

                                wv = (WebView) findViewById(R.id.webView);

                                // mostrar gif de fuegos artificiales
                                wv.setVisibility(View.VISIBLE);
                                wv.loadUrl("file:///android_asset/gifs/index.html");
                                // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
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

            storeDataInLocalTxt store = new storeDataInLocalTxt();
            store.saveData(list.toString(), context);
        }
    };
}