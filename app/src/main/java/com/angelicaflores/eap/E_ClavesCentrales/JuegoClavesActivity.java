package com.angelicaflores.eap.E_ClavesCentrales;

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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.angelicaflores.Utils.Constants;
import com.angelicaflores.Utils.storeDataInLocalTxt;
import com.angelicaflores.eap.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.angelicaflores.Utils.Constants.getExerciseHeader;

/**
 * Created by GPS-003 on 28/07/2015.
 */
public class JuegoClavesActivity extends AppCompatActivity {

    Button BtnDerecho;
    Button BtnIzquierdo;
    Button BtnSalir;
    TextToSpeech t1;
    RelativeLayout LayoudPrincipal;
    //MediaPlayer abucheo, aplauso;

    //starTimer variables
    long startTime, ellapsedTime;
    long estimatedTime;
    final String exerciseId = "4";
    String userData = getExerciseHeader(Integer.valueOf(exerciseId));
    Context context;
    int curTime; // Curtime init time
    Boolean flag = false;
    int result = 0;

    private boolean returnPressed;

    private int position = 1;
    private Timer timer = null;
    private boolean endFlag;

    WebView wv;

    private ImageSwitcher imageSwitcher;
    private int[] gallery = {
            /*EXPERIMENTO 1 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dabeja,R.drawable.clavescruz,//4
            /*EXPERIMENTO 2 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dballena,R.drawable.clavescruz,//8
            /*EXPERIMENTO 3 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.izorro,R.drawable.clavescruz,//12
            /*EXPERIMENTO 4 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dbuho,R.drawable.clavescruz,//20
            /*EXPERIMENTO 5 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dburro,R.drawable.clavescruz,//25
            /*EXPERIMENTO 6 IZQUIERDA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.itucan,R.drawable.clavescruz,//30
            /*EXPERIMENTO 7 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.itortuga,R.drawable.clavescruz,//35
            /*EXPERIMENTO 8 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.itigre,R.drawable.clavescruz,//40
            /*EXPERIMENTO 9 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dcamaleon,R.drawable.clavescruz,//45
            /*EXPERIMENTO 10 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dcangrejo,R.drawable.clavescruz,//40

            /*EXPERIMENTO 11 IZQUIERDA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.itiburon,R.drawable.clavescruz,//55
            /*EXPERIMENTO 12 DERECHA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.dcatarina,R.drawable.clavescruz,//60
            /*EXPERIMENTO 13 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iserpiente,R.drawable.clavescruz,//65
            /*EXPERIMENTO 14 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dcocodrilo,R.drawable.clavescruz,//70
            /*EXPERIMENTO 15 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dcolibri,R.drawable.clavescruz,//75
            /*EXPERIMENTO 16 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iraton,R.drawable.clavescruz,//80
            /*EXPERIMENTO 17 IZQUIERDA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.irana,R.drawable.clavescruz,//85
            /*EXPERIMENTO 18 IZQUIERDA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.ipulpo,R.drawable.clavescruz,//90
            /*EXPERIMENTO 19 DERECHA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.dconejo,R.drawable.clavescruz,//95
            /*EXPERIMENTO 20 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.ipinguino,R.drawable.clavescruz,//80

            /*EXPERIMENTO 21 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.ipez,R.drawable.clavescruz,//105
            /*EXPERIMENTO 22 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.ddelfin,R.drawable.clavescruz,//110
            /*EXPERIMENTO 23 IZQUIERDA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.iperico,R.drawable.clavescruz,//115
            /*EXPERIMENTO 24 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dgallo,R.drawable.clavescruz,//120
            /*EXPERIMENTO 25 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dguajolote,R.drawable.clavescruz,//125
            /*EXPERIMENTO 26 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.ipato,R.drawable.clavescruz,//130
            /*EXPERIMENTO 27 DERECHA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.dkoala,R.drawable.clavescruz,//135
            /*EXPERIMENTO 28 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dlibelula,R.drawable.clavescruz,//140
            /*EXPERIMENTO 29 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.imariposa,R.drawable.clavescruz,//145
            /*EXPERIMENTO 30 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iloro,R.drawable.clavescruz,//150

            /*EXPERIMENTO 31 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dloro,R.drawable.clavescruz,//155
            /*EXPERIMENTO 32 DERECHA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.dmariposa,R.drawable.clavescruz,//160
            /*EXPERIMENTO 33 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dpato,R.drawable.clavescruz,//165
            /*EXPERIMENTO 34 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.ilibelula,R.drawable.clavescruz,//170
            /*EXPERIMENTO 35 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.ikoala,R.drawable.clavescruz,//175
            /*EXPERIMENTO 36 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dperico,R.drawable.clavescruz,//180
            /*EXPERIMENTO 37 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iguajolote,R.drawable.clavescruz,//185
            /*EXPERIMENTO 38 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dpez,R.drawable.clavescruz,//190
            /*EXPERIMENTO 39 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.igallo,R.drawable.clavescruz,//195
            /*EXPERIMENTO 40 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dpinguino,R.drawable.clavescruz,//200

            /*EXPERIMENTO 41 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dpulpo,R.drawable.clavescruz,//205
            /*EXPERIMENTO 42 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.drana,R.drawable.clavescruz,//210
            /*EXPERIMENTO 43 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.idelfin,R.drawable.clavescruz,//215
            /*EXPERIMENTO 44 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iconejo,R.drawable.clavescruz,//220
            /*EXPERIMENTO 45 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.icolibri,R.drawable.clavescruz,//225
            /*EXPERIMENTO 46 DERECHA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.draton,R.drawable.clavescruz,//230
            /*EXPERIMENTO 47 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dzorro,R.drawable.clavescruz,//235
            /*EXPERIMENTO 48 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.icocodrilo,R.drawable.clavescruz,//240
            /*EXPERIMENTO 49 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.icatarina,R.drawable.clavescruz,//245
            /*EXPERIMENTO 50 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.icangrejo,R.drawable.clavescruz,//250

            /*EXPERIMENTO 51 IZQUIERDA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.icamaleon,R.drawable.clavescruz,//255
            /*EXPERIMENTO 52 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iburro,R.drawable.clavescruz,//260
            /*EXPERIMENTO 53 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.ibuho,R.drawable.clavescruz,//265
            /*EXPERIMENTO 54 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dserpiente,R.drawable.clavescruz,//270
            /*EXPERIMENTO 55 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dtiburon,R.drawable.clavescruz,//275
            /*EXPERIMENTO 56 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dtigre,R.drawable.clavescruz,//280
            /*EXPERIMENTO 57 DERECHA*/R.drawable.derecha,R.drawable.clavescruz,R.drawable.dtortuga,R.drawable.clavescruz,//285
            /*EXPERIMENTO 58 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iballena,R.drawable.clavescruz,//290
            /*EXPERIMENTO 59 IZQUIERDA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.iabeja,R.drawable.clavescruz,//295
            /*EXPERIMENTO 60 DERECHA*/R.drawable.izquierda,R.drawable.clavescruz,R.drawable.dtucan,R.drawable.clavescruz};//301


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.*/

