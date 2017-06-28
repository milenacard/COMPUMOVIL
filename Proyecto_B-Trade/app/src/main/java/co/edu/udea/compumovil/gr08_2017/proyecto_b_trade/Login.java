package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities.MenuPrincipal;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Token;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Usuario;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mAuth;
    private final int RC_SIGN_IN = 45;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sesionSave;
    private String nombreUsuario;
    private String correoUsuario;
    private String fotoUsuario;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private String email;
    private String password;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.login_google_button)
    SignInButton _loginButtonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //TODO: Hacer el login con usuario y contraseña
        // _emailText.setText("email@algo.com");
        // _passwordText.setText("123456");

        sesionSave = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        Log.i("save", sesionSave.getString("email", ""));

        _loginButtonGoogle.setSize(SignInButton.SIZE_WIDE);
        _loginButtonGoogle.setColorScheme(SignInButton.COLOR_DARK);

        myRef = database.getReference().child("Usuarios");

        initGoogleAccount();
    }

    @OnClick(R.id.btn_login)
    public void login(View view) {
        Log.d(TAG, "Login");
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!validateForm()) {
            return;
        }
        //showProgressDialog();

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verificando Cuenta...");
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

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startMainActivity(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Log.w(TAG, "signInWithEmail:failure" + task.getException().getMessage());
                            //Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog1 = new ProgressDialog(Login.this, R.style.AppTheme);
        progressDialog1.setIndeterminate(true);
        progressDialog1.setMessage("Autenticando...");
        progressDialog1.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog1.dismiss();
                    }
                }, 1000);

    }

    public void onSignupSuccess() {
        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            _emailText.setError("Required.");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        String password = _passwordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            _passwordText.setError("Required.");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    @OnClick(R.id.link_signup)
    public void signup(View view) {
        Log.d(TAG, "SignUp");
        Intent intent = new Intent(getApplicationContext(), SingUp.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @OnClick(R.id.login_google_button)
    public void loginGoogle(View view) {
        Log.d(TAG, "LoginGoogle");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
//        progressDialog.show();

    }

    private void initGoogleAccount() {
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        startMainActivity(currentUser);

    }

    private void startMainActivity(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(this, MenuPrincipal.class);
            /*intent.putExtra("Email", user.getEmail());
            intent.putExtra("Name", user.getDisplayName());
            if (user.getPhotoUrl() == null) {
                intent.putExtra("Photo", "https://firebasestorage.googleapis.com/v0/b/b-trade-bde54.appspot.com/o/usuarios%2Fdeafult.png?alt=media&token=7f74c6b2-def7-464e-a4e3-c6c2a131c1a4");
            } else {
                intent.putExtra("Photo", user.getPhotoUrl());
            }*/
            guardarToken(user.getEmail());
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }

        Log.d(TAG, "onActivityResult");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Log.d(TAG, "Google Sign In success");
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                //updateUI(null);
                Log.d(TAG, "Google Sign In failed");
                // [END_EXCLUDE]
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser userGoogle = mAuth.getCurrentUser();
                            startMainActivity(userGoogle);


                            if (userGoogle != null) {


                                guardarToken(userGoogle.getEmail());

                                Usuario user = new Usuario();
                                user.setNombre(userGoogle.getDisplayName());
                                user.setEmail(userGoogle.getEmail());
                                user.setFoto(userGoogle.getPhotoUrl().toString());

                                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                Intent intMain = new Intent(Login.this, MenuPrincipal.class);
                                intMain.putExtra("usuario", user);

                                SharedPreferences.Editor editor = sesionSave.edit();
                                editor.putString("Name", user.getNombre());
                                editor.putString("Email", user.getEmail());
                                editor.putString("Photo", user.getFoto());
                                editor.commit();
                                startActivity(intMain);
                                finish();

                                //Envía el usuario al main activity

                                Log.d(TAG, "signed_in:" + userGoogle.getPhotoUrl());
                                Log.d(TAG, "signed_in:" + userGoogle.getDisplayName());
                                Log.d(TAG, "onAuthStateChanged: signed_in:" + mAuth.getCurrentUser().getUid());
                            } else {
                                Log.d(TAG, "onAuthStateChanged: signed_out:");

                            }

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sesionSave.getString("email", "").equals("")) {
            Intent intMain = new Intent(this, MenuPrincipal.class);
            intMain.putExtra("Name", sesionSave.getString("Name", ""));
            intMain.putExtra("Email", sesionSave.getString("Email", ""));
            startActivity(intMain);
            finish();
        }
    }

    public void onLoginSuccess() {

        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }

    private void subirUsuario(final Usuario user) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean emailExiste = false;
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String emailDB = (String) singleSnapshot.child("email").getValue();
                    if (emailDB.equals(user.getEmail())) {
                        emailExiste = true;
                    }
                }
                if (!emailExiste) {
                    //Calendar cal = new GregorianCalendar();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void guardarToken(final String email) {
        final String token = FirebaseInstanceId.getInstance().getToken();

        final DatabaseReference tokenRef;
        tokenRef = database.getReference().child("Tokens");
        final String[] key = new String[1];


        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean emailExiste = false;
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String emailDB = (String) singleSnapshot.child("email").getValue();
                    if (emailDB.equals(email)) {
                        emailExiste = true;
                        key[0] = singleSnapshot.getKey();
                    }
                }

                if (emailExiste) {
                    tokenRef.child(key[0]).child("token").setValue(token);
                } else {
                    Token t = new Token(token, email);
                    Calendar cal = new GregorianCalendar();
                    tokenRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(t);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
