package com.angelicaflores.eap.D_Flanker;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelicaflores.eap.app1.R;


public class Instr_Flanker extends AppCompatActivity {

    TextView txt, instrTxt, practiceTxt, playTxt;
    ImageButton BtnPrueba, BtnEjercicios;
    Button BtnContinuar;

    ImageView ImgEstrella,ImgTriangulo, ImgDerecha,ImgIzquierda;
    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;

    String exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.

        setContentView(R.layout.activity_flanker_instr);
        BtnPrueba=(ImageButton) findViewById(R.id.btnDemo);
        BtnEjercicios = (ImageButton) findViewById(R.id.btnJugar);
        BtnContinuar = (Button) findViewById(R.id.continuarBtn);
        ImgEstrella = (ImageView) findViewById(R.id.imageViewP);
        ImgTriangulo = (ImageView) findViewById(R.id.imageView2);
        ImgDerecha = (ImageView) findViewById(R.id.imageViewD);
        ImgIzquierda = (ImageView) findViewById(R.id.imageViewI);

        TF = Typeface.createFromAsset(getAssets(), font_path);
        txt = (TextView)findViewById(R.id.titleTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        playTxt = (TextView)findViewById(R.id.playTxt);
        playTxt.setTypeface(TF, Typeface.BOLD);
        practiceTxt = (TextView)findViewById(R.id.practiceTxt);
        practiceTxt.setTypeface(TF, Typeface.BOLD);
        instrTxt = (TextView)findViewById(R.id.instrTxt);
        instrTxt.setTypeface(TF, Typeface.BOLD);
        BtnContinuar.setTypeface(TF, Typeface.BOLD);

        //Evento Onclick del boton para la presentacion
        BtnPrueba.setOnClickListener(new View.OnClickListener() {

            //@Override
            public void onClick(View v) {
                Intent intent = new Intent(Instr_Flanker.this, Demo_Flanker.class);
                startActivity(intent);
            }
        });

        //Evento Onclick del boton para iniciar con los ejercicios
        BtnEjercicios.setOnClickListener(new View.OnClickListener() {

            //@Override
            public void onClick(View v) {
                Intent intentE = new Intent(Instr_Flanker.this, Juego_Flanker.class);
                intentE.putExtra("exerciseId", exerciseId);
                startActivity(intentE);
            }
        });


        //Evento Onclick del boton para iniciar con los ejercicios
        BtnContinuar.setOnClickListener(new View.OnClickListener() {

            //@Override
            public void onClick(View v) {
                instrTxt.setText("Indica el nombre de cada  figura.");
                BtnPrueba.setVisibility(View.VISIBLE);
                playTxt.setVisibility(View.VISIBLE);
                practiceTxt.setVisibility(View.VISIBLE);
                //BtnReproducir.setVisibility(View.INVISIBLE);
                BtnEjercicios.setVisibility(View.VISIBLE);
                BtnContinuar.setVisibility(View.INVISIBLE);
                ImgIzquierda.setVisibility(View.INVISIBLE);
                ImgDerecha.setVisibility(View.INVISIBLE);

            }
        });
    }
}