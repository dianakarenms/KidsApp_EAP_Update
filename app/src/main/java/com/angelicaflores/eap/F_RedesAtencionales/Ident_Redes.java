package com.angelicaflores.eap.F_RedesAtencionales;

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

import com.angelicaflores.eap.app1.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Ident_Redes extends AppCompatActivity {

    Button BtnIzquierdo,BtnDerecho;
    ImageView manoDer, manoIzq;
    ImageSwitcher imageSwitcher;
    //MediaPlayer abucheo, aplauso;

    private int[] gallery = {
            2,R.drawable.arribaizquierdos,R.drawable.arribaizquierdoscirc,R.drawable.arribaizquierdoscirc,
            2,R.drawable.arribaizquierdo,R.drawable.arribaizquierdocirc,R.drawable.arribaizquierdocirc,
            2,R.drawable.abajoderechos,R.drawable.abajoderechoscirc,R.drawable.abajoderechoscirc,
            2,R.drawable.abajoderecho,R.drawable.abajoderechocirc,R.drawable.abajoderechocirc,R.drawable.fondoblanco};
    private int position = 1;
    private Timer timer = null;

    //starTimer variables
    long startTime, ellapsedTime;
    long estimatedTime;
    int curTime; // Curtime init time
    Boolean clickFlag = false,  flag = false, ranActive = true;;
    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_demo);

        //aplauso = MediaPlayer.create(this,R.raw.aplauso);
        //abucheo = MediaPlayer.create(this,R.raw.abucheo);

        BtnDerecho = (Button) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (Button) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);

        manoDer = (ImageView) findViewById(R.id.manoderimg);
        manoIzq = (ImageView) findViewById(R.id.manoizqimg);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(Ident_Redes.this);
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
                    if (position == 12 || position == 16) {
                        //aplauso.start();
                        result = 1;
                    } else if (position == 4 || position == 8) {
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
                    if (position == 4 || position == 8) {
                        //aplauso.start();
                        result = 1;
                    } else if (position == 12 || position == 16) {
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
                    Intent intentE = new Intent(Ident_Redes.this, ElegirEjercicioActivity.class);
                    startActivity(intentE);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        Intent i = new Intent(Ident_Redes.this, ElegirEjercicioActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);*/

        position = 1;
        timer.cancel();
        finish();
    }

    public void startTimer() {
        //asignacion del tiempo para la visualizacion de las imagenes
        imageSwitcher.setImageResource(gallery[0]);

        BtnDerecho.setClickable(false);
        BtnIzquierdo.setClickable(false);

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
                                curTime = 1300; // inicia cue types
                                ranActive = false;
                            } else if(curTime == 1300 && flag == false) {
                                curTime = 1300; // inicia fijacion
                                flag = true;
                            } else if(curTime == 1300 && flag == true) {
                                flag = false;
                                curTime = 5000; // inicia estimulo
                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                                startTime = System.currentTimeMillis();
                                if(position == 3 || position == 7)
                                    manoIzq.setVisibility(View.VISIBLE);
                                else
                                    manoDer.setVisibility(View.VISIBLE);
                            } else if (curTime == 5000 || clickFlag == true) {
                                curTime = (getRandom(0,24)*50)+400; // inicia feedback
                                clickFlag = false;
                                ranActive = true;
                                manoDer.setVisibility(View.INVISIBLE);
                                manoIzq.setVisibility(View.INVISIBLE);
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
                            if (position == gallery.length) {
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
