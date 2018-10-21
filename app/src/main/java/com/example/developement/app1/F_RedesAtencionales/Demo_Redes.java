package com.example.developement.app1.F_RedesAtencionales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.developement.app1.R;
import com.example.developement.app1.menuElegirEjercicio.ElegirEjercicioActivity;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Demo_Redes extends AppCompatActivity {

    Button BtnIzquierdo,BtnDerecho;
    ImageSwitcher imageSwitcher;
    //MediaPlayer abucheo, aplauso;

    private int[] gallery = {
            R.drawable.puntocentral,R.drawable.central, 2,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.central, 2,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.doble, 2,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.doble, 2,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.abajo, 2,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.vacio, 2,R.drawable.abajoderecho,
            R.drawable.puntocentral,R.drawable.vacio, 2,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.arriba, 2,R.drawable.arribaderechos,R.drawable.fondoblanco};
    private int position = 1;
    private Timer timer = null;

    //starTimer variables
    long startTime;
    long estimatedTime;
    ArrayList<Integer> list = new ArrayList<Integer> ();
    int curTime; // Curtime init time
    Boolean clickFlag = false; // bandera para determinar si se ya se dio la respuesta durante el tiempo de 5seg de estímulo
    Boolean ranActive = true; //
    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_redes_instr);

        //aplauso = MediaPlayer.create(this,R.raw.aplauso);
        //abucheo = MediaPlayer.create(this,R.raw.abucheo);

        BtnDerecho = (Button) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (Button) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(Demo_Redes.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        curTime = (getRandom(0,24)*50)+400;
        startTimer();
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonD:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 12 || position == 24 || position==28  || position==32) {
                        //aplauso.start();
                        result = 1;
                    } else if (position == 4 || position == 8 || position==16 || position==20 ) {
                        //abucheo.start();
                        result = 0;
                    } else {
                        //abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                    }
                    clickFlag = true;
                    curTime = (int) (estimatedTime+300);
                    break;

                case R.id.buttonI:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 4 || position == 8 || position==16 || position==20 ) {
                        //aplauso.start();
                        result = 1;
                    } else if (position == 12 || position == 24 || position==28  || position==32) {
                        //abucheo.start();
                        result = 0;
                    } else {
                        //abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                    }
                    clickFlag = true;
                    curTime = (int) (estimatedTime+300);
                    break;

                case R.id.continuarBtn:
                    finish();
                    Intent intentE = new Intent(Demo_Redes.this, ElegirEjercicioActivity.class);
                    startActivity(intentE);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent i = new Intent(Demo_Redes.this, ElegirEjercicioActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);*/

        position = 0;
        timer.cancel();
        finish();
    }

    //asignacion del tiempo para la visualizacion de las imagenes
    public void startTimer() {
        imageSwitcher.setImageResource(gallery[0]);

        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                estimatedTime += 50;
                if (estimatedTime == curTime  || clickFlag == true) {
                    Log.d("ellapsed", String.valueOf(estimatedTime));
                    estimatedTime = 0;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Asignacion de tiempos de muestreo
                            if (ranActive == true && clickFlag == false) {
                                curTime = 150; // inicia cue types
                                ranActive = false;


                            } else if(curTime == 150)
                                curTime = 450; // inicia fijacion


                            else if(curTime == 450) {
                                curTime = 5000; // inicia estimulo
                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);


                            } else if (curTime == 5000 || clickFlag == true) {
                                curTime = (getRandom(0,24)*50)+400; // inicia feedback
                                clickFlag = false;
                                ranActive = true;
                            }

                            imageSwitcher.setImageResource(gallery[position]);
                            position++;

                            if (position%4 == 0) {
                                BtnDerecho.setEnabled(true);
                                BtnIzquierdo.setEnabled(true);
                            }
                            else {
                                BtnIzquierdo.setEnabled(false);
                                BtnDerecho.setEnabled(false);
                            }
                            if (position == 33) {
                                timer.cancel();
                                finish();
                            }

                        }
                    });
                }
            }
        }, 0, 50);
    }

    public static int getRandom(int from, int to) {
        if (from < to)
            return from + new Random().nextInt(Math.abs(to - from));
        return from - new Random().nextInt(Math.abs(to - from));
    }
}
