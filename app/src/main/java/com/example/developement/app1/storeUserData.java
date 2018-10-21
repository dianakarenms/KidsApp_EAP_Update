package com.example.developement.app1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.developement.app1.Login.LoginActivity;
import com.example.developement.app1.menuElegirEjercicio.ElegirEjercicioActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEVELOPEMENT on 26/08/2015.
 */
public final class storeUserData implements ResultsListener {

    public static ConnectionTest conntask;
    public static ResultsListener listener;
    public static Context context;
    public static JSONArray jsonArray;
    public static String usrId;

    // Shared Preferences
    SharedPreferences prefs;

    //URL del archivo PHP para crear un producto nuevo
    public static String url_json = "http://104.131.143.76/eap/include/data2.php";
    Boolean exitSave;
    //String statusTxt;

    public storeUserData(){}

    public void firstConnStorage(Context context){
        this.context = context;

        prefs = context.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        try{
            if(prefs.getString("resultKids", "[]") != "[]") {
                jsonArray = new JSONArray(prefs.getString("resultKids", "[]"));
                Log.i("firstConnStorage()", jsonArray.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener = this;
        exitSave = true;

        conntask = new ConnectionTest();
        conntask.setOnResultsListener(context, url_json, listener);
        conntask.execute();
    }

    public void saveData(String exId, String data, Context context){
        this.context = context;
        prefs = context.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        if(prefs.getString("userId", "") != "")
            usrId = prefs.getString("userId", "");

        try{
            JSONObject usrAnswer = new JSONObject();
            usrAnswer.put("usrId", usrId);
            usrAnswer.put("exId", exId);
            data = data.replace(" ", "");
            usrAnswer.put("data", data);

            if(prefs.getString("resultKids", "[]") != "[]") {
                jsonArray = new JSONArray(prefs.getString("resultKids", "[]"));
                jsonArray.put(usrAnswer);
                //Log.i("saveData()", jsonArray.toString());
            } else {
                jsonArray = new JSONArray();
                jsonArray.put(usrAnswer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener = this;
        exitSave = false;

        conntask = new ConnectionTest();
        conntask.setOnResultsListener(context, url_json, listener);
        conntask.execute();

    }

    /**
     * Escucha la respuesta de la tarea as?ncrona ConnectionTest
     * @param  -email
     * @return - true/false
     */
    @Override
    public void onResultsSucceeded(Boolean result) {
        if(result){
            // Si hay internet
            try{
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("resultKids").commit();
                if(exitSave == true)
                    editor.remove("userId").commit();

                Toast.makeText(context, "Guardado Exitoso", Toast.LENGTH_SHORT).show();
                new SendExerciseData(context, jsonArray.toString(), exitSave).execute();
                Log.i("resultVacio", prefs.getString("resultKids", "[]"));
            } catch (NullPointerException e) {
                Log.d("Class Call", "Not context found");
            }
        } else {
            // Conexión fallida
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("resultKids", jsonArray.toString());
            Log.d("Saving json", jsonArray.toString());
            editor.commit();

            if(exitSave == true)
                Toast.makeText(context, "Habilite la conexión a internet para salir", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ElegirEjercicioActivity.class);
                context.startActivity(i);
            }
        }

    }

}


/**
 * Background Async Task to Create new product
 * */
class SendExerciseData extends AsyncTask<String, String, String> {

    String id, sol, data;
    Context context;
    //ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    Boolean exitSave;

    public static String url_json = "http://104.131.143.76/eap/include/data2.php";

    public SendExerciseData(Context context, String data, Boolean firstSave) {
        this.context = context;
        this.data = data;
        this.exitSave = firstSave;
    }
    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*pDialog = new ProgressDialog(context);
        pDialog.setMessage("Guardando Resultados...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        // check for success tag
        try {

            //data = "nR," + data;

            //Creaci?n de la cadena para enviar a la base de datos
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("data", "nR"));
            params.add(new BasicNameValuePair("results", data));

            Log.i("sqlData", data);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_json, "POST", params);
            Log.d("Create Response", json.toString());

            int success = json.getInt("success");
            if (success == 1) {
                return "1";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * After completing background task Dismiss the progress dialog
     */
    protected void onPostExecute(String success) {
        // dismiss the dialog once done
        //pDialog.dismiss();
        if (success == "1") {
            Activity activity = (Activity) context;
            if(exitSave == true){
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                activity.finish();
            } else {
                Intent i = new Intent(context, ElegirEjercicioActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                activity.finish();
            }

            Log.i("Save Status: ", "exito");
            //Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show();
        } else {
            // failed to create product
            Log.i("ERROR!", "Ouch");
        }
    }

}

