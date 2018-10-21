package com.example.developement.app1.F_RedesAtencionales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.developement.app1.R;
import com.example.developement.app1.menuElegirEjercicio.ElegirEjercicioActivity;
import com.example.developement.app1.storeDataInLocalTxt;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Juego_Redes extends AppCompatActivity {

    Button BtnIzquierdo,BtnDerecho,BtnPausa;
    ImageSwitcher imageSwitcher;
    MediaPlayer abucheo, aplauso;

    private boolean endFlag;
    private int[] gallery = {
            /** Bloque 1 **/
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoizquierdo,   //4
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoderecho,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoderecho,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoderechos,    //44
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaderecho,    //84
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoderecho,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaizquierdos,   //124
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoderecho,

            /** Bloque 2 **/
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoizquierdos,    //132
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoderecho,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaderechos,   //172
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaderechos,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoizquierdo,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaizquierdo,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaderecho,  //212
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoderecho,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaderecho,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.vacio, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.central, R. drawable.puntocentral,R.drawable.abajoderechos,
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoizquierdos,
            R.drawable.puntocentral,R.drawable.arriba, R. drawable.puntocentral,R.drawable.arribaizquierdos,
            R.drawable.puntocentral,R.drawable.abajo, R. drawable.puntocentral,R.drawable.abajoderecho, //252
            R.drawable.puntocentral,R.drawable.doble, R. drawable.puntocentral,R.drawable.abajoderecho,

            /** Bloque 3 **/

            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaizquierdos,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaizquierdos,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaizquierdos,

            /** Bloque 4 **/
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaizquierdos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaderecho,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoizquierdos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaizquierdos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaizquierdos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoderecho,
            R.drawable.puntocentral, R.drawable.central, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.arribaizquierdos,
            R.drawable.puntocentral, R.drawable.doble, R.drawable.puntocentral, R.drawable.abajoizquierdo,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.abajoderechos,
            R.drawable.puntocentral, R.drawable.arriba, R.drawable.puntocentral, R.drawable.arribaderechos,
            R.drawable.puntocentral, R.drawable.vacio, R.drawable.puntocentral, R.drawable.arribaizquierdo,
            R.drawable.puntocentral, R.drawable.abajo, R.drawable.puntocentral, R.drawable.abajoizquierdos,

            R.drawable.fondoblanco};

    private int position = 1;
    private Timer timer = null;

    //starTimer variables
    long startTime, ellapsedTime;
    long estimatedTime;
    ArrayList<Integer> list = new ArrayList<Integer> ();
    final String exerciseId = "6";
    Context context;
    int curTime; // Curtime init time
    Boolean ranActive = true;
    Boolean clickFlag = false; // bandera para determinar si se ya se dio la respuesta durante el tiempo de 5seg de estímulo
    int result = 0;

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_redes_gameplay);

        /*Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored. */

        context = this;

        aplauso = MediaPlayer.create(this,R.raw.aplauso);
        abucheo = MediaPlayer.create(this,R.raw.abucheo);

        BtnDerecho = (Button) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (Button) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);
        BtnPausa = (Button) findViewById(R.id.pausaBtn);
        BtnPausa.setOnClickListener(onClick);

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(Juego_Redes.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        imageSwitcher.setImageResource(gallery[0]);
        curTime = (getRandom(0,24)*50)+400;
        startTimer();
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {

            ellapsedTime = System.currentTimeMillis() - startTime;
            int nCorrida = (int) Math.ceil( (position+1)/4 );

            switch (v.getId()) {
                case R.id.buttonD:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);

                    // || position ==
                    if ( position == 8 || position == 12 || position == 16 || position == 20 || position == 24 || position == 28 || position == 40 || position == 44 || position == 48
                            || position == 56 || position == 64 || position == 80 || position == 84 || position == 104 || position == 120 || position == 128 || position == 136 || position ==152 || position ==156 || position ==160 || position ==172 || position ==180 || position ==184 || position ==188
                                    || position ==192 || position ==196 || position ==212 || position ==216 || position ==224 || position ==240 || position ==252 || position ==256
                                    || position ==264 || position ==268 || position ==272 || position ==276 || position ==280 || position ==288 || position ==292 || position ==296 || position ==300
                                    || position ==312 || position ==316 || position ==320 || position ==328 || position ==344 || position ==356 || position ==360 || position ==388 || position ==392
                                    || position ==412 || position ==416 || position ==420 || position ==432 || position ==444 || position ==448 || position ==456 || position ==468 || position ==472
                                    || position ==476 || position ==484 || position ==488 || position ==500 || position ==504 ) {
                        aplauso.start();
                        result = 1;
                    } else if ( position == 4 || position == 32 || position == 36 || position == 52 || position == 60 || position == 68 || position == 72 || position == 76 || position == 88
                                    || position == 92 || position == 96 || position == 100 || position == 108 || position == 112 || position == 116 || position == 124
                                    || position == 132  || position == 140 || position == 144  || position == 148  || position ==164 || position ==168 || position ==176
                                    || position ==200 || position ==204 || position ==208 || position ==220 || position ==228 || position ==232 || position ==236 || position ==244
                                    || position ==248 || position ==260 || position ==284 || position ==304 || position ==308 || position ==324 || position ==332 || position ==336 || position ==340
                                    || position ==348 || position ==352 || position ==364 || position ==368 || position ==372 || position ==376 || position ==380 || position ==384 || position ==396
                                    || position ==400 || position ==404 || position ==408 || position ==424 || position ==428 || position ==436 || position ==440 || position ==452 || position ==460
                                    || position ==464 || position ==480 || position ==492 || position ==496 || position ==508 || position ==512 ) {
                        abucheo.start();
                        result = 0;
                    } else {
                        abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                    }
                    clickFlag = true; //b
                    curTime = (int) (estimatedTime+300);

                    list.add(result);
                    list.add(nCorrida);
                    list.add((int) ellapsedTime);
                    Log.d("nCorrida", String.valueOf(nCorrida));
                    break;

                case R.id.buttonI:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if ( position == 4 || position == 32 || position == 36 || position == 52 || position == 60 || position == 68 || position == 72 || position == 76 || position == 88
                        || position == 92 || position == 96 || position == 100 || position == 108 || position == 112 || position == 116 || position == 124
                        || position == 132  || position == 140 || position == 144  || position == 148  || position ==164 || position ==168 || position ==176
                        || position ==200 || position ==204 || position ==208 || position ==220 || position ==228 || position ==232 || position ==236 || position ==244
                        || position ==248 || position ==260 || position ==284 || position ==304 || position ==308 || position ==324 || position ==332 || position ==336 || position ==340
                        || position ==348 || position ==352 || position ==364 || position ==368 || position ==372 || position ==376 || position ==380 || position ==384 || position ==396
                        || position ==400 || position ==404 || position ==408 || position ==424 || position ==428 || position ==436 || position ==440 || position ==452 || position ==460
                        || position ==464 || position ==480 || position ==492 || position ==496 || position ==508 || position ==512 ) {
                        aplauso.start();
                        result = 1;
                    } else if ( position == 8 || position == 12 || position == 16 || position == 20 || position == 24 || position == 28 || position == 40 || position == 44 || position == 48
                            || position == 56 || position == 64 || position == 80 || position == 84 || position == 104 || position == 120 || position == 128 || position == 136 || position ==152 || position ==156 || position ==160 || position ==172 || position ==180 || position ==184 || position ==188
                            || position ==192 || position ==196 || position ==212 || position ==216 || position ==224 || position ==240 || position ==252 || position ==256
                            || position ==264 || position ==268 || position ==272 || position ==276 || position ==280 || position ==288 || position ==292 || position ==296 || position ==300
                            || position ==312 || position ==316 || position ==320 || position ==328 || position ==344 || position ==356 || position ==360 || position ==388 || position ==392
                            || position ==412 || position ==416 || position ==420 || position ==432 || position ==444 || position ==448 || position ==456 || position ==468 || position ==472
                            || position ==476 || position ==484 || position ==488 || position ==500 || position ==504 ) {
                        abucheo.start();
                        result = 0;
                    } else {
                        abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                    }
                    clickFlag = true;
                    curTime = (int) (estimatedTime+300); // tiempo de espacio para reproducir sonido de feedback

                    list.add(result);
                    list.add(nCorrida);
                    list.add((int) ellapsedTime);
                    break;

                case R.id.pausaBtn:
                    imageSwitcher.setVisibility(View.VISIBLE);
                    BtnPausa.setVisibility(View.INVISIBLE);
                    startTimer();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(!endFlag) {
            position = 0;
            timer.cancel();

            Intent i = new Intent(context, ElegirEjercicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            finish();

            storeDataInLocalTxt store = new storeDataInLocalTxt();
            store.saveData(list.toString(), context);
        }
    }

    public void startTimer() {
        //asignacion del tiempo para la visualizacion de las imagenes

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
                            if ((position+3)%4 == 0 && clickFlag == false) {
                                curTime = 150; // inicia cue types
                                ranActive = false;
                            } else if((position+2)%4 == 0) {
                                curTime = 450; // inicia fijacion

                            } else if((position+1)%4 == 0) {
                                curTime = 5000; // inicia estimulo
                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                                startTime = System.currentTimeMillis();
                            } else if (position%4 == 0 || clickFlag == true) {
                                curTime = (getRandom(0,24)*50)+400; // inicia fijación


				                Log.d("ranNum", String.valueOf(curTime));; // inicia fijación
                                clickFlag = false;
                                ranActive = true;
                            }

                            imageSwitcher.setImageResource(gallery[position]);
                            position++;

                            if(position%4 == 0) {
                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                            }
                            else {
                                BtnIzquierdo.setClickable(false);
                                BtnDerecho.setClickable(false);
                            }

                            Log.i("position", String.valueOf(position));

                            Log.i("gallerylength", String.valueOf(gallery.length));

                            if (position == gallery.length) {
                                timer.cancel();
                                wv = (WebView) findViewById(R.id.webView);

                                // mostrar gif de fuegos artificiales
                                wv.setVisibility(View.VISIBLE);
                                wv.loadUrl("file:///android_asset/gifs/index.html");
                                // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
                                Handler handler = new Handler();
                                handler.postDelayed(runnable, 3000);
                            } else if((position-1)%128 == 0){
                                timer.cancel();
                                imageSwitcher.setVisibility(View.INVISIBLE);
                                BtnPausa.setVisibility(View.VISIBLE);
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
