package com.angelicaflores.eap.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.angelicaflores.Utils.ConnectionTest;
import com.angelicaflores.Utils.Constants;
import com.angelicaflores.Utils.JSONParser;
import com.angelicaflores.eap.R;
import com.angelicaflores.Utils.ResultsListener;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioActivity;
import com.angelicaflores.eap.menuElegirEjercicio.ElegirEjercicioObject;
import com.flipboard.bottomsheet.BottomSheetLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Karencita on 02/09/2015.
 */
public class LoginActivity extends AppCompatActivity {

    // Json
    JSONParser jsonParser = new JSONParser();
    JSONArray exercises = null;

    // Variables internas
    Button nextBtn;
    private ProgressDialog pDialog;
    ConnectionTest conntask;
    ResultsListener listener;
    ArrayList<ElegirEjercicioObject> list;
    Context context;

    // Shared Preferences
    SharedPreferences prefs;
    File myFile;

    //URL del archivo PHP para crear un producto nuevo
    String url_prueba = "http://104.131.143.76/eap/include/data.php";
    String user;
    EditText usuarioField;

    protected BottomSheetLayout bottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        bottomSheetLayout = findViewById(R.id.bottomsheet);
        bottomSheetLayout.setPeekOnDismiss(true);

        usuarioField = findViewById(R.id.usuarioEdit);
        nextBtn = findViewById(R.id.nextBtn);

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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = usuarioField.getText().toString();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userId", user);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, ElegirEjercicioActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });
    } // onCreate()

    //Nombres de los nodos del JSON
    private static final String TAG_SUCCESS = "success";

    private void verificarUsuario(String user, String password) {
        try {
            File myFile = new File("/sdcard/EAPpacientes.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            JSONArray jsonArray = new JSONArray(convertStreamToString(fIn));
            JSONObject jsonObject;
            int successFlag = 0;

            for(int i = 0; i < jsonArray.length(); i++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("u").equals(user)){
                    if(jsonObject.getString("p").equals(password)){
                        successFlag = 1;
                        break;
                    }
                }
            }

            if(successFlag == 1){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userId", user);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, ElegirEjercicioActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Error de verificación")
                        .setMessage("Verifique usuarioField y/o contraseña")
                        .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }

        } catch (Exception e){
            Log.e("ERROR", "Error de lectura EAPPacientes.txt en verificarUsuario()");
            new AlertDialog.Builder(context)
                    .setTitle("No Existen Pacientes")
                    .setMessage("Sincronice la lista de Pacientes desde el Menú")
                    .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

       /* try {
            File myFile = new File("/sdcard/EAPpacientes.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile, false);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(user.toString());
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            Log.e("ERROR","No se pudo crear EAPpacientes.txt");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Html.fromHtml("<font color='#006600'>DESCARGA EXITOSA</font>"));
        builder.setIcon(R.drawable.peopleicon);
        builder.setMessage("Sincronización de Pacientes Completa");
        builder.setPositiveButton("Ok", null);
        final AlertDialog alert = builder.create();

        //show AlertDialog
        alert.show();
        */
}


