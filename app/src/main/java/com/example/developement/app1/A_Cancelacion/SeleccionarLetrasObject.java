package com.example.developement.app1.A_Cancelacion;

/**
 * Created by DEVELOPEMENT on 27/05/2015.
 *
 * La clase va a tener la imagen y el id de cada imagen
 */
public class SeleccionarLetrasObject {
    int imageId;
    String figureName;
    int counterId;

    //contructor
    SeleccionarLetrasObject(int imageId, String figureName, int counterId){
        this.imageId = imageId;
        this.figureName = figureName;
        this.counterId = counterId;
    }
}
