package com.example.developement.app1.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.developement.app1.ConnectionTest;
import com.example.developement.app1.JSONParser;
import com.example.developement.app1.R;
import com.example.developement.app1.ResultsListener;
import com.example.developement.app1.menuElegirEjercicio.ElegirEjercicioActivity;
import com.example.developement.app1.menuElegirEjercicio.ElegirEjercicioObject;
import com.example.developement.app1.storeDataInLocalTxt;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karencita on 02/09/2015.
 */
public class LoginActivity extends AppCompatActivity implements ResultsListener {

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
    EditText usuario, clave;
    String usuarioVal, claveVal;

    protected BottomSheetLayout bottomSheetLayout;

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
                        .setMessage("Verifique usuario y/o contraseña")
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        listener = this;
        context = this;

        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bottomSheetLayout.setPeekOnDismiss(true);

        nextBtn = (Button)findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(onClick);
        usuario = (EditText)findViewById(R.id.usuarioEdit);
        clave = (EditText)findViewById(R.id.claveEdit);

        String font_path = "font/ChalkboardSE.ttc";
        Typeface TF = Typeface.createFromAsset(getAssets(), font_path);
        nextBtn.setTypeface(TF, Typeface.BOLD);
        usuario.setTypeface(TF, Typeface.BOLD);
        clave.setTypeface(TF, Typeface.BOLD);

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        if(prefs.getString("userId", "") != ""){
            Intent i = new Intent(LoginActivity.this, ElegirEjercicioActivity.class);
            LoginActivity.this.startActivity(i);
            finish();
        }
    } // onCreate()

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nextBtn:
                    usuarioVal = usuario.getText().toString();
                    claveVal = clave.getText().toString();

                    verificarUsuario(usuarioVal, claveVal);
                    break;

            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showMenuSheet(MenuSheetView.MenuType.LIST);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }


    /**
     * Escucha la respuesta de la tarea as�ncrona ConnectionTest
     * @param  -email
     * @return - true/false
     */
    @Override
    public void onResultsSucceeded(Boolean result) {
        // urlBtn.setEnabled(true);
        if(result){
            try{
                new SincronizarListaUsuarios().execute();
            } catch (NullPointerException e) {
                Log.d("Class Call", "Not context found");
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error al sincronizar");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("No hay conexión a internet");
            builder.setPositiveButton("Ok", null);
            final AlertDialog alert = builder.create();

            Activity activity = (Activity)context;
            alert.show();
        }

    }


    /** jh
     * Background Async Task to Create new product
     * */
    class SincronizarListaUsuarios extends AsyncTask<String, String, String> {

        String id, sol, data;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Sincronizando Pacientes...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // check for success tag
            try {
                //Creaci�n de la cadena para enviar a la base de datos
                /*List<NameValuePair> params = new ArrayList<NameValuePair>();
                String data = "9,"+usuarioVal+","+claveVal;
                params.add(new BasicNameValuePair("data", data));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_prueba, "POST", params);*/

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String data = "7";
                params.add(new BasicNameValuePair("data", data));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_prueba, "POST", params);

                Log.d("Create Response", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    user = json.getString("users");
                    Log.d("Users", user);
                    return "1";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String success) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if (success == "1") {
                try {
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

                Activity activity = (Activity)context;
                activity.runOnUiThread(new java.lang.Runnable() {
                    public void run() {
                        //show AlertDialog
                        alert.show();
                    }
                });
            } else {
                // failed to create product
                Log.d("ERROR!", "Ouch");
            }
        }

    }

    private void showMenuSheet(final MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView =
                new MenuSheetView(LoginActivity.this, menuType, Html.fromHtml("<font color='#000066'>Menú de Opciones</font>"), new MenuSheetView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(LoginActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (bottomSheetLayout.isSheetShowing()) {
                            bottomSheetLayout.dismissSheet();
                        }
                        if(item.getItemId() == R.id.synch) {
                            conntask = new ConnectionTest();
                            conntask.setOnResultsListener(LoginActivity.this, url_prueba, listener);
                            conntask.execute();
                        }

                        if (item.getItemId() == R.id.upload) {
                            storeDataInLocalTxt store = new storeDataInLocalTxt();
                            store.uploadDataToServer(context);
                        }

                        /*if(item.getItemId() == R.id.delete) {
                            myFile = new File("/sdcard");
                            File file[] = myFile.listFiles();
                            for (int i=0; i < file.length; i++) {
                                if(file[i].getName().contains("EAPdata")){
                                    myFile = new File("/sdcard/"+file[i].getName());
                                    break;
                                }
                            }

                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle("Eliminar datos");
                            alertDialog.setMessage("Los datos no podrán ser recuperados");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            myFile.delete();
                                            new AlertDialog.Builder(context)
                                                    .setMessage("Datos eliminados correctamente")
                                                    .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });

                            alertDialog.show();

                        }*/
                        return true;
                    }
                });
        menuSheetView.inflateMenu(R.menu.create);
        bottomSheetLayout.showWithSheetView(menuSheetView);
    }
}


