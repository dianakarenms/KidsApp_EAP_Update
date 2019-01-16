package com.example.developement.app1.A_Cancelacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developement.app1.R;
import com.example.developement.app1.menuElegirEjercicio.ElegirEjercicioActivity;
import com.example.developement.app1.storeDataInLocalTxt;

import java.util.ArrayList;


public class SeleccionarLetrasActivity extends AppCompatActivity {

    Button finalizarBtn;
    SeleccionarLetrasAdapter adapter;
    SharedPreferences prefs;
    ArrayList<String> userData = new ArrayList<String>();
    String exerciseId = "1";
    String exSol;
    ArrayList<Integer> figArray;
    String tempData;
    public long long_remaingtime;
    Context context;

    Resources res;
    String[] tempFigureNames;
    int[] figureImages = {R.drawable.ic_axx, R.drawable.ic_bxx, R.drawable.ic_cxx, R.drawable.ic_dxx, R.drawable.ic_exx}; //array de figuras en la carpeta drawable

    ImageButton boton, playBtn;
    Button continuarBtn;

    Boolean nextScreenFlag = false;
    Boolean flag = false;

    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;

    //URL del archivo PHP para crear un producto nuevo
    String url_prueba = "http://66.7.193.242/~udlap/app_project/administrador/gestor/include/data.php";
    WebView wv;

    //Nombres de los nodos del JSON
    private boolean endFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //exerciseId = extras.getString("exerciseId"); //if it's a string you stored.
            exSol = extras.getString("exSol");
            tempData = extras.getString("tempData");
            Log.i("gameSol", String.valueOf(exSol));
            String iValue = extras.getString("orderArr"); // Array of objects forma: 1,3,2,4,1,3,4...
            long_remaingtime = Long.parseLong("60000");

