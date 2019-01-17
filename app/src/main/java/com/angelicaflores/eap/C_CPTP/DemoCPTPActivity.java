package com.angelicaflores.eap.C_CPTP;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.angelicaflores.eap.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DemoCPTPActivity extends AppCompatActivity {
    int curTime = 2000;

    Timer timer;
    Boolean active = false;

    long startTime, ellapsedTime;
    long estimatedTime;
    ArrayList<Integer> list = new ArrayList<Integer> ();

    private ImageView imageSwitcher;
    Button BtnTerminar, btnMostrar, btnFin;
    String exerciseId;
    Context context;

    RelativeLayout LayoudPrincipal;
    //MediaPlayer abucheo, aplauso;

    private  int[] figures = {
            R.drawable.puerco, R.drawable.mujer, R.drawable.paleta, R.drawable.sol, R.drawable.helado,R.drawable.flor};
    private int[] gallery = {
            5,0,1,2,3,4, 1,5,4,0,2,3
    };

    private int position = 0;
    boolean firstFlag = true;

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

            btnMostrar = (Button) findViewById(R.id.button2);

            btnMostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMostrar.setClickable(false);

                    if (imageSwitcher.getTag() == "1") {
                        //aplauso.start();
                        list.add(gallery[position]);
                    } else if(imageSwitcher.getTag() == "0"){
                        //abucheo.start();
                        list.add(gallery[position]);
                    }

                    list.add((int) ellapsedTime);
                }
            });

            Bundle extras = getIntent().getExtras();
            if(extras != null)
                exerciseId = extras.getString("exerciseId"); //if it's a string you stored.

            startTimer();
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Intent i = new Intent(DemoCPTPActivity.this, ElegirEjercicioActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            position = 0;
            timer.cancel();
            finish();
        }

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

                                if (curTime == 2000 || curTime == 500) {
                                    btnMostrar.setClickable(true);
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
                                        curTime = 750;
                                    }
                                } else {
                                    imageSwitcher.setVisibility(View.INVISIBLE);
                                    if ((position + 1) % 6 == 0)
                                        curTime = 2000;
                                    else
                                        curTime = 500;
                                }

                                if(position == gallery.length-1 && curTime == 2000){
                                    cancel();
                                    position = 0;
                                    finish();
                               }

                            }
                        });
                    }
                }

            }, 0, 50);

        }
}