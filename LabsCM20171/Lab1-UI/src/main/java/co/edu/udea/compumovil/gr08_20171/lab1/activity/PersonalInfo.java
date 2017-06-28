package co.edu.udea.compumovil.gr08_20171.lab1.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Date;

import co.edu.udea.compumovil.gr08_20171.lab1.util.DatePickerFragment;
import co.edu.udea.compumovil.gr08_20171.lab1.R;

/**
 * @author Cristian Camilo Isaza
 * @email sisaza92@gmail.com
 * @date 17/02/2017
 * Esta clase Controla la vista de la actividad PersonalInfo.
 */
public class PersonalInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText input_nombres,input_apellidos;
    RadioGroup rgSexo;
    Spinner sprGradoEscolaridad;
    Date fechaNaci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        input_nombres = (EditText) findViewById(R.id.input_nombres);
        input_apellidos = (EditText) findViewById(R.id.input_apellidos);
        rgSexo = (RadioGroup) findViewById(R.id.rgSexo);
        sprGradoEscolaridad = (Spinner) findViewById(R.id.sprGradoEscolaridad);
        sprGradoEscolaridad.setPrompt(getResources().getString(R.string.siguiente));
        sprGradoEscolaridad.setSelection(-1);
    }

    /**
     * Método creado para mostrar el diálogo con el
     * selector de fecha y que es accionado por el boton
     * cambiar desde el XML.
     * @param view parámetro requerido para llamar el método a travéz del onClick en el XML.
     */
    public void showDatePicker(View view){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Método ejecutado por el boton siguiente de la actividad y
     * que se encarga de recopilar los datos y lanzar la proxima
     * actividad.
     * @param view parámetro requerido para llamar el método a travéz del onClick en el XML.
     */
    public void showNextActivity(View view){

        if (validarCampos()){
            Bundle datos = new Bundle();

            datos.putString("nombres", input_nombres.getText().toString());
            datos.putString("apellidos", input_apellidos.getText().toString());

            RadioButton rbChecked = (RadioButton) findViewById(rgSexo.getCheckedRadioButtonId());
            datos.putString("sexo", rbChecked.getText().toString());
            datos.putString("fechaNaci",fechaNaci.toString());
            datos.putString("grado", sprGradoEscolaridad.getSelectedItem().toString());

            Intent otherInfo = new Intent(view.getContext(),ContactInfo.class);
            otherInfo.putExtra("datos",datos);
            startActivity(otherInfo);
        }
    }

    /**
     * Este método es activado justo después de haber seleccionado una
     * fecha del cuadro de dialogo.
     * @param view la actividad actual.
     * @param year Año seleccionado.
     * @param month Mes seleccionado.
     * @param dayOfMonth Día seleccionado.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fechaNaci = new Date(year,month,dayOfMonth);
    }

    /**
     * Método para validar los campos de la actividad.
     * @return true si la todos los campos se encuentran bien
     * false de lo contrario.
     */
    private boolean validarCampos(){
        if (input_nombres.getText().length() == 0){
            input_nombres.setError(getString(R.string.error_nombre));
            return false;
        }
        if(input_apellidos.getText().length() == 0){
            input_apellidos.setError(getString(R.string.error_apellidos));
            return false;
        }
        if(fechaNaci==null){
            ((Button)findViewById(R.id.btnFecha)).setError(getString(R.string.error_fecha));
            return false;
        }
        return true;
    }
}