            // array to get convert the String from json call, to an String[]
            String[] sValue = iValue.split(",");
            figArray = new ArrayList<Integer>();
            for (int i = 0; i < sValue.length; i++) {
                try {
                    figArray.add(Integer.parseInt(sValue[i]));
                } catch (NumberFormatException nfe) {
                }

            }
        }

        TF = Typeface.createFromAsset(getAssets(), font_path);

        instrucciones();

        context = this;

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("exerciseId", exerciseId);
        editor.commit();

    }

    @Override
    public void onBackPressed() {
        if(!endFlag) {
            if (flag == true) {
                Intent i = new Intent(context, ElegirEjercicioActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                finish();

                storeDataInLocalTxt store = new storeDataInLocalTxt();
                store.saveData(userData.toString(), context);
            } else {
                Intent i = new Intent(SeleccionarLetrasActivity.this, ElegirEjercicioActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnDemo:
                    setContentView(R.layout.tarea_cancelacion_instr);
                    finalizarBtn = (Button) findViewById(R.id.finInstrBtn);
                    finalizarBtn.setOnClickListener(onClick);
                    finalizarBtn.setTypeface(TF, Typeface.BOLD);

                    //inicializar el ArrayList list
                    ArrayList<SeleccionarLetrasObject> list; //this array list id going to store just Single Items
                    list = new ArrayList<>(); //list empty

                    Resources res = getApplicationContext().getResources(); //obtiene los elementos necesarios
                    String[] tempFigureNames = res.getStringArray(R.array.figurenames); //array de los nombre de las figuras que se encuentra en <string-array>
                    int[] figureImages = {R.drawable.ic_axx, R.drawable.ic_bxx, R.drawable.ic_cxx, R.drawable.ic_dxx, R.drawable.ic_exx}; //array de figuras en la carpeta drawable
                    //llenar la lista "list"}
                    String practiceList = "2,0,1,4,3,2,4,1,4,1,2,4";

                    // array to get convert the String from json call, to an String[]
                    String[] sValue = practiceList.split(",");

                    ArrayList<Integer> practiceFigArray = new ArrayList<Integer>();
                    for (int i = 0; i < sValue.length; i++) {
                        try {
                            practiceFigArray.add(Integer.parseInt(sValue[i]));
                        } catch (NumberFormatException nfe) {
                        }

                    }

                    SeleccionarLetrasObject tempFigure;

                    int counter = 0;
                    while( counter < 12 ) {
                        tempFigure = new SeleccionarLetrasObject(figureImages[practiceFigArray.get(counter)], tempFigureNames[practiceFigArray.get(counter)], practiceFigArray.get(counter));
                        list.add(tempFigure);
                        counter ++;
                    }

                    adapter = new SeleccionarLetrasAdapter(context, R.layout.cancelacion_single_item, list, Integer.parseInt(exSol));
                    //asignar el objeto gridview a myGrid variable object
                    GridView myGrid = findViewById(R.id.practiceGrid);
                    myGrid.setAdapter(adapter);
                    break;

                case R.id.BTNjugar:
                    iniciarJuego();
                    break;

                case R.id.continuarBtn:
                        nextScreenFlag = true;
                        instrucciones();
                    break;

                case R.id.finInstrBtn:
                    instrucciones();
                    break;

                case R.id.finalizarBtn:
                    if(finalizarBtn.getText() ==  "Siguiente") {
                        userData = concat(userData, adapter.getMylist());
                        iniciarJuego();
                    } else { //si finalizar
                        finalizarBtn.setClickable(false);
                        userData = concat(userData, adapter.getMylist());

                        endFlag = true;
                        // mostrar gif de fuegos artificiales
                        wv.setVisibility(View.VISIBLE);
                        wv.loadUrl("file:///android_asset/gifs/index.html");

                        // esperar 300ms antes de comenzar el guardado de datos, para dar tiempo al gif de mostrarse
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 3000);

                        break;
                    }
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(context, ElegirEjercicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            finish();

            storeDataInLocalTxt store = new storeDataInLocalTxt();
            store.saveData(userData.toString(), context);
        }
    };

    public void iniciarJuego() {
        setContentView(R.layout.cancelacion_gameplay);

        flag = true;

        wv = (WebView) findViewById(R.id.webView);
        finalizarBtn = (Button) findViewById(R.id.finalizarBtn);
        finalizarBtn.setOnClickListener(onClick);
        finalizarBtn.setTypeface(TF, Typeface.BOLD);

        //inicializar el ArrayList list
        ArrayList<SeleccionarLetrasObject> list; //this array list id going to store just Single Items
        list = new ArrayList<>(); //list empty

        res = getApplicationContext().getResources(); //obtiene los elementos necesarios
        tempFigureNames = res.getStringArray(R.array.figurenames); //array de los nombre de las figuras que se encuentra en <string-array>

        int size = figArray.size()/60;
        Log.d("size", String.valueOf(size));
        if( size > 1 ) {
            finalizarBtn.setText("Siguiente");
            Log.d("siguiente", "");
        } else {
            finalizarBtn.setText("Finalizar");
        }

        SeleccionarLetrasObject tempFigure;
        int counter = 1;
        int maxSize = 0;
        if(figArray.size() <60)
            maxSize = figArray.size();
        else
            maxSize = 60;
        while( counter <= maxSize ) {
            tempFigure = new SeleccionarLetrasObject(figureImages[figArray.get(0)], tempFigureNames[figArray.get(0)], figArray.get(0));
            list.add(tempFigure);
            figArray.remove(0);
            counter ++;
        }

        adapter = new SeleccionarLetrasAdapter(context, R.layout.cancelacion_single_item, list, Integer.parseInt(exSol));
        //asignar el objeto gridview a myGrid variable object
        GridView myGrid = findViewById(R.id.gridView);
        myGrid.setAdapter(adapter);
    }

    public void instrucciones(){
        setContentView(R.layout.cancelacion_instr);

        TextView txt;

        boton = findViewById(R.id.btnDemo);
        boton.setOnClickListener(onClick);

        playBtn = findViewById(R.id.BTNjugar);
        playBtn.setOnClickListener(onClick);

        continuarBtn = findViewById(R.id.continuarBtn);
        continuarBtn.setOnClickListener(onClick);

        if(nextScreenFlag == false){
            boton.setVisibility(View.INVISIBLE);
            playBtn.setVisibility(View.INVISIBLE);
            continuarBtn.setVisibility(View.VISIBLE);

            TextView pracTxt = findViewById(R.id.practiceTxt);
            pracTxt.setVisibility(View.INVISIBLE);

            pracTxt = findViewById(R.id.playTxt);
            pracTxt.setVisibility(View.INVISIBLE);

            pracTxt = findViewById(R.id.instrTxt);
            pracTxt.setText("A continuación aparecerán varias figuras geométricas, el instructor te indicará el nombre de cada una de ellas.");
            pracTxt.setTypeface(TF,Typeface.BOLD);

            txt = findViewById(R.id.titleTxt);
            txt.setTypeface(TF, Typeface.BOLD);

            continuarBtn.setTypeface(TF,Typeface.BOLD);
        } else {
            setContentView(R.layout.cancelacion_instr);
            ImageView instrImg = findViewById(R.id.instrImage);
            instrImg.setImageResource(R.drawable.trianguloverde);
            instrImg.getLayoutParams().height = 85;

            boton = findViewById(R.id.btnDemo);
            boton.setOnClickListener(onClick);

            playBtn = findViewById(R.id.BTNjugar);
            playBtn.setOnClickListener(onClick);

            txt = findViewById(R.id.titleTxt);
            txt.setTypeface(TF, Typeface.BOLD);
            txt = findViewById(R.id.playTxt);
            txt.setTypeface(TF, Typeface.BOLD);
            txt = findViewById(R.id.practiceTxt);
            txt.setTypeface(TF, Typeface.BOLD);
            txt = findViewById(R.id.instrTxt);
            txt.setTypeface(TF, Typeface.BOLD);
        }

    }

    public ArrayList<String> concat(ArrayList<String> A, ArrayList<String> B) {
        int bLen = B.size();
        for(int i = 0; i<bLen; i++)
            A.add(B.get(i));
        return A;
    }
}