package com.angelicaflores.eap.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.angelicaflores.Utils.Constants;
import com.angelicaflores.eap.BuildConfig;
import com.angelicaflores.eap.R;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;

/**
 * Created by Karencita on 02/09/2015.
 */
public class LoginActivity extends AppCompatActivity {

    // Variables internas
    Button nextBtn;
    Context context;

    // Shared Preferences
    SharedPreferences prefs;

    //URL del archivo PHP para crear un producto nuevo
    //String url_prueba = "http://104.131.143.76/eap/include/data.php";
    String user;
    EditText usuarioField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        usuarioField = findViewById(R.id.usuarioEdit);
        nextBtn = findViewById(R.id.loginNextBtn);

        String font_path = "font/ChalkboardSE.ttc";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        nextBtn.setTypeface(TF, Typeface.BOLD);
        usuarioField.setTypeface(TF, Typeface.BOLD);

        prefs = this.getSharedPreferences(
                Constants.prefsName, Context.MODE_PRIVATE);

        if(prefs.getString("userId", "") != ""){
            Intent i = new Intent(LoginActivity.this, ElegirEjercicioActivity.class);
            LoginActivity.this.startActivity(i);
            finish();
        }

        TextView versionName = findViewById(R.id.loginVersionLabel);
        versionName.setText("Versión " + BuildConfig.VERSION_NAME);
        versionName.setTypeface(TF, Typeface.BOLD);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usuarioField.getText().toString().isEmpty()) {
                    user = usuarioField.getText().toString();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("userId", user);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, ElegirEjercicioActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                } else {
                    usuarioField.setError("Campo obligatorio");
                }
            }
        });
    } // onCreate()
}


