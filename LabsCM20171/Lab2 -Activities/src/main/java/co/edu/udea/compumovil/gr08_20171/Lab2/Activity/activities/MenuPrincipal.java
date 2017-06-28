package co.edu.udea.compumovil.gr08_20171.Lab2.Activity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.Models.dao.DBInfomation;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.adapters.EventViewHolder;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments.FragmentAbout;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments.FragmentEventDetail;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments.FragmentEvents;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments.FragmentNuevoEvento;
import co.edu.udea.compumovil.gr08_20171.Lab2.Activity.fragments.FragmentPerfil;
import co.edu.udea.compumovil.gr08_20171.Lab2.R;

public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragmentEvents;
    private Fragment fragmentEventDetail;
    public Fragment fragmentoGenerico;
    public FragmentManager fragmentManager;
    public Intent logInIntent;
    private SharedPreferences sesionSave;
    private SharedPreferences.Editor editor;
    private Fragment fragmentNewEvent;

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
                fab.setVisibility(View.GONE);
                fragmentManager = getSupportFragmentManager();
                fragmentNewEvent = new FragmentNuevoEvento();

                if (fragmentNewEvent != null) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.content_menu_principal, fragmentNewEvent)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_principal_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_busqueda) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        fragmentoGenerico = null;
        fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_perfil) {
            Bundle bundle = new Bundle();
            bundle.putString(DBInfomation.ColumnUser.USERNAME, logInIntent.getExtras().getString(DBInfomation.ColumnUser.USERNAME));
            bundle.putString(DBInfomation.ColumnUser.EMAIL, logInIntent.getExtras().getString(DBInfomation.ColumnUser.EMAIL));
            fragmentoGenerico = new FragmentPerfil();
            fragmentoGenerico.setArguments(bundle);
            if (logInIntent != null) {
                Log.d("Profile", logInIntent.getExtras().getString(DBInfomation.ColumnUser.USERNAME));
                Log.d("Profile", logInIntent.getExtras().getString(DBInfomation.ColumnUser.EMAIL));
            }

        } else if (id == R.id.item_eventos) {
            fragmentoGenerico = new FragmentEvents();
        } else if (id == R.id.item_configuracion) {

        } else if (id == R.id.item_cerrar_sesion) {
                sesionSave  = getSharedPreferences("sesion",0);
                editor = sesionSave.edit();
                editor.clear().commit();
                Intent intLogin = new Intent(this, Login.class);
                startActivity(intLogin);
                finish();
        } else if (id == R.id.item_acerca_de) {
            fragmentoGenerico = new FragmentAbout();
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_menu_principal, fragmentoGenerico)
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

//TODO: Refactorizar
   /* protected void showEventsFragment() {
        FragmentEventDetail fragmentEventDetail = (FragmentEventDetail) getSupportFragmentManager().findFragmentByTag("fragmentEventDetail");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragmentEvents.isAdded()) {
            fragmentTransaction.show(fragmentEvents);
        } else {
            fragmentTransaction.add(R.id.content_menu_principal, fragmentEvents,"fragmentEvents");
        }
        if (fragmentEventDetail.isAdded()) {
            fragmentTransaction.hide(fragmentEventDetail);
        }
        fragmentTransaction.commit();
    }

    protected void showEventsDetailFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentEventDetail.isAdded()) {
            fragmentTransaction.show(fragmentEventDetail);
            //TODO: llamar al metodo update para cambiar la informacion.
        } else {
            fragmentTransaction.add(R.id.content_menu_principal, fragmentEventDetail,"fragmentEventDetail");
        }
        if (fragmentEvents.isAdded()) {
            fragmentTransaction.hide(fragmentEvents);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onEventSelected(int position) {
        Log.d("TAG","presionado");
        FragmentEventDetail fragmentEventDetail = (FragmentEventDetail) getSupportFragmentManager().findFragmentByTag("fragmentEventDetail");
        FragmentEvents fragmentEvents = (FragmentEvents) getSupportFragmentManager().findFragmentByTag("fragmentEvents");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentEventDetail != null) {
            fragmentEventDetail.updateDetailEventInformation(position);
            transaction.show(fragmentEventDetail);
        } else {

            FragmentEventDetail newFragmentEventDetail = new FragmentEventDetail();
            Bundle bundleArguments = new Bundle();
            bundleArguments.putInt(FragmentEventDetail.ARG_POSITION, position);
            newFragmentEventDetail.setArguments(bundleArguments);

            transaction.replace(R.id.content_menu_principal, newFragmentEventDetail,"fragmentEventDetail");
            transaction.addToBackStack(null);

        }
        if (fragmentEvents != null && fragmentEvents.isAdded()) {
            transaction.hide(fragmentEvents);
        }
        transaction.commit();
    }*/

