package com.angelicaflores.eap.E_ClavesCentrales;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.angelicaflores.eap.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GPS-003 on 28/07/2015.
 */
public class DemoClavesActivity extends AppCompatActivity {
    Button BtnDerecho;
    Button BtnIzquierdo;
    TextToSpeech t1;
    //MediaPlayer abucheo, aplauso;
    private ImageSwitcher imageSwitcher;
    private int[] gallery = {
            R.drawable.derecha,R.drawable.clavescruz,R.drawable.idelfin,R.drawable.clavescruz,
            R.drawable.izquierda,R.drawable.clavescruz,R.drawable.itortuga,R.drawable.clavescruz,
            R.drawable.izquierda,R.drawable.clavescruz,R.drawable.dabeja,R.drawable.clavescruz,R.drawable.clavescruz};
    private int position = 1;
    private Timer timer = null;

    //starTimer();
    long startTime, ellapsedTime;
    long estimatedTime;
    ArrayList<Integer> list = new ArrayList<Integer> ();
    String exerciseId;
    Context context;
    int curTime; // Curtime init time
    Boolean flag = false;

    int result = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claves_juego);
        BtnDerecho = (Button) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (Button) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);

        //aplauso = MediaPlayer.create(this,R.raw.aplauso);
        //abucheo = MediaPlayer.create(this,R.raw.abucheo);


        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(DemoClavesActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        startTimer();
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {

            ellapsedTime = System.currentTimeMillis() - startTime;
            int nCorrida = (int) Math.ceil( (position+1)/6 );

            switch (v.getId()) {
                case R.id.buttonD:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 11 || position==12) {
                        //aplauso.start();
                        result = 1;
                    } else if (position==3 || position==4 || position==7 || position==8) {
                        //abucheo.start();
                        result = 0;
                    } else {
                        //abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                    }

                    list.add(result);
                    list.add(nCorrida);
                    list.add((int) ellapsedTime);
                    break;

                case R.id.buttonI:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position==3 || position==4 || position==7 || position==8) {
                        //aplauso.start();
                        result = 1;
                    } else if (position == 11 || position==12) {
                        //abucheo.start();
                        result = 0;
                    } else {
                        //abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                    }

                    list.add(result);
                    list.add(nCorrida);
                    list.add((int) ellapsedTime);
                    break;
            }
        }
    };

    public void startTimer() {
        curTime = 750;
        //asignacion del tiempo para la visualizacion de las imagenes
        imageSwitcher.setImageResource(gallery[0]);

        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                estimatedTime += 50;
                if (estimatedTime == curTime) {
                    ellapsedTime = System.currentTimeMillis() - startTime;
                    //Log.d("ellapsed", String.valueOf(ellapsedTime));
                    Log.d("ellapsed", String.valueOf(estimatedTime));
                    estimatedTime = 0;

                    runOnUiThread(new Runnable() {
                        public void run() {

                            // Asignacion de tiempos de muestreo
                            if (curTime == 750 && flag == false) {
                                flag = true;
                                curTime = 500; // inicia cruz
                            } else if (curTime == 500) {
                                curTime = 750; // inicia estimulo

                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                                startTime = System.currentTimeMillis();
                            } else if (curTime == 750 && flag == true) {
                                flag = false;
                                curTime = 2000; // inicia feedback
                            } else if (curTime == 2000) {
                                curTime = 750; // inicia cue
                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                                startTime = System.currentTimeMillis();
                            }

                            imageSwitcher.setImageResource(gallery[position]);
                            position++;
                            if (position == 3 || position == 7 || position == 11) {
                                BtnDerecho.setEnabled(true);
                                BtnIzquierdo.setEnabled(true);
                            }

                            if (position == 13) {
                                timer.cancel();
                                finish();
                            }

                        }
                    });
                }
            }
        }, 0, 50);
    }
}
