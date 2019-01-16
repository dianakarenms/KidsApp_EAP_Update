package com.angelicaflores.eap.E_ClavesCentrales;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angelicaflores.eap.app1.R;


public class Instr_Claves extends AppCompatActivity {

    TextView textview;
    Button BtnReproducir;
    ImageButton BtnPrueba, BtnEjercicios;
    String exerciseId;

    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_claves_instr);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.

        String font_path = "font/ChalkboardSE.ttc";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        textview=(TextView) findViewById(R.id.instrTxt);
        textview.setTypeface(TF, Typeface.BOLD);
        textview=(TextView) findViewById(R.id.playTxt);
        textview.setTypeface(TF, Typeface.BOLD);
        textview=(TextView) findViewById(R.id.practiceTxt);
        textview.setTypeface(TF, Typeface.BOLD);
        textview=(TextView) findViewById(R.id.titleTxt);
        textview.setTypeface(TF, Typeface.BOLD);

        BtnPrueba=(ImageButton) findViewById(R.id.btnDemo);
        BtnEjercicios = (ImageButton) findViewById(R.id.btnJugar);

        //Evento Onclick del boton para la demostracion
        BtnPrueba.setOnClickListener(new View.OnClickListener() {

            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(Instr_Claves.this, Demo_Claves.class);
                startActivity(intent);
            }
        });

        //Evento Onclick del boton para iniciar con los ejercicios
        BtnEjercicios.setOnClickListener(new View.OnClickListener() {

            //@Override
            public void onClick(View v) {
                Intent intentE = new Intent(Instr_Claves.this, Juego_Claves.class);
                intentE.putExtra("exerciseId", exerciseId);
                startActivity(intentE);
            }
        });
    }

}
