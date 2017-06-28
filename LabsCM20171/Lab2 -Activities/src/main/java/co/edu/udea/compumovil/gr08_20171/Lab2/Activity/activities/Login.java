package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments.FragmentPerfil;
import co.edu.udea.compumovil.gr08_20171.Lab2.R;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity{

    private  DbHelper dbHelper;
    private  SQLiteDatabase db;
    private String KEY_USERNAME = "Username";

    // UI references.
    private AutoCompleteTextView usernameView;
    private EditText mPasswordView;
    private Button buttonRegister;
    private Button signInButton;

    SharedPreferences sesionSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");

        dbHelper = new DbHelper(this); //Instancia de DbHelper
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
        sesionSave = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        Log.i("save", sesionSave.getString("email",""));

        usernameView = (AutoCompleteTextView) findViewById(R.id.username_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);

          final Context context = usernameView.getContext();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;

                }
                return false;
            }
        });

        signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        buttonRegister = (Button) findViewById(R.id.register_button);

        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intRegister = new Intent(Login.this, Register.class);
                    startActivity(intRegister);
                }catch(Exception e){
                    //Snackbar.make(findViewById(R.id.login_layout), "Try again later.", Snackbar.LENGTH_LONG).setAction("action", null).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!sesionSave.getString("email","").equals("")){
            Intent intMain = new Intent(this, MenuPrincipal.class);
            intMain.putExtra(DBInfomation.ColumnUser.USERNAME,sesionSave.getString("username",""));
            intMain.putExtra(DBInfomation.ColumnUser.EMAIL, sesionSave.getString("email",""));
            startActivity(intMain);
            finish();
        }
    }

    private void attemptLogin() {
        /**Resetea los Errores*/
        usernameView.setError(null);
        mPasswordView.setError(null);

        /**Obtiene y guarda los valores respectivos para el email y el password*/
        String username = usernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        /**
         * Bandera evidenciar algun error durante la validación de los datos
         * Variable para contener el campo a ser enfocado
         */
        boolean cancel = false;
        View focusView = null;

        /**Comprobar si el password ingresado no es nulo y es valido*/
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            /**Envia el error a la caja de Texto*/
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        /**Comprobar si el campo para el username esta vacio. */
        if (TextUtils.isEmpty(username)) {
            /**Envia el error a la caja de Texto*/
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }
        /**Comprobar si el UserName Ingresado es valido.*/
        else if (!isUsernameValid(username)) {
            /**Envia el error a la caja de Texto*/
            usernameView.setError(getString(R.string.error_invalid_email));
            focusView = usernameView;
            cancel = true;
        }

        /**Comprobar si hubo un fallo durante el ingreso de datos*/
        if (cancel) {
            focusView.requestFocus();
        } else {
            String loginResult = dbHelper.loginQuery(db,username,password);
            if(!loginResult.equals(null) || !loginResult.equals("")){
                Log.i("Login.java: Access",dbHelper.loginQuery(db,username,password).toString());
                Intent intMain = new Intent(this, MenuPrincipal.class);
                intMain.putExtra( DBInfomation.ColumnUser.USERNAME, username);
                intMain.putExtra(DBInfomation.ColumnUser.PASSWORD, password);
                intMain.putExtra(DBInfomation.ColumnUser.EMAIL, loginResult);
                SharedPreferences.Editor editor = sesionSave.edit();
                editor.putString("email",loginResult);
                editor.putString("username",username);
                editor.commit();
                startActivity(intMain);
                finish();

            }else{
                Snackbar.make(findViewById(R.id.login_layout), "Error iniciando sesión", Snackbar.LENGTH_LONG)
                        .setAction("action", null).show();
            }
        }

    }


    private boolean isUsernameValid(String username) {
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }




}

