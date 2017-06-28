package co.edu.udea.compumovil.gr08_20171.lab1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import co.edu.udea.compumovil.gr08_20171.lab1.R;

public class ContactInfo extends AppCompatActivity {

    EditText telefonoInfoContact,correoInfoContact,direccionInfoContact;
    AutoCompleteTextView autoCompletePaises, autoCompleteCiudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        telefonoInfoContact = (EditText)findViewById(R.id.telefonoInfoContact);
        correoInfoContact = (EditText)findViewById(R.id.correoInfoContact);
        direccionInfoContact = (EditText)findViewById(R.id.direccionInfoContact);
        autoCompletePaises = (AutoCompleteTextView) findViewById(R.id.AutoCompletePaises);
        autoCompleteCiudades = (AutoCompleteTextView) findViewById(R.id.AutoCompleteCiudades);

        // Get the string array
        String[] paises = getResources().getStringArray(R.array.paises_arrays);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterPais = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paises);
        autoCompletePaises.setAdapter(adapterPais);

        // Get the string array
        String[] ciudades = getResources().getStringArray(R.array.ciudades_arrays);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterCiudad = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ciudades);
        autoCompleteCiudades.setAdapter(adapterCiudad);
    }

    /**
     * Método ejecutado por el boton siguiente de la actividad y
     * que se encarga de recopilar los datos y lanzar la proxima
     * actividad.
     * @param view parámetro requerido para llamar el método a travéz del onClick en el XML.
     */
    public void showNextActivity(View view){

        if (validarCampos()){
            Bundle datos = getIntent().getBundleExtra("datos");

            datos.putString("telefono", telefonoInfoContact.getText().toString());
            datos.putString("correo", correoInfoContact.getText().toString());
            datos.putString("ciudad", autoCompleteCiudades.getText().toString());
            datos.putString("pais", autoCompletePaises.getText().toString());
            datos.putString("direccion", direccionInfoContact.getText().toString());

            Intent otherInfo = new Intent(view.getContext(),OtherInfo.class);
            otherInfo.putExtra("datos",datos);
            startActivity(otherInfo);
        }


    }

    /**
     * Método para validar los campos de la actividad.
     * @return true si la todos los campos se encuentran bien
     * false de lo contrario.
     */
    private boolean validarCampos(){
        if (telefonoInfoContact.getText().length() == 0){
            telefonoInfoContact.setError(getString(R.string.error_telefono));
            return false;
        }
        if(autoCompletePaises.getText().length() == 0){
            autoCompletePaises.setError(getString(R.string.error_pais));
            return false;
        }
        if(autoCompleteCiudades.getText().length() == 0){
            autoCompleteCiudades.setError(getString(R.string.error_ciudad));
            return false;
        }
        return true;
    }
}
