package com.angelicaflores.eap.C_CPTP;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angelicaflores.eap.R;


public class InstrCPTPActivity extends AppCompatActivity implements View.OnClickListener
{
    TextToSpeech t1;
    ImageButton boton;
    TextView txt;

    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;
    String exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cptp_instr);
        //Asignamos la variable a nuestros elementos.
        boton = (ImageButton) findViewById(R.id.btnDemo);
        boton.setOnClickListener(this);
        boton = (ImageButton) findViewById(R.id.BTNjugar);
        boton.setOnClickListener(this);

        TF = Typeface.createFromAsset(getAssets(), font_path);
        txt = (TextView)findViewById(R.id.titleTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt = (TextView)findViewById(R.id.playTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt = (TextView)findViewById(R.id.practiceTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt = (TextView)findViewById(R.id.instrTxt);
        txt.setTypeface(TF, Typeface.BOLD);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.
    }
    //Se declara el metodo onclick al boton para avanzar a la siguiente Activity DEMO.
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnDemo:
                Intent intent= new Intent(this, DemoCPTPActivity.class);
                intent.putExtra("exerciseId", exerciseId);
                        startActivity(intent);
                break;
            case R.id.BTNjugar:
                Intent btnnueve= new Intent(this, JuegoCPTPActivity.class);
                btnnueve.putExtra("exerciseId", exerciseId);
                        startActivity(btnnueve);
                break;
            default:
                break;
        }
    }
}

