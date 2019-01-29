package com.angelicaflores.eap.B_Corsi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.angelicaflores.Utils.Constants;
import com.angelicaflores.eap.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;
import com.angelicaflores.Utils.storeDataInLocalTxt;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.angelicaflores.Utils.Constants.getExerciseHeader;

public class JuegoCorsiActivity extends AppCompatActivity
{

    Button ctrlBtn;
    String name;
    final String exerciseId = "5";
    private static Timer timer;
    int figId;
    ImageButton cuboBtn;
    ImageView semaphore;
    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;
    long startTime, ellapsedTime;
    String userData = getExerciseHeader(Integer.valueOf(exerciseId));
    Context context;
    WebView wv;

    int counter = 1; // manteniene el número de elemento de la serie mostrada durante cada estímulo
    int position = 0; // posición del elemento en el array de gallery
    int estimNum = 0; // número de estímulo
    int clickNum = 1;

    private boolean endFlag;

    private int[] estimElements = {2,2,3,4,5,6,7,8,9};

    private int[] gallery = {
            4,9,//
            6,2,//
            5,9,1,//
            3,7,2,5,//
            8,1,4,9,3,//
            2,5,7,1,3,9,
            6,1,2,9,7,3,5,
            7,3,5,2,6,8,9,1,
            1,5,7,2,9,3,4,8,6,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corsi_juego);
        context = this;
        TF = Typeface.createFromAsset(getAssets(), font_path);

        semaphore = findViewById(R.id.imgsemaforo);
        ctrlBtn = findViewById(R.id.ctrlBtn);
        ctrlBtn.setOnClickListener(onClick);
        ctrlBtn.setTypeface(TF, Typeface.BOLD);

        for(int i=1; i<=9; i++){
            name = "cubo" + i;
            figId = getResources().getIdentifier(name, "id", getPackageName());
            cuboBtn = findViewById(figId);
            cuboBtn.setOnClickListener(onClick);
            cuboBtn.setClickable(false);
        }

        SharedPreferences prefs = this.getSharedPreferences(
                Constants.prefsName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();

        /*Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.
    */
        //timer = new Timer();

        //Timer para imagen habilitar los botones
        /*countDownTimer = new CountDownTimer(29500, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.i("Time", Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
            }
        };*/

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

            storeDataInLocalTxt store = new storeDataInLocalTxt(context);
            store.saveData(userData, "");
        }
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {

                if (Integer.parseInt(String.valueOf(v.getTag())) != 0) {
                    ellapsedTime = System.currentTimeMillis() - startTime;
                    startTime = System.currentTimeMillis();
                    if (semaphore.getVisibility() == View.VISIBLE)
                        semaphore.setVisibility(View.INVISIBLE);

                    /*if(clickNum == 9) {
                        ctrlBtn.setVisibility(View.VISIBLE);
                    } else */if (clickNum == estimElements[estimNum]) {
                        ctrlBtn.setVisibility(View.VISIBLE);

                        for(int i=1; i<=9; i++){
                            name = "cubo" + i;
                            figId = getResources().getIdentifier(name, "id", getPackageName());
                            cuboBtn = findViewById(figId);
                            cuboBtn.setClickable(false);
                        }

                        estimNum++;
                    }

                    cuboBtn = findViewById(v.getId());
                    cuboBtn.setBackgroundResource(R.drawable.amarillo);
                    cuboBtn.setClickable(false);
                    clickNum++;

                    userData += Integer.parseInt(String.valueOf(v.getTag())) + ",";
                    userData += ((int) ellapsedTime) + ",\n";
                }

            switch (v.getId()) {
                case R.id.ctrlBtn:
                    if(ctrlBtn.getText().equals("Finalizar")) {
                        ctrlBtn.setClickable(false);

                        endFlag = true;
                        wv = findViewById(R.id.webView);

                        // mostrar gif de fuegos artificiales
                        wv.setVisibility(View.VISIBLE);
                        wv.loadUrl("file:///android_asset/gifs/index.html");
                        // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 3000);

                    } else if(ctrlBtn.getText().equals("Siguiente")) {

                        for(int i=1; i<=9; i++){
                            name = "cubo" + i;
                            figId = getResources().getIdentifier(name, "id", getPackageName());
                            cuboBtn = findViewById(figId);
                            cuboBtn.setBackgroundResource(R.drawable.azul);
                            cuboBtn.setClickable(false);
                        }

                        ctrlBtn.setVisibility(View.INVISIBLE);

                        startCorsiTimer();

                    } else {
                        ctrlBtn.setVisibility(View.INVISIBLE);
                        startCorsiTimer();
                    }

                    break;
            }
        }
    };

    public void startCorsiTimer() {
        timer = new Timer();
        clickNum = 1;

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                runOnUiThread(new Runnable() {
                    public void run() {

                        if(counter <= estimElements[estimNum]) {
                            name = "cubo" + gallery[position];
                            figId = getResources().getIdentifier(name, "id", getPackageName());
                            cuboBtn = findViewById(figId);
                            cuboBtn.setBackgroundResource(R.drawable.amarillo);
                            position++;
                            counter++;
                        } else {
                            counter = 1;

                            for(int i=1; i<=9; i++){
                                name = "cubo" + i;
                                figId = getResources().getIdentifier(name, "id", getPackageName());
                                cuboBtn = findViewById(figId);
                                cuboBtn.setBackgroundResource(R.drawable.azul);
                                cuboBtn.setClickable(true);
                            }

                            if (estimNum == 8) {
                                timer.cancel();
                                semaphore.setVisibility(View.VISIBLE);
                                ctrlBtn.setText("Finalizar");
                                startTime = System.currentTimeMillis();
                            } else {
                                timer.cancel();
                                semaphore.setVisibility(View.VISIBLE);
                                ctrlBtn.setText("Siguiente");
                                startTime = System.currentTimeMillis();
                            }


                        }

                    }
                });

            }

        }, 0, 500); //wait 500 to change background image of next cube

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(context, ElegirEjercicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            finish();

            storeDataInLocalTxt store = new storeDataInLocalTxt(context);
            store.saveData(userData, "");
        }
    };

}
