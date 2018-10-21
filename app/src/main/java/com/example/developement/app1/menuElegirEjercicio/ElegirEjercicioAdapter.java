package com.example.developement.app1.menuElegirEjercicio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.developement.app1.C_CPT.Instr_CPT;
import com.example.developement.app1.B_Corsi.Instr_Corsi;
import com.example.developement.app1.D_Flanker.Instr_Flanker;
import com.example.developement.app1.E_ClavesCentrales.Instr_Claves;
import com.example.developement.app1.R;
import com.example.developement.app1.A_Cancelacion.SeleccionarLetrasActivity;
import com.example.developement.app1.F_RedesAtencionales.Instr_Redes;

import java.util.ArrayList;

/**
 * Created by DEVELOPEMENT on 27/05/2015.
 */
//
public class ElegirEjercicioAdapter extends ArrayAdapter<ElegirEjercicioObject>
{

    /** Variables **/
    /* Variables cuyo valor sera el recibido desde la actividad que los mando a llamar */
    Context context;
    int layoutResourceId;

    // ArrayList es un tipo especial de array que no necesita inicializarse con num determinado de elementos
    ArrayList<ElegirEjercicioObject> data; // recibe el array que recibe de la Activity todos los objetos de tipo SeleccionarLetrasObject que se mostraran en la pantalla
    ViewHolder holder = null;

    // Adapter construtor
    ElegirEjercicioAdapter(Context context, int layoutResourceId, ArrayList<ElegirEjercicioObject> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ElegirEjercicioObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View row = view;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.elegir_ejercicio_item,viewGroup, false); //contiene el relative layout de sigle_item.xml

            holder = new ViewHolder();
            //asigna una variable nueva al view holder
            holder.exImg = (SquareImageButton) row.findViewById(R.id.imageViewP);
            holder.exTitle = (TextView) row.findViewById(R.id.exTitleTxt);
            row.setTag(holder);
            holder.exImg.setTag(position);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        ElegirEjercicioObject temp = data.get(position);
        holder.exImg.setImageResource(data.get(position).imageId);
        holder.exTitle.setText(data.get(position).exName);
        String font_path = "font/ChalkboardSE.ttc";
        Typeface TF = Typeface.createFromAsset(context.getAssets(), font_path);
        holder.exTitle.setTypeface(TF, Typeface.BOLD);

        holder.exImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int wantedPosition = (Integer) v.getTag();
                //RelativeLayout ll = (RelativeLayout) v.getParent();
                //ImageButton btnImg = (ImageButton) ll.findViewById(R.id.imageView);
                //controlClass control;
                // onOff = ((ControlDispActivity)context).controlMethod(onOff);
                //if(onOff)
                //btnOnOff.setBackgroundResource(R.drawable.stop);
                //else
                //  btnOnOff.setBackgroundResource(R.drawable.play);


                // Log.i("currentID", String.valueOf(data.get(position).counterId));

               /* if (data.get(position).counterId == exSol) {
                    // seleccion de la figura correcta (verde)
                    btnImg.setImageResource(R.drawable.correct);
                } else {
                    // figura incorrecta (rojo)
                    btnImg.setImageResource(R.drawable.incorrect);
                }*/
                Intent i = new Intent();
                switch (data.get(position).exId) {
                    case 1:
                        i = new Intent(context, SeleccionarLetrasActivity.class);
                        i.putExtra("exSol", Integer.toString(data.get(position).sol));
                        i.putExtra("orderArr", data.get(position).data);
                        i.putExtra("tempData", "");
                        break;
                    case 2:
                        i = new Intent(context, Instr_CPT.class);
                        break;
                    case 3:
                        i = new Intent(context, Instr_Flanker.class);
                        break;
                    case 4:
                        i = new Intent(context, Instr_Claves.class);
                        break;
                    case 5:
                        i = new Intent(context, Instr_Corsi.class);
                        break;
                    case 6:
                        i = new Intent(context, Instr_Redes.class);
                        break;
                }

                context.startActivity(i);

                // TODO Auto-generated method stub
                Log.i("Control Button Clicked", "**********");
                //Toast.makeText(context, "Posicion: " + wantedPosition,
                //        Toast.LENGTH_SHORT).show();
                ;
            }
        });


        return row;

    }

    static class ViewHolder
    {
        SquareImageButton exImg;
        TextView exTitle;
    }
}
