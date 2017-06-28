package co.edu.udea.compumovil.gr08_20171.Lab3.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

import co.edu.udea.compumovil.gr08_20171.Lab3.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class Login extends AppCompatActivity {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private final String TAG = "VolleyGSON";
    private ArrayList<String> result;

    // UI references.
    private AutoCompleteTextView usernameView;
    private EditText mPasswordView;
    private Button buttonRegister;
    private Button signInButton;
    String urlLogin;

    SharedPreferences sesionSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");

       /* dbHelper = new DbHelper(this); //Instancia de DbHelper
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBInfomation.ColumnUser.ID, "1"); //Se pasan pares nombre-valor
        values.put(DBInfomation.ColumnUser.NAME, "Luisa");
        values.put(DBInfomation.ColumnUser.PASSWORD, "1234567");
        values.put(DBInfomation.ColumnUser.EMAIL, "luisa@gmail.com");
        values.put(DBInfomation.ColumnUser.AGE, "12");
        values.put(DBInfomation.ColumnUser.USERNAME, "Luisita");
        values.put(DBInfomation.ColumnUser.PHOTO, String.valueOf(R.drawable.ic_face_black_24px));
        db.insertWithOnConflict(DBInfomation.USER_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        */

        sesionSave = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        Log.i("save", sesionSave.getString("email", ""));

        usernameView = (AutoCompleteTextView) findViewById(R.id.username_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);
        result = new ArrayList<String>();


        final Context context = usernameView.getContext();
        signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                urlLogin = "http://" + IpDispositivoServicio.ip + ":3000/api/users/findOne?filter[where][username]=";
                urlLogin = urlLogin + usernameView.getText().toString();
                sendRequest(urlLogin);

            }
        });

        buttonRegister = (Button) findViewById(R.id.register_button);

        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intRegister = new Intent(Login.this, Register.class);
                    startActivity(intRegister);
                } catch (Exception e) {
                    //Snackbar.make(findViewById(R.id.login_layout), "Try again later.", Snackbar.LENGTH_LONG).setAction("action", null).show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!sesionSave.getString("email", "").equals("")) {
            Intent intMain = new Intent(this, MenuPrincipal.class);
            intMain.putExtra(DBInfomation.ColumnUser.USERNAME, sesionSave.getString("username", ""));
            intMain.putExtra(DBInfomation.ColumnUser.EMAIL, sesionSave.getString("email", ""));
            intMain.putExtra(DBInfomation.ColumnUser.AGE, sesionSave.getString("age", ""));
            intMain.putExtra(DBInfomation.ColumnUser.PHOTO, sesionSave.getString("photo", ""));
            intMain.putExtra(DBInfomation.ColumnUser.NAME, sesionSave.getString("name",""));
            startActivity(intMain);
            finish();
        }
    }

    private void sendRequest(final String URL) {
        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("hola");
                System.out.println(URL);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET,
                                URL,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(URL);

                                        System.out.println(response.toString());

                                        JsonParser jsonParser = new JsonParser();
                                        JsonObject jo = (JsonObject) jsonParser.parse(response.toString());

                                        //JsonArray jsonArr = jo.getAsJsonArray("Login");

                                        Gson gson;
                                        gson = new Gson();
                                        user datosUser = gson.fromJson(jo, user.class);

                                        if (datosUser != null) {
                                            attemptLogin(datosUser);
                                            Log.d(TAG, "*datosUser: " + datosUser.getUsername());
                                            result.add("password: " + datosUser.getPassword()
                                                    + ", Email:" + datosUser.getEmail()
                                                    + ", Name :" + datosUser.getName()
                                                    + ", edad :" + datosUser.getAge()
                                                    + ", foto :" + datosUser.getPhoto());
                                        } else
                                            Log.d(TAG, "**datosUser is null ");
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                attemptLogin(null);
                                Log.d("Datos Login", "Error: " + error.toString());

                            }
                        }
                        );
                queue.add(jsObjRequest);


            }

        });
        hilo.start();
    }

    private void attemptLogin(user datos) {

        if (datos != null) {
            usernameView.setError(null);
            mPasswordView.setError(null);

            String username = usernameView.getText().toString();
            String password = mPasswordView.getText().toString();


            boolean cancel = false;
            View focusView = null;

            //TODO: hacer validaciones para el formulario Login
        /*
        //Comprobar si el password ingresado no es nulo y es valido
        if (!TextUtils.isEmpty(password)) {
            //Envia el error a la caja de Texto
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        */

            /**Comprobar si el campo para el username esta vacio. */
            if (TextUtils.isEmpty(username)) {
                /**Envia el error a la caja de Texto*/
                usernameView.setError(getString(R.string.error_field_required));
                focusView = usernameView;
                cancel = true;
            }
            /**Comprobar si el UserName Ingresado es valido.*/


            /**Comprobar si hubo un fallo durante el ingreso de datos*/
            if (cancel) {
                focusView.requestFocus();
            } else {
                System.out.println(password);
                if (password.equals(datos.getPassword())) {
                    Intent intMain = new Intent(this, MenuPrincipal.class);
                    intMain.putExtra(DBInfomation.ColumnUser.USERNAME, username);
                    intMain.putExtra(DBInfomation.ColumnUser.NAME, datos.getName());
                    intMain.putExtra(DBInfomation.ColumnUser.EMAIL, datos.getEmail());
                    intMain.putExtra(DBInfomation.ColumnUser.AGE, datos.getAge());
                    intMain.putExtra(DBInfomation.ColumnUser.PHOTO, datos.getPhoto());

                    SharedPreferences.Editor editor = sesionSave.edit();
                    editor.putString("email", datos.getEmail());
                    editor.putString("username", username);
                    editor.putString("nombre", datos.getName());
                    editor.putString("age", datos.getAge());
                    editor.putString("photo", datos.getPhoto());
                    editor.putString("name",datos.getName());
                    editor.commit();

                    startActivity(intMain);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.login_layout), "Error iniciando sesión", Snackbar.LENGTH_LONG)
                            .setAction("action", null).show();
                }
            }
        }else{
            Snackbar.make(findViewById(R.id.login_layout), "Error iniciando sesión", Snackbar.LENGTH_LONG)
                    .setAction("action", null).show();
        }
    }


    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }


}

