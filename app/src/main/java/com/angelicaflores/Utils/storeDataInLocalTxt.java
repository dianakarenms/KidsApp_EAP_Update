package com.angelicaflores.Utils;

/**
 * Created by Karencita on 27/02/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by DEVELOPEMENT on 26/08/2015.
 */
public final class storeDataInLocalTxt {

    private Context context;
    private String usrId, exId;
    SharedPreferences prefs;
    private int tryNumber = 1;
    private File dir;
    private File file;

    public storeDataInLocalTxt(Context context){
        this.context = context;
    }

    public void saveData(String rawData, String computedData){
        prefs = context.getSharedPreferences(Constants.prefsName, MODE_PRIVATE);

        if(!prefs.getString("userId", "").isEmpty()) {
            usrId = prefs.getString("userId", "");
            exId = prefs.getString("exerciseId", "");
        }

        writeFileExternalStorage(rawData, computedData);

    }

    public void writeFileExternalStorage(String rawdata, String computedData) {

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        //Create a new file that points to the root directory, with the given name:
        dir = new File(context.getExternalFilesDir(null), "EAPdata/");
        if(!dir.exists()) {
            if(!dir.mkdirs()) {
                Log.e("ALERT", "could not create directories");
            }
        }

        /** Raw data file */
        createNewTry();

        //This point and below is responsible for the write operation
        FileOutputStream outputStream;
        try {
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);

            outputStream.write(rawdata.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!computedData.isEmpty()) {
            /** Computed data file */
            file = new File(dir, usrId + "_" + Constants.gamesNames.get(Integer.valueOf(exId)) + "_" + tryNumber + "_Resultados.txt");

            try {
                //second argument of FileOutputStream constructor indicates whether
                //to append or create new file if one exists
                outputStream = new FileOutputStream(file, true);

                outputStream.write(computedData.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createNewTry() {
        file = new File(dir,  usrId + "_" + Constants.gamesNames.get(Integer.valueOf(exId)) + "_" + tryNumber + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                tryNumber ++;
                createNewTry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}