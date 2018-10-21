package com.example.developement.app1.B_Corsi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.developement.app1.R;

import java.util.Timer;
import java.util.TimerTask;

public class Demo_Corsi extends AppCompatActivity
{

    Button ctrlBtn;
    CountDownTimer countDownTimer;
    int counter = 1;
    String name;
    Timer timer;
    int figId;
    ImageButton cuboBtn;
    ImageView semaphore;
    String font_path = "font/ChalkboardSE.ttc";
    Typeface TF;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_corsi_gameplay);
        TF = Typeface.createFromAsset(getAssets(), font_path);

        semaphore = (ImageView)findViewById(R.id.imgsemaforo);
        ctrlBtn = (Button) findViewById(R.id.ctrlBtn);
        ctrlBtn.setOnClickListener(onClick);
        ctrlBtn.setTypeface(TF, Typeface.BOLD);

        for(int i=1; i<=2; i++){
            name = "cubo" + i;
            figId = getResources().getIdentifier(name, "id", getPackageName());
            cuboBtn = (ImageButton) findViewById(figId);
            cuboBtn.setOnClickListener(onClick);
            cuboBtn.setClickable(false);
        }

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
        Intent i = new Intent(Demo_Corsi.this, Instr_Corsi.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            if (Integer.parseInt(String.valueOf(v.getTag())) != 0) {
                if(semaphore.getVisibility() == View.VISIBLE)
                    semaphore.setVisibility(View.INVISIBLE);

                if(counter == 2)
                    ctrlBtn.setVisibility(View.VISIBLE);

                cuboBtn = (ImageButton) findViewById(v.getId());
                cuboBtn.setBackgroundResource(R.drawable.amarillo);
                cuboBtn.setClickable(false);
                counter++;
            }

            switch (v.getId()) {
                case R.id.ctrlBtn:
                    if(ctrlBtn.getText().equals("Finalizar")) {
                        finish();
                        Intent intentE = new Intent(Demo_Corsi.this, Instr_Corsi.class);
                        startActivity(intentE);
                    } else {
                        ctrlBtn.setVisibility(View.INVISIBLE);
                        startCorsiTimer();
                    }
                    break;

                case R.id.cubo1:
                    v.setBackgroundResource(R.drawable.amarillo);
                    break;
            }
        }
    };

    public void startCorsiTimer() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                runOnUiThread(new Runnable() {
                    public void run() {

                        if(counter <= 2) {
                            name = "cubo" + counter;
                            figId = getResources().getIdentifier(name, "id", getPackageName());
                            cuboBtn = (ImageButton) findViewById(figId);
                            cuboBtn.setBackgroundResource(R.drawable.amarillo);
                            counter++;
                        } else {
                            for(int i=1; i<=2; i++){
                                name = "cubo" + i;
                                figId = getResources().getIdentifier(name, "id", getPackageName());
                                cuboBtn = (ImageButton) findViewById(figId);
                                cuboBtn.setBackgroundResource(R.drawable.azul);
                                cuboBtn.setClickable(true);
                            }
                            semaphore.setVisibility(View.VISIBLE);
                            ctrlBtn.setText("Finalizar");
                            counter = 1;
                            timer.cancel();
                        }

                    }
                });

            }

        }, 0, 500); //wait 500 to change background image of next cube

    }

}
