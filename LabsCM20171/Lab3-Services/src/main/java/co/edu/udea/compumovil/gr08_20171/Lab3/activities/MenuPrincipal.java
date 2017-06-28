package co.edu.udea.compumovil.gr08_20171.Lab3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import java.util.List;
import co.edu.udea.compumovil.gr08_20171.Lab3.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;
import co.edu.udea.compumovil.gr08_20171.Lab3.fragments.FragmentConfiguracion;
import co.edu.udea.compumovil.gr08_20171.Lab3.fragments.FragmentEditPerfil;
import co.edu.udea.compumovil.gr08_20171.Lab3.fragments.FragmentEvents;
import co.edu.udea.compumovil.gr08_20171.Lab3.fragments.FragmentNuevoEvento;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab3.fragments.FragmentAbout;
import co.edu.udea.compumovil.gr08_20171.Lab3.fragments.FragmentPerfil;

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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

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
            String name = bundle.getString("username");
            String email = bundle.getString("email");
            String foto = bundle.getString("photo");

            nameUserHeader.setText(name);
            emailUserHeader.setText(email);
            //photoUserHeader.getImageMatrix(foto);
        }
        if (findViewById(R.id.content_menu_principal) != null) {

            Fragment fragmentEvents = new FragmentEvents();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_menu_principal, fragmentEvents);
            transaction.commit();

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
            bundle.putString(DBInfomation.ColumnUser.USERNAME, logInIntent.getExtras().getString(DBInfomation.ColumnUser.USERNAME));
            bundle.putString(DBInfomation.ColumnUser.EMAIL, logInIntent.getExtras().getString(DBInfomation.ColumnUser.EMAIL));
            bundle.putString(DBInfomation.ColumnUser.AGE, logInIntent.getExtras().getString(DBInfomation.ColumnUser.AGE));
            bundle.putString(DBInfomation.ColumnUser.PHOTO, logInIntent.getExtras().getString(DBInfomation.ColumnUser.PHOTO));
            fragmentoGenerico = new FragmentPerfil();
            fragmentoGenerico.setArguments(bundle);
        } else {
            if (id == R.id.item_eventos) {
                //fab.setVisibility(View.VISIBLE);
                fragmentoGenerico = new FragmentEvents();
            } else {
                if (id == R.id.item_actualizar) {
                    actualizarEventos();
                } else if (id == R.id.item_configuracion) {
                    //fab.setVisibility(View.INVISIBLE);
                    //fab.setVisibility(View.GONE);
                    fragmentoGenerico = new FragmentConfiguracion();
                } else if (id == R.id.item_cerrar_sesion) {
                    sesionSave = getSharedPreferences("sesion", 0);
                    editor = sesionSave.edit();
                    editor.clear().commit();
                    Intent intLogin = new Intent(this, Login.class);
                    startActivity(intLogin);
                    finish();
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
        fm.beginTransaction().replace(R.id.content_menu_principal, f).commit();

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

