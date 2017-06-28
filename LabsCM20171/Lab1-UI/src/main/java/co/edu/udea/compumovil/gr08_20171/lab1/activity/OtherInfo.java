package co.edu.udea.compumovil.gr08_20171.lab1.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import co.edu.udea.compumovil.gr08_20171.lab1.R;

/**
 * @author Cristian Camilo Isaza
 * @email sisaza92@gmail.com
 * @date 17/02/2017
 * Esta clase Controla la vista de la actividad OtherInfo.
 */
public class OtherInfo extends AppCompatActivity {

    TextView tvResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);

        tvResumen = (TextView)findViewById(R.id.tvResumen);
    }

    /**
     * Método que maneja el evento onclick de el botón
     * para mostrar el resumen de la información recolectada.
     * @param view parámetro requerido para llamar el método a travéz del onClick en el XML.
     */
    public void mostrarInformacion(View view){

        Bundle datos = getIntent().getBundleExtra("datos");

        String nombres = datos.getString("nombres");
        String apellidos = datos.getString("apellidos");
        String sexo = datos.getString("sexo");
        String fechaNaci = datos.getString("fechaNaci");
        String gradoEscolar = datos.getString("grado");
        String telefono = datos.getString("telefono");
        String correo = datos.getString("correo");
        String pais = datos.getString("pais");
        String ciudad = datos.getString("ciudad");
        String direccion = datos.getString("direccion");
        

        tvResumen.setText(
                        getResources().getString(R.string.nombres)+" :"+nombres+
                        "\n"+getResources().getString(R.string.apellidos)+" :"+apellidos+
                        "\n"+getResources().getString(R.string.sexo)+" :"+sexo+
                        "\n"+getResources().getString(R.string.fechaNaci)+" :"+fechaNaci+
                        "\n"+getResources().getString(R.string.gradoEscol)+" :"+gradoEscolar+
                        "\n"+getResources().getString(R.string.telefono)+" :"+telefono+
                        "\n"+getResources().getString(R.string.correo)+" :"+correo+
                        "\n"+getResources().getString(R.string.pais)+" :"+pais+
                        "\n"+getResources().getString(R.string.ciudad)+" :"+ciudad+
                        "\n"+getResources().getString(R.string.direccion)+" :"+direccion
        );

    }
}
