package com.angelicaflores.eap.menuElegirEjercicio;

import java.io.Serializable;

/**
 * Created by DEVELOPEMENT on 27/05/2015.
 *
 * La clase va a tener la imagen y el id de cada imagen
 */
public class ElegirEjercicioObject implements Serializable {
    int exId, imageId;
    String data, exName;
    int sol;

    //contructor
    ElegirEjercicioObject(int exId, int imgId, String data, int sol, String exName){
        this.exId  = exId;
        this.imageId = imgId;
        this.data  = data;
        this.sol = sol;
        this.exName = exName;
    }
}
