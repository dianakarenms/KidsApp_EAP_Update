package com.example.developement.app1;

/**
 * Clase que implementa una Tarea Asincrona para corroborar 
 * que existe acceso a internet. 
 * Evita la terminaci�n del programa cada vez que se intenta 
 * acceder a la base de datos sin conexion a internet.
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionTest extends AsyncTask<Void, Void, Boolean> {
	
	//Declaracion de variables
	private Context context;	//Contexto de la actividad que ejecut� a ConnectionTest
	private String url;
    private ResultsListener listener;	//Variable para implementar el listener de ConnectionTest
    private static final String LOG_TAG = null;		//
    
	 // Progress Dialog
    //private ProgressDialog pDialog;
    
    //Metodo que recibe la informacion que ConnectionTest necesita
    public void setOnResultsListener(Context context, String url, ResultsListener listener) {
        this.context = context;
        this.url = url;
        this.listener = listener;
    }
    
    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*pDialog = new ProgressDialog(this.context);
        pDialog.setMessage("Checando conexión...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/
    }
    
    @Override
	protected Boolean doInBackground(Void... params) {
    	//Checa si existe una conexion disponible
    	ConnectivityManager check = (ConnectivityManager) 
    			this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo activeNetworkInfo = check.getActiveNetworkInfo();
		   
		if(activeNetworkInfo != null){
			//Comprueba que hay acceso a internet
			try {
	                HttpURLConnection urlc = (HttpURLConnection) 
	                		(new URL(url).openConnection());
	                		//(new URL("http://www.google.com/").openConnection());	
	                urlc.setRequestProperty("User-Agent", "Test");
	                urlc.setRequestProperty("Connection", "close");
	                urlc.setConnectTimeout(5000);
	                urlc.connect();
	                return (urlc.getResponseCode() == 200);
	            } catch (java.net.SocketTimeoutException e) {
					Log.d(LOG_TAG, "Checking Network Is Taking So Long");
					return false;
				} catch (IOException e) {
	            	Log.d(LOG_TAG, "Error checking internet connection");
					return false;
	            }
		   	} else {
               Log.d(LOG_TAG, "No network available!");
		   	}
        return false; 
	}
	
	@Override 
	protected void onPostExecute(Boolean result) {
        // dismiss the dialog once got all details
        //pDialog.dismiss();
		//Regresa a la Actividad el resultado de la prueba de conexion
		listener.onResultsSucceeded(result);
	  	}
	}
