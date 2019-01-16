package com.angelicaflores.eap.B_Corsi;

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
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;

public class Instr_Corsi extends AppCompatActivity implements View.OnClickListener
{
    TextToSpeech t1;
    ImageButton btnpracticardemo,btnpracticarjuego;
    Button btnReproducir;
    TextView txt;
    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;
    String exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corsi_instr);

        btnpracticardemo = (ImageButton)findViewById(R.id.btnpractica);
        btnpracticardemo.setOnClickListener(this);

        btnpracticarjuego = (ImageButton)findViewById(R.id.btnjugar);
        btnpracticarjuego.setOnClickListener(this);

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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Instr_Corsi.this, ElegirEjercicioActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnpractica:
                Intent demo= new Intent(this,Demo_Corsi.class);
                demo.putExtra("exerciseId", exerciseId);
                        startActivity(demo);
                finish();
                break;
            case R.id.btnjugar:
                Intent jugar= new Intent(this,Juego_Corsi.class);
                jugar.putExtra("exerciseId", exerciseId);
                startActivity(jugar);
                finish();
                break;
            default:
        }
    }
}
