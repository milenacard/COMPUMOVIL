package co.edu.udea.compumovil.gr08_20171.Lab4.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;

import static co.edu.udea.compumovil.gr08_20171.Lab4.R.string.email;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class Login extends AppCompatActivity {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    //private final String TAG = "VolleyGSON";
    private ArrayList<String> result;

    // UI references.
    private AutoCompleteTextView usernameView;
    private EditText mPasswordView;
    private Button buttonRegister;
    private Button signInButton;
    private SignInButton sinInGoogleButton;

    private SharedPreferences sesionSave;
    private final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private final String TAG = "LOGIN_ACTIVITY";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

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

        myRef = database.getReference().child("Usuarios") ;

        sesionSave = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        Log.i("save", sesionSave.getString("email", ""));

        usernameView = (AutoCompleteTextView) findViewById(R.id.username_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);
        result = new ArrayList<String>();

        final Context context = usernameView.getContext();

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sinInGoogleButton = (SignInButton) findViewById(R.id.login_google_button);
        sinInGoogleButton.setSize(SignInButton.SIZE_WIDE);
        sinInGoogleButton.setColorScheme(SignInButton.COLOR_DARK);

        sinInGoogleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userGoogle = firebaseAuth.getCurrentUser();

                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intMain = new Intent(Login.this, MenuPrincipal.class);
                    intMain.putExtra(DBInfomation.ColumnUser.USERNAME, userGoogle.getDisplayName());
                    intMain.putExtra(DBInfomation.ColumnUser.EMAIL, userGoogle.getEmail());

                    if (userGoogle.getPhotoUrl() != null) {
                        intMain.putExtra(DBInfomation.ColumnUser.PHOTO, userGoogle.getPhotoUrl().toString());
                    }
                    startActivity(intMain);
                    finish();

                    Log.d(TAG, "signed_in:" + userGoogle.getPhotoUrl());
                    Log.d(TAG, "signed_in:" + userGoogle.getDisplayName());
                    //Toast.makeText(getApplicationContext(), "usuario " + userGoogle.getPhotoUrl(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + firebaseAuth.getCurrentUser().getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out:");

                }
            }
        };

        signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = mPasswordView.getText().toString();
                String email = usernameView.getText().toString();
                singIn(email, pass);

            }
        });


        buttonRegister = (Button) findViewById(R.id.register_button);

        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intRegisterLogin = new Intent(getApplicationContext(), Register.class);
                startActivity(intRegisterLogin);

            }
        });

        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo("co.edu.udea.compumovil.gr08_20171.Lab4", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        }catch (NoSuchAlgorithmException e) {

        }*/
    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void singIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    public void registrarEmail(final GoogleSignInAccount usuario){
        Log.d("LOGIN", "Se revisara si el email ya existe");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean emailExiste = false;

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String emailDB = (String) singleSnapshot.child("email").getValue();
                    if (emailDB.equals(usuario.getEmail())){
                        emailExiste = true;
                    }
                }

                if (!emailExiste){
                    Log.d("LOGIN", "El email no existe, se va a registrar");
                    Random rand = new Random();
                    rand.nextInt(9999999);

                    user nuevoUsuario = new user("Por definir", String.valueOf(rand.nextInt(9999999)),
                            usuario.getDisplayName(), "Por definir", usuario.getPhotoUrl().toString(), usuario.getEmail());
                    Calendar cal = new GregorianCalendar();
                    myRef.child(String.valueOf(cal.getTimeInMillis())).setValue(nuevoUsuario);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            registrarEmail(acct);
                            startActivity(new Intent(Login.this, MenuPrincipal.class));
                            finish();
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
            // intMain.putExtra(DBInfomation.ColumnUser.PHOTO, sesionSave.getString("photo", ""));
            intMain.putExtra(DBInfomation.ColumnUser.NAME, sesionSave.getString("name", ""));
            startActivity(intMain);
            finish();
        }
    }

    //TODO: Hacer refactoring

    private void sendRequest(final String URL) {
        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {
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
                    //intMain.putExtra(DBInfomation.ColumnUser.PHOTO, datos.getPhoto());

                    SharedPreferences.Editor editor = sesionSave.edit();
                    editor.putString("email", datos.getEmail());
                    editor.putString("username", username);
                    editor.putString("nombre", datos.getName());
                    editor.putString("age", datos.getAge());
                    // editor.putString("photo", datos.getPhoto());
                    editor.putString("name", datos.getName());
                    editor.commit();

                    startActivity(intMain);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.login_layout), "Error iniciando sesión", Snackbar.LENGTH_LONG)
                            .setAction("action", null).show();
                }
            }
        } else {
            Snackbar.make(findViewById(R.id.login_layout), "Error iniciando sesión", Snackbar.LENGTH_LONG)
                    .setAction("action", null).show();
        }
    }


    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }


}

