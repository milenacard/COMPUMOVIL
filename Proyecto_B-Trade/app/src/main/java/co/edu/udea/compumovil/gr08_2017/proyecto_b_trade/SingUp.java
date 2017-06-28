package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.MenuPrincipal;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Usuario;

import static android.R.string.cancel;

public class SingUp extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOGIN = 0;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String password;
    private String reEnterPassword;

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference().child("Usuarios") ;
    }

    @OnClick(R.id.btn_signup)
    public void signup(View view){
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SingUp.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
//                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @OnClick(R.id.link_login)
    public void goLogin(View view){
        Log.d(TAG, "GoLogin");
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivityForResult(intent, REQUEST_LOGIN);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }



    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

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


                    Calendar cal = new GregorianCalendar();

                    createAccount(email, password);



                } else {
                    Toast.makeText(SingUp.this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return valid;
    }

    public void createAccount(String myemail, String mypassword) {
        mAuth.createUserWithEmailAndPassword(myemail, mypassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Usuario nuevoUsuario = new Usuario(name, "https://firebasestorage.googleapis.com/v0/b/b-trade-bde54.appspot.com/o/usuarios%2Fdeafult.png?alt=media&token=7f74c6b2-def7-464e-a4e3-c6c2a131c1a4",  email, password);
                            myRef.child(user.getUid()).setValue(nuevoUsuario);

                            Intent intentRegister = new Intent(SingUp.this, MenuPrincipal.class);
                            intentRegister.putExtra("Name", name);
                            intentRegister.putExtra("Email", email);
                            startActivity(intentRegister);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SingUp.this, "Register failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

}
