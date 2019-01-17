package com.angelicaflores.eap.F_RedesAtencionales;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angelicaflores.eap.R;

public class InstrRedesActivity extends AppCompatActivity {

    TextView txt;
    ImageButton boton, playBtn;
    Button continuarBtn;
    Boolean nextScreenFlag = false;
    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;
    String exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            exerciseId = extras.getString("exerciseId"); //if it's a string you stored.

        TF = Typeface.createFromAsset(getAssets(), font_path);
        instrucciones();
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btnDemo:
                    Intent i = new Intent(InstrRedesActivity.this, IdentRedesActivity.class);
                    startActivity(i);
                    break;

                case R.id.continuarBtn:
                    if(continuarBtn.getText().equals("Continuar")) {
                        nextScreenFlag = true;
                        instrucciones();
                    } else if(continuarBtn.getText().equals("Practicar")){
                        Intent i2 = new Intent(InstrRedesActivity.this, DemoRedesActivity.class);
                        startActivity(i2);
                    }
                    break;

                case R.id.btnJugar:
                    Intent i2 = new Intent(InstrRedesActivity.this, JuegoRedesActivity.class);
                    i2.putExtra("exerciseId", exerciseId);
                    startActivity(i2);
                    break;





            }
        }
    };


    public void instrucciones(){
        setContentView(R.layout.activity_redes_instr);

        txt=(TextView) findViewById(R.id.instrTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt=(TextView) findViewById(R.id.playTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt=(TextView) findViewById(R.id.practiceTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt=(TextView) findViewById(R.id.titleTxt);
        txt.setTypeface(TF, Typeface.BOLD);

        boton = (ImageButton) findViewById(R.id.btnDemo);
        boton.setOnClickListener(onClick);

        playBtn = (ImageButton) findViewById(R.id.btnJugar);
        playBtn.setOnClickListener(onClick);

        continuarBtn = (Button) findViewById(R.id.continuarBtn);
        continuarBtn.setOnClickListener(onClick);

        setContentView(R.layout.activity_redes_instr);

        txt=(TextView) findViewById(R.id.instrTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt=(TextView) findViewById(R.id.playTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt=(TextView) findViewById(R.id.practiceTxt);
        txt.setTypeface(TF, Typeface.BOLD);
        txt=(TextView) findViewById(R.id.titleTxt);
        txt.setTypeface(TF, Typeface.BOLD);

        boton = (ImageButton) findViewById(R.id.btnDemo);
        boton.setOnClickListener(onClick);

        continuarBtn = (Button) findViewById(R.id.continuarBtn);
        continuarBtn.setOnClickListener(onClick);
        continuarBtn.setTypeface(TF, Typeface.BOLD);
        continuarBtn.setText("Practicar");
        continuarBtn.setTextSize(30);

        playBtn = (ImageButton) findViewById(R.id.btnJugar);
        playBtn.setOnClickListener(onClick);

    }
}

