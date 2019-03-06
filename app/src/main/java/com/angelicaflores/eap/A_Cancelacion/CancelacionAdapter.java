package com.angelicaflores.eap.A_Cancelacion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.angelicaflores.eap.R;

import java.util.ArrayList;

/**
 * Created by DEVELOPEMENT on 27/05/2015.
 */
//
public class CancelacionAdapter extends ArrayAdapter<CancelacionObject> {

    /** Variables **/
    /* Variables cuyo valor sera el recibido desde la actividad que los mando a llamar */
    Context context;
    int layoutResourceId;
    // ArrayList es un tipo especial de array que no necesita inicializarse con num determinado de elementos
    ArrayList<CancelacionObject> data; // recibe el array que recibe de la Activity todos los objetos de tipo CancelacionObject que se mostraran en la pantalla
    int exSol;

    /* Variables creadas y modificadas internamente */
    private String pickedData = ""; // array para guardar los datos de cada imagen seleccionada
    private long initTime;  //guarda el tiempo de inicio antes del click
    private int numAciertos = 0, numErrores = 0;

    // Adapter construtor
    CancelacionAdapter(Context context, int layoutResourceId, ArrayList<CancelacionObject> data, int exSol){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.exSol = exSol;

        // como configuracion inicial del constructor se guarda el tiempo actual del sistema
        initTime = android.os.SystemClock.uptimeMillis();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CancelacionObject getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder{
        ImageView myFigure;
        ViewHolder(View v){
            myFigure = v.findViewById(R.id.imageViewP);
        }
    }

    public long clickTime(int initTime) {
        long elapsedTime = android.os.SystemClock.uptimeMillis() - initTime;
        return elapsedTime;
    }

    public String getPickedData() {
        return pickedData;
    }

    public int getNumAciertos() {
        return numAciertos;
    }

    public int getNumEComision() {
        return numErrores;
    }

    //
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = null;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_cancelacion,viewGroup, false); //contiene el relative layout de sigle_item.xml

            holder = new ViewHolder(row);
            //asigna una variable nueva al view holder
            holder.myFigure =(ImageButton) row.findViewById(R.id.imageViewP);
            row.setTag(holder);
            holder.myFigure.setTag(position);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        CancelacionObject temp = data.get(position);
        holder.myFigure.setImageDrawable(context.getResources().getDrawable(temp.imageId));
        holder.myFigure.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        holder.myFigure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout ll = (RelativeLayout) v.getParent();
                ImageButton btnImg = ll.findViewById(R.id.imageViewP);
                btnImg.setEnabled(false);

                Log.i("currentID", String.valueOf(data.get(position).counterId));
                Log.i("exSolID", String.valueOf(exSol));

                if (data.get(position).counterId == exSol) {
                    // figura correcta (verde)
                    btnImg.setBackgroundColor(0xFF2fac66);
                    btnImg.setEnabled(false);
                    numAciertos++;
                } else {
                    // figura incorrecta (rojo)
                    btnImg.setBackgroundColor(0xFFbf4c5b);
                    btnImg.setEnabled(false);
                    numErrores++;
                }
                // una vez hecho el click se toma el tiempo actual y se le resta el tiempo anterior
                long elapsedTime = android.os.SystemClock.uptimeMillis() - initTime;

                // se guarda en initTime el tiempo actual para la siguiente pulsacion
                initTime = android.os.SystemClock.uptimeMillis();

                Log.i("elapsedTime", Long.toString(elapsedTime));

                // Se agregan los nuevos datos en el array
                pickedData += data.get(position).figureName + ","; // add figure id
                pickedData += Long.toString(elapsedTime) + ","; // add elapsed time between last click and current click
                pickedData += String.valueOf(position) + ",\n"; // add position value

                Log.i("Control Button Clicked", "**********");
            }
        });


        return row;
    }
}