        context = this;

        setContentView(R.layout.activity_claves_juego);
        BtnDerecho = (Button) findViewById(R.id.buttonD);
        BtnDerecho.setOnClickListener(onClick);
        BtnIzquierdo = (Button) findViewById(R.id.buttonI);
        BtnIzquierdo.setOnClickListener(onClick);
        LayoudPrincipal=(RelativeLayout) findViewById(R.id.layoudPrincipal);
        //aplauso = MediaPlayer.create(this,R.raw.aplauso);
        //abucheo = MediaPlayer.create(this,R.raw.abucheo);

        SharedPreferences prefs = this.getSharedPreferences(
                Constants.prefsName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();

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
                ImageView imageView = new ImageView(JuegoClavesActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

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

            storeDataInLocalTxt store = new storeDataInLocalTxt(context);
            store.saveData(userData, "");
        }

    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {

            ellapsedTime = System.currentTimeMillis() - startTime;
            //int nCorrida = (int) Math.ceil( 1 + (position)/4 );
            int nCorrida = (int) Math.ceil( (position + 1)/4 );

            switch (v.getId()) {
                case R.id.buttonD:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if(position==3 || position==4 || position==7 || position==8 || position==15 || position==16 || position==19 || position==20 || position==35 || position==36 || position==39 || position==40
                            || position==47 || position==48 || position==55 || position==56 || position==59 || position==60 || position==75 || position==76
                            || position==87 || position==88 || position==95|| position==96  || position==99 || position==100 || position==107 || position==108 || position==111 || position==112
                            || position==123 || position==124 || position==127 || position==128 || position==131 || position==132 || position==143 || position==144 || position==151 || position==152
                            || position==159 || position==160 || position==163 || position==164 || position==167 || position==168 || position==183 || position==184 || position==187 || position==188
                            || position==215 || position==216 || position==219 || position==220 || position==223 || position==224 || position==227 || position==228 || position==239 || position==240 )
                    {
                        //aplauso.start();
                        result = 1;
                    } else if(position==11 || position ==12 || position ==23 || position ==24 || position==27 || position==28 || position==31 || position==32 || position==43 || position==44
                            || position==51 || position==52 || position==63 || position==64 || position==67 || position==68 || position==71 || position==72 || position==79 || position==80
                            || position==83 || position==84 || position ==91 || position==92 || position==103 || position==104 || position==115 || position==116 || position==119 || position==120
                            || position==135 || position==136 || position==139 || position==140 || position==147 || position==148 || position==155 || position==156 || position==171 || position==172

                            || position==175 || position==176 || position==179 || position==180 || position==191 || position==192 || position==195 || position==196 || position==199 || position==200
                            || position==203 || position==204 || position==207 || position==208 || position==211 || position==212 || position==231 || position==232 || position==235 || position==236 ) {
                        //abucheo.start();
                        result = 0;
                    } else {
                        //abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                        result = 2;
                        nCorrida ++;
                    }

                    userData += result + ",";
                    userData += nCorrida + ",";
                    userData += (int) ellapsedTime + ",\n";
                    Log.d("nCorrida", String.valueOf(nCorrida));
                    break;

                case R.id.buttonI:
                    BtnDerecho.setClickable(false);
                    BtnIzquierdo.setClickable(false);
                    if(position==11 || position ==12 || position ==23 || position ==24 || position==27 || position==28 || position==31 || position==32 || position==43 || position==44
                            || position==51 || position==52 || position==63 || position==64 || position==67 || position==68 || position==71 || position==72 || position==79 || position==80
                            || position==83 || position==84 || position ==91 || position==92 || position==103 || position==104 || position==115 || position==116 || position==119 || position==120
                            || position==135 || position==136 || position==139 || position==140 || position==147 || position==148 || position==155 || position==156 || position==171 || position==172

                            || position==175 || position==176 || position==179 || position==180 || position==191 || position==192 || position==195 || position==196 || position==199 || position==200
                            || position==203 || position==204 || position==207 || position==208 || position==211 || position==212 || position==231 || position==232 || position==235 || position==236 ) {
                        //aplauso.start();
                        result = 1;
                    } else if(position==3 || position==4 || position==7 || position==8 || position==15 || position==16 || position==19 || position==20 || position==35 || position==36 || position==39 || position==40
                            || position==47 || position==48 || position==55 || position==56 || position==59 || position==60 || position==75 || position==76
                            || position==87 || position==88 || position==95|| position==96  || position==99 || position==100 || position==107 || position==108 || position==111 || position==112
                            || position==123 || position==124 || position==127 || position==128 || position==131 || position==132 || position==143 || position==144 || position==151 || position==152
                            || position==159 || position==160 || position==163 || position==164 || position==167 || position==168 || position==183 || position==184 || position==187 || position==188
                            || position==215 || position==216 || position==219 || position==220 || position==223 || position==224 || position==227 || position==228 || position==239 || position==240 )
                    {
                        //abucheo.start();
                        result = 0;
                    } else {
                        //abucheo.start();
                        BtnDerecho.setClickable(false);
                        BtnIzquierdo.setClickable(false);
                        result = 2;
                        nCorrida ++;
                    }
                    if (position == gallery.length) {
                        timer.cancel();
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

                    userData += result + ",";
                    userData += nCorrida + ",";
                    userData += (int) ellapsedTime + ",\n";
                    Log.d("nCorrida", String.valueOf(nCorrida));
                    Log.d("List", String.valueOf(userData));
                    break;

                case R.id.continuarBtn:

                    finish();
                    Intent intentE = new Intent(JuegoClavesActivity.this, ElegirEjercicioActivity.class);
                    startActivity(intentE);
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
                                if( BtnIzquierdo.isClickable() == true ) {
                                    int nCorrida = (int) Math.ceil( (position)/4);
                                    result = 3;
                                    userData += result + ",";
                                    userData += nCorrida + ",";
                                    userData += 0 + ",\n";
                                    Log.d("List", String.valueOf(userData));
                                    Log.d("nCorrida", String.valueOf(nCorrida));
                                }

                                curTime = 750; // inicia cue
                                BtnDerecho.setClickable(true);
                                BtnIzquierdo.setClickable(true);
                                startTime = System.currentTimeMillis();
                            }

                            // Asingacion de pulsaciones de botón
                            imageSwitcher.setImageResource(gallery[position]);
                            position++;
                            if (position==3 || position==7 || position==11 || position==15 || position==19 || position==23 || position==27 || position==31 || position==35 || position==39 || position==43 || position==47 || position==51 || position==55 || position==59 || position==63
                                    || position==67 || position==71 || position==75 || position==79 || position==83 || position==87 || position==91 || position==95 || position==99 || position==103 || position==107 || position==111 || position==115 || position==119 || position==123 || position==127
                                    || position==131 || position==135 || position==139 || position==143 || position==147 || position==151 || position==155 || position==159 || position==163 || position==167 || position==171 || position==175 || position==179 || position==183 || position==187 || position==191
                                    || position==195 || position==199 || position==203 || position==207 || position==211 || position==215 || position==219 || position==223 || position==227 || position==231 || position==235 || position==239) {
                                BtnDerecho.setEnabled(true);
                                BtnIzquierdo.setEnabled(true);
                            }
                            if (position == gallery.length) {
                                // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
                                timer.cancel();
                                Handler handler = new Handler();
                                handler.postDelayed(response, 3000);
                            }

                        }
                    });
                }
            }
        }, 0, 50);
    }

    private Runnable response = new Runnable() {
        @Override
        public void run() {
            imageSwitcher.setVisibility(View.INVISIBLE);

            wv = (WebView) findViewById(R.id.webView);

            // mostrar gif de fuegos artificiales
            wv.setVisibility(View.VISIBLE);
            wv.loadUrl("file:///android_asset/gifs/index.html");
            // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
            Handler handler = new Handler();
            handler.postDelayed(runnable, 3000);
        }
    };

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