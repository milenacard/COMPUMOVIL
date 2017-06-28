package co.edu.udea.compumovil.gr08_20171.lab1.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Esta clase Representa el cuadro de dialogo de selección
 * de fecha.
 * @author Cristian Camilo Isaza
 * @email  sisaza@gmail.com
 * @date 15/02/2017
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int ano = calendario.get(Calendar.YEAR);

        // Create a new instance of TimePickerDialog and return it
        DatePickerDialog dialogoFecha = new DatePickerDialog(getActivity(),this,ano,mes,dia);
        return dialogoFecha;
    }

    /**
     * Este método es activado justo después de haber seleccionado una
     * fecha del cuadro de dialogo y está encargado de llamar el Listener
     * OnDateSet de la actividad que solicito el cuadro de dialogo.
     * @param view la actividad actual.
     * @param year Año seleccionado.
     * @param month Mes seleccionado.
     * @param dayOfMonth Día seleccionado.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ((DatePickerDialog.OnDateSetListener)getActivity()).onDateSet(view,year,month,dayOfMonth);
    }
}
