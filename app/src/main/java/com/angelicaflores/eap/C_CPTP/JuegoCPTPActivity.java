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
import android.widget.ImageView;

import com.angelicaflores.Utils.Constants;
import com.angelicaflores.Utils.storeDataInLocalTxt;
import com.angelicaflores.eap.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.angelicaflores.Utils.Constants.getExerciseHeader;


public class JuegoCPTPActivity extends AppCompatActivity {
    int curTime = 800;

    private Timer timer;

    long startTime, ellapsedTime;
    long estimatedTime;

    private ImageView imageSwitcher;
    private final String exerciseId = "2";
    private String userData = getExerciseHeader(Integer.valueOf(exerciseId));
    private String computedData = "";// = getComputedDataHeader();
    private Context context;
    private HashMap<Integer, Integer> TR_Map = new HashMap<>();
    private int N_EComision = 0;

    private WebView wv;

    private boolean endFlag;
    private HashMap<Integer, String> figureNames = new HashMap<Integer, String>() {{
        put(0,"Cerdito");
        put(1,"Mujer");
        put(2,"Paleta");
        put(3,"Sol");
        put(4,"Helado");
        put(5,"Flor");
    }};
    private  int[] figures = {
            //           0                  1                 2                 3                4                5
            R.drawable.puerco, R.drawable.mujer, R.drawable.paleta, R.drawable.sol, R.drawable.helado,R.drawable.flor};
    private int[] gallery = {
            5,0,1,2,3,4, 1,5,4,0,2,3, 1,5,2,3,4,0, 5,3,1,0,2,4, 5,3,1,4,2,0,
            2,4,3,0,1,5, 2,4,1,3,5,0, 5,0,3,1,4,2, 4,0,2,3,5,1, 0,3,5,1,4,2,
            4,1,2,0,5,3, 2,5,4,3,0,1, 5,2,3,4,1,0, 1,4,0,3,5,2, 3,4,5,0,1,2,
            1,0,3,4,2,5, 0,3,1,4,5,2, 1,2,5,0,4,3, 1,4,3,2,0,5, 2,0,3,5,1,4,

            0,2,3,5,1,4, 0,5,4,1,2,3, 1,4,2,0,5,3, 5,0,2,4,3,1, 0,2,4,5,3,1,
            0,4,3,5,2,1, 4,1,5,0,2,3, 1,0,5,2,3,4, 2,5,3,0,4,1, 4,0,5,1,3,2,
            2,5,1,0,4,3, 4,0,2,1,3,5, 3,2,1,4,5,0, 4,0,2,1,3,5, 3,1,4,2,5,0,
            3,1,5,4,0,2, 4,2,3,1,5,0, 3,4,5,2,0,1, 5,4,3,0,2,1, 1,4,0,2,3,5,

            1,2,3,4,0,5, 5,1,0,2,4,3, 5,1,4,2,0,3, 2,3,0,1,5,4, 2,1,3,5,0,4,
            5,3,1,4,2,0, 4,2,3,5,1,0, 0,5,1,4,2,3, 4,1,5,2,3,0, 3,4,1,0,2,5,
            0,2,1,3,4,5, 4,1,5,0,2,3, 0,1,5,4,2,3, 5,3,2,0,1,4, 0,1,2,3,5,4,
            2,3,5,1,4,0, 1,2,4,3,5,0, 2,5,0,1,4,3, 1,0,4,2,3,5, 5,1,2,3,4,0
    };

    private int position = -1;
    private int nCorrida = 0;
    private int lastClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cptp_juego);
        context = this;

        imageSwitcher = findViewById(R.id.imageSwitcher);
        imageSwitcher.setVisibility(View.INVISIBLE);
        imageSwitcher.setTag("0");

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

            saveData();
        }
    }


    public void onClick(View v) {
        if(lastClickedPosition != position) {
            lastClickedPosition = position;
            ellapsedTime = System.currentTimeMillis() - startTime;

            if (position != -1) {
                userData += figureNames.get(gallery[position]) + ",";
                userData += nCorrida + ",";
                userData += (int) ellapsedTime + ",\n";

                if (imageSwitcher.getTag() == "1") {
                    // Picked Correct answer
                    TR_Map.put(nCorrida, (int) ellapsedTime); // substituted to position
                } else if (imageSwitcher.getTag() == "0") {
                    // Picked Wrong answer
                    N_EComision ++;
                }

            }
        }
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
                            if (curTime == 800 /*|| curTime == 2000*/) { //inicia feedback
                                startTime = System.currentTimeMillis();
                                if (position < gallery.length-1) {
                                    position++;

                                    if (position == gallery.length) // Prevents null pointer exception
                                        position = gallery.length - 1;

                                    imageSwitcher.setImageResource(figures[gallery[position]]);
                                    if (figures[gallery[position]] == R.drawable.puerco)
                                        imageSwitcher.setTag("1");
                                    else
                                        imageSwitcher.setTag("0");
                                    imageSwitcher.setVisibility(View.VISIBLE);
                                    curTime = 750; // presentación de imagen
                                }
                                nCorrida = (int) Math.ceil( ((double) position + 1.0)/6.0 );
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
                                //position = -1; removed from here and added into saveData to reset only after data computation is completed
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
            saveData();
        }
    };

    private void saveData() {
        Intent i = new Intent(context, ElegirEjercicioActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        finish();

        storeDataInLocalTxt store = new storeDataInLocalTxt(context);
        store.saveData(userData, computeData());

        position = -1;

    }

    private String computeData() {
        String testData = "NumEnsayo,TR,\n";
        float TRsum = 0, TRanswered = 0, N_EOmisión = 0;
        for(int i = 1; i <= nCorrida; i++) {
            // NumEnsayo,TR
            if(TR_Map.containsKey(i)) {
                testData += i + "," + TR_Map.get(i).toString() + ",\n";
                TRsum += TR_Map.get(i);
                TRanswered ++;
            } else {
                testData += i + ",0,\n";
                N_EOmisión ++;
            }
        }
        testData += "TRPromedio,N_Aciertos,N_EComisión,N_EOmisión,P_EComisión,P_EOmisión,NumEstimulos,\n";

        float TRPromedio = TRsum / TRanswered;
        testData += TRPromedio + ","; // TRPromedio
        testData += (int) TRanswered + ","; // N_Aciertos
        testData += N_EComision + ","; // N_EComisión
        testData += (int) N_EOmisión + ","; // N_EOmisión
        testData += "NA,"; // P_EComisión
        testData += ((N_EOmisión * 100) / nCorrida) + "%,"; // P_EOmisión -> % de errores de omisión en todas las corrida realizadas
        testData += position + 1;

        return testData;
    }
}