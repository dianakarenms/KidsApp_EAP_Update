/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.developement.app1.menuElegirEjercicio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.developement.app1.Login.LoginActivity;
import com.example.developement.app1.R;

import java.util.ArrayList;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 *
 */
public class ElegirEjercicioFragment extends android.support.v4.app.Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private ArrayList<ElegirEjercicioObject> list;

    ElegirEjercicioAdapter adapter;
    GridView myGrid;
    Button salirBtn;
    TextView titleTxt;
    Context context = null;

    // Shared Preferences
    SharedPreferences prefs;

    String usrId;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ElegirEjercicioFragment create(int pageNumber, ArrayList<ElegirEjercicioObject> list) {
        ElegirEjercicioFragment fragment = new ElegirEjercicioFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    public ElegirEjercicioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        list = (ArrayList<ElegirEjercicioObject>)  getArguments().getSerializable("list");

        prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        if(prefs.getString("userId", "") != "")
            usrId = prefs.getString("userId", "");

        this.context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.select_exercise, container, false);

        myGrid = (GridView) rootView.findViewById(R.id.gridView);
        titleTxt = (TextView) rootView.findViewById(R.id.titleTxt);
        salirBtn = (Button) rootView.findViewById(R.id.salirBtn);
        salirBtn.setOnClickListener(onClick);
        String font_path = "font/ChalkboardSE.ttc";
        Typeface TF = Typeface.createFromAsset(getActivity().getAssets(), font_path);
        titleTxt.setTypeface(TF, Typeface.BOLD);
        salirBtn.setTypeface(TF, Typeface.BOLD);
        titleTxt.setText("¡Hola " + usrId + "!");

        ArrayList<ElegirEjercicioObject> exIdArray = new ArrayList<>();

        int size = (int) Math.ceil((double) list.size()/24);
        Log.d("pagination", String.valueOf(size));
       /* if( size > 1 ) {
            finalizarBtn.setText("Siguiente");
            Log.d("siguiente", "");
        } else {
            finalizarBtn.setText("Finalizar");
        }*/


            int maxVal = (mPageNumber + 1) * 24;
            int minVal = maxVal - 24;
            Log.d("pageNum", String.valueOf(mPageNumber));
            Log.d("maxVal", String.valueOf(maxVal));
            if (list.size() < maxVal)
                maxVal = list.size();

            Log.d("maxVal", String.valueOf(maxVal));
            Log.d("minVal", String.valueOf(minVal));

            for (int j = minVal; j < maxVal; j++) {
                exIdArray.add(list.get(j));
            }


       /* int counter = 1;
        int maxSize = 0;
        if(list.size() <24)
            maxSize = list.size();
        else
            maxSize = 24;
        while( counter <= maxSize ) {
            tempFigure = new SeleccionarLetrasObject(figureImages[figArray.get(0)], tempFigureNames[figArray.get(0)], figArray.get(0));
            list.add(tempFigure);
            figArray.remove(0);
            counter ++;
        }*/

        adapter = new ElegirEjercicioAdapter(getActivity(), R.layout.elegir_ejercicio_item, exIdArray);
        myGrid.setAdapter(adapter);

        // Set the title view to show the page number.
        /*((TextView) rootView.findViewById(android.R.id.text1)).setText(
                getString(R.string.title_template_step, mPageNumber + 1));

        if(mPageNumber == 0){
            ((TextView) rootView.findViewById(android.R.id.text2)).setText("La pruebita que se la de su abuelitaaa turun");
        } else {
            ((TextView) rootView.findViewById(android.R.id.text2)).setText("Lolo");
        }*/

        return rootView;
    }

    View.OnClickListener onClick =  new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.salirBtn:

                    // Return to login since all data is stored on db
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("userId").commit();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    ElegirEjercicioFragment.this.startActivity(i);
                    Activity activity = (Activity) context;
                    activity.finish();

                    /*if(prefs.getString("resultKids", "[]") != "[]") {
                        storeUserData store = new storeUserData();
                        store.firstConnStorage(context);
                    } else{
                        // Return to login since all data is stored on db
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("userId").commit();
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        ElegirEjercicioFragment.this.startActivity(i);
                        Activity activity = (Activity) context;
                        activity.finish();
                    }*/
                    break;
                    }
            }
    };

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
