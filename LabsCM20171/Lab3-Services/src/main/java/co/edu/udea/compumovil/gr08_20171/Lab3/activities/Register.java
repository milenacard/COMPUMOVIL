package co.edu.udea.compumovil.gr08_20171.Lab3.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import co.edu.udea.compumovil.gr08_20171.Lab3.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class Register extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private EditText mAge;
    private EditText mName;
    private Button buttonSaveRegister;
    String urlPost;

    public String username, password, email, age, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Register");

        mUsername = (EditText) findViewById(R.id.username_register);
        mPassword = (EditText) findViewById(R.id.password_register);
        mEmail = (EditText) findViewById(R.id.email_register);
        mAge = (EditText) findViewById(R.id.age_register);
        mName = (EditText) findViewById(R.id.name_register);
        buttonSaveRegister = (Button) findViewById(R.id.button_saveregister);

        buttonSaveRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo(view);
            }
        });


    }


    //Guarda la informacion en la base de datos
    public void saveInfo(View v) {
        mUsername.setError(null);
        mPassword.setError(null);
        mEmail.setError(null);
        mAge.setError(null);
        mName.setError(null);

        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        email = mEmail.getText().toString();
        age = mAge.getText().toString();
        name = mName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }else if (!TextUtils.isEmpty(username) && (username.length()<4)) {
            mUsername.setError(getString(R.string.error_invalid_username));
            focusView = mUsername;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(password) && (password.length()<5)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }
        else if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        } else if (!TextUtils.isEmpty(name) && !validarNombre(name,5)) {
            mName.setError(getString(R.string.error_name));
            focusView = mName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!TextUtils.isEmpty(email) && !esCorreoValido(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(age)) {
            mAge.setError(getString(R.string.error_field_required));
            focusView = mAge;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(age) && !validarEdad(age, 3)) {
            mAge.setError(getString(R.string.error_age));
            focusView = mAge;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            sendRequest(username, password, name, email, age);
        }
    }


    private void sendRequest(final String username, final String password, final String name, final String email, final String age) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                urlPost = "http://" + IpDispositivoServicio.ip + ":3000/api/users";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest
                        (Request.Method.POST,
                                urlPost,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Response", response);
                                        Toast.makeText(getApplicationContext(), "Los datos han sido guardados correctamente", Toast.LENGTH_SHORT).show();
                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error registrando el usuario", Toast.LENGTH_SHORT).show();
                                // Log.d("ErrorPeticion", "Error: " + error.toString());

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("password", password);
                        params.put("nombre", name);
                        params.put("edad", age);
                        params.put("email", email);
                        return params;
                    }
                };
                queue.add(stringRequest);
                finish();
            }
        });
        hilo.start();
    }


    //Funciones communes

    public boolean validarNombre(String name, int limit) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        return patron.matcher(name).matches() && name.length() > limit;
    }

    public boolean esCorreoValido(String correo) {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }
    private boolean validarEdad(String age, int i) {
        return (age.length() < i);
    }
}
