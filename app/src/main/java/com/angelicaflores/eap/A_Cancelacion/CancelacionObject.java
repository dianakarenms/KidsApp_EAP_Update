package com.angelicaflores.eap.A_Cancelacion;

/**
 * Created by DEVELOPEMENT on 27/05/2015.
 *
 * La clase va a tener la imagen y el id de cada imagen
 */
public class CancelacionObject {
    int imageId;
    String figureName;
    int counterId;

    //constructor
    CancelacionObject(int imageId, String figureName, int counterId){
        this.imageId = imageId;
        this.figureName = figureName;
        this.counterId = counterId;
    }
}
