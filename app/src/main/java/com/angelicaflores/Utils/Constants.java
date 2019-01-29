package com.angelicaflores.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static String prefsName = "com.angelicaflores.eap";

    public static HashMap<Integer, String> gamesNames = new HashMap<Integer, String>() {{
        put(1,"Cancelacion");
        put(2,"CPTP");
        put(3,"Flanker");
        put(4,"Claves");
        put(5,"Corsi");
        put(6,"Redes");
    }};

    private static String exerciseHeader(int exerciseId) {
        List<String> headers = new ArrayList<>();
        switch (exerciseId) {
            case 1: // Cancelacion
                headers.add("FIGURA");
                headers.add("TIEMPO");
                headers.add("POSICIÓN");
                break;
            case 2: // CPTP
                headers.add("Figura");
                headers.add("N. ENSAYO");
                headers.add("TIEMPO");
                break;
            case 3: // Flanker
                headers.add("NOMBRE");
                headers.add("TIEMPO");
                headers.add("RESULTADO");
                headers.add("N. ENSAYO");
                break;
            case 4: // Claves
                headers.add("RESUTADO");
                headers.add("N. ENSAYO");
                headers.add("TIEMPO");
                break;
            case 5: // Corsi
                headers.add("POSICIÓN");
                headers.add("TIEMPO");
                break;
            case 6: // Redes
                headers.add("IMAGEN");
                headers.add("N. ENSAYO");
                headers.add("TIEMPO");
                headers.add("RESPUESTA");
                break;
        }
        String formattedHeader = "";
        for(int i = 0; i < headers.size(); i++) {
            formattedHeader += headers.get(i) + ",";
        }
        return formattedHeader;
    }

    public static String getCurrentTime() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return format.format(today);
    }

    public static String getExerciseHeader(int exerciseId) {
        String formattedData = "";
        formattedData += gamesNames.get(exerciseId) + ",";
        formattedData += Constants.getCurrentTime() + ",,\n";
        formattedData += exerciseHeader(exerciseId) + "\n";
        return formattedData;
    }

    public static String getComputedDataHeader() {
        return "NumEnsayo,TR,TRPromedio,N_Aciertos,N_EComisión,N_EOmisión,P_EComisión,P_EOmisión,NumEstimulos,\n";
    }

}
