package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments.FragmentAcercaDe;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments.FragmentChat;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments.FragmentLibreriaPublica;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments.FragmentLibrosDeInteres;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Fragments.FragmentMisLibros;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Login;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Token;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo.Usuario;
import co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.R;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentLibreriaPublica.OnFragmentInteractionListener, FirebaseAuth.AuthStateListener {


    public Fragment fragmentoGenerico;
    public FragmentManager fragmentManager;
    boolean isListview;
    boolean mostrarBoton = true;
    private TextView nameUserHeader;
    private TextView emailUserHeader;
    private ImageView photoUserHeader;
    private String name;
    private String email;
    private String photoUrl;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sesionSave;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public Usuario user;

    public String fragmentActual = "libreria publica";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nd);
        setTitle("B-Trade");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (Usuario) getIntent().getSerializableExtra("usuario");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        nameUserHeader = (TextView) header.findViewById(R.id.nameUser_header);
        emailUserHeader = (TextView) header.findViewById(R.id.emailUser_header);
        photoUserHeader = (ImageView) header.findViewById(R.id.imageUser_header);

        FirebaseDatabase.getInstance().getReference("Usuarios/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //snapshot.getChildren();
                System.out.println(snapshot);
                name = (String)snapshot.child("nombre").getValue();
                email = (String) snapshot.child("email").getValue();
                photoUrl = (String) snapshot.child("foto").getValue();
                nameUserHeader.setText(name);
                emailUserHeader.setText(email);
                Glide.with(getApplicationContext()).load(photoUrl).apply(RequestOptions.fitCenterTransform()).into(photoUserHeader);


                snapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));

        isListview = true;


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        /*
        if (bundle != null) {
            name = bundle.getString("Name");
            email = bundle.getString("Email");
            photoUrl = bundle.getString("Photo");

            nameUserHeader.setText(name);
            emailUserHeader.setText(email);
            Glide.with(getApplicationContext()).load(photoUrl).apply(RequestOptions.fitCenterTransform()).into(photoUserHeader);

            Log.d("Name", "signed_in:" + name);
            Log.d("email", "signed_in:" + email);
            Log.d("Cargandoimagen", "signed_in:" + photoUrl);
        }

*/
        initGoogleAccount();


        if (findViewById(R.id.contentND) != null && fragmentActual.equals("libreria publica")) {
            Fragment fragment = new FragmentLibreriaPublica();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentND, fragment);
            transaction.commit();

        }

    }



    private void initGoogleAccount() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        nameUserHeader.setText(name);
        emailUserHeader.setText(email);
        Glide.with(getApplicationContext()).load(photoUrl).apply(RequestOptions.fitCenterTransform()).into(photoUserHeader);

        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.d("Menu Principal", "onAuthStateChanged:signed_in:" + currentUser.getUid());
            Log.d("Menu Principal", "onAuthStateChanged:PhotoUrl:" + currentUser.getPhotoUrl());
            nameUserHeader.setText(currentUser.getDisplayName());
            emailUserHeader.setText(currentUser.getEmail());
            Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl()).apply(RequestOptions.fitCenterTransform()).into(photoUserHeader);


            if (fragmentActual.equals("libreria publica")) {
                fragmentoGenerico = new FragmentLibreriaPublica();

                Bundle mbundle = new Bundle();
                mbundle.putString("email", email);
                System.out.println("EMAIL: " + email);
                fragmentoGenerico.setArguments(mbundle);

                fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_nd, fragmentoGenerico)
                        .commit();
            }


        } else {
            // User is signed out
            Log.d("Menu Principal", "onAuthStateChanged:signed_out");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nd, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mostrarBoton) {
            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btn_cambiar_modo) {
            if (fragmentActual.equals("mis libros")) {
                FragmentMisLibros frag = (FragmentMisLibros) getSupportFragmentManager().findFragmentById(R.id.content_nd);
                isListview = frag.cambiarModo(isListview, item);
            } else if (fragmentActual.equals("libros de interes")) {
                FragmentLibrosDeInteres frag = (FragmentLibrosDeInteres) getSupportFragmentManager().findFragmentById(R.id.content_nd);
                isListview = frag.cambiarModo(isListview, item);
            } else {
                FragmentLibreriaPublica frag = (FragmentLibreriaPublica) getSupportFragmentManager().findFragmentById(R.id.content_nd);
                isListview = frag.cambiarModo(isListview, item);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        fragmentoGenerico = null;
        fragmentManager = getSupportFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_mislibros) {

            mostrarBoton = true;
            this.invalidateOptionsMenu();
            isListview = true;
            fragmentoGenerico = new FragmentMisLibros();
            fragmentActual = "mis libros";

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragmentoGenerico.setArguments(bundle);

        } else if (id == R.id.nav_librosinteres) {
            mostrarBoton = true;
            this.invalidateOptionsMenu();
            isListview = true;
            fragmentoGenerico = new FragmentLibrosDeInteres();
            fragmentActual = "libros de interes";

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragmentoGenerico.setArguments(bundle);
        } else if (id == R.id.nav_chat) {
            mostrarBoton = false;
            this.invalidateOptionsMenu();
            fragmentoGenerico = new FragmentChat();
            fragmentActual = "ChatActivity";

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragmentoGenerico.setArguments(bundle);
        } else if (id == R.id.nav_libreriaPublica) {
            mostrarBoton = true;
            isListview = true;
            this.invalidateOptionsMenu();
            fragmentoGenerico = new FragmentLibreriaPublica();
            fragmentActual = "libreria publica";

            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            fragmentoGenerico.setArguments(bundle);
        } else if (id == R.id.nav_about) {
            mostrarBoton = false;
            this.invalidateOptionsMenu();
            fragmentoGenerico = new FragmentAcercaDe();
        } else if (id == R.id.nav_cerrarsesion) {
            sesionSave = getSharedPreferences("sesion", 0);
            editor = sesionSave.edit();
            editor.clear().commit();

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.getInstance().signOut();

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                        Intent intLogin = new Intent(getApplicationContext(), Login.class);
                        startActivity(intLogin);
                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo cerrar la sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_nd, fragmentoGenerico)
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
