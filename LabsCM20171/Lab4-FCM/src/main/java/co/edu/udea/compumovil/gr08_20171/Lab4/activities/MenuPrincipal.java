package co.edu.udea.compumovil.gr08_20171.Lab4.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import co.edu.udea.compumovil.gr08_20171.Lab4.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab4.R;
import co.edu.udea.compumovil.gr08_20171.Lab4.Volley.MySingleton;
import co.edu.udea.compumovil.gr08_20171.Lab4.fragments.FragmentConfiguracion;
import co.edu.udea.compumovil.gr08_20171.Lab4.fragments.FragmentEditPerfil;
import co.edu.udea.compumovil.gr08_20171.Lab4.fragments.FragmentEvents;
import co.edu.udea.compumovil.gr08_20171.Lab4.fragments.FragmentNuevoEvento;
import co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab4.fragments.FragmentAbout;
import co.edu.udea.compumovil.gr08_20171.Lab4.fragments.FragmentPerfil;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */
public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentConfiguracion.OnFragmentInteractionListener, FragmentNuevoEvento.crearEventoInterfaz{

    private Fragment fragmentEvents;
    public Fragment fragmentoGenerico;
    public FragmentManager fragmentManager;
    public Intent logInIntent;
    private SharedPreferences sesionSave;
    private SharedPreferences.Editor editor;
    private Fragment fragmentNewEvent;
    private TextView nameUserHeader;
    private TextView emailUserHeader;
    private ImageView photoUserHeader;
    private FloatingActionButton fab;
    public DbHelper dbHelper;
    private SQLiteDatabase db;
    private GoogleApiClient mGoogleApiClient;
    private String name;
    private String email;
    private String photoUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_completo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        logInIntent = getIntent();

        fragmentEvents = new FragmentEvents();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                //fab.setVisibility(View.GONE);
                fragmentManager = getSupportFragmentManager();
                fragmentNewEvent = new FragmentNuevoEvento();

                if (fragmentNewEvent != null) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.content_menu_principal, fragmentNewEvent)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

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


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            name = bundle.getString(DBInfomation.ColumnUser.USERNAME);
            email = bundle.getString(DBInfomation.ColumnUser.EMAIL);
            photoUrl = bundle.getString(DBInfomation.ColumnUser.PHOTO);

            nameUserHeader.setText(name);
            emailUserHeader.setText(email);

            Log.d("Name", "signed_in:" + name);
            Log.d("email", "signed_in:" + email);
            Log.d("Cargandoimagen", "signed_in:" + photoUrl);
            LoadedImageFromUrl(photoUrl);
        }

        if (findViewById(R.id.content_menu_principal) != null) {
            Fragment fragmentEvents = new FragmentEvents();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_menu_principal, fragmentEvents);
            transaction.commit();

        }
    }

    private void LoadedImageFromUrl(String photoUrl) {
        Picasso.with(getApplicationContext()).load(photoUrl).into(photoUserHeader);
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        fragmentoGenerico = null;
        fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_perfil) {
            //fab.setVisibility(View.INVISIBLE);
            //fab.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putString(DBInfomation.ColumnUser.NAME, logInIntent.getExtras().getString(DBInfomation.ColumnUser.NAME));
            bundle.putString(DBInfomation.ColumnUser.USERNAME, name);
            bundle.putString(DBInfomation.ColumnUser.EMAIL, email);
            bundle.putString(DBInfomation.ColumnUser.AGE, logInIntent.getExtras().getString(DBInfomation.ColumnUser.AGE));
            bundle.putString(DBInfomation.ColumnUser.PHOTO, photoUrl);
            fragmentoGenerico = new FragmentPerfil();
            fragmentoGenerico.setArguments(bundle);
        } else {
            if (id == R.id.item_eventos) {
                //fab.setVisibility(View.VISIBLE);
                fragmentoGenerico = new FragmentEvents();
            } else {
                if (id == R.id.item_configuracion) {
                    //fab.setVisibility(View.INVISIBLE);
                    //fab.setVisibility(View.GONE);
                    fragmentoGenerico = new FragmentConfiguracion();
                } else if (id == R.id.item_cerrar_sesion) {
                    sesionSave = getSharedPreferences("sesion", 0);
                    editor = sesionSave.edit();
                    editor.clear().commit();

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.getInstance().signOut();

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()){
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                Intent intLogin = new Intent(getApplicationContext(), Login.class);
                                startActivity(intLogin);
                            }else {
                                Toast.makeText(getApplicationContext(),"No se pudo cerrar la sesion",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                } else if (id == R.id.item_acerca_de) {
                    //fab.setVisibility(View.INVISIBLE);
                    //fab.setVisibility(View.GONE);
                    fragmentoGenerico = new FragmentAbout();
                }
            }
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_menu_principal, fragmentoGenerico)
                    .addToBackStack(null)
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void actualizarEventos() {
        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {

                String urlContar = "http://" + IpDispositivoServicio.ip + ":3000/api/events/count";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                dbHelper = new DbHelper(getApplicationContext());
                db = dbHelper.getWritableDatabase();
                List<Event> eventos = dbHelper.getAllEvents();

                final int numEventosDB = eventos.size();

                Log.d("Actualizar Eventos", "Se agregaran nuevos eventos a DB");
                String urlAllEvents = "http://" + IpDispositivoServicio.ip + ":3000/api/events";

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlAllEvents, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JsonParser jsonParser = new JsonParser();
                        JsonArray jsonArray = (JsonArray) jsonParser.parse(response.toString());

                        Gson gson;
                        gson = new Gson();
                        Event[] events = gson.fromJson(jsonArray, Event[].class);

                        if (events != null) {
                            for (int i = numEventosDB; i < events.length; i++) {
                                dbHelper.addSportEvent(events[i]);
                                Log.d("Actualizar Eventos", "Se ha agregado un evento a la DB local");
                            }


                        } else
                            Log.d("Actualizar Eventos", "eventos es nulo");

                        fragmentoGenerico = new FragmentEvents();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.content_menu_principal, fragmentoGenerico)
                                .addToBackStack(null)
                                .commit();



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Actualizar Eventos", "Error: " + error.toString());
                    }
                });
                queue.add(jsonArrayRequest);
            }
        });
        hilo.start();
    }

    @Override
    public void llamadoAEditarPerfil() {
        Log.d("Configuracion", "Llamando a editar perfil");
        //fab.setVisibility(View.INVISIBLE);
        Bundle bundle = new Bundle();
        bundle.putString(DBInfomation.ColumnUser.NAME, logInIntent.getExtras().getString(DBInfomation.ColumnUser.NAME));
        bundle.putString(DBInfomation.ColumnUser.USERNAME, logInIntent.getExtras().getString(DBInfomation.ColumnUser.USERNAME));
        bundle.putString(DBInfomation.ColumnUser.EMAIL, logInIntent.getExtras().getString(DBInfomation.ColumnUser.EMAIL));
        bundle.putString(DBInfomation.ColumnUser.AGE, logInIntent.getExtras().getString(DBInfomation.ColumnUser.AGE));
        bundle.putString(DBInfomation.ColumnUser.PHOTO, logInIntent.getExtras().getString(DBInfomation.ColumnUser.PHOTO));

        Fragment f = new FragmentEditPerfil();
        f.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_menu_principal, f)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void crearEventoInteraccion() {
        fragmentoGenerico = new FragmentEvents();
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_menu_principal, fragmentoGenerico)
                .addToBackStack(null)
                .commit();
    }
}

