package com.angelicaflores.Utils;

/**
 * Created by Karencita on 27/02/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;

import com.angelicaflores.eap.R;

import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by DEVELOPEMENT on 26/08/2015.
 */
public final class storeDataInLocalTxt {

    private ConnectionTest conntask;
    private ResultsListener listener;
    private ProgressDialog pDialog;
    private Context context;
    private JSONArray jsonArray;
    private String usrId, exId;
    private String ret="";

    // Shared Preferences
    SharedPreferences prefs;

    //URL del archivo PHP para crear un producto nuevo
    public static String url_json = "http://104.131.143.76/eap/include/data2.php";
    Boolean exitSave;
    private int tryNumber = 1;
    private File dir;
    private File file;
    //String statusTxt;

    public storeDataInLocalTxt(Context context){
        this.context = context;
    }

    public void saveData(String data){
        prefs = context.getSharedPreferences(Constants.prefsName, MODE_PRIVATE);

        if(!prefs.getString("userId", "").isEmpty()) {
            usrId = prefs.getString("userId", "");
            exId = prefs.getString("exerciseId", "");
        }

        writeFileExternalStorage(data);
        /*try {
            File myFile = new File("/sdcard/");

            File file[] = myFile.listFiles();
            if(file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    if (file[i].getName().contains("EAPdata")) {
                        myFile = new File("/sdcard/" + file[i].getName());
                        FileInputStream fIn = new FileInputStream(myFile);
                        ret = convertStreamToString(fIn);
                        break;
                    }
                }

                if(!myFile.toString().contains("EAPdata")){
                    try {

                        Date today = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                        String folderName = format.format(today);

                        File dataFile = new File("/sdcard/EAPdata" + folderName + ".txt");
                        dataFile.createNewFile();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        try{
            JSONObject usrAnswer = new JSONObject();
            usrAnswer.put("usrId", usrId);
            usrAnswer.put("exId", exId);
            data = data.replace(" ", "");
            usrAnswer.put("data", data);

            if(usrAnswer.has("usrId") == false)
                usrAnswer.put("usrId", usrId);

            if(usrAnswer.has("exId") == false)
                usrAnswer.put("exId", exId);

            if(usrAnswer.has("data") == false)
                usrAnswer.put("data", data);


            if(ret != "") {
                jsonArray = new JSONArray(ret);
            } else {
                jsonArray = new JSONArray();
            }

            jsonArray.put(usrAnswer);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Append new JSON Formatted Data in EAPdata.txt
        try {
            // Upload some files.
            File myFile = new File("/sdcard");
            File file[] = myFile.listFiles();
            for (int i=0; i < file.length; i++) {
                if(file[i].getName().contains("EAPdata")){
                    myFile = new File("/sdcard/"+file[i].getName());
                    break;
                }
            }

            FileOutputStream fOut = new FileOutputStream(myFile, false);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);

            myOutWriter.append(jsonArray.toString());
            myOutWriter.close();
            fOut.close();
            Toast.makeText(context,
                    "Guardado Exitoso",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }*/

    }

    public void writeFileExternalStorage(String data) {

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

        createNewTry();

        //This point and below is responsible for the write operation
        FileOutputStream outputStream;
        try {
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);

            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    public void uploadDataToServer(Context context) {
        this.context = context;
        new uploadThread().execute();
    }

    class uploadThread extends AsyncTask<Boolean, Boolean, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Subiendo Datos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            boolean success;
            try {
                SimpleFTP ftp = new SimpleFTP();

                // Connect to an FTP server on port 21.
                ftp.connect("104.131.143.76", 21, "john", "onlyhy");

                // Set binary mode.
                ftp.bin();

                // Change to a new working directory on the FTP server.
                success = ftp.cwd("eap/include/data/");
                if (success != true) {
                    Log.i("ERROR", ftp.pwd());
                    return false;
                }

                // Upload some files.
                File myFile = new File("/sdcard");
                File file[] = myFile.listFiles();
                for (int i=0; i < file.length; i++) {
                    if(file[i].getName().contains("EAPdata")){
                        myFile = new File("/sdcard/"+file[i].getName());
                        break;
                    }
                }

                success = ftp.stor(myFile);
                if (success != true) {
                    Log.i("ERROR", ftp.pwd());
                    return false;
                } else {
                    // delete file
                    //myFile.getCanonicalFile().delete();
                    FileOutputStream fOut = new FileOutputStream(myFile, false);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);

                    myOutWriter.write("");
                    myOutWriter.flush();
                    myOutWriter.close();
                    fOut.close();

                    // Quit from the FTP server.
                    ftp.disconnect();

                    // Upload some files.
                    File from = new File("/sdcard");
                    File files[] = from.listFiles();
                    for (int i=0; i < files.length; i++) {
                        if(files[i].getName().contains("EAPdata")){
                            from = new File("/sdcard/"+files[i].getName());
                            break;
                        }
                    }

                    Date today = Calendar.getInstance().getTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                    String folderName = format.format(today);
                    File to = new File("/sdcard/EAPdata" + folderName + ".txt");

                    from.renameTo(to);

                    return true;
                }

            } catch (IOException e) {
                //Log.d("ERROR",ftp.);
                return false;
            }

        }

        /*
          * After completing background task Dismiss the progress dialog
          */
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            // dismiss the dialog once done
            pDialog.dismiss();
            if(success == true) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(Html.fromHtml("<font color='#B0171F'>SUBIDA EXITOSA</font>"));
                builder.setIcon(R.drawable.cloudicon);
                builder.setMessage("Se han guardado los datos");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog alert = builder.create();

                Activity activity = (Activity)context;
                activity.runOnUiThread(new java.lang.Runnable() {
                    public void run() {
                        //show AlertDialog
                        alert.show();
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error en guardado");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Verifique su conexión a internet");
                builder.setPositiveButton("Ok", null);
                final AlertDialog alert = builder.create();

                Activity activity = (Activity)context;
                activity.runOnUiThread(new java.lang.Runnable() {
                    public void run() {
                        //show AlertDialog
                        alert.show();
                    }
                });
            }
        }


    }


}