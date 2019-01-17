package com.angelicaflores.eap.D_Flanker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.angelicaflores.eap.app1.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;
import com.angelicaflores.Utils.storeDataInLocalTxt;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class JuegoFlankerActivity extends AppCompatActivity {

    ImageButton BtnDerecho,BtnIzquierdo;
    TextToSpeech t1;
    RelativeLayout LayoudPrincipal;
    Button BtnSalir;
    //MediaPlayer abucheo, aplauso;
    private ImageSwitcher imageSwitcher;

    //StartTimer variables
    long startTime, ellapsedTime;
    long estimatedTime;
    final String exerciseId = "3";
    Context context;
    int curTime; // Curtime init time
    //Boolean flag = false;
    Boolean clickFlag = false; // bandera para determinar si se ya se dio la respuesta durante el tiempo de 5seg de estímulo
    ArrayList<Integer> list = new ArrayList<Integer> ();
    int result = 0;

    int position = 1;
    private Timer timer = null;
    private boolean endFlag;

    WebView wv;

    private  int[] figures = {R.drawable.semaforo,R.drawable.flankerscruz,R.drawable.estrella,R.drawable.triangulo,
                                R.drawable.estrellas,R.drawable.triangulos};
    private int[] gallery = {
            0,1,2,1, 0,1,2,1, 0,1,3,1, 0,1,3,1, 0,1,4,1, 0,1,2,1, 0,1,2,1, 0,1,5,1,0,1,4,1, 0,1,3,1,
            0,1,4,1, 0,1,5,1, 0,1,2,1, 0,1,5,1, 0,1,4,1, 0,1,4,1, 0,1,5,1, 0,1,3,1, 0,1,3,1, 0,1,2,1,
            0,1,5,1, 0,1,3,1, 0,1,3,1, 0,1,4,1, 0,1,5,1, 0,1,3,1, 0,1,2,1, 0,1,4,1, 0,1,5,1, 0,1,2,1,//21-30
            0,1,5,1, 0,1,4,1,1//31-32
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flanker_juego);

        context = this;

        BtnDerecho = (ImageButton) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (ImageButton) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);
        BtnSalir= (Button) findViewById(R.id.button5);
        BtnSalir.setOnClickListener(onClick);

        LayoudPrincipal=(RelativeLayout) findViewById(R.id.layoudPrincipal);

        //aplauso = MediaPlayer.create(this,R.raw.aplauso);
        //abucheo = MediaPlayer.create(this,R.raw.abucheo);

        //Metodo ocupado para pasar el texto de la instruccion a sonido.
        t1=new
                TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener()
                {
                    @Override
                    public void onInit ( int status){
                        if (status != TextToSpeech.ERROR) {
                            t1.setLanguage(new Locale(Locale.getDefault().getLanguage()));
                        }
                    }
                }
        );

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imageView = new ImageView(JuegoFlankerActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();

        /*Bundle extras = getIntent().getExtras();
        if(extras != null) {
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.

            Log.d("idflanker", exerciseId);
        }*/

        startTimer();
    }

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


    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {

            ellapsedTime = System.currentTimeMillis() - startTime;
            int nCorrida = (int) Math.ceil( (position+1)/4 );

            switch (v.getId()) {
                case R.id.buttonD:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 11 ||position == 12 || position == 15 || position == 16 ||position == 31 || position == 32 ||position == 39 || position == 40 ||position == 47 || position == 48 ||position == 55 || position == 56 ||position == 67 || position == 68 ||
                            position == 71 || position == 72 ||position == 75 || position == 76 ||position == 83 || position == 84 ||position == 87 || position == 88 ||position == 91 || position == 92 ||position == 99 || position == 100 ||position == 103 || position == 104 ||position == 115 || position == 116 ||position == 123 || position == 124) {
                        //abucheo.start();
                        result = 0;

                    }
                    if (position == 3 || position == 4 ||position == 7 || position == 8 ||position == 19 || position == 20 ||position == 23 || position == 24 ||position == 27 || position == 28 ||position == 35 || position == 36 ||position == 43 || position == 44 || position == 51 || position == 52 ||
                            position == 59 || position == 60 ||position == 63 || position == 64 ||position == 79 || position == 80 || position == 95 || position == 96 ||position == 107 || position == 108 ||position == 111 || position == 112 ||position == 119 || position == 120 ||position == 127 ||position == 128) {
                        //aplauso.start();
                        result = 1;
                    }

                    list.add(result);
                    list.add(nCorrida);
                    list.add((int) ellapsedTime);
                    list.add(gallery[position-1]);
                    Log.d("gallery[pos]", String.valueOf(gallery[position-1]));

                    clickFlag = true;
                    break;

                case R.id.buttonI:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if (position == 11 ||position == 12 || position == 15 || position == 16 ||position == 31 || position == 32 ||position == 39 || position == 40 ||position == 47 || position == 48 ||position == 55 || position == 56 ||position == 67 || position == 68 ||
                            position == 71 || position == 72 ||position == 75 || position == 76 ||position == 83 || position == 84 ||position == 87 || position == 88 ||position == 91 || position == 92 ||position == 99 || position == 100 ||position == 103 || position == 104 ||position == 115 || position == 116 ||position == 123 || position == 124) {
                        //aplauso.start();
                        result = 1;
                    }
                    if (position == 3 || position == 4 ||position == 7 || position == 8 ||position == 19 || position == 20 ||position == 23 || position == 24 ||position == 27 || position == 28 ||position == 35 || position == 36 ||position == 43 || position == 44 || position == 51 || position == 52 ||
                            position == 59 || position == 60 ||position == 63 || position == 64 ||position == 79 || position == 80 || position == 95 || position == 96 ||position == 107 || position == 108 ||position == 111 || position == 112 ||position == 119 || position == 120 ||position == 127 ||position == 128) {
                        //abucheo.start();
                        result = 0;
                    }

                    list.add(result);
                    list.add(nCorrida);
                    list.add((int) ellapsedTime);
                    list.add(gallery[position-1]);
                    Log.d("gallery[pos]", String.valueOf(gallery[position-1]));

                    clickFlag = true;
                    break;

                case R.id.button5:

                    finish();
                    Intent intentE = new Intent(JuegoFlankerActivity.this, ElegirEjercicioActivity.class);
                    startActivity(intentE);
                    break;
            }
        }
    };

    //asignacion del tiempo para la visualizacion de las imagenes
    public void startTimer() {
        curTime = 300;
        imageSwitcher.setImageResource(figures[gallery[0]]);

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
                                curTime = 2000; // inicia interestimulo
                                clickFlag = false;
                                BtnDerecho.setClickable(false);
                                BtnIzquierdo.setClickable(false);

                            } else if (position%4 == 0 && clickFlag == false) {
                                curTime = 300; // inicia semaforo
                                BtnDerecho.setVisibility(View.INVISIBLE);
                                BtnIzquierdo.setVisibility(View.INVISIBLE);
                            }

                            imageSwitcher.setImageResource(figures[gallery[position]]);
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

                            if (position == 129) {
                                timer.cancel();
                                BtnDerecho.setVisibility(View.INVISIBLE);
                                BtnIzquierdo.setVisibility(View.INVISIBLE);
                                //BtnSalir.setVisibility(View.VISIBLE);
                                imageSwitcher.setVisibility(View.INVISIBLE);

                                endFlag = true;

                                wv = (WebView) findViewById(R.id.webView);
                                // mostrar gif de fuegos artificiales
                                wv.setVisibility(View.VISIBLE);
                                wv.loadUrl("file:///android_asset/gifs/index.html");
                                // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
                                Handler handler = new Handler();
                                handler.postDelayed(runnable, 3000);
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
