package com.example.developement.app1.menuElegirEjercicio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.developement.app1.ConnectionTest;
import com.example.developement.app1.JSONParser;
import com.example.developement.app1.R;
import com.example.developement.app1.ResultsListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ge0va on 10/06/2015.
 */
public class ElegirEjercicioActivity extends AppCompatActivity implements ResultsListener {

    // Variable para conexión de internet
    private static int NUM_PAGES = 0;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    // Json
    String strEx = "[{\"id\":\"1\",\"exName\":\"cancelacion\",\"data\":\"4,2,1,0,2,1,2,4,3,2,2,3,4,1,4,2,3,1,0,4,2,0,2,4,3,0,4,0,2,1,3,1,2,1,0,4,0,3,4,3,0,4,1,0,4,2,1,4,1,4,1,0,4,3,2,3,4,1,0,2,1,4,2,0,4,1,3,4,3,1,3,2,3,4,1,2,4,3,2,4,2,4,0,2,3,4,1,0,1,2,0,1,4,1,0,3,0,4,0,4,2,0,1,3,4,1,2,1,4,0,4,3,0,2,1,0,4,3,0,3\",\"sol\":\"4\"},{\"id\":\"5\",\"exName\":\"corsi\",\"data\":\"-1\",\"sol\":\"-1\"},{\"id\":\"2\",\"exName\":\"go-nogo\",\"data\":\"-1\",\"sol\":\"-1\"},{\"id\":\"3\",\"exName\":\"flanker\",\"data\":\"-1\",\"sol\":\"-1\"},{\"id\":\"4\",\"exName\":\"\",\"data\":\"-1\",\"sol\":\"-1\"},{\"id\":\"6\",\"exName\":\"\",\"data\":\"-1\",\"sol\":\"-1\"}],\"success\":\"1\"";
    JSONParser jsonParser = new JSONParser();
    JSONArray exercises = null;

    // Variables internas
    Button urlBtn;
    private ProgressDialog pDialog;
    ConnectionTest conntask;
    ResultsListener listener;
    ElegirEjercicioAdapter adapter;
    GridView myGrid;
    ArrayList<ElegirEjercicioObject> list;

    String[] exNames = {"Tarea de Cancelación", "Tarea de Corsi", "Tarea de\r\nCPTP", "Tarea de Flankers", "Tarea de Claves Centrales", "Tarea de Redes Atencionales"};
    int[] figureImages = {R.drawable.botoncancelacion, R.drawable.botoncorsi, R.drawable.botoncptp, R.drawable.botonflankers, R.drawable.botonclaves, R.drawable.botonredes}; //array de figuras en la carpeta drawable

    // Shared Preferences
    SharedPreferences prefs;

    //URL del archivo PHP para crear un producto nuevo
    String url_prueba = "http://104.131.143.76/eap/include/data.php";

    //Nombres de los nodos del JSON
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EXERCISES = "exercises";

    private void noConnectionExecution() {

        try {
            //JSONArray jsonArray2 = new JSONArray(prefs.getString("resultExercises", "[]"));
            JSONArray jsonArray2 = new JSONArray(strEx);

            //if(prefs.getString("resultExercises", "[]") != "[]") {
                ElegirEjercicioObject tempExercise;  //tempExercises object

                // looping through All Products
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject c = jsonArray2.getJSONObject(i);

                    // Se guardan los datos de cada objeto JSON en variables
                    String id = c.getString("id");
                    Log.d("ExID", id);
                    String data = c.getString("data");
                    String sol = c.getString("sol");
                    String exName = c.getString("exName");


                    // adding HashList to ArrayList
                    tempExercise = new ElegirEjercicioObject(Integer.parseInt(id), figureImages[i], data, Integer.parseInt(sol), exNames[i]);



                    list.add(tempExercise); //adding ElegirEjercicioObject to the list
                }

                NUM_PAGES = (int) Math.ceil((double) list.size() / 24);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);
            /*} else {
                Toast.makeText(this, "No hay datos guardados", Toast.LENGTH_SHORT).show();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.select_exercise);
        setContentView(R.layout.activity_screen_slide);

        listener = this;

        /*conntask = new ConnectionTest();
        conntask.setOnResultsListener(ElegirEjercicioActivity.this, url_prueba, listener);
        conntask.execute();*/

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

        list = new ArrayList<ElegirEjercicioObject>();

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        noConnectionExecution();


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
                new RecibirDatosEjercicio().execute();
            } catch (NullPointerException e) {
                Log.d("Class Call", "Not context found");
            }
        } else {
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            noConnectionExecution();
        }

    }


    /**
     * Background Async Task to Create new product
     * */
    class RecibirDatosEjercicio extends AsyncTask<String, String, String> {

        String id, sol, data;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(ElegirEjercicioActivity.this);
            pDialog.setMessage("Buscando Ejercicios...");
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
                //Creaci�n de la cadena para enviar a la base de datos
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("data", "nE1"));

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_prueba, "POST", params);

                Log.d("Create Response", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    exercises = json.getJSONArray(TAG_EXERCISES); //EXERCISES got from the url JSONArray array
                    ElegirEjercicioObject tempExercise;  //tempExercises object
                    int[] figureImages = {R.drawable.botoncancelacion, R.drawable.botoncorsi, R.drawable.botoncptp, R.drawable.botonflankers, R.drawable.botonclaves, R.drawable.botonredes}; //array de figuras en la carpeta drawable

                    // looping through All Products
                    for (int i = 0; i < exercises.length(); i++) {
                        JSONObject c = exercises.getJSONObject(i);

                        // Se guardan los datos de cada objeto JSON en variables
                        String id = c.getString("id");
                        Log.d("ExID", id);
                        String data = c.getString("data");
                        String sol = c.getString("sol");
                        String exName = c.getString("exName");


                        // adding HashList to ArrayList
                        tempExercise = new ElegirEjercicioObject(Integer.parseInt(id), figureImages[i], data, Integer.parseInt(sol), exNames[i]);
                        list.add(tempExercise); //adding ElegirEjercicioObject to the list
                    }

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
            //pDialog.dismiss();
            if (success == "1") {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("resultExercises", exercises.toString());
                Log.d("Saving json", exercises.toString());
                editor.commit();

                NUM_PAGES = (int) Math.ceil((double) list.size()/24);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);

            } else {
                // failed to create product
                Log.d("ERROR!", "Ouch");
            }
        }

    }

    /**
     * A simple pager adapter that represents 5 {@link ElegirEjercicioFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return ElegirEjercicioFragment.create(position, list);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}


