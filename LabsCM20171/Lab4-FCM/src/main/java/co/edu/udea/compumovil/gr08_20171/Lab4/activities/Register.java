package co.edu.udea.compumovil.gr08_20171.Lab4.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import co.edu.udea.compumovil.gr08_20171.Lab4.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;

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
    private final String TAG = "LOGIN-REGISTER_ACTIVITY";
    private FirebaseAuth mAuth;
    //String urlPost;
    user nuevoUsuario;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

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

        mAuth = FirebaseAuth.getInstance();

        myRef = database.getReference().child("Usuarios") ;

        buttonSaveRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo(view);
            }
        });


    }

    public void createAccount(String myemail, String mypassword) {
        mAuth.createUserWithEmailAndPassword(myemail, mypassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Register failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
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



            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean emailExiste;
                    emailExiste = false;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        String emailDB = (String) singleSnapshot.child("email").getValue();
                        if (emailDB.equals(email)){
                            emailExiste = true;
                        }
                    }

                    if (!emailExiste){

                        Log.d("REGISTRO", "El email ya está registrado");

                        nuevoUsuario = new user(username, password, name, age, "/Usuarios/deafult.png", email);
                        Calendar cal = new GregorianCalendar();
                        myRef.child(String.valueOf(cal.getTimeInMillis())).setValue(nuevoUsuario);
                        createAccount(email, password);

                        Intent intentRegister = new Intent(Register.this, MenuPrincipal.class);
                        intentRegister.putExtra(DBInfomation.ColumnUser.USERNAME, username);
                        intentRegister.putExtra(DBInfomation.ColumnUser.NAME, name);
                        intentRegister.putExtra(DBInfomation.ColumnUser.EMAIL, email);
                        intentRegister.putExtra(DBInfomation.ColumnUser.AGE, age);
                        startActivity(intentRegister);
                        finish();
                    } else {
                        Toast.makeText(Register.this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




            //sendRequest(username, password, name, email, age);
        }
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
