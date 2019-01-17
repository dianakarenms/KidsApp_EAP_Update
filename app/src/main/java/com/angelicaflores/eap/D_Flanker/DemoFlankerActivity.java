package com.angelicaflores.eap.D_Flanker;

/**
 * Created by GPS-003 on 27/07/2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.angelicaflores.eap.app1.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DemoFlankerActivity extends AppCompatActivity {

    ImageButton BtnDerecho;
    ImageButton BtnIzquierdo;
    TextToSpeech t1;
    //MediaPlayer abucheo, aplauso;

    long startTime, ellapsedTime;
    long estimatedTime;
    ArrayList<Integer> list = new ArrayList<Integer> ();
    String exerciseId;
    Context context;
    int curTime; // Curtime init time
    Boolean flag = false;
    Boolean clickFlag = false; // bandera para determinar si se ya se dio la respuesta durante el tiempo de 5seg de estímulo

    private ImageSwitcher imageSwitcher;
    private int[] gallery = {R.drawable.semaforo, R.drawable.flankerscruz, R.drawable.triangulos, R.drawable.flankerscruz,
            R.drawable.semaforo, R.drawable.flankerscruz, R.drawable.estrellas, R.drawable.flankerscruz,
            R.drawable.semaforo, R.drawable.flankerscruz, R.drawable.triangulo, R.drawable.flankerscruz,
            R.drawable.semaforo, R.drawable.flankerscruz, R.drawable.estrella, R.drawable.flankerscruz, R.drawable.flankerscruz};
    private int position = 1;
    private Timer timer = null;
    int result = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flanker_juego);

        context= this;
        BtnDerecho = (ImageButton) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (ImageButton) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);
        //aplauso = MediaPlayer.create(this,R.raw.aplauso);
        //abucheo = MediaPlayer.create(this,R.raw.abucheo);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.

        imageSwitcher=(ImageSwitcher) findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(DemoFlankerActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        startTimer();

    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {

            ellapsedTime = System.currentTimeMillis() - startTime;
            int nCorrida = (int) Math.ceil( (position+1)/6 );

            switch (v.getId()) {
                case R.id.buttonD:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 3 || position == 4 || position == 11 || position == 12) {
                        //abucheo.start();
                        result = 0;

                    }
                    if (position == 7 || position == 8|| position == 15 || position == 16) {
                        //aplauso.start();
                        result = 1;
                    }
                    clickFlag = true;
                    break;

                case R.id.buttonI:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 3 || position == 4 || position == 11 || position == 12) {
                        //aplauso.start();
                        result = 1;
                    }
                    if (position == 7 || position == 8|| position == 15 || position == 16) {
                        //abucheo.start();
                        result = 0;
                    }
                    clickFlag = true;
                    break;
            }
        }
    };

    //asignacion del tiempo para la visualizacion de las imagenes
    public void startTimer() {
        curTime = 300;
        imageSwitcher.setImageResource(gallery[0]);

        startTime = System.currentTimeMillis();
        //asignacion del tiempo para la visualizacion de las imagenes
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                estimatedTime += 50;
                if (estimatedTime == curTime || clickFlag == true) {
                    ellapsedTime = System.currentTimeMillis() - startTime;
                    //Log.d("ellapsed", String.valueOf(ellapsedTime));
                    Log.d("ellapsed", String.valueOf(estimatedTime));
                    estimatedTime = 0;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if ((position+3)%4 == 0) {
                                curTime = 300; // inicia cruz

                            } else if ((position+2)%4 == 0) {
                                curTime = 5000; // inicia estimulo

                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                                startTime = System.currentTimeMillis();

                            } else if ((position+1)%4 == 0 || clickFlag == true) {
                                curTime = 2000; // inicia feedback
                                clickFlag = false;

                            } else if (position%4 == 0 && clickFlag == false) {
                                curTime = 300; // inicia semaforo
                                BtnDerecho.setVisibility(View.INVISIBLE);
                                BtnIzquierdo.setVisibility(View.INVISIBLE);
                            }

                            imageSwitcher.setImageResource(gallery[position]);
                            position++;

                            if ((position + 1) % 4 == 0) {
                                BtnDerecho.setImageResource(R.drawable.estrellasola);
                                BtnIzquierdo.setImageResource(R.drawable.triangulo1);
                                BtnDerecho.setVisibility(View.VISIBLE);
                                BtnIzquierdo.setVisibility(View.VISIBLE);
                            } else {
                                BtnDerecho.setImageResource(0);
                                BtnIzquierdo.setImageResource(0);
                            }

                            if (position == 17) {
                                timer.cancel();
                                BtnDerecho.setVisibility(View.INVISIBLE);
                                BtnIzquierdo.setVisibility(View.INVISIBLE);
                                imageSwitcher.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        }
                    });
                }
            }
        }, 0, 50);
    }
}