package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.activities.MenuPrincipal;
import co.edu.udea.compumovil.gr08_20171.Lab2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNuevoEvento extends Fragment {

    private EditText newNameEvent;
    private EditText newResponsibleEvent;
    private EditText newDateEvent;
    private EditText newScoreEvent;
    private EditText newDescriptionEvent;
    private EditText newGralInfoEvent;
    private Button createEvent;
    public  FragmentManager fragmentManager;


    public FragmentNuevoEvento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nuevo_evento, container, false);

        getActivity().setTitle("Nuevo Evento");

        newNameEvent = (EditText) view.findViewById(R.id.newevent_name_event);
        newResponsibleEvent = (EditText) view.findViewById(R.id.newevent_responsible);
        newDateEvent = (EditText) view.findViewById(R.id.newevent_fecha_event);
        newScoreEvent = (EditText) view.findViewById(R.id.newevent_score);
        newDescriptionEvent = (EditText) view.findViewById(R.id.newevent_description);
        newGralInfoEvent = (EditText) view.findViewById(R.id.newevent_infogeneral);
        createEvent = (Button) view.findViewById(R.id.btn_crear_carrera);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*saveEvent(view);*/
            }
        });
         return view;
    }

    /*private void saveEvent(View view){
        String nameEvent = newNameEvent.getText().toString();
        String responsable = newResponsibleEvent.getText().toString();
        String date = newDateEvent.getText().toString();
        String score = newScoreEvent.getText().toString();
        String descripcion = newDescriptionEvent.getText().toString();
        String infoEvent = newGralInfoEvent.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(nameEvent)) {
            newNameEvent.setError(getString(R.string.error_field_required));
            focusView = newNameEvent;
            cancel = true;

        } else if (TextUtils.isEmpty(responsable)) {
            newResponsibleEvent.setError(getString(R.string.error_field_required));
            focusView = newResponsibleEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(date)) {
            newDateEvent.setError(getString(R.string.error_field_required));
            focusView = newDateEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(descripcion)) {
            newDescriptionEvent.setError(getString(R.string.error_field_required));
            focusView = newDescriptionEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(infoEvent)) {
            newGralInfoEvent.setError(getString(R.string.error_field_required));
            focusView = newGralInfoEvent;
            cancel = true;
        } else if (TextUtils.isEmpty(score)) {
            newScoreEvent.setError(getString(R.string.error_field_required));
            focusView = newScoreEvent;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            DbHelper dbHelper = new DbHelper(getActivity()); //Instancia de DbHelper
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Obtener instancia de la BD
            if(dbHelper.createEventDB(db, nameEvent, responsable,Float.parseFloat(score),date,descripcion,infoEvent)){
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_menu_principal, new FragmentEvents())
                        .commit();
            }else{
                Snackbar.make(view.findViewById(R.id.content_menu_principal), "No se pudo guardar evento", Snackbar.LENGTH_LONG)
                        .setAction("action", null).show();
            }


        }
    }*/

}
