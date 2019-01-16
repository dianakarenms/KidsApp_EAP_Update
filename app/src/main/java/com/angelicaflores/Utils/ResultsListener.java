package com.angelicaflores.Utils;

/**
 * Esta interfaz "escucha" si se ha ejecutado la AsyncTask ConnectionTest
 * y le devuelve el resultado de la AsyncTask a la Actividad que la ha llamado 
 */

public interface ResultsListener {
    public void onResultsSucceeded(Boolean result);
}


/**
 * Activities that wish to be notified about results
 * in onPostExecute of an AsyncTask must implement
 * this interface.
 *
 * This is the basic Observer pattern.
 */
