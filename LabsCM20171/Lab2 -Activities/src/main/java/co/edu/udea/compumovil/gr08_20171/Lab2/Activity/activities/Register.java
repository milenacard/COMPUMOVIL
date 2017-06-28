package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab2.R;

public class Register extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;

    public String username, password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Register");

        mUsername = (EditText) findViewById(R.id.username_register);
        mPassword = (EditText) findViewById(R.id.password_register);
        mEmail = (EditText) findViewById(R.id.email_register);
    }

    //Guarda la informacion en la base de datos
    public void saveInfo(View v) {
        mUsername.setError(null);
        mPassword.setError(null);
        mEmail.setError(null);
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        email = mEmail.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;

        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            DbHelper dbHelper = new DbHelper(this); //Instancia de DbHelper
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Obtener instancia de la BD
            if(dbHelper.registerQuery(db,username,email,password)){
                finish();//Se guarda la fila en la base de datos
            }else{
                Snackbar.make(findViewById(R.id.login_layout), "Error registrando el usuario", Snackbar.LENGTH_LONG)
                        .setAction("action", null).show();
            }


        }


    }
}
