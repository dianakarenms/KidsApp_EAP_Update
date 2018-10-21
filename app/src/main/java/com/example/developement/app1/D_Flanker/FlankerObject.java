package com.example.developement.app1.D_Flanker;

/**
 * Created by DEVELOPEMENT on 25/07/2015.
 *
 * Clase  que va a contener la imagen y el id de cada imagen
 */
public class FlankerObject {

    int ImageId;
    String FigureName;
    int Solucion;

    //contructor
    FlankerObject(int imageId, String figureName, int solucion){
        this.ImageId = imageId;
        this.FigureName = figureName;
        this.Solucion = solucion;
    }
}
